package com.mworld.ui.fragment;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mworld.support.utils.GlobalContext;
import com.mworld.ui.adapter.StatusListAdapter;
import com.mworld.weibo.api.FavoritesAPI;
import com.mworld.weibo.api.StatusAPI;
import com.mworld.weibo.entities.FavoriteList;
import com.mworld.weibo.entities.Status;

import android.R.integer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

public class MyFavFragment extends BaseFragment {

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAPI = new FavoritesAPI(GlobalContext.getInstance().getAccount()
				.getAccessToken());
		mArrayList = new ArrayList<Status>();
		mAdapter = new StatusListAdapter(getActivity(), mArrayList);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		PullToRefreshListView listView = new PullToRefreshListView(
				getActivity());
		listView.setAdapter(mAdapter);
		((FavoritesAPI) mAPI).favorites(20, page++, new AjaxCallBack<String>() {

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String jsonString) {
				super.onSuccess(jsonString);
				FavoriteList favList = FavoriteList.parse(jsonString);
				mArrayList.addAll(favList.statuses);
				mAdapter.notifyDataSetChanged();
			}

		});
		return listView;
	}

	int page = 1;

}
