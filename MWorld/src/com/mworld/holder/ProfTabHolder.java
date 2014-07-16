package com.mworld.holder;

import android.view.View;
import android.widget.TextView;

import com.mworld.ui.R;
import com.weibo.entities.User;

public class ProfTabHolder {
	public TextView statusesCount;
	public TextView friendsCount;
	public TextView followersCount;

	/**
	 * 
	 * @param context
	 * @param view
	 */
	public ProfTabHolder(View view) {
		statusesCount = (TextView) view.findViewById(R.id.statuses_count);
		friendsCount = (TextView) view.findViewById(R.id.friends_count);
		followersCount = (TextView) view.findViewById(R.id.followers_count);
	}

	public void inflate(User user) {
		statusesCount.setText("" + user.statuses_count);
		friendsCount.setText("" + user.friends_count);
		followersCount.setText("" + user.followers_count);
	}
}
