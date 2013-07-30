package com.filetest;

import java.io.File;

class CreateFolderAlertDialog implements OnDismissListener, OnCancelListener {

	private EditText edittext;
	private AlertDialog alertDialog;

	private boolean canceled;

	public CreateFolderAlertDialog() {
		edittext = new EditText(mContext);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		edittext.setLayoutParams(lp);
		alertDialog = buildAlertDialog();
		alertDialog.setOnDismissListener(this);
		alertDialog.setOnCancelListener(this);
		alertDialog.setCanceledOnTouchOutside(true);
		show();
	}

	private void show() {
		canceled = false;
		alertDialog.show();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		canceled = true;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if (!canceled) {
			String newFolderName = edittext.getText().toString();
			if (newFolderName == null || newFolderName.trim().equals("")
					|| newFolderName.trim().isEmpty()) {
				Toast.makeText(getActivity(), R.string.emptyAlert,
						Toast.LENGTH_SHORT).show();
				show();
			} else {
				File newFolder = new File(currentParent, newFolderName);
				newFolder.mkdirs();
				if (newFolder.exists()) {
					Toast.makeText(getActivity(), R.string.foldercreated,
							Toast.LENGTH_SHORT).show();
					currentFiles = currentParent.listFiles();
					inflateListView(currentFiles);
				}
			}
		}
	}

	private AlertDialog buildAlertDialog() {
		return new AlertDialog.Builder(mContext)
				.setTitle(R.string.newFolder)
				.setView(edittext)
				.setPositiveButton(R.string.OK, null)
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								canceled = true;
							}

						}).create();
	}

}
