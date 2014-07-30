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

import com.mworld.ui.ProfileActivity;
import com.mworld.ui.R;
import com.mworld.utils.TimeUtils;
import com.weibo.entities.Comment;

public class StatusCmtListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	public ArrayList<Comment> mCommentsList;

	public StatusCmtListAdapter(Context context, ArrayList<Comment> list) {
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
			convertView = mInflater.inflate(R.layout.list_item_comment, null);
			holder = new ViewHolder();
			holder.userAvatar = (ImageView) convertView
					.findViewById(R.id.user_com_avatar);
			holder.userName = (TextView) convertView
					.findViewById(R.id.user_com_name);
			holder.date = (TextView) convertView.findViewById(R.id.com_date);
			holder.textComment = (TextView) convertView
					.findViewById(R.id.text_comment);
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
				Intent intent = new Intent(mContext, ProfileActivity.class);
				intent.putExtra("user", comment.user);
				mContext.startActivity(intent);
			}
		});
		holder.userName.setText(comment.user.screen_name);
		holder.date.setText(TimeUtils.getTime(comment.created_at));
		holder.textComment.setText(comment.text);
		return convertView;
	}

	private class ViewHolder {
		ImageView userAvatar;
		TextView userName;
		TextView date;
		TextView textComment;
	}

}
