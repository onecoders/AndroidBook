package com.example.syncfiletest;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static final String regex = "^\\..+/.+|.+\\.note$";
	public static final String startDir = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ "handrite";

	private TextView textview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		textview = (TextView) findViewById(R.id.textview);
		TreeInfo treeInfo = TreeInfo.walk(startDir, regex);
		List<File> files = treeInfo.files;
		List<File> dirs = treeInfo.dirs;
		String text = "";
		for (File file : files) {
			text += (file.getAbsolutePath() + "\n");
		}
		textview.setText(text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
