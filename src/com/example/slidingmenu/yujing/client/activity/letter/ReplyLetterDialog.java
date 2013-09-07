package com.example.slidingmenu.yujing.client.activity.letter;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import com.example.slidingmenu.yujing.client.database.table.PrivateLetter;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;


public class ReplyLetterDialog extends Dialog implements View.OnClickListener{

	private Window mWindow;

	private Button cancel, send;
	private EditText mEditText;
	
	private ContentValues mValues;

	private SendLetterTask mSendLetterTask;
	
	private OnReplyLetterListener mReplyLetterListener;
	
	public ReplyLetterDialog(Context context) {
		super(context);
		// 获得窗口管理对象
		mWindow = this.getWindow();
		// 去掉系统默认的对话框背景
		mWindow.setBackgroundDrawable(new ColorDrawable(0));
		// 获得窗口参数
		WindowManager.LayoutParams lp = mWindow.getAttributes();
		// 设置对话框显示在屏幕的顶部，默认是现实在屏幕的中心
		lp.gravity = Gravity.TOP;
		// 设置点击对话框外部可以回收对话框
		setCanceledOnTouchOutside(true);
		// 去掉对话框的标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置对话框的内容
		mWindow.setContentView(R.layout.reply_letter_dialog);
	}

	// 下面是几个生命周期函数
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cancel = (Button) findViewById(R.id.close_dialog);
		cancel.setOnClickListener(this);
		send = (Button) findViewById(R.id.send_content);
		send.setOnClickListener(this);
		mEditText = (EditText)findViewById(R.id.edit_broadcast);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_content:
			onSendClick(v);
			break;
		case R.id.close_dialog:
			onCancelClick(v);
			break;
		default:
			break;
		}
		
	}
	
	public void onSendClick(View v) {
		String contents = mEditText.getText().toString();
		if(TextUtils.isEmpty(contents)) {
			mEditText.setError(v.getContext().getString(R.string.empty_content));
			return;
		}
		
		ContentValues values = getValues();
		final String name = values.getAsString(PrivateLetter.name);
		final String time = String.valueOf((int) (System.currentTimeMillis()/1000));
		final String contact = values.getAsString(PrivateLetter.PrivateLetterUID);
		
		String[] letter = new String[] {
				contact,
				contents,
				time,
				name,
				"baidu.com"
		};
		
		RequestParam rp = new RequestParam();
		SharedPreferences sp = ((ClientApplication)this.getOwnerActivity().getApplication()).getLoginUserInfo();
		rp.setUserName(sp.getString(RequestParam.USER_NAME, ""));
		rp.setPassword(sp.getString(RequestParam.PASSWORD, ""));
		rp.setRandomKey("1234");
		rp.setRequestType(RequestParam.SEND_PRIVATELETTER);
		rp.setParams(letter);
		
		mSendLetterTask = new SendLetterTask(this.getOwnerActivity(), new SendLetterTask.HandleSendLetter() {
			
			@Override
			public void onSendLetterSucc() {
				if(mReplyLetterListener != null) {
					mReplyLetterListener.onSendLetterSucc();
				}
			}
			
			@Override
			public void onSendLetterFail() {
				if(mReplyLetterListener != null) {
					mReplyLetterListener.onSendLetterFail();
				}
			}
		});
		mSendLetterTask.execute(rp);
		dismiss();
	}
	
	public void onCancelClick(View v) {
		dismiss();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	public void setOnReplyLetterListener(OnReplyLetterListener rllistener) {
		mReplyLetterListener = rllistener;
	}
	
	public interface OnReplyLetterListener{
		public void onSendLetterSucc();
		public void onSendLetterFail();
	}
	
	/**
	 * @param mValues the mValues to set
	 */
	public void setValues(ContentValues mValues) {
		this.mValues = mValues;
	}

	/**
	 * @return the mValues
	 */
	public ContentValues getValues() {
		return mValues;
	}

	

}