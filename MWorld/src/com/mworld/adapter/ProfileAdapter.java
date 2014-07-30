package com.mworld.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mworld.holder.ProfInfoHodler;
import com.mworld.holder.ProfTabHolder;
import com.mworld.holder.StatusHolder;
import com.mworld.holder.UserHolder;
import com.mworld.ui.R;
import com.weibo.entities.Status;
import com.weibo.entities.User;

public class ProfileAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	@SuppressWarnings("rawtypes")
	public ArrayList mArrayList;

	private final int PROFILE_TYPE = 0;
	private final int TAB_TYPE = 1;
	private final int USER_TYPE = 2;
	private final int STATUS_TYPE = 3;

	@SuppressWarnings("rawtypes")
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
		else if (position < mArrayList.size()
				&& mArrayList.get(position) instanceof Status)
			return STATUS_TYPE;
		else if (position < mArrayList.size()
				&& mArrayList.get(position) instanceof User)
			return USER_TYPE;
		return -1;
	}

	@Override
	public int getViewTypeCount() {
		return 4;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ProfInfoHodler iHolder = null;
		ProfTabHolder tHolder = null;
		StatusHolder sHolder = null;
		UserHolder uHolder = null;
		int type = getItemViewType(position);
		if (null == convertView) {
			switch (type) {
			case PROFILE_TYPE:
				convertView = mInflater.inflate(
						R.layout.list_item_profile_info, null);
				iHolder = new ProfInfoHodler(mContext, convertView);
				convertView.setTag(iHolder);
				break;
			case TAB_TYPE:
				convertView = mInflater.inflate(R.layout.list_item_profle_tab,
						null);
				tHolder = new ProfTabHolder(mContext,convertView);
				convertView.setTag(tHolder);
				break;
			case STATUS_TYPE:
				convertView = mInflater
						.inflate(R.layout.list_item_status, null);
				sHolder = new StatusHolder(mContext, convertView);
				convertView.setTag(sHolder);
				break;
			case USER_TYPE:
				convertView = mInflater.inflate(R.layout.list_item_user, null);
				uHolder = new UserHolder(mContext, convertView);
				convertView.setTag(uHolder);
				break;
			default:
				break;
			}

		} else {
			switch (type) {
			case PROFILE_TYPE:
				iHolder = (ProfInfoHodler) convertView.getTag();
				break;
			case TAB_TYPE:
				tHolder = (ProfTabHolder) convertView.getTag();
				break;
			case STATUS_TYPE:
				sHolder = (StatusHolder) convertView.getTag();
				break;
			case USER_TYPE:
				uHolder = (UserHolder) convertView.getTag();
				break;
			default:
				break;
			}

		}

		switch (type) {
		case PROFILE_TYPE:
			iHolder.inflate((User) mArrayList.get(position));
			break;
		case TAB_TYPE:
			tHolder.inflate((User) mArrayList.get(position));
			break;
		case STATUS_TYPE:
			sHolder.inflate((Status) mArrayList.get(position));
			break;
		case USER_TYPE:
			uHolder.inflate((User) mArrayList.get(position));
			break;
		default:
			break;
		}
		return convertView;
	}

}
