package com.mworld.ui.main;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.mworld.R;
import com.mworld.support.utils.EmotionUtils;

public class EmotionActivity extends SwipeBackActivity {

	@ViewInject(id = R.id.gridview)
	private GridView mGridView;

	public static Intent newIntent(Activity activity) {
		return new Intent(activity, EmotionActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview);
		FinalActivity.initInjectedView(this);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mGridView.setAdapter(adapter);

	}

	private BaseAdapter adapter = new BaseAdapter() {

		@Override
		public View getView(final int position, View convertView,
				ViewGroup container) {
			if (convertView == null) {
				convertView = new ImageView(EmotionActivity.this);
			}
			((ImageView) convertView).setImageBitmap(EmotionUtils.getEmotion(
					EmotionActivity.this, position));
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					Intent intent = new Intent();
					intent.putExtra("index", position);
					setResult(WriteActivity.EMOTION, intent);
				}
			});
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return EmotionUtils.getEmotion(EmotionActivity.this,
					EmotionUtils.text[position]);
		}

		@Override
		public int getCount() {
			return EmotionUtils.getEmotionCount();
		}
	};
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
