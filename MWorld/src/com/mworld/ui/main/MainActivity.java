package com.mworld.ui.main;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mworld.R;
import com.mworld.support.preference.PrefUtility;
import com.mworld.support.utils.GlobalContext;
import com.mworld.ui.fragment.CommentsFragment;
import com.mworld.ui.fragment.FriendsFragment;
import com.mworld.ui.fragment.LeftMenuFragment;
import com.mworld.ui.fragment.MentionsFragment;
import com.mworld.ui.fragment.MyFavFragment;
import com.mworld.ui.fragment.RepostFragment;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.entities.User;

public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	private Account mAccount;

	private User mUser;

	private String mToken;

	private SlidingMenu menu;

	public static Intent newIntent() {
		return new Intent(GlobalContext.getInstance(), MainActivity.class);
	}

	public static Intent newIntent(Account account) {
		GlobalContext.getInstance().setAccount(account);
		Intent intent = newIntent();
		intent.putExtra("account_extra", account);
		return intent;
	}

	@SuppressLint("NewApi")
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

		Window window = getWindow();
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		wl.alpha = 1.0f;
		window.setAttributes(wl);

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

			switchFragment(0);
		}

	}

	private void initFragment() {
		Fragment friends = getFriendsFragment();
		Fragment mentions = getMentionsFragment();
		Fragment comments = getCommentsFragment();

		Fragment fav = getFavFragment();
		Fragment repost = getRepostFragment();

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

		if (!fav.isAdded()) {
			fragmentTransaction.add(R.id.content_frame, fav,
					MyFavFragment.class.getName());
			fragmentTransaction.hide(fav);
		}

		if (!repost.isAdded()) {
			fragmentTransaction.add(R.id.content_frame, repost,
					RepostFragment.class.getName());
			fragmentTransaction.hide(repost);
		}

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
			getActionBar().setLogo(R.drawable.ic_menu_home);
			getActionBar().setDisplayShowTitleEnabled(false);
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			GlobalContext.getInstance().setActivityGroup(this, this);
			fragmentTransaction.show(getFriendsFragment());
			fragmentTransaction.hide(getMentionsFragment());
			fragmentTransaction.hide(getCommentsFragment());
			fragmentTransaction.hide(getFavFragment());
			fragmentTransaction.hide(getRepostFragment());
			fragmentTransaction.commit();
			break;
		case 1:
			getActionBar().setLogo(R.drawable.repost_light);
			getActionBar().setDisplayShowTitleEnabled(true);
			getActionBar().setTitle("提及");
			getActionBar()
					.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			fragmentTransaction.hide(getFriendsFragment());
			fragmentTransaction.show(getMentionsFragment());
			fragmentTransaction.hide(getCommentsFragment());
			fragmentTransaction.hide(getFavFragment());
			fragmentTransaction.hide(getRepostFragment());
			fragmentTransaction.commit();
			break;
		case 2:
			getActionBar().setLogo(R.drawable.comment_light);
			getActionBar().setDisplayShowTitleEnabled(true);
			getActionBar().setTitle("评论");
			getActionBar()
					.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			fragmentTransaction.hide(getFriendsFragment());
			fragmentTransaction.hide(getMentionsFragment());
			fragmentTransaction.show(getCommentsFragment());
			fragmentTransaction.hide(getFavFragment());
			fragmentTransaction.hide(getRepostFragment());
			fragmentTransaction.commit();
			break;
		case 3:
			getActionBar().setLogo(R.drawable.ic_menu_fav);
			getActionBar().setDisplayShowTitleEnabled(true);
			getActionBar().setTitle("收藏");
			getActionBar()
					.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			fragmentTransaction.hide(getFriendsFragment());
			fragmentTransaction.hide(getMentionsFragment());
			fragmentTransaction.hide(getCommentsFragment());
			fragmentTransaction.show(getFavFragment());
			fragmentTransaction.hide(getRepostFragment());
			fragmentTransaction.commit();
			break;
		case 4:
			getActionBar().setLogo(R.drawable.repost_light);
			getActionBar().setDisplayShowTitleEnabled(true);
			getActionBar().setTitle("转发");
			getActionBar()
					.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			fragmentTransaction.hide(getFriendsFragment());
			fragmentTransaction.hide(getMentionsFragment());
			fragmentTransaction.hide(getCommentsFragment());
			fragmentTransaction.hide(getFavFragment());
			fragmentTransaction.show(getRepostFragment());
			fragmentTransaction.commit();
		default:
			break;
		}
	}

	private Fragment getRepostFragment() {
		RepostFragment fragment = ((RepostFragment) getSupportFragmentManager()
				.findFragmentByTag(RepostFragment.class.getName()));
		if (fragment == null) {
			fragment = RepostFragment.newInstance(mAccount, mUser, mToken);
		}
		return fragment;
	}

	private Fragment getFavFragment() {

		MyFavFragment fragment = ((MyFavFragment) getSupportFragmentManager()
				.findFragmentByTag(MyFavFragment.class.getName()));
		if (fragment == null) {
			fragment = MyFavFragment.newInstance(mAccount, mUser, mToken);
		}
		return fragment;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		// 设置Menu可见
		MenuItem addItem = menu.findItem(R.id.action_add);
<<<<<<< HEAD
		MenuItemCompat.setShowAsAction(searchItem,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
=======
>>>>>>> origin/dev-2.0
		MenuItemCompat.setShowAsAction(addItem,
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
		case R.id.action_add:
			Intent intent = new Intent(this, WriteActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean secondExit = false;

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else if (!secondExit) {
			secondExit = true;
			Toast.makeText(this, "再按一次退出M-World", Toast.LENGTH_SHORT).show();
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					secondExit = false;
				}
			}, 3000);

		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onNavigationItemSelected(int which, long itemId) {
		((FriendsFragment) getFriendsFragment()).switchGroup(which);
		return false;
	}

}
