package com.example.slidingmenu.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.example.slidingmenu.R;
import com.example.slidingmenu.network.HttpClient;
import com.example.slidingmenu.network.Request;
import com.example.slidingmenu.network.mode.LoginoutResponseParam;
import com.example.slidingmenu.network.mode.RequestParam;
import com.example.slidingmenu.welcome.MyApplication;
import com.example.slidingmenu.yujing.client.utils.Utils;
import org.json.JSONException;

public class LoginActivity extends Activity {

    private static final boolean D = true;
    private static final String TAG = "LoginActivity";
    private Context mContext;
	private RelativeLayout rl_user;
	private Button mLogin;
	private Button register;


    private EditText etAccount,etPassword;

    private MyApplication myApplication;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		mContext=this;
		findView();
		init();
	}
	
	private void findView(){
		rl_user=(RelativeLayout) findViewById(R.id.rl_user);
		mLogin=(Button) findViewById(R.id.login);
		register=(Button) findViewById(R.id.register);
        etAccount=(EditText)findViewById(R.id.account);
        etPassword=(EditText)findViewById(R.id.password);
	}

	private void init(){
		Animation anim= AnimationUtils.loadAnimation(mContext, R.anim.login_anim);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);
		
		mLogin.setOnClickListener(loginOnClickListener);
		register.setOnClickListener(registerOnClickListener);
	}
	

	private OnClickListener loginOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent=new Intent(mContext,NewsYuJingActivity.class);
			startActivity(intent);
			finish();


		if(TextUtils.isEmpty(etAccount.getText().toString())) {
			etAccount.setError(getString(R.string.no_empyt_name));
			return;
		}

		if(TextUtils.isEmpty(etPassword.getText().toString())) {
			etPassword.setError(getString(R.string.no_empty_password));
			return;
		}

		RequestParam requestParam = new RequestParam();
		requestParam.setUserName(etAccount.getText().toString());
		requestParam.setPassword(etPassword.getText().toString());
		requestParam.setRequestType(requestParam.LOGIN);
		requestParam.setRandomKey("1234");
		requestParam.setParams(new String[]{""});

        LoginTask mLoginTask = new LoginTask();
		mLoginTask.execute(requestParam);
		}
	};

	private OnClickListener registerOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
//			Intent intent=new Intent(mContext, RegisterPhoneActivity.class);
//			startActivity(intent);
			
		}
	};


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
			if(D) Log.i(TAG, "------1---------");
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
				myApplication.setLoginUserInfo(etAccount.getText().toString());
				SharedPreferences sharedPreferences = myApplication.getLoginUserInfo();
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString(RequestParam.USER_NAME, etPassword.getText().toString());
				editor.putString(RequestParam.PASSWORD, etPassword.getText().toString());
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
				startActivity(new Intent(LoginActivity.this, NewsYuJingActivity.class));
				finish();
			} else {
				Utils.myToast(LoginActivity.this, getText(R.string.login_fail).toString(), R.drawable.toast_error);
			}
			super.onPostExecute(result);

		}
	}
}
