package com.mworld.ui.holder;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.ui.main.ProfileActivity;
import com.mworld.weibo.entities.User;

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
		int curTab = GlobalContext.getInstance().getCurTab();
		int selected = R.drawable.profile_roundconer_relativelayout;
		int unselected = android.R.color.transparent;

		int statusCnt = user.getStatusesCount();
		int friendsCnt = user.getFriendsCount();
		int followersCnt = user.getFollowersCount();
		if (statusCnt > 1e4) {
			String str = statusCnt / 1e4 + "万"
					+ (statusCnt % 1e4 == 0 ? "" : "+");
			statusesCount.setText("" + str);
		} else {
			statusesCount.setText("" + statusCnt);
		}
		if (friendsCnt > 1e4) {
			String str = friendsCnt / 1e4 + "万"
					+ (friendsCnt % 1e4 == 0 ? "" : "+");
			friendsCount.setText("" + str);
		} else {
			friendsCount.setText("" + friendsCnt);
		}
		if (followersCnt > 1e4) {
			String str = followersCnt / 1e4 + "万"
					+ (followersCnt % 1e4 == 0 ? "" : "+");
			followersCount.setText("" + str);
		} else {
			followersCount.setText("" + followersCnt);
		}

		statusesTab.setOnClickListener(this);
		statusesTab.setBackgroundResource(curTab == 0 ? selected : unselected);
		friendsTab.setOnClickListener(this);
		friendsTab.setBackgroundResource(curTab == 1 ? selected : unselected);
		followersTab.setOnClickListener(this);
		followersTab.setBackgroundResource(curTab == 2 ? selected : unselected);
	}

	@Override
	public void onClick(View view) {
		final ProfileActivity activity = (ProfileActivity) mContext;
		switch (view.getId()) {
		case R.id.statuses_tab:
			if (GlobalContext.getInstance().getCurTab() != 0) {
				GlobalContext.getInstance().setCurTab(0);
				for (int i = activity.mArrayList.size() - 1; i > 1; i--)
					activity.mArrayList.remove(i);
				((ProfileActivity) mContext).performLoad(0);
				activity.friendsCur = 0;
				activity.followerCur = 0;
			}
			break;
		case R.id.friends_tab:
			if (!((ProfileActivity) mContext).isLoginUser) {
				Toast.makeText(mContext, "由于新浪接口的限制，不能获取他人的好友/粉丝列表",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (GlobalContext.getInstance().getCurTab() != 1) {
				GlobalContext.getInstance().setCurTab(1);
				for (int i = activity.mArrayList.size() - 1; i > 1; i--)
					activity.mArrayList.remove(i);
				((ProfileActivity) mContext).performLoad(1);
				activity.followerCur = 0;
				activity.page = 1;
			}
			break;
		case R.id.followers_tab:
			if (!((ProfileActivity) mContext).isLoginUser) {
				Toast.makeText(mContext, "由于新浪接口的限制，不能获取他人的好友/粉丝列表",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (GlobalContext.getInstance().getCurTab() != 2) {
				GlobalContext.getInstance().setCurTab(2);
				for (int i = activity.mArrayList.size() - 1; i > 1; i--)
					activity.mArrayList.remove(i);
				((ProfileActivity) mContext).performLoad(2);
				activity.friendsCur = 0;
				activity.page = 1;
			}
			break;
		default:
			break;
		}
	}
}