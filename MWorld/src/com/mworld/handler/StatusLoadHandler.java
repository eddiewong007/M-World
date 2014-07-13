package com.mworld.handler;

import net.tsz.afinal.http.AjaxCallBack;
import android.util.Log;
import android.widget.Toast;

import com.mworld.fragment.BaseFragment;
import com.weibo.entities.StatusesList;

public class StatusLoadHandler extends AjaxCallBack<String> {

	private BaseFragment mFragment;

	public StatusLoadHandler(BaseFragment mFregment) {
		this.mFragment = mFregment;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(String jsonString) {
		super.onSuccess(jsonString);
		StatusesList statusList = new StatusesList();
		try {
			statusList = StatusesList.parse(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (statusList.statusesList == null
				|| statusList.statusesList.isEmpty()) {
			Toast.makeText(mFragment.getActivity(), "加载失败", Toast.LENGTH_SHORT)
					.show();
		} else {
			mFragment.mArrayList.addAll(statusList.statusesList);
			mFragment.mAdapter.notifyDataSetChanged();
			Toast.makeText(mFragment.getActivity(),
					"加载了" + statusList.statusesList.size() + "条微博",
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
