package com.example.slidingmenu.network;

import android.util.Log;
import com.example.slidingmenu.yujing.client.application.ClientApplication;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;


public class Request {
	
	private static final int CONNECTTIMEOUT = 30000;
	
	private static final int READTIMEOUT = 10000;

    private static final boolean D =true;

    private static final String  TAG = "Request.java";
	/**
	 * 访问服务器<br/>
	 * 没有做是否联网的判断。但是可以用于wifi、wap、net
	 * @param json - 手机客户端请求json
	 * @return ""表示访问失败，成功则返回对应的json字符串
	 */
	public static String request( String json ) {
		//判断是否为json字符串
		try {
            if(D) Log.i(TAG,"===========request json ====>>"+json);
			new JSONObject( json );
		} catch ( JSONException e ) {
			Log.e( "Request", "不是json数据", e );
			return "";
		}		
		
		byte[] response = null;
		try {
			
			URL url = new URL( 
					ClientApplication.HTTP, 
					ClientApplication.IP_ADDRESS, 
					ClientApplication.PORT, 
					ClientApplication.FILE );
			response = HttpClient.connect(
                    url,
                    HttpClient.HTTP_POST,
                    "requestParam=" + URLEncoder.encode(json, "utf-8"),
                    Request.CONNECTTIMEOUT,
                    Request.READTIMEOUT);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			String res = new String( response, "utf-8" );
            if(D) Log.i(TAG,"=====response===res==>>>"+res);
			return res.trim();
		} catch ( Exception e ) {
			Log.e( "Request", "访问失败", e );
			return "";
		}
	}
}