package com.example.slidingmenu.yujing.client.network.mode;

import java.util.List;

import org.json.JSONException;

import android.content.ContentValues;

import com.example.slidingmenu.yujing.client.database.DatabaseHelper;
import com.example.slidingmenu.yujing.client.service.NewMessage;


public class MsgResponseParam extends ResponseParam implements NewMessage {

	public MsgResponseParam(String responseJson) throws JSONException {
		super(responseJson);
		
	}


	@Override
	public int dealNewMessage(List<ContentValues> list, DatabaseHelper helper) {
		
		return 0;
	}


	@Override
	public List<ContentValues> getNewMessage() {
		
		return null;
	}

}