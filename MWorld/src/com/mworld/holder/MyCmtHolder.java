package com.mworld.holder;

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

import com.mworld.ui.CommentsActivity;
import com.mworld.ui.ProfileActivity;
import com.mworld.ui.R;
import com.mworld.utils.TimeUtils;
import com.weibo.entities.Comment;

public class MyCmtHolder {

	private Context mContext;
	public View layoutMessage;
	public ImageView userAvatar;
	public TextView userName;
	public TextView textFrom;
	public ImageView icImage;
	public TextView textComment;
	public View layoutThumbnailPic;
	public View retweetLayout;
	public TextView retweetTextStatus;
	public View layoutRetweetThumbnailPic;
	public TextView retweetCount;
	public View countLayout;

	/**
	 * 
	 * @param context
	 * @param view
	 */
	public MyCmtHolder(Context context, View view) {
		mContext = context;
		layoutMessage = view.findViewById(R.id.layout_message);
		userAvatar = (ImageView) view.findViewById(R.id.user_avatar);
		userName = (TextView) view.findViewById(R.id.user_name);
		textFrom = (TextView) view.findViewById(R.id.text_from);
		icImage = (ImageView) view.findViewById(R.id.ic_image);
		textComment = (TextView) view.findViewById(R.id.text_status);
		layoutThumbnailPic = view.findViewById(R.id.layout_thumbnail_pic);
		layoutThumbnailPic.setVisibility(View.GONE);
		retweetLayout = view.findViewById(R.id.retweet_layout);
		retweetTextStatus = (TextView) view
				.findViewById(R.id.retweet_text_status);
		layoutRetweetThumbnailPic = view
				.findViewById(R.id.layout_retweet_thumbnail_pic);
		layoutRetweetThumbnailPic.setVisibility(View.GONE);
		retweetCount = (TextView) view.findViewById(R.id.retweet_count);
		retweetCount.setVisibility(View.GONE);
		countLayout = view.findViewById(R.id.count_layout);
		countLayout.setVisibility(View.GONE);
	}

	/**
	 * 
	 * @param status
	 */
	public void inflate(final Comment comment) {
		FinalBitmap fb = FinalBitmap.create(mContext);
		fb.display(userAvatar, comment.user.avatar_large);
		String screenName = comment.user.screen_name;
		if (!TextUtils.isEmpty(comment.user.remark))
			screenName += "(" + comment.user.remark + ")";
		userName.setText(screenName);
		textFrom.setText(Html.fromHtml(comment.source + "·"
				+ TimeUtils.getTime(comment.created_at)));
		int verified_type = comment.user.verified_type;
		if (0 == verified_type) {
			icImage.setVisibility(View.VISIBLE);
			icImage.setImageResource(R.drawable.ic_verified);
		} else if (1 < verified_type && verified_type < 10) {
			icImage.setVisibility(View.VISIBLE);
			icImage.setImageResource(R.drawable.ic_verified_blue);
		} else {
			icImage.setVisibility(View.GONE);
		}
		textComment.setText(comment.text);

		if (null == comment.status && null == comment.reply_comment) {
			retweetLayout.setVisibility(View.GONE);
		} else if (null == comment.reply_comment && null != comment.status) {
			retweetLayout.setVisibility(View.VISIBLE);
			retweetTextStatus.setText("评论 @" + comment.status.user.screen_name
					+ " 的微博:" + comment.status.text);
		} else if (null != comment.reply_comment) {
			retweetLayout.setVisibility(View.VISIBLE);
			retweetTextStatus.setText("回复 @"
					+ comment.reply_comment.user.screen_name + " 的评论:"
					+ comment.reply_comment.text);
		}

		userAvatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("adapter", "click");
				Intent intent = new Intent(mContext, ProfileActivity.class);
				intent.putExtra("user", comment.user);
				mContext.startActivity(intent);
			}
		});

		layoutMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, CommentsActivity.class);
				intent.putExtra("status", comment.status);
				mContext.startActivity(intent);
			}
		});
	}
}
