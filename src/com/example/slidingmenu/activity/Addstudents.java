package com.example.slidingmenu.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.slidingmenu.R;
//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Addstudents extends Activity {
	private ListView listView;
	private Button mybutton,back;
	private StudentBaseAdapter listItemAdapter;

	private List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> list2 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> list3 = new ArrayList<Map<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);
		list1 = initDada();

		initListView();
		addDada2ListView();

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

				Intent intent = new Intent();
				intent.setClass(Addstudents.this, DialogStudentActivity.class);
				startActivity(intent);

			}
		});

	}

	private List<Map<String, String>> initDada() {

		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("name", "lucy");
		map1.put("phone", "15369332099");
		map1.put("myclass", "信工一班");

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("name", "Amy");
		map2.put("phone", "15369332099");
		map2.put("myclass", "信工二班");

		list1.add(map2);
		list1.add(map1);
		return list1;

	}

	private void addDada2ListView() {
		SharedPreferences share = getSharedPreferences("studentdata", 0);
		String data1 = share.getString("name", "无");
		String data2 = share.getString("phone", "无");
		String data3 = share.getString("myclass", "无");
		System.out.println("data1=======" + data1 + "data2=======" + data2
				+ "data3=======" + data3);
		Map<String, String> maplast = new HashMap<String, String>();
		maplast.put("name", data1);
		maplast.put("phone", data2);
		maplast.put("myclass", data3);

		list1.add(maplast);
		listItemAdapter.notifyDataSetChanged();

	}

	private void initListView() {

		listView = (ListView) this.findViewById(R.id.list);
		listItemAdapter = new StudentBaseAdapter(this, list1);
		listView.setAdapter(listItemAdapter);

	}

	class StudentBaseAdapter extends BaseAdapter {

		Context context;
		List<Map<String, String>> list;

		public StudentBaseAdapter(Context context) {
			super();
			this.context = context;
		}

		public StudentBaseAdapter(Context context,
				List<Map<String, String>> list) {
			super();
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			System.out.println(list.size() + "=========大小");

			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			LayoutInflater mInflater = LayoutInflater.from(this.context);
			convertView = mInflater.inflate(R.layout.item_student, null);
			EditText et1 = (EditText) convertView.findViewById(R.id.et1);
			EditText et2 = (EditText) convertView.findViewById(R.id.et2);
			EditText et3 = (EditText) convertView.findViewById(R.id.et3);

			et1.setText(list.get(position).get("name"));

			et2.setText(list.get(position).get("phone"));

			et3.setText(list.get(position).get("myclass"));

			return convertView;
		}

	}

}
