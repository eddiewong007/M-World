package com.mworld.ui.main;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.ui.adapter.StatusCmtListAdapter;
import com.mworld.weibo.api.CommentAPI;
import com.mworld.weibo.entities.CommentList;
import com.mworld.weibo.entities.Status;

public class CommentActivity extends SwipeBackActivity {

	private Status mStatus;

	@ViewInject(id = R.id.comments_list)
	private ListView mListView;

	@SuppressWarnings("rawtypes")
	private ArrayList mArrayList = new ArrayList();

	private StatusCmtListAdapter mAdapter;

	private CommentAPI mCommentsAPI;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commentactivity_layout);
		FinalActivity.initInjectedView(this);

		getActionBar().setTitle("微博正文");
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mStatus = (Status) getIntent().getParcelableExtra("status");
		mArrayList.add(mStatus);
		// mArrayList.add(GlobalContext.getInstance().getAccount().getUserInfo());
		mAdapter = new StatusCmtListAdapter(this, mArrayList);
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						loadComments();
					}
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

			}
		});
		mCommentsAPI = new CommentAPI(GlobalContext.getInstance().getAccount()
				.getAccessToken());
		loadComments();
	}

	int page = 1;

	boolean isLoading = false;

	private void loadComments() {
		if (page > 1)
			Toast.makeText(this, "正在加载评论...", Toast.LENGTH_SHORT).show();
		if (isLoading) {
			Toast.makeText(this, "不要着急(◐﹏◐)", Toast.LENGTH_SHORT).show();
			return;
		}
		isLoading = true;
		mCommentsAPI.show(mStatus.id, 0, 0, 10, page++, 0,
				new AjaxCallBack<String>() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(String jsonString) {
						super.onSuccess(jsonString);
						CommentList commentList = CommentList.parse(jsonString);
						mArrayList.addAll(commentList.comments);
						mAdapter.notifyDataSetChanged();
						isLoading = false;
					}

				});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
