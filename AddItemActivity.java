package com.example.dynamicallyadditem;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class AddItemActivity extends Activity {

	private Button addItem;
	private ListView listView;
	private static int LENGTH = 10;
	final List<String> listItems = new ArrayList<String>();
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		listView = (ListView) findViewById(R.id.showItems);//
		addItem = (Button) findViewById(R.id.addItem);//

		for (int i = 0; i < LENGTH; i++) {
			listItems.add("Item #" + i);
		}
		adapter = new ArrayAdapter<String>(getBaseContext(),
				android.R.layout.simple_list_item_1, listItems);
		listView.setAdapter(adapter);

		addItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listItems.add("Item #" + LENGTH++);
				adapter.notifyDataSetChanged();// key code
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
