package com.example.fragmenttest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.example.fragmenttest.BookListFragment.Callbacks;

public class SelectBookActivity extends Activity implements Callbacks {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_twopane);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_book, menu);
		return true;
	}

	@Override
	public void onItemSelected(Integer id) {
		Bundle arguments = new Bundle();
		arguments.putInt(BookDetailFragment.ITEM_ID, id);
		BookDetailFragment fragment = new BookDetailFragment();
		fragment.setArguments(arguments);
		getFragmentManager().beginTransaction()
				.replace(R.id.book_detail_container, fragment).commit();
	}

}
