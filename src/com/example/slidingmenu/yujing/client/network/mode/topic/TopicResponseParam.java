package com.example.slidingmenu.yujing.client.network.mode.topic;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;

import com.example.slidingmenu.yujing.client.database.DatabaseHelper;
import com.example.slidingmenu.yujing.client.database.table.Topic;
import com.example.slidingmenu.yujing.client.network.mode.MsgResponseParam;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;

/**
 * 解析 GetNewTopic、GetAllTopic请求的返回数据
 * 
 */

public class TopicResponseParam extends MsgResponseParam {

	private JSONArray array;

	public TopicResponseParam(String responseJson) throws JSONException {
		super(responseJson);
		// 对于成功返回的json字符串获取其返回内容（content）
		if (super.getResult() == ResponseParam.RESULT_SUCCESS) {
			this.array = super.jsonObject.getJSONArray(ResponseParam.CONTENT);
		}
	}

	public List<ContentValues> getAllTopic() {

		List<ContentValues> list = new LinkedList<ContentValues>();
		ContentValues values = null;

		for (int i = 0; i < array.length(); i++) {
			values = new ContentValues();
			try {
				JSONObject object = array.getJSONObject(i);
				values.put(Topic.ID, object.getLong("topicID"));
				values.put(Topic.UID, object.getLong("topicUID"));
				values.put(Topic.name, object.getString("topicName"));
				values.put(Topic.content, object.getString("topicContent"));
				values.put(Topic.time, object.getInt("topicTime"));
				values.put(Topic.photo, object.getString("topicPhoto"));
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
		if (list != null && list.size() > 0) {
			for(ContentValues values : list){
				Topic.insertTopic(helper, values);							
			}
			return list.size();
		}
		return 0;
	}

	@Override
	public List<ContentValues> getNewMessage() {
		return getAllTopic();
	}

}