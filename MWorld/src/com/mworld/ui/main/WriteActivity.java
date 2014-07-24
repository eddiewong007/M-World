package com.mworld.ui.main;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.weibo.api.CommentAPI;
import com.mworld.weibo.api.StatusAPI;
import com.mworld.weibo.entities.Comment;
import com.mworld.weibo.entities.Status;
import com.mworld.weibo.entities.User;

public class WriteActivity extends SwipeBackActivity {

	private static final String TAG = "Scroll";

	private String mPicPath = null;

	private int writeType = 0;

	private StatusAPI mStatusAPI;

	private CommentAPI mCommentAPI;

	private User mUser;

	private Status mStatus;

	private Comment mComment;

	@ViewInject(id = R.id.write_user_avatar)
	private ImageView userAvatar;
	@ViewInject(id = R.id.write_user_name)
	private TextView userName;

	@ViewInject(id = R.id.write_count)
	private TextView mTextCount;
	@ViewInject(id = R.id.edit)
	private EditText mEdit;
	@ViewInject(id = R.id.write_send_btn, click = "send")
	private Button mSendBtn;
	@ViewInject(id = R.id.write_hash_btn, click = "addHash")
	private Button mHashButton;
	@ViewInject(id = R.id.write_picture_btn, click = "selectPic")
	private ImageButton mPicButton;
	@ViewInject(id = R.id.scroll, click = "scroll")
	private ScrollView mScrollView;
	@ViewInject(id = R.id.status_pic, click = "deletePic")
	private ImageView mStatusPic;

