package com.mworld.ui.main;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mworld.R;
import com.mworld.support.preference.PrefUtility;
import com.mworld.support.utils.GlobalContext;
import com.mworld.weibo.entities.Account;

public class MainTimelineActivity extends FragmentActivity {

	private Account mAccount;

	public static Intent newIntent() {
		return new Intent(GlobalContext.getInstance(),
				MainTimelineActivity.class);
	}

	public static Intent newIntent(Account account) {
		Intent intent = newIntent();
		intent.putExtra("account_extra", account);
		return intent;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("account", mAccount);
	}

	
}
