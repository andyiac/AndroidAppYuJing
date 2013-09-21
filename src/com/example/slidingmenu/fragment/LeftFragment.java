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
package com.example.slidingmenu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.slidingmenu.R;
import com.example.slidingmenu.activity.AttendanceActivity;
import com.example.slidingmenu.activity.ConfigActivity;
import com.example.slidingmenu.activity.ContactActivity;
import com.example.slidingmenu.activity.EmergencyActivity;
import com.example.slidingmenu.activity.MyMsgActivity;
import com.example.slidingmenu.activity.NewsYuJingActivity;
import com.example.slidingmenu.activity.PersonalCenterActivity;
import com.example.slidingmenu.activity.SlidingActivity;

public class LeftFragment extends Fragment {

    TextView news,tuFa, kaoQin, mMessage, mContact, personal_center, mConfig;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left, null);

        initView(view);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void initView(View view) {


        news = (TextView) view.findViewById(R.id.tv_left_yu_jing);
        tuFa = (TextView) view.findViewById(R.id.tv_left_tu_fa);
        kaoQin = (TextView) view.findViewById(R.id.tv_left_kao_qin);
        mMessage = (TextView) view.findViewById(R.id.tv_left_xiao_xi);
        mContact = (TextView) view.findViewById(R.id.tv_left_contact);
        personal_center = (TextView) view.findViewById(R.id.tv_left_person_center);
        mConfig = (TextView) view.findViewById(R.id.tv_left_config);

        news.setOnClickListener(listener);
        tuFa.setOnClickListener(listener);
        kaoQin.setOnClickListener(listener);
        mMessage.setOnClickListener(listener);
        mContact.setOnClickListener(listener);
        personal_center.setOnClickListener(listener);
        mConfig.setOnClickListener(listener);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent();
            switch (view.getId()) {


                case R.id.tv_left_yu_jing:
                    intent.setClass(getActivity(), NewsYuJingActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                    break;
                case R.id.tv_left_tu_fa:
                    intent.setClass(getActivity(), EmergencyActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                    break;
                case R.id.tv_left_kao_qin:
                    intent.setClass(getActivity(), AttendanceActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                    break;
                case R.id.tv_left_xiao_xi:
                    intent.setClass(getActivity(), MyMsgActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                    break;
                case R.id.tv_left_contact:
                    intent.setClass(getActivity(), ContactActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                    break;
                case R.id.tv_left_person_center:
                    intent.setClass(getActivity(), PersonalCenterActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                    break;
                case R.id.tv_left_config:
                    intent.setClass(getActivity(), ConfigActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                    break;
            }


        }
    };

}
