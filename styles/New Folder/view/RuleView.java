package com.nachuantech.opensync.view;

import java.util.Arrays;

import opensync.syncPair.SyncRule;
import opensync.syncPair.SyncRule.Direction;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nachuantech.sync.R;
import com.nachuantech.opensync.activity.SyncConfigActivity;

public class RuleView extends RelativeLayout implements OnClickListener {

	private Context mContext;
	private SyncRule syncRule;

	private Button finish, cancel;
	private View interval, connection, method, filterHelp,
			conflictReplacePatternHelp, add, modify;
	private FlowLayout filtersContainer;
	private TextView conflictReplacePatternText, intervalText, connectionText,
			methodText;

	private String[] intervalArray, connectionArray, methodArray;
	private int intervalPos, connectionPos, methodPos;
	private String fileFilter, conflictReplacePattern;

	private SyncConfigActivity activity;

	private int[] intervals = { 5, 15, 30, 60, 120, 240, 360, 720, 1440 };

	public RuleView(Context context) {
		super(context);
		this.mContext = context;
		this.activity = (SyncConfigActivity) context;
		inflate(mContext, R.layout.advance_config, this);
		init();
	}

	private void init() {
		initSyncRule();
		findViews();
		initArrayFromRes();
		setOriginalConfig();
		setListener();
	}

	private void initSyncRule() {
		syncRule = activity.getSyncRule();
	}

	private void findViews() {
		interval = findViewById(R.id.interval);
		connection = findViewById(R.id.connection);
		method = findViewById(R.id.method);
		finish = (Button) findViewById(R.id.finish);
		cancel = (Button) findViewById(R.id.cancel);
		filterHelp = findViewById(R.id.filterHelp);
		conflictReplacePatternHelp = findViewById(R.id.conflictReplacePatternHelp);
		add = findViewById(R.id.filterAdd);
		modify = findViewById(R.id.conflictReplacePatternUpdate);
		filtersContainer = (FlowLayout) findViewById(R.id.filtersContainer);
		intervalText = (TextView) findViewById(R.id.interval_text);
		connectionText = (TextView) findViewById(R.id.connection_text);
		methodText = (TextView) findViewById(R.id.method_text);
		conflictReplacePatternText = (TextView) findViewById(R.id.conflictReplacePatternText);
	}

