package com.example.slidingmenu.activity;

import java.util.ArrayList;
import java.util.List;
import com.example.slidingmenu.R;
import com.example.slidingmenu.entity.MyConstant;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ManageActivity extends Activity {

	private ListView listView;
	private Button back;
	private int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.check_myclasses);

		back = (Button) this.findViewById(R.id.btn_check_myclasses);

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				ManageActivity.this.finish();
			}
		});

		// index is classname
		Bundle bundle = getIntent().getExtras();
		index = bundle.getInt(MyConstant.KEY_1);
		Log.e("mytag", "ManageActivity_class=====" + index);

		initView(listView);

	}

	private void initView(View view) {
		listView = (ListView) this
				.findViewById(R.id.lv_attendence_yujing_bfxy_frag1);
		listView.setAdapter(new ArrayAdapter<String>(ManageActivity.this,
				android.R.layout.simple_expandable_list_item_1, getData()));
		listView.setOnItemClickListener(listener);
	}

	private List<String> getData() {
		List<String> data = new ArrayList<String>();

		data.add("添加学生");
		data.add("开始点名");
		return data;
	}

	private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long l) {
			switch (position) {
			case 0:
				// Add student
				Intent intenta = new Intent(ManageActivity.this,
						Addstudents.class);
				Bundle bundlea = new Bundle();
				bundlea.putInt(MyConstant.KEY_1, index);
				Log.e("mytag", "classNameManage=====" + index);
				intenta.putExtras(bundlea);
				startActivity(intenta);
				break;
			case 1:
				// Going to call the roll
				Intent intentb = new Intent(ManageActivity.this,
						CallNameActivity.class);
				Bundle bundleb = new Bundle();
				bundleb.putInt(MyConstant.KEY_1, index);
				Log.e("mytag", "classNameManage=====" + index);
				intentb.putExtras(bundleb);
				startActivity(intentb);
				break;
			}
		}
	};
}
