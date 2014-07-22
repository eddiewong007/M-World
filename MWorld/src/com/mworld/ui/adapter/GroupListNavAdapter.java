package com.mworld.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.weibo.entities.Account;

public class GroupListNavAdapter extends BaseAdapter {

	private Activity mActivity;

	private String[] mValueArray;

	public GroupListNavAdapter(Activity activity, String[] valueArray) {
		super();
		mActivity = activity;
		mValueArray = valueArray;
	}

	@Override
	public int getCount() {
		return mValueArray.length;
	}

	@Override
	public Object getItem(int position) {
		return mValueArray[position];
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		final ViewHolder holder;

		if (convertView == null || convertView.getTag() == null) {
			LayoutInflater inflater = mActivity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.spinner_selector_text_view,
					container, false);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView;
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position != 0) {
			holder.textView.setText(mValueArray[position]);
		} else {
			Account account = GlobalContext.getInstance().getAccount();
			holder.textView.setText(account.getUserInfo().getScreenName());
		}
		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView,
			ViewGroup container) {
        ViewHolder holder;

        if (convertView == null || convertView.getTag() == null) {
            LayoutInflater inflater = mActivity.getLayoutInflater();
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, container, false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(mValueArray[position]);
        return convertView;

    }
	
	private static class ViewHolder {
		TextView textView;
	}

}
