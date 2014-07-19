package com.mworld.support.preference;

import com.mworld.support.utils.GlobalContext;

import android.content.Context;

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

}
