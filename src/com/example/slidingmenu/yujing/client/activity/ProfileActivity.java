package com.example.slidingmenu.yujing.client.activity;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.activity.friends.AddFriendTask;
import com.example.slidingmenu.yujing.client.activity.letter.SendLetterActivity;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.database.table.Friend;
import com.example.slidingmenu.yujing.client.network.HttpClient;
import com.example.slidingmenu.yujing.client.network.Request;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;
import com.example.slidingmenu.yujing.client.network.mode.personinfo.PersonInfoResponseParam;


public class ProfileActivity extends Activity {

	private ClientApplication clientApplication;
	
	private TextView name, phone, sex, address, isFriend;
	private ImageView photo;
	private Button sendLetter;
	
	private GetMyInfoTask mGetMyInfoTask;
	private AddFriendTask mAddFriendTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_layout);
		clientApplication = (ClientApplication) getApplication();
		this.setupView();
		setMyProfile();
		sendLetter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendLetterTo();
			}
		});
	}

	private void sendLetterTo() {
		Intent intent = new Intent(this, SendLetterActivity.class);
		intent.putExtra("values", getIntentFor());
		startActivityForResult(intent, 0);
	}

	private void setMyProfile() {

		ContentValues values = this.getIntentFor();
		if (values == null) {

			SharedPreferences shared = clientApplication.getLoginUserInfo();
			String name = shared.getString(RequestParam.USER_NAME, "");
			String pswd = shared.getString(RequestParam.PASSWORD, "");

			RequestParam requestParam = new RequestParam();
			requestParam.setUserName(name);
			requestParam.setPassword(pswd);
			requestParam.setRequestType(RequestParam.GET_PERSONINFO);
			requestParam.setRandomKey("1234");
			requestParam.setParams(new String[] {name});

			mGetMyInfoTask = new GetMyInfoTask();
			mGetMyInfoTask.execute(requestParam);
			
		} else {
			setProfile(values);
		}
	}

	private void setupView() {
		name = (TextView) findViewById(R.id.name);
		phone = (TextView) findViewById(R.id.phone);
		sex = (TextView) findViewById(R.id.sex);
		address = (TextView) findViewById(R.id.address);
		isFriend = (TextView) findViewById(R.id.is_friend);
		photo = (ImageView) findViewById(R.id.thumb);
		sendLetter = (Button) findViewById(R.id.send_letter);
	}

	private boolean IsFriend(String uid) {
		return Friend.IsFriend(clientApplication.getDatabaseHelper(), uid);
	}
	
	private ContentValues getIntentFor() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			ContentValues values = (ContentValues) bundle.get("values");
			String localhost = clientApplication.getLoginUserInfo().getString(
					RequestParam.USER_NAME, "");
			String who = values.getAsString(Friend.mobile);
			if (localhost.equals(who)) {
				sendLetter.setEnabled(false);
				return null;
			}
			return values;
		}
		sendLetter.setEnabled(false);
		return null;
	}

	@Override
	protected void onDestroy() {
		if(mGetMyInfoTask != null && mGetMyInfoTask.getStatus() == Status.RUNNING) {
			mGetMyInfoTask.cancel(true);
			mGetMyInfoTask = null;
		}
		super.onDestroy();
		
	}
	
	private void setProfile(final ContentValues values) {
		if (values == null) {
			return;
		}
		final String uid = values.getAsString(Friend.mobile);
		name.setText(values.getAsString(Friend.name));
		phone.setText(values.getAsString(Friend.mobile));
		sex.setText(values.getAsString(Friend.sex));
		address.setText(values.getAsString(Friend.address));
		final boolean bool = IsFriend(uid);
		isFriend.setText(bool ? "是好友" : "不是好友，点击添加");
		isFriend.setOnClickListener(bool ? null : new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RequestParam requestParam = new RequestParam();
				requestParam.setUserName(clientApplication.getLoginUserInfo().getString(RequestParam.USER_NAME, null));
				requestParam.setPassword(clientApplication.getLoginUserInfo().getString(RequestParam.PASSWORD, null));
				requestParam.setRandomKey("1234");
				requestParam.setRequestType(RequestParam.ADD_FRIENDS);
				requestParam.setParams(new String[] {String.valueOf(uid)});
				mAddFriendTask = new AddFriendTask(ProfileActivity.this, new AddFriendTask.OnAddFriend() {
					
					@Override
					public void onAddFriendSuccess(Activity activity, int position) {
						isFriend.setText("是好友");
					}
					
					@Override
					public void onAddFriendFail(Activity activity, int position) {
						
					}
				});
				mAddFriendTask.execute(requestParam, values, 0);
			}
		});
	}

	public class GetMyInfoTask extends
			AsyncTask<RequestParam, Integer, Integer> {

		private ProgressDialog dialog;
		private ContentValues values = new ContentValues();

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ProfileActivity.this, "",
					getText(R.string.waiting));
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(RequestParam... param) {

			if (!HttpClient.isConnect(ProfileActivity.this)) {
				return -1;
			}

			RequestParam requestParam = param[0];
			String res = Request.request(requestParam.getJSON());
			if ("".equals(res)) {
				return -1;
			}
			try {
				PersonInfoResponseParam response = new PersonInfoResponseParam(
						res);
				if (response.getResult() != PersonInfoResponseParam.RESULT_SUCCESS) {
					return -1;
				}
				values.put(Friend.name, response.getPersonName());
				values.put(Friend.mobile, response.getPersonMobile());
				values.put(Friend.sex, response.getPersonSex());
				values.put(Friend.address, response.getPersonAddress());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return ResponseParam.RESULT_SUCCESS;
		}

		@Override
		protected void onPostExecute(Integer result) {
			dialog.dismiss();
			if (result == ResponseParam.RESULT_SUCCESS) {
				setProfile(this.values);
			}
			super.onPostExecute(result);
		}
	}

}
