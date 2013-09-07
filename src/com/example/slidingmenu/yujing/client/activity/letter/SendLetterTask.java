package com.example.slidingmenu.yujing.client.activity.letter;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.network.Request;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;


public class SendLetterTask extends AsyncTask<RequestParam, Integer, Integer>{
	
		private ProgressDialog dialog;
		private Activity context;
		private HandleSendLetter letter;
		public static interface HandleSendLetter{
			public void onSendLetterSucc();
			public void onSendLetterFail();
		}
		
		public SendLetterTask(Activity context, HandleSendLetter letter) {
			this.context = context;
			this.letter = letter;
		}
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(context, "", context.getText(R.string.waiting));
			super.onPreExecute();
		}
		
		@Override
		protected Integer doInBackground(RequestParam... params) {
			
			RequestParam requestParam = params[0];
			String res = Request.request(requestParam.getJSON());
			if ("".equals(res)) {
				return ResponseParam.REQUEST_FAIL;
			}
			ResponseParam response = null;
			try {
				response = new ResponseParam(res);
			} catch (JSONException e) {
				System.out.println("发送私信异常==="+ e.toString());
				e.printStackTrace();
			}
			return response.getResult();
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			dialog.dismiss();
			super.onPostExecute(result);
			if(result == ResponseParam.RESULT_SUCCESS) {
				if(letter != null) {
					letter.onSendLetterSucc();
				}
				Toast.makeText(context, context.getText(R.string.send_letter_succ), Toast.LENGTH_SHORT).show();
			} else {
				if(letter != null) {
					letter.onSendLetterFail();
				}
				Toast.makeText(context, context.getText(R.string.send_letter_fail), Toast.LENGTH_SHORT).show();
			}
			
		}
	}
	
	