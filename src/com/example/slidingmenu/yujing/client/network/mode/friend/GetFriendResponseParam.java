package com.example.slidingmenu.yujing.client.network.mode.friend;

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


/**
 * 解析 GetDeleteFriends、GetAllFriends、GetNewFriends请求的返回数据
 * 
 */
public class GetFriendResponseParam extends MsgResponseParam {

	private JSONArray array;

	public GetFriendResponseParam(String responseJson) throws JSONException {
		super(responseJson);
		// 对于成功返回的json字符串获取其返回内容（content）
		if (getResult() == ResponseParam.RESULT_SUCCESS) {
			System.out.println(responseJson);
			try {
				this.array = super.jsonObject.getJSONArray(ResponseParam.CONTENT);
			} catch (Exception e) {
				System.out.println("解析出错");
				e.printStackTrace();
			}
		}
	}

	public List<ContentValues> getAllFriend() {

		List<ContentValues> list = new LinkedList<ContentValues>();
		ContentValues values = null;

		for (int i = 0; i < array.length(); i++) {
			values = new ContentValues();
			try {
				JSONObject object = array.getJSONObject(i);
				values.put(Friend.ID, object.getLong("personMobile"));
				values.put(Friend.UID, object.getLong("UID"));
				values.put(Friend.name, object.getString("personName"));
				values.put(Friend.sex, object.getString("personSex"));
				values.put(Friend.mobile, object.getString("personMobile"));
				values.put(Friend.address, object.getString("personAddress"));
				values.put(Friend.photo, object.getString("personPhoto"));
				list.add(values);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	@Override
	public int dealNewMessage(List<ContentValues> list, DatabaseHelper helper) {
		if (list!=null && list.size() > 0) {
			for (ContentValues values : list) {
				Friend.insertFriend(helper, values);
			}
			return list.size();
		}
		return 0;
	}

	@Override
	public List<ContentValues> getNewMessage() {
		
		return getAllFriend();
	}


}
