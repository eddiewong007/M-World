package com.mworld.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.weibo.entities.AccessToken;

/**
 * 该类用于保存AccessToken到sharepreference，并提供读取功能
 * 
 * @author MengMeng
 * 
 */
public class PrefUtils {

	private static final String PREF_SUFFIX = "_preferences";

	private static final String ACCESS_TOKEN = "access_token";
	private static final String EXPIRES_IN = "expires_in";
	private static final String UID = "uid";
	private static final String IS_GUIDED = "is_guided";

	/**
	 * 清空sharepreference
	 * 
	 * @param context
	 */
	public static void clear(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				context.getPackageName() + PREF_SUFFIX, 0);
		pref.edit().clear().commit();
	}

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
				context.getPackageName() + PREF_SUFFIX, 0);
		pref.edit().putString(ACCESS_TOKEN, accessToken.access_token)
				.putString(EXPIRES_IN, accessToken.expires_in)
				.putString(UID, accessToken.uid).commit();
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
				context.getPackageName() + PREF_SUFFIX, 0);
		accessToken.access_token = pref.getString(ACCESS_TOKEN, "");
		accessToken.expires_in = pref.getString(EXPIRES_IN, "");
		accessToken.uid = pref.getString(UID, "");
		return TextUtils.isEmpty(accessToken.access_token) ? null : accessToken;

	}

	/**
	 * 用户注销
	 * 
	 * @param context
	 *            上下文对象
	 */
	public static void logOut(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				context.getPackageName() + PREF_SUFFIX, 0);
		pref.edit().putString(ACCESS_TOKEN, "").putString(EXPIRES_IN, "")
				.putString(UID, "").commit();
	}

	/**
	 * 判断是否进入过引导界面
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isGuided(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				context.getPackageName() + PREF_SUFFIX, 0);
		return pref.getBoolean(IS_GUIDED, false);
	}

	/**
	 * 设置已经进入过引导界面
	 * 
	 * @param context
	 */
	public static void setGuided(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				context.getPackageName() + PREF_SUFFIX, 0);
		pref.edit().putBoolean(IS_GUIDED, true).commit();
	}

}
