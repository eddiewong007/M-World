package com.mworld.support.utils;

import android.app.Activity;
import android.app.Application;

public class GlobalContext extends Application {

	private static GlobalContext globalContext;

	private Activity activity = null;

	@Override
	public void onCreate() {
		super.onCreate();
		globalContext = this;
	}

	public static GlobalContext getInstance() {
		return globalContext;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
