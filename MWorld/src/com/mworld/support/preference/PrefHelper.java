package com.mworld.support.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefHelper {

	private static SharedPreferences.Editor editor = null;
	private static SharedPreferences sharedPreferences = null;

	public static boolean getBoolean(Context context, String key, boolean value) {
		return getSharedPreferencesObject(context).getBoolean(key, value);
	}

	public static void putBoolean(Context context, String key, boolean value) {
		getEditorObject(context).putBoolean(key, value).commit();
	}

	public static int getInt(Context context, String key, int value) {
		return getSharedPreferencesObject(context).getInt(key, value);
	}

	public static void putInt(Context context, String key, int value) {
		getEditorObject(context).putInt(key, value).commit();
	}

	public static String getString(Context context, String key, String value) {
		return getSharedPreferencesObject(context).getString(key, value);
	}

	public static void putString(Context context, String key, String value) {
		getEditorObject(context).putString(key, value).commit();
	}

	private static SharedPreferences getSharedPreferencesObject(Context context) {
		if (sharedPreferences == null)
			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(context);
		return sharedPreferences;
	}

	private static SharedPreferences.Editor getEditorObject(Context context) {
		if (editor == null)
			editor = getSharedPreferencesObject(context).edit();
		return editor;
	}

}
