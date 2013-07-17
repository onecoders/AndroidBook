package com.dropbox.sample.hellodropbox;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

public class HelloDropboxActivity extends Activity {

	private static final String appKey = "50cuc9y6ulgkq5g";
	private static final String appSecret = "q7zfefehr2iuuky";

	private static final int REQUEST_LINK_TO_DBX = 0;

	private TextView mTestOutput;
	private Button mLinkButton;
	private DbxAccountManager mDbxAcctMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hello_dropbox);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mTestOutput = (TextView) findViewById(R.id.test_output);
		mLinkButton = (Button) findViewById(R.id.link_button);
		mLinkButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onClickLinkToDropbox();
			}
		});

		mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(),
				appKey, appSecret);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mDbxAcctMgr.hasLinkedAccount()) {
			showLinkedView();
			try {
				doDropboxTest();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			showUnlinkedView();
		}
	}

	private void showLinkedView() {
		mLinkButton.setVisibility(View.GONE);
		mTestOutput.setVisibility(View.VISIBLE);
	}

	private void showUnlinkedView() {
		mLinkButton.setVisibility(View.VISIBLE);
		mTestOutput.setVisibility(View.GONE);
	}

	private void onClickLinkToDropbox() {
		mDbxAcctMgr.startLink((Activity) this, REQUEST_LINK_TO_DBX);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_LINK_TO_DBX) {
			if (resultCode == Activity.RESULT_OK) {
				try {
					doDropboxTest();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				mTestOutput.setText("Link to Dropbox failed or was cancelled.");
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void doDropboxTest() throws Exception {
		try {
			DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr
					.getLinkedAccount());

			String sdcard = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + File.separator;
			File from = new File(sdcard + "handrite");

			recurseDirs(from);

			long timeBegin = System.currentTimeMillis();

			for (File file : result) {
				String path = file.getAbsolutePath().replace(sdcard, "");
				DbxPath testPath = new DbxPath(DbxPath.ROOT, path);

				DbxFile testFile = dbxFs.create(testPath);
				try {
					copy1(file, testFile);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (testFile != null) {
						testFile.close();
					}
				}
			}

			long timeEnd = System.currentTimeMillis();

			Log.v("Handrite", "time used:" + (timeEnd - timeBegin));

		} catch (IOException e) {
			mTestOutput.setText("Dropbox test failed: " + e);
		}
	}

	private void copy1(File from, DbxFile to) throws Exception {
		write(to, read(from));
	}

	private void copy2(File from, DbxFile to) throws Exception {
		to.writeFromExistingFile(from, false);
	}

	private byte[] read(File file) throws Exception {
		BufferedInputStream bis = null;
		byte[] buff = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			buff = new byte[bis.available()];
			bis.read(buff);
		} finally {
			if (bis != null) {
				bis.close();
			}
		}
		return buff;
	}

	private void write(DbxFile file, byte[] buff) throws Exception {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(file.getWriteStream());
			bos.write(buff);
			bos.flush();
		} finally {
			if (bos != null) {
				bos.close();
			}
		}
	}

	private List<File> result = new ArrayList<File>();

	private void recurseDirs(File startDir) {
		File[] list = startDir.listFiles();
		for (File item : list) {
			if (item.isDirectory()) {
				recurseDirs(item);
			} else {
				result.add(item);
			}
		}
	}
}
