package com.example.progressbartest;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

	private ProgressBar syncing_start, syncing, syncing_pause, syncing_end;

	public enum SyncState {
		START, SYNCING, PAUSE, END;
	}

	private SyncState state = SyncState.SYNCING;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		syncing_start = (ProgressBar) findViewById(R.id.syncing_start);
		syncing = (ProgressBar) findViewById(R.id.syncing);
		syncing_pause = (ProgressBar) findViewById(R.id.syncing_pause);
		syncing_end = (ProgressBar) findViewById(R.id.syncing_end);

		syncing_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				syncing_start.setVisibility(View.GONE);
				syncing.setVisibility(View.VISIBLE);
			}

		});

		syncing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				syncing.setVisibility(View.GONE);
				syncing_pause.setVisibility(View.VISIBLE);
			}
		});

		syncing_pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				syncing_pause.setVisibility(View.GONE);
				syncing.setVisibility(View.VISIBLE);
			}
		});

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x123) {
					syncing.setVisibility(View.GONE);
					syncing_pause.setVisibility(View.GONE);
					syncing_end.setVisibility(View.VISIBLE);
				}
				super.handleMessage(msg);
			}
		};

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(0x123);
			}
		}, 10000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
