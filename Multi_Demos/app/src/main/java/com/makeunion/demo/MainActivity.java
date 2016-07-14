package com.makeunion.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initFAB();
        initTabBar();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initTabBar() {
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setCustomView(getCustomView(1));

        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setCustomView(getCustomView(2));

        TabLayout.Tab tab3 = tabLayout.newTab();
        tab3.setCustomView(getCustomView(3));

        TabLayout.Tab tab4 = tabLayout.newTab();
        tab4.setCustomView(getCustomView(4));

        TabLayout.Tab tab5 = tabLayout.newTab();
        tab5.setCustomView(getCustomView(5));

        TabLayout.Tab tab6 = tabLayout.newTab();
        tab6.setCustomView(getCustomView(6));
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);
        tabLayout.addTab(tab4);
    }

    public void toolbarTest(View view) {
        startActivity(new Intent(this, ToolbarActivity.class));
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }

    public void animTest(View view) {
        startActivity(new Intent(this, BActivity.class));
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }

    public void curvedMotionTest(View view) {
        startActivity(new Intent(this, CurvedMotionActivity.class));
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }

    public void notificationTest(View view) {
        startActivity(new Intent(this, NotificationActivity.class));
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }

    public void circularRevealTest(View view) {
        startActivity(new Intent(this, CirActivity.class));
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }

    public void rippleTest(View view) {
        startActivity(new Intent(this, RipperActivity.class));
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }

    public void SVGTest(View view) {
        startActivity(new Intent(this, SVGActivity.class));
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }

    public void pathViewTest(View view) {
        startActivity(new Intent(this, PathViewActivity.class));
        overridePendingTransition(R.anim.anim_in_right, R.anim.anim_out_left);
    }

    private View getCustomView(int i) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_tab, null);
        ImageView iv = (ImageView)view.findViewById(R.id.iv);
        TextView tx = (TextView)view.findViewById(R.id.tx);
        switch (i) {
            case 1:
                iv.setImageResource(R.mipmap.ic_1);
                tx.setText("娱乐");
                break;
            case 2:
                iv.setImageResource(R.mipmap.ic_2);
                tx.setText("教育");
                break;
            case 3:
                iv.setImageResource(R.mipmap.ic_3);
                tx.setText("体育");
                break;
            case 4:
                iv.setImageResource(R.mipmap.ic_4);
                tx.setText("军事");
                break;
            case 5:
                iv.setImageResource(R.mipmap.ic_5);
                tx.setText("文娱");
                break;
            case 6:
                iv.setImageResource(R.mipmap.ic_6);
                tx.setText("赛事");
                break;
        }
        return view;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
