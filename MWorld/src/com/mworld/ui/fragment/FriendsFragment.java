package com.mworld.ui.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.ui.adapter.StatusListAdapter;
import com.mworld.ui.handler.StatusLoadHandler;
import com.mworld.ui.handler.StatusRefHandler;
<<<<<<< HEAD
import com.mworld.ui.main.TimelineInfo;
=======
>>>>>>> origin/dev-2.0
import com.mworld.weibo.api.GroupAPI;
import com.mworld.weibo.api.StatusAPI;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.entities.Group;
import com.mworld.weibo.entities.Status;
import com.mworld.weibo.entities.User;

public class FriendsFragment extends BaseFragment {

<<<<<<< HEAD
	private ArrayList<Group> mGroupList = null;

=======
>>>>>>> origin/dev-2.0
	private int curGroup = 0;

	@SuppressWarnings("unused")
	private Account mAccount;

	@SuppressWarnings("unused")
	private User mUser;

	private String mToken;

	private GroupAPI mGroupAPI;

	private SparseArray<TimelineInfo> mSparsArray = new SparseArray<TimelineInfo>();

	public static FriendsFragment newInstance(Account account, User user,
			String token) {
		FriendsFragment fragment = new FriendsFragment(account, user, token);
		fragment.setArguments(new Bundle());
		return fragment;
	}

	public FriendsFragment(Account account, User user, String token) {
		mAccount = account;
		mUser = user;
		mToken = token;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAPI = new StatusAPI(mToken);
		mGroupAPI = new GroupAPI(mToken);
		mArrayList = new ArrayList<Status>();
		TimelineInfo tlInfo = new TimelineInfo(mArrayList);
		mSparsArray.put(0, tlInfo);
		mAdapter = new StatusListAdapter(getActivity(), mArrayList);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null);
		mList = (PullToRefreshListView) view.findViewById(R.id.home_timeline);
		mList.setAdapter(mAdapter);
		mProgressBar = (ProgressBar) view.findViewById(R.id.loading);
		mProgressBar.setVisibility(View.VISIBLE);
		((StatusAPI) mAPI).friendsTimeline(since_id, 0, 20, 1, false, 0, false,
				new StatusRefHandler(this));
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		mList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				Log.i("Home", "refresh");
<<<<<<< HEAD
				refresh();
=======
				if (curGroup == 0) {
					((StatusAPI) mAPI).friendsTimeline(since_id, 0, 20, 1,
							false, 0, false, new StatusRefHandler(
									FriendsFragment.this));
				} else {
					new GroupAPI(mToken).timeline(curGroup, since_id, 0, 20, 1,
							false, 0,
							new StatusRefHandler(FriendsFragment.this));
				}

>>>>>>> origin/dev-2.0
			}
		});
		mList.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
<<<<<<< HEAD
				load();
=======
				if (isLoading) {
					Toast.makeText(getActivity(), "不要着急，正在加载",
							Toast.LENGTH_SHORT).show();
					mList.onRefreshComplete();
				} else {
					Toast.makeText(getActivity(), "加载中...", Toast.LENGTH_SHORT)
							.show();
					if (curGroup == 0) {
						((StatusAPI) mAPI).friendsTimeline(0, init_id, 20,
								page++, false, 0, false, new StatusLoadHandler(
										FriendsFragment.this));
					} else {
						new GroupAPI(mToken).timeline(curGroup, 0, init_id, 20,
								page++, false, 0, new StatusLoadHandler(
										FriendsFragment.this));
					}
					isLoading = true;
				}
>>>>>>> origin/dev-2.0
			}
		});

	}

<<<<<<< HEAD
	private void refresh() {
		if (curGroup == 0) {
			((StatusAPI) mAPI).friendsTimeline(since_id, 0, 20, 1, false, 0,
					false, new StatusRefHandler(this));
		} else {
			mGroupAPI.timeline(Long.parseLong(mGroupList.get(curGroup - 1).id),
					since_id, 0, 20, 1, false, 0, new StatusRefHandler(this));
		}
	}

	private void load() {
		if (isLoading) {
			Toast.makeText(getActivity(), "不要着急，正在加载", Toast.LENGTH_SHORT)
					.show();
			mList.onRefreshComplete();
		} else {
			Toast.makeText(getActivity(), "加载中...", Toast.LENGTH_SHORT).show();
			if (curGroup == 0) {
				((StatusAPI) mAPI).friendsTimeline(0, init_id, 20, page++,
						false, 0, false, new StatusLoadHandler(this));
			} else {
				mGroupAPI.timeline(
						Long.parseLong(mGroupList.get(curGroup - 1).id), 0,
						init_id, 20, page++, false, 0, new StatusLoadHandler(
								this));
			}
			isLoading = true;
=======
	public void switchGroup(int which) {
		if (curGroup != which) {
			curGroup = which;
			since_id = 0;
			init_id = 0;
			page = 2;
			mArrayList = new ArrayList<Status>();
			if (curGroup == 0) {
				((StatusAPI) mAPI).friendsTimeline(since_id, 0, 20, 1, false,
						0, false, new StatusRefHandler(this));
			} else {
				new GroupAPI(mToken).timeline(curGroup, since_id, 0, 20, 1,
						false, 0, new StatusRefHandler(FriendsFragment.this));
			}
>>>>>>> origin/dev-2.0
		}
	}

	@SuppressWarnings("unchecked")
	public void switchGroup(int which) {
		if (mGroupList == null) {
			mGroupList = GlobalContext.getInstance().getGroup().groupList;
		}
		if (curGroup != which) {
			TimelineInfo tlInfo = mSparsArray.get(curGroup);
			tlInfo.init_id = init_id;
			tlInfo.since_id = since_id;
			tlInfo.page = page;
			tlInfo = mSparsArray.get(which);
			curGroup = which;
			if (tlInfo == null) {
				mArrayList = new ArrayList<Status>();
				((StatusListAdapter) mAdapter).changeData(mArrayList);
				tlInfo = new TimelineInfo(mArrayList);
				mSparsArray.put(which, tlInfo);
				init_id = tlInfo.init_id;
				since_id = tlInfo.since_id;
				page = tlInfo.page;
				mGroupAPI.timeline(
						Long.parseLong(mGroupList.get(curGroup - 1).id),
						since_id, 0, 20, 1, false, 0,
						new StatusRefHandler(this));
			} else {
				init_id = tlInfo.init_id;
				since_id = tlInfo.since_id;
				page = tlInfo.page;
				mArrayList = tlInfo.statuses;
				((StatusListAdapter) mAdapter).changeData(mArrayList);
				refresh();
			}
		}
	}
}