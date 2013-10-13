package com.nachuantech.opensync.adapter;

import java.util.List;

import opensync.cloudAccount.CloudAccount;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.nachuantech.opensync.view.AccountManagerItemView;

public class AccountManagerAdapter extends ArrayAdapter<CloudAccount> {

	private List<CloudAccount> items;

	public AccountManagerAdapter(Context context, List<CloudAccount> items) {
		super(context, 0, items);
		this.items = items;
	}

	@Override
	public CloudAccount getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new AccountManagerItemView(getContext(),
					getItem(position), this);
		} else {
			((AccountManagerItemView) convertView)
					.setAccount(getItem(position));
		}
		return convertView;
	}
}
