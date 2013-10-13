package com.nachuantech.opensync.view;

import java.util.List;

import opensync.cloudAccount.CloudAccount;
import opensync.cloudAccount.CloudAccountManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nachuantech.sync.R;
import com.nachuantech.opensync.activity.SyncConfigActivity;
import com.nachuantech.opensync.adapter.AccountLoginAdapter;

public class AccountView extends RelativeLayout implements OnItemClickListener {

	private Context mContext;
	private SyncConfigActivity activity;

	private ListView accountManagerListView;
	private View headerView;

	private CloudAccountManager cloudAccountManager;
	private List<CloudAccount> accountList;
	private ArrayAdapter<CloudAccount> adapter;

	public AccountView(Context context) {
		super(context);
		this.mContext = context;
		this.activity = (SyncConfigActivity) context;
		inflate(mContext, R.layout.fragment_account, this);
		init();
	}

	private void init() {
		findViews();
		loadData();
		initAccountListView();
		setListener();
	}

	private void findViews() {
		accountManagerListView = (ListView) findViewById(R.id.select_account);
		headerView = LayoutInflater.from(mContext).inflate(
				R.layout.new_account_header, null);
	}

	private void loadData() {
		cloudAccountManager = CloudAccountManager.getInstance(activity
				.getApplicationContext());
		accountList = cloudAccountManager.getAll();
	}

	private void initAccountListView() {
		accountManagerListView.addHeaderView(headerView);
		adapter = new AccountLoginAdapter(mContext, accountList);
		accountManagerListView.setAdapter(adapter);
	}

	private void setListener() {
		accountManagerListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {
			activity.setAccount(null);
		} else {
			int accountPos = position - 1;
			CloudAccount account = accountList.get(accountPos);
			activity.setAccount(account);
		}
	}

}
