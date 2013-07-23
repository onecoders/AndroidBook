package com.example.notificationtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NotificationTest extends Activity implements OnClickListener {

	static final int NOTIFICATION_ID = 0x123;
	NotificationManager nm;
	private Button send, cancel, dialog;
	private TextView show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		send = (Button) findViewById(R.id.send);
		cancel = (Button) findViewById(R.id.cancel);
		dialog = (Button) findViewById(R.id.dialog);
		send.setOnClickListener(this);
		cancel.setOnClickListener(this);
		dialog.setOnClickListener(this);
		show = (TextView) findViewById(R.id.show);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notification_test, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.send) {
			Intent intent = new Intent(NotificationTest.this,
					SecondActivity.class);
			PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
			Notification notify = new Notification.Builder(this)
					.setAutoCancel(true)
					.setTicker("New Message")
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("One Notification!")
					.setContentText("Congratulations to you !")
					.setDefaults(
							Notification.DEFAULT_SOUND
									| Notification.DEFAULT_LIGHTS)
					.setWhen(System.currentTimeMillis()).setContentIntent(pi)
					.build();
			nm.notify(NOTIFICATION_ID, notify);
		} else if (v.getId() == R.id.cancel) {
			nm.cancel(NOTIFICATION_ID);
		} else if (v.getId() == R.id.dialog) {
			createDialog();
		}
	}

	private void createDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle("Simple Dialog").setIcon(R.drawable.ic_launcher)
				.setMessage("Dialog Content\nSecond");
		//setNegativeButton(setPositiveButton(builder)).create().show();
		setClickableButton(setClickableButton(builder, true), false).create().show();
	}

	private Builder setNegativeButton(Builder builder) {
		return builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						show.setText("you click the Cancel button");
					}
				});
	}

	private Builder setPositiveButton(Builder builder) {
		return builder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						show.setText("you click the OK button");
					}
				});
	}
	
	private Builder setClickableButton(Builder builder, boolean isPositive){
		if (isPositive) {
			return setPositiveButton(builder);
		} else {
			return setNegativeButton(builder);
		}
	}
}
