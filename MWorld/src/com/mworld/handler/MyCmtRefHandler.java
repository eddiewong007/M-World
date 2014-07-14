package com.mworld.handler;

import net.tsz.afinal.http.AjaxCallBack;
import android.util.Log;
import android.widget.Toast;

import com.mworld.fragment.BaseFragment;
import com.weibo.entities.CommentsList;

public class MyCmtRefHandler extends AjaxCallBack<String> {

	private BaseFragment mFragment;

	public MyCmtRefHandler(BaseFragment mFregment) {
		this.mFragment = mFregment;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(String jsonString) {
		super.onSuccess(jsonString);
		CommentsList commentsList = new CommentsList();

		try {
			commentsList = CommentsList.parse(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (commentsList.commentsList == null
				|| commentsList.commentsList.isEmpty()) {
			Toast.makeText(mFragment.getActivity(), "没有更新", Toast.LENGTH_SHORT)
					.show();
		} else {
			if (0 == mFragment.init_id)
				mFragment.init_id = commentsList.commentsList.get(0).id;
			mFragment.since_id = commentsList.commentsList.get(0).id;
			mFragment.mArrayList.addAll(0, commentsList.commentsList);
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
