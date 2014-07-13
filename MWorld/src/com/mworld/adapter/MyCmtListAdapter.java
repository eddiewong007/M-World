package com.mworld.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mworld.holder.MyCmtHolder;
import com.mworld.ui.R;
import com.weibo.entities.Comment;

public class MyCmtListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	public ArrayList<Comment> mCommentsList;

	public MyCmtListAdapter(Context context, ArrayList<Comment> list) {
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
		MyCmtHolder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.list_item_status, null);
			holder = new MyCmtHolder(mContext, convertView);
			convertView.setTag(holder);
		} else {
			holder = (MyCmtHolder) convertView.getTag();
		}
		final Comment comment = mCommentsList.get(position);
		holder.inflate(comment);

		return convertView;
	}

}
