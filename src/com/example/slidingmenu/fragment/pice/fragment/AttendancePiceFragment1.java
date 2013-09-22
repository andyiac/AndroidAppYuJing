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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.slidingmenu.R;
import com.example.slidingmenu.activity.Addstudents;
import com.example.slidingmenu.activity.CheckClasses;

public class AttendancePiceFragment1 extends Fragment implements
		OnClickListener {

	private ImageView img_class;
	private ImageView img_addStudent;
	private ImageView img_startname;
	private ImageView img_exit;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.attendance_yujing_pice_frag_1,
				null);

		img_class = (ImageView) view.findViewById(R.id.myclass);
		img_addStudent = (ImageView) view.findViewById(R.id.addStudent);
		img_startname = (ImageView) view.findViewById(R.id.startName);
		img_exit = (ImageView) view.findViewById(R.id.exit);

		img_class.setOnClickListener(this);
		img_addStudent.setOnClickListener(this);
		img_startname.setOnClickListener(this);
		img_exit.setOnClickListener(this);

		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		if (v == img_class) {
			// 查看班级
			intent.setClass(getActivity(), CheckClasses.class);

			startActivity(intent);

		} else if (v == img_addStudent) {

			// 添加学生
			intent.setClass(getActivity(), Addstudents.class);
			startActivity(intent);

		} else if (v == img_startname) {
			// 开始点名
		} else if (v == img_exit) {
			
			getActivity().finish();
		}

	}

}
