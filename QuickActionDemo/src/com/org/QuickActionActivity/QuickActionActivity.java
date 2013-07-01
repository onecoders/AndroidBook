package com.org.QuickActionActivity;
import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

public class QuickActionActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		//Add action item
        ActionItem addAction = new ActionItem();
		
		addAction.setTitle("Add");
		addAction.setIcon(getResources().getDrawable(R.drawable.ic_add));

		//Accept action item
		ActionItem accAction = new ActionItem();
		
		accAction.setTitle("Accept");
		accAction.setIcon(getResources().getDrawable(R.drawable.ic_accept));
		
		//Upload action item
		ActionItem upAction = new ActionItem();
		
		upAction.setTitle("Upload");
		upAction.setIcon(getResources().getDrawable(R.drawable.ic_up));
		
		final QuickAction mQuickAction 	= new QuickAction(this);
		
		mQuickAction.addActionItem(addAction);
		mQuickAction.addActionItem(accAction);
		mQuickAction.addActionItem(upAction);
		
		//setup the action item click listener
		mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {			
			@Override
			public void onItemClick(int pos) {
				
				if (pos == 0) { //Add item selected
					Toast.makeText(QuickActionActivity.this, "Add item selected", Toast.LENGTH_SHORT).show();
				} else if (pos == 1) { //Accept item selected
					Toast.makeText(QuickActionActivity.this, "Accept item selected", Toast.LENGTH_SHORT).show();
				} else if (pos == 2) { //Upload item selected
					Toast.makeText(QuickActionActivity.this, "Upload items selected", Toast.LENGTH_SHORT).show();
				}	
			}
		});
		Button btn2 = (Button) this.findViewById(R.id.btn2);
		btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mQuickAction.show(v);
				mQuickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_LEFT);
			}
		});
	}
}