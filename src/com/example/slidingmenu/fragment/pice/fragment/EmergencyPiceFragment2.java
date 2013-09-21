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
import android.widget.TextView;

import com.example.slidingmenu.R;

import java.util.ArrayList;
import java.util.List;


public class EmergencyPiceFragment2 extends Fragment {

    ListView listView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emergency_pice_frag_1, null);

        init(view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void init(View view) {
        TextView textView = (TextView) view.findViewById(R.id.tv_emergency_title);
        textView.setText("可行方案二");

        listView = (ListView) view.findViewById(R.id.lv_emergency);
        listView.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_expandable_list_item_1, getData()));


    }

    private List<String> getData() {
        List<String> data = new ArrayList<String>();
        data.add("快速定位");
        data.add("呼救110");
        data.add("呼救120");
        data.add("联系家长");

        data.add("联系班长");
        data.add("联系保卫处");
        data.add("联系导员");
        data.add("联系学院");
        return data;
    }

}

