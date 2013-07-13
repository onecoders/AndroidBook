package com.example.animatortest;

import android.animation.AnimatorInflater;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

public class AnimatorTest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		/*textview = (TextView) findViewById(R.id.textview);

		ObjectAnimator colorAnim = (ObjectAnimator) AnimatorInflater
				.loadAnimator(this, R.animator.my_anim);
		colorAnim.setEvaluator(new ArgbEvaluator());
		colorAnim.setTarget(textview);
		colorAnim.start();*/

		LinearLayout container = (LinearLayout) findViewById(R.id.container);
		container.addView(new MyAnimationView(this));
	}

	private class MyAnimationView extends View {
		public MyAnimationView(Context context) {
			super(context);
			ObjectAnimator colorAnim = (ObjectAnimator) AnimatorInflater
					.loadAnimator(getContext(), R.animator.my_anim);
			colorAnim.setEvaluator(new ArgbEvaluator());
			colorAnim.setTarget(this);
			colorAnim.start();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.animator_test, menu);
		return true;
	}

}
