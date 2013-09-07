package com.example.slidingmenu.yujing.client.network.mode.personinfo;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;

import com.example.slidingmenu.yujing.client.database.DatabaseHelper;
import com.example.slidingmenu.yujing.client.database.table.Friend;
import com.example.slidingmenu.yujing.client.network.mode.MsgResponseParam;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;


public class GetPersonInfoResponseParam extends MsgResponseParam {

	private JSONArray array;

	public GetPersonInfoResponseParam(String responseJson) throws JSONException {
		super(responseJson);
		// 对于成功返回的json字符串获取其返回内容（content）
		if (super.getResult() == ResponseParam.RESULT_SUCCESS) {
			this.array = super.jsonObject.getJSONArray(ResponseParam.CONTENT);
		}
	}

	public List<ContentValues> getPersonInfo() {

		List<ContentValues> list = new LinkedList<ContentValues>();
		ContentValues values = null;

		for (int i = 0; i < array.length(); i++) {
			values = new ContentValues();
			try {
				JSONObject object = array.getJSONObject(i);
				values.put(Friend.ID, object.getLong("personMobile"));
				values.put(Friend.name, object.getString("personName"));
				values.put(Friend.sex, object.getString("personSex"));
				values.put(Friend.mobile, object.getString("personMobile"));
				values.put(Friend.photo, object.getString("personPhoto"));
				list.add(values);
			} catch (JSONException e) {
				System.out.println("获得私信内容出错：===" + e.toString());
				e.printStackTrace();
			}
		}
		return list;

	}
	
	public List<ContentValues> getPersonState() {

		List<ContentValues> list = new LinkedList<ContentValues>();
		ContentValues values = null;

		for (int i = 0; i < array.length(); i++) {
			values = new ContentValues();
			try {
				JSONObject object = array.getJSONObject(i);
				values.put(Friend.state, object.getInt("personStatus"));
				values.put(Friend.ID, object.getLong("UID"));				
				list.add(values);
			} catch (JSONException e) {
				System.out.println("获得好友状态出错：===" + e.toString());
				e.printStackTrace();
			}
		}
		return list;

	}

	@Override
	public int dealNewMessage(List<ContentValues> list, DatabaseHelper helper) {
		if (list != null && list.size() > 0) {
			for(ContentValues values : list){
				Friend.updataFriendState(helper, values, values.getAsLong(Friend.ID));
			}
			return list.size();
		}
		return 0;
	}

	@Override
	public List<ContentValues> getNewMessage() {
		
		return getPersonState();
	}
}