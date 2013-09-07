package com.example.slidingmenu.yujing.client.activity.loginsignin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.slidingmenu.R;


public class LoginSigninActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin_layout);
		
	}
	
	public void onLoginClick(View v) {
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}
	
	public void onSigninClick(View v) {
		startActivity(new Intent(this, SignProfileActivity.class));
	}

}
