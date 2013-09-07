package com.example.slidingmenu.yujing.client.network.mode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginoutResponseParam extends ResponseParam {

	private JSONObject object;
	private JSONArray array;
	
	public LoginoutResponseParam(String responseJson) throws JSONException {
		super(responseJson);
		if (super.getResult() == ResponseParam.RESULT_SUCCESS) {
			this.array = super.jsonObject.getJSONArray(ResponseParam.CONTENT);
			this.object = this.array.getJSONObject(0);
		}
	}

	public String getPersonName() {
		try {
			return (String) this.object.get("personName");
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getPersonAddress() {
		try {
			return (String) this.object.get("personAddress");
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getPersonSex() {
		try {
			return (String) this.object.get("personSex");
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getPersonMobile() {
		try {
			return (String) this.object.get("personMobile");
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getPersonPhoto() {
		try {
			return (String) this.object.get("personPhoto");
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}
}
