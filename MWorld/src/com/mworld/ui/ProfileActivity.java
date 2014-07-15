package com.mworld.ui;

import java.util.ArrayList;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mworld.adapter.ProfileAdapter;
import com.mworld.view.FixListView;
import com.weibo.entities.User;

public class ProfileActivity extends Activity {

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case FixListView.SHOW_TAB:
				showTab();
				break;
			case FixListView.HIDE_TAB:
				hideTab();
				break;
			default:
				break;
			}
		}

		private void hideTab() {

		}

		private void showTab() {

		}

	};

	private User mUser;

	private ProfileAdapter adapter;

	@SuppressWarnings("rawtypes")
	private ArrayList mArrayList;

	FixListView mList;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		mList = (FixListView) findViewById(R.id.profile_timeline);
		mUser = (User) getIntent().getParcelableExtra("user");
		mArrayList.add(mUser);
		adapter = new ProfileAdapter(this, mArrayList);
		mList.setAdapter(adapter);
	}

}
