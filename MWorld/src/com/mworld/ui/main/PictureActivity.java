package com.mworld.ui.main;

import java.util.Locale;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.mworld.support.utils.Utility;

public class PictureActivity extends Activity {

	private Handler mHandler;

	public static Intent newInstance(Activity activity, String url) {
		Intent intent = new Intent(activity, PictureActivity.class);
		intent.putExtra("url", url);
		return intent;
	}

	public static Intent newInstance(Activity activity, String[] urls, int pos) {
		Intent intent = new Intent(activity, PictureActivity.class);
		intent.putExtra("urls", urls);
		intent.putExtra("pos", pos);
		return intent;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		getWindow().setBackgroundDrawableResource(android.R.color.black);
		ColorDrawable color = new ColorDrawable(Color.BLACK);
		color.setAlpha(128);
		getActionBar().setBackgroundDrawable(color);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mHandler = new Handler();

		String url = getIntent().getStringExtra("url");
		if (url != null) {
			buildSinglePicture(url);
			return;
		}
		String[] urls = getIntent().getStringArrayExtra("urls");
		int pos = getIntent().getIntExtra("pos", 0);
		buildMultiPicture(urls, pos);
	}

	private void buildSinglePicture(final String url) {
		setTitle("浏览图片");
		final String newUrl = url.replaceFirst("thumbnail", "large");
		final ImageView imageView = new ImageView(this);
		imageView.setMaxWidth(Utility.getScreenWidth());
		imageView.setScaleType(ScaleType.FIT_CENTER);
		imageView.setVerticalScrollBarEnabled(true);
		if (url.toLowerCase(Locale.CHINA).endsWith(".gif")) {
			FinalBitmap.create(this).display(imageView, newUrl);
		} else {
			FinalBitmap.create(this).display(imageView, newUrl);
		}
		imageView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getActionBar().show();
				hideActionBarDelayed(mHandler);
			}
		});
		imageView.setOnTouchListener(new View.OnTouchListener() {

			float curX, curY;

			@Override
			public boolean onTouch(View view, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					curX = event.getX();
					curY = event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					imageView.scrollBy((int) (curX - event.getX()),
							(int) (curY - event.getY()));
					curX = event.getX();
					curY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					imageView.scrollBy((int) (curX - event.getX()),
							(int) (curY - event.getY()));
					curX = event.getX();
					curY = event.getY();
					break;
				}

				return view.performClick();
			}

		});
		setContentView(imageView);
	}

	private void buildMultiPicture(final String[] urls, int pos) {
		setTitle("浏览图片 " + (pos + 1) + "/" + urls.length);
		ViewPager viewPager = new ViewPager(this);
		ViewPagerAdapter adapter = new ViewPagerAdapter(this, urls);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(pos);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				setTitle("浏览图片 " + (position + 1) + "/" + urls.length);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		setContentView(viewPager);
	}

	@Override
	public void onResume() {
		super.onResume();
		getActionBar().show();
		hideActionBarDelayed(mHandler);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void hideActionBarDelayed(Handler handler) {
		handler.postDelayed(new Runnable() {
			public void run() {
				getActionBar().hide();
			}
		}, 2000);
	}

	private class ViewPagerAdapter extends PagerAdapter {

		Activity activity;
		String imageArray[];

		public ViewPagerAdapter(Activity act, String[] imgArra) {
			imageArray = imgArra;
			activity = act;
		}

		public int getCount() {
			return imageArray.length;
		}

		public Object instantiateItem(View collection, final int position) {
			final String newUrl = imageArray[position].replaceFirst(
					"thumbnail", "bmiddle");
			final ImageView imageView = new ImageView(activity);
			imageView.setMaxWidth(Utility.getScreenWidth());
			imageView.setScaleType(ScaleType.FIT_CENTER);
			imageView.setVerticalScrollBarEnabled(true);
			if (imageArray[position].toLowerCase(Locale.CHINA).endsWith(".gif")) {
				FinalBitmap.create(activity).display(imageView, newUrl);
			} else {
				FinalBitmap.create(activity).display(imageView, newUrl);
			}
			imageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					getActionBar().show();
					hideActionBarDelayed(mHandler);
				}
			});
			imageView.setOnTouchListener(new View.OnTouchListener() {

				float curX, curY;

				@Override
				public boolean onTouch(View view, MotionEvent event) {

					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						curX = event.getX();
						curY = event.getY();
						break;
					case MotionEvent.ACTION_MOVE:
						imageView.scrollBy((int) (curX - event.getX()),
								(int) (curY - event.getY()));
						curX = event.getX();
						curY = event.getY();
						break;
					case MotionEvent.ACTION_UP:
						imageView.scrollBy((int) (curX - event.getX()),
								(int) (curY - event.getY()));
						curX = event.getX();
						curY = event.getY();
						break;
					}

					return view.performClick();
				}

			});
			((ViewPager) collection).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
}