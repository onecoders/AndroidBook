package com.nachuantech.opensync.view;

import opensync.cloudAccount.ProviderInfo.ProviderType;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nachuantech.sync.R;

public class CloudItemView extends LinearLayout {

	private ProviderType type;

	private ImageView image;
	private TextView text;

	public CloudItemView(Context context) {
		super(context);
	}

	public CloudItemView(Context context, ProviderType type) {
		super(context);
		this.type = type;
		inflate(context, R.layout.account_login_item, this);
		init();
	}

	public void setCloud(ProviderType type) {
		this.type = type;
		setContent();
	}

	private void init() {
		findviews();
		setContent();
	}

	private void findviews() {
		image = (ImageView) findViewById(R.id.item_image);
		text = (TextView) findViewById(R.id.item_text);
	}

	private void setContent() {
		Drawable cloudIcon = null;
		CharSequence cloudName = null;
		switch (type) {
		case DROPBOX:
			cloudIcon = getCloudIcon(R.drawable.icon_dropbox);
			cloudName = getCloudName(R.string.dropbox);
			break;
		case GOOGLEDRIVE:
			cloudIcon = getCloudIcon(R.drawable.icon_google_drive);
			cloudName = getCloudName(R.string.google_drive);
			break;
		case SUGARSYNC:
			cloudIcon = getCloudIcon(R.drawable.icon_sugar_sync);
			cloudName = getCloudName(R.string.sugarSync);
			break;
		case BAIDUCLOUD:
			cloudIcon = getCloudIcon(R.drawable.icon_baidu_cloud);
			cloudName = getCloudName(R.string.baidu);
			break;
		case SINACLOUD:
			cloudIcon = getCloudIcon(R.drawable.icon_sina_cloud);
			cloudName = getCloudName(R.string.sina);
			break;
		default:
			break;
		}
		if (cloudIcon != null && cloudName != null) {
			image.setImageDrawable(cloudIcon);
			text.setText(cloudName);
		}
	}

	private Drawable getCloudIcon(int resid) {
		return getResources().getDrawable(resid);
	}

	private CharSequence getCloudName(int resid) {
		return getResources().getText(resid);
	}

}
