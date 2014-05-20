package com.example.slidingmenu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.example.slidingmenu.R;

public class LoginActivity extends Activity {

	private Context mContext;
	private RelativeLayout rl_user;
	private Button mLogin;
	private Button register;
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
			
		}
	};
	
	private OnClickListener registerOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
//			Intent intent=new Intent(mContext, RegisterPhoneActivity.class);
//			startActivity(intent);
			
		}
	};
}
