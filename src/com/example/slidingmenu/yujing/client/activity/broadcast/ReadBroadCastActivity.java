package com.example.slidingmenu.yujing.client.activity.broadcast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.database.table.Topic;
import com.example.slidingmenu.yujing.client.network.Request;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;
import com.example.slidingmenu.yujing.client.network.mode.topic.GetTopicComResponseParam;
import com.example.slidingmenu.yujing.client.utils.Utils;


@SuppressWarnings("unchecked")
public class ReadBroadCastActivity extends Activity{
	
	private ListView commentList;
	
	private TextView name, content, time;
	private ImageView photo;
	
	
	private ArrayList<HashMap> list;
	private EditText addCom;
	
	private ComBroadCastAdapter comAdapter;
	
	private GetTopicComTask mGetTopicComTask;
	private AddTopicComTask mAddTopicComTask;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_broadcast_layout);
		commentList = (ListView) findViewById(R.id.broadcast_disscuss);
		list = new ArrayList<HashMap>();
		addCom = (EditText) findViewById(R.id.edit_discuss_content);
		
		// broadcast
		View view = findViewById(R.id.broadcast);
		name = (TextView) view.findViewById(R.id.broadcast_name);
		content = (TextView) view.findViewById(R.id.broadcast_content);
		time = (TextView) view.findViewById(R.id.broadcast_date);
		photo = (ImageView) view.findViewById(R.id.broadcast_thumb);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra(Topic.tableName);
		name.setText(bundle.getString(Topic.name));
		content.setText(bundle.getString(Topic.content));
		time.setText(Utils.timeFormat(bundle.getInt(Topic.time)));
		
		getComFromWeb();
	}

	
	@Override
	protected void onDestroy() {

		if(mGetTopicComTask != null && mGetTopicComTask.getStatus() == Status.RUNNING) {
			mGetTopicComTask.cancel(true);
			mGetTopicComTask = null;
		}
		if(mAddTopicComTask != null && mAddTopicComTask.getStatus() == Status.RUNNING) {
			mAddTopicComTask.cancel(true);
			mAddTopicComTask = null;
		}
		
		super.onDestroy();
	}
	
	private void getComFromWeb() {
		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra(Topic.tableName);
		long topic_id = bundle.getLong(Topic.ID);
		
		ClientApplication capp = (ClientApplication) this.getApplication();
		SharedPreferences shared = capp.getLoginUserInfo();
		RequestParam sp = new RequestParam();
		sp.setUserName(shared.getString(RequestParam.USER_NAME, ""));
		sp.setPassword(shared.getString(RequestParam.PASSWORD, ""));
		sp.setRandomKey("1234");
		sp.setRequestType(RequestParam.GET_TOPIC_COM);
		sp.setParams(new String[] {String.valueOf(topic_id)});
		
		mGetTopicComTask = new GetTopicComTask();
		mGetTopicComTask.execute(sp);
	}
	
	public void onSendClick(View view) {
		
		String content = addCom.getText().toString();
		if(TextUtils.isEmpty(content)) {
			addCom.setError("no empty content");
			return;
		}
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra(Topic.tableName);
		long topic_id = bundle.getLong(Topic.ID);
		String time = String.valueOf((int)(System.currentTimeMillis()/1000));
		
		String[] params = new String[] {
				String.valueOf(topic_id),
				content,
				"baidu.com",
				time,
				String.valueOf(System.currentTimeMillis()),
		};
		
		ClientApplication capp = (ClientApplication) this.getApplication();
		SharedPreferences shared = capp.getLoginUserInfo();
		RequestParam sp = new RequestParam();
		sp.setUserName(shared.getString(RequestParam.USER_NAME, ""));
		sp.setPassword(shared.getString(RequestParam.PASSWORD, ""));
		sp.setRandomKey("1234");
		sp.setRequestType(RequestParam.ADD_TOPIC_COM);
		sp.setParams(params);
		
		HashMap tempComValue = new HashMap();
		tempComValue.put("Topic_Com_Content", content);
		tempComValue.put("Topic_Com_Time", time);
		tempComValue.put("Topic_Com_From", shared.getString(RequestParam.USER_NAME, ""));
		
		mAddTopicComTask = new AddTopicComTask();
		mAddTopicComTask.execute(sp, tempComValue);
		
		
		
		addCom.setText("");
	}
	
	private void setAdapter() {
		if(comAdapter == null) {
            //TODO : list cast error
//			comAdapter = new ComBroadCastAdapter(ReadBroadCastActivity.this, (List<? extends Map<String, ?>>) list);
			commentList.setAdapter(comAdapter);
		} else {
			comAdapter.notifyDataSetChanged();
		}
	}
	
	public class AddTopicComTask extends AsyncTask<Object, Integer, Integer>{
		
		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ReadBroadCastActivity.this, 
					"", 
					getText(R.string.waiting));
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(Object... params) {
			RequestParam requestParam = (RequestParam) params[0];
			String res = Request.request(requestParam.getJSON());
			if ("".equals(res)) {
				return -1;
			}
			try {
				ResponseParam response = new ResponseParam(res);
//				System.out.println("return params："+response.toString());
				if (response.getResult() == ResponseParam.RESULT_SUCCESS) {
					list.add((HashMap)params[1]);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return 0;
		}
	
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if(result == 0) {
				setAdapter();
			} else {
				Toast.makeText(ReadBroadCastActivity.this, "fail", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public class GetTopicComTask extends AsyncTask<RequestParam, Integer, Integer>{

		private ProgressDialog dialog;
		private TextView empty;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ReadBroadCastActivity.this, 
					"", 
					getText(R.string.waiting));
			empty = (TextView) findViewById(R.id.empty);
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
			GetTopicComResponseParam response = new GetTopicComResponseParam(res);
//			System.out.println("返回参数："+response.toString());
			if (response.getResult() != ResponseParam.RESULT_SUCCESS) {
				return -1;
			}
			list.addAll(response.getAllTopicCom());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			dialog.dismiss();
			if(result == 0) {
				if(list == null || list.isEmpty()) {
					empty.setVisibility(View.VISIBLE);
					return;
				}
				empty.setVisibility(View.GONE);
				setAdapter();
			} else {
				Toast.makeText(ReadBroadCastActivity.this, "fail", Toast.LENGTH_SHORT).show();
			}
		}

		
	}
}
