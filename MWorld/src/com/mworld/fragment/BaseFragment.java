package com.mworld.fragment;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.BaseAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mworld.handler.StatusLoadHandler;
import com.mworld.handler.StatusRefHandler;
import com.mworld.utils.PreUtils;
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

	protected AjaxCallBack<String> refHandler, loadHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAccessToken = PreUtils.readAccessToken(getActivity());
		refHandler = new StatusRefHandler(this);
		loadHandler = new StatusLoadHandler(this);
	}

}
