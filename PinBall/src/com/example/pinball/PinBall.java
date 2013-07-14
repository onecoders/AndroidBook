package com.example.pinball;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class PinBall extends Activity {

	private int tableWidth;
	private int tableHeight;
	private int racketY;
	private final int RACKET_HEIGHT = 20;
	private final int RACKET_WIDTH = 70;

	private final int BALL_SIZE = 12;
	private int ySpeed = 10;
	Random rand = new Random();
	private double xyRate = rand.nextDouble() - 0.5;
	private int xSpeed = (int) (ySpeed * xyRate * 2);
	private int ballX = rand.nextInt(200) + 20;
	private int ballY = rand.nextInt(10) + 20;

	private int racketX = rand.nextInt(200);
	private boolean isLose = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		final GameView gameView = new GameView(this);
		setContentView(gameView);
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);

		tableWidth = metrics.widthPixels;
		tableHeight = metrics.heightPixels;
		racketY = tableHeight - 80;
		final Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 0x123) {
					gameView.invalidate();
				}
			};
		};

		gameView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				racketX = (int) event.getX();
				gameView.invalidate();
				return true;
			}
		});

		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (ballX <= 0 || ballX >= tableWidth - BALL_SIZE) {
					xSpeed = -xSpeed;
				}
				if (ballY >= racketY - BALL_SIZE
						&& (ballX < racketX || ballX > racketX + RACKET_WIDTH)) {
					timer.cancel();
					isLose = true;
				} else if (ballY <= 0
						|| (ballY >= racketY - BALL_SIZE && ballX > racketX && ballX <= racketX
								+ RACKET_WIDTH)) {
					ySpeed = -ySpeed;
				}
				ballY += ySpeed;
				ballX += xSpeed;
				handler.sendEmptyMessage(0x123);
			}
		}, 0, 50);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pin_ball, menu);
		return true;
	}

	class GameView extends View {

		Paint paint = new Paint();

		public GameView(Context context) {
			super(context);
			setFocusable(true);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			paint.setStyle(Paint.Style.FILL);
			paint.setAntiAlias(true);
			if (isLose) {
				paint.setColor(Color.RED);
				paint.setTextSize(40);
				canvas.drawText("Game over", 50, 200, paint);
			} else {
				paint.setColor(Color.RED);
				canvas.drawCircle(ballX, ballY, BALL_SIZE, paint);
				paint.setColor(Color.rgb(80, 80, 200));
				canvas.drawRect(racketX, racketY, racketX + RACKET_WIDTH,
						racketY + RACKET_HEIGHT, paint);
			}
		}
	}

}
