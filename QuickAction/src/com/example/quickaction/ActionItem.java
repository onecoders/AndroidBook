package com.example.quickaction;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ActionItem {
	private Context context;
	private Drawable hintIcon;
	private String hintText;
	private String btnText;
	private boolean isShow;

	public ActionItem() {
	}

	public ActionItem(Context context, int hintIconId, int hintTextId,
			int btnTextId) {
		this.context = context;
		this.hintIcon = context.getResources().getDrawable(hintIconId);
		this.hintText = context.getResources().getString(hintTextId);
		this.btnText = context.getResources().getString(btnTextId);
	}

	public Drawable getHintIcon() {
		return hintIcon;
	}

	public void setHintIcon(Drawable hintIcon) {
		this.hintIcon = hintIcon;
	}

	public String getHintText() {
		return hintText;
	}

	public void setHintText(String hintText) {
		this.hintText = hintText;
	}

	public String getBtnText() {
		return btnText;
	}

	public void setBtnText(String btnText) {
		this.btnText = btnText;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

}
