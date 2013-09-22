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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.slidingmenu.R;
import com.example.slidingmenu.map.RapidPositioning;
import com.example.slidingmenu.map.wmap.MapMainActivity;

import java.util.ArrayList;
import java.util.List;


public class EmergencyPiceFragment1 extends Fragment {

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

        listView = (ListView) view.findViewById(R.id.lv_emergency);
        listView.setAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_expandable_list_item_1, getData()));
        listView.setOnItemClickListener(onItemClickListener);
    }

    private List<String> getData() {
        List<String> data = new ArrayList<String>();
        data.add("快速定位");
        data.add("紧急呼救");
        data.add("一键广播");
        data.add("联系班长");
        data.add("联系保卫处");
        data.add("联系导员");
        data.add("联系学院");
        data.add("帮助说明");
        return data;
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            switch (i) {
                case 0:
//                  Toast.makeText(EmergencyPiceFragment1.this.getActivity(),"快速定位",Toast.LENGTH_LONG).show();
                    intent.setClass(EmergencyPiceFragment1.this.getActivity(), MapMainActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    intent=new Intent("android.intent.action.CALL", Uri.parse("tel:" + 10001));
                    startActivity(intent);
                    break;
                case 2:
                    intent=new Intent("android.intent.action.CALL", Uri.parse("tel:" + 10001));
                    startActivity(intent);
                    break;
                case 3:
                    intent=new Intent("android.intent.action.CALL", Uri.parse("tel:" + 10001));
                    startActivity(intent);
                    break;
                case 4:
                    intent=new Intent("android.intent.action.CALL", Uri.parse("tel:" + 10001));
                    startActivity(intent);
                    break;
                case 5:
                    intent=new Intent("android.intent.action.CALL", Uri.parse("tel:" + 10001));
                    startActivity(intent);
                    break;
                case 6:
                    intent=new Intent("android.intent.action.CALL", Uri.parse("tel:" + 10001));
                    startActivity(intent);
                    break;
            }
        }
    };

}

