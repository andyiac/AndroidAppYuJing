package com.example.slidingmenu.yujing.client.activity.friends;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.activity.ProfileActivity;
import com.example.slidingmenu.yujing.client.activity.letter.SendLetterActivity;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.database.table.Friend;
import com.example.slidingmenu.yujing.client.network.HttpClient;
import com.example.slidingmenu.yujing.client.network.Request;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;
import com.example.slidingmenu.yujing.client.network.mode.friend.GetPeopleResponseParam;
import com.example.slidingmenu.yujing.client.utils.Utils;


public class AddFriendsActivity extends Activity{

	private static final int SEND_LETTER = 55;
	private ClientApplication clientApplication;
	private ListView friendsList; 
	private List<? extends Map<String, ?>> list;
	private AllPeopleAdapter fAdapter;
	
	private GetAllPeopleTask mGetAllPeopleTask;
	private AddFriendTask mAddFriendTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friends_layout);
		
		clientApplication = (ClientApplication) getApplication();
		
		friendsList = (ListView) findViewById(R.id.friends_list);
		
		// 向服务器申请数据
		RequestParam requestParam = new RequestParam();
		requestParam.setUserName(clientApplication.getLoginUserInfo().getString(RequestParam.USER_NAME, null));
		requestParam.setPassword(clientApplication.getLoginUserInfo().getString(RequestParam.PASSWORD, null));
		requestParam.setRandomKey("1234");
		requestParam.setRequestType(RequestParam.GET_ALL_PEOPLE);
		requestParam.setParams(new String[] { "" });
		mGetAllPeopleTask = new GetAllPeopleTask();
		mGetAllPeopleTask.execute(requestParam);
		//事件监听
		friendsList.setOnItemClickListener(new ClickListView());
		registerForContextMenu(friendsList);		
		
	}	
	
	public void setAdapter(){
		fAdapter = new AllPeopleAdapter(AddFriendsActivity.this, list);
		friendsList.setAdapter(fAdapter);
	}

	@Override
	protected void onDestroy() {
		
		if(mGetAllPeopleTask != null && mGetAllPeopleTask.getStatus() == Status.RUNNING) {
			mGetAllPeopleTask.cancel(true);
			mGetAllPeopleTask = null;
		}
		
		if(mAddFriendTask != null && mAddFriendTask.getStatus() == Status.RUNNING) {
			mAddFriendTask.cancel(true);
			mAddFriendTask = null;
		}
		
		super.onDestroy();
	}
	
	@Override
	public void onCreateContextMenu( ContextMenu menu, View v,
			ContextMenuInfo menuInfo ) {		

		menu.add( 0, 0, 0, "私信" );
		menu.add( 0, 1, 0, "查看" );
		menu.add( 0, 2, 0, "加为好友" );

		super.onCreateContextMenu(menu, v, menuInfo);

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

		ContentValues values = (ContentValues) Utils.hashMapToContentValues((HashMap<String, Object>) fAdapter
                .getItem(menuInfo.position));

		switch (item.getItemId()) {
		// 私信
		case 0:
			Intent intent1 = new Intent(this, SendLetterActivity.class);
			intent1.putExtra("values", values);
			startActivityForResult(intent1, SEND_LETTER);
			break;
		// 查看
		case 1:
			Intent intent2 = new Intent();
			intent2.putExtra("values", values);
			intent2.setClass(AddFriendsActivity.this, ProfileActivity.class);
			startActivity(intent2);
			break;
		// 加为好友
		case 2:
			addFriend(menuInfo.position, values);
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void addFriend(int position,
			ContentValues values) {
		RequestParam requestParam = new RequestParam();
		requestParam.setUserName(clientApplication.getLoginUserInfo().getString(RequestParam.USER_NAME, null));
		requestParam.setPassword(clientApplication.getLoginUserInfo().getString(RequestParam.PASSWORD, null));
		requestParam.setRandomKey("1234");
		requestParam.setRequestType(RequestParam.ADD_FRIENDS);
		requestParam.setParams(new String[] {String.valueOf(list.get(position).get(Friend.ID))});
		mAddFriendTask = new AddFriendTask(this, new AddFriendTask.OnAddFriend() {
			
			@Override
			public void onAddFriendSuccess(Activity activity, int position) {
				list.remove(position);
				fAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void onAddFriendFail(Activity activity, int position) {
				
			}
		});
		mAddFriendTask.execute(requestParam, values, position);
	}

	
	class GetAllPeopleTask extends AsyncTask<RequestParam, Integer, Boolean>{

		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(AddFriendsActivity.this, "", getText(R.string.waiting));
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(RequestParam... param) {
			
			//如果网络没有连接则更新进度为 网络连接异常
			if( !HttpClient.isConnect(AddFriendsActivity.this)) {
				return false;
			}
			
			RequestParam requestParam = param[0];
			String res = Request.request(requestParam.getJSON());
			
			if ("".equals(res)) {
				return false;
			}

			try {
				GetPeopleResponseParam response = new GetPeopleResponseParam(res);
				if (response.getResult() != GetPeopleResponseParam.RESULT_SUCCESS) {
					return false;
				}							
				list = response.getAllPeople();
				return true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				setAdapter();
			}
			dialog.dismiss();
			dialog = null;
			super.onPostExecute(result);
		}
	}
	
	
	
	private class ClickListView implements OnItemClickListener{

		@SuppressWarnings("unchecked")
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {
			Intent intent = new Intent();
			intent.putExtra("values", Utils.hashMapToContentValues(
					(HashMap<String, Object>)fAdapter.getItem(position)));
			intent.setClass(AddFriendsActivity.this, ProfileActivity.class);
			startActivity(intent);
		}
	}
	
/*	class AddFriendTask extends AsyncTask<Object, Integer, Boolean>{

		private ProgressDialog dialog;		
		private int position = -1;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(AddFriendsActivity.this, "", getText(R.string.waiting));
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Object... param) {

			//如果网络没有连接则更新进度为 网络连接异常
			if(!HttpClient.isConnect(AddFriendsActivity.this)) {
				return false;
			}			

			RequestParam requestParam = (RequestParam) param[0];
			ContentValues values = (ContentValues)param[1];
			position = (Integer) param[2];
			
			String res = Request.request(requestParam.getJSON());

			// 如果请求结果为空字符串，则请求失败
			if ("".equals(res)) {
				return false;
			}
			
			try {
				ResponseParam response = new ResponseParam(res);
				if (response.getResult() != ResponseParam.RESULT_SUCCESS) {
					return false;
				}			
				Friend.insertFriend(clientApplication.getDatabaseHelper(), values);
				return true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			dialog = null;
			if(result) {
				list.remove(position);
				fAdapter.notifyDataSetChanged();
				Toast.makeText(AddFriendsActivity.this, "添加好友完成", 0).show();
			}
			super.onPostExecute(result);
		}
	}*/

	
}