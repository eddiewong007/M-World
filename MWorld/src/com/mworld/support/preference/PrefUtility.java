package com.mworld.support.preference;

import android.content.Context;

import com.mworld.support.utils.GlobalContext;

public class PrefUtility {

	private static final String FIRSTSTART = "firststart";

	private static Context getContext() {
		return GlobalContext.getInstance();
	}

	public static boolean firstStart() {
		boolean value = PrefHelper.getSharedPreferences(getContext(),
				FIRSTSTART, true);
		if (value) {
			PrefHelper.setEditor(getContext(), FIRSTSTART, false);
		}
		return value;
	}

	public static String getDefaultAccountId() {
		return PrefHelper.getSharedPreferences(getContext(), "id", "");
	}

	public static void setDefaultAccountId(int id) {
		PrefHelper.setEditor(getContext(), "id", id);
	}

}
