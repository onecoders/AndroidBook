package com.nachuantech.opensync.activity;

import java.util.ArrayList;
import java.util.List;

import opensync.CloudAuthenticator;
import opensync.CloudLoginManager;
import opensync.cloud.Cloud;
import opensync.cloudAccount.CloudAccount;
import opensync.cloudAccount.ProviderInfo.ProviderType;
import opensync.listener.AuthListener;
import opensync.syncPair.CursorManager;
import opensync.syncPair.SyncPair;
import opensync.syncPair.SyncPairManager;
import opensync.syncPair.SyncRule;
import opensync.syncPair.SyncRule.Direction;
import opensync.syncer.SyncStatusManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nachuantech.sync.R;
import com.nachuantech.opensync.fragment.SyncConfigFragement;
import com.nachuantech.opensync.util.ViewHelper;

public class SyncConfigActivity extends SherlockFragmentActivity {

	public static final String LOCAL_PATH = "localPath";
	public static final String CREATOR = "creator";
	public static final String DIRECTION = "direction";
	public static final String FILE_FILTER = "fileFilter";
	public static final String CONFLICT_REPLACE_PATTERN = "conflitReplacePattern";
	public static final String INTERVAL = "interval";

	public static final String SYNCPAIR_ID = "syncpair_id";
	public static final String DEFAULT_CREATOR = "com.nachuantech.opensync";

	private FragmentTransaction transaction;
	private SyncConfigFragement fragment;

	private CloudAuthenticator cloudAuthenticator;
	private SyncPair mSyncPair;
	private Cloud mCloud;

