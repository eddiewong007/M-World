package com.mworld.adapter;

import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import android.app.Activity;
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
import com.mworld.weibo.api.Comment;
import com.mworld.weibo.entities.Status;

public class CommentsListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	public ArrayList<Comment> mCommentsList;

	public CommentsListAdapter(Context context) {
		super();
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mCommentsList = new ArrayList<Comment>();
		for (int i = 0; i < 3; i++) {
			mCommentsList.add(new Comment());
		}
	}

	public CommentsListAdapter(Context context, ArrayList<Comment> list) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {

		ViewHolder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.status_list_item, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Comment comment = mCommentsList.get(position);

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
