package com.example.slidingmenu.yujing.client.activity.broadcast;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.network.Request;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;


public class SendBroadCastActivity extends Activity{
	
	private EditText content;
	private SendTopicTask mSendTopicTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_broadcast_layout);
		content = (EditText) findViewById(R.id.edit_broad);
	}
	
	public void onSendClick(View view) {
	
		ClientApplication capp = (ClientApplication) this.getApplication();
		SharedPreferences shared = capp.getLoginUserInfo();
		String contents = content.getText().toString();
		if(TextUtils.isEmpty(contents)) {
			content.setError(getText(R.string.empty_content));
			return;
		}
		
		String uid = shared.getString(RequestParam.USER_NAME, "");
		String time = String.valueOf((int)(System.currentTimeMillis()/1000));
		String[] params = new String[] {
				contents,
				time,
				shared.getString(RequestParam.NAME, uid),
				"baidu.com",
		};
		
		RequestParam sp = new RequestParam();
		sp.setUserName(uid);
		sp.setPassword(shared.getString(RequestParam.PASSWORD, ""));
		sp.setRandomKey("1234");
		sp.setRequestType(RequestParam.ADD_TOPIC);
		sp.setParams(params);
		
		mSendTopicTask = new SendTopicTask();
		mSendTopicTask.execute(sp);
	}
	
	@Override
	protected void onDestroy() {
		
		if(mSendTopicTask != null && mSendTopicTask.getStatus()==Status.RUNNING) {
			mSendTopicTask.cancel(false);
		}
		
		super.onDestroy();
	}
	
	public class SendTopicTask extends AsyncTask<RequestParam, Integer, Integer>{

		ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(SendBroadCastActivity.this, 
									"", 
									getText(R.string.waiting));
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(RequestParam... params) {
			
			RequestParam requestParam = params[0];
			String res = Request.request(requestParam.getJSON());
			if ("".equals(res)) {
				return -1;
			}
			try {
				ResponseParam response = new ResponseParam(res);
				System.out.println("返回参数："+response.toString());
				if (response.getResult() != ResponseParam.RESULT_SUCCESS) {
					return -1;
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			dialog.dismiss();
			if(result != ResponseParam.RESULT_SUCCESS) {
				Toast.makeText(SendBroadCastActivity.this, getText(R.string.add_topic_fail), 0).show();
			} else {
				Toast.makeText(SendBroadCastActivity.this, getText(R.string.add_topic_succ), 0).show();
				setResult(RESULT_OK);
				finish();
			}
			super.onPostExecute(result);
			
		}
	}
}
