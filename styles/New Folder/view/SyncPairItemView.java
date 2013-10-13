package com.nachuantech.opensync.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import opensync.SyncPairRequest;
import opensync.service.SyncService;
import opensync.syncPair.CursorCloud;
import opensync.syncPair.CursorManager;
import opensync.syncPair.SyncPair;
import opensync.syncer.SyncerRunState;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nachuantech.sync.R;

public class SyncPairItemView extends LinearLayout implements OnClickListener {

	private Context mContext;

	private SyncPair syncPair;
	private SyncService service;

	// top left
	private ImageView successFlag;
	// top middle
	private TextView folderView;
	private TextView lastSyncDate;
	// top right
	private Button startBtn, syncing_pause;
	private ProgressBar syncingProgressBar;
	private ImageView syncing_end;
	// bottom
	private View top_dynamic, bottom_dynamic;
	private ProgressBar bottomProgressBar;

	private Animation zoom;

	private volatile boolean request;
	private boolean notFinished;

	private enum Status {
		START, PAUSE, RESUME, RUNNING, FINISH, FAILED
	}

	public SyncPairItemView(Context context) {
		super(context);
	}

	public SyncPairItemView(Context context, SyncPair syncPair,
			SyncService service) {
		super(context);
		this.mContext = context;
		this.syncPair = syncPair;
		this.service = service;
		inflate(getContext(), R.layout.sync_pair_item, this);
		findViews();
		setSyncPair(syncPair);
	}

	public void setSyncPair(SyncPair syncPair) {
		this.syncPair = syncPair;
		setTag(syncPair.getId());
		init();
	}

	private void init() {
		initContent();
		setListeners();
	}

	private void findViews() {
		successFlag = (ImageView) findViewById(R.id.success_flag);
		folderView = ((TextView) findViewById(R.id.sync_folder));
		lastSyncDate = (TextView) findViewById(R.id.sync_date);

		startBtn = (Button) findViewById(R.id.syncing_start);
		syncingProgressBar = (ProgressBar) findViewById(R.id.syncing);
		syncing_pause = (Button) findViewById(R.id.syncing_pause);
		syncing_end = (ImageView) findViewById(R.id.syncing_end);

		bottomProgressBar = (ProgressBar) findViewById(R.id.sync_bottom);

		top_dynamic = findViewById(R.id.divider_top_dynamic);
		bottom_dynamic = findViewById(R.id.divider_bottom_dynamic);
		zoom = AnimationUtils.loadAnimation(getContext(), R.anim.zoom);
	}

	private void initContent() {
		folderView.setText(syncPair.getLocalPath());
		String date = getFormatSyncDate();
		lastSyncDate.setText(date);
	}

	private void setListeners() {
		startBtn.setOnClickListener(this);
		syncingProgressBar.setOnClickListener(this);
		syncing_pause.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.syncing_start:
			request = true;
			List<SyncPairRequest> requestSyncPairs = new ArrayList<SyncPairRequest>();
			requestSyncPairs.add(new SyncPairRequest(syncPair,
					SyncPairRequest.HIGHLEVEL));
			service.request(requestSyncPairs, mContext);
			break;
		case R.id.syncing:
			pauseSync();
			service.pauseSyncer(syncPair);
			break;
		case R.id.syncing_pause:
			resumeSync();
			service.resumeSyncer(syncPair);
			break;
		default:
			break;
		}
	}

	public void startSync() {
		refreshViews(Status.RUNNING);
	}

	public void pauseSync() {
		refreshViews(Status.PAUSE);
	}

	public void resumeSync() {
		refreshViews(Status.RESUME);
	}

	public void stopSync() {
		//
	}

	public void finishSync() {
		bottomProgressBar.setProgress(0);
		bottomProgressBar.setVisibility(GONE);
		top_dynamic.setVisibility(VISIBLE);
		bottom_dynamic.setVisibility(VISIBLE);
		// show last sync time except first sync
		lastSyncDate.setText(getFormatSyncDate());
		if (notFinished) {
			notFinished = false;
			refreshViews(Status.FINISH);
			finishAnimation();
		}
	}

	public void updateSync() {
		notFinished = true;
		refreshViews(Status.RUNNING);
	}

	public void failedSync() {
		refreshViews(Status.START);
	}

	private void refreshViews(Status status) {
		startBtn.setVisibility(status == Status.START ? VISIBLE : GONE);
		syncing_pause.setVisibility(status == Status.PAUSE ? VISIBLE : GONE);
		syncingProgressBar.setVisibility(status == Status.RUNNING
				|| status == Status.RESUME ? VISIBLE : GONE);
		syncing_end.setVisibility(status == Status.FINISH ? VISIBLE : GONE);
		successFlag.setVisibility(status == Status.FINISH ? VISIBLE : GONE);
	}

	public void updateProgress(SyncerRunState syncerRunState) {
		int module = syncerRunState.getModule();
		int total = syncerRunState.getTotal();
		int done = syncerRunState.getDone();
		int progress = 0;
		if (module == 1) {
			progress = (int) (done * 100.0 / total);
		} else if (module == 2) {
			progress = (int) (done * 100.0 / total) + 100;
		}
		bottomProgressBar.setVisibility(VISIBLE);
		top_dynamic.setVisibility(GONE);
		bottom_dynamic.setVisibility(GONE);
		bottomProgressBar.setProgress(progress);
	}

	private String getFormatSyncDate() {
		if (getCursorCloud() != null) {
			lastSyncDate.setVisibility(View.VISIBLE);
			long date = getCursorCloud().getDate();
			String prefix = getResources().getString(R.string.lastSyncTime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",
					Locale.getDefault());
			return prefix + sdf.format(new Date(date));
		}
		return "";
	}

	private CursorCloud getCursorCloud() {
		return CursorManager.getInstance(mContext.getApplicationContext())
				.getCursorCloud(syncPair.getId());
	}

	private void finishAnimation() {
		successFlag.startAnimation(zoom);
		lastSyncDate.startAnimation(zoom);
		syncing_end.startAnimation(zoom);
		TranslateAnimation slide = new TranslateAnimation(0, 0, 10, 0);
		slide.setDuration(500);
		folderView.startAnimation(slide);
		postDelayed(new Runnable() {

			@Override
			public void run() {
				refreshViews(Status.START);
			}
		}, 1000);
	}

	public boolean getRequest() {
		return request;
	}

	public void setRequest(boolean request) {
		this.request = request;
	}

}
