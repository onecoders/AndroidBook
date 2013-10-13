package com.nachuantech.opensync.view;

import java.util.List;

import opensync.cloud.Cloud;
import opensync.cloud.CloudFactory;
import opensync.cloudAccount.CloudAccount;
import opensync.cloudAccount.CloudAccountManager;
import opensync.cloudAccount.ProviderInfo;
import opensync.syncPair.SyncPair;
import opensync.syncPair.SyncPairManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nachuantech.sync.R;

public class AccountManagerItemView extends RelativeLayout implements
		OnClickListener {

	private CloudAccount cloudAccount;

	private ImageView image;
	private TextView text;
	private Button logout;

	private ArrayAdapter<CloudAccount> adapter;

	public AccountManagerItemView(Context context) {
		super(context);
	}

	public AccountManagerItemView(Context context, CloudAccount cloudAccount,
			ArrayAdapter<CloudAccount> adapter) {
		super(context);
		this.adapter = adapter;
		inflate(context, R.layout.account_manager_item, this);
		findViews();
		setAccount(cloudAccount);
	}

	public void setAccount(CloudAccount cloudAccount) {
		this.cloudAccount = cloudAccount;
		init();
	}

	private void init() {
		setContent();
		setListener();
	}

	private void setListener() {
		logout.setOnClickListener(this);
	}

	private void findViews() {
		image = (ImageView) findViewById(R.id.item_image);
		text = (TextView) findViewById(R.id.item_text);
		logout = (Button) findViewById(R.id.logout);
	}

	private void setContent() {
		Drawable accountIcon;
		switch (cloudAccount.getProviderType()) {
		case ProviderInfo.DROPBOX:
			accountIcon = getResources().getDrawable(R.drawable.icon_dropbox);
			break;
		default:
			accountIcon = null;
			break;
		}
		if (accountIcon != null) {
			image.setImageDrawable(accountIcon);
		}
		String userName = cloudAccount.getUserName();
		if (userName != null) {
			text.setText(userName);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.logout) {
			createDeleteAccountDialog().show();
		}
	}

	private Dialog createDeleteAccountDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(R.string.deleteAccountTitle);
		builder.setMessage(R.string.deleteAccountMsg);
		builder.setPositiveButton(R.string.gotIt,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteSelectedItem();
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		return builder.create();
	}

	private void deleteSelectedItem() {
		// log out
		Cloud cloud = CloudFactory.getInstance().getCloudProvider(
				cloudAccount.getId());
		if (cloud != null) {
			cloud.logOut();
		}
		// delete corresponding sync pairs and their status and then cloud
		// account
		SyncPairManager syncPairManager = SyncPairManager
				.getInstance(getContext().getApplicationContext());
		List<SyncPair> syncPairs = syncPairManager
				.getAllSyncPairBycloudAccountId(cloudAccount.getId());
		if (syncPairs != null && syncPairs.size() != 0) {
			for (SyncPair syncPair : syncPairs) {
				syncPairManager.removeSyncPairWithStatus(syncPair.getId());
			}
		}
		// syncPairManager.removeSyncPairByCloudAccountId(cloudAccountToDel.getId());
		CloudAccountManager.getInstance(getContext().getApplicationContext())
				.remove(cloudAccount);
		// update listview
		adapter.remove(cloudAccount);
	}

}
