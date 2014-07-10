package com.mworld.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mworld.adapter.TabsAdapter;
import com.mworld.fragment.AtFragment;
import com.mworld.fragment.CommentFragment;
import com.mworld.fragment.HomeFragment;
import com.mworld.utils.PreUtils;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.weibo.api.UsersAPI;
import com.weibo.entities.AccessToken;
import com.weibo.entities.User;

public class MainActivity extends ActionBarActivity {

	public static AccessToken sAccessToken;
	public static User sUser;

	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.vPager);
		setContentView(mViewPager);

		// ActionBar and Tabs
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);

		// 设置ActionBar的背景
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_gradient_bg));
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

		obtainCurUser();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
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
			Toast.makeText(this, "I am search", Toast.LENGTH_LONG).show();
			break;
		case R.id.action_add:
			Toast.makeText(this, "I am add", Toast.LENGTH_LONG).show();
			break;
		case R.id.action_more:
			Toast.makeText(this, "I am more", Toast.LENGTH_LONG).show();
			startActivity(new Intent(MainActivity.this, DialogActivity.class));

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void obtainCurUser() {
		sAccessToken = PreUtils.readAccessToken(this);
		new UsersAPI(sAccessToken).show(Long.parseLong(sAccessToken.uid),
				new RequestListener() {

					@Override
					public void onComplete(String jsonString) {
						sUser = User.parse(jsonString);
					}

					@Override
					public void onWeiboException(WeiboException arg0) {

					}
				});

	}

}
