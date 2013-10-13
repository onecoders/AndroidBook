package com.nachuantech.opensync.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;

import com.nachuantech.sync.R;

public class SideView extends View {

	public SideView(Context context) {
		super(context);
		init();
	}

	public SideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SideView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setBackgroundResource(R.color.landscope_two_bar);
		Configuration initConfig = getResources().getConfiguration();
		setCoupleBarVisibility(initConfig);
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setCoupleBarVisibility(newConfig);
	}

	private void setCoupleBarVisibility(Configuration config) {
		if (tabletLandscape(config)) {
			setVisibility(VISIBLE);
		} else {
			setVisibility(GONE);
		}
	}

	private static boolean tabletLandscape(Configuration config) {
		return isTablet(config) && isLandscape(config);
	}

	private static boolean isTablet(Configuration config) {
		return (config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	private static boolean isLandscape(Configuration config) {
		return config.orientation == Configuration.ORIENTATION_LANDSCAPE;
	}

}
