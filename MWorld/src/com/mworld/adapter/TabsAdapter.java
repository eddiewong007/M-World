package com.mworld.adapter;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;

public class TabsAdapter extends FragmentPagerAdapter
        implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
	private final Context mContext;         
	private final ActionBar mActionBar;         
	private final ViewPager mViewPager;         
	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>(); 
	
	static final class TabInfo {             
		private final Class<?> clss;             
		private final Bundle args; 
		
		TabInfo(Class<?> _class, Bundle _args) {                 
			clss = _class;                 
			args = _args;             
		}         
	} 
	
	public TabsAdapter(FragmentManager fm, ActionBarActivity activity, ViewPager pager){
		super(fm);
		mContext = activity;
		mActionBar = activity.getActionBar();
		mViewPager = pager;
		mViewPager.setAdapter(this);
		mViewPager.setOnPageChangeListener(this);
	}
	
	public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args){
		TabInfo info = new TabInfo(clss, args);             
		tab.setTag(info);
		tab.setTabListener(this);             
		mTabs.add(info);             
		mActionBar.addTab(tab);             
		notifyDataSetChanged(); 
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		TabInfo info = mTabs.get(arg0);
		return Fragment.instantiate(mContext, info.clss.getName(), info.args);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mTabs.size();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		mActionBar.setSelectedNavigationItem(arg0);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		Object tag = tab.getTag();
		int tabSize = mTabs.size();
		for(int i=0; i<tabSize; i++){
			if(mTabs.get(i) == tag){
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

}
