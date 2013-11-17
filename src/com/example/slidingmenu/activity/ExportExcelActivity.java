package com.example.slidingmenu.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import com.example.slidingmenu.R;
import com.example.slidingmenu.database.AttendanceHelper;
import com.example.slidingmenu.database.table.Mclass;
import com.example.slidingmenu.database.table.Student;
import com.example.slidingmenu.entity.MyConstant;
import com.example.slidingmenu.entity.MyStudent;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ExportExcelActivity extends Activity {
	// ** Called when the activity is first created. *//*
	public String FILE_PATH = " ";
	public String FILE_NAME = "";
	public static final String END_TAG = "导出完毕！！，可以退出本应用~~";
	WritableWorkbook wwb;
	WritableSheet ws;
	TextView tv;
	TextView tip;
	Button btn;
	EditText fileNameEdit;
	ProgressBar bar;
	Handler handler;
	int index;
	AttendanceHelper attendhelper;
	MyStudent student;
	int id;
	String className;
	String studentNo;
	String studentName;
	String score;

	HashMap<String, MyStudent> map = new HashMap<String, MyStudent>();
	HashMap<String, String> map_new = new HashMap<String, String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.export);

		Bundle bundle = getIntent().getExtras();
		index = bundle.getInt(MyConstant.KEY_1);
		attendhelper = new AttendanceHelper(this);
		initView();
		handler = new Handler() {
			public void handleMessage(Message msg) {
				String content = (String) msg.obj;
				int added = msg.arg1;
				tv.setText(content);
				bar.setProgress(added);
				if (content.equals(END_TAG)) {
					bar.setVisibility(View.INVISIBLE);
					tip.setVisibility(View.INVISIBLE);
					tv.append("\n本次导出记录，" + String.valueOf(added) + "条");
				}
			}
		};
	}

	public void initView() {

		tv = (TextView) findViewById(R.id.tv);
		tip = (TextView) findViewById(R.id.tip);
		bar = (ProgressBar) findViewById(R.id.bar);
		tip.setVisibility(View.INVISIBLE);
		bar.setVisibility(View.INVISIBLE);
		fileNameEdit = (EditText) findViewById(R.id.nameEdit);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				map.clear();
				map_new.clear();
				FILE_NAME = fileNameEdit.getText().toString();
				FILE_PATH = getSDPath();
				if (initXls()) {
					/*bar.setMax(map.size());*/
					tip.setVisibility(View.VISIBLE);
					bar.setVisibility(View.VISIBLE);
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							add();
						}
					});
					t.start();
				}
			}
		});
	}

	/**
	 * 
	 * @return
	 */
	public boolean initXls() {
		/*
		 * if (FILE_PATH.trim().equals("")) { FILE_PATH =
		 * Environment.getExternalStorageDirectory() .getAbsolutePath(); }
		 */
		if (!FILE_NAME.endsWith(".xls") && !FILE_NAME.contains(".")) {
			FILE_NAME = FILE_NAME + ".xls";
		}
		String path = FILE_PATH + "/" + FILE_NAME.trim();
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
				Log.i("file", file + "");
			}
			wwb = Workbook.createWorkbook(file);
			ws = wwb.createSheet("联系人表", 0);

			SQLiteDatabase db = attendhelper.getReadableDatabase();
			Cursor cursor = db.query("student", null, "cid = ?",
					new String[] { index + "" }, null, null, "sno"
							+ " COLLATE LOCALIZED ASC");
			Log.e("mytag", "cursorgetcount=========" + cursor.getCount());

			/* Cursor cursor = Student.getAllStudentName(attendhelper, index); */

			// 循环遍历
			if (cursor.moveToFirst()) {
				do {
					className = Mclass.getClassName(attendhelper, index);
					id = cursor.getInt(cursor.getColumnIndex("_id"));
					studentNo = cursor.getString(cursor.getColumnIndex("sno"));
					studentName = cursor.getString(cursor
							.getColumnIndex("sname"));
					score = cursor.getString(cursor.getColumnIndex("grade"));
					student = new MyStudent(id);
					student.setClassName(className);
					student.setStudentNo(studentNo);
					student.setStudentName(studentName);
					student.setScore(score);
					Log.e("mytag", "getstudentid=====" + student.getId());
					Log.e("mytag", "getclassname=====" + student.getClassName());
					Log.e("mytag", "getstudentNo=====" + student.getStudentNo());
					Log.e("mytag",
							"getstudentname=====" + student.getStudentName());
					Log.e("mytag", "getsScore=====" + student.getScore());
					map.put(id+"", student);
				} while (cursor.moveToNext());
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(getBaseContext(), "路径设置错误", Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
		}
		return false;
	}

	public void add() {
		try {
			Iterator<Entry<String, MyStudent>> iter = map.entrySet().iterator();
			Label labelC0 = new Label(0, 0, "班级");
			Label labelC1 = new Label(1, 0, "学号");
			Label labelC2 = new Label(2, 0, "姓名");
			Label labelC3 = new Label(3, 0, "考勤成绩");
			ws.addCell(labelC0);
			ws.addCell(labelC1);
			ws.addCell(labelC2);
			ws.addCell(labelC3);
			int row = 1;
			while (iter != null && iter.hasNext()) {
				Entry<String, MyStudent> entry = iter.next();
				String name = entry.getKey();
				MyStudent student = entry.getValue();
				System.out.println(entry.getKey() + entry.getValue());

				/*
				 * if (map_new.containsKey(student.getStudentNo())){ continue; }
				 * else { map_new.put(student.getStudentNo() , " "); }
				 */
				if (name != "") {
					Label labelCname = new Label(0, row, student.getClassName());
					Label labelSno = new Label(1, row, student.getStudentNo());
					Label labelSname = new Label(2, row,
							student.getStudentName());
					Label labelScore = new Label(3, row, student.getScore()); // 住宅号码
					ws.addCell(labelCname);
					ws.addCell(labelSno);
					ws.addCell(labelSname);
					ws.addCell(labelScore);
					Message msg = new Message();
					msg.obj = name;
					msg.arg1 = row;
					handler.sendMessage(msg);
					row++;

				}
			}
			wwb.write();
			wwb.close();

			Message msg = new Message();
			msg.obj = END_TAG;
			msg.arg1 = row - 1;
			handler.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();

	}

}