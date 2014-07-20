package com.mworld.ui.holder;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mworld.R;
import com.mworld.weibo.entities.User;

public class ProfInfoHodler {
	private Context mContext;
	public ImageView userAvatar;
	public TextView userName;
	public TextView userLocation;

	/**
	 * 
	 * @param context
	 * @param view
	 */
	public ProfInfoHodler(Context context, View view) {
		mContext = context;
		userAvatar = (ImageView) view.findViewById(R.id.user_avatar);
		userName = (TextView) view.findViewById(R.id.user_name);
		userLocation = (TextView) view.findViewById(R.id.user_location);
	}

	public void inflate(User user) {
		FinalBitmap fb = FinalBitmap.create(mContext);
		fb.display(userAvatar, user.getAvatarLarge());
		userName.setText(user.getScreenName());
		userLocation.setText(user.getLocation());
	}
}