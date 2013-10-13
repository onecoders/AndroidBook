package com.nachuantech.opensync.view;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import opensync.SyncPairFilter;
import opensync.cloud.Cloud;
import opensync.cloud.CloudFile;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.nachuantech.sync.R;
import com.nachuantech.opensync.activity.SyncConfigActivity;
import com.nachuantech.opensync.adapter.CloudFileAdapter;
import com.nachuantech.opensync.util.ViewHelper;

public class CloudFileView extends RelativeLayout implements OnClickListener,
		OnItemClickListener {

	private String currentParentPath;
	private List<CloudFile> currentFiles;
	private Cloud mCloud;

	private Context mContext;
	private SyncConfigActivity activity;

	private ListView listView;
	private LinearLayout location;
	private HorizontalScrollView s;
	private Button select, cancel, newFolder;
	private View headerView;

	private String separator;

	public CloudFileView(Context context) {
		super(context);
		this.mContext = context;
		this.activity = (SyncConfigActivity) context;
		init();
	}

	private void init() {
		inflate(mContext, R.layout.fragment_file_list, this);
		findViews();
		initFileListView();
		setListener();
	}

	private void findViews() {
		listView = (ListView) findViewById(R.id.file_list);
		location = (LinearLayout) findViewById(R.id.location);
		s = (HorizontalScrollView) findViewById(R.id.locationScroll);
		select = (Button) findViewById(R.id.select);
		cancel = (Button) findViewById(R.id.cancel);
		newFolder = (Button) findViewById(R.id.newFolder);
		headerView = LayoutInflater.from(mContext).inflate(
				R.layout.file_listview_header, null);
	}

	private void initFileListView() {
		listView.addHeaderView(headerView);
		mCloud = activity.getCloud();
		separator = mCloud.getFileSeparator();
		currentParentPath = activity.getRemotePath();
		new CloudFileAsyncTask().execute(currentParentPath);
	}

	private void setListener() {
		select.setOnClickListener(this);
		cancel.setOnClickListener(this);
		newFolder.setOnClickListener(this);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.select) {
			activity.setCloudFolder(currentParentPath);
		} else if (v.getId() == R.id.cancel) {
			activity.onBackPressed();
		} else if (v.getId() == R.id.newFolder) {
			createFolderAlertDialog();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {
			upLevle();
		} else {
			intoFolder(position - 1);
		}
	}

	private void upLevle() {
		int lastSeparatorPos = currentParentPath.lastIndexOf(separator);
		if (lastSeparatorPos != 0) {
			currentParentPath = currentParentPath
					.substring(0, lastSeparatorPos);
		} else {
			currentParentPath = separator;
		}
		new CloudFileAsyncTask().execute(currentParentPath);
	}

	private void intoFolder(int position) {
		if (!currentFiles.get(position).isDir()) {
			return;
		}
		currentParentPath = currentFiles.get(position).getPath();
		new CloudFileAsyncTask().execute(currentParentPath);
	}

	private void inflateListView(List<CloudFile> files) {
		ArrayAdapter<CloudFile> adapter = null;
		if (currentFiles != null && currentFiles.size() != 0) {
			Collections.sort(files, new SortFileName());
			Collections.sort(files, new SortFolder());
			adapter = new CloudFileAdapter(mContext, files, separator);
		}
		listView.setAdapter(adapter);
		String currentLocation = getResources().getString(R.string.cloud)
				+ currentParentPath;
		updateLocation(currentLocation);
		updateHeaderView();
	}

	private void updateHeaderView() {
		boolean alreadyTopLevel = currentParentPath.equals(separator);
		int visibility = alreadyTopLevel ? GONE : VISIBLE;
		headerView.findViewById(R.id.upLevel).setVisibility(visibility);
		headerView.findViewById(R.id.divider_top).setVisibility(visibility);
		// update select button
		boolean selectEnabled = !SyncPairFilter.isFilePathConflict(
				currentParentPath, mContext, true, activity.getSyncPair()
						.getId());
		select.setEnabled(selectEnabled);
	}

	private void updateLocation(String currentLocation) {
		location.removeAllViews();
		String[] parents = currentLocation.split(separator);
		int arrayLength = parents.length;
		// update header view
		if (!currentParentPath.equals(separator)) {
			((TextView) headerView.findViewById(R.id.upLevel))
					.setText(getResources().getString(R.string.go_up) + " "
							+ parents[arrayLength - 2]);
		}
		for (int i = 0; i < parents.length; i++) {
			TextView textview = (TextView) LayoutInflater.from(mContext)
					.inflate(R.layout.location_parent, null);
			textview.setText(parents[i]);
			if (i == parents.length - 1) {
				textview.setTextColor(getResources().getColor(R.color.location));
			}
			if (i == 0) {
				textview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}
			String pathClicked = separator;
			if (i != 0) {
				pathClicked = currentParentPath.substring(
						0,
						currentParentPath.indexOf(parents[i])
								+ parents[i].length());
			}
			final String path = pathClicked;
			textview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					currentParentPath = path;
					new CloudFileAsyncTask().execute(path);
				}
			});
			location.addView(textview);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					s.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
				}
			}, 100L);
		}
	}

	private void createFolderAlertDialog() {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.new_folder_edittext, null);
		final AlertDialog dialog = buildEditTextAlertDialog(view);
		final EditText edittext = (EditText) view.findViewById(R.id.input);
		final View msg = view.findViewById(R.id.msg);
		edittext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String input = edittext.getText().toString();
				boolean enabled;
				boolean visible;
				if (input == null || input.trim().equals("")) {
					enabled = false;
					visible = false;
				} else if (isCloudFileExists(input)) {
					enabled = false;
					visible = true;
				} else {
					enabled = true;
					visible = false;
				}
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(
						enabled);
				msg.setVisibility(visible ? VISIBLE : GONE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		dialog.show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
	}

	private AlertDialog buildEditTextAlertDialog(final View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
				.setTitle(R.string.create_folder).setView(view);
		builder.setPositiveButton(R.string.OK,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText edittext = (EditText) view
								.findViewById(R.id.input);
						String newFolderName = edittext.getText().toString();
						createFolder(newFolderName);
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

	private boolean isCloudFileExists(String newFolderName) {
		for (CloudFile cloudFile : currentFiles) {
			if (cloudFile.getName(separator).equals(newFolderName)) {
				return true;
			}
		}
		return false;
	}

	private void createFolder(final String newFolderName) {
		new AsyncTask<String, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(String... params) {
				try {
					mCloud.createFolder(currentParentPath
							+ (inRootPath() ? "" : separator) + params[0]);
					return true;
				} catch (DropboxException e) {
					return false;
				} catch (Exception e) {
					return false;
				}
			}

			protected void onPostExecute(Boolean result) {
				currentParentPath += ((inRootPath() ? "" : separator) + newFolderName);
				new CloudFileAsyncTask().execute(currentParentPath);
				int msgId = result ? R.string.foldercreated
						: R.string.failToCreate;
				ViewHelper.showToastMessage(mContext, msgId);
			};

		}.execute(newFolderName);
		new CloudFileAsyncTask().execute(currentParentPath);
	}

	private boolean inRootPath() {
		return currentParentPath.equals(separator);
	}

	private class SortFileName implements Comparator<CloudFile> {

		@Override
		public int compare(CloudFile f1, CloudFile f2) {
			return f1.getPath().compareToIgnoreCase(f2.getPath());
		}
	}

	private class SortFolder implements Comparator<CloudFile> {

		@Override
		public int compare(CloudFile f1, CloudFile f2) {
			if ((f1.isDir() && f2.isDir()) || (!f1.isDir() && !f2.isDir()))
				return 0;
			else if (f1.isDir() && !f2.isDir())
				return -1;
			else
				return 1;
		}
	}

	class CloudFileAsyncTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog dialog;

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				loadFiles(params[0]);
				return true;
			} catch (DropboxUnlinkedException e) {
				mCloud.fixSocket();
				try {
					loadFiles(params[0]);
				} catch (Exception e1) {
					return false;
				}
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(mContext);
			dialog.setMessage(getResources().getString(R.string.loading));
			dialog.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				inflateListView(currentFiles);
			} else {
				activity.setCloudFolder(null);
				ViewHelper.showToastMessage(mContext, R.string.notConnect);
			}
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}

		private void loadFiles(String remotePath) throws Exception {
			currentFiles = mCloud.listFiles(remotePath);
			currentFiles.remove(0);
		}

	}

}
