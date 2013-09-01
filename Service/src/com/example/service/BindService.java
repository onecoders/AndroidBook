package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class BindService extends Service {

	private int count;
	private boolean quit;

	private MyBinder binder = new MyBinder();

	public class MyBinder extends Binder {
		public int getCount() {
			return count;
		}

		public void doInBackground() {
			doInBackgroundInService();
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		new Thread() {
			public void run() {
				while (!quit) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					count++;
				}
			};
		}.start();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.quit = true;
	}

	private void doInBackgroundInService() {
		this.count += 100;
	}

}
