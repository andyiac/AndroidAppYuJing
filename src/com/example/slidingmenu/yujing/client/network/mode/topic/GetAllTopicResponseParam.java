package com.example.slidingmenu.yujing.client.network.mode.topic;

import com.example.slidingmenu.yujing.client.database.table.Topic;
import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GetAllTopicResponseParam extends ResponseParam {

	private JSONArray array;
	
	public GetAllTopicResponseParam(String responseJson) throws JSONException {
		super(responseJson);
		if (super.getResult() == ResponseParam.RESULT_SUCCESS) {
			this.array = super.jsonObject.getJSONArray(ResponseParam.CONTENT);
		}
	}
	
	public List<HashMap<String, Object>> getAllTopic(){
		
		List<HashMap<String, Object>> list = new LinkedList<HashMap<String,Object>>();
		HashMap<String, Object> map = null;
		
		for(int i=0; i< array.length(); i++) {
			map = new HashMap<String, Object>();
			try {
				JSONObject object = array.getJSONObject(i);
				map.put(Topic.ID, object.getLong("topicID"));
				map.put(Topic.UID, object.getLong("topicUID"));
				map.put(Topic.name, object.getString("topicName"));
				map.put(Topic.content, object.getString("topicContent"));
				map.put(Topic.time, object.getInt("topicTime"));
				map.put(Topic.photo, object.getString("topicPhoto"));
				list.add(map);
				map = null;
			} catch (JSONException e) {
				System.out.println("获得私信内容出错：===" + e.toString());
				e.printStackTrace();
			}
		}
		return list;
		
	}

}
