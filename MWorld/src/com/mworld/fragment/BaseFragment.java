package com.mworld.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mworld.utils.PrefUtils;
import com.weibo.api.BaseAPI;
import com.weibo.entities.AccessToken;

public class BaseFragment extends Fragment {

	protected AccessToken mAccessToken;

	protected BaseAPI mAPI;

	@SuppressWarnings("rawtypes")
	public ArrayList mArrayList;

	public BaseAdapter mAdapter;

	public long since_id = 0;

	public long init_id = 0;

	protected int page = 2;

	public PullToRefreshListView mList;
	
	public ProgressBar mProgressBar;

	public boolean isRefreshing, isLoading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAccessToken = PrefUtils.readAccessToken(getActivity());

	}

}
