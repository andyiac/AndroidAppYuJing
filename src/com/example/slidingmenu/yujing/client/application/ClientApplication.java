package com.example.slidingmenu.yujing.client.application;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.slidingmenu.yujing.client.database.DatabaseHelper;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;


public class ClientApplication extends Application{
	
	/**
	 * 请求协议
	 */
	public static final String HTTP = "http";
	
	/**
	 * 服务器地址
	 */
	public static final String IP_ADDRESS = "60.8.194.163";
	
	/**
	 * 服务器端口
	 */
	public static final int PORT = 8084;
	
	/**
	 * 请求的文件
	 */
	public static final String FILE = "/book/Book";
	
	private DatabaseHelper databaseHelper;
	
	private SharedPreferences loginUserInfo;

	@Override
	public void onCreate() {
		super.onCreate();
		databaseHelper = new DatabaseHelper(this.getApplicationContext(), "client.db", null, 1);
	}
	
	public DatabaseHelper getDatabaseHelper(){
		return this.databaseHelper;		
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










