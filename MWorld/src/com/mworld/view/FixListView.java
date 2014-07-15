package com.mworld.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.AbsListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class FixListView extends PullToRefreshListView {

	public static final int SHOW_TAB = 1;
	public static final int HIDE_TAB = 2;

	private Handler handler;

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
		if (firstVisibleItem == 0) {
			Message msg = new Message();
			msg.what = HIDE_TAB;
			msg.obj = null;
			handler.sendMessage(msg);
		} else if (firstVisibleItem == 1) {
			Message msg = new Message();
			msg.what = HIDE_TAB;
			msg.obj = null;
			handler.sendMessage(msg);
		}
		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
	}

}