	private List<String> syncedLocalPaths;
	private List<String> syncedRemotePaths;
	private int requestCode;
	private boolean isConfigIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync_config_main);
		init();
	}

	private void init() {
		loadDataFromIntent();
		getSyncedPaths();
	}

	private void loadDataFromIntent() {
		Bundle bundle = getIntent().getExtras();
		requestCode = getIntent().getIntExtra(SyncIndexActivity.REQUEST_CODE,
				SyncIndexActivity.REQUEST_CODE_ADD);
		switch (requestCode) {
		case SyncIndexActivity.REQUEST_CODE_ADD:
			mSyncPair = new SyncPair();
			mSyncPair.setCreator(DEFAULT_CREATOR);
			SyncRule defaultSyncRule = new SyncRule();
			defaultSyncRule.setWifiOnly(true);
			mSyncPair.setSyncRule(defaultSyncRule);
			break;
		case SyncIndexActivity.REQUEST_CODE_THIRDAPP:
			mSyncPair = new SyncPair();
			mSyncPair.setCreator(bundle.getString(CREATOR));
			mSyncPair.setLocalPath(bundle.getString(LOCAL_PATH));
			SyncRule thirdAppSyncRule = new SyncRule();
			thirdAppSyncRule.setDirection(Direction.getDirection(bundle.getInt(
					DIRECTION, 1)));
			thirdAppSyncRule.setInterval(bundle.getInt(INTERVAL, 5));
			thirdAppSyncRule.setWifiOnly(true);
			String conflict = bundle.getString(CONFLICT_REPLACE_PATTERN);
			if (conflict != null) {
				thirdAppSyncRule.setConflictReplacePattern(conflict);
			}
			thirdAppSyncRule.setFileFilter(bundle.getString(FILE_FILTER));
			mSyncPair.setSyncRule(thirdAppSyncRule);
			break;
		case SyncIndexActivity.REQUEST_CODE_UPDATE:
			int syncpair_id = bundle.getInt(SYNCPAIR_ID);
			try {
				SyncPairManager mSyncPairManager = SyncPairManager
						.getInstance(getApplicationContext());
				mSyncPair = mSyncPairManager.getSyncPair(syncpair_id);
				cloudAuthenticator = new CloudAuthenticator(
						mSyncPair.getCloudAccount());
				mCloud = CloudLoginManager.getInstance().login(
						cloudAuthenticator, this);
			} catch (Exception e) {
				showToastMessage(R.string.updateError);
			}
			break;
		default:
			break;
		}
		loadFragmentById(SyncConfigFragement.FRAGMENT_SYNC_TASK);
	}

	public void loadFragmentById(int fragment_id) {
		fragment = new SyncConfigFragement();
		Bundle arguments = new Bundle();
		arguments.putInt(SyncConfigFragement.SYNC_CONFIG_ID, fragment_id);
		if (fragment_id != SyncConfigFragement.FRAGMENT_SYNC_TASK) {
			isConfigIndex = false;
		} else {
			isConfigIndex = true;
			switch (requestCode) {
			case SyncIndexActivity.REQUEST_CODE_ADD:
				arguments.putInt(SyncConfigFragement.SYNC_TASK_TITLE,
						R.string.sync_task_add);
				break;
			case SyncIndexActivity.REQUEST_CODE_THIRDAPP:
				arguments.putInt(SyncConfigFragement.SYNC_TASK_TITLE,
						R.string.sync_task_add);
				break;
			case SyncIndexActivity.REQUEST_CODE_UPDATE:
				arguments.putInt(SyncConfigFragement.SYNC_TASK_TITLE,
						R.string.sync_task_update);
				break;
			default:
				break;
			}
		}
		fragment.setArguments(arguments);
		transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.sync_pair_config_main, fragment);
		transaction.commit();
	}

	public void setLocalFolder(String localPath) {
		mSyncPair.setLocalPath(localPath);
		loadFragmentById(SyncConfigFragement.FRAGMENT_SYNC_TASK);
	}

	public void setAccount(CloudAccount cloudAccount) {
		if (cloudAccount != null) {
			/* exist account */
			loginExistAccount(cloudAccount);
		} else {
			/* create account */
			loadFragmentById(SyncConfigFragement.FRAGMENT_CLOUD);
		}
	}

	public void setCloud(final ProviderType type) {
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				loginNewAccount(type);
			}
		});
	}

	public void setCloudFolder(String remotePath) {
		if (remotePath == null) {
			loadFragmentById(SyncConfigFragement.FRAGMENT_SYNC_TASK);
			showToastMessage(R.string.loginFirst);
		} else {
			mSyncPair.setRemotePath(remotePath);
			loadFragmentById(SyncConfigFragement.FRAGMENT_SYNC_TASK);
		}
	}

	public void setSyncRule(SyncRule syncRule) {
		mSyncPair.setSyncRule(syncRule);
		loadFragmentById(SyncConfigFragement.FRAGMENT_SYNC_TASK);
	}

	private void loginExistAccount(CloudAccount cloudAccount) {
		cloudAuthenticator = new CloudAuthenticator(cloudAccount);
		try {
			mCloud = CloudLoginManager.getInstance().login(cloudAuthenticator,
					this);
			updateAccountAndRemotePath(cloudAccount);
			loadFragmentById(SyncConfigFragement.FRAGMENT_SYNC_TASK);
		} catch (Exception e) {
			loginNotInAction(R.string.loginFailed);
		}
	}

	private void loginNewAccount(ProviderType type) {
		if (type != null) {
			CloudAccount cloudAccount = new CloudAccount();
			cloudAccount.setProviderType(type.getMode());
			cloudAuthenticator = new CloudAuthenticator(cloudAccount,
					new AuthListener() {

						@Override
						public void onLoginOut(CloudAccount cloudAccount) {
							loginNotInAction(R.string.loginout);
						}

						@Override
						public void onLoginFailed(CloudAccount cloudAccount,
								String reason) {
							loginNotInAction(R.string.loginFailed);
						}

						@Override
						public void onLoginCanceled(CloudAccount cloudAccount) {
							loginNotInAction(R.string.loginCanceled);
						}

						@Override
						public void onLogin(CloudAccount cloudAccount) {
							updateAccountAndRemotePath(cloudAccount);
							loadFragmentById(SyncConfigFragement.FRAGMENT_SYNC_TASK);
						}
					});
			try {
				mCloud = CloudLoginManager.getInstance().login(
						cloudAuthenticator, this);
			} catch (Exception e) {
				loginNotInAction(R.string.loginFailed);
			}
		}
	}

	private void updateAccountAndRemotePath(CloudAccount cloudAccount) {
		if (mSyncPair.getCloudAccount() != null) {
			if (mSyncPair.getCloudAccount().getId() != cloudAccount.getId()) {
				mSyncPair.setCloudAccount(cloudAccount);
				mSyncPair.setRemotePath(null);
			}
		} else {
			mSyncPair.setCloudAccount(cloudAccount);
		}
	}

	private void loginNotInAction(int msgId) {
		showToastMessage(msgId);
		loadFragmentById(SyncConfigFragement.FRAGMENT_ACCOUNT_MANAGER);
	}

	@Override
	protected void onResume() {
		if (requestCode != SyncIndexActivity.REQUEST_CODE_UPDATE) {
			new Handler().post(new Runnable() {

				@Override
				public void run() {
					if (mCloud != null) {
						mCloud.activityOnResume(getApplicationContext());
					}
				}
			});
		}
		super.onResume();
	}

	private void setResultBack() {
		/* send back the new syncPair or updated syncPair */
		Intent intent = new Intent();
		intent.putExtra(SyncIndexActivity.SYNCPAIR, mSyncPair);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setActinBarCenterTitleCustomView();
		return super.onCreateOptionsMenu(menu);
	}

	private void setActinBarCenterTitleCustomView() {
		ActionBar actionBar = getSupportActionBar();
		ViewHelper.customActinBarCenterTitleView(this, actionBar);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.finish:
			finishSyncTask();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		if (isConfigIndex) {
			finish();
		} else {
			loadFragmentById(SyncConfigFragement.FRAGMENT_SYNC_TASK);
		}
	}

	public void finishSyncTask() {
		if (mSyncPair.getLocalPath() == null) {
			showToastMessage(R.string.check_local);
		} else if (mSyncPair.getRemotePath() == null) {
			showToastMessage(R.string.check_remote);
		} else {
			if (requestCode == SyncIndexActivity.REQUEST_CODE_UPDATE) {
				int syncpair_id = getIntent().getExtras().getInt(SYNCPAIR_ID);
				SyncPair original = SyncPairManager.getInstance(
						getApplicationContext()).getSyncPair(syncpair_id);
				if (compareSyncPair(mSyncPair, original)) {
					SyncStatusManager.getInstance(getApplicationContext())
							.removeBySyncPairId(mSyncPair.getId());
					CursorManager.getInstance(getApplicationContext()).remove(
							mSyncPair.getId());
				}
			}
			setResultBack();
		}
	}

	private boolean compareSyncPair(SyncPair update, SyncPair original) {
		return update.getCloudAccount().getId() == original.getCloudAccount()
				.getId()
				&& update.getLocalPath().equals(original.getLocalPath())
				&& update.getRemotePath().equals(original.getRemotePath())
				&& update.getSyncRule().getDirection()
						.equals(original.getSyncRule().getDirection());
	}

	public Cloud getCloud() {
		return mCloud;
	}

	public String getLocalPath() {
		String sdcard = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		return mSyncPair.getLocalPath() == null ? sdcard : mSyncPair
				.getLocalPath();
	}

	public String getRemotePath() {
		String remote = mCloud.getFileSeparator();
		return mSyncPair.getRemotePath() == null ? remote : mSyncPair
				.getRemotePath();
	}

	public SyncRule getSyncRule() {
		return mSyncPair.getSyncRule();
	}

	public SyncPair getSyncPair() {
		return mSyncPair;
	}

	private void getSyncedPaths() {
		syncedLocalPaths = new ArrayList<String>();
		syncedRemotePaths = new ArrayList<String>();
		List<SyncPair> syncPairItems = SyncPairManager.getInstance(
				getApplicationContext()).getPairForApp("ALL");
		if (syncPairItems != null && syncPairItems.size() != 0) {
			for (SyncPair syncPair : syncPairItems) {
				syncedLocalPaths.add(syncPair.getLocalPath());
				syncedRemotePaths.add(syncPair.getRemotePath());
			}
		}
	}

	private void showToastMessage(int resId) {
		ViewHelper.showToastMessage(this, resId);
	}

	public boolean isLocalPathSynced(String localPath) {
		return syncedLocalPaths.contains(localPath);
	}

	public boolean isRemotePathSynced(String remotePath) {
		return syncedRemotePaths.contains(remotePath);
	}
}
