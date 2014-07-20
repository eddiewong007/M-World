package com.mworld.ui.fragment;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mworld.weibo.api.BaseAPI;

public class BaseFragment extends Fragment {

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

}
