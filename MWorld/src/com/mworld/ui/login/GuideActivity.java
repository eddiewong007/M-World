package com.mworld.ui.login;

import java.util.ArrayList;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;

public class GuideActivity extends Activity implements OnClickListener {

	@ViewInject(id = R.id.viewpager)
	private ViewPager viewPager;

	@ViewInject(id = R.id.page0)
	private ImageView pointImage0;
	@ViewInject(id = R.id.page1)
	private ImageView pointImage1;
	@ViewInject(id = R.id.page2)
	private ImageView pointImage2;
	@ViewInject(id = R.id.page3)
	private ImageView pointImage3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.guideactivity_layout);
		FinalActivity.initInjectedView(this);

		initView();
	}

	@Override
	public void onBackPressed() {
		GlobalContext.getInstance().getActivity().finish();
		finish();
	}

	@SuppressLint("InflateParams")
	private void initView() {
		Drawable[] wallpaper = new Drawable[4];
		wallpaper[0] = getResources().getDrawable(R.drawable.wallpaper_morning);
		wallpaper[1] = getResources().getDrawable(R.drawable.wallpaper_noon);
		wallpaper[2] = getResources().getDrawable(R.drawable.wallpaper_evening);
		wallpaper[3] = getResources().getDrawable(R.drawable.wallpaper_night);

		ArrayList<View> views = new ArrayList<View>();
		LayoutInflater mLi = LayoutInflater.from(this);
		for (int i = 0; i < 4; i++) {
			View view = mLi.inflate(R.layout.guideactivity_viewpager_layout,
					null);
			view.setBackground(wallpaper[i]);
			Button button = (Button) view.findViewById(R.id.startBtn);
			if (i != 3)
				button.setVisibility(View.GONE);
			else
				button.setOnClickListener(this);
			views.add(view);
		}

		ViewPagerAdapter vpAdapter = new ViewPagerAdapter(views);
		viewPager.setAdapter(vpAdapter);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		private Drawable focusedImage = getResources().getDrawable(
				R.drawable.page_indicator_focused);

		private Drawable unfocusedImage = getResources().getDrawable(
				R.drawable.page_indicator_unfocused);

		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 0:
				pointImage0.setImageDrawable(focusedImage);
				pointImage1.setImageDrawable(unfocusedImage);
				break;
			case 1:
				pointImage1.setImageDrawable(focusedImage);
				pointImage0.setImageDrawable(unfocusedImage);
				pointImage2.setImageDrawable(unfocusedImage);
				break;
			case 2:
				pointImage2.setImageDrawable(focusedImage);
				pointImage1.setImageDrawable(unfocusedImage);
				pointImage3.setImageDrawable(unfocusedImage);
				break;
			case 3:
				pointImage3.setImageDrawable(focusedImage);
				pointImage2.setImageDrawable(unfocusedImage);
				break;
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

	}

	private class ViewPagerAdapter extends PagerAdapter {

		private ArrayList<View> views;

		public ViewPagerAdapter(ArrayList<View> views) {
			this.views = views;
		}

		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}
			return 0;
		}

		@Override
		public Object instantiateItem(View view, int position) {

			((ViewPager) view).addView(views.get(position), 0);

			return views.get(position);
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return (view == obj);
		}

		@Override
		public void destroyItem(View view, int position, Object arg2) {
			((ViewPager) view).removeView(views.get(position));
		}
	}

	@Override
	public void onClick(View v) {
		finish();
	}

}
