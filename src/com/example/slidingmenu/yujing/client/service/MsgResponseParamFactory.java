package com.example.slidingmenu.yujing.client.service;

import com.example.slidingmenu.yujing.client.network.mode.MsgResponseParam;
import com.example.slidingmenu.yujing.client.network.mode.RequestParam;
import com.example.slidingmenu.yujing.client.network.mode.friend.GetFriendResponseParam;
import com.example.slidingmenu.yujing.client.network.mode.letter.GetAllLetterResponseParam;
import com.example.slidingmenu.yujing.client.network.mode.personinfo.GetPersonInfoResponseParam;
import com.example.slidingmenu.yujing.client.network.mode.topic.TopicResponseParam;

import org.json.JSONException;


public class MsgResponseParamFactory {

	public static MsgResponseParam getMsgResponseParam(String type, String res) {
		
		if(type.equals(RequestParam.GET_PERSON_STATE)) {
			try {
				return new GetPersonInfoResponseParam(res);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		if(type.equals(RequestParam.GET_NEW_TOPIC)) {
			try {
				return new TopicResponseParam(res);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		if(type.equals(RequestParam.GET_NEW_FRIENDS)) {
			try {
				return new GetFriendResponseParam(res);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		if(type.equals(RequestParam.GET_NEW_PRIVATELETTER)) {
			try {
				return new GetAllLetterResponseParam(res);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return null;
		
	}
	
}
