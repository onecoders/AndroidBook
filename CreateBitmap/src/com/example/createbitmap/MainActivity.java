package com.example.createbitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

public class MainActivity extends Activity {

	ImageView imgSource, imgTarget;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		imgSource = (ImageView) findViewById(R.id.imgsource);
		imgTarget = (ImageView) findViewById(R.id.imgtarget);

		// Load bitmap from internet
		String onLineImgSource = "http://goo.gl/yxNeG";
		URL urlImgSource;

		try {
			urlImgSource = new URL(onLineImgSource);
			new MyNetworkTask(imgSource, imgTarget).execute(urlImgSource);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	private class MyNetworkTask extends AsyncTask<URL, Void, Bitmap> {

		ImageView ivSource, ivTarget;

		public MyNetworkTask(ImageView iSource, ImageView iTarget) {
			ivSource = iSource;
			ivTarget = iTarget;
		}

		@Override
		protected Bitmap doInBackground(URL... urls) {
			Bitmap networkBitmap = null;
			
			String imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/create_bitmap/" + "Image01.jpg"; 

			if (new File(imagePath).exists()) {				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				networkBitmap = BitmapFactory.decodeFile(imagePath, options);
			} else {
				URL networkUrl = urls[0]; // Load the first element
				try {
					networkBitmap = BitmapFactory.decodeStream(networkUrl
							.openConnection().getInputStream());
					saveImage(networkBitmap);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			return networkBitmap;
		}

		private void saveImage(Bitmap bitmap) {
			String root = Environment.getExternalStorageDirectory().getAbsolutePath();
			File myDir = new File(root + "/create_bitmap");
			myDir.mkdirs();
			String fileName = "Image01.jpg";
			File file = new File(myDir, fileName);
			FileOutputStream out;
			try {
				out = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			ivSource.setImageBitmap(result);
			ivTarget.setImageBitmap(copyBitmap(result));
		}

	}

	private Bitmap copyBitmap(Bitmap src) {

		// Copy the whole bitmap
		// Bitmap newBitmap = Bitmap.createBitmap(src);

		// Copy the center part only
		int w = src.getWidth();
		int h = src.getHeight();
		Bitmap newBitmap = Bitmap.createBitmap(src, w / 4, h / 4, w / 2, h / 2);

		return newBitmap;
	}

}