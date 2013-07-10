package com.example.photoorientation;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private Button btn;
	private ImageView iv;
	private static final String APP_DIR = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	private static final int CAPTURE_PHOTO_CONSTANT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btn = (Button) findViewById(R.id.capture);
		iv = (ImageView) findViewById(R.id.dispaly);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				i.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(APP_DIR + "/latest.png")));
				startActivityForResult(i, CAPTURE_PHOTO_CONSTANT);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_PHOTO_CONSTANT) {
			Bitmap bmp = BitmapFactory.decodeFile(APP_DIR + "/latest.png");
			int rotate = 0;
			try {
				String imagePath = APP_DIR + "/latest.png";
				File imageFile = new File(imagePath);
				Uri imageUri = Uri.fromFile(imageFile);
				getContentResolver().notifyChange(imageUri, null);
				ExifInterface exif = new ExifInterface(
						imageFile.getAbsolutePath());
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
			Bitmap rotateBitmap = Bitmap.createBitmap(bmp, 0, 0,
					bmp.getWidth(), bmp.getHeight(), matrix, true);
			iv.setImageBitmap(rotateBitmap);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
