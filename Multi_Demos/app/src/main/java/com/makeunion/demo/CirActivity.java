package com.makeunion.demo;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;

/**
 * Circular Reveal
 * Created by LGL on 2016/5/2.
 */
public class CirActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView oval;

    ImageView rect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cir);

        oval = (ImageView) findViewById(R.id.oval);
        rect =  (ImageView) findViewById(R.id.rect);
        oval.setOnClickListener(this);
        rect.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.oval:
                Animator animator1 = ViewAnimationUtils.createCircularReveal(oval, oval.getWidth() / 2, oval.getHeight() / 2, oval.getWidth(), 0);
                animator1.setDuration(2000);
                animator1.start();
                break;
            case R.id.rect:
                Animator animator2 = ViewAnimationUtils.createCircularReveal(rect, 0, 0, 0, (float) Math.hypot(rect.getWidth(), rect.getHeight()));
                animator2.setDuration(2000);
                animator2.start();
                break;
        }
    }
}
