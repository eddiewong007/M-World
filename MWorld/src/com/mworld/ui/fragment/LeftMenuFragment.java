package com.mworld.ui.fragment;

import net.tsz.afinal.FinalBitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.ui.main.MainActivity;
import com.mworld.weibo.entities.User;

public class LeftMenuFragment extends Fragment {

	private Holder holder;

	public static LeftMenuFragment newInstance() {
		LeftMenuFragment fragment = new LeftMenuFragment();
		fragment.setArguments(new Bundle());
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final ScrollView view = (ScrollView) inflater.inflate(
				R.layout.slidingdrawer_contents, container, false);

		holder = new Holder();

		holder.avatar = (Spinner) view.findViewById(R.id.avatar);
		holder.nickname = (TextView) view.findViewById(R.id.nickname);

		holder.home = (LinearLayout) view.findViewById(R.id.btn_home);
		holder.mention = (LinearLayout) view.findViewById(R.id.btn_mention);
		holder.comment = (LinearLayout) view.findViewById(R.id.btn_comment);
		holder.search = (Button) view.findViewById(R.id.btn_search);
		holder.profile = (Button) view.findViewById(R.id.btn_profile);
		holder.setting = (Button) view.findViewById(R.id.btn_setting);
		holder.logout = (Button) view.findViewById(R.id.btn_logout);
		holder.fav = (Button) view.findViewById(R.id.btn_favourite);
		holder.homeCount = (TextView) view.findViewById(R.id.tv_home_count);
		holder.mentionCount = (TextView) view
				.findViewById(R.id.tv_mention_count);
		holder.commentCount = (TextView) view
				.findViewById(R.id.tv_comment_count);

		holder.search.setVisibility(View.GONE);

		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		User user = GlobalContext.getInstance().getAccount().getUserInfo();
		FinalBitmap.create(getActivity()).display(holder.avatar,
				user.getAvatarLarge());
		holder.nickname.setText(user.getScreenName());
		holder.home.setOnClickListener(onClickListener);
		holder.mention.setOnClickListener(onClickListener);
		holder.comment.setOnClickListener(onClickListener);
		holder.search.setOnClickListener(onClickListener);
		holder.profile.setOnClickListener(onClickListener);
		holder.setting.setOnClickListener(onClickListener);
		holder.logout.setOnClickListener(onClickListener);
		holder.fav.setOnClickListener(onClickListener);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.btn_home:
				((MainActivity) getActivity()).switchFragment(0);
				break;
			case R.id.btn_mention:
				((MainActivity) getActivity()).switchFragment(1);
				break;
			case R.id.btn_comment:
				((MainActivity) getActivity()).switchFragment(2);
				break;
			}

		}
	};

	private class Holder {

		Spinner avatar;

		TextView nickname;

		LinearLayout home;

		LinearLayout mention;

		LinearLayout comment;

		TextView homeCount;

		TextView mentionCount;

		TextView commentCount;

		Button search;

		Button logout;

		Button profile;

		Button setting;

		Button fav;
	}

}
