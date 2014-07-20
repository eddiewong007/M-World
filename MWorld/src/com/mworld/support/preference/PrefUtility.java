package com.mworld.support.preference;

import android.content.Context;

import com.mworld.support.utils.GlobalContext;

public class PrefUtility {

	private static final String FIRSTSTART = "firststart";

	private static Context getContext() {
		return GlobalContext.getInstance();
	}

	public static boolean firstStart() {
		boolean value = PrefHelper.getBoolean(getContext(), FIRSTSTART, true);
		if (value) {
			PrefHelper.putBoolean(getContext(), FIRSTSTART, false);
		}
		return value;
	}

	public static int getDefaultAccountId() {
		return PrefHelper.getInt(getContext(), "id", 0);
	}

	public static void setDefaultAccountId(int id) {
		PrefHelper.putInt(getContext(), "id", id);
	}

}
