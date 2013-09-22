package com.example.slidingmenu.activity;

import com.example.slidingmenu.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogStudentActivity extends Activity implements OnClickListener {
	Button but1;
	Button but2;
	EditText edt1;
	EditText edt2;
	EditText edt3;
	private SharedPreferences sp_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_dialog);
		TextView view = (TextView) findViewById(R.id.dialog_change_name);
		view.setText("现在可以新添加一个学生！");

		edt1 = (EditText) findViewById(R.id.text1);
		edt2 = (EditText) findViewById(R.id.text2);
		edt3 = (EditText) findViewById(R.id.text3);
		// 取消
		but1 = (Button) findViewById(R.id.exitBtn1);
		// 确定
		but2 = (Button) findViewById(R.id.exitBtn0);
		but1.setOnClickListener(this);
		but2.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == but1) {
			// 清空编辑框
			edt1.setText("");
			edt2.setText("");
			edt3.setText("");
			this.finish();

		} else if (v == but2) {
			String text1 = edt1.getText().toString();
			String text2 = edt2.getText().toString();
			String text3 = edt3.getText().toString();
			if (text1 != "" && text2 != "" && text3 != "") {
				remember(text1, text2, text3);
				Intent intent = new Intent();
				intent.setClass(this, Addstudents.class);
				startActivity(intent);

			   DialogStudentActivity.this.finish();
			}

		}

	}

	private void remember(String text1, String text2, String text3) {

		sp_data = getSharedPreferences("studentdata", Context.MODE_PRIVATE);
		Editor editor = sp_data.edit();

		editor.putString("name", text1);
		editor.putString("phone", text2);
		editor.putString("myclass", text3);
		editor.commit();

	}

}
