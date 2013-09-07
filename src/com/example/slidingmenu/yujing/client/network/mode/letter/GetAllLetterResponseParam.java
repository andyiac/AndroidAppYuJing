package com.example.slidingmenu.yujing.client.network.mode.letter;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;

import com.example.slidingmenu.yujing.client.database.DatabaseHelper;
import com.example.slidingmenu.yujing.client.database.table.PrivateLetter;
import com.example.slidingmenu.yujing.client.network.mode.MsgResponseParam;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;


public class GetAllLetterResponseParam extends MsgResponseParam {

	private JSONArray array;
	
	public GetAllLetterResponseParam(String responseJson) throws JSONException {
		super(responseJson);
		if (super.getResult() == ResponseParam.RESULT_SUCCESS) {
			this.array = super.jsonObject.getJSONArray(ResponseParam.CONTENT);
		}
	}
	
	public List<ContentValues> getAllLetter(){
		
		List<ContentValues> list = new LinkedList<ContentValues>();
		ContentValues values = null;
		
		for(int i=0; i< array.length(); i++) {
			values = new ContentValues();
			try {
				JSONObject object = array.getJSONObject(i);
				values.put(PrivateLetter.PrivateLetterID, object.getLong("privateLetterID"));
				values.put(PrivateLetter.UID, object.getLong("UID"));
				values.put(PrivateLetter.name, object.getString("privateLetterName"));
				values.put(PrivateLetter.content, object.getString("privateLetterContent"));
				values.put(PrivateLetter.time, object.getInt("privateLetterTime"));
				values.put(PrivateLetter.photo, object.getString("privateLetterPhoto"));
				values.put(PrivateLetter.PrivateLetterUID, object.getLong("privateLetterUID"));
				values.put(PrivateLetter.isSend, object.getInt("privateLetterIsSend"));
				list.add(values);
			} catch (JSONException e) {
				System.out.println("获得私信内容出错：===" + e.toString());
				e.printStackTrace();
			}
		}
		return list;
		
	}

	@Override
	public int dealNewMessage(List<ContentValues> list, DatabaseHelper helper) {
		if (list!=null && list.size() > 0) {
			for(ContentValues values : list){
				PrivateLetter.insertPrivateLetter(helper, values);
			}
			return list.size();
		}
		return 0;
	}

	@Override
	public List<ContentValues> getNewMessage() {
		return getAllLetter();
	}

}
