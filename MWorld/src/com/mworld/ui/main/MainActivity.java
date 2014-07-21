package com.mworld.ui.main;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mworld.R;
import com.mworld.support.preference.PrefUtility;
import com.mworld.support.utils.GlobalContext;
import com.mworld.ui.adapter.TabsAdapter;
import com.mworld.ui.fragment.CommentsFragment;
import com.mworld.ui.fragment.FriendsFragment;
import com.mworld.ui.fragment.LeftMenuFragment;
import com.mworld.ui.fragment.MentionsFragment;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.entities.User;

public class MainActivity extends FragmentActivity {

	private Account mAccount;

	private User mUser;

	private String mToken;

	private ViewPager mViewPager;

	private TabsAdapter mTabsAdapter;

	private SlidingMenu menu;

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
		} else {
			mUser = mAccount.getUserInfo();
			mToken = mAccount.getAccessToken();
		}

		GlobalContext.getInstance().setAccount(mAccount);
		GlobalContext.getInstance().setActivity(this);
		PrefUtility.setDefaultAccountId(mAccount.getId());

		setContentView(R.layout.content_frame);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(mUser.getScreenName());

		// set the Above View
		if (savedInstanceState == null) {
			initFragment();
			// configure the SlidingMenu
			menu = new SlidingMenu(this);
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			menu.setShadowWidthRes(R.dimen.shadow_width);
			menu.setShadowDrawable(R.drawable.shadow);
			menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
			menu.setFadeDegree(0.35f);
			menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
			menu.setMenu(R.layout.menu_frame);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.menu_frame, getMenuFragment()).commit();

			getSupportFragmentManager().beginTransaction()
					.show(getFriendsFragment()).commit();
		}

		// bindUserInterfaces(savedInstanceState);

	}

	private void initFragment() {
		Fragment friends = getFriendsFragment();
		Fragment mentions = getMentionsFragment();
		Fragment comments = getCommentsFragment();

		Fragment fav = getFavFragment();
		Fragment myself = getMyProfileFragment();

		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		if (!friends.isAdded()) {
			fragmentTransaction.add(R.id.content_frame, friends,
					FriendsFragment.class.getName());
			fragmentTransaction.hide(friends);
		}
		if (!mentions.isAdded()) {
			fragmentTransaction.add(R.id.content_frame, mentions,
					MentionsFragment.class.getName());
			fragmentTransaction.hide(mentions);

		}
		if (!comments.isAdded()) {
			fragmentTransaction.add(R.id.content_frame, comments,
					CommentsFragment.class.getName());
			fragmentTransaction.hide(comments);

		}

		// if (!fav.isAdded()) {
		// fragmentTransaction.add(R.id.menu_right_fl, fav,
		// MyFavListFragment.class.getName());
		// fragmentTransaction.hide(fav);
		// }
		//
		// if (!myself.isAdded()) {
		// fragmentTransaction.add(R.id.menu_right_fl, myself,
		// UserInfoFragment.class.getName());
		// fragmentTransaction.hide(myself);
		// }

		if (!fragmentTransaction.isEmpty()) {
			fragmentTransaction.commit();
			getSupportFragmentManager().executePendingTransactions();
		}
	}

	public void switchFragment(int which) {
		menu.showContent();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		switch (which) {
		case 0:
			fragmentTransaction.show(getFriendsFragment());
			fragmentTransaction.hide(getMentionsFragment());
			fragmentTransaction.hide(getCommentsFragment());
			fragmentTransaction.commit();
			break;
		case 1:
			fragmentTransaction.hide(getFriendsFragment());
			fragmentTransaction.show(getMentionsFragment());
			fragmentTransaction.hide(getCommentsFragment());
			fragmentTransaction.commit();
			break;
		case 2:
			fragmentTransaction.hide(getFriendsFragment());
			fragmentTransaction.hide(getMentionsFragment());
			fragmentTransaction.show(getCommentsFragment());
			fragmentTransaction.commit();
			break;
		default:
			break;
		}
	}

	private Fragment getMyProfileFragment() {
		return null;
	}

	private Fragment getFavFragment() {
		return null;
	}

	private Fragment getCommentsFragment() {
		CommentsFragment fragment = ((CommentsFragment) getSupportFragmentManager()
				.findFragmentByTag(CommentsFragment.class.getName()));
		if (fragment == null) {
			fragment = CommentsFragment.newInstance(mAccount, mUser, mToken);
		}
		return fragment;
	}

	private Fragment getMentionsFragment() {
		MentionsFragment fragment = ((MentionsFragment) getSupportFragmentManager()
				.findFragmentByTag(MentionsFragment.class.getName()));
		if (fragment == null) {
			fragment = MentionsFragment.newInstance(mAccount, mUser, mToken);
		}
		return fragment;
	}

	private Fragment getFriendsFragment() {
		FriendsFragment fragment = ((FriendsFragment) getSupportFragmentManager()
				.findFragmentByTag(FriendsFragment.class.getName()));
		if (fragment == null) {
			fragment = FriendsFragment.newInstance(mAccount, mUser, mToken);
		}
		return fragment;
	}

	public LeftMenuFragment getMenuFragment() {
		LeftMenuFragment fragment = ((LeftMenuFragment) getSupportFragmentManager()
				.findFragmentByTag(LeftMenuFragment.class.getName()));
		if (fragment == null) {
			fragment = LeftMenuFragment.newInstance();
		}
		return fragment;
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
				FriendsFragment.class, null);
		mTabsAdapter.addTab(actionBar.newTab().setText("At"),
				MentionsFragment.class, null);
		mTabsAdapter.addTab(actionBar.newTab().setText("Comment"),
				CommentsFragment.class, null);

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
		case android.R.id.home:
			menu.toggle();
			break;
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
