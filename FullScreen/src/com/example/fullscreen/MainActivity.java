package com.example.fullscreen;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.EditText;

public class MainActivity extends Activity {

	private boolean isFullScreen;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		@SuppressWarnings("deprecation")
		final GestureDetector gestureDetector = new GestureDetector( // implement DoubleClick
				new GestureDetector.SimpleOnGestureListener() {
					public boolean onDoubleTap(final MotionEvent e) {
						isFullScreen = !isFullScreen;
						ActionBar myActionBar = getActionBar();
						WindowManager.LayoutParams params = getWindow()
								.getAttributes();
						if (isFullScreen) {
							params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
							getWindow().setAttributes(params);
							getWindow()
									.addFlags(
											WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
							myActionBar.hide();
						} else {
							params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
							getWindow().setAttributes(params);
							getWindow()
									.clearFlags(
											WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
							myActionBar.show();
						}
						return true;
					}
				});

		EditText et = (EditText) findViewById(R.id.edittext);
		et.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetector.onTouchEvent(event);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
