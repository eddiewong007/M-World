package com.mworld.ui.main;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.weibo.api.FriendshipsAPI;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.entities.User;
import com.mworld.weibo.entities.UserList;

public class AtFriendsActivity extends SwipeBackActivity {

	private User mUser;

	private int mCursor = 0;

	private FriendshipsAPI mFriendshipsAPI;

	private ArrayList<User> mArrayList;

	private AtFriendsAdapter mAdapter;

	@ViewInject(id = R.id.listview)
	private ListView mListView;

	public static Intent newIntent(Activity activity) {
		return new Intent(activity, AtFriendsActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		FinalActivity.initInjectedView(this);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		mArrayList = new ArrayList<User>();
		mAdapter = new AtFriendsAdapter(mArrayList);
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(mListener);

		Account account = GlobalContext.getInstance().getAccount();
		mUser = account.getUserInfo();
		mFriendshipsAPI = new FriendshipsAPI(GlobalContext.getInstance()
				.getAccount().getAccessToken());
		mFriendshipsAPI.friends(Long.parseLong(mUser.getId()), 20, mCursor,
				true, mCallBack);
	}

	private class AtFriendsAdapter extends BaseAdapter {

		private ArrayList<User> mUserList;

		public AtFriendsAdapter(ArrayList<User> userList) {
			mUserList = userList;
		}

		@Override
		public int getCount() {
			return mUserList.size();
		}

		@Override
		public Object getItem(int position) {
			return mUserList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup container) {
			Holder holder = null;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.list_item_user, null);
				holder = new Holder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.inflate(mUserList.get(position));
			return convertView;
		}

		private class Holder {

			private View userLayout;

			private ImageView userAvatar;

			private TextView userName;

			private ImageView icImage;

			public Holder(View view) {
				userLayout = view.findViewById(R.id.layout_user);
				userAvatar = (ImageView) view.findViewById(R.id.user_avatar);
				userName = (TextView) view.findViewById(R.id.user_name);
				icImage = (ImageView) view.findViewById(R.id.ic_image);
			}

			public void inflate(final User user) {
				FinalBitmap fb = FinalBitmap.create(AtFriendsActivity.this);
				fb.display(userAvatar, user.getProfileImageUrl());
				userName.setText(user.getScreenName());
				int verified_type = user.getVerifiedType();
				if (0 == verified_type) {
					icImage.setVisibility(View.VISIBLE);
					icImage.setImageResource(R.drawable.ic_verified);
				} else if (1 < verified_type && verified_type < 10) {
					icImage.setVisibility(View.VISIBLE);
					icImage.setImageResource(R.drawable.ic_verified_blue);
				} else {
					icImage.setVisibility(View.GONE);
				}
				userLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						Intent intent = new Intent();
						intent.putExtra("username", user.getScreenName());
						setResult(WriteActivity.AT_FRIENDS, intent);
						finish();
					}
				});
			}
		}

	}

	private boolean isLoading = false;

	private OnScrollListener mListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				if (view.getLastVisiblePosition() == view.getCount() - 1) {
					if (isLoading)
						return;
					Toast.makeText(AtFriendsActivity.this, "加载中",
							Toast.LENGTH_SHORT).show();
					mFriendshipsAPI.friends(Long.parseLong(mUser.getId()), 20,
							mCursor, true, mCallBack);
				}
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}
	};

	private AjaxCallBack<String> mCallBack = new AjaxCallBack<String>() {

		@Override
		public void onSuccess(String jsonString) {
			super.onSuccess(jsonString);
			UserList list = UserList.parse(jsonString);
			if (list != null && list.users != null) {
				mArrayList.addAll(list.users);
				mAdapter.notifyDataSetChanged();
				mCursor = list.next_cursor;
			} else {
				Toast.makeText(AtFriendsActivity.this, "没有更多好友了",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			Toast.makeText(AtFriendsActivity.this, "加载失败", Toast.LENGTH_SHORT)
					.show();
		}

	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
