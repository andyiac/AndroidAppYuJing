package com.example.slidingmenu.yujing.client.activity.letter;


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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.activity.controller.BaseActivity;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.contentprovider.DataProvider;
import com.example.slidingmenu.yujing.client.database.table.PrivateLetter;
import com.example.slidingmenu.yujing.client.network.Request;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;
import com.example.slidingmenu.yujing.client.network.mode.letter.GetAllLetterResponseParam;
import com.example.slidingmenu.yujing.client.service.MsgRefresh;
import com.example.slidingmenu.yujing.client.service.MsgService;


public class LetterActivity extends BaseActivity implements MsgRefresh {
	
	private ClientApplication clientApplication;
	private ListView letterList;
	private LetterAdapter letterAdapter;
	
	private DeleteLetterTask mDeleteLetterTask;
	private GetLetterTask mGetLetterTask;
	private SendLetterTask mSendLetterTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.letter_layout);
		init();

		clientApplication = (ClientApplication) getApplication();
		letterList = (ListView) findViewById(R.id.letter_list);
		setListAdapter();
		if(letterAdapter.getCount() == 0) {
			getNewLetterFromNetWork();
		}
		registerForContextMenu(letterList);
	}
	
	public void onRefreshClick(View v) {
		getNewLetterFromNetWork();
	}
	
	private void setListAdapter() {
		Cursor cs = managedQuery(DataProvider.PrivateLetter_CONTENT_URI, null, null, null, PrivateLetter.time + " COLLATE LOCALIZED DESC");
		letterList.setAdapter(letterAdapter = new LetterAdapter(this, 
				R.layout.letter_layout_item, cs, false));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == SEND_LETTER && resultCode == RESULT_OK) {
			getNewLetterFromNetWork();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void getNewLetterFromNetWork() {
		mGetLetterTask = new GetLetterTask();
		mGetLetterTask.execute(getRequestParam());
	}
	
	public void onAddLetterClick(View v) {
		sendLetter();
	}
	
	private RequestParam getRequestParam() {
		
		int count = PrivateLetter.getLetterCount(clientApplication.getDatabaseHelper());
		SharedPreferences shared = clientApplication.getLoginUserInfo();
		RequestParam sp = new RequestParam();
		sp.setUserName(shared.getString(RequestParam.USER_NAME, ""));
		sp.setPassword(shared.getString(RequestParam.PASSWORD, ""));
		sp.setRandomKey("1234");
		sp.setRequestType(count == 0 ? RequestParam.GET_ALL_PRIVATELETTER : RequestParam.GET_NEW_PRIVATELETTER);
		sp.setParams(new String[] {""});
		return sp;
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean retValue = super.onCreateOptionsMenu(menu);
		MenuItem item = menu.add(0,1,1,R.string.refresh);
		item.setIcon(android.R.drawable.ic_menu_share);
		return retValue;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == 1) {
			getNewLetterFromNetWork();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.add(1, 0, 0, R.string.reply);
        menu.add(1, 1, 1, R.string.delete);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo = ( AdapterView.AdapterContextMenuInfo ) item.getMenuInfo();
		
		switch (item.getItemId()) {
		case 0:
			replyLetter(menuInfo.position);
			break;
		case 1:
			deleteLetter(menuInfo.position);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void deleteLetter(int position) {
		SharedPreferences shared = clientApplication.getLoginUserInfo();
		RequestParam sp = new RequestParam();
		sp.setUserName(shared.getString(RequestParam.USER_NAME, ""));
		sp.setPassword(shared.getString(RequestParam.PASSWORD, ""));
		sp.setRandomKey("1234");
		sp.setRequestType(RequestParam.DEL_LETTER);
		sp.setParams(new String[]{letterAdapter.getItem(position).getAsString(PrivateLetter.PrivateLetterID)});
		mDeleteLetterTask = new DeleteLetterTask();
		mDeleteLetterTask.execute(sp, position);
	}

	public void replyLetter(int position) {
		
		ContentValues values  = letterAdapter.getItem(position);
		final ReplyLetterDialog dialog = new ReplyLetterDialog(this);
		dialog.setOwnerActivity(this);
		dialog.setValues(values);
		dialog.setOnReplyLetterListener(new ReplyLetterDialog.OnReplyLetterListener() {
			
			@Override
			public void onSendLetterSucc() {
				getNewLetterFromNetWork();
			}
			
			@Override
			public void onSendLetterFail() {
				
			}
		});
		dialog.show();
	}
	
	@Override
	protected void onDestroy() {
		if(mGetLetterTask != null && mGetLetterTask.getStatus() == Status.RUNNING) {
			mGetLetterTask.cancel(true);
			mGetLetterTask = null;
		}
		
		if(mDeleteLetterTask != null && mDeleteLetterTask.getStatus() == Status.RUNNING) {
			mDeleteLetterTask.cancel(true);
			mDeleteLetterTask = null;
		}
		
		if(mSendLetterTask != null && mSendLetterTask.getStatus() == Status.RUNNING) {
			mSendLetterTask.cancel(true);
			mSendLetterTask = null;
		}
		super.onDestroy();
	}
	
	public class GetLetterTask extends AsyncTask<RequestParam, Integer, Integer> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(LetterActivity.this, "", getText(R.string.waiting));
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(RequestParam... params) {
			
			RequestParam requestParam = params[0];
			String res = Request.request(requestParam.getJSON());
			if ("".equals(res)) {
				return ResponseParam.REQUEST_FAIL;
			}
			try {
				ResponseParam response = new GetAllLetterResponseParam(res);
				if (response.getResult() != ResponseParam.RESULT_SUCCESS) {
					return ResponseParam.REQUEST_FAIL;
				}
				this.insertToDataBase((ClientApplication) LetterActivity.this.getApplication(), 
						((GetAllLetterResponseParam) response).getAllLetter());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return ResponseParam.RESULT_SUCCESS;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			dialog.dismiss();
			if(result == ResponseParam.RESULT_SUCCESS) {
				letterAdapter.refresh();
			} else {
				Toast.makeText(LetterActivity.this, getText(R.string.get_letter_fail), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
		private void insertToDataBase(ClientApplication context, List<ContentValues> letters) {
			
			for(ContentValues values : letters) {
				PrivateLetter.insertPrivateLetter(context.getDatabaseHelper(), values);
			}
			
		}
	}
	public class DeleteLetterTask extends AsyncTask<Object, Integer, Integer> {

		private ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(LetterActivity.this, "", getText(R.string.waiting));
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(Object... params) {
			
			RequestParam requestParam = (RequestParam) params[0];
			int position = (Integer)params[1];
			
			String res = Request.request(requestParam.getJSON());
			if ("".equals(res)) {
				return ResponseParam.REQUEST_FAIL;
			}
			try {
				ResponseParam response = new ResponseParam(res);
				if (response.getResult() != ResponseParam.RESULT_SUCCESS) {
					return ResponseParam.REQUEST_FAIL;
				}
				PrivateLetter.deletePrivateLetter(clientApplication.getDatabaseHelper(), 
						letterAdapter.
						getItem(position).
						getAsInteger(PrivateLetter._id));
			} catch (JSONException e) {
				System.out.println("删除私信异常==="+ e.toString());
				e.printStackTrace();
			}
			return ResponseParam.RESULT_SUCCESS;
		}
		  
		@Override
		protected void onPostExecute(Integer result) {
			dialog.dismiss();
			if(result == ResponseParam.RESULT_SUCCESS) {
				letterAdapter.refresh();
				Toast.makeText(LetterActivity.this, getText(R.string.delete_letter_succ), Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(LetterActivity.this, getText(R.string.delete_letter_fail), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
			
		}
	}
	@Override
	public void init() {
		MsgService.acList.add(this);
	}

	@Override
	public void refresh(int what, Object... objects) {
		letterAdapter.refresh();
	}

}
