package com.nachuantech.opensync.adapter;

import java.util.List;

import opensync.cloudAccount.ProviderInfo.ProviderType;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.nachuantech.opensync.view.CloudItemView;

public class CloudAdapter extends ArrayAdapter<ProviderType> {

	private List<ProviderType> types;

	public CloudAdapter(Context context, List<ProviderType> types) {
		super(context, 0);
		this.types = types;
	}

	@Override
	public ProviderType getItem(int position) {
		return types.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return types.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new CloudItemView(getContext(), getItem(position));
		} else {
			((CloudItemView) convertView).setCloud(getItem(position));
		}
		return convertView;
	}
}
