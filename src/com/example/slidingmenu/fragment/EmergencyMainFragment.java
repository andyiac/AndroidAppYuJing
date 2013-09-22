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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.slidingmenu.R;
import com.example.slidingmenu.activity.EmergencyActivity;
import com.example.slidingmenu.fragment.pice.fragment.EmergencyPiceFragment1;
//import com.example.slidingmenu.fragment.pice.fragment.EmergencyPiceFragment2;

import java.util.ArrayList;

public class EmergencyMainFragment extends Fragment {

	private Button showLeft;
	private Button showRight;
	private MyAdapter mAdapter;
	private ViewPager mPager;
	private ArrayList<Fragment> pagerItemList = new ArrayList<Fragment>();
    private TextView tvTilte;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.view_pager, null);
		showLeft = (Button) mView.findViewById(R.id.showLeft);
		showRight = (Button) mView.findViewById(R.id.showRight);
		mPager = (ViewPager) mView.findViewById(R.id.pager);
        EmergencyPiceFragment1 page1 = new EmergencyPiceFragment1();
//        EmergencyPiceFragment2 page2 = new EmergencyPiceFragment2();
		PageFragment2 page2 = new PageFragment2();
		pagerItemList.add(page1);
		pagerItemList.add(page2);
		mAdapter = new MyAdapter(getFragmentManager());
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				if (myPageChangeListener != null)
					myPageChangeListener.onPageSelected(position);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				

			}

			@Override
			public void onPageScrollStateChanged(int position) {

				

			}
		});

	    initTitle(mView);
		return mView;
    }

    private void initTitle(View view){

        tvTilte = (TextView)view.findViewById(R.id.tv_yujing_center);
        tvTilte.setText("突发事件");


    }
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		showLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((EmergencyActivity) getActivity()).showLeft();
			}
		});

		showRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((EmergencyActivity) getActivity()).showRight();
			}
		});
	}

	public boolean isFirst() {
		if (mPager.getCurrentItem() == 0)
			return true;
		else
			return false;
	}

	public boolean isEnd() {
		if (mPager.getCurrentItem() == pagerItemList.size() - 1)
			return true;
		else
			return false;
	}

	public class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return pagerItemList.size();
		}

		@Override
		public Fragment getItem(int position) {

			Fragment fragment = null;
			if (position < pagerItemList.size())
				fragment = pagerItemList.get(position);
			else
				fragment = pagerItemList.get(0);

			return fragment;

		}
	}

	private MyPageChangeListener myPageChangeListener;

	public void setMyPageChangeListener(MyPageChangeListener l) {

		myPageChangeListener = l;

	}

	public interface MyPageChangeListener {
		public void onPageSelected(int position);
	}

}
