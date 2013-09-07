package com.example.slidingmenu.yujing.client.activity.friends;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.database.table.Friend;
import com.example.slidingmenu.yujing.client.network.HttpClient;
import com.example.slidingmenu.yujing.client.network.Request;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;


public class AddFriendTask extends AsyncTask<Object, Integer, Boolean>{

		private ProgressDialog dialog;		
		private int position = -1;
		private Activity activity;
		private OnAddFriend onAddListener;
		
		public static interface OnAddFriend{
			public void onAddFriendSuccess(Activity activity, int position);
			public void onAddFriendFail(Activity activity, int position);
		}
		
		public AddFriendTask(Activity activity, OnAddFriend listener) {
			this.activity = activity;
			this.onAddListener = listener;
		}
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(activity, "", activity.getText(R.string.waiting));
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Object... param) {

			//如果网络没有连接则更新进度为 网络连接异常
			if(!HttpClient.isConnect(activity)) {
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
				Friend.insertFriend(((ClientApplication) activity.getApplication()).getDatabaseHelper(), values);
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
				if(this.onAddListener != null)
					this.onAddListener.onAddFriendSuccess(activity, position);
				Toast.makeText(activity, "添加好友完成", 0).show();
			} else {
				if(this.onAddListener != null)
					this.onAddListener.onAddFriendFail(activity, position);
				Toast.makeText(activity, "添加好友失败", 0).show();
			}
			super.onPostExecute(result);
		}
}