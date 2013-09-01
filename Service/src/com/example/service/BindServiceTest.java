package com.example.service;

import android.app.Activity;
import android.app.Service;
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

public class BindServiceTest extends Activity {

	private Button bind, unbind, getServiceStatus, add;
	private BindService.MyBinder binder;

	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (BindService.MyBinder) service;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		bind = (Button) findViewById(R.id.bind);
		unbind = (Button) findViewById(R.id.unbind);
		getServiceStatus = (Button) findViewById(R.id.getStatus);
		add = (Button) findViewById(R.id.add);

		final Intent intent = new Intent();
		intent.setAction("com.example.service.BindService");

		bind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bindService(intent, conn, Service.BIND_AUTO_CREATE);
			}
		});

		unbind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				unbindService(conn);
			}
		});

		getServiceStatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(BindServiceTest.this,
						"Service's count value is :" + binder.getCount(),
						Toast.LENGTH_SHORT).show();
			}
		});

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				binder.doInBackground();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bind, menu);
		return true;
	}

}
