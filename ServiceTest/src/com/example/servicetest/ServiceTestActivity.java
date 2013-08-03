package com.example.servicetest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.servicetest.FirstService.MyBinder;

public class ServiceTestActivity extends Activity {

	private Button start, stop, bind, unbind, getState;
	FirstService.MyBinder binder;

	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			System.out.println("--Service Disconnected--");
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			System.out.println("--Service connected");
			binder = (FirstService.MyBinder) service;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		start = (Button) findViewById(R.id.startService);
		stop = (Button) findViewById(R.id.stopService);

		bind = (Button) findViewById(R.id.bindService);
		unbind = (Button) findViewById(R.id.unbindService);
		getState = (Button) findViewById(R.id.getServiceState);

		final Intent intent = new Intent();
		intent.setAction("com.example.ServiceTest.FisrtService");

		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startService(intent);
			}
		});
		stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopService(intent);
			}
		});
		bind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bindService(intent, conn, BIND_AUTO_CREATE);
			}
		});
		unbind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				unbindService(conn);
			}
		});
		getState.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(ServiceTestActivity.this, "Service count value: " + binder.getCount(),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.service_test, menu);
		return true;
	}

}
