package com.mworld.handler;

import net.tsz.afinal.http.AjaxCallBack;
import android.util.Log;
import android.widget.Toast;

import com.mworld.fragment.BaseFragment;
import com.weibo.entities.CommentsList;

public class MyComLoadHandler extends AjaxCallBack<String> {
	private BaseFragment mFragment;

	public MyComLoadHandler(BaseFragment mFregment) {
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
			Toast.makeText(mFragment.getActivity(), "加载失败", Toast.LENGTH_SHORT)
					.show();
		} else {
			mFragment.mArrayList.addAll(commentsList.commentsList);
			mFragment.mAdapter.notifyDataSetChanged();
			Toast.makeText(mFragment.getActivity(),
					"加载了" + commentsList.commentsList.size() + "条评论",
					Toast.LENGTH_SHORT).show();
		}
		mFragment.mList.onRefreshComplete();
	}

	@Override
	public void onFailure(Throwable t, int errorNo, String strMsg) {
		super.onFailure(t, errorNo, strMsg);
		Log.e(mFragment.getClass().getName(), strMsg);
	}
}
