package com.mworld.ui.holder;

import net.tsz.afinal.http.AjaxCallBack;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.ui.main.ProfileActivity;
import com.mworld.weibo.api.FriendshipsAPI;
import com.mworld.weibo.api.StatusAPI;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.entities.StatusList;
import com.mworld.weibo.entities.User;
import com.mworld.weibo.entities.UserList;

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
		statusesCount.setText("" + user.getStatusesCount());
		friendsCount.setText("" + user.getFriendsCount());
		followersCount.setText("" + user.getFollowersCount());
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
				Account account = GlobalContext.getInstance().getAccount();
				new StatusAPI(account.getAccessToken()).userTimeline(
						Long.parseLong(account.getUid()), 0, 0, 20, 1, false,
						0, false, new AjaxCallBack<String>() {

							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(String jsonString) {
								super.onSuccess(jsonString);
								if (activity.currentTab != 0)
									return;
								StatusList statusesList = StatusList
										.parse(jsonString);
								if (statusesList.statuses != null)
									activity.mArrayList
											.addAll(statusesList.statuses);
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
				Account account = GlobalContext.getInstance().getAccount();
				new FriendshipsAPI(account.getAccessToken()).friends(
						Long.parseLong(account.getUid()), 20,
						activity.friendsCur, true, new AjaxCallBack<String>() {

							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(String jsonString) {
								super.onSuccess(jsonString);
								if (activity.currentTab != 1)
									return;
								UserList usersList = UserList.parse(jsonString);
								if (usersList.users != null) {
									activity.friendsCur = usersList.next_cursor;
									activity.mArrayList.addAll(usersList.users);
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
				Account account = GlobalContext.getInstance().getAccount();
				new FriendshipsAPI(account.getAccessToken()).followers(
						Long.parseLong(account.getUid()), 20,
						activity.followerCur, true, new AjaxCallBack<String>() {

							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(String jsonString) {
								super.onSuccess(jsonString);
								if (activity.currentTab != 1)
									return;
								UserList usersList = UserList.parse(jsonString);
								if (usersList.users != null) {
									activity.followerCur = usersList.next_cursor;
									activity.mArrayList.addAll(usersList.users);
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