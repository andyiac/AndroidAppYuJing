package com.example.slidingmenu.yujing.client.activity.controller;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.activity.AboutActivity;
import com.example.slidingmenu.yujing.client.activity.ProfileActivity;
import com.example.slidingmenu.yujing.client.activity.letter.SendLetterActivity;
import com.example.slidingmenu.yujing.client.activity.loginsignin.LoginActivity;
import com.example.slidingmenu.yujing.client.activity.loginsignin.SignProfileActivity;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.broadcastreceiver.LoginLogoutBroadCast;
import com.example.slidingmenu.yujing.client.database.table.Friend;
import com.example.slidingmenu.yujing.client.network.HttpClient;
import com.example.slidingmenu.yujing.client.network.Request;
import com.example.slidingmenu.yujing.client.network.mode.LoginoutResponseParam;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;


public class BaseActivity extends Activity{
	
	private static final int DIALOG_YES_NO_MESSAGE = 0;
	private static final int DIALOG_YES_NO_LOGOUT = 1;
	
	protected static final int ADD_FRIEND = 222;

	public static final int SEND_LETTER = 55;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public boolean onKeyDown(int arg0, KeyEvent arg1) {
		
		if(arg0 == KeyEvent.KEYCODE_BACK) {
			showDialog(DIALOG_YES_NO_MESSAGE);
		}
		
		return super.onKeyDown(arg0, arg1);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.base_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_profile:
			profile();
			break;
		case R.id.menu_letter:
			sendLetter();		
			break;
		case R.id.menu_signin:
			startActivity(new Intent(this, SignProfileActivity.class));
			break;
		case R.id.menu_logout:
			showDialog(DIALOG_YES_NO_LOGOUT);
			break;
		case R.id.menu_about:
			startActivity(new Intent(BaseActivity.this, AboutActivity.class));
			break;
		case R.id.menu_quit:
			showDialog(DIALOG_YES_NO_MESSAGE);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void sendLetter() {
		startActivityForResult(new Intent(this, SendLetterActivity.class), SEND_LETTER);
	}

	private void profile() {
		ClientApplication ca = (ClientApplication) this.getApplication();
		SharedPreferences share = ca.getLoginUserInfo();
		Intent intent = new Intent(this, ProfileActivity.class);
		ContentValues values = new ContentValues();
		values.put(Friend.mobile, share.getString(RequestParam.USER_NAME, ""));
		intent.putExtra("values", values);
		startActivity(intent);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_YES_NO_MESSAGE:
			return new AlertDialog.Builder(BaseActivity.this)
	         .setIcon(android.R.drawable.ic_dialog_alert)
	         .setTitle(R.string.alter)
	         .setMessage(getString(R.string.is_quit))
	         .setPositiveButton(R.string.comfirm, new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int whichButton) {
	            	 finish();
	             }
	         })
	         .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int whichButton) {
	            	 
	             }
	         })
	         .create();
		case DIALOG_YES_NO_LOGOUT:
			return new AlertDialog.Builder(BaseActivity.this)
	         .setIcon(android.R.drawable.ic_dialog_alert)
	         .setTitle(R.string.alter)
	         .setMessage(getString(R.string.is_logout))
	         .setPositiveButton(R.string.comfirm, new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int whichButton) {
	            	setRequestParam();
	             }
	         })
	         .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int whichButton) {
	            	 
	             }
	         })
	         .create();
		default:
			break;
		}
		 
		return super.onCreateDialog(id);
	}
	
	private void setRequestParam() {
		
		ClientApplication client = (ClientApplication) this.getApplication();
		SharedPreferences shared = client.getLoginUserInfo();
		RequestParam requestParam = new RequestParam();
 		requestParam.setUserName(shared.getString(RequestParam.USER_NAME, ""));
 		requestParam.setPassword(shared.getString(RequestParam.PASSWORD, ""));
 		requestParam.setRequestType(RequestParam.LOGOUT);
 		requestParam.setRandomKey("1234");
 		requestParam.setParams(new String[]{"5"});
 		
 		new LogoutTask().execute(requestParam);
	}
	
	private void sendLogOutBroadCast() {
		Intent intent = new Intent(BaseActivity.this, LoginLogoutBroadCast.class);
		intent.setAction(LoginLogoutBroadCast.BROADCAST_LOGOUT);
		sendBroadcast(intent);
	}
	
	private class LogoutTask extends AsyncTask<RequestParam, Integer, Boolean>{

		private ProgressDialog dialog;		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(BaseActivity.this, "", getText(R.string.waiting));
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(RequestParam... param) {

			if(!HttpClient.isConnect(BaseActivity.this)) {
				return false;
			}
			
			RequestParam requestParam = param[0];
			String res = Request.request(requestParam.getJSON());
			if ("".equals(res)) {
				return false;
			}
			try {
				LoginoutResponseParam response = new LoginoutResponseParam(res);
				if (response.getResult() != LoginoutResponseParam.RESULT_SUCCESS) {
					return false;
				}							
				return true;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			sendLogOutBroadCast();
			super.onPostExecute(result);
			if (result) {
				startActivity(new Intent(BaseActivity.this, LoginActivity.class));
				ClientActivity c = (ClientActivity) BaseActivity.this.getParent();
				c.finish();
			}
		}
	}
}
