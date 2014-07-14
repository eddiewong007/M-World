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

import com.mworld.displayer.ClipDisplayer;
import com.mworld.ui.CommentsActivity;
import com.mworld.ui.DisplayActivity;
import com.mworld.ui.R;
import com.mworld.utils.StatusUtils;
import com.mworld.utils.TimeUtils;
import com.weibo.entities.Status;

/**
 * 
 * @author YMM
 * 
 */
public class StatusHolder {

	private static final int maxHeight = 400;

	private Context mContext;
	public View layoutMessage;
	public ImageView userAvatar;
	public TextView userName;
	public TextView textFrom;
	public ImageView icImage;
	public TextView textStatus;
	public View layoutThumbnailPic;
	public ImageView thumbnailPic;
	public ImageView icGif;
	public View layoutMultiPic;
	public ImageView[] imageView = new ImageView[9];
	public View retweetLayout;
	public TextView retweetTextStatus;
	public View layoutRetweetThumbnailPic;
	public ImageView retweetThumbnailPic;
	public ImageView retweetIcGif;
	public View retweetLayoutMultiPic;
	public ImageView[] retweetImageView = new ImageView[9];
	public TextView retweetCount;
	public TextView textRet;
	public TextView textCmt;
	public ImageView btnSd;

	/**
	 * 
	 * @param context
	 * @param view
	 */
	public StatusHolder(Context context, View view) {
		mContext = context;
		layoutMessage = view.findViewById(R.id.layout_message);
		userAvatar = (ImageView) view.findViewById(R.id.user_avatar);
		userName = (TextView) view.findViewById(R.id.user_name);
		textFrom = (TextView) view.findViewById(R.id.text_from);
		icImage = (ImageView) view.findViewById(R.id.ic_image);
		textStatus = (TextView) view.findViewById(R.id.text_status);
		layoutThumbnailPic = view.findViewById(R.id.layout_thumbnail_pic);
		thumbnailPic = (ImageView) view.findViewById(R.id.thumbnail_pic);
		icGif = (ImageView) view.findViewById(R.id.ic_gif);
		layoutMultiPic = view.findViewById(R.id.layout_multi_pic);
		imageView[0] = (ImageView) view.findViewById(R.id.imageView1);
		imageView[1] = (ImageView) view.findViewById(R.id.imageView2);
		imageView[2] = (ImageView) view.findViewById(R.id.imageView3);
		imageView[3] = (ImageView) view.findViewById(R.id.imageView4);
		imageView[4] = (ImageView) view.findViewById(R.id.imageView5);
		imageView[5] = (ImageView) view.findViewById(R.id.imageView6);
		imageView[6] = (ImageView) view.findViewById(R.id.imageView7);
		imageView[7] = (ImageView) view.findViewById(R.id.imageView8);
		imageView[8] = (ImageView) view.findViewById(R.id.imageView9);
		retweetLayout = view.findViewById(R.id.retweet_layout);
		retweetTextStatus = (TextView) view
				.findViewById(R.id.retweet_text_status);
		layoutRetweetThumbnailPic = view
				.findViewById(R.id.layout_retweet_thumbnail_pic);
		retweetThumbnailPic = (ImageView) view
				.findViewById(R.id.retweet_thumbnail_pic);
		retweetIcGif = (ImageView) view.findViewById(R.id.retweet_ic_gif);
		retweetLayoutMultiPic = view
				.findViewById(R.id.retweet_layout_multi_pic);
		retweetImageView[0] = (ImageView) view
				.findViewById(R.id.retweet_imageView1);
		retweetImageView[1] = (ImageView) view
				.findViewById(R.id.retweet_imageView2);
		retweetImageView[2] = (ImageView) view
				.findViewById(R.id.retweet_imageView3);
		retweetImageView[3] = (ImageView) view
				.findViewById(R.id.retweet_imageView4);
		retweetImageView[4] = (ImageView) view
				.findViewById(R.id.retweet_imageView5);
		retweetImageView[5] = (ImageView) view
				.findViewById(R.id.retweet_imageView6);
		retweetImageView[6] = (ImageView) view
				.findViewById(R.id.retweet_imageView7);
		retweetImageView[7] = (ImageView) view
				.findViewById(R.id.retweet_imageView8);
		retweetImageView[8] = (ImageView) view
				.findViewById(R.id.retweet_imageView9);
		retweetCount = (TextView) view.findViewById(R.id.retweet_count);
		textRet = (TextView) view.findViewById(R.id.text_ret);
		textCmt = (TextView) view.findViewById(R.id.text_cmt);
		btnSd = (ImageView) view.findViewById(R.id.btn_sd);
	}

