package com.mworld.ui;

import java.util.ArrayList;

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
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.mworld.adapter.ProfileAdapter;
import com.mworld.holder.ProfTabHolder;
import com.weibo.api.StatusesAPI;
import com.weibo.entities.StatusesList;
import com.weibo.entities.User;

public class ProfileActivity extends Activity {

	private User mUser;

	private ProfileAdapter adapter;

	@SuppressWarnings("rawtypes")
	private ArrayList mArrayList = new ArrayList();

	private ListView mList;

	private View header;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		// ActionBar and Tabs
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);

		// 设置ActionBar的背景
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_gradient_bg));
		actionBar.setDisplayUseLogoEnabled(true);

		mUser = (User) getIntent().getSerializableExtra("user");
		header = findViewById(R.id.header);
		header.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				view.performClick();
				return true;
			}
		});
		new ProfTabHolder(header).inflate(mUser);
		mList = (ListView) findViewById(R.id.profile_timeline);
		mList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if (firstVisibleItem == 0) {
					header.setVisibility(View.GONE);
				} else {
					header.setVisibility(View.VISIBLE);
				}
			}
		});

		mArrayList.add(mUser);
		mArrayList.add(mUser);
		StatusesAPI statusesAPI = new StatusesAPI(MainActivity.sAccessToken);
		statusesAPI.userTimeline(mUser.id, 0, 0, 20, 1, false, 0, false,
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
