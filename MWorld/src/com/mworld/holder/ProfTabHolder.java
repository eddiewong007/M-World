package com.mworld.holder;

import net.tsz.afinal.http.AjaxCallBack;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mworld.ui.ProfileActivity;
import com.mworld.ui.R;
import com.mworld.utils.PreUtils;
import com.weibo.api.FriendshipsAPI;
import com.weibo.api.StatusesAPI;
import com.weibo.entities.StatusesList;
import com.weibo.entities.User;
import com.weibo.entities.UsersList;

public class ProfTabHolder implements OnClickListener {

	private Context mContext;
	private TextView statusesCount;
	private TextView friendsCount;
	private TextView followersCount;
	private View statusesTab;
	private View friendsTab;
	private View followersTab;

	/**
	 * 
	 * @param context
	 * @param view
	 */
	public ProfTabHolder(Context context, View view) {
		mContext = context;
		statusesCount = (TextView) view.findViewById(R.id.statuses_count);
		friendsCount = (TextView) view.findViewById(R.id.friends_count);
		followersCount = (TextView) view.findViewById(R.id.followers_count);
		statusesTab = view.findViewById(R.id.statuses_tab);
		friendsTab = view.findViewById(R.id.friends_tab);
		followersTab = view.findViewById(R.id.followers_tab);
	}

	public void inflate(User user) {
		statusesCount.setText("" + user.statuses_count);
		friendsCount.setText("" + user.friends_count);
		followersCount.setText("" + user.followers_count);
		statusesTab.setOnClickListener(this);
		friendsTab.setOnClickListener(this);
		followersTab.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		final ProfileActivity activity = (ProfileActivity) mContext;
		switch (view.getId()) {
		case R.id.statuses_tab:
			if (activity.currentTab != 0) {
				for (int i = activity.mArrayList.size() - 1; i > 1; i--)
					activity.mArrayList.remove(i);
				new StatusesAPI(PreUtils.readAccessToken(mContext))
						.userTimeline(activity.mUser.id, 0, 0, 20, 1, false, 0,
								false, new AjaxCallBack<String>() {

									@SuppressWarnings("unchecked")
									@Override
									public void onSuccess(String jsonString) {
										super.onSuccess(jsonString);
										if (activity.currentTab != 0)
											return;
										StatusesList statusesList = StatusesList
												.parse(jsonString);
										if (statusesList.statusesList != null)
											activity.mArrayList
													.addAll(statusesList.statusesList);
										activity.adapter.notifyDataSetChanged();
									}

								});
				activity.friendsCur = 0;
				activity.followerCur = 0;
				activity.currentTab = 0;
			}
			break;
		case R.id.friends_tab:
			if (activity.currentTab != 1) {
				for (int i = activity.mArrayList.size() - 1; i > 1; i--)
					activity.mArrayList.remove(i);
				new FriendshipsAPI(PreUtils.readAccessToken(mContext)).friends(
						activity.mUser.id, 20, activity.friendsCur, true,
						new AjaxCallBack<String>() {

							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(String jsonString) {
								super.onSuccess(jsonString);
								if (activity.currentTab != 1)
									return;
								UsersList usersList = UsersList
										.parse(jsonString);
								if (usersList.usersList != null) {
									activity.friendsCur = usersList.next_cursor;
									activity.mArrayList
											.addAll(usersList.usersList);
								}
								activity.adapter.notifyDataSetChanged();
							}

						});
				activity.followerCur = 0;
				activity.currentTab = 1;
			}
			break;
		case R.id.followers_tab:
			if (activity.currentTab != 2) {
				for (int i = activity.mArrayList.size() - 1; i > 1; i--)
					activity.mArrayList.remove(i);
				new FriendshipsAPI(PreUtils.readAccessToken(mContext))
						.followers(activity.mUser.id, 20, activity.followerCur,
								true, new AjaxCallBack<String>() {

									@SuppressWarnings("unchecked")
									@Override
									public void onSuccess(String jsonString) {
										super.onSuccess(jsonString);
										if (activity.currentTab != 1)
											return;
										UsersList usersList = UsersList
												.parse(jsonString);
										if (usersList.usersList != null) {
											activity.followerCur = usersList.next_cursor;
											activity.mArrayList
													.addAll(usersList.usersList);
										}
										activity.adapter.notifyDataSetChanged();
									}

								});
				activity.friendsCur = 0;
				activity.currentTab = 2;
			}
			break;
		default:
			break;
		}
	}
}
