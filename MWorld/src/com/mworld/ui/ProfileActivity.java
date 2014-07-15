package com.mworld.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.mworld.adapter.ProfileAdapter;
import com.mworld.view.FixListView;
import com.weibo.entities.User;

public class ProfileActivity extends Activity {

	private User mUser;

	private ProfileAdapter adapter;

	@SuppressWarnings("rawtypes")
	private ArrayList mArrayList = new ArrayList();

	FixListView mList;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		mList = (FixListView) findViewById(R.id.profile_timeline);
		mList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem == 0) {
					findViewById(R.id.header).setVisibility(View.GONE);
				} else if (firstVisibleItem == 1) {
					findViewById(R.id.header).findViewById(R.id.retweet_layout)
							.setVisibility(View.GONE);
					findViewById(R.id.header).findViewById(
							R.id.layout_multi_pic).setVisibility(View.GONE);
					findViewById(R.id.header).setVisibility(View.VISIBLE);
				}
			}
		});
		mUser = (User) getIntent().getSerializableExtra("user");
		for (int i = 0; i < 15; i++)
			mArrayList.add(mUser);

		adapter = new ProfileAdapter(this, mArrayList);
		mList.setAdapter(adapter);
	}

}
