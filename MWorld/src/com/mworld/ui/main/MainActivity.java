package com.mworld.ui.main;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.mworld.R;
import com.mworld.support.preference.PrefUtility;
import com.mworld.support.utils.GlobalContext;
import com.mworld.ui.adapter.TabsAdapter;
import com.mworld.ui.fragment.AtFragment;
import com.mworld.ui.fragment.CommentFragment;
import com.mworld.ui.fragment.HomeFragment;
import com.mworld.weibo.entities.Account;

public class MainActivity extends FragmentActivity {

	private Account mAccount;

	private ViewPager mViewPager;

	private TabsAdapter mTabsAdapter;

	public static Intent newIntent() {
		return new Intent(GlobalContext.getInstance(), MainActivity.class);
	}

	public static Intent newIntent(Account account) {
		Intent intent = newIntent();
		intent.putExtra("account_extra", account);
		return intent;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			mAccount = savedInstanceState.getParcelable("account");
		} else {
			Intent intent = getIntent();
			mAccount = intent.getParcelableExtra("account_extra");
		}

		if (mAccount == null) {
			mAccount = GlobalContext.getInstance().getAccount();
		}

		GlobalContext.getInstance().setAccount(mAccount);
		GlobalContext.getInstance().setActivity(this);
		PrefUtility.setDefaultAccountId(mAccount.getId());

		bindUserInterfaces(savedInstanceState);

	}

	private void bindUserInterfaces(Bundle savedInstanceState) {
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.vPager);
		setContentView(mViewPager);

		// ActionBar and Tabs
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mAccount.getUserInfo().getScreenName());

		// 设置ActionBar的背景
		actionBar.setDisplayUseLogoEnabled(true);

		mTabsAdapter = new TabsAdapter(getSupportFragmentManager(), this,
				mViewPager);
		mTabsAdapter.addTab(actionBar.newTab().setText("Home"),
				HomeFragment.class, null);
		mTabsAdapter.addTab(actionBar.newTab().setText("At"), AtFragment.class,
				null);
		mTabsAdapter.addTab(actionBar.newTab().setText("Comment"),
				CommentFragment.class, null);

		if (savedInstanceState != null) {
			actionBar.setSelectedNavigationItem(savedInstanceState.getInt(
					"tab", 0));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		// 设置Menu可见
		MenuItem searchItem = menu.findItem(R.id.action_search);
		MenuItem addItem = menu.findItem(R.id.action_add);
		MenuItem moreItem = menu.findItem(R.id.action_more);
		MenuItemCompat.setShowAsAction(searchItem,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		MenuItemCompat.setShowAsAction(addItem,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		MenuItemCompat.setShowAsAction(moreItem,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			break;
		case R.id.action_add:
			Intent intent = new Intent(this, WriteActivity.class);
			startActivity(intent);
			break;
		case R.id.action_more:
			startActivity(new Intent(MainActivity.this, DialogActivity.class));

			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
