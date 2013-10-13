package com.nachuantech.opensync.view;

import opensync.cloudAccount.CloudAccount;
import opensync.cloudAccount.ProviderInfo;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nachuantech.sync.R;

public class AccountLoginItemView extends LinearLayout {

	private CloudAccount cloudAccount;

	private ImageView image;
	private TextView text;

	public AccountLoginItemView(Context context) {
		super(context);
	}

	public AccountLoginItemView(Context context, CloudAccount cloudAccount) {
		super(context);
		this.cloudAccount = cloudAccount;
		inflate(context, R.layout.account_login_item, this);
		init();
	}

	private void init() {
		findViews();
		setContent();
	}

	public void setAccount(CloudAccount cloudAccount) {
		this.cloudAccount = cloudAccount;
		setContent();
	}

	private void findViews() {
		image = (ImageView) findViewById(R.id.item_image);
		text = (TextView) findViewById(R.id.item_text);
	}

	private void setContent() {
		int imageResId;
		switch (cloudAccount.getProviderType()) {
		case ProviderInfo.DROPBOX:
			imageResId = R.drawable.icon_dropbox;
			break;
		case ProviderInfo.BAIDUCLOUD:
			imageResId = R.drawable.icon_baidu_cloud;
			break;
		case ProviderInfo.GOOGLEDRIVE:
			imageResId = R.drawable.icon_google_drive;
			break;
		case ProviderInfo.KINGSOFT:
			imageResId = R.drawable.icon_kingsorf_cloud;
			break;
		case ProviderInfo.SINACLOUD:
			imageResId = R.drawable.icon_sina_cloud;
			break;
		case ProviderInfo.SUGARSYNC:
			imageResId = R.drawable.icon_sugar_sync;
			break;
		default:
			imageResId = R.drawable.user_group_new;
			break;
		}
		image.setImageResource(imageResId);
		String userName = cloudAccount.getUserName();
		if (userName != null) {
			text.setText(userName);
		}
	}
}
