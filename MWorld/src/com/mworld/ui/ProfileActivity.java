package com.mworld.ui;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import com.mworld.adapter.ProfileAdapter;
import com.mworld.holder.ProfTabHolder;
import com.mworld.view.FixListView;
import com.weibo.api.StatusesAPI;
import com.weibo.entities.StatusesList;
import com.weibo.entities.User;

public class ProfileActivity extends Activity {

	private User mUser;

	private ProfileAdapter adapter;

	@SuppressWarnings("rawtypes")
	private ArrayList mArrayList = new ArrayList();

	private FixListView mList;

	private View header;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
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
		mList = (FixListView) findViewById(R.id.profile_timeline);
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
		statusesAPI.userTimeline(Long.parseLong(MainActivity.sAccessToken.uid),
				0, 0, 20, 1, false, 0, false, new AjaxCallBack<String>() {

					@Override
					public void onSuccess(String jsonString) {
						super.onSuccess(jsonString);
						StatusesList statusesList = StatusesList
								.parse(jsonString);
						mArrayList.addAll(statusesList.statusesList);
						adapter.notifyDataSetChanged();
					}

				});

		adapter = new ProfileAdapter(this, mArrayList);
		mList.setAdapter(adapter);
	}

}
