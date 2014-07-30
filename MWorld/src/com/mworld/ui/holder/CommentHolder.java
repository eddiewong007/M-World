package com.mworld.ui.holder;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mworld.R;
import com.mworld.support.utils.TimeUtils;
import com.mworld.ui.main.ProfileActivity;
import com.mworld.ui.main.WriteActivity;
import com.mworld.weibo.entities.Comment;

public class CommentHolder {
	private Context mContext;
	public View layoutComment;
	public ImageView userAvatar;
	public TextView userName;
	public TextView textFrom;
	public ImageView icImage;
	public TextView textComment;
	public ImageView cmtBtn;

	/**
	 * 
	 * @param context
	 * @param view
	 */
	public CommentHolder(Context context, View view) {
		mContext = context;
		layoutComment = view.findViewById(R.id.layout_comment);
		userAvatar = (ImageView) view.findViewById(R.id.user_avatar);
		userName = (TextView) view.findViewById(R.id.user_name);
		textFrom = (TextView) view.findViewById(R.id.text_from);
		icImage = (ImageView) view.findViewById(R.id.ic_image);
		textComment = (TextView) view.findViewById(R.id.text_comment);
		cmtBtn = (ImageView) view.findViewById(R.id.cmt_btn);
	}

	/**
	 * 
	 * @param status
	 */
	public void inflate(final Comment comment) {
		FinalBitmap fb = FinalBitmap.create(mContext);
		fb.display(userAvatar, comment.user.getAvatarLarge());
		String screenName = comment.user.getScreenName();
		if (!TextUtils.isEmpty(comment.user.getRemark()))
			screenName += "(" + comment.user.getRemark() + ")";
		userName.setText(screenName);
		textFrom.setText(Html.fromHtml(comment.source + "·"
				+ TimeUtils.getTime(comment.created_at)));
		// int verified_type = comment.user.getVerifiedType();
		// if (0 == verified_type) {
		// icImage.setVisibility(View.VISIBLE);
		// icImage.setImageResource(R.drawable.ic_verified);
		// } else if (1 < verified_type && verified_type < 10) {
		// icImage.setVisibility(View.VISIBLE);
		// icImage.setImageResource(R.drawable.ic_verified_blue);
		// } else {
		// icImage.setVisibility(View.GONE);
		// }
		textComment.setText(comment.text);

		userAvatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("adapter", "click");
				Intent intent = new Intent(mContext, ProfileActivity.class);
				intent.putExtra("user", comment.user);
				mContext.startActivity(intent);
			}
		});

		cmtBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, WriteActivity.class);
				intent.putExtra("title", "回复");
				intent.putExtra("comment", comment);
				mContext.startActivity(intent);
			}
		});
	}
}
