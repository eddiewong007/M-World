package com.mworld.fragment;

import java.util.ArrayList;

import net.tsz.afinal.http.AjaxCallBack;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mworld.adapter.StatusesListAdapter;
import com.mworld.handler.StatusLoadHandler;
import com.mworld.handler.StatusRefreshHandler;
import com.mworld.utils.PreUtils;
import com.weibo.api.StatusesAPI;
import com.weibo.entities.AccessToken;
import com.weibo.entities.Status;

public class BaseFragment extends Fragment {

	protected AccessToken mAccessToken;

	protected StatusesAPI mStatusesAPI;

	public ArrayList<Status> mArrayList;

	public StatusesListAdapter mAdapter;

	public long since_id = 0;

	public long init_id = 0;

	protected int page = 2;

	public PullToRefreshListView mList;

	protected AjaxCallBack<String> refHandler, loadHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAccessToken = PreUtils.readAccessToken(getActivity());
		mStatusesAPI = new StatusesAPI(mAccessToken);
		mArrayList = new ArrayList<Status>();
		mAdapter = new StatusesListAdapter(getActivity(), mArrayList);
		refHandler = new StatusRefreshHandler(this);
		loadHandler = new StatusLoadHandler(this);
	}

}
