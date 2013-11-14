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

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.slidingmenu.R;
import com.example.slidingmenu.activity.DialogClassActivity;
import com.example.slidingmenu.activity.ManageActivity;
import com.example.slidingmenu.database.AttendanceHelper;
import com.example.slidingmenu.database.table.Mclass;
import com.example.slidingmenu.database.table.Student;
import com.example.slidingmenu.entity.MyConstant;

public class AttendancePiceFragment2 extends Fragment {

	ListView listView;
	Button button;
	EditText editclass;
	SimpleCursorAdapter mAdapter = null;

	private LayoutInflater mInflater;
	AttendanceHelper attendhelper;
	String[] from;
	int[] to;
	Cursor cursor;
	String classname;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.attendance_yujing_pice_frag_2,
				null);

		initView(view);

		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initView(View view) {

		attendhelper = new AttendanceHelper(this.getActivity());
		listView = (ListView) view.findViewById(R.id.lv_attendance);
		editclass = (EditText) view.findViewById(R.id.edtclass);
		button = (Button) view.findViewById(R.id.button);

		cursor = Mclass.getAllClassName(attendhelper);
		Log.e("mytag", cursor.getColumnName(1).toString());

		from = new String[] { "cname" };
		to = new int[] { R.id.classname };

		mAdapter = new SimpleCursorAdapter(this.getActivity(),
				R.layout.attendance_pice_frag_class, cursor, from, to, 2);

		listView.setAdapter(mAdapter);
	
		mAdapter.notifyDataSetChanged();

		listView.setOnItemClickListener(new ClassItemListener());
		listView.setOnItemLongClickListener(new ItemLongClickListener());
		button.setOnClickListener(new ButtonListener());

	}

	/*
	 * Sure to save class listener
	 */
	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if ("".equals(editclass.getText().toString().trim())) {
				Toast.makeText(getActivity(), "班级不能为空", Toast.LENGTH_SHORT)
						.show();
			} else {
				String className = editclass.getText().toString();

				Mclass.insertClass(attendhelper, className);
                editclass.setText("");
               
                cursor = Mclass.getAllClassName(attendhelper);
                mAdapter.changeCursor(cursor);
                mAdapter.notifyDataSetChanged();
			}
		}

	}

	public class ClassItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int i, long id) {
			// TODO Auto-generated method stub
			
			Intent intenta = new Intent(getActivity(), ManageActivity.class);
			Bundle bundlea = new Bundle();
		    int cid = (int)id;
			/*TextView tview = (TextView) arg1.findViewById(R.id.classname);*/
			bundlea.putInt(MyConstant.KEY_1, cid);
			Log.v("mytag", "cid=====" + cid);
			intenta.putExtras(bundlea);
			startActivity(intenta);
		}

	}

	public class ItemLongClickListener implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position, long id) {
			// TODO Auto-generated method stub
			int idt = (int) id;
		/*	TextView tview = (TextView) arg1.findViewById(R.id.classname);
			classname = tview.getText().toString();*/
			Log.e("mytag", "idt====="+ idt);
			showManageDialog(manage, idt);
			return true;
		}

	}

	private String[] manage = new String[] { "修改", "删除" };

	private void showManageDialog(final String[] arg, final int idt) {
		new AlertDialog.Builder(this.getActivity()).setTitle("提示")
				.setItems(arg, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						switch (which) {
						case 0:// 修改

							Intent intent2 = new Intent(getActivity(),
									DialogClassActivity.class);
							Bundle bundle = new Bundle();
							bundle.putInt(MyConstant.KEY_1, idt);
							intent2.putExtras(bundle);
							startActivity(intent2);
							break;

						case 1:// 删除
							showDelete(idt);
							break;
						}
					}
				}).show();
	}

	// 删除
	private void showDelete(final int id) {

		new AlertDialog.Builder(this.getActivity())
				.setTitle("提示")
				.setMessage("确定删除?")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Log.v("",""+_id);
						Mclass.deleteClass(attendhelper, id);
						Student.deleteAllStudent(attendhelper, id);
						cursor = Mclass.getAllClassName(attendhelper);
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
