package com.example.slidingmenu.yujing.client.activity.letter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.activity.friends.AllFriendsAdapter;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.database.table.Friend;
import com.example.slidingmenu.yujing.client.network.HttpClient;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;

public class SendLetterActivity extends Activity implements OnItemClickListener{
	
	private ClientApplication clientApplication;

	private LinearLayout layout;
	private ListView personList;
	private Button send;
	private EditText content, contactName;
	private TextView name, number, sex, addr, online;
	private Button back;
	private AllFriendsAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_letter_layout);
		
		clientApplication = (ClientApplication) getApplication();
		
		layout = (LinearLayout) findViewById(R.id.profile);
		send = (Button) findViewById(R.id.send);
		personList = (ListView) findViewById(R.id.person);
		personList.setOnItemClickListener(this);
		content = (EditText) findViewById(R.id.letter_content);
		contactName = (EditText) findViewById(R.id.letter_name);
		name = (TextView) findViewById(R.id.name);
		number = (TextView) findViewById(R.id.number);
		sex = (TextView) findViewById(R.id.sex);
		addr = (TextView) findViewById(R.id.addr);
		online = (TextView) findViewById(R.id.online);
		back = (Button) findViewById(R.id.back);

		this.getIntentFor();
	
		
		send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setRequestParam();
			}
		});
		
	}
	
	public void setInfo(ContentValues values) {
		contactName.setText(values.getAsString(Friend.mobile));
		name.setText("姓名：" + values.getAsString(Friend.name));
		number.setText("号码：" + values.getAsString(Friend.mobile));
		sex.setText("性别：" + values.getAsString(Friend.sex));
		addr.setText("地址：" + values.getAsString(Friend.address));
//		if (values.getAsInteger(Friend.state) == 0) {
//			online.setText("状态：" + "online");
//		} else {
//			online.setText("状态：" + "offline");
//		}
		layout.setVisibility(View.VISIBLE);
		personList.setVisibility(View.INVISIBLE);

	}

	public void backclick(View view) {
		layout.setVisibility(View.INVISIBLE);
		personList.setVisibility(View.VISIBLE);
	}

	public Cursor getAllFriends() {
		SQLiteDatabase db = clientApplication.getDatabaseHelper().getReadableDatabase();
		Cursor c = db.query(Friend.tableName, null, Friend.UID
				+ " = "
				+ clientApplication.getLoginUserInfo().getString(
						RequestParam.USER_NAME, null), null, null, null,
				Friend.name + " COLLATE LOCALIZED ASC ");
		return c;
	}

	private void getIntentFor() {
		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			ContentValues values = (ContentValues) intent.getExtras().get( "values");

			String mobile = values.getAsString(Friend.mobile);
			this.contactName.setText(mobile);
			setInfo(values);
			back.setVisibility(View.INVISIBLE);
		} else {
			personList.setAdapter(adapter = new AllFriendsAdapter(
					SendLetterActivity.this, R.layout.friends_layout_item,
					getAllFriends(), true));
		}
		
	}
	
	@Override
	protected void onResume() {
		IsNetWorkConnected();
		super.onResume();
	}
	
	private boolean IsNetWorkConnected() {
		if(HttpClient.isConnect(this)) {
			return true;
		}
		Toast.makeText(this, R.string.network_no_connect, Toast.LENGTH_SHORT).show();
		return false;
		
	}
	
	private boolean setRequestParam() {
		
		String contents = content.getText().toString();
		String contact = contactName.getText().toString();
		if(TextUtils.isEmpty(contents)) {
			content.setError(getText(R.string.letter_content_empty));
			return false;
		}
		if(TextUtils.isEmpty(contact)) {
			contactName.setError(getText(R.string.letter_contact_empty));
			return false;
		}
	
		String names = name.getText().toString().substring(3);
		
		int time = (int) (System.currentTimeMillis()/1000);
		
		String[] letter = new String[] {
			contact,
			contents,
			String.valueOf(time),
			names,
			"baidu.com"
		};
		
		RequestParam rp = new RequestParam();
		SharedPreferences sp = ((ClientApplication)this.getApplication()).getLoginUserInfo();
		rp.setUserName(sp.getString(RequestParam.USER_NAME, ""));
		rp.setPassword(sp.getString(RequestParam.PASSWORD, ""));
		rp.setRandomKey("1234");
		rp.setRequestType(RequestParam.SEND_PRIVATELETTER);
		rp.setParams(letter);
		
		new SendLetterTask(SendLetterActivity.this, new SendLetterTask.HandleSendLetter() {
			
			@Override
			public void onSendLetterSucc() {
				setResult(RESULT_OK);
				finish();
			}
			
			@Override
			public void onSendLetterFail() {
				setResult(RESULT_CANCELED);
				finish();
			}
		}).execute(rp);
		
		return true;
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		setInfo(adapter.getItem(position));
	}
	
}
