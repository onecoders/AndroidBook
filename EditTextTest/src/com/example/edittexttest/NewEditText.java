package com.example.edittexttest;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class NewEditText extends EditText {

	private Drawable imgCloseButton = getResources().getDrawable(
			R.drawable.btn_clear);

	public NewEditText(Context context) {
		super(context);
		init();
	}

	public NewEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public NewEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		imgCloseButton.setBounds(0, 0, imgCloseButton.getIntrinsicWidth(),
				imgCloseButton.getIntrinsicHeight());

		handleClearButton();

		this.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				NewEditText et = NewEditText.this;

				if (et.getCompoundDrawables()[2] == null) {
					return false;
				}

				if (event.getAction() != MotionEvent.ACTION_UP) {
					return false;
				}

				if (event.getX() > et.getWidth() - et.getPaddingRight()
						- imgCloseButton.getIntrinsicWidth()) {
					et.setText("");
					NewEditText.this.handleClearButton();
				}
				return false;
			}
		});

		this.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				NewEditText.this.handleClearButton();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void handleClearButton() {
		if (this.getText().toString().equals("")) {
			this.setCompoundDrawables(this.getCompoundDrawables()[0],
					this.getCompoundDrawables()[1], null,
					this.getCompoundDrawables()[3]);
		} else {
			this.setCompoundDrawables(this.getCompoundDrawables()[0],
					this.getCompoundDrawables()[1], imgCloseButton,
					this.getCompoundDrawables()[3]);
		}
	}

}
