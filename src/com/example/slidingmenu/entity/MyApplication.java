package com.example.slidingmenu.entity;

import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application
{
	private List<Activity> activityList;
	private static MyApplication uniqueInstance = new MyApplication();

	private MyApplication()
	{
		activityList = new LinkedList<Activity>();
	}

	public static MyApplication getInstance()
	{
		return uniqueInstance;
	}

	public void addActivity(Activity activity)
	{
		activityList.add(activity);
	}
	
	public void exit()
	{		
		for (Activity activity : activityList)
		{
			activity.finish();
		}
		System.exit(0);
	}
}