	/**
	 * 
	 * @param status
	 */
	public void inflate(final Status status) {
		FinalBitmap fb = FinalBitmap.create(mContext);
		fb.configDisplayer(new ClipDisplayer(maxHeight));
		fb.display(userAvatar, status.user.avatar_large);
		String screenName = status.user.screen_name;
		if (!TextUtils.isEmpty(status.user.remark))
			screenName += "(" + status.user.remark + ")";
		userName.setText(screenName);
		textFrom.setText(Html.fromHtml(status.source + "·"
				+ TimeUtils.getTime(status.created_at)));
		int verified_type = status.user.verified_type;
		if (0 == verified_type) {
			icImage.setVisibility(View.VISIBLE);
			icImage.setImageResource(R.drawable.ic_verified);
		} else if (1 < verified_type && verified_type < 10) {
			icImage.setVisibility(View.VISIBLE);
			icImage.setImageResource(R.drawable.ic_verified_blue);
		} else {
			icImage.setVisibility(View.GONE);
		}

		textStatus.setText(StatusUtils.addEmotions(mContext,
				StatusUtils.matchNameTopic(status.text)));

		if (null != status.pic_urls && status.pic_urls.size() > 1) {
			layoutThumbnailPic.setVisibility(View.VISIBLE);
			thumbnailPic.setVisibility(View.GONE);
			icGif.setVisibility(View.GONE);
			layoutMultiPic.setVisibility(View.VISIBLE);
			for (int i = 0; i < 9; i++) {
				if (i < status.pic_urls.size()) {
					fb.display(imageView[i], status.pic_urls.get(i));
					imageView[i].setVisibility(View.VISIBLE);
				} else
					imageView[i].setVisibility(View.GONE);
			}
		} else if (null != status.thumbnail_pic) {
			layoutThumbnailPic.setVisibility(View.VISIBLE);
			thumbnailPic.setVisibility(View.VISIBLE);
			icGif.setVisibility(View.GONE);
			layoutMultiPic.setVisibility(View.GONE);
			if (null != status.bmiddle_pic)
				fb.display(thumbnailPic, status.bmiddle_pic, maxHeight);
			else
				fb.display(thumbnailPic, status.thumbnail_pic, maxHeight);
		} else {
			layoutThumbnailPic.setVisibility(View.GONE);
		}
		if (null == status.retweeted_status) {
			retweetLayout.setVisibility(View.GONE);
		} else if (null != status.retweeted_status.user) {
			retweetLayout.setVisibility(View.VISIBLE);
			retweetTextStatus.setText(StatusUtils.addEmotions(
					mContext,
					StatusUtils.matchNameTopic("@"
							+ status.retweeted_status.user.screen_name + ":"
							+ status.retweeted_status.text)));
			if (null != status.retweeted_status.pic_urls
					&& status.retweeted_status.pic_urls.size() > 1) {
				layoutRetweetThumbnailPic.setVisibility(View.VISIBLE);
				retweetThumbnailPic.setVisibility(View.GONE);
				retweetIcGif.setVisibility(View.GONE);
				retweetLayoutMultiPic.setVisibility(View.VISIBLE);
				for (int i = 0; i < 9; i++) {
					if (i < status.retweeted_status.pic_urls.size()) {
						fb.display(retweetImageView[i],
								status.retweeted_status.pic_urls.get(i));
						retweetImageView[i].setVisibility(View.VISIBLE);
					} else
						retweetImageView[i].setVisibility(View.GONE);
				}
			} else if (null != status.retweeted_status.thumbnail_pic) {
				layoutRetweetThumbnailPic.setVisibility(View.VISIBLE);
				retweetThumbnailPic.setVisibility(View.VISIBLE);
				retweetIcGif.setVisibility(View.GONE);
				retweetLayoutMultiPic.setVisibility(View.GONE);
				if (null != status.retweeted_status.bmiddle_pic)
					fb.display(retweetThumbnailPic,
							status.retweeted_status.bmiddle_pic, maxHeight);
				else
					fb.display(retweetThumbnailPic,
							status.retweeted_status.thumbnail_pic, maxHeight);
			} else {
				layoutRetweetThumbnailPic.setVisibility(View.GONE);
			}
			retweetCount.setText("转发 " + status.retweeted_status.reposts_count
					+ " 评论 " + status.retweeted_status.comments_count);
		}
		textRet.setText(String.valueOf(status.reposts_count));
		textCmt.setText(String.valueOf(status.comments_count));

		userAvatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("adapter", "click");
				Intent intent = new Intent(mContext, DisplayActivity.class);
				intent.putExtra("type", 3);
				intent.putExtra("uid", String.valueOf(status.user.id));
				mContext.startActivity(intent);
			}
		});

		layoutMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, CommentsActivity.class);
				intent.putExtra("id", status.id);
				mContext.startActivity(intent);
			}
		});
	}

}
