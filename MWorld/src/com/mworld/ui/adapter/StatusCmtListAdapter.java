package com.mworld.ui.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mworld.R;
import com.mworld.ui.holder.CommentHolder;
import com.mworld.ui.holder.ProfTabHolder;
import com.mworld.ui.holder.StatusHolder;
import com.mworld.weibo.entities.Comment;
import com.mworld.weibo.entities.Status;
import com.mworld.weibo.entities.User;

public class StatusCmtListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	@SuppressWarnings("rawtypes")
	public ArrayList mArrayList;

	private final int STATUS_TYPE = 0;
	private final int TAB_TYPE = 1;
	private final int CMT_TYPE = 2;

	public StatusCmtListAdapter(Context context, ArrayList<Comment> list) {
		super();
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mArrayList = list;
	}

	@Override
	public int getCount() {
		return mArrayList.size();
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0)
			return STATUS_TYPE;
//		else if (position == 1)
//			return TAB_TYPE;
		else if (position < mArrayList.size()
				&& mArrayList.get(position) instanceof Comment)
			return CMT_TYPE;
		return -1;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public Object getItem(int position) {
		return mArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {

		StatusHolder sHolder = null;
		ProfTabHolder tHolder = null;
		CommentHolder cHolder = null;
		int type = getItemViewType(position);
		if (null == convertView) {
			switch (type) {
			case STATUS_TYPE:
				convertView = mInflater.inflate(
						R.layout.list_item_status, null);
				sHolder = new StatusHolder(mContext, convertView);
				convertView.setTag(sHolder);
				break;
			case TAB_TYPE:
				convertView = mInflater.inflate(R.layout.list_item_profile_tab,
						null);
				tHolder = new ProfTabHolder(mContext, convertView);
				convertView.setTag(tHolder);
				break;
			case CMT_TYPE:
				convertView = mInflater
						.inflate(R.layout.list_item_comment, null);
				cHolder = new CommentHolder(mContext, convertView);
				convertView.setTag(cHolder);
				break;
			default:
				break;
			}

		} else {
			switch (type) {
			case STATUS_TYPE:
				sHolder = (StatusHolder) convertView.getTag();
				break;
			case TAB_TYPE:
				tHolder = (ProfTabHolder) convertView.getTag();
				break;
			case CMT_TYPE:
				cHolder = (CommentHolder) convertView.getTag();
				break;
			default:
				break;
			}

		}

		switch (type) {
		case STATUS_TYPE:
			sHolder.inflate((Status) mArrayList.get(position));
			break;
		case TAB_TYPE:
			tHolder.inflate((User) mArrayList.get(position));
			break;
		case CMT_TYPE:
			cHolder.inflate((Comment) mArrayList.get(position));
			break;
		default:
			break;
		}
		return convertView;
	}

}