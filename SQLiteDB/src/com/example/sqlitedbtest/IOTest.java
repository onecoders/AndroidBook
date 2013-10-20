package com.example.sqlitedbtest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class IOTest extends Activity {

	private Button btn;
	private ListView listView;
	private String path = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/my.io";
	private List<News> newsList;
	private EditText title;
	private EditText content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		listView = (ListView) findViewById(R.id.show);
		title = (EditText) findViewById(R.id.title);
		content = (EditText) findViewById(R.id.content);

		inflateList();

		btn = (Button) findViewById(R.id.insert);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String titleStr = title.getText().toString();
				String contentStr = content.getText().toString();
				insertData(titleStr, contentStr);
				title.setText("");
				content.setText("");
				inflateList();
			}
		});
	}

	private void inflateList() {
		newsList = new ArrayList<News>();
		queryAll();
		inflateList(newsList);
	}

	private void queryAll() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		News news = null;
		try {
			fis = new FileInputStream(path);
			ois = new ObjectInputStream(fis);
			while ((news = (News) ois.readObject()) != null) {
				newsList.add(news);
			}
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void insertData(String title, String content) {
		newsList.add(new News(title, content));
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(path);
			oos = new ObjectOutputStream(fos);
			for (News news : newsList) {
				oos.writeObject(news);
				oos.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void inflateList(List<News> newsList) {
		ArrayAdapter<News> adapter = new NewsAdapter(this, newsList);
		listView.setAdapter(adapter);
	}

	class NewsAdapter extends ArrayAdapter<News> {

		List<News> list;

		public NewsAdapter(Context context, List<News> list) {
			super(context, 0);
			this.list = list;
		}

		@Override
		public News getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			News news = getItem(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.line, null);
			}
			((TextView) convertView.findViewById(R.id.title)).setText(news
					.getTitle());
			((TextView) convertView.findViewById(R.id.content)).setText(news
					.getContent());
			return convertView;
		}

	}

}
