package com.example.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BroadcastMain extends Activity {

	private Button sendBroadcast;
	private TextView receiver;

	private static final String BROADCAST_ACTION = "com.example.action.BROADCAST";

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			receiver.setText(intent.getStringExtra("msg"));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		sendBroadcast = (Button) findViewById(R.id.sendBroadcast);
		receiver = (TextView) findViewById(R.id.receiver);
		sendBroadcast.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(BROADCAST_ACTION);
				intent.putExtra("msg", "A simple message");
				sendBroadcast(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.broadcast_main, menu);
		return true;
	}

}
