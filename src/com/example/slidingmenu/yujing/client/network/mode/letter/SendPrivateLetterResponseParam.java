package com.example.slidingmenu.yujing.client.network.mode.letter;

import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * 解析SendPrivateLetter的请求数据
 *
 */
public class SendPrivateLetterResponseParam extends ResponseParam {

	private JSONArray array;

	public SendPrivateLetterResponseParam(String responseJson) throws JSONException {
		super(responseJson);
		// 对于成功返回的json字符串获取其返回内容（content）
		if (super.getResult() == ResponseParam.RESULT_SUCCESS) {
			this.array = super.jsonObject.getJSONArray(ResponseParam.CONTENT);
		}
	}

	/**
	 * 获得私信Id
	 * @return
	 */
	public long getPrivateletterId() {
		try {
			return this.array.getLong( 0 );
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
}