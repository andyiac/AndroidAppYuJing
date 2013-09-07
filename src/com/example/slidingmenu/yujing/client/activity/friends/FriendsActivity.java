package com.example.slidingmenu.yujing.client.activity.friends;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.activity.ProfileActivity;
import com.example.slidingmenu.yujing.client.activity.controller.BaseActivity;
import com.example.slidingmenu.yujing.client.activity.letter.SendLetterActivity;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.contentprovider.DataProvider;
import com.example.slidingmenu.yujing.client.database.table.Friend;
import com.example.slidingmenu.yujing.client.network.HttpClient;
import com.example.slidingmenu.yujing.client.network.Request;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;
import com.example.slidingmenu.yujing.client.network.mode.friend.GetFriendResponseParam;
import com.example.slidingmenu.yujing.client.service.MsgRefresh;
import com.example.slidingmenu.yujing.client.service.MsgService;


public class FriendsActivity extends BaseActivity implements MsgRefresh {

	private ClientApplication clientApplication;
	private ListView friendsList; 
	private AllFriendsAdapter fAdapter;
	private GetAllFriendTask mGetAllFriendTask;
	private DeleteFriendTask mDeleteFriendTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_layout);
		
		clientApplication = (ClientApplication) getApplication();
		friendsList = (ListView) findViewById(R.id.friends_list);
		init();
		setListAdapter();
		if(fAdapter.getCount() == 0) {
			getFriendFromNetwork2();
		}
		friendsList.setOnItemClickListener(new ClickListView());
		registerForContextMenu(friendsList);		
		
	}

	public void onRefrehClick(View v) {
		getFriendFromNetwork();
	}
	
	private void getFriendFromNetwork() {
		
		RequestParam requestParam = new RequestParam();
		requestParam.setUserName(clientApplication.getLoginUserInfo().getString(RequestParam.USER_NAME, null));
		requestParam.setPassword(clientApplication.getLoginUserInfo().getString(RequestParam.PASSWORD, null));
		requestParam.setRandomKey("1234");
		requestParam.setRequestType(RequestParam.GET_NEW_FRIENDS);
		requestParam.setParams(new String[]{""});

		mGetAllFriendTask = new GetAllFriendTask();
		mGetAllFriendTask.execute(requestParam);
	}	
	
	private void getFriendFromNetwork2() {
		
		RequestParam requestParam = new RequestParam();
		requestParam.setUserName(clientApplication.getLoginUserInfo().getString(RequestParam.USER_NAME, null));
		requestParam.setPassword(clientApplication.getLoginUserInfo().getString(RequestParam.PASSWORD, null));
		requestParam.setRandomKey("1234");
		requestParam.setRequestType(RequestParam.GET_ALL_FRIENDS);
		requestParam.setParams(new String[]{""});

		mGetAllFriendTask = new GetAllFriendTask();
		mGetAllFriendTask.execute(requestParam);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == ADD_FRIEND && resultCode == RESULT_OK) {
			getFriendFromNetwork();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void onAddClick(View v){
		startActivityForResult(new Intent(this, AddFriendsActivity.class), ADD_FRIEND);
	}
	
	private void setListAdapter(){
		
		String user = clientApplication.getLoginUserInfo().getString(RequestParam.USER_NAME, "");
		Cursor cs = managedQuery(DataProvider.Friend_CONTENT_URI, null, Friend.UID
				+ "=?", new String[] {user}, Friend.name + " COLLATE LOCALIZED ASC ");
		fAdapter = new AllFriendsAdapter(FriendsActivity.this, 
				R.layout.friends_layout_item, 
				cs, 
				false);
		friendsList.setAdapter(fAdapter);
	}

	@Override
	public void onCreateContextMenu( ContextMenu menu, View v,
			ContextMenuInfo menuInfo ) {		
		menu.add( 0, 0, 0, "私信" );
		menu.add( 0, 1, 0, "查看" );
		menu.add( 0, 2, 0, "删除" );
		super.onCreateContextMenu( menu, v, menuInfo );
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

		ContentValues values = this.fAdapter.getItem(menuInfo.position);

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
			intent2.setClass(FriendsActivity.this, ProfileActivity.class);
			startActivity(intent2);
			break;

		// 删除
		case 2:
			RequestParam requestParam = new RequestParam();
			requestParam.setUserName(clientApplication.getLoginUserInfo().getString(RequestParam.USER_NAME, null));
			requestParam.setPassword(clientApplication.getLoginUserInfo().getString(RequestParam.PASSWORD, null));
			requestParam.setRandomKey("1234");
			requestParam.setRequestType(RequestParam.DEL_FRIENDS);
			requestParam.setParams(new String[] { values.getAsString(Friend.mobile)});
			mDeleteFriendTask = new DeleteFriendTask();
			mDeleteFriendTask.execute(requestParam, values.getAsInteger(Friend._id));
			
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		if(mGetAllFriendTask != null && mGetAllFriendTask.getStatus() == Status.RUNNING) {
			mGetAllFriendTask.cancel(true);
			mGetAllFriendTask = null;
		}
		
		if(mDeleteFriendTask != null && mDeleteFriendTask.getStatus() == Status.RUNNING) {
			mDeleteFriendTask.cancel(true);
			mDeleteFriendTask = null;
		}
		
		super.onDestroy();
	}
	
	private class GetAllFriendTask extends AsyncTask<RequestParam, Integer, Boolean>{

		private ProgressDialog dialog;		
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(FriendsActivity.this, "", getText(R.string.waiting));
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(RequestParam... param) {
			
			//如果网络没有连接则更新进度为 网络连接异常
			if(!HttpClient.isConnect(clientApplication.getApplicationContext())) {
				return false;
			}
			
			RequestParam requestParam = param[0];
			String res = Request.request(requestParam.getJSON());
			// 如果请求结果为空字符串，则请求失败
			if ("".equals(res)) {
				return false;
			}

			try {
				GetFriendResponseParam response = new GetFriendResponseParam(res);
				if (response.getResult() != GetFriendResponseParam.RESULT_SUCCESS) {
					return false;
				}
				
				for(ContentValues values : response.getAllFriend()){
					Friend.insertFriend(clientApplication.getDatabaseHelper(), values);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				fAdapter.refresh();
			}
			dialog.dismiss();
			super.onPostExecute(result);
		}
	}
	
	private class ClickListView implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {
			Intent intent = new Intent();
			intent.putExtra("values", fAdapter.getItem(position));
			intent.setClass(FriendsActivity.this, ProfileActivity.class);
			startActivity(intent);
		}
	}
	
	private class DeleteFriendTask extends AsyncTask<Object, Integer, Boolean>{

		private ProgressDialog dialog;		
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(FriendsActivity.this, "", getText(R.string.waiting));
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Object... param) {

			//如果网络没有连接则更新进度为 网络连接异常
			if( !HttpClient.isConnect( clientApplication.getApplicationContext() ) ) {
				return false;
			}			
			RequestParam requestParam = (RequestParam) param[0];
			int _id = (Integer)param[1];
			String res = Request.request(requestParam.getJSON());
			if ("".equals(res)) {
				return false;
			}

			try {
				ResponseParam response = new ResponseParam(res);
				if (response.getResult() != ResponseParam.RESULT_SUCCESS) {
					return false;
				}							
				Friend.deleteFriend(clientApplication.getDatabaseHelper(), _id);
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				fAdapter.refresh();
			}
			dialog.dismiss();
			super.onPostExecute(result);
		}
	}

	@Override
	public void init() {
		MsgService.acList.add(this);
	}

	@Override
	public void refresh(int what, Object... objects) {
		fAdapter.refresh();
	}	
}