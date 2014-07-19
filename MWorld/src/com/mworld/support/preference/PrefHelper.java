package com.mworld.support.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefHelper {

	private static SharedPreferences.Editor editor = null;
	private static SharedPreferences sharedPreferences = null;

	public static Boolean getSharedPreferences(Context paramContext,
			String paramString, Boolean paramBoolean) {
		return getSharedPreferencesObject(paramContext).getBoolean(paramString,
				paramBoolean);
	}

	public static String getSharedPreferences(Context paramContext,
			String paramString1, String paramString2) {
		return getSharedPreferencesObject(paramContext).getString(paramString1,
				paramString2);
	}

	private static SharedPreferences getSharedPreferencesObject(
			Context paramContext) {
		if (sharedPreferences == null)
			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(paramContext);
		return sharedPreferences;
	}

	public static void setEditor(Context paramContext, String paramString,
			Boolean paramBoolean) {
		getEditorObject(paramContext).putBoolean(paramString, paramBoolean)
				.commit();
	}

	private static SharedPreferences.Editor getEditorObject(Context paramContext) {
		if (editor == null)
			editor = PreferenceManager
					.getDefaultSharedPreferences(paramContext).edit();
		return editor;
	}

	public static void setEditor(Context paramContext, String paramString, int i) {
		getEditorObject(paramContext).putInt(paramString, i).commit();
	}

}
