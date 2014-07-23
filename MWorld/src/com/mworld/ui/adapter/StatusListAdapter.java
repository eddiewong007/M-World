package com.mworld.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mworld.R;
import com.mworld.ui.holder.StatusHolder;
import com.mworld.weibo.entities.Status;

public class StatusListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	public ArrayList<Status> mStatusesList;

	public StatusListAdapter(Context context, ArrayList<Status> list) {
		super();
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mStatusesList = list;
	}

	public void changeData(ArrayList<Status> statusesList) {
		mStatusesList = statusesList;
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {

		StatusHolder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.list_item_status, null);
			holder = new StatusHolder(mContext, convertView);
			convertView.setTag(holder);
		} else {
			holder = (StatusHolder) convertView.getTag();
		}

		final Status status = mStatusesList.get(position);
		holder.inflate(status);
		return convertView;
	}

}
