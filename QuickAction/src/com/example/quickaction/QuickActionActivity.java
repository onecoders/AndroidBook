package com.example.quickaction;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class QuickActionActivity extends Activity {

	private Button btn1, btn2, btn3;
	private ActionItem actionItem;
	private QuickAction mQuickAction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		actionItem = new ActionItem(this, R.drawable.menu_down_arrow,
				R.string.hintText, R.string.btnText);

		mQuickAction = new QuickAction(this);

		mQuickAction.addActionItem(actionItem);

		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);

	}

	public void onClick(View v) {
		mQuickAction.show(v);
		mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_LEFT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quick_action, menu);
		return true;
	}

}
