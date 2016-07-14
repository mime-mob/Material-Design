package com.makeunion.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.eftimoff.androipathview.PathView;

/**
 * Created by renjialiang on 2016/6/17.
 */
public class ToolbarActivity extends AppCompatActivity {

    PathView pathView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_toolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_back);//设置导航栏图标
        toolbar.setLogo(R.mipmap.ic_launcher);//设置app logo
        toolbar.setTitle("Title");//设置主标题
        toolbar.setSubtitle("Subtitle");//设置子标题
        toolbar.setTitleTextColor(Color.rgb(250, 250, 250));//设置标题颜色
        toolbar.setSubtitleTextColor(Color.rgb(250, 250, 250));//设置子标题颜色
        toolbar.inflateMenu(R.menu.menu_toolbar);//设置右上角的填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return true;
            }
        });
    }
}
