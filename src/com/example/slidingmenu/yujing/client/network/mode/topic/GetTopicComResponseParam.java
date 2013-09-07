package com.example.slidingmenu.yujing.client.network.mode.topic;

import com.example.slidingmenu.yujing.client.network.mode.ResponseParam;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GetTopicComResponseParam extends ResponseParam {

	public static final String Topic_Com_Content = "Topic_Com_Content";
	public static final String Topic_Com_Time = "Topic_Com_Time";
	public static final String Topic_Com_Photo = "Topic_Com_Photo";
	public static final String Topic_Com_ID = "Topic_Com_ID";
	public static final String Topic_Com_From = "Topic_Com_From";
	
	private JSONArray array;
	
	public GetTopicComResponseParam(String responseJson) throws JSONException {
		super(responseJson);
		if (super.getResult() == ResponseParam.RESULT_SUCCESS) {
			this.array = super.jsonObject.getJSONArray(ResponseParam.CONTENT);
		}
	}
	
	public List<HashMap<String, Object>> getAllTopicCom(){
		
		List<HashMap<String, Object>> list = new LinkedList<HashMap<String,Object>>();
		HashMap<String, Object> map = null;
		
		for(int i=0; i< array.length(); i++) {
			map = new HashMap<String, Object>();
			try {
				JSONObject object = array.getJSONObject(i);
				map.put(Topic_Com_ID, object.getLong("Topic_Com_ID"));
				map.put(Topic_Com_Content, object.getString("Topic_Com_Content"));
				map.put(Topic_Com_From, object.getLong("Topic_Com_From"));
				map.put(Topic_Com_Time, object.getInt("Topic_Com_Time"));
				map.put(Topic_Com_Photo, object.getString("Topic_Com_Photo"));
				list.add(map);
				map = null;
			} catch (JSONException e) {
				System.out.println("获得评论出错：===" + e.toString());
				e.printStackTrace();
			}
		}
		return list;
		
	}

}