	private void setListener() {
		interval.setOnClickListener(this);
		connection.setOnClickListener(this);
		method.setOnClickListener(this);
		add.setOnClickListener(this);
		modify.setOnClickListener(this);
		finish.setOnClickListener(this);
		cancel.setOnClickListener(this);
		filterHelp.setOnClickListener(this);
		conflictReplacePatternHelp.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.finish:
			updateRule();
			activity.setSyncRule(syncRule);
			break;
		case R.id.cancel:
			activity.onBackPressed();
			break;
		case R.id.filterAdd:
		case R.id.conflictReplacePatternUpdate:
			createEditTextDialog(id);
			break;
		case R.id.filterHelp:
			showHelperAlertDialog(R.string.filterDes, R.string.aboutFilter);
			break;
		case R.id.conflictReplacePatternHelp:
			showHelperAlertDialog(R.string.conflictReplacePatternDes,
					R.string.aboutConflictReplacePattern);
			break;
		case R.id.interval:
			showDialogSingleChoice(id, intervalPos, R.string.intervalTitle,
					intervalArray);
			break;
		case R.id.connection:
			showDialogSingleChoice(id, connectionPos, R.string.connection,
					connectionArray);
			break;
		case R.id.method:
			showDialogSingleChoice(id, methodPos, R.string.syncMethod,
					methodArray);
			break;
		default:
			break;
		}
	}

	private void showDialogSingleChoice(final int viewId, int checkedPos,
			int titleId, String[] array) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(titleId).setSingleChoiceItems(array, checkedPos,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (viewId) {
						case R.id.interval:
							intervalPos = which;
							intervalText.setText(intervalArray[which]);
							break;
						case R.id.connection:
							connectionPos = which;
							connectionText.setText(connectionArray[which]);
							break;
						case R.id.method:
							methodPos = which;
							methodText.setText(methodArray[which]);
							break;
						default:
							break;
						}
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	private void initArrayFromRes() {
		intervalArray = getResources().getStringArray(R.array.interval);
		connectionArray = getResources().getStringArray(R.array.connection);
		methodArray = getResources().getStringArray(R.array.method);
	}

	private void setOriginalConfig() {
		// interval
		intervalPos = Arrays.binarySearch(intervals, syncRule.getInterval());
		intervalText.setText(intervalArray[intervalPos]);
		// connection
		connectionPos = syncRule.isWifiOnly() ? 0 : 1;
		connectionText.setText(connectionArray[connectionPos]);
		// method
		methodPos = syncRule.getDirection().getMode() - 1;
		methodText.setText(methodArray[methodPos]);
		// filter
		fileFilter = syncRule.getFileFilter();
		updateFilterView(fileFilter);
		// conflict replace pattern
		conflictReplacePattern = syncRule.getConflictReplacePattern();
		conflictReplacePatternText.setText(conflictReplacePattern);
	}

	private void updateFilterView(String filter) {
		String[] filters = filter.split("\n");
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		filtersContainer.removeAllViews();
		for (String string : filters) {
			if (string.trim().equals("")) {
				continue;
			}
			View parent = layoutInflater.inflate(R.layout.filter_text, null);
			TextView child = (TextView) parent.findViewById(R.id.filter_text);
			child.setText(string);
			filtersContainer.addView(parent);
		}
	}

	private void updateRule() {
		syncRule.setInterval(intervals[intervalPos]);
		syncRule.setWifiOnly(connectionPos == 0);
		syncRule.setDirection(Direction.getDirection(methodPos + 1));
		syncRule.setFileFilter(fileFilter);
		syncRule.setConflictReplacePattern(conflictReplacePattern);
	}

	private void showHelperAlertDialog(int textResId, int titleId) {
		TextView description = (TextView) LayoutInflater.from(mContext)
				.inflate(R.layout.help_for_des, null);
		description.setMovementMethod(new ScrollingMovementMethod());
		description.setText(textResId);
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
				.setTitle(titleId).setView(description);
		builder.setPositiveButton(R.string.gotIt,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.create().show();
	}

	private void createEditTextDialog(final int viewId) {
		final EditText edittext = (EditText) LayoutInflater.from(mContext)
				.inflate(
						viewId == R.id.filterAdd ? R.layout.filter_edittext
								: R.layout.conflict_edittext, null);
		final AlertDialog dialog = buildDialog(edittext, viewId);
		edittext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (viewId == R.id.conflictReplacePatternUpdate) {
					if (edittext.getText() == null
							|| edittext.getText().toString().trim().equals("")) {
						dialog.getButton(AlertDialog.BUTTON_POSITIVE)
								.setEnabled(false);
					} else {
						dialog.getButton(AlertDialog.BUTTON_POSITIVE)
								.setEnabled(true);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		dialog.show();
		if (viewId == R.id.conflictReplacePatternUpdate) {
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(
					!edittext.getText().toString().trim().equals(""));
		}
	}

	private AlertDialog buildDialog(final EditText edittext, final int viewId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		if (viewId == R.id.filterAdd) {
			builder.setTitle(R.string.addFilter);
			edittext.setText(fileFilter);
		} else if (viewId == R.id.conflictReplacePatternUpdate) {
			builder.setTitle(R.string.updateConflictReplacePattern);
			edittext.setText(conflictReplacePattern);
		}
		builder.setView(edittext);
		builder.setPositiveButton(R.string.OK,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String input = edittext.getText().toString();
						if (viewId == R.id.filterAdd) {
							fileFilter = input;
							updateFilterView(fileFilter);
						}
						if (viewId == R.id.conflictReplacePatternUpdate) {
							conflictReplacePattern = input;
							conflictReplacePatternText
									.setText(conflictReplacePattern);
						}
					}
				});
		builder.setNegativeButton(R.string.cancel, null);
		final AlertDialog dialog = builder.create();
		return dialog;
	}

}
