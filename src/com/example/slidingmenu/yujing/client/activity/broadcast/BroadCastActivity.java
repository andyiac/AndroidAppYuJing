package com.example.slidingmenu.yujing.client.activity.broadcast;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.activity.controller.BaseActivity;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.contentprovider.DataProvider;
import com.example.slidingmenu.yujing.client.database.DatabaseHelper;
import com.example.slidingmenu.yujing.client.database.table.Topic;
import com.example.slidingmenu.yujing.client.network.Request;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;
import com.example.slidingmenu.yujing.client.network.mode.topic.GetAllTopicResponseParam;
import com.example.slidingmenu.yujing.client.service.MsgRefresh;
import com.example.slidingmenu.yujing.client.service.MsgService;
import com.example.slidingmenu.yujing.client.utils.Utils;


public class BroadCastActivity extends BaseActivity implements MsgRefresh {
	
	private ListView broadcastList; 
	private BroadCastAdapter broadCastAdapter;
	private ReadTask mReadTask;
	
	private TextView loadText;
	private ProgressBar loadBar;
	private ViewGroup loadParent;
	
	private ClientApplication mApplication;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (ClientApplication) getApplication();
		setContentView(R.layout.broadcast_layout);
		broadcastList = (ListView) findViewById(R.id.broadcast_list);
		loadParent = (ViewGroup) findViewById(R.id.load_parent);
		loadText = (TextView) findViewById(R.id.load_text);
		loadBar = (ProgressBar) findViewById(R.id.load_progress);
		setListAdapter();
		init();
		broadcastList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startComActivity(position);
			}
		});
		registerForContextMenu(broadcastList);
	}
	
	public void onAddTopicClick(View v) {
		startActivityForResult(new Intent(this, SendBroadCastActivity.class), RequestParam.SEND_TOPIC);
	}
	
	private void startComActivity(int position) {
		HashMap<?, ?> map = (HashMap<?, ?>) broadCastAdapter.getItem(position);
		Intent intent = new Intent(BroadCastActivity.this, ReadBroadCastActivity.class);
		Bundle bundle = new Bundle();
		bundle.putLong(Topic.ID, (Long) map.get(Topic.ID));
		bundle.putString(Topic.content, (String) map.get(Topic.content));
		bundle.putInt(Topic.time, (Integer) map.get(Topic.time));
		bundle.putString(Topic.name, (String) map.get(Topic.name));
		bundle.putString(Topic.photo, (String) map.get(Topic.photo));
		intent.putExtra(Topic.tableName, bundle);
		startActivity(intent);
	}
	
	public void onRefrehClick(View view) {
		getTopicFromNetwork();
	}
	
	private void getTopicFromNetwork() {
		DatabaseHelper dbHelper = mApplication.getDatabaseHelper();
		int size = Topic.getCount(dbHelper);
		SharedPreferences shared = mApplication.getLoginUserInfo();
		RequestParam sp = new RequestParam();
		sp.setUserName(shared.getString(RequestParam.USER_NAME, ""));
		sp.setPassword(shared.getString(RequestParam.PASSWORD, ""));
		sp.setRandomKey("1234");
		sp.setRequestType(RequestParam.GET_NEW_TOPIC);
		sp.setParams(new String[] {String.valueOf(size)});
		
		mReadTask = new ReadTask();
		mReadTask.execute(sp);
	}
	
	@Override
	protected void onDestroy() {
		if(mReadTask != null && mReadTask.getStatus() == Status.RUNNING) {
			mReadTask.cancel(true);
			mReadTask = null;
		}
		super.onDestroy();
	}
	
	private void setListAdapter() {
		Cursor cs = managedQuery(DataProvider.Topic_CONTENT_URI, null, null, null,  Topic.time + " DESC ");
		broadcastList.setAdapter(broadCastAdapter = new BroadCastAdapter(this, 
				R.layout.broadcast_layout_item, 
				cs, 
				false));
	}

	private void deleteTopic(int position) {
		HashMap<?, ?> map = broadCastAdapter.getItem(position);
		Topic.deleteTopic(((ClientApplication)this.getApplication())
						.getDatabaseHelper(), (Integer) map.get(Topic._id));
		broadCastAdapter.refresh();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
        menu.add(0, 0, 0, R.string.delete);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo = ( AdapterView.AdapterContextMenuInfo ) item.getMenuInfo();
		
		switch (item.getItemId()) {
		case 0:
			deleteTopic(menuInfo.position);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == RequestParam.SEND_TOPIC && resultCode == RESULT_OK) {
			getTopicFromNetwork();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public class ReadTask extends AsyncTask<RequestParam, Integer, Integer>{

		ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(BroadCastActivity.this, 
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
				GetAllTopicResponseParam response = new GetAllTopicResponseParam(res);
				System.out.println("返回参数：" + response.toString());
				if (response.getResult() != GetAllTopicResponseParam.RESULT_SUCCESS) {
					return -1;
				}
				insertToDataBase(response.getAllTopic());
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			dialog.dismiss();
			if(result != ResponseParam.RESULT_SUCCESS) {
				Toast.makeText(BroadCastActivity.this, R.string.read_topic_fail, 0).show();
			} else {
				Toast.makeText(BroadCastActivity.this, R.string.read_topic_succ, 0).show();
				broadCastAdapter.refresh();
			}
			
			super.onPostExecute(result);
			
		}
		private void insertToDataBase(List<HashMap<String, Object>> letters) {
			
			HashMap<String, Object> map = null; 
			ContentValues values = null;
			for(int i = 0; i< letters.size(); i++) {
				map = letters.get(i);
				values = Utils.hashMapToContentValues(map);
				try {
					Topic.insertTopic(mApplication.getDatabaseHelper(), values);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}

	private void setLoadText(boolean begin) {
	
		if(begin) {
			if(broadCastAdapter.getCount() == 0) {
				return;
			}
			if(broadCastAdapter.getCount() > 0) {
				loadParent.setVisibility(View.GONE);
				return;
			}
		} 
			
		if(broadCastAdapter.getCount() <= 0) {	
			loadBar.setVisibility(View.INVISIBLE);
			loadText.setText("当前没有话题。");
		} else {
			loadParent.setVisibility(View.GONE);
		}
		
	}
	
	@Override
	public void init() {
		setLoadText(true);
		MsgService.acList.add(this);
	}

	@Override
	public void refresh(int what, Object... objects) {
		broadCastAdapter.refresh();
		setLoadText(false);
	}
}
