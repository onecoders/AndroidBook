package com.example.testviewpage;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author chenyali
 * 
 */
public class NewsActivity extends Activity implements OnClickListener,
		OnPageChangeListener {

	private ViewPager mViewPager;
	private ViewGroup mViewGroup;
	private NewsViewPagerAdapter mAdapter;
	private String[] mTabItems = new String[] {"社会", "国内", "国际", "娱乐","今日头条",
			"体育", "军事", "教育", "得瑟", "睡觉", "哈哈" };
	private int mPreSelectItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.channel_news);

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewGroup = (ViewGroup) findViewById(R.id.viewgroup);
		addViewPagerView();
	}

	@SuppressLint("NewApi")
	private void addViewPagerView() {
		LayoutParams params = new LayoutParams();
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;

		LayoutInflater inflater = this.getLayoutInflater();
		ArrayList<View> newview = new ArrayList<View>();
		for (int i = 0; i < mTabItems.length; i++) {

			String label = mTabItems[i];

			View v = inflater.inflate(R.layout.channel_news_view_pager_item,
					null);
			((TextView) v.findViewById(R.id.content)).setText(label);
			newview.add(v);

			NewsTitleTextView tv = new NewsTitleTextView(this);
			int itemWidth = (int) tv.getPaint().measureText(label);
			tv.setLayoutParams(new LinearLayout.LayoutParams((itemWidth * 2),
					-1));
			tv.setTextSize(20);
			tv.setText(label);
			tv.setGravity(Gravity.CENTER);
			tv.setOnClickListener(this);
			if (i == 0) {
				tv.setTextColor(Color.RED);
				tv.setIsHorizontaline(true);
			} else {
				tv.setTextColor(Color.BLACK);
				tv.setIsHorizontaline(false);
			}
			mViewGroup.addView(tv);
		}

		mAdapter = new NewsViewPagerAdapter(this, newview);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);

	}

	@Override
	public void onClick(View v) {
		// 点击tabbar
		for (int i = 0; i < mViewGroup.getChildCount(); i++) {
			NewsTitleTextView child = (NewsTitleTextView) mViewGroup
					.getChildAt(i);
			if (child == v) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int selectPosition) {
		moveTitleLabel(selectPosition);
	}

	/*
	 * 点击新闻分类的tabbar，使点击的bar居中显示到屏幕中间
	 */
	@SuppressLint("NewApi")
	private void moveTitleLabel(int position) {

		// 点击当前按钮所有左边按钮的总宽度
		int visiableWidth = 0;
		// HorizontalScrollView的宽度
		int scrollViewWidth = 0;

		mViewGroup.measure(mViewGroup.getMeasuredWidth(), -1);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				mViewGroup.getMeasuredWidth(), -1);
		params.gravity = Gravity.CENTER_VERTICAL;
		mViewGroup.setLayoutParams(params);
		for (int i = 0; i < mViewGroup.getChildCount(); i++) {
			NewsTitleTextView itemView = (NewsTitleTextView) mViewGroup
					.getChildAt(i);
			int width = itemView.getMeasuredWidth();
			if (i < position) {
				visiableWidth += width;
			}
			scrollViewWidth += width;

			if (i == mViewGroup.getChildCount() - 1) {
				break;
			}
			if (position != i) {
				itemView.setTextColor(Color.BLACK);
				itemView.setIsHorizontaline(false);
			} else {
				itemView.setTextColor(Color.RED);
				itemView.setIsHorizontaline(true);
			}
		}
		// 当前点击按钮的宽度
		int titleWidth = mViewGroup.getChildAt(position).getMeasuredWidth();
		int nextTitleWidth = 0;
		if (position > 0) {
			// 当前点击按钮相邻右边按钮的宽度
			nextTitleWidth = position == mViewGroup.getChildCount() - 1 ? 0
					: mViewGroup.getChildAt(position - 1).getMeasuredWidth();
		}
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		final int move = visiableWidth - (screenWidth - titleWidth) / 2;
		if (mPreSelectItem < position) {// 向屏幕右边移动
			if ((visiableWidth + titleWidth + nextTitleWidth) >= (screenWidth / 2)) {
				// new Handler().post(new Runnable() {
				//
				// @Override
				// public void run() {
				((HorizontalScrollView) mViewGroup.getParent())
						.setScrollX(move);
				// }
				// });

			}
		} else {// 向屏幕左边移动
			if ((scrollViewWidth - visiableWidth) >= (screenWidth / 2)) {
				((HorizontalScrollView) mViewGroup.getParent())
						.setScrollX(move);
			}
		}
		mPreSelectItem = position;
	}

}
