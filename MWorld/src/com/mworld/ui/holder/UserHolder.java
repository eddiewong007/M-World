package com.mworld.ui.holder;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mworld.R;
import com.mworld.weibo.entities.User;

public class UserHolder {

	private Context mContext;
	private ImageView userAvatar;
	private TextView userName;
	public ImageView icImage;

	public UserHolder(Context context, View view) {
		mContext = context;
		userAvatar = (ImageView) view.findViewById(R.id.user_avatar);
		userName = (TextView) view.findViewById(R.id.user_name);
		icImage = (ImageView) view.findViewById(R.id.ic_image);
	}

	public void inflate(User user) {
		FinalBitmap fb = FinalBitmap.create(mContext);
		fb.display(userAvatar, user.getAvatarLarge());
		userName.setText(user.getScreenName());
		int verified_type = user.getVerifiedType();
		if (0 == verified_type) {
			icImage.setVisibility(View.VISIBLE);
			icImage.setImageResource(R.drawable.ic_verified);
		} else if (1 < verified_type && verified_type < 10) {
			icImage.setVisibility(View.VISIBLE);
			icImage.setImageResource(R.drawable.ic_verified_blue);
		} else {
			icImage.setVisibility(View.GONE);
		}
	}
}