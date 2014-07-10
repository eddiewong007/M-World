package com.mworld.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;
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

import com.mworld.ui.CommentsActivity;
import com.mworld.ui.DisplayActivity;
import com.mworld.ui.R;
import com.weibo.entities.Status;

public class StatusesListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	public ArrayList<Status> mStatusesList;

	public StatusesListAdapter(Context context) {
		super();
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mStatusesList = new ArrayList<Status>();
		for (int i = 0; i < 3; i++) {
			mStatusesList.add(new Status());
		}
	}

	public StatusesListAdapter(Context context, ArrayList<Status> list) {
		super();
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mStatusesList = list;
	}

	@Override
	public int getCount() {
		return mStatusesList.size();
	}

	@Override
	public Object getItem(int position) {
		return mStatusesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {

		ViewHolder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.status_list_item, null);
			holder = new ViewHolder();
			holder.userAvatar = (ImageView) convertView
					.findViewById(R.id.user_avatar);
			holder.userName = (TextView) convertView
					.findViewById(R.id.user_name);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.textStatus = (TextView) convertView
					.findViewById(R.id.text_status);
			holder.textRepost = (TextView) convertView
					.findViewById(R.id.text_repost);
			holder.repostCount = (TextView) convertView
					.findViewById(R.id.repost_count);
			holder.retCount = (TextView) convertView
					.findViewById(R.id.ret_count);
			holder.comCount = (TextView) convertView
					.findViewById(R.id.com_count);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Status status = mStatusesList.get(position);

		FinalBitmap.create(mContext).display(holder.userAvatar,
				status.user.avatar_large);
		holder.userAvatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("adapter", "click");
				Intent intent = new Intent(mContext, DisplayActivity.class);
				intent.putExtra("type", 3);
				intent.putExtra("uid", String.valueOf(status.user.id));
				mContext.startActivity(intent);
			}
		});
		holder.userName.setText(status.user.screen_name);
		holder.date.setText(status.created_at);
		holder.textStatus.setText(status.text);
		if (null == status.retweeted_status) {
			convertView.findViewById(R.id.layout_repost).setVisibility(
					View.GONE);
		} else if (null != status.retweeted_status.user) {
			convertView.findViewById(R.id.layout_repost).setVisibility(
					View.VISIBLE);
			holder.textRepost.setText("@"
					+ status.retweeted_status.user.screen_name + ":\n"
					+ status.retweeted_status.text);
			holder.repostCount.setText("转发 "
					+ status.retweeted_status.reposts_count + " 评论 "
					+ status.retweeted_status.comments_count);
		}
		holder.retCount.setText(String.valueOf(status.reposts_count));
		holder.comCount.setText(String.valueOf(status.comments_count));

		convertView.findViewById(R.id.layout_message).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,
								CommentsActivity.class);
						intent.putExtra("id", status.id);
						mContext.startActivity(intent);
					}
				});

		return convertView;
	}

	private class ViewHolder {
		ImageView userAvatar;
		TextView userName;
		TextView date;
		TextView textStatus;
		TextView textRepost;
		TextView repostCount;
		TextView retCount;
		TextView comCount;
	}

}
