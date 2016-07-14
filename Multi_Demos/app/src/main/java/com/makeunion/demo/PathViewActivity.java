package com.makeunion.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.eftimoff.androipathview.PathView;

/**
 * Created by renjialiang on 2016/6/17.
 */
public class PathViewActivity extends AppCompatActivity {

    PathView pathView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pathview);

        pathView = (PathView) findViewById(R.id.pathView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pathView.getPathAnimator().
                delay(100).
                duration(1500).
                interpolator(new AccelerateDecelerateInterpolator()).
                start();
    }

    public void startPathView(View view) {
        pathView.getPathAnimator().
                delay(100).
                duration(1500).
                interpolator(new AccelerateDecelerateInterpolator()).
                start();
    }
}
