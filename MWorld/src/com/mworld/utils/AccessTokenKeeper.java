package com.mworld.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.mworld.weibo.entities.AccessToken;

/**
 * 该类用于保存AccessToken到sharepreference，并提供读取功能
 * 
 * @author MengMeng
 * 
 */
public class AccessTokenKeeper {

	/**
	 * 保存accesstoken到SharedPreferences
	 * 
	 * @param context
	 *            Activity 上下文环境
	 * @param token
	 *            AccessToken
	 */
	public static void keepAccessToken(Context context, AccessToken accessToken) {
		SharedPreferences pref = context.getSharedPreferences(
				context.getPackageName() + "_preferences", 0);
		Editor editor = pref.edit();
		editor.putString("access_token", accessToken.access_token);
		editor.putString("expires_in", accessToken.expires_in);
		editor.putString("remind_in", accessToken.remind_in);
		editor.putString("uid", accessToken.uid);
		editor.commit();
	}

	/**
	 * 从SharedPreferences读取accessstoken
	 * 
	 * @param context
	 * @return Oauth2AccessToken
	 */
	public static AccessToken readAccessToken(Context context) {
		AccessToken accessToken = new AccessToken();
		SharedPreferences pref = context.getSharedPreferences(
				context.getPackageName() + "_preferences", 0);
		accessToken.access_token = pref.getString("access_token", "");
		accessToken.expires_in = pref.getString("expires_in", "");
		accessToken.remind_in = pref.getString("remind_in", "");
		accessToken.uid = pref.getString("uid", "");
		if ("".equals(accessToken.access_token)) {
			return null;
		}
		return accessToken;
	}

	/**
	 * 清空sharepreference
	 * 
	 * @param context
	 */
	public static void clear(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				context.getPackageName() + "_preferences", 0);
		Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}

}
