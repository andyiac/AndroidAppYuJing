/*
 * Copyright (C) 2012 yueyueniao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.slidingmenu.fragment.pice.fragment;

//import java.awt.Image;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.example.slidingmenu.R;
import com.example.slidingmenu.activity.ManageActivity;
import com.example.slidingmenu.entity.DateManager;
import com.example.slidingmenu.entity.MyConstant;
import com.example.slidingmenu.entity.MyData;

import java.sql.Date;
import java.util.List;

public class AttendancePiceFragment2 extends Fragment {

	ListView listView;
	BluetoothAdapter adapter;
	Button button;
	EditText editclass;
	List<String> data;
	ClassBaseAdapter myAdapter;
	private LayoutInflater mInflater;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.attendance_yujing_pice_frag_2,
				null);
		/* loadDate(); */
		initView(view);

		return view;
	}

	/**
	 * Loading Data
	 */
	/*
	 * private void loadDate() { DateManager.getInstance().load(getActivity());
	 * }
	 */

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void initView(View view) {
		listView = (ListView) view.findViewById(R.id.lv_attendance);
		editclass = (EditText) view.findViewById(R.id.edtclass);
		button = (Button) view.findViewById(R.id.button);
		myAdapter = new ClassBaseAdapter(this.getActivity());
		listView.setAdapter(myAdapter);
		button.setOnClickListener(new ButtonListener());
	}

	/*
	 * Sure to save class listener
	 */
	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String className = editclass.getText().toString();
			MyData.getInstance().addClass(className);
			DateManager.getInstance().save(getActivity());
			myAdapter.notifyDataSetChanged();
		}

	}

	class ClassBaseAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private Context context;

		public ClassBaseAdapter(Context context) {
			this.context = context;
			mInflater = LayoutInflater.from(this.context);
		}

		@Override
		public int getCount() {
			return MyData.getInstance().getClassList().size();
		}

		@Override
		public Object getItem(int position) {
			return MyData.getInstance().getClassList().get(position);
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

				convertView = mInflater.inflate(
						R.layout.attendance_pice_frag_class, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView
						.findViewById(R.id.classname);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Log.e("mytag", "getName()====="
					+ MyData.getInstance().getClassList().get(position)
							.getName());
			holder.name.setText(MyData.getInstance().getClassList()
					.get(position).getName());
			holder.name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showActivity(position);
				}
			});
			return convertView;
		}

		/*
		 * position is classname
		 */
		private void showActivity(int position) {
			Intent intent = new Intent(context, ManageActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt(MyConstant.KEY_1, position);
			Log.e("mytag", "AttendancePiceFragment2_position===" + position);
			intent.putExtras(bundle);
			context.startActivity(intent);
		}
	}

	private class ViewHolder {
		TextView name;
	}

}
