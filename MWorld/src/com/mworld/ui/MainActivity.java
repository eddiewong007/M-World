package com.mworld.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mworld.adapter.TabsAdapter;
import com.mworld.fragment.AtFragment;
import com.mworld.fragment.CommentFragment;
import com.mworld.fragment.HomeFragment;

public class MainActivity extends ActionBarActivity{
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.vPager);
		this.setContentView(mViewPager);
		
		
		//ActionBar and Tabs
		ActionBar actionBar = this.getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		//设置ActionBar的背景
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_gradient_bg));
		actionBar.setDisplayUseLogoEnabled(true);
		
		mTabsAdapter = new TabsAdapter(this.getSupportFragmentManager(), 
				this, mViewPager);
		mTabsAdapter.addTab(actionBar.newTab().setText("Home"), 
				HomeFragment.class, null);
		mTabsAdapter.addTab(actionBar.newTab().setText("At"), 
				AtFragment.class, null);
		mTabsAdapter.addTab(actionBar.newTab().setText("Comment"),
				CommentFragment.class, null);
		
		if(savedInstanceState != null){
			actionBar.setSelectedNavigationItem(savedInstanceState.getInt("tab",0));
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		    case R.id.action_search:
		    	Toast.makeText(this, "I am search", 1000).show();
		    	break;
		    case R.id.action_add:
		    	Toast.makeText(this, "I am add", 1000).show();
		    	break;
		    case R.id.action_more:
		    	Toast.makeText(this, "I am more", 1000).show();
				startActivity(new Intent(MainActivity.this, DialogActivity.class));
				
		    	break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
}
