package com.weibo.entities;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 授权（access token）令牌实体对象结构
 * 
 * @author MengMeng
 * 
 */
public class AccessToken {
	private static final String TAG = "AccessToken";

	/** 调用access_token接口获取授权后的access token */
	public String access_token;
	/** access_token的生命周期，单位是秒数 */
	public String expires_in;
	/** 当前授权用户的UID */
	public String uid;

	/**
	 * 将json字符串解析成AccessToken对象
	 * 
	 * @param jsonString
	 *            待解析的json字符串
	 * @return 解析出来的AccessToken对象
	 */
	public static AccessToken parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return AccessToken.parse(JSON.parseObject(jsonString));
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}

		return null;
	}

	private static AccessToken parse(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}

		AccessToken accessToken = new AccessToken();
		accessToken.access_token = jsonObject.getString("access_token");
		accessToken.expires_in = jsonObject.getString("expires_in");
		accessToken.uid = jsonObject.getString("uid");

		return accessToken;
	}

}
