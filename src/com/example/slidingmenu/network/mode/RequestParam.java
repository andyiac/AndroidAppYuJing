package com.example.slidingmenu.network.mode;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 请求服务器的参数
 * 
 *
 */
public class RequestParam {
	
	public static final String USER_NAME = "userName";//其实是手机号
	public static final String PASSWORD = "password";
	public static final String RANDOM_KEY = "randomKey";
	public static final String REQUEST_TYPE = "requestType";
	public static final String PARAMS = "params";
	public static final String SEX = "sex";
	public static final String ADDR = "addr";
	public static final String NAME = "name";
	public static final String PHOTO = "photo";

	public static final String STATUS = "loginStatus";
	
	public static final int ONLINE = 0;
	public static final int OFFLINE = 1;
	
	public static final int SEND_TOPIC = 111;
	
	/**
	 * 登录
	 */
	public final String LOGIN = "Login";
	
	/**
	 * 注销
	 */
	public static final String LOGOUT = "Logout";
	
	/**
	 * 定时访问服务器
	 */
	public static final String UPDATE_INFO = "update_info";
		
	/**
	 * 添加话题
	 */
	public static final String ADD_TOPIC = "AddTopic";
	
	/**
	 * 获得所有话题
	 */
	public static final String GET_ALL_TOPIC = "GetAllTopic";
	
	/**
	 * 获得话题评论
	 */
	public static final String GET_TOPIC_COM = "GetTopicComment";
	
	/**
	 * 添加话题评论
	 */
	public static final String ADD_TOPIC_COM = "AddTopicComment";
	
	/**
	 * 获得最新话题
	 */
	public static final String GET_NEW_TOPIC = "GetNewTopic";
	
	/**
	 * 获取用户资料
	 */
	public static final String GET_PERSONINFO = "GetPersonInfo";
	
	/**
	 * 发送私信
	 */
	public static final String SEND_PRIVATELETTER = "SendPrivateLetter";
	
	/**
	 * 获得最新的私信
	 */
	public static final String GET_NEW_PRIVATELETTER = "GetNewPrivateLetter";
	
	/**
	 * 获得全部私信
	 */
	public static final String GET_ALL_PRIVATELETTER = "GetAllPrivateLetter";
	
	/**
	 * 添加好友
	 */
	public static final String ADD_FRIENDS = "AddFriends";
	
	/**
	 * 获得全部好友
	 */
	public static final String GET_ALL_FRIENDS = "GetAllFriends";
	
	/**
	 * 删除好友
	 */
	public static final String DEL_FRIENDS = "DeleteFriends";
	
	/**
	 * 获得删除的好友
	 */
	public static final String GET_DEL_FRIENDS = "GetDeleteFriends";
	
	/**
	 * 获得新添加的好友
	 */
	public static final String GET_NEW_FRIENDS = "GetNewFriends";
	
	/**
	 * 获得好友在线状态
	 */
	public static final String GET_PERSON_STATE = "GetPersonStatus";
	
	/**
	 * 注册
	 */
	public static final String SIGNIN = "Signin";
	
	/**
	 * 检查更新
	 */
	public static final String UPDATE = "SoftWareUpdate";
	
	/**
	 * 获得全部用户 
	 */
	public static final String GET_ALL_PEOPLE = "GetAllPeople";
	
	/**
	 * 删除私信
	 */
	public static final String DEL_LETTER = "DeletePrivateLetter";
	
	
	/**
	 * 登录名
	 */
	private String userName;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 随机字符串
	 */
	private String randomKey;
	
	/**
	 * 请求类型
	 */
	private String requestType;	
	
	/**
	 * 请求参数
	 */
	private Object params[];

	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRandomKey(String randomKey) {
		this.randomKey = randomKey;
	}	
	
	public void setParams(Object[] params) {
		this.params = params;
	}
	
	public void setRequestType( String requestType ) {
		this.requestType = requestType;
	}
	
	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getRandomKey() {
		return randomKey;
	}

	public String getRequestType() {
		return requestType;
	}	
	
	public String getJSON() {
		
		JSONObject object = new JSONObject();
		try {
			object.put( RequestParam.USER_NAME, this.userName );
			object.put( RequestParam.PASSWORD, this.password );
			object.put( RequestParam.RANDOM_KEY, this.randomKey );
			object.put( RequestParam.REQUEST_TYPE,this. requestType );
			
			JSONArray jsonArray = new JSONArray();
			
			for(Object param : params) {
				jsonArray.put(param);
			}
		
			object.put(RequestParam.PARAMS, jsonArray);
			System.out.println("请求参数"+object.toString());
			return object.toString();
			
		} catch (Exception e) {
			Log.e( "RequestParam", "构建发送请求参数出错", e );
			return "";
		}
	}
}