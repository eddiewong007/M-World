package com.mworld.ui;

import java.util.ArrayList;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.mworld.adapter.ViewPagerAdapter;
import com.mworld.utils.PreUtils;

public class GuideViewActivity extends Activity {

	// 定义ViewPager对象
	@ViewInject(id = R.id.viewpager)
	private ViewPager viewPager;

	// 定义底部小点图片
	@ViewInject(id = R.id.page0)
	private ImageView pointImage0;
	@ViewInject(id = R.id.page1)
	private ImageView pointImage1;
	@ViewInject(id = R.id.page2)
	private ImageView pointImage2;
	@ViewInject(id = R.id.page3)
	private ImageView pointImage3;

	// 定义开始按钮对象
	private Button startBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guideview);
		FinalActivity.initInjectedView(this);

		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (PreUtils.isGuided(this)) {
			start();
		} else {
			PreUtils.setGuided(this);
		}
	}

	/**
	 * 初始化组件
	 */
	private void initView() {

		// 实例化ArrayList对象
		ArrayList<View> views = new ArrayList<View>();

		// 实例化各个界面的布局对象
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.guide_view01, null);
		View view2 = mLi.inflate(R.layout.guide_view02, null);
		View view3 = mLi.inflate(R.layout.guide_view03, null);
		View view4 = mLi.inflate(R.layout.guide_view04, null);

		// 将要分页显示的View装入数组中
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);

		// 实例化ViewPager适配器
		ViewPagerAdapter vpAdapter = new ViewPagerAdapter(views);

		// 设置适配器数据
		viewPager.setAdapter(vpAdapter);

		// 设置viewpager监听
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		// 实例化button并设置监听
		startBtn = ((Button) view4.findViewById(R.id.startBtn));
		startBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				start();
			}
		});
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

	/**
	 * 相应按钮点击事件
	 */
	public void start() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

}
