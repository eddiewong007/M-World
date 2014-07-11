package com.mworld.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mworld.ui.DisplayActivity;
import com.mworld.ui.R;
import com.mworld.utils.TimeUtils;
import com.weibo.entities.Comment;

public class MyComListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	public ArrayList<Comment> mCommentsList;

	public MyComListAdapter(Context context, ArrayList<Comment> list) {
		super();
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mCommentsList = list;
	}

	@Override
	public int getCount() {
		return mCommentsList.size();
	}

	@Override
	public Object getItem(int position) {
		return mCommentsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {

		ViewHolder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.list_item_status, null);
			holder = new ViewHolder();
			holder.userAvatar = (ImageView) convertView
					.findViewById(R.id.user_avatar);
			holder.userName = (TextView) convertView
					.findViewById(R.id.user_name);
			holder.date = (TextView) convertView.findViewById(R.id.text_from);
			holder.textCom = (TextView) convertView
					.findViewById(R.id.text_status);
			holder.textStatus = (TextView) convertView
					.findViewById(R.id.retweet_text_status);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Comment comment = mCommentsList.get(position);

		FinalBitmap.create(mContext).display(holder.userAvatar,
				comment.user.avatar_large);
		holder.userAvatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("adapter", "click");
				Intent intent = new Intent(mContext, DisplayActivity.class);
				intent.putExtra("type", 3);
				intent.putExtra("uid", String.valueOf(comment.user.id));
				mContext.startActivity(intent);
			}
		});
		holder.userName.setText(comment.user.screen_name);
		holder.date.setText(TimeUtils.parse(comment.created_at));
		holder.textCom.setText(comment.text);
		if (null == comment.status) {
			convertView.findViewById(R.id.retweet_layout).setVisibility(
					View.GONE);
		} else if (null != comment.status.user) {
			convertView.findViewById(R.id.retweet_layout).setVisibility(
					View.VISIBLE);
			holder.textStatus.setText("评论 @" + comment.status.user.screen_name
					+ "的微博:\n" + comment.status.text);
		}

		return convertView;
	}

	private class ViewHolder {
		ImageView userAvatar;
		TextView userName;
		TextView date;
		TextView textCom;
		TextView textStatus;
	}

}
