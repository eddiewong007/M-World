package com.mworld.ui;

import java.util.ArrayList;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.mworld.adapter.ProfileAdapter;
import com.mworld.holder.ProfTabHolder;
import com.weibo.api.FriendshipsAPI;
import com.weibo.api.StatusesAPI;
import com.weibo.entities.StatusesList;
import com.weibo.entities.User;
import com.weibo.entities.UsersList;

public class ProfileActivity extends Activity {

	public User mUser;

	public ProfileAdapter adapter;

	private StatusesAPI mStatusesAPI;

	private FriendshipsAPI mFriendshipsAPI;

	@SuppressWarnings("rawtypes")
	public ArrayList mArrayList = new ArrayList();

	@ViewInject(id = R.id.profile_timeline)
	private ListView mList;
	@ViewInject(id = R.id.header)
	private View header;
	@ViewInject(id = R.id.statuses_tab, click = "click")
	private View statusesTab;
	@ViewInject(id = R.id.friends_tab, click = "click")
	private View friendsTab;
	@ViewInject(id = R.id.followers_tab, click = "click")
	private View followersTab;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		FinalActivity.initInjectedView(this);

		// ActionBar and Tabs
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);

		// 设置ActionBar的背景
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_gradient_bg));
		actionBar.setDisplayUseLogoEnabled(true);

		mUser = (User) getIntent().getSerializableExtra("user");
		new ProfTabHolder(this, header).inflate(mUser);
		header.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				view.performClick();
				return true;
			}
		});

		mArrayList.add(mUser);
		mArrayList.add(mUser);
		mStatusesAPI = new StatusesAPI(MainActivity.sAccessToken);
		mFriendshipsAPI = new FriendshipsAPI(MainActivity.sAccessToken);
		mStatusesAPI.userTimeline(mUser.id, 0, 0, 20, 1, false, 0, false,
				new AjaxCallBack<String>() {

					@Override
					public void onSuccess(String jsonString) {
						super.onSuccess(jsonString);
						StatusesList statusesList = StatusesList
								.parse(jsonString);
						if (statusesList.statusesList != null)
							mArrayList.addAll(statusesList.statusesList);
						adapter.notifyDataSetChanged();
					}

				});

		adapter = new ProfileAdapter(this, mArrayList);
		mList.setAdapter(adapter);
		mList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if (firstVisibleItem + visibleItemCount == totalItemCount) {

				}

				if (firstVisibleItem == 0) {
					header.setVisibility(View.GONE);
				} else {
					header.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	public int currentTab = 0;
	public int friendsCur = 0;
	public int followerCur = 0;

	public void click(View view) {
		switch (view.getId()) {
		case R.id.statuses_tab:
			if (currentTab != 0) {
				for (int i = mArrayList.size() - 1; i > 1; i--)
					mArrayList.remove(i);
				mStatusesAPI.userTimeline(mUser.id, 0, 0, 20, 1, false, 0,
						false, new AjaxCallBack<String>() {

							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(String jsonString) {
								super.onSuccess(jsonString);
								if (currentTab != 0)
									return;
								StatusesList statusesList = StatusesList
										.parse(jsonString);
								if (statusesList.statusesList != null)
									mArrayList
											.addAll(statusesList.statusesList);
								adapter.notifyDataSetChanged();
							}

						});
				friendsCur = 0;
				followerCur = 0;
				currentTab = 0;
			}
			break;
		case R.id.friends_tab:
			if (currentTab != 1) {
				for (int i = mArrayList.size() - 1; i > 1; i--)
					mArrayList.remove(i);
				mFriendshipsAPI.friends(mUser.id, 20, friendsCur, true,
						new AjaxCallBack<String>() {

							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(String jsonString) {
								super.onSuccess(jsonString);
								if (currentTab != 1)
									return;
								UsersList usersList = UsersList
										.parse(jsonString);
								if (usersList.usersList != null) {
									friendsCur = usersList.next_cursor;
									mArrayList.addAll(usersList.usersList);
								}
								adapter.notifyDataSetChanged();
							}

						});
				followerCur = 0;
				currentTab = 1;
			}
			break;
		case R.id.followers_tab:
			if (currentTab != 2) {
				for (int i = mArrayList.size() - 1; i > 1; i--)
					mArrayList.remove(i);
				mFriendshipsAPI.followers(mUser.id, 20, followerCur, true,
						new AjaxCallBack<String>() {

							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(String jsonString) {
								super.onSuccess(jsonString);
								if (currentTab != 1)
									return;
								UsersList usersList = UsersList
										.parse(jsonString);
								if (usersList.usersList != null) {
									followerCur = usersList.next_cursor;
									mArrayList.addAll(usersList.usersList);
								}
								adapter.notifyDataSetChanged();
							}

						});
				friendsCur = 0;
				currentTab = 2;
			}
			break;
		default:
			break;
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
			Toast.makeText(this, "I am search", Toast.LENGTH_LONG).show();
			break;
		case R.id.action_add:
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, 1);
			break;
		case R.id.action_more:
			Toast.makeText(this, "I am more", Toast.LENGTH_LONG).show();
			startActivity(new Intent(ProfileActivity.this, DialogActivity.class));

			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
