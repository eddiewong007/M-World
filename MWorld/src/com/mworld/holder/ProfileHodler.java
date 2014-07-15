package com.mworld.holder;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.mworld.ui.R;
import com.weibo.entities.User;

public class ProfileHodler {
	private Context mContext;
	public ImageView userAvatar;

	/**
	 * 
	 * @param context
	 * @param view
	 */
	public ProfileHodler(Context context, View view) {
		mContext = context;
		userAvatar = (ImageView) view.findViewById(R.id.user_avatar);
	}

	public void inflate(User user) {
		FinalBitmap fb = FinalBitmap.create(mContext);
		fb.display(userAvatar, user.profile_image_url);
	}
}
