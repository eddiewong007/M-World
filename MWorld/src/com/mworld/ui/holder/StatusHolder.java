package com.mworld.ui.holder;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.support.utils.StatusBuilder;
import com.mworld.support.utils.TimeUtils;
import com.mworld.ui.adapter.ClipDisplayer;
import com.mworld.ui.main.DetailActivity;
import com.mworld.ui.main.PictureActivity;
import com.mworld.ui.main.ProfileActivity;
import com.mworld.ui.main.WriteActivity;
import com.mworld.weibo.api.FavoritesAPI;
import com.mworld.weibo.entities.Status;

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
	public View layoutRet;
	public TextView textRet;
	public View layoutCmt;
	public TextView textCmt;
	public ImageView btnFav;

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
		layoutRet = view.findViewById(R.id.layout_ret);
		textRet = (TextView) view.findViewById(R.id.text_ret);
		layoutCmt = view.findViewById(R.id.layout_cmt);
		textCmt = (TextView) view.findViewById(R.id.text_cmt);
		btnFav = (ImageView) view.findViewById(R.id.btn_favorate);
	}

	/**
	 * 
	 * @param status
	 */
	public void inflate(final Status status) {
		FinalBitmap fb = FinalBitmap.create(mContext);
		fb.configDisplayer(new ClipDisplayer(maxHeight));
		fb.display(userAvatar, status.user.getAvatarLarge());
		String screenName = status.user.getScreenName();
		if (!TextUtils.isEmpty(status.user.getRemark()))
			screenName += "(" + status.user.getRemark() + ")";
		userName.setText(screenName);
		textFrom.setText(Html.fromHtml(status.source + "·"
				+ TimeUtils.getTime(status.created_at)));
		int verified_type = status.user.getVerifiedType();
		if (0 == verified_type) {
			icImage.setVisibility(View.VISIBLE);
			icImage.setImageResource(R.drawable.ic_verified);
		} else if (1 < verified_type && verified_type < 10) {
			icImage.setVisibility(View.VISIBLE);
			icImage.setImageResource(R.drawable.ic_verified_blue);
		} else {
			icImage.setVisibility(View.GONE);
		}

		textStatus.setText(new StatusBuilder(mContext, status.text).matchName()
				.matchTopic().matchEmotions().build());

		if (null != status.pic_urls && status.pic_urls.size() > 1) {
			layoutThumbnailPic.setVisibility(View.VISIBLE);
			thumbnailPic.setVisibility(View.GONE);
			icGif.setVisibility(View.GONE);
			layoutMultiPic.setVisibility(View.VISIBLE);
			for (int i = 0; i < 9; i++) {
				if (i < status.pic_urls.size()) {
					fb.display(imageView[i], status.pic_urls.get(i));
					imageView[i].setVisibility(View.VISIBLE);
					imageView[i].setOnClickListener(new PictureLink(mContext,
							status.pic_urls.toArray(new String[0]), i));
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
			thumbnailPic.setOnClickListener(new PictureLink(mContext,
					status.pic_urls.toArray(new String[0]), -1));
		} else {
			layoutThumbnailPic.setVisibility(View.GONE);
		}
		if (null == status.retweeted_status) {
			retweetLayout.setVisibility(View.GONE);
		} else if (null != status.retweeted_status.user) {
			retweetLayout.setVisibility(View.VISIBLE);
			retweetTextStatus.setText(new StatusBuilder(mContext, "@"
					+ status.retweeted_status.user.getScreenName() + ":"
					+ status.retweeted_status.text).matchName().matchTopic()
					.matchEmotions().build());
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
						retweetImageView[i].setOnClickListener(new PictureLink(
								mContext, status.retweeted_status.pic_urls
										.toArray(new String[0]), i));
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
				retweetThumbnailPic.setOnClickListener(new PictureLink(
						mContext, status.retweeted_status.pic_urls
								.toArray(new String[0]), -1));
			} else {
				layoutRetweetThumbnailPic.setVisibility(View.GONE);
			}
			retweetCount.setText("转发 " + status.retweeted_status.reposts_count
					+ " 评论 " + status.retweeted_status.comments_count);
		}
		textRet.setText(String.valueOf(status.reposts_count));
		textCmt.setText(String.valueOf(status.comments_count));
		if (status.favorited)
			btnFav.setImageResource(R.drawable.favorate);
		else
			btnFav.setImageResource(R.drawable.un_favorate);

		userAvatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ProfileActivity.class);
				intent.putExtra("user", status.user);
				mContext.startActivity(intent);
			}
		});

		layoutMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, DetailActivity.class);
				intent.putExtra("status", status);
				mContext.startActivity(intent);
			}
		});

		layoutRet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(mContext, WriteActivity.class);
				intent.putExtra("title", "转发");
				intent.putExtra("status", status);
				mContext.startActivity(intent);
			}
		});

		layoutCmt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(mContext, WriteActivity.class);
				intent.putExtra("title", "评论");
				intent.putExtra("status", status);
				mContext.startActivity(intent);
			}
		});

		btnFav.setOnClickListener(new OnClickListener() {

			boolean isFaving = false;
			FavoritesAPI favAPI = new FavoritesAPI(GlobalContext.getInstance()
					.getAccount().getAccessToken());

			@Override
			public void onClick(View view) {
				if (isFaving)
					return;
				isFaving = true;
				if (status.favorited) {
					favAPI.destroy(status.id, new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							Toast.makeText(mContext, "取消收藏失败", Toast.LENGTH_SHORT)
									.show();
							isFaving = false;
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							btnFav.setImageResource(R.drawable.un_favorate);
							Toast.makeText(mContext, "取消收藏成功", Toast.LENGTH_SHORT)
									.show();
							status.favorited = true;
							isFaving = false;
						}

					});
				} else {
					favAPI.create(status.id, new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							Toast.makeText(mContext, "收藏失败",
									Toast.LENGTH_SHORT).show();
							isFaving = false;
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							btnFav.setImageResource(R.drawable.favorate);
							Toast.makeText(mContext, "收藏成功",
									Toast.LENGTH_SHORT).show();
							status.favorited = false;
							isFaving = false;
						}

					});
				}
			}
		});
	}

	private class PictureLink implements OnClickListener {

		private Context context;

		private String[] urls;

		private int pos;

		public PictureLink(Context context, String[] urls, int pos) {
			super();
			this.context = context;
			this.urls = urls;
			this.pos = pos;
		}

		@Override
		public void onClick(View view) {
			if (pos == -1)
				mContext.startActivity(PictureActivity.newInstance(
						(Activity) context, urls[0]));
			else
				mContext.startActivity(PictureActivity.newInstance(
						(Activity) context, urls, pos));
		}
	}
}