package com.example.slidingmenu.yujing.client.network.mode.friend;

import com.example.slidingmenu.yujing.client.database.table.Friend;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 解析 GetDeleteFriends、GetAllFriends、GetNewFriends请求的返回数据
 * 
 */
public class GetPeopleResponseParam extends ResponseParam {

	private JSONArray array;

	public GetPeopleResponseParam(String responseJson) throws JSONException {
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

	public List<? extends Map<String, Object>> getAllPeople() {

		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> values = null;

		for (int i = 0; i < array.length(); i++) {
			values = new HashMap<String, Object>();
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
				System.out.println("获得好友出错：===" + e.toString());
				e.printStackTrace();
			}
		}
		return list;

	}


}
