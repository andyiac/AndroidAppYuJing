package com.example.slidingmenu.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.slidingmenu.R;
import com.example.slidingmenu.database.AttendanceHelper;
import com.example.slidingmenu.database.table.Mclass;
import com.example.slidingmenu.database.table.Student;
import com.example.slidingmenu.entity.MyConstant;
import com.example.slidingmenu.entity.MyData;
import com.example.slidingmenu.entity.MyStudent;
//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Addstudents extends Activity {
	private ListView listView;
	private Button mybutton, back;
	private int index;
	AttendanceHelper attendhelper;
	Cursor cursor;
	SimpleCursorAdapter mAdapter=null;
	String[] from;
	int[] to;
	

	
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
				Log.e("mytag", "Addstudents_position11====" + index);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initListView() {

		Bundle bundle = getIntent().getExtras();// 获得传输的数据班级
		index = bundle.getInt(MyConstant.KEY_1);
		Log.e("mytag", "Addstudents_position===" + index);
		/*cursor = attendenHelper.getStudents(index);*/
		attendhelper = new AttendanceHelper(this);
        listView = (ListView) this.findViewById(R.id.list);
        
        Cursor cursor = Student.getStudentName(attendhelper, index);
		Log.e("mytag", cursor.getColumnName(1).toString());
		
		from = new String[] {"sname","num","grade"};
		to = new int[]{R.id.stuname,R.id.stunum,R.id.stuscore};
		
		mAdapter = new SimpleCursorAdapter(this,R.layout.item_student,cursor,from,to,2);
	   
		listView.setAdapter(mAdapter);
		/*listView.setOnItemLongClickListener(new ItemLongClickListener());*/

	}

	/*
	 * longclick
	 
	class ItemLongClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			// TODO Auto-generated method stub
			MyStudent mystudent = (MyStudent) listItemAdapter.getItem(position);
			showclasstDialog(mystudent, position);
			return false;
		}


		private void showclasstDialog(MyStudent mystudent, int position) {
			// TODO Auto-generated method stub
			switch (position) {
			
			}

		}
	}*/

}

