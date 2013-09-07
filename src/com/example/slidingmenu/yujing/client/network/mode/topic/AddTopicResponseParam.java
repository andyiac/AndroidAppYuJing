package com.example.slidingmenu.yujing.client.network.mode.topic;

import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;

import org.json.JSONArray;
import org.json.JSONException;


public class AddTopicResponseParam extends ResponseParam {

	private JSONArray array;

	public AddTopicResponseParam(String responseJson) throws JSONException {
		super(responseJson);
		// 对于成功返回的json字符串获取其返回内容（content）
		if (super.getResult() == ResponseParam.RESULT_SUCCESS) {
			this.array = super.jsonObject.getJSONArray(ResponseParam.CONTENT);
		}
	}

	/**
	 * 获得话题Id
	 * @return
	 */
	public long getTopicId() {
		try {
			return this.array.getLong( 0 );
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}