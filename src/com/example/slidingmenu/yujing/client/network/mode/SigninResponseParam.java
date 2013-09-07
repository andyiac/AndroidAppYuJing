package com.example.slidingmenu.yujing.client.network.mode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SigninResponseParam extends ResponseParam {

	private JSONObject content;
	
	private JSONArray cardArray;
	
	
	public SigninResponseParam(String responseJson) throws JSONException {
		super( responseJson );
		if( super.getResult() == ResponseParam.RESULT_SUCCESS ) {
			
			this.content = new JSONObject(super.getContent());
			this.cardArray = this.content.getJSONArray( ResponseParam.CONTENT );
		}
	}

}