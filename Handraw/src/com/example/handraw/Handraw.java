package com.example.handraw;

import android.app.Activity;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Handraw extends Activity {

	EmbossMaskFilter emboss;
	BlurMaskFilter blur;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mian);

		emboss = new EmbossMaskFilter(new float[] { 1.5f, 1.5f, 1.5f }, 0.6f,
				6, 4.2f);
		blur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.handraw, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		DrawView dv = (DrawView) findViewById(R.id.drawview);
		switch (item.getItemId()) {
		case R.id.red:
			dv.paint.setColor(Color.RED);
			item.setChecked(true);
			break;
		case R.id.green:
			dv.paint.setColor(Color.GREEN);
			item.setChecked(true);
			break;
		case R.id.blue:
			dv.paint.setColor(Color.BLUE);
			item.setChecked(true);
			break;
		case R.id.width_1:
			dv.paint.setStrokeWidth(1);
			break;
		case R.id.width_3:
			dv.paint.setStrokeWidth(3);
			break;
		case R.id.width_5:
			dv.paint.setStrokeWidth(5);
			break;
		case R.id.blur:
			dv.paint.setMaskFilter(blur);
			break;
		case R.id.emboss:
			dv.paint.setMaskFilter(emboss);
			break;

		}
		return true;
	}

}
