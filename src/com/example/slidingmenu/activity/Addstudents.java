package com.example.slidingmenu.activity;



import com.example.slidingmenu.R;
import com.example.slidingmenu.database.AttendanceHelper;
import com.example.slidingmenu.database.table.Student;
import com.example.slidingmenu.entity.MyConstant;


//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;



public class Addstudents extends Activity {

	private ListView listView;
	private Button mybutton, back;
	private int index;
	AttendanceHelper attendhelper;
	Cursor cursor;
	SimpleCursorAdapter mAdapter = null;
	String[] from;
	int[] to;
	String no;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.addstudent);

		initListView();

		mybutton = (Button) this.findViewById(R.id.btn_add);

		back = (Button) this.findViewById(R.id.btn_add_student);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				Addstudents.this.finish();
			}
		});

		mybutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(Addstudents.this,
						DialogStudentActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt(MyConstant.KEY_1, index);
				bundle.putString("flag", "1");
				Log.e("mytag", "Addstudents_class====" + index);
				intent.putExtras(bundle);
				startActivityForResult(intent, 0);

			}
		});

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initListView() {

		Bundle bundle = getIntent().getExtras();// 获得传输的数据班级
		index = bundle.getInt(MyConstant.KEY_1);
		Log.e("mytag", "Addstudents_position===" + index);

		attendhelper = new AttendanceHelper(this);
		listView = (ListView) this.findViewById(R.id.list);

	    cursor = Student.getAllStudentName(attendhelper, index);

		from = new String[] { "sno", "sname", "grade" };
		to = new int[] { R.id.name, R.id.text, R.id.score };

		mAdapter = new SimpleCursorAdapter(this, R.layout.search_name, cursor,
				from, to, 2);

		listView.setAdapter(mAdapter);
		
		mAdapter.notifyDataSetChanged();
		
		listView.setOnItemLongClickListener(new ItemLongClickListener()); 

	}

	
	  
	 class ItemLongClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			/*TextView tview = (TextView)view.findViewById( R.id.stuname); 
			no = tview.getText().toString();*/
			int idt = (int)id;
			Log.v("mytag", "student+idt====" + idt);
			showManageDialog(manage, idt);
			return true;
		}
		
		private String[] manage = new String[] { "修改", "请假", "删除" };

		private void showManageDialog(final String[] arg, final int id) {
			new AlertDialog.Builder(Addstudents.this).setTitle("提示")
					.setItems(arg, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							switch (which) {
							case 0:// 修改

								Intent intent2 = new Intent(Addstudents.this,
										DialogStudentActivity.class);
								Bundle bundle = new Bundle();
								bundle.putInt(MyConstant.KEY_1, index);
								bundle.putInt("student",id);
								Log.e("mytag", "student=====" + id);
								bundle.putString("flag", "2");
								intent2.putExtras(bundle);
								startActivityForResult(intent2,0);
								break;
							case 1://请假
								int grade = Integer.parseInt(Student.getStudentGrade(attendhelper, index, id)) + 1;
								Log.e("mytag", "qingjia======"+ grade);
								Student.updateStudentAttend(attendhelper, index, id, "出勤");
								Student.updateStudentGrade(attendhelper, index, id, grade + "");
								cursor = Student.getAllStudentName(attendhelper, index);
								mAdapter.changeCursor(cursor);
								mAdapter.notifyDataSetChanged();
								break;
							case 2:// 删除
								showDelete(id);
								break;
							}
						}
					}).show();
		}

		// 删除
		private void showDelete(final int id) {

			new AlertDialog.Builder(Addstudents.this)
					.setTitle("提示")
					.setMessage("确定删除?")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							
							Student.deleteStudent(attendhelper, index, id);
							cursor = Student.getAllStudentName(attendhelper, index);
							mAdapter.changeCursor(cursor);
							mAdapter.notifyDataSetChanged();
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						}
					}).show();
		}
	  
	 }
	 public boolean onCreateOptionsMenu(Menu menu) {
			super.onCreateOptionsMenu(menu);
			menu.add(0, 1, 0, "导入");
			menu.add(0, 2, 0, "导出 ");

			return true;
		}
	 public boolean onOptionsItemSelected(MenuItem item) {
		 
			switch (item.getItemId()) {
			case 1:
				Intent intenta = new Intent(Addstudents.this,
						ImportExcelActivity.class);
				Bundle bundlea = new Bundle();
				bundlea.putInt(MyConstant.KEY_1, index);
				Log.e("mytag", "classname=====" + index);
				intenta.putExtras(bundlea);
				startActivityForResult(intenta,0);
				return true;
			case 2:
				Intent intentb = new Intent(Addstudents.this,
						ExportExcelActivity.class);
				Bundle bundleb = new Bundle();
				bundleb.putInt(MyConstant.KEY_1, index);
				Log.e("mytag", "classname=====" + index);
				intentb.putExtras(bundleb);
				startActivity(intentb);
                return true;
          }
			return false;
		}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		cursor = Student.getAllStudentName(attendhelper, index);
		mAdapter.changeCursor(cursor);
		mAdapter.notifyDataSetChanged();
		Log.e("mytag","AddStudent111---------Addstudent");  
	}
	 
}
