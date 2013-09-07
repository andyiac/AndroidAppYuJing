package com.example.slidingmenu.yujing.client.network.mode.friend;

import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;

import org.json.JSONArray;
import org.json.JSONException;


public class AddFriendResponseParam extends ResponseParam {

	private JSONArray array;

	public AddFriendResponseParam(String responseJson) throws JSONException {
		super(responseJson);
		// 对于成功返回的json字符串获取其返回内容（content）
		if (super.getResult() == ResponseParam.RESULT_SUCCESS) {
			this.array = super.jsonObject.getJSONArray(ResponseParam.CONTENT);
		}
	}	
}