package com.example.slidingmenu.yujing.client.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.activity.broadcast.BroadCastActivity;
import com.example.slidingmenu.yujing.client.activity.controller.ClientActivity;
import com.example.slidingmenu.yujing.client.activity.friends.FriendsActivity;
import com.example.slidingmenu.yujing.client.activity.letter.LetterActivity;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.database.table.Topic;
import com.example.slidingmenu.yujing.client.network.HttpClient;
import com.example.slidingmenu.yujing.client.network.Request;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;


public class MsgService extends Service implements Runnable{	
	
	public static final int NOTICE_REQUEST_INTERVAL = 30 * 1000;

	private ClientApplication clientApplication;
	private String userName;
	private String passWord;
	
	private int[] newNum = new int[]{0,0,0};
	
	public static LinkedList<Activity> acList = new LinkedList<Activity>();
	
	public static final int TOPIC = 0;
	public static final int LETTER = 2;
	public static final int FRIEND = 1;
	
	public static Activity getActivityInList(String name) {
		for(Activity ac : acList) {
			if(ac.getClass().getName().indexOf(name) >= 0) {
				return ac;
			}
		}
		return null;
	}
	
	private MsgRefresh mMsgRefresh;
	
	/**
	 * 进行界面的刷新功能
	 * 
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case TOPIC:
				mMsgRefresh = (MsgRefresh) getActivityInList(BroadCastActivity.class.getSimpleName());
				if (mMsgRefresh != null) {
					mMsgRefresh.refresh(msg.what, "");
				}
				break;
			case LETTER:
				mMsgRefresh = (MsgRefresh) getActivityInList(LetterActivity.class.getSimpleName());
				if (mMsgRefresh != null) {
					mMsgRefresh.refresh(msg.what, "");
					notifyMsg((int)(System.currentTimeMillis()/1000), 
							ClientActivity.class, R.drawable.log_incoming, "新私信", "Book客户端", "您收到新私信");
				}
				break;
			case FRIEND:
				mMsgRefresh = (MsgRefresh) getActivityInList(FriendsActivity.class.getSimpleName());
				if (mMsgRefresh != null) {
					mMsgRefresh.refresh(FRIEND, "");
				}
				break;
			default:
				break;
			}
			mMsgRefresh = (MsgRefresh) getActivityInList(ClientActivity.class.getSimpleName());
			if(mMsgRefresh != null){
				mMsgRefresh.refresh(msg.what, newNum);
			}
			
			super.handleMessage(msg);
		}
	};
	
	@Override
	public void onCreate() {
		clientApplication = (ClientApplication) getApplication();
		userName = clientApplication.getLoginUserInfo().getString(RequestParam.USER_NAME, null);
		passWord = clientApplication.getLoginUserInfo().getString(RequestParam.PASSWORD, null);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		MsgService.setNextRequestTime(clientApplication.getApplicationContext(), 
				MsgService.NOTICE_REQUEST_INTERVAL);
		Thread request = new Thread(this);
		request.start();
		newNum = new int[]{0,0,0};
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}	
	
	public static void setNextRequestTime(Context context, int repeatTime) {
		long currentTime = System.currentTimeMillis();
		addAlarmManager(context).set(AlarmManager.RTC_WAKEUP,
				currentTime + repeatTime, addPendingIntent(context));
	}

	public static AlarmManager addAlarmManager(Context context) {
		AlarmManager mAlarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		return mAlarmManager;
	}

	public static PendingIntent addPendingIntent(Context context) {
		Intent intent = new Intent(context, MsgService.class);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		return pendingIntent;
	}

	public static void cancelNextRequest(Context context) {
		addAlarmManager(context).cancel(addPendingIntent(context));
	}
	
	@Override
	public void onDestroy() {
		MsgService.cancelNextRequest(clientApplication);
		super.onDestroy();
	}

	@Override
	public void run() {
		synchronized (MsgService.class) {
			
			request(RequestParam.GET_PERSON_STATE);
			
			int fcount = request(RequestParam.GET_NEW_FRIENDS);
			if(fcount != -1){
				newNum[FRIEND] = fcount; 
				handler.sendEmptyMessage(MsgService.FRIEND);
			}
				
			int pcount = request(RequestParam.GET_NEW_PRIVATELETTER);
			if(pcount > 0){
				newNum[LETTER] = pcount;
				handler.sendEmptyMessage(MsgService.LETTER);
			}
			
			int tcount = request(RequestParam.GET_NEW_TOPIC);
			if(tcount > 0){
				newNum[TOPIC] = tcount;
				handler.sendEmptyMessage(MsgService.TOPIC);
			}				
		}
	}
	
	private RequestParam getRequestParam(String requestType, String[] params) {
		RequestParam requestParam = new RequestParam();;
		requestParam.setUserName(userName);
		requestParam.setPassword(passWord);
		requestParam.setRandomKey("1234");
		requestParam.setRequestType(requestType);
		requestParam.setParams(params);
		return requestParam;
	}
	
	private int request(String requestType){
		
		String[] params = new String[] {""};
		if (requestType == RequestParam.GET_NEW_TOPIC) {
			int maxId = Topic.getMaxId(clientApplication.getDatabaseHelper());
			if (maxId >= 0) {
				params = new String[] {String.valueOf(maxId)};
			} else {
				return -1;
			}
		} 
		RequestParam requestParam = getRequestParam(requestType, params);
		
		if(!HttpClient.isConnect(clientApplication.getApplicationContext())) {
			return -1;
		}	
		
		String res = Request.request(requestParam.getJSON());
		if ("".equals(res)) {
			return -1;
		}

		int count = 0;
		try {
			ResponseParam rs = new ResponseParam(res);
			if(rs.getResult() != ResponseParam.RESULT_SUCCESS) {
				return -1;
			}
			count = doWork(requestType, res);
		} catch (Exception e1) {
			e1.printStackTrace();
			return -1;
		}
		
		return count;
	}

	/**
	 * @param res
	 * @return
	 * @throws org.json.JSONException
	 */
	private int doWork(String type, String res){
		List<ContentValues> friendList = new ArrayList<ContentValues>();
		NewMessage nm = MsgResponseParamFactory.getMsgResponseParam(type, res);
		friendList = nm.getNewMessage();
		int count = nm.dealNewMessage(friendList, clientApplication.getDatabaseHelper());
		return count;
	}
	
