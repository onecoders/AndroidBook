package com.nachuantech.opensync.view;

import java.util.List;

import opensync.cloudAccount.ProviderInfo.ProviderType;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nachuantech.sync.R;
import com.nachuantech.opensync.activity.SyncConfigActivity;
import com.nachuantech.opensync.adapter.CloudAdapter;

public class CloudView extends RelativeLayout implements OnItemClickListener {

	private Context mContext;
	private SyncConfigActivity activity;

	private ListView cloudListView;

	private List<ProviderType> types;

	public CloudView(Context context) {
		super(context);
		this.mContext = context;
		this.activity = (SyncConfigActivity) context;
		init();
	}

	private void init() {
		loadData();
		if (types.size() == 1) {
			activity.setCloud(types.get(0));
		} else {
			inflate(mContext, R.layout.fragment_cloud, this);
			findViews();
			initCloudListView();
			setListener();
		}
	}

	private void findViews() {
		cloudListView = (ListView) findViewById(R.id.cloud_list);
	}

	private void setListener() {
		cloudListView.setOnItemClickListener(this);
	}

	private void loadData() {
		types = ProviderType.getAllValidProviderTypes();
	}

	private void initCloudListView() {
		ArrayAdapter<ProviderType> adapter = new CloudAdapter(mContext, types);
		cloudListView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		activity.setCloud(types.get(position));
	}

}
