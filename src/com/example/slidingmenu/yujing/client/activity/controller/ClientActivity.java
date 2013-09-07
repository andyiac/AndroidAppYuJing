package com.example.slidingmenu.yujing.client.activity.controller;

import java.util.LinkedList;
import java.util.List;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

import com.example.slidingmenu.R;
import com.example.slidingmenu.yujing.client.activity.broadcast.BroadCastActivity;
import com.example.slidingmenu.yujing.client.activity.friends.FriendsActivity;
import com.example.slidingmenu.yujing.client.activity.letter.LetterActivity;
import com.example.slidingmenu.yujing.client.service.MsgRefresh;
import com.example.slidingmenu.yujing.client.service.MsgService;


public class ClientActivity extends TabActivity implements MsgRefresh, OnTabChangeListener{
  
	private static final String BROADCAST_ACTIVITY_TAG = "0";
	private static final String FRIENDS_ACTIVITY_TAG = "1";
	private static final String LETTER_ACTIVITY_TAG = "2";
	
	private Animation slideLeftIn;
	private Animation slideLeftOut;
	private Animation slideRightIn;
	private Animation slideRightOut;
	
	private Intent broadcastIntent;
	private Intent friendsIntent;
	private Intent letterIntent;
	
	private TabHost mTabHost;
	
	private GestureDetector gestureDetector;
	
	private List<ImageView> msgCount;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        
        setContentView(R.layout.main);
        mTabHost = this.getTabHost();
        
        msgCount = new LinkedList<ImageView>();

        slideLeftIn   = AnimationUtils.loadAnimation(this,R.anim.slide_left_in);
		slideLeftOut  = AnimationUtils.loadAnimation(this,R.anim.slide_left_out);
		slideRightIn  = AnimationUtils.loadAnimation(this,R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        
        broadcastIntent = new Intent(this, BroadCastActivity.class);
        friendsIntent = new Intent(this, FriendsActivity.class);
        letterIntent = new Intent(this, LetterActivity.class);
        
        this.setDefaultTab(BROADCAST_ACTIVITY_TAG);
        
        this.addTabSpec(BROADCAST_ACTIVITY_TAG, 
        		broadcastIntent,
        		R.drawable.tab_broadcast_btn, 
        		getString(R.string.broadcast_activity));
        this.addTabSpec(FRIENDS_ACTIVITY_TAG, 
        		friendsIntent,
        		R.drawable.tab_friendst_btn, 
        		getString(R.string.friends_activity));
        this.addTabSpec(LETTER_ACTIVITY_TAG, 
        		letterIntent,
        		R.drawable.tab_letter_btn, 
        		getString(R.string.letter_activity));
       
        
        gestureDetector = new GestureDetector(new TabHostTouch());
		new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		};
		
		init(); 
    }
    
    private void addTabSpec(String tag, Intent intentContent, int imageDrawable, String text){
    	LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View view = inflater.inflate(R.layout.tab_widget_item, null);
    	ImageView tabImage = (ImageView) view.findViewById(R.id.tab_image);
    	TextView  tabText  = (TextView) view.findViewById(R.id.tab_text);
    	ImageView tabCount = (ImageView) view.findViewById(R.id.count);

    	tabImage.setImageResource(imageDrawable);
    	tabText.setText(text);
    	
    	TabHost.TabSpec letter = this.getTabHost().newTabSpec(tag);
    	letter.setContent(intentContent);
    	letter.setIndicator(view);
    	
    	mTabHost.addTab(letter);
    	msgCount.add(tabCount);
    }                                                                                                                                                                                                                                                                                                                                                                                                                                                                
    
    @Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		if (gestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);

	}
    
	private void scrollTo(int index) {
		int mCurrentTabID = mTabHost.getCurrentTab();
		View currentView = mTabHost.getCurrentView();
		int tabCount = getTabWidget().getChildCount();
		if (null != currentView) {
			if (mCurrentTabID == (tabCount - 1) && index == 0) {
				currentView.startAnimation(slideLeftOut);
			} else if (mCurrentTabID == 0 && index == (tabCount - 1)) {
				currentView.startAnimation(slideRightOut);
			} else if (index > mCurrentTabID) {
				currentView.startAnimation(slideLeftOut);
			} else if (index < mCurrentTabID) {
				currentView.startAnimation(slideRightOut);
			}
		}
		mTabHost.setCurrentTab(index);
		currentView = mTabHost.getCurrentView();
		if (mCurrentTabID == (tabCount - 1) && index == 0) {
			currentView.startAnimation(slideLeftIn);
		} else if (mCurrentTabID == 0 && index == (tabCount - 1)) {
			currentView.startAnimation(slideRightIn);
		} else if (index > mCurrentTabID) {
			currentView.startAnimation(slideLeftIn);
		} else if (index < mCurrentTabID) {
			currentView.startAnimation(slideRightIn);
		}
	}
    
    
    private class TabHostTouch extends SimpleOnGestureListener {

		private static final int ON_TOUCH_DISTANCE = 80;
		private int currentTabID = 0;
		private int tabCount = getTabWidget().getChildCount();
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			
			if(e1.getY() - e2.getY() >= 50 || e1.getY() - e2.getY() <= -50) {
				return false;
			}
			
			if (e1.getX() - e2.getX() <= (-ON_TOUCH_DISTANCE)) {
				currentTabID = mTabHost.getCurrentTab() - 1;
				if (currentTabID < 0) {
					currentTabID = tabCount - 1;
				}
			} else if (e1.getX() - e2.getX() >= ON_TOUCH_DISTANCE) {
				currentTabID = mTabHost.getCurrentTab() + 1;
				if (currentTabID >= tabCount) {
					currentTabID = 0;
				}
			}
			ClientActivity.this.scrollTo(currentTabID);
			return true;
		}
	}


	@Override
	public void init() {
		this.startService(new Intent(this, MsgService.class));
		MsgService.acList.add(this);
		mTabHost.setOnTabChangedListener(this);
	}

	@Override
	public void refresh(int what, Object... objects) {
		int[] count = (int[]) objects[0];
		for(int i = 0; i< count.length; i++) {
			if(count[i] == 0) {
				continue;
			}
			msgCount.get(i).setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onTabChanged(String tabId) {
		int id = Integer.parseInt(tabId);
		if(msgCount.get(id).getVisibility() == View.VISIBLE) {
			msgCount.get(id).setVisibility(View.GONE);
		}
	}

}