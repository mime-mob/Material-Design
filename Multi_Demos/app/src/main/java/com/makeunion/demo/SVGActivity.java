package com.makeunion.demo;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SVGActivity extends Activity {

	private ImageView imgBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animate_vector_drawables);
		imgBtn = (ImageView)findViewById(R.id.imgBtn);
	}

	public void startAnim(View view) {
		Drawable drawable = imgBtn.getDrawable();
		((Animatable) drawable).start();
	}
}
