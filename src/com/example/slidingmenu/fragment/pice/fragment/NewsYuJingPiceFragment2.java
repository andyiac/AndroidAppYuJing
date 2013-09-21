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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.slidingmenu.R;

import java.util.ArrayList;
import java.util.List;


public class NewsYuJingPiceFragment2 extends Fragment {

    ListView listView ;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.news_yujing_pice_frag_2, null);
        initView(view);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

    private void initView(View view){

        listView= (ListView) view.findViewById(R.id.lv_news_yujing_xgxy_frag2);
        listView.setAdapter(new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_expandable_list_item_1,getData()));


    }

    private List<String> getData(){
        List<String> data = new ArrayList<String>();
        data.add("我校13级新生军训圆满结束...");
        data.add("法政学院与关心下一代委员...");
        data.add("我校对师生开展消防安全知...");
        data.add("张家口市社科联领导莅临我...");
        data.add("我校召开党外人士中秋座谈...");
        data.add("我校2013级新生开学典礼暨...");
        data.add("我校2013级新生开学典礼暨...");
        data.add("我校2013级新生开学典礼暨...");
        data.add("我校2013级新生开学典礼暨...");
        data.add("我校2013级新生开学典礼暨...");
        data.add("我校2013级新生开学典礼暨...");
        return data;
    }

}

