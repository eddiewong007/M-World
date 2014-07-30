package com.mworld.ui.fragment;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mworld.R;
import com.mworld.ui.adapter.StatusListAdapter;
import com.mworld.weibo.api.FavoritesAPI;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.entities.FavoriteList;
import com.mworld.weibo.entities.Status;
import com.mworld.weibo.entities.User;

public class MyFavFragment extends BaseFragment {

	@SuppressWarnings("unused")
	private Account mAccount;

	@SuppressWarnings("unused")
	private User mUser;

	private String mToken;

	public static MyFavFragment newInstance(Account account, User user,
			String token) {
		MyFavFragment fragment = new MyFavFragment(account, user, token);
		fragment.setArguments(new Bundle());
		return fragment;
	}

	public MyFavFragment(Account account, User user, String token) {
		mAccount = account;
		mUser = user;
		mToken = token;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAPI = new FavoritesAPI(mToken);
		mArrayList = new ArrayList<Status>();
		mAdapter = new StatusListAdapter(getActivity(), mArrayList);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		PullToRefreshListView listView = (PullToRefreshListView) inflater
				.inflate(R.layout.listview_pulltorefresh, null);
		listView.setAdapter(mAdapter);
		loadFavorate();
		listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				loadFavorate();
			}
		});
		return listView;
	}

	private int page = 1;

	private void loadFavorate() {
		if (isLoading) {
			Toast.makeText(getActivity(), "正在加载，(◐﹏◐)不要着急", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		((FavoritesAPI) mAPI).favorites(20, page++, new AjaxCallBack<String>() {

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

}
