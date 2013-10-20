package com.example.sqlitedbtest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class DBTest extends Activity {

	private SQLiteDatabase db;
	private Button btn;
	private ListView listView;
	private String path = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/my.db3";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		db = SQLiteDatabase.openOrCreateDatabase(path, null);
		listView = (ListView) findViewById(R.id.show);

		inflateList();

		btn = (Button) findViewById(R.id.insert);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String title = ((EditText) findViewById(R.id.title)).getText()
						.toString();
				String content = ((EditText) findViewById(R.id.content))
						.getText().toString();
				insertData(title, content);
				inflateList();
			}
		});
	}

	private void inflateList() {
		try {
			Cursor cursor = db.rawQuery("select * from news_inf", null);
			inflateList(cursor);
		} catch (Exception e) {
			db.execSQL("create table news_inf(_id integer"
					+ " primary key autoincrement,"
					+ " news_title varchar(50),"
					+ " news_content varchar(255))");
			Cursor cursor = db.rawQuery("select * from news_inf", null);
			inflateList(cursor);
		}
	}

	private void insertData(String title, String content) {
		db.execSQL("insert into news_inf values(null, ?, ?)", new String[] {
				title, content });
	}

	private void inflateList(Cursor cursor) {
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.line, cursor, new String[] { "news_title",
						"news_content" },
				new int[] { R.id.title, R.id.content },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		listView.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		if (db != null && db.isOpen()) {
			db.close();
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dbtest, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.toIOSaving:
			startActivity(new Intent(this, IOTest.class));
			break;

		default:
			break;
		}
		return true;
	}

}
