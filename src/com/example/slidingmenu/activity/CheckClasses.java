package com.example.slidingmenu.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.slidingmenu.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CheckClasses extends Activity implements OnClickListener {
	private ListView ls;
	private EditText edtext;
	private Button mybutton;
	private MyAdapter myAdapter;
	List<String> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_myclasses);

		edtext = (EditText) this.findViewById(R.id.edt);
		mybutton = (Button) this.findViewById(R.id.button);

		data = new ArrayList<String>();
		data.add("信工一班");
		data.add("信工二班");
		data.add("信工三班");
		data.add("信工四班");

		initView(ls);

		mybutton.setOnClickListener(this);

	}

	private void initView(View view) {
		ls = (ListView) this.findViewById(R.id.lv_attendence_yujing_bfxy_frag1);
		myAdapter =new MyAdapter(CheckClasses.this, data);

		ls.setAdapter(myAdapter);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String text = edtext.getText().toString();

		if (text != "" && !text.equals(null)) {
			data.add(text);
			myAdapter.notifyDataSetChanged();
		

		}

	}

	class MyAdapter extends BaseAdapter  {

		private Context context;
		private List<String> listofclass;

		public MyAdapter() {
			super();

		}

		public MyAdapter(Context context, List<String> listofclass) {
			super();
			this.context = context;
			this.listofclass = listofclass;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listofclass.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LinearLayout layout = new LinearLayout(CheckClasses.this);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			LayoutInflater vi = (LayoutInflater) CheckClasses.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = vi.inflate(R.layout.list_item_classes, layout, true);

			TextView tv = (TextView) convertView.findViewById(R.id.tv);
			ImageView img = (ImageView) convertView.findViewById(R.id.img);

			img.setImageResource(R.drawable.delete);
			tv.setText(listofclass.get(position));
			img.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					data.remove(position);
					myAdapter.notifyDataSetChanged();
					
				}
			});

			return convertView;
		}

	

	}

}
