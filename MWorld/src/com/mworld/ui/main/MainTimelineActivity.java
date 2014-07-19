package com.mworld.ui.main;

import android.app.Activity;
import android.content.Intent;

import com.mworld.support.utils.GlobalContext;
import com.mworld.weibo.entities.Account;

public class MainTimelineActivity extends Activity {

	public static Intent newIntent() {
		return new Intent(GlobalContext.getInstance(),
				MainTimelineActivity.class);
	}

	public static Intent newIntent(Account account) {
		Intent intent = newIntent();
		intent.putExtra("account_extra", account);
		return intent;
	}
}