	private Handler mHandler = new Handler();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.writeactivity_layout);
		FinalActivity.initInjectedView(this);

		mUser = GlobalContext.getInstance().getAccount().getUserInfo();
		FinalBitmap.create(this).display(userAvatar, mUser.getAvatarLarge());

		String title = getIntent().getStringExtra("title");
		mStatus = (Status) getIntent().getParcelableExtra("status");
		mComment = (Comment) getIntent().getParcelableExtra("comment");
		if (mStatus != null) {
			mEdit.setHint("@" + mStatus.user.getScreenName() + ":"
					+ mStatus.text);
			mPicButton.setVisibility(View.GONE);
		}
		if (mComment != null) {
			mEdit.setHint("@" + mComment.user.getScreenName() + ":"
					+ mComment.text);
			mPicButton.setVisibility(View.GONE);
		}
		if (title == null || TextUtils.isEmpty(title)) {
			buildWeibo();
		} else if (title.equals("评论")) {
			writeType = 1;
			userName.setText(title);
		} else if (title.equals("转发")) {
			writeType = 2;
			userName.setText(title);
		} else if (title.equals("转发")) {
			writeType = 3;
			userName.setText(title);
		}
		String token = GlobalContext.getInstance().getAccount()
				.getAccessToken();
		mStatusAPI = new StatusAPI(token);
		mCommentAPI = new CommentAPI(token);
		mEdit.addTextChangedListener(new TextNumLimitWatcher());

	}

	private void buildWeibo() {
		String name = mUser.getScreenName();
		if (name.length() > 18)
			name = name.substring(0, 15) + "...";
		userName.setText(name);
	}

	public void scroll(View view) {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mScrollView.fullScroll(View.FOCUS_DOWN);
			}
		}, 100);

	}

	public void addHash(View view) {
		Editable edit = mEdit.getEditableText();
		int start = mEdit.getSelectionStart();
		int end = mEdit.getSelectionEnd();
		if (end - start != 0)
			edit.delete(start, end);
		edit.insert(start, "##");
		mEdit.setText(edit);
		mEdit.setSelection(start + 1);
	}

	public void selectPic(View view) {
		if (mPicPath == null) {
			ArrayList<String> itemValueList = new ArrayList<String>();
			itemValueList.add("图库");
			itemValueList.add("拍照");

			new AlertDialog.Builder(this).setItems(
					itemValueList.toArray(new String[0]),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (which == 0) {
								Intent intent = new Intent(
										Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
								startActivityForResult(intent, 0);
							} else {
								Intent intent = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								startActivityForResult(intent, 1);
							}
						}
					}).show();

		}
	}

	public void deletePic(View view) {
		mPicPath = null;
		mStatusPic.setVisibility(View.GONE);
	}

	public void send(View view) {
		String text = mEdit.getText().toString();
		int length = length(text);
		if (length > 140 || length < 1) {
			Toast.makeText(WriteActivity.this, "字数不符合要求", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (writeType == 1) {
			mCommentAPI.create(text, mStatus.id, false,
					new AjaxCallBack<String>() {

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							Toast.makeText(WriteActivity.this, "评论成功",
									Toast.LENGTH_LONG).show();
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							Toast.makeText(WriteActivity.this, "评论失败",
									Toast.LENGTH_LONG).show();
						}

					});
		} else if (writeType == 2) {
			mStatusAPI.repost(mStatus.id, text, 0, getLocalIpAddress(),
					new AjaxCallBack<String>() {

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							Toast.makeText(WriteActivity.this, "评论成功",
									Toast.LENGTH_LONG).show();
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							Toast.makeText(WriteActivity.this, "评论失败",
									Toast.LENGTH_LONG).show();
						}

					});
		} else if (writeType == 3) {
			mCommentAPI.reply(mComment.id, mComment.status.id, text, true,
					false, new AjaxCallBack<String>() {
						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							Toast.makeText(WriteActivity.this, "回复成功",
									Toast.LENGTH_LONG).show();
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							Toast.makeText(WriteActivity.this, "回复失败",
									Toast.LENGTH_LONG).show();
						}

					});
		} else if (mPicPath == null) {

			mStatusAPI.update(text, 0, "", 0.0F, 0.0F, "[]",
					getLocalIpAddress(), new AjaxCallBack<String>() {

						@Override
						public void onStart() {
							super.onStart();
							Toast.makeText(WriteActivity.this, "开始发送文字",
									Toast.LENGTH_LONG).show();
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							Toast.makeText(WriteActivity.this, "发送文字成功",
									Toast.LENGTH_LONG).show();
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							Toast.makeText(WriteActivity.this, "发送文字失败",
									Toast.LENGTH_LONG).show();
							Log.i(TAG, t.getMessage() + " ; " + errorNo + " ; "
									+ strMsg);
						}

					});
		} else {
			mStatusAPI.upload(mEdit.getText().toString(), 0, "", mPicPath,
					0.0F, 0.0F, "[]", getLocalIpAddress(),
					new AjaxCallBack<String>() {

						@Override
						public void onStart() {
							super.onStart();
							Toast.makeText(WriteActivity.this, "开始发送图片",
									Toast.LENGTH_LONG).show();
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							Toast.makeText(WriteActivity.this, "发送图片成功",
									Toast.LENGTH_LONG).show();
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							Toast.makeText(WriteActivity.this, "发送图片失败",
									Toast.LENGTH_LONG).show();
							Log.i(TAG, t.getMessage() + strMsg);
						}

					});

		}
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK || null == data) {
			Toast.makeText(WriteActivity.this, "添加图片失败", Toast.LENGTH_LONG)
					.show();
			return;
		}
		Bitmap bitmap = null;
		Uri thisUri = data.getData();
		if (requestCode == 1) {
			Bundle bundle = data.getExtras();
			bitmap = (Bitmap) bundle.get("data");
		}
		if (thisUri == null) {
			thisUri = Uri.parse(MediaStore.Images.Media.insertImage(
					getContentResolver(), bitmap, null, null));
		}
		mPicPath = getRealPathFromURI(this, thisUri);
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeFile(mPicPath);
		}

		mStatusPic.setImageBitmap(bitmap);
		mStatusPic.setVisibility(View.VISIBLE);
		if (TextUtils.isEmpty(mEdit.getText().toString())) {
			mEdit.setText("分享图片");
		}

	}

	public class TextNumLimitWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			int sum = length(mEdit.getText().toString());

			int left = 140 - sum;

			mTextCount.setText(left + " X");

			mTextCount.setTextColor(left < 0 ? Color.WHITE : Color.WHITE);

		}

		@Override
		public void afterTextChanged(Editable s) {
		}

	}

	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return "";
	}

	public int length(String paramString) {
		int i = 0;
		for (int j = 0; j < paramString.length(); j++) {
			if (paramString.substring(j, j + 1).matches("[Α-￥]")) {
				i += 2;
			} else {
				i++;
			}
		}
		if (i % 2 > 0) {
			i = 1 + i / 2;
		} else {
			i = i / 2;
		}
		return i;
	}

	@SuppressWarnings("deprecation")
	public String getRealPathFromURI(Context context, Uri uri) {

		// 指定获取的列
		String columns[] = new String[] { Media.DATA, Media._ID, Media.TITLE,
				Media.DISPLAY_NAME };

		Cursor cursor = this.managedQuery(uri, columns, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);

	}

}
