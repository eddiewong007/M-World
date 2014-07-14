package com.mworld.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class FixListView extends PullToRefreshListView {

	public FixListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FixListView(
			Context context,
			com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode,
			com.handmark.pulltorefresh.library.PullToRefreshBase.AnimationStyle style) {
		super(context, mode, style);
	}

	public FixListView(Context context,
			com.handmark.pulltorefresh.library.PullToRefreshBase.Mode mode) {
		super(context, mode);
	}

	public FixListView(Context context) {
		super(context);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		
	}

}
