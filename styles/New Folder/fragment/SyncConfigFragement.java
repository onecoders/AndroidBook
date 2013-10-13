package com.nachuantech.opensync.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.nachuantech.sync.R;
import com.nachuantech.opensync.util.ViewHelper;
import com.nachuantech.opensync.view.AccountView;
import com.nachuantech.opensync.view.CloudFileView;
import com.nachuantech.opensync.view.CloudView;
import com.nachuantech.opensync.view.LocalFileView;
import com.nachuantech.opensync.view.RuleView;
import com.nachuantech.opensync.view.SyncTaskView;

public class SyncConfigFragement extends SherlockFragment {

	public static final String SYNC_CONFIG_ID = "sync_config_id";

	public static final int FRAGMENT_SYNC_TASK = 0;
	public static final int FRAGMENT_LOCAL_FOLDER = 1;
	public static final int FRAGMENT_ACCOUNT_MANAGER = 2;
	public static final int FRAGMENT_CLOUD = 3;
	public static final int FRAGMENT_CLOUD_FOLDER = 4;
	public static final int FRAGMENT_CONFIGURATION = 5;

	public static final String SYNC_TASK_TITLE = "sync_task_title";

	private int fragment_id;
	private Context mContext;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mContext = getSherlockActivity();
		fragment_id = getArguments().getInt(SYNC_CONFIG_ID);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		switch (fragment_id) {
		case FRAGMENT_SYNC_TASK:
			rootView = new SyncTaskView(mContext);
			break;
		case FRAGMENT_LOCAL_FOLDER:
			rootView = new LocalFileView(mContext);
			break;
		case FRAGMENT_ACCOUNT_MANAGER:
			rootView = new AccountView(mContext);
			break;
		case FRAGMENT_CLOUD:
			rootView = new CloudView(mContext);
			break;
		case FRAGMENT_CLOUD_FOLDER:
			rootView = new CloudFileView(mContext);
			break;
		case FRAGMENT_CONFIGURATION:
			rootView = new RuleView(mContext);
			break;
		default:
			break;
		}
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		fragment_id = getArguments().getInt(SYNC_CONFIG_ID);
		switch (fragment_id) {
		case FRAGMENT_SYNC_TASK:
			int titleId = getArguments().getInt(SYNC_TASK_TITLE);
			ViewHelper.setActionBarTitle(actionBar, titleId);
			inflater.inflate(R.menu.finish_menu, menu);
			break;
		case FRAGMENT_LOCAL_FOLDER:
			ViewHelper.setActionBarTitle(actionBar,
					R.string.select_local_folder);
			break;
		case FRAGMENT_ACCOUNT_MANAGER:
			ViewHelper.setActionBarTitle(actionBar, R.string.select_account);
			break;
		case FRAGMENT_CLOUD:
			// ViewHelper.setActionBarTitle(actionBar, R.string.select_sync_cloud);
			ViewHelper.setActionBarTitle(actionBar, R.string.loading);
			break;
		case FRAGMENT_CLOUD_FOLDER:
			ViewHelper.setActionBarTitle(actionBar,
					R.string.select_cloud_folder);
			break;
		case FRAGMENT_CONFIGURATION:
			ViewHelper.setActionBarTitle(actionBar, R.string.advance);
		default:
			break;
		}
	}

}
