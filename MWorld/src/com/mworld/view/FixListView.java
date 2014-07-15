package com.mworld.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class FixListView extends ListView implements OnScrollListener {

	private OnScrollListener mScrollListener;

	public FixListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FixListView(Context context) {
		super(context);
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		if (l != this) {
			mScrollListener = l;
		}
		super.setOnScrollListener(this);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		Log.i("FixListView", "OnScroll");
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

}
