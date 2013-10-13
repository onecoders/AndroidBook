package com.nachuantech.opensync.activity;

import java.util.ArrayList;
import java.util.List;

import opensync.service.SyncService;
import opensync.syncPair.SyncPair;
import opensync.syncPair.SyncPairManager;
import opensync.syncer.SyncerRunState;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nachuantech.marketing.About;
import com.nachuantech.opensync.adapter.SyncPairItemAdapter;
import com.nachuantech.opensync.util.AboutUtils;
import com.nachuantech.opensync.util.ViewHelper;
import com.nachuantech.opensync.view.SyncPairItemView;
import com.nachuantech.sync.R;

public class SyncIndexActivity extends SherlockActivity implements
		OnClickListener, OnItemLongClickListener, OnTouchListener,
		AnimationListener {

	public static final String FRAGMENT_ID = "fragment_id";
	public static final String SYNCPAIR = "syncPair";
	public static final String REQUEST_CODE = "requestCode";
	public static final int REQUEST_CODE_ADD = 1;
	public static final int REQUEST_CODE_UPDATE = 2;
	public static final int REQUEST_CODE_THIRDAPP = 3;
	public static final int REQUEST_ACCOUNT_MANAGER = 4;

	private static final String FEEDBACKADDRESS = "feedback@nachuantech.com";
	private static final String SERVICE_ACTION = "opensync.SyncService";
	private static final String CROWDIN_PROJECT = "http://crowdin.net/project/opensync";

	private List<SyncPair> syncPairItems;
	private static ListView syncPairListView;
	private LinearLayout context_menu;
	private View leftBar, rightBar;
	private ImageButton menu_config, menu_delete;
	private SyncPairItemAdapter adapter;

	private int selectedRow;
	private String currentCreator;

	private SyncPairManager syncPairManager;
	private SyncService service;
	private About about;

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			service = ((SyncService.MyService) binder).getService();
			if (service == null) {
				startService(new Intent(SERVICE_ACTION));
				bindService();
			}
			setAdapter();
		}
	};

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			updateSyncPairItemView(intent);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync_index_main);
		checkEdition();
		if (editionInvalid()) {
			showNonLicensedProDialog();
		}
		init();
	}

	private void checkEdition() {
		about = AboutUtils.getAbout(getApplicationContext());
		about.checkEdition();
	}

	public boolean editionInvalid() {
		return about.getEdition() == About.Edition.FREE;
	}

	public void showNonLicensedProDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.licenseExpired);
		builder.setPositiveButton(R.string.extendLicense,
				new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						AboutUtils.startAboutActivity(SyncIndexActivity.this);
					}
				});
		builder.setNegativeButton(R.string.cancel, null);
		builder.create().show();
	}

	private void init() {
		syncPairManager = SyncPairManager.getInstance(getApplicationContext());
		initActionBar();
		findViews();
		initCreatorByIntent();
		setListener();
		bindService();
	}

	private void findViews() {
		syncPairListView = (ListView) findViewById(R.id.syncPairItems);
		context_menu = (LinearLayout) findViewById(R.id.context_menu);
		leftBar = findViewById(R.id.left);
		rightBar = findViewById(R.id.right);
		menu_config = (ImageButton) findViewById(R.id.menu_config);
		menu_delete = (ImageButton) findViewById(R.id.menu_delete);
	}

	private void initCreatorByIntent() {
		if (getIntent() != null && getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			currentCreator = bundle.getString(SyncConfigActivity.CREATOR);
			String localPath = bundle.getString(SyncConfigActivity.LOCAL_PATH);

			if (!getSyncedLocalPaths("ALL").contains(localPath)) {
				startSyncConfigActivity(REQUEST_CODE_THIRDAPP, getIntent()
						.getExtras());
			} else {
				if (!getSyncedLocalPaths(currentCreator).contains(localPath)) {
					showThirdAppCreatorDialog(localPath);
				}
			}
		} else {
			currentCreator = "ALL";
		}
	}

	private void showThirdAppCreatorDialog(final String localPath) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.localPathConflictTitle);
		builder.setMessage(R.string.localPathConflictMsg);
		builder.setPositiveButton(R.string.OK,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteSyncPairByLocalPath(localPath);
						startSyncConfigActivity(REQUEST_CODE_THIRDAPP,
								getIntent().getExtras());
					}
				});
		builder.setNegativeButton(R.string.cancel, null);
		builder.create().show();
	}

	private List<SyncPair> loadSyncPairsForPackageName(String appPackageName) {
		return syncPairManager.getPairForApp(appPackageName);
	}

	private List<String> getSyncedLocalPaths(String appPackageName) {
		List<String> syncedLocalPaths = new ArrayList<String>();
		List<SyncPair> syncPairItems = SyncPairManager.getInstance(
				getApplicationContext()).getPairForApp(appPackageName);
		if (syncPairItems != null && syncPairItems.size() != 0) {
			for (SyncPair syncPair : syncPairItems) {
				syncedLocalPaths.add(syncPair.getLocalPath());
			}
		}
		return syncedLocalPaths;
	}

	private void deleteSyncPairByLocalPath(String localPath) {
		for (SyncPair syncPair : loadSyncPairsForPackageName("ALL")) {
			if (syncPair.getLocalPath().equals(localPath)) {
				syncPairManager.removeSyncPairWithStatus(syncPair.getId());
			}
		}
	}

	private void setAdapter() {
		syncPairItems = loadSyncPairsForPackageName(currentCreator);
		syncPairListView.setEmptyView(findViewById(R.id.empty));
		adapter = new SyncPairItemAdapter(this, syncPairItems, service);
		syncPairListView.setAdapter(adapter);
	}

	private void setListener() {
		menu_config.setOnClickListener(this);
		menu_delete.setOnClickListener(this);
		syncPairListView.setOnItemLongClickListener(this);
		syncPairListView.setOnTouchListener(this);
		leftBar.setOnClickListener(this);
		rightBar.setOnClickListener(this);
	}

	private void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.index_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (context_menu.isShown()) {
			translate(false);
		}
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.addPair:
			if (currentCreator.equals("ALL")) {
				startSyncConfigActivity(REQUEST_CODE_ADD, null);
			} else {
				startSyncConfigActivity(REQUEST_CODE_THIRDAPP, getIntent()
						.getExtras());
			}
			break;
		case R.id.account_manager:
			startActivityForResult(new Intent(this,
					AccountManagerActivity.class), REQUEST_ACCOUNT_MANAGER);
			break;
		case R.id.feedback:
			sendFeedback();
			break;
		case R.id.about:
			AboutUtils.startAboutActivity(this);
			break;
		case R.id.translate:
			Intent i = new Intent(Intent.ACTION_VIEW,
					Uri.parse(CROWDIN_PROJECT));
			startActivity(i);
			break;
		default:
			break;
		}
		return true;
	}

	private void sendFeedback() {
		Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
				FEEDBACKADDRESS, null));
		try {
			startActivity(Intent.createChooser(i,
					getResources().getString(R.string.sendMail)));
		} catch (ActivityNotFoundException e) {
			showToastMessage(R.string.sendError);
		}
	}

	private void updateSyncPairItemView(Intent intent) {
		int syncPairId = intent.getIntExtra("SynPairId", 0);
		SyncPairItemView syncPairItemView = getSyncPairItemViewByTag(syncPairId);
		if (syncPairItemView == null) {
			return;
		}
		String action = intent.getStringExtra("Action");
		if (syncPairItemView.getRequest()) {
			if (action.equals("Success")) {
				syncPairItemView.startSync();
				syncPairItemView.setRequest(false);
			} else if (action.equals("Fail")) {
				syncPairItemView.failedSync();
				syncPairItemView.setRequest(false);
				if (editionInvalid()) {
					showToastMessage(R.string.expired);
				} else {
					showToastMessage(R.string.notConnect);
				}
			} else if (action.equals("Refuse")) {
				syncPairItemView.failedSync();
				syncPairItemView.setRequest(false);
			}
		} else {
			if (action.equals("Auto")) {
				SyncerRunState syncerRunState = intent
						.getParcelableExtra("SyncerRunState");
				syncPairItemView.updateProgress(syncerRunState);
				if (syncerRunState.isInitialized()) {
					if (!syncerRunState.isRunning()
							&& !syncerRunState.isFinished()) {
						syncPairItemView.pauseSync();
					} else if (syncerRunState.isRunning()) {
						syncPairItemView.updateSync();
					} else if (syncerRunState.isFinished()) {
						syncPairItemView.finishSync();
					}
				}
			}
		}
	}

	private static SyncPairItemView getSyncPairItemViewByTag(int syncPairId) {
		return (SyncPairItemView) syncPairListView.findViewWithTag(syncPairId);
	}

	private void register() {
		IntentFilter filter = new IntentFilter("opensync.action.SYNCER_BC");
		registerReceiver(receiver, filter);
	}

	private void unregister() {
		unregisterReceiver(receiver);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_config:
			translate(false);
			Bundle bundle = new Bundle();
			int syncpair_id = syncPairItems.get(selectedRow).getId();
			bundle.putInt(SyncConfigActivity.SYNCPAIR_ID, syncpair_id);
			startSyncConfigActivity(REQUEST_CODE_UPDATE, bundle);
			break;
		case R.id.menu_delete:
			translate(false);
			deleteSelectedItem();
			break;
		case R.id.left:
		case R.id.right:
			if (context_menu.isShown()) {
				translate(false);
			}
			break;
		default:
			break;
		}
	}

	private void startSyncConfigActivity(int request_code, Bundle bundle) {
		Intent intent = new Intent(this, SyncConfigActivity.class);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, request_code);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		intent.putExtra(REQUEST_CODE, requestCode);
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE_ADD:
			if (resultCode == RESULT_OK) {
				SyncPair syncPair = data.getExtras().getParcelable(SYNCPAIR);
				try {
					syncPairManager.add(syncPair);
				} catch (Exception e) {
					showToastMessage(R.string.syncpairConflict);
				}
			}
			break;
		case REQUEST_CODE_UPDATE:
			if (resultCode == RESULT_OK) {
				SyncPair syncPair = data.getExtras().getParcelable(SYNCPAIR);
				syncPairManager.updateSyncPair(syncPair);
			}
			break;
		case REQUEST_CODE_THIRDAPP:
			if (resultCode == RESULT_OK) {
				SyncPair syncPair = data.getExtras().getParcelable(SYNCPAIR);
				try {
					syncPairManager.add(syncPair);
				} catch (Exception e) {
					showToastMessage(R.string.syncpairConflict);
				}
				// send back to third party
				Intent intent = new Intent(currentCreator);
				setResult(RESULT_OK, intent);
			}
		case REQUEST_ACCOUNT_MANAGER:
			break;
		default:
			break;
		}
		refreshSyncPairItemViews(currentCreator);
	}

	private void showToastMessage(int resId) {
		ViewHelper.showToastMessage(this, resId);
	}

	private void refreshSyncPairItemViews(String packageName) {
		setAdapter();
	}

	private void bindService() {
		Intent serviceIntent = new Intent(SERVICE_ACTION);
		if (mConnection != null) {
			bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
		}
	}

	private void unBind() {
		if (mConnection != null) {
			unbindService(mConnection);
		}
	}

	private void deleteSelectedItem() {
		final Animation animation = AnimationUtils.loadAnimation(
				getBaseContext(), R.anim.splash_fade_out);
		if (animation != null) {
			animation.setAnimationListener(this);
			syncPairListView.getChildAt(
					selectedRow - syncPairListView.getFirstVisiblePosition())
					.startAnimation(animation);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		this.selectedRow = position;
		translate(true);
		updateSelectedStatus(true);
		return true;
	}

	@SuppressWarnings("deprecation")
	private void updateSelectedStatus(boolean isSelected) {
		Drawable originalBg = null;
		View selectedItem = syncPairListView.getChildAt(selectedRow
				- syncPairListView.getFirstVisiblePosition());
		if (isSelected) {
			if (selectedItem != null) {
				originalBg = selectedItem.getBackground();
				selectedItem.setBackgroundColor(getResources().getColor(
						R.color.selected_bg));
			}
		} else {
			if (selectedItem != null) {
				selectedItem.setBackgroundDrawable(originalBg);
			}
		}
	}

	private void translate(boolean isShow) {
		int fromY;
		int toY;
		if (isShow) {
			fromY = 100;
			toY = 0;
			context_menu.setVisibility(View.VISIBLE);
		} else {
			fromY = 0;
			toY = 100;
			context_menu.setVisibility(View.GONE);
			updateSelectedStatus(false);
		}
		TranslateAnimation slide = new TranslateAnimation(0, 0, fromY, toY);
		slide.setDuration(100);
		context_menu.startAnimation(slide);
	}

	@Override
	public void onBackPressed() {
		if (context_menu.isShown()) {
			translate(false);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (context_menu.isShown()
				&& event.getAction() == MotionEvent.ACTION_DOWN) {
			translate(false);
		}
		return false;
	}

	@Override
	public void onAnimationStart(Animation animation) {
		int syncPairIdToDel = syncPairItems.get(selectedRow).getId();
		syncPairManager.removeSyncPairWithStatus(syncPairIdToDel);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		adapter.remove(adapter.getItem(selectedRow));
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	protected void onResume() {
		register();
		super.onResume();
	}

	@Override
	protected void onPause() {
		unregister();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		unBind();
		about.onDestroy();
		super.onDestroy();
	}

}
