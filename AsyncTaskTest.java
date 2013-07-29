package com.example.asynctasktest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AsyncTaskTest extends Activity {

	private TextView showResult;
	private Button loadData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		showResult = (TextView) findViewById(R.id.showResult);
		loadData = (Button) findViewById(R.id.loadData);

		loadData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DownLoadTask task = new DownLoadTask(AsyncTaskTest.this);
				try {
					task.execute(new URL("http://www.crazyit.org/ethos.php"));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		});

	}

	class DownLoadTask extends AsyncTask<URL, Integer, String> {

		ProgressDialog pdialog;
		int hasRead;
		Context mContext;

		public DownLoadTask(Context ctx) {
			this.mContext = ctx;
		}

		@Override
		protected String doInBackground(URL... params) {
			StringBuilder sb = new StringBuilder();
			try {
				URLConnection conn = params[0].openConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
					hasRead++;
					publishProgress(hasRead);
				}
				return sb.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			showResult.setText(result);
			pdialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			pdialog = new ProgressDialog(mContext);
			pdialog.setTitle("Task doing...");
			pdialog.setMessage("Task executing...Please wait...");
			pdialog.setCancelable(false);
			pdialog.setMax(202);
			pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pdialog.setIndeterminate(false);
			pdialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			showResult.setText("Have read " + values[0] + "ÐÐ");
			pdialog.setProgress(values[0]);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.async_task_test, menu);
		return true;
	}

}
