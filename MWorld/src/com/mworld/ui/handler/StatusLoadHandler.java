package com.mworld.ui.handler;

import net.tsz.afinal.http.AjaxCallBack;
import android.util.Log;
import android.widget.Toast;

import com.mworld.ui.fragment.BaseFragment;
import com.mworld.weibo.entities.StatusList;

public class StatusLoadHandler extends AjaxCallBack<String> {

	private BaseFragment mFragment;

	public StatusLoadHandler(BaseFragment mFregment) {
		this.mFragment = mFregment;
	}

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
			Toast.makeText(mFragment.getActivity(), "加载失败", Toast.LENGTH_SHORT)
					.show();
		} else {
			mFragment.mArrayList.addAll(statusList.statuses);
			mFragment.mAdapter.notifyDataSetChanged();
			Toast.makeText(mFragment.getActivity(),
					"加载了" + statusList.statuses.size() + "条微博",
					Toast.LENGTH_SHORT).show();
		}

		mFragment.mList.onRefreshComplete();
		mFragment.isLoading = false;
	}

	@Override
	public void onFailure(Throwable t, int errorNo, String strMsg) {
		super.onFailure(t, errorNo, strMsg);
		Log.e(mFragment.getClass().getName(), strMsg);
	}

}
