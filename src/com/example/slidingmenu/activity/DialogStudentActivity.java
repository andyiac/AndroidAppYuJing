package com.example.slidingmenu.activity;

import com.example.slidingmenu.R;
import com.example.slidingmenu.database.AttendanceHelper;
import com.example.slidingmenu.database.table.Student;
import com.example.slidingmenu.entity.MyConstant;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
	Button cancleBtn, sureBtn;
	TextView view;
	EditText edt1;
	EditText edt2;
	EditText edt3;
	EditText edt4;
	AttendanceHelper attendhelper;
	Cursor cursor;
	Bundle bundle;

	private int index;
	private String flagname;
	private int studentnum;

	private final int CLICK_SUREA = 0;
	private final int CLICK_CANCLEA = 1;
	private final int CLICK_SUREB = 2;
	private final int CLICK_CANCLEB = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		bundle = getIntent().getExtras();
		index = bundle.getInt(MyConstant.KEY_1);
		flagname = bundle.getString("flag");

		Log.v("mytag", "flagname====" + flagname);

		Log.e("mytag", "DialogStudentActivity_position====" + index);

		setContentView(R.layout.activity_dialog);

		attendhelper = new AttendanceHelper(this);
		view = (TextView) findViewById(R.id.dialog_change_name);

		edt1 = (EditText) findViewById(R.id.sno);
		edt2 = (EditText) findViewById(R.id.text1);
		edt3 = (EditText) findViewById(R.id.text2);
		edt4 = (EditText) findViewById(R.id.text3);

		sureBtn = (Button) findViewById(R.id.sureBtn0);
		cancleBtn = (Button) findViewById(R.id.cancleBtn0);

		sureBtn.setOnClickListener(this);
		cancleBtn.setOnClickListener(this);

		if (flagname.equals("1")) {
			inita();// 添加学生
		} else if (flagname.equals("2")) {
			initb();// 修改学生
		}

	}

	/*
	 * 添加
	 */
	private void inita() {
		view.setText("现在可以新添加一个学生！");
		sureBtn.setTag(CLICK_SUREA);// 0
		cancleBtn.setTag(CLICK_CANCLEA);// 1

	}

	/*
	 * 修改
	 */
	private void initb() {

		view.setText("修改");

		studentnum = bundle.getInt("student");
		cursor = Student.getStudentName(attendhelper, index, studentnum);
		Log.v("mytag",
				"sname====" + cursor.getString(cursor.getColumnIndex("sname")));
		Log.v("mytag",
				"num====" + cursor.getString(cursor.getColumnIndex("num")));
		Log.v("mytag",
				"grade====" + cursor.getString(cursor.getColumnIndex("grade")));
        String sno = cursor.getString(cursor.getColumnIndex("sno"));
		String sname = cursor.getString(cursor.getColumnIndex("sname"));
		String num = cursor.getString(cursor.getColumnIndex("num"));
		String grade = cursor.getString(cursor.getColumnIndex("grade"));
		
		edt1.setText(sno);
		edt2.setText(sname);
		edt3.setText(num);
		edt4.setText(grade);

		sureBtn.setTag(CLICK_SUREB);// 2
		cancleBtn.setTag(CLICK_CANCLEB);// 3

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = (Integer) v.getTag();
		switch (tag) {
		case 0: {

			if ("".equals(edt1.getText().toString().trim())) {
				Toast.makeText(this, "学号不能为空", Toast.LENGTH_SHORT).show();
			} else if ("".equals(edt2.getText().toString().trim())) {
				Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
			} else if ("".equals(edt3.getText().toString().trim())) {
				Toast.makeText(this, "电话号码不能为空", Toast.LENGTH_SHORT).show();
			} else if ("".equals(edt3.getText().toString().trim())) {
				Toast.makeText(this, "成绩不能为空", Toast.LENGTH_SHORT).show();

				/*
				 * if ("".equals(edt1.getText().toString()) ||
				 * "".equals(edt2.getText().toString())
				 * ||"".equals(edt3.getText().toString()) ||
				 * "".equals(edt4.getText().toString())) { Toast.makeText(this,
				 * "不能为空", Toast.LENGTH_SHORT).show();
				 */
			} else {
				
				String text1 = edt1.getText().toString();
				String text2 = edt2.getText().toString();
				String text3 = edt3.getText().toString();
				String text4 = edt4.getText().toString();

				Student.insertStudent(attendhelper, index, text1, text2, text3,
						text4, "未出勤");
                  
				  Intent intent = new Intent(DialogStudentActivity.this,
				  Addstudents.class);
				  setResult(0, intent);
				  Log.e("mytag","DialogStudent111----success-----Addstudent");  
				  this.finish();
			}

		}
		case 1: {
			// 清空编辑框
			edt1.setText("");
			edt2.setText("");
			edt3.setText("");
			edt4.setText("");
			Intent intent = new Intent(DialogStudentActivity.this,
					Addstudents.class);
			setResult(0, intent);
			Log.e("mytag","DialogStudent111----filed-----Addstudent");
		    this.finish(); 

		}

		case 2: {
			
			String sno = edt1.getText().toString();
			String name = edt2.getText().toString();
			String num = edt3.getText().toString();
			String grade = edt4.getText().toString();
			
			Student.updateStudent(attendhelper, index, studentnum, sno, name, num,
					grade);

			Intent intent = new Intent(DialogStudentActivity.this,
					Addstudents.class);
			setResult(0, intent);
			Log.e("mytag","DialogStudent222-----success----Addstudent");
			this.finish();
		}
		case 3: {
			Intent intent = new Intent(DialogStudentActivity.this,
					Addstudents.class);
			setResult(0, intent);
			Log.e("mytag","DialogStudent222-----filed----Addstudent");
            this.finish();

		}

		}
	}
}
