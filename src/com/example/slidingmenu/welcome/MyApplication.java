package com.example.slidingmenu.welcome;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;

/**
 * Created by andyiac on 13-9-23.
 */
public class MyApplication  extends Application{
    private SharedPreferences loginUserInfo;
    @Override
    public void onCreate() {
        super.onCreate();

//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    public SharedPreferences getLoginUserInfo(){
		SharedPreferences shared = this.getSharedPreferences("lastest_login", Context.MODE_PRIVATE);
		String name = shared.getString(RequestParam.USER_NAME, "");
		this.loginUserInfo = this.getSharedPreferences(name, Context.MODE_PRIVATE);
		return this.loginUserInfo;
	}

	public void setLoginUserInfo(String name) {
		SharedPreferences shared = this.getSharedPreferences("lastest_login", Context.MODE_PRIVATE);
		shared.edit().putString(RequestParam.USER_NAME, name).commit();
	}
}

