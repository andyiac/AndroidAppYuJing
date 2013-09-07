package com.example.slidingmenu.yujing.client.activity.loginsignin;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.activity.controller.ClientActivity;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.broadcastreceiver.LoginLogoutBroadCast;
import com.example.slidingmenu.yujing.client.network.HttpClient;
import com.example.slidingmenu.yujing.client.network.Request;
import com.example.slidingmenu.yujing.client.network.mode.LoginoutResponseParam;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;
import com.example.slidingmenu.yujing.client.utils.Utils;


public class LoginActivity extends Activity {
	
	public static final String TAG = "LoginActivity";
	
	private static final boolean D  = true;

	private ClientApplication clientApplication;
	
	private EditText name, password;

	private LoginTask mLoginTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		name = (EditText) findViewById(R.id.name);
		password = (EditText) findViewById(R.id.password);
		clientApplication = (ClientApplication) getApplication();
	}
	
	public void onLoginClick(View v) {
		
		if(TextUtils.isEmpty(name.getText().toString())) {
			name.setError(getString(R.string.no_empyt_name));
			return;
		}
		
		if(TextUtils.isEmpty(password.getText().toString())) {
			password.setError(getString(R.string.no_empty_password));
			return;
		}		
		
		RequestParam requestParam = new RequestParam();
		requestParam.setUserName(name.getText().toString());
		requestParam.setPassword(password.getText().toString());
		requestParam.setRequestType(requestParam.LOGIN);
		requestParam.setRandomKey("1234");
		requestParam.setParams(new String[]{""});

		mLoginTask = new LoginTask();
		mLoginTask.execute(requestParam);
		
	}
	
	public void onSigninClick(View v) {
		Intent intent = new Intent(this, SignProfileActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		if(mLoginTask != null && mLoginTask.getStatus() == Status.RUNNING) {
			mLoginTask.cancel(true);
		}
		super.onDestroy();
	}
	
	private void sendLoginBroadCast() {
		Intent intent = new Intent(LoginActivity.this, LoginLogoutBroadCast.class);
		intent.setAction(LoginLogoutBroadCast.BROADCAST_LOGIN);
		sendBroadcast(intent);
	}		

	
	/**
	 * 请求登录的Task
	 *
	 */
	public class LoginTask extends AsyncTask<RequestParam, Integer, Boolean> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(LoginActivity.this, "",
					getText(R.string.waiting));
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(RequestParam... param) {

			if(!HttpClient.isConnect(LoginActivity.this)) {
			if(D) Log.i(TAG,"------1---------");
			return false;
			}
			
			RequestParam requestParam = param[0];
			String res = Request.request(requestParam.getJSON());
			//如果请求结果为空字符串，则请求失败
			if ("".equals(res)) {
			if(D)	Log.i(TAG,"------2---------");
				return false;
			}
			try {
				LoginoutResponseParam response = new LoginoutResponseParam(res);
				if (response.getResult() != LoginoutResponseParam.RESULT_SUCCESS) {
				if(D) Log.i(TAG,"------3---------"+response.getResult() + LoginoutResponseParam.RESULT_SUCCESS);
					return false;
				}
				// 存储用户登录信息
				clientApplication.setLoginUserInfo(name.getText().toString());
				SharedPreferences sharedPreferences = clientApplication.getLoginUserInfo();
				Editor editor = sharedPreferences.edit();
				editor.putString(RequestParam.USER_NAME, name.getText().toString());
				editor.putString(RequestParam.PASSWORD, password.getText().toString());
				editor.putString(RequestParam.ADDR, response.getPersonAddress());
				editor.putString(RequestParam.SEX, response.getPersonSex());
				editor.putString(RequestParam.NAME, response.getPersonName());
				editor.putString(RequestParam.PHOTO, response.getPersonPhoto());
				editor.putInt(RequestParam.STATUS, RequestParam.ONLINE);
				editor.commit();
				Log.i(TAG,"------4---------");
				
				return true;
			} catch (JSONException e) {
				System.out.println("登陆异常==="+ e.toString());
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {			
			
			dialog.dismiss();
			if (result) {
				sendLoginBroadCast();
				startActivity(new Intent(LoginActivity.this, ClientActivity.class));
				finish();
			} else {
				Utils.myToast(LoginActivity.this, getText(R.string.login_fail).toString(), R.drawable.toast_error);
			}
			super.onPostExecute(result);
			
		}
	}
}


