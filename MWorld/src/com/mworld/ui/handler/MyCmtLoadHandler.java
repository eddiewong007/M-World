package com.mworld.ui.handler;

import net.tsz.afinal.http.AjaxCallBack;
import android.util.Log;
import android.widget.Toast;

import com.mworld.ui.fragment.BaseFragment;
import com.mworld.weibo.entities.CommentList;

public class MyCmtLoadHandler extends AjaxCallBack<String> {
	private BaseFragment mFragment;

	public MyCmtLoadHandler(BaseFragment mFregment) {
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
			Toast.makeText(mFragment.getActivity(), "加载失败", Toast.LENGTH_SHORT)
					.show();
		} else {
			mFragment.mArrayList.addAll(commentsList.comments);
			mFragment.mAdapter.notifyDataSetChanged();
			Toast.makeText(mFragment.getActivity(),
					"加载了" + commentsList.comments.size() + "条评论",
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