package com.mworld.handler;

import android.support.v4.app.Fragment;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class StatusRefreshHandler implements RequestListener {

	private Fragment mFregment;


	@Override
	public void onComplete(String jsonString) {
//		StatusesList statusList = new StatusesList();
//		try {
//			statusList = StatusesList.parse(jsonString);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (statusList.statusesList == null
//				|| statusList.statusesList.isEmpty()) {
//			Toast.makeText(mFregment.getActivity(), "没有更新的微博",
//					Toast.LENGTH_SHORT).show();
//		} else {
//			if (0 == init_id)
//				init_id = statusList.statusesList.get(0).id;
//			since_id = statusList.statusesList.get(0).id;
//			mArrayList.addAll(0, statusList.statusesList);
//			mAdapter.notifyDataSetChanged();
//		}
//
//		mList.onRefreshComplete();
	}

	@Override
	public void onWeiboException(WeiboException arg0) {

	}

}
