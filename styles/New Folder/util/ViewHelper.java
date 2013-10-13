package com.nachuantech.opensync.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.nachuantech.sync.R;

public class ViewHelper {

	public static void customActinBarCenterTitleView(Context context,
			ActionBar actionBar) {
		// set LayoutParams
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
		// Set display to custom next
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		// Do any other config to the action bar
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		// Get custom view
		TextView actionbarTitleCenterView = (TextView) LayoutInflater.from(
				context).inflate(R.layout.actionbar_title, null);
		// Now set custom view
		actionBar.setCustomView(actionbarTitleCenterView, params);
	}

	public static void setActionBarTitle(ActionBar actionBar, int titleId) {
		TextView actionBarTitle = (TextView) (actionBar.getCustomView());
		actionBarTitle.setText(titleId);
	}

	public static void showToastMessage(Context context, int msgId) {
		Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
	}

}
