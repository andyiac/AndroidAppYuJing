package com.example.slidingmenu.yujing.client.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;
import com.example.slidingmenu.yujing.client.service.MsgService;

public class LoginLogoutBroadCast extends BroadcastReceiver{

	public static final String BROADCAST_LOGIN = "login";
	public static final String BROADCAST_LOGOUT= "logout";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equals(BROADCAST_LOGIN) ) {
			Toast.makeText(context, 
					context.getText(R.string.login),
					Toast.LENGTH_SHORT)
					.show();
			SharedPreferences sharedPreferences = ((ClientApplication) context.getApplicationContext()).getLoginUserInfo();
			Editor editor = sharedPreferences.edit();
			editor.putInt(RequestParam.STATUS, RequestParam.ONLINE);
			editor.commit();
			
			return;
		}
		
		if(intent.getAction().equals(BROADCAST_LOGOUT) ) {
			Toast.makeText(context, 
					context.getText(R.string.menu_logout), 
					Toast.LENGTH_SHORT)
					.show();
			SharedPreferences sharedPreferences = ((ClientApplication) context.getApplicationContext()).getLoginUserInfo();
			Editor editor = sharedPreferences.edit();
			editor.putInt(RequestParam.STATUS, RequestParam.OFFLINE);
			editor.commit();
			
			Intent service = new Intent(context, MsgService.class);
			context.stopService(service);

			return;
		}
		
	}

}
