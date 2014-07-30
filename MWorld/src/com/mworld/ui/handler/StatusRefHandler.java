package com.mworld.ui.handler;

import net.tsz.afinal.http.AjaxCallBack;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mworld.ui.fragment.BaseFragment;
import com.mworld.weibo.entities.StatusList;

public class StatusRefHandler extends AjaxCallBack<String> {

	private BaseFragment mFragment;

	public StatusRefHandler(BaseFragment mFregment) {
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
			Toast.makeText(mFragment.getActivity(), "没有更新", Toast.LENGTH_SHORT)
					.show();
		} else {
			if (0 == mFragment.init_id)
				mFragment.init_id = statusList.statuses.get(0).id;
			mFragment.since_id = statusList.statuses.get(0).id;
			mFragment.mArrayList.addAll(0, statusList.statuses);
		}
		mFragment.mAdapter.notifyDataSetChanged();

		if (null != mFragment.mProgressBar)
			mFragment.mProgressBar.setVisibility(View.GONE);
		mFragment.mList.onRefreshComplete();
	}

	@Override
	public void onFailure(Throwable t, int errorNo, String strMsg) {
		super.onFailure(t, errorNo, strMsg);
		Log.e(mFragment.getClass().getName(), strMsg);
	}

}