package com.example.slidingmenu.yujing.client.service;

import java.util.List;


import android.content.ContentValues;

import com.example.slidingmenu.yujing.client.database.DatabaseHelper;

public interface NewMessage {
	
	public List<ContentValues> getNewMessage();
	public int dealNewMessage(List<ContentValues> list, DatabaseHelper helper);
	
}
