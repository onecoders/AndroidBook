package com.nachuantech.opensync.adapter;

import java.util.List;

import opensync.service.SyncService;
import opensync.syncPair.SyncPair;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.nachuantech.opensync.view.SyncPairItemView;

public class SyncPairItemAdapter extends ArrayAdapter<SyncPair> {

	private List<SyncPair> items;
	private SyncService service;

	public SyncPairItemAdapter(Context context, List<SyncPair> items,
			SyncService service) {
		super(context, 0, items);
		this.items = items;
		this.service = service;
	}

	@Override
	public SyncPair getItem(int position) {
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
			convertView = new SyncPairItemView(getContext(), getItem(position),
					service);
		} else {
			((SyncPairItemView) convertView).setSyncPair(getItem(position));
		}
		return convertView;
	}
}
