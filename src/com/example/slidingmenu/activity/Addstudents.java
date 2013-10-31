package com.example.slidingmenu.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.slidingmenu.R;
import com.example.slidingmenu.entity.MyConstant;
import com.example.slidingmenu.entity.MyData;
//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Addstudents extends Activity {
	private ListView listView;
	private Button mybutton, back;
	private StudentBaseAdapter listItemAdapter;

	/*private List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();*/

	private int index; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.addstudent);
		

		initListView();
	/*	addDada2ListView();*/

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
				
				Intent intent = new Intent (Addstudents.this, DialogStudentActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt(MyConstant.KEY_1, index);
				Log.e("mytag", "Addstudents_position11===="+ index);
				intent.putExtras(bundle);	
				startActivity(intent);

			}
		});

	}

	private void initListView() {

		Bundle bundle = getIntent().getExtras();// 获得传输的数据班级
		index = bundle.getInt(MyConstant.KEY_1);
		Log.e("mytag", "Addstudents_position==="+ index);
        
		listView = (ListView) this.findViewById(R.id.list);
		listItemAdapter = new StudentBaseAdapter(this, index);
		listView.setAdapter(listItemAdapter);

	}
	
	class StudentBaseAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context context;
		private int index;

		public StudentBaseAdapter(Context context, int index) {
			this.context = context;
			mInflater = LayoutInflater.from(this.context);
			this.index = index;
		}

		@Override
		public int getCount() {
			return MyData.getInstance().getClassList().get(index)
					.getStudentList().size();
		}

		@Override
		public Object getItem(int position) {
			return MyData.getInstance().getClassList().get(index)
					.getStudentList().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_student, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.stuname);
				holder.num = (TextView) convertView.findViewById(R.id.stunum);
				holder.score = (TextView) convertView.findViewById(R.id.stuscore);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Log.e("mytag","getStudentName()=====" + MyData.getInstance().getClassList().get(index).getStudentList().get(position).getName());
			holder.name.setText(MyData.getInstance().getClassList().get(index)
					.getStudentList().get(position).getName());
			holder.num.setText(MyData.getInstance().getClassList().get(index)
					.getStudentList().get(position).getNum());
			holder.score.setText(MyData.getInstance().getClassList().get(index)
					.getStudentList().get(position).getScore()
					+ "");
			/*holder.btn1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 修改姓名
					showTextEdit(position);
				}
			});
			holder.btn2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 修改分数
					showScoreEdit(position);
				}
			});
			holder.btn3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 修改号码
					showPhoneEdit(position);
				}
			});
			holder.btn4.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 发送消息
					sms(position);
				}
			});*/
			return convertView;
		}

		/*private void showTextEdit(int position) {
			Intent intent = new Intent(context, DialogStudentActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt(MyConstant.KEY_1, index);
			bundle.putInt(MyConstant.KEY_2, position);
			intent.putExtras(bundle);
			context.startActivity(intent);
		}*/

		/*private void showScoreEdit(int position) {
			Intent intent = new Intent(context, DialogNumActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt(MyConstant.KEY_1, index);
			bundle.putInt(MyConstant.KEY_2, position);
			bundle.putString(MyConstant.KEY_3, "score");
			intent.putExtras(bundle);
			context.startActivity(intent);
		}

		private void showPhoneEdit(int position) {
			Intent intent = new Intent(context, DialogNumActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt(MyConstant.KEY_1, index);
			bundle.putInt(MyConstant.KEY_2, position);
			bundle.putString(MyConstant.KEY_3, "phone");
			intent.putExtras(bundle);
			context.startActivity(intent);
		}

		private void sms(int position) {
			Uri uri = Uri.parse("smsto:"
					+ MyDate.getInstance().getClassList().get(index)
							.getStudentList().get(position).getNum());
			Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
			intent.putExtra("sms_body", "快点来上课吧！");
			context.startActivity(intent);
		}
*/
		private class ViewHolder {
			TextView name;
			TextView score;
			TextView num;
		}

	}

}
