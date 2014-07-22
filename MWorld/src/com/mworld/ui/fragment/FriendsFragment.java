package com.mworld.ui.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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
import com.mworld.ui.adapter.StatusListAdapter;
import com.mworld.ui.handler.StatusLoadHandler;
import com.mworld.ui.handler.StatusRefHandler;
import com.mworld.weibo.api.StatusAPI;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.entities.Status;
import com.mworld.weibo.entities.User;

public class FriendsFragment extends BaseFragment {

	@SuppressWarnings("unused")
	private int curGroup;

	@SuppressWarnings("unused")
	private Account mAccount;

	@SuppressWarnings("unused")
	private User mUser;

	private String mToken;

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
		mArrayList = new ArrayList<Status>();
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
				((StatusAPI) mAPI).friendsTimeline(since_id, 0, 20, 1, false,
						0, false, new StatusRefHandler(FriendsFragment.this));
			}
		});
		mList.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if (isLoading) {
					Toast.makeText(getActivity(), "不要着急，正在加载",
							Toast.LENGTH_SHORT).show();
					mList.onRefreshComplete();
				} else {
					Toast.makeText(getActivity(), "加载中...", Toast.LENGTH_SHORT)
							.show();
					((StatusAPI) mAPI).friendsTimeline(0, init_id, 20, page++,
							false, 0, false, new StatusLoadHandler(
									FriendsFragment.this));
					isLoading = true;
				}
			}
		});

	}

	public void switchGroup(int which) {

	}

}