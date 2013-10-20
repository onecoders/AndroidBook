package com.activity;

import java.util.List;

import opensync.cloudAccount.CloudAccount;
import opensync.cloudAccount.CloudAccountManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.nachuantech.sync.R;
import com.nachuantech.opensync.adapter.AccountManagerAdapter;
import com.nachuantech.opensync.util.ViewHelper;

public class AccountManagerActivity extends SherlockActivity {

	private ListView accountManagerListView;

	private CloudAccountManager cloudAccountManager;
	private List<CloudAccount> accountList;
	private ArrayAdapter<CloudAccount> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_manager);
		init();
	}

	private void init() {
		cloudAccountManager = CloudAccountManager
				.getInstance(getApplicationContext());
		findViews();
		loadData();
		initAccountListView();
	}

	private void findViews() {
		accountManagerListView = (ListView) findViewById(R.id.select_account);
	}

	private void loadData() {
		accountList = cloudAccountManager.getAll();
	}

	private void initAccountListView() {
		accountManagerListView.setEmptyView(findViewById(R.id.empty));
		adapter = new AccountManagerAdapter(this, accountList);
		accountManagerListView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setActinBarCenterTitleCustomView();
		return super.onCreateOptionsMenu(menu);
	}

	private void setActinBarCenterTitleCustomView() {
		ActionBar actionBar = getSupportActionBar();
		ViewHelper.customActinBarCenterTitleView(this, actionBar);
		ViewHelper.setActionBarTitle(actionBar, R.string.account_manager);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
