package com.example.alertdialogtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AlertDialogActivity extends Activity {

	private Button createDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		createDialog = (Button) findViewById(R.id.dialog);
		createDialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final EditText editText = new EditText(AlertDialogActivity.this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				editText.setLayoutParams(lp);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						AlertDialogActivity.this);
				builder.setTitle(R.string.dialogTitle).setView(editText);
				builder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Toast.makeText(AlertDialogActivity.this,
										editText.getText().toString(),
										Toast.LENGTH_SHORT).show();
							}
						});
				builder.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				final AlertDialog dialog = builder.create();

				editText.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						if (editText == null
								|| editText.getText().toString().trim()
										.equals("")) {
							dialog.getButton(AlertDialog.BUTTON_POSITIVE)
									.setEnabled(false);
						} else {
							dialog.getButton(AlertDialog.BUTTON_POSITIVE)
									.setEnabled(true);
						}
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {

					}

					@Override
					public void afterTextChanged(Editable s) {

					}
				});

				dialog.show();
				// must use (dialog.getButto() to get the button not reference)
				dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alert_dialog, menu);
		return true;
	}

}
