package com.example.phototest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.widget.ImageView;

public class MainActivity extends Activity {
	ImageView imgSource, imgTarget;
	private static final String IMAGE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/images.jpeg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		imgSource = (ImageView) findViewById(R.id.imgsource);
		imgTarget = (ImageView) findViewById(R.id.imgtarget);

		// Load bitmap from internet
		String onLineImgSource = "http://www.xanthir.com/etc/exif/exif-rotated.jpg";
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

			if (new File(IMAGE_PATH).exists()) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				networkBitmap = BitmapFactory.decodeFile(IMAGE_PATH, options);
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
			File file = new File(IMAGE_PATH);
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
			ivTarget.setImageBitmap(decodeAndRotate(IMAGE_PATH));
		}

	}

	private Bitmap decodeAndRotate(String photoPaht) {
		Bitmap bmp = BitmapFactory.decodeFile(photoPaht);
		getContentResolver().notifyChange(Uri.parse("file://" + IMAGE_PATH),
				null);
		int rotate = 0;
		try {
			ExifInterface exif = new ExifInterface(photoPaht);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Matrix matrix = new Matrix();
		matrix.postRotate(rotate);
		Bitmap rotateBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
				bmp.getHeight(), matrix, true);
		return rotateBitmap;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
