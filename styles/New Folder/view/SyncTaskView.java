package com.nachuantech.opensync.view;

import opensync.cloudAccount.CloudAccount;
import opensync.cloudAccount.ProviderInfo;
import opensync.syncPair.SyncPair;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nachuantech.sync.R;
import com.nachuantech.opensync.activity.SyncConfigActivity;
import com.nachuantech.opensync.fragment.SyncConfigFragement;
import com.nachuantech.opensync.util.ViewHelper;

public class SyncTaskView extends LinearLayout implements OnClickListener {

	private Context mContext;
	private SyncConfigActivity activity;

	private View account, cloudFolder, localFolder, advance;
	private TextView accountText, cloudFolerText, localFolderText;
	private ImageView account_left;

	public SyncTaskView(Context context) {
		super(context);
		this.mContext = context;
		this.activity = (SyncConfigActivity) context;
		inflate(mContext, R.layout.fragment_sync_task, this);
		init();
	}

	private void init() {
		findviews();
		setListener();
		setContent();
	}

	private void findviews() {
		account = findViewById(R.id.account);
		cloudFolder = findViewById(R.id.cloudFolder);
		localFolder = findViewById(R.id.localFolder);
		advance = findViewById(R.id.advance);
		accountText = (TextView) findViewById(R.id.account_text);
		cloudFolerText = (TextView) findViewById(R.id.cloudFolder_text);
		localFolderText = (TextView) findViewById(R.id.localFolder_text);
		account_left = (ImageView) findViewById(R.id.account_left);
	}

	private void setListener() {
		account.setOnClickListener(this);
		cloudFolder.setOnClickListener(this);
		localFolder.setOnClickListener(this);
		advance.setOnClickListener(this);
	}

	private void setContent() {
		SyncPair syncPair = activity.getSyncPair();
		CloudAccount cloudAccount = syncPair.getCloudAccount();
		String localPath = syncPair.getLocalPath();
		String remotePath = syncPair.getRemotePath();
		if (cloudAccount != null) {
			String userName = cloudAccount.getUserName();
			accountText.setText(userName);
			setSelectedFlag(accountText);
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
			account_left.setImageResource(imageResId);
		}
		if (localPath != null) {
			localFolderText.setText(localPath);
			setSelectedFlag(localFolderText);
		}
		if (remotePath != null) {
			String separator = activity.getCloud().getFileSeparator();
			if (remotePath.equals(separator)) {
				remotePath = getResources().getString(R.string.wholeCloud);
			}
			cloudFolerText.setText(remotePath);
			setSelectedFlag(cloudFolerText);
		}
	}

	private void setSelectedFlag(TextView view) {
		view.setCompoundDrawablesWithIntrinsicBounds(0, 0,
				R.drawable.success_flag, 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.account:
			activity.loadFragmentById(SyncConfigFragement.FRAGMENT_ACCOUNT_MANAGER);
			break;
		case R.id.cloudFolder:
			if (activity.getCloud() != null) {
				activity.loadFragmentById(SyncConfigFragement.FRAGMENT_CLOUD_FOLDER);
			} else {
				ViewHelper.showToastMessage(mContext, R.string.loginFirst);
			}
			break;
		case R.id.localFolder:
			activity.loadFragmentById(SyncConfigFragement.FRAGMENT_LOCAL_FOLDER);
			break;
		case R.id.advance:
			activity.loadFragmentById(SyncConfigFragement.FRAGMENT_CONFIGURATION);
		default:
			break;
		}
	}

}
