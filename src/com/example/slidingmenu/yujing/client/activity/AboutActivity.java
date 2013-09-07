package com.example.slidingmenu.yujing.client.activity;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.view.View;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.network.HttpClient;
import com.example.slidingmenu.yujing.client.network.Request;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;
import com.example.slidingmenu.yujing.client.utils.Utils;


public class AboutActivity extends Activity{
	
	private String updateUrl = "";
	private UpdateTask mUpdateTask = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_help_layout);
	}
	
	public void onUpdateClick(View v) {
		
		ClientApplication client = (ClientApplication) this.getApplication();
		SharedPreferences shared = client.getLoginUserInfo();
		RequestParam requestParam = new RequestParam();
 		requestParam.setUserName(shared.getString(RequestParam.USER_NAME, ""));
 		requestParam.setPassword(shared.getString(RequestParam.PASSWORD, ""));
 		requestParam.setRequestType(RequestParam.UPDATE);
 		requestParam.setRandomKey("1234");
 		requestParam.setParams(new String[]{""});
		
 		mUpdateTask = new UpdateTask();
 		mUpdateTask.execute(requestParam);
		
	}

	private void update() {
		try {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			if(updateUrl == null || updateUrl.equals("")) {
				Utils.myToast(AboutActivity.this, getString(R.string.update_fail), R.drawable.toast_error);
				return;
			}
			Uri url = Uri.parse(updateUrl);
			intent.setData(url);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Utils.myToast(AboutActivity.this, e.getMessage(), R.drawable.toast_error);
			return;
		}
	}
	
	
	class UpdateTask extends AsyncTask<RequestParam, Integer, Integer>{

		private ProgressDialog dialog;	
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(AboutActivity.this, "", getText(R.string.waiting));
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(RequestParam... params) {
			if(!HttpClient.isConnect(AboutActivity.this)) {
				return -1;
			}
			
			RequestParam requestParam = params[0];
			String res = Request.request(requestParam.getJSON());
			if ("".equals(res)) {
				return -1;
			}
			try {
				ResponseParam response = new ResponseParam(res);
				if (response.getResult() != ResponseParam.RESULT_SUCCESS) {
					return -1;
				}		
				AboutActivity.this.updateUrl = response.getContent();
				return 0;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return -1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if(result == -1) {
				Utils.myToast(AboutActivity.this, getString(R.string.update_fail), R.drawable.toast_error);
				return;
			}
			if(result == 0) {
				update();
			}
		}
	}


	@Override
	protected void onDestroy() {
		if(mUpdateTask != null && mUpdateTask.getStatus() == Status.RUNNING) {
			mUpdateTask.cancel(true);
			mUpdateTask = null;
		}
		super.onDestroy();
	}
	
}




