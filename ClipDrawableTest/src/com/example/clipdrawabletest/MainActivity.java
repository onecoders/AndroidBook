package com.example.clipdrawabletest;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.graphics.drawable.ClipDrawable;
import android.view.Menu;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		ImageView imageView = (ImageView) findViewById(R.id.image);
		final ClipDrawable drawable = (ClipDrawable) imageView.getDrawable();
		//always use the .png (.jpg could cause error)
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x123) {
					drawable.setLevel(drawable.getLevel() + 200);
				}
			}
		};
		
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 0x123;
				handler.sendMessage(msg);
				if (drawable.getLevel() >= 10000) {
					timer.cancel();
				}
			}
		}, 0, 300);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
