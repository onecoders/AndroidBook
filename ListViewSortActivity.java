package com.example.listviewsort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ListViewSortActivity extends Activity {

	private Button sort;
	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		sort = (Button) findViewById(R.id.sort);
		listview = (ListView) findViewById(R.id.showItems);

		final List<String> items = new ArrayList<String>();
		items.add("ab");
		items.add("cd");
		items.add("bc");
		items.add("df");
		items.add("dd");
		items.add("ef");
		items.add("de");
		items.add("as");
		items.add("re");
		items.add("fd");
		items.add("ff");
		items.add("hg");
		items.add("jk");
		items.add("kl");

		final ArrayAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, items);

		listview.setAdapter(adapter);
		sort.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Collections.sort(items, new Comparator() {

					@Override
					public int compare(Object lhs, Object rhs) {
						String str1 = (String) lhs;
						String str2 = (String) rhs;
						return str1.compareToIgnoreCase(str2);
					}
				});
				adapter.notifyDataSetChanged();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_view_sort, menu);
		return true;
	}

}
