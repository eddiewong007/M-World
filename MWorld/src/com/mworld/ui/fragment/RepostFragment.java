package com.mworld.ui.fragment;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.os.Bundle;
<<<<<<< HEAD
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mworld.ui.adapter.StatusListAdapter;
import com.mworld.weibo.api.StatusAPI;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.entities.Status;
import com.mworld.weibo.entities.StatusList;
import com.mworld.weibo.entities.User;

public class RepostFragment extends BaseFragment {

	@SuppressWarnings("unused")
	private Account mAccount;

=======
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.mworld.R;
import com.mworld.ui.adapter.StatusListAdapter;
import com.mworld.weibo.api.FavoritesAPI;
import com.mworld.weibo.api.StatusAPI;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.entities.FavoriteList;
import com.mworld.weibo.entities.Status;
import com.mworld.weibo.entities.User;

public class RepostFragment extends BaseFragment {
	@SuppressWarnings("unused")
	private Account mAccount;

	@SuppressWarnings("unused")
>>>>>>> origin/dev-2.0
	private User mUser;

	private String mToken;

	public static RepostFragment newInstance(Account account, User user,
			String token) {
		RepostFragment fragment = new RepostFragment(account, user, token);
		fragment.setArguments(new Bundle());
		return fragment;
	}

	public RepostFragment(Account account, User user, String token) {
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
<<<<<<< HEAD
		mList = new PullToRefreshListView(getActivity());
		mList.setAdapter(mAdapter);
		((StatusAPI) mAPI).userTimeline(Long.parseLong(mUser.getId()),
				since_id, 0, 40, 1, false, 0, false, new RetRefHandler());
		return mList;
	}

	@Override
	public void onStart() {
		super.onStart();

		mList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				Log.i("At", "refresh");
				((StatusAPI) mAPI).userTimeline(Long.parseLong(mUser.getId()),
						since_id, 0, 40, 1, false, 0, false,
						new RetRefHandler());
			}
		});
		mList.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(getActivity(), "正在加载微博", Toast.LENGTH_SHORT)
						.show();
				((StatusAPI) mAPI).userTimeline(Long.parseLong(mUser.getId()),
						0, init_id, 40, page++, false, 0, false,
						new RetLoadHandler());
			}
		});
	}

	private class RetRefHandler extends AjaxCallBack<String> {

		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(String jsonString) {
			super.onSuccess(jsonString);
			StatusList statusList = new StatusList();
			try {
				statusList = StatusList.parse(jsonString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (statusList.statuses == null || statusList.statuses.isEmpty()) {
				Toast.makeText(getActivity(), "没有更新", Toast.LENGTH_SHORT)
						.show();
			} else {
				if (0 == init_id)
					init_id = statusList.statuses.get(0).id;
				since_id = statusList.statuses.get(0).id;
				for (int i = statusList.statuses.size() - 1; i >= 0; i--)
					if (statusList.statuses.get(i).retweeted_status != null)
						mArrayList.add(0,
								statusList.statuses.get(i).retweeted_status);
				mAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			Log.e(getClass().getName(), strMsg);
		}

	}

	private class RetLoadHandler extends AjaxCallBack<String> {

		@SuppressWarnings("unchecked")
		@Override
		public void onSuccess(String jsonString) {
			super.onSuccess(jsonString);
			StatusList statusList = new StatusList();
			try {
				statusList = StatusList.parse(jsonString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (statusList.statuses == null || statusList.statuses.isEmpty()) {
				Toast.makeText(getActivity(), "这就没了〒_〒", Toast.LENGTH_SHORT)
						.show();
			} else {
				int count = 0;
				for (Status status : statusList.statuses)
					if (status.retweeted_status != null) {
						mArrayList.add(status);
						count++;
					}
				mAdapter.notifyDataSetChanged();
				Toast.makeText(getActivity(), "加载了" + count + "条微博",
						Toast.LENGTH_SHORT).show();
			}

			mList.onRefreshComplete();
			isLoading = false;
		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			Log.e(getClass().getName(), strMsg);
		}

	}

=======

		PullToRefreshListView listView = (PullToRefreshListView) inflater
				.inflate(R.layout.listview_pulltorefresh, null);
		listView.setAdapter(mAdapter);
		loadRepost();
		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				loadRepost();
			}
		});
		return listView;
	}

	private int page = 1;

	private void loadRepost() {
		if (isLoading) {
			Toast.makeText(getActivity(), "正在加载，(◐﹏◐)不要着急", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		((StatusAPI) mAPI).userTimeline(20, page++, new AjaxCallBack<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String jsonString) {
				super.onSuccess(jsonString);
				isLoading = false;
				FavoriteList favList = FavoriteList.parse(jsonString);
				if (favList.statuses == null || favList.statuses.size() == 0) {
					Toast.makeText(getActivity(), "没有更多收藏了", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				mArrayList.addAll(favList.statuses);
				mAdapter.notifyDataSetChanged();
			}

		});
	}
>>>>>>> origin/dev-2.0
}