	/**
	 * 接收私信
	 * @param notify_id
	 * @param clazz
	 * @param whatIcon
	 * @param tickerText
	 * @param contentTittle
	 * @param contentText
	 */
	public void notifyMsg(int notify_id, Class<?> clazz, int whatIcon,
			String tickerText, String contentTittle, String contentText) {
		// 通过getSystemService获得NotificationManager对象
		NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// 创建Notification对象
		Notification notification = new Notification();
		// 生成Intent对象，为PendingIntent准备
		Intent intent = new Intent();
		// clazz表示当点击该通知时启动的Activity，同时根据PendingIntent
		// 中的getBroadCast()启动某个广播，通过getService()启动服务。
		intent.setClass(this, clazz);
		intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		int flags = PendingIntent.FLAG_CANCEL_CURRENT;
		// 为Notification对象设置属性
		notification.contentIntent = PendingIntent.getActivity(this, 0, intent,
				flags);
		notification.when = System.currentTimeMillis();
		notification.tickerText = tickerText;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.icon = whatIcon;
		notification.defaults = Notification.DEFAULT_ALL;
		// 设置最新的提示信息
		notification.setLatestEventInfo(this, contentTittle, contentText,
				notification.contentIntent);
		// 将通知发布到通知栏上，notify_id标示了唯一一个Notification对象
		notifyManager.notify(notify_id, notification);
	}


}
