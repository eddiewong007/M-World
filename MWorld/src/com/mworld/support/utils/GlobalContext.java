package com.mworld.support.utils;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import com.mworld.support.preference.PrefUtility;
import com.mworld.weibo.entities.Account;

public class GlobalContext extends Application {

	private static GlobalContext globalContext;

	private Activity activity = null;

	private Account mAccount;

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
			String id = PrefUtility.getDefaultAccountId();
			FinalDb fd = FinalDb.create(globalContext);
			if (!TextUtils.isEmpty(id)) {
				mAccount = fd.findById(id, Account.class);
			} else {
				List<Account> accountList = fd.findAll(Account.class);
				if (accountList != null && accountList.size() > 0) {
					mAccount = accountList.get(0);
				}
			}
		}
		return mAccount;
	}

	public void setAccount(final Account account) {
		mAccount = account;
	}

	public String getCurrentAccountName() {
		return getAccount().getUserInfo().getScreenName();
	}

}
