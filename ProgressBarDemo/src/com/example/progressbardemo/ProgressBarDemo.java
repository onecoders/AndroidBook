package com.example.progressbardemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ProgressBarDemo extends Activity {

	private TextView statusTextView;
	private Button beginBtn;
	private ProgressDialog progressDialog;
	private int calTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		statusTextView = (TextView) findViewById(R.id.status);
		beginBtn = (Button) findViewById(R.id.beginBtn);
		setListener();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				progressDialog.dismiss();
				statusTextView.setText("Completed!" + calTime++);				
			}
		};
	};

	private void setListener() {
		beginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				progressDialog = ProgressDialog.show(ProgressBarDemo.this,
						"Loading...", "Please wait...", true, false);
				
				new Thread(){
					public void run() {
						Calculation.calculate(4);
						Message msg = new Message();
						msg.what = 0x123;
						handler.sendMessage(msg);
					};
				}.start();
			}
		});
	}
	
	public static class Calculation{
		public static void calculate(int sleepSeconds){
			try {
				Thread.sleep(sleepSeconds*1000);
			} catch (Exception e) {
				
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.progress_bar_demo, menu);
		return true;
	}

}
