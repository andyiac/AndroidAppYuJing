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
package com.example.slidingmenu.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.example.slidingmenu.R;
import com.example.slidingmenu.fragment.ConfigMainFragment;
import com.example.slidingmenu.fragment.LeftFragment;
import com.example.slidingmenu.fragment.RightFragment;
import com.example.slidingmenu.fragment.ViewPageFragment;
import com.example.slidingmenu.fragment.ViewPageFragment.MyPageChangeListener;
import com.example.slidingmenu.view.SlidingMenu;

public class ConfigActivity extends FragmentActivity {
	SlidingMenu mSlidingMenu;
	LeftFragment leftFragment;
	RightFragment rightFragment;
	ConfigMainFragment configMainFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
		init();
		initListener();

	}

	private void init() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(
				R.layout.left_frame, null));
		mSlidingMenu.setRightView(getLayoutInflater().inflate(
				R.layout.right_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(
				R.layout.center_frame, null));

		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		leftFragment = new LeftFragment();
		t.replace(R.id.left_frame, leftFragment);

		rightFragment = new RightFragment();
		t.replace(R.id.right_frame, rightFragment);

		configMainFragment = new ConfigMainFragment();
		t.replace(R.id.center_frame, configMainFragment);
		t.commit();
	}

	private void initListener() {
		configMainFragment.setMyPageChangeListener(new ConfigMainFragment.MyPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (configMainFragment.isFirst()) {
                    mSlidingMenu.setCanSliding(true, false);
                } else if (configMainFragment.isEnd()) {
                    mSlidingMenu.setCanSliding(false, true);
                } else {
                    mSlidingMenu.setCanSliding(false, false);
                }
            }
        });
	}

	public void showLeft() {
		mSlidingMenu.showLeftView();
	}

	public void showRight() {
		mSlidingMenu.showRightView();
	}

}
