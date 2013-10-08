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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.slidingmenu.R;
import com.example.slidingmenu.activity.Addstudents;
import com.example.slidingmenu.activity.CheckClasses;

import java.util.ArrayList;
import java.util.List;

public class AttendancePiceFragment2 extends Fragment {

	ListView listView;



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

    private void initView(View view){
        listView = (ListView)view.findViewById(R.id.lv_attendance);
        listView.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_expandable_list_item_1,getData()));
        listView.setOnItemClickListener(listener);
    }
    private List<String> getData(){
        List<String> data = new ArrayList<String>();
        data.add("班级");
        data.add("添加学生");
        data.add("开始点名");
//        data.add("返回");
        return data;
    }

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            switch (i){
                case 0:
                    // 查看班级
			intent.setClass(getActivity(), CheckClasses.class);

			startActivity(intent);
                    break;
                case 1:
                    // 添加学生
			intent.setClass(getActivity(), Addstudents.class);
			startActivity(intent);
                    break;
                case 2:
                    //开始点名
                    break;
                case 3:
                    //
                    break;
            }

        }
    };
}
