package com.weibo.entities;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author MengMeng
 * 
 */
public class AccessToken {

	/** 用于调用access_token，接口获取授权后的access token */
	public String access_token;
	/** access_token的生命周期，单位是秒数 */
	public String expires_in;
	/** 当前授权用户的UID */
	public String uid;

	public static AccessToken parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return AccessToken.parse(JSON.parseObject(jsonString));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static AccessToken parse(JSONObject jsonObject) {
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
