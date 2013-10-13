package com.nachuantech.opensync.adapter;

import java.io.File;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nachuantech.sync.R;
import com.nachuantech.opensync.activity.SyncConfigActivity;

public class FileAdapter extends ArrayAdapter<File> {

	private Context mContext;

	private File[] items;

	public FileAdapter(Context context, File[] items) {
		super(context, 0, items);
		this.mContext = context;
		this.items = items;
	}

	@Override
	public File getItem(int position) {
		return items[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return items.length;
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
		File file = items[position];
		if (file != null) {
			if (file.isDirectory()) {
				if (((SyncConfigActivity) mContext).isLocalPathSynced(file
						.getAbsolutePath())) {
					viewHolder.item_image
							.setImageResource(R.drawable.synced_folder);
				} else {
					viewHolder.item_image.setImageResource(R.drawable.folder);
				}
			} else {
				viewHolder.item_image.setImageResource(R.drawable.file);
			}
			viewHolder.item_text.setText(file.getName());
		}
		return convertView;
	}
}
