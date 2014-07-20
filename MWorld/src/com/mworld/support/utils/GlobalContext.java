package com.mworld.support.utils;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.app.Application;

import com.mworld.support.preference.PrefUtility;
import com.mworld.weibo.entities.Account;

public class GlobalContext extends Application {

	private static GlobalContext globalContext;

	private Activity activity = null;

	private Account mAccount = null;

	private Object mGroup = null;

	private int currentTab = 0;

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

	public Account getAccount() {
		if (mAccount == null) {
			int id = PrefUtility.getDefaultAccountId();
			FinalDb fd = FinalDb.create(getInstance());
			mAccount = fd.findById(id, Account.class);
		}
		return mAccount;
	}

	public void setAccount(final Account account) {
		mAccount = account;
	}

	public String getCurrentAccountName() {
		return getAccount().getUserInfo().getScreenName();
	}

	public Object getGroup() {
		return mGroup;
	}

	public void setGroup(Object group) {
		mGroup = group;
	}

	public int getCurTab() {
		return currentTab;
	}

	public void setCurTab(int tab) {
		currentTab = tab;
	}

}
