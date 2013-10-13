package com.nachuantech.opensync.view;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import opensync.SyncPairFilter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.nachuantech.sync.R;
import com.nachuantech.opensync.activity.SyncConfigActivity;
import com.nachuantech.opensync.adapter.FileAdapter;
import com.nachuantech.opensync.util.ViewHelper;

public class LocalFileView extends RelativeLayout implements OnClickListener,
		OnItemClickListener {

	private File currentParent;
	private File[] currentFiles;

	private Context mContext;
	private SyncConfigActivity activity;

	private ListView listView;
	private LinearLayout location;
	private HorizontalScrollView s;
	private Button select, cancel, newFolder;
	private View headerView;

	public LocalFileView(Context context) {
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
		File root = new File(activity.getLocalPath());
		if (root.exists()) {
			currentParent = root;
			inflateListView(currentParent);
		}
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
			activity.setLocalFolder(currentParent.getAbsolutePath());
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
		if (currentParent != null) {
			inflateListView(currentParent);
		}
	}

	private void upLevle() {
		currentParent = currentParent.getParentFile();
	}

	private void intoFolder(int position) {
		if (currentFiles[position].isFile()) {
			return;
		}
		currentParent = currentFiles[position];
	}

	private void inflateListView(File parentFile) {
		currentFiles = parentFile.listFiles();
		ArrayAdapter<File> adapter = null;
		if (currentFiles != null && currentFiles.length != 0) {
			Arrays.sort(currentFiles, new SortFileName());
			Arrays.sort(currentFiles, new SortFolder());
			adapter = new FileAdapter(mContext, currentFiles);
		}
		listView.setAdapter(adapter);
		String currentLocation = getResources().getString(R.string.phone)
				+ currentParent.getAbsolutePath();
		updateLocation(currentLocation);
		updateHeaderView();
	}

	private void updateHeaderView() {
		boolean alreadyTopLevel = currentParent.getAbsolutePath().equals("/");
		int visibility = alreadyTopLevel ? GONE : VISIBLE;
		headerView.findViewById(R.id.upLevel).setVisibility(visibility);
		headerView.findViewById(R.id.divider_top).setVisibility(visibility);
		// update select button
		String currentParentPath = currentParent.getAbsolutePath();
		boolean selectEnabled = (!alreadyTopLevel && !SyncPairFilter
				.isFilePathConflict(currentParentPath, mContext, false,
						activity.getSyncPair().getId()));
		select.setEnabled(selectEnabled);
	}

	private void updateLocation(String currentLocation) {
		location.removeAllViews();
		String[] parents = currentLocation.split("/");
		int arrayLength = parents.length;
		// update headerview
		if (!currentParent.getAbsolutePath().equals("/")) {
			((TextView) headerView.findViewById(R.id.upLevel))
					.setText(getResources().getString(R.string.go_up) + " "
							+ parents[arrayLength - 2]);
		}
		for (int i = 0; i < arrayLength; i++) {
			if (parents[i].equals("")) {
				continue;
			}
			TextView textview = (TextView) LayoutInflater.from(mContext)
					.inflate(R.layout.location_parent, null);
			textview.setText(parents[i]);
			if (i == parents.length - 1) {
				textview.setTextColor(getResources().getColor(R.color.location));
			}
			if (i == 0) {
				textview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			}
			String pathClicked = "/";
			if (i != 0) {
				String currentPath = currentParent.getAbsolutePath();
				pathClicked = currentPath.substring(0,
						currentPath.indexOf(parents[i]) + parents[i].length());
			}
			final File folderClicked = new File(pathClicked);
			textview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					currentParent = folderClicked;
					inflateListView(currentParent);
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
				} else if (isFileExists(input)) {
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

	private boolean isFileExists(String newFolderName) {
		return new File(currentParent, newFolderName).exists();
	}

	private void createFolder(String newFolderName) {
		File newFolder = new File(currentParent, newFolderName);
		boolean success = newFolder.mkdirs();
		if (success) {
			currentParent = newFolder;
			inflateListView(currentParent);
		}
		int msgId = success ? R.string.foldercreated : R.string.failToCreate;
		ViewHelper.showToastMessage(mContext, msgId);
	}

	private class SortFileName implements Comparator<File> {

		@Override
		public int compare(File f1, File f2) {
			return f1.getName().compareToIgnoreCase(f2.getName());
		}
	}

	private class SortFolder implements Comparator<File> {

		@Override
		public int compare(File f1, File f2) {
			if ((f1.isDirectory() && f2.isDirectory())
					|| (!f1.isDirectory() && !f2.isDirectory()))
				return 0;
			else if (f1.isDirectory() && !f2.isDirectory())
				return -1;
			else
				return 1;
		}
	}

}
