package com.example.slidingmenu.activity;

import com.example.slidingmenu.R;
import com.example.slidingmenu.database.AttendanceHelper;
import com.example.slidingmenu.database.table.Student;
import com.example.slidingmenu.entity.DateManager;
import com.example.slidingmenu.entity.MyConstant;
import com.example.slidingmenu.entity.MyData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DialogStudentActivity extends Activity implements OnClickListener {
	Button but1;
	Button but2;
	EditText edt1;
	EditText edt2;
	EditText edt3;
	AttendanceHelper attendhelper;
	/* private SharedPreferences sp_data; */

	private int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Bundle bundle = getIntent().getExtras();
		index = bundle.getInt(MyConstant.KEY_1);

		Log.e("mytag", "DialogStudentActivity_position====" + index);

		setContentView(R.layout.activity_dialog);
		TextView view = (TextView) findViewById(R.id.dialog_change_name);
		view.setText("现在可以新添加一个学生！");
		attendhelper = new AttendanceHelper(this);

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

			if ("".equals(edt1.getText().toString().trim())) {
				Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
			} else if ("".equals(edt2.getText().toString().trim())) {
				Toast.makeText(this, "电话号码不能为空", Toast.LENGTH_SHORT).show();
			} else if ("".equals(edt3.getText().toString().trim())) {
				Toast.makeText(this, "成绩不能为空", Toast.LENGTH_SHORT).show();
			} else {
				String text1 = edt1.getText().toString();
				String text2 = edt2.getText().toString();
				String text3 = edt3.getText().toString();
				
				Student.insertStudent(attendhelper, index,text1, text2, Integer.parseInt(text3),"未出勤");
				
				Intent intent = new Intent(DialogStudentActivity.this,
						Addstudents.class);
				Bundle bundle = new Bundle();
				bundle.putInt(MyConstant.KEY_1, index);
				intent.putExtras(bundle);
				startActivity(intent);
				DialogStudentActivity.this.finish();
			}

		}

	}

	/*
	 * private void remember(String text1, String text2, String text3) {
	 * 
	 * sp_data = getSharedPreferences("studentdata", Context.MODE_PRIVATE);
	 * Editor editor = sp_data.edit();
	 * 
	 * editor.putString("name", text1); editor.putString("phone", text2);
	 * editor.putString("myclass", text3); editor.commit();
	 * 
	 * }
	 */
}
