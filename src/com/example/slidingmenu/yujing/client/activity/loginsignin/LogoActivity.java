package com.example.slidingmenu.yujing.client.activity.loginsignin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.activity.controller.ClientActivity;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;


public class LogoActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = new View(this);
		view.setBackgroundResource(R.drawable.logo);
		setContentView(view);
		Handler launchHandler = new Handler();
		launchHandler.postDelayed(new LogoLanuch(), 1000);
	}
	
	private class LogoLanuch implements Runnable {
		public void run() {
			SharedPreferences info = ((ClientApplication)LogoActivity.this.getApplication()).getLoginUserInfo();
			int loginState = info.getInt(RequestParam.STATUS, RequestParam.OFFLINE);
			if( loginState == RequestParam.ONLINE ) {
				Intent intent = new Intent(LogoActivity.this, ClientActivity.class);
				LogoActivity.this.startActivity(intent);
			} else if(loginState == RequestParam.OFFLINE) {
				Intent intent = new Intent(LogoActivity.this, LoginSigninActivity.class);
				LogoActivity.this.startActivity(intent);
			} 
			LogoActivity.this.finish();
		}

	}
	
}
