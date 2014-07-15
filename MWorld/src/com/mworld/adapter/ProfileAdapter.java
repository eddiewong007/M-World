package com.mworld.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mworld.holder.ProfileHodler;
import com.mworld.ui.R;
import com.weibo.entities.User;

public class ProfileAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	public ArrayList mArrayList;

	private final int PROFILE_TYPE = 0;
	private final int TAB_TYPE = 1;
	private final int ITEM_TYPE = 2;

	public ProfileAdapter(Context mContext, ArrayList list) {
		super();
		this.mContext = mContext;
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
			return PROFILE_TYPE;
		else if (position == 1)
			return TAB_TYPE;
		else
			return ITEM_TYPE;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ProfileHodler holder = null;
		int type = getItemViewType(position);
		if (null == convertView) {
			switch (type) {
			case PROFILE_TYPE:
				convertView = mInflater
						.inflate(R.layout.list_item_status, null);
				holder = new ProfileHodler(mContext, convertView);
				convertView.setTag(holder);
				break;
			case TAB_TYPE:
				break;
			case ITEM_TYPE:
				break;
			default:
				break;
			}

		} else {
			switch (type) {
			case PROFILE_TYPE:
				holder = (ProfileHodler) convertView.getTag();
				break;
			case TAB_TYPE:
				break;
			case ITEM_TYPE:
				break;
			default:
				break;
			}

		}

		switch (type) {
		case PROFILE_TYPE:
			holder.inflate((User) mArrayList.get(position));
			break;
		case TAB_TYPE:
			break;
		case ITEM_TYPE:
			break;
		default:
			break;
		}
		return convertView;
	}

}
