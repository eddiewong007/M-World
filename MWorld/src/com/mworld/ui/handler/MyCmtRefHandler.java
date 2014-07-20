package com.mworld.ui.handler;

import net.tsz.afinal.http.AjaxCallBack;
import android.util.Log;
import android.widget.Toast;

import com.mworld.ui.fragment.BaseFragment;
import com.mworld.weibo.entities.CommentList;

public class MyCmtRefHandler extends AjaxCallBack<String> {

	private BaseFragment mFragment;

	public MyCmtRefHandler(BaseFragment mFregment) {
		this.mFragment = mFregment;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(String jsonString) {
		super.onSuccess(jsonString);
		CommentList commentsList = new CommentList();

		try {
			commentsList = CommentList.parse(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (commentsList.comments == null
				|| commentsList.comments.isEmpty()) {
			Toast.makeText(mFragment.getActivity(), "没有更新", Toast.LENGTH_SHORT)
					.show();
		} else {
			if (0 == mFragment.init_id)
				mFragment.init_id = commentsList.comments.get(0).id;
			mFragment.since_id = commentsList.comments.get(0).id;
			mFragment.mArrayList.addAll(0, commentsList.comments);
			mFragment.mAdapter.notifyDataSetChanged();
		}

		mFragment.mList.onRefreshComplete();
	}

	@Override
	public void onFailure(Throwable t, int errorNo, String strMsg) {
		super.onFailure(t, errorNo, strMsg);
		Log.e(mFragment.getClass().getName(), strMsg);
	}
}
