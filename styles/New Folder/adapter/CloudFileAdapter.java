package com.nachuantech.opensync.adapter;

import java.util.List;

import opensync.cloud.CloudFile;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nachuantech.sync.R;
import com.nachuantech.opensync.activity.SyncConfigActivity;

public class CloudFileAdapter extends ArrayAdapter<CloudFile> {

	private Context mContext;

	private List<CloudFile> items;
	private String separator;

	public CloudFileAdapter(Context context, List<CloudFile> items,
			String separator) {
		super(context, 0, items);
		this.mContext = context;
		this.items = items;
		this.separator = separator;
	}

	@Override
	public CloudFile getItem(int position) {
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

	static class ViewHolder {
		ImageView item_image;
		TextView item_text;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.file_item, null);
			viewHolder = new ViewHolder();
			viewHolder.item_image = (ImageView) convertView
					.findViewById(R.id.item_image);
			viewHolder.item_text = (TextView) convertView
					.findViewById(R.id.item_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		CloudFile cloudFile = items.get(position);
		if (cloudFile != null) {
			if (cloudFile.isDir()) {
				if (((SyncConfigActivity) mContext)
						.isRemotePathSynced(cloudFile.getPath())) {
					viewHolder.item_image
							.setImageResource(R.drawable.synced_folder);
				} else {
					viewHolder.item_image
							.setImageResource(R.drawable.cloud_folder);
				}
			} else {
				viewHolder.item_image.setImageResource(R.drawable.file);
			}
			viewHolder.item_text.setText(cloudFile.getName(separator));
		}
		return convertView;
	}
}
