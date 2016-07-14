package com.makeunion.demo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RemoteViews;

/**
 * Created by renjialiang on 2016/6/12.
 */
public class NotificationActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notification);
    }

    public void showNormal(View view) {
        Notification.Builder builder = new Notification.Builder(this);
        Intent[] intents = {new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"))};
        //构造pendingdintent
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, intents, 0);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentText("Title");
        builder.setContentText("内容");
        builder.setSubText("text");

        //通过NotificationManager来发出
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    public void showCustom(View view) {
        Notification.Builder builder = new Notification.Builder(this);
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.csdn.net/itachi85/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0);

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_head);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setAutoCancel(true);
        builder.setContentTitle("折叠式通知");
        selectNotofovatiomLevel(builder, Notification.VISIBILITY_PRIVATE);
        //用RemoteViews来创建自定义Notification视图
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.view_fold);
        Notification notification = builder.build();
        //指定展开时的视图
        notification.bigContentView = remoteViews;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    public void showHeadsup(View view) {
        Notification.Builder builder = new Notification.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setPriority(Notification.PRIORITY_DEFAULT).setCategory(Notification.CATEGORY_MESSAGE).setContentTitle("Headsup Notification").setContentText("I am Headsup Notification");
        Intent push = new Intent();
        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        push.setClass(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0, new Intent[]{push}, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentText("Android 5.X Headsup Notification").setFullScreenIntent(pendingIntent,true);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0,builder.build());
    }

    private void selectNotofovatiomLevel(Notification.Builder builder, int level) {
        switch (level) {
            case Notification.VISIBILITY_PUBLIC:
                builder.setVisibility(Notification.VISIBILITY_PUBLIC);
                builder.setContentText("public");
                break;
            case Notification.VISIBILITY_PRIVATE:
                builder.setVisibility(Notification.VISIBILITY_PRIVATE);
                builder.setContentText("private");
                break;
            case Notification.VISIBILITY_SECRET:
                builder.setVisibility(Notification.VISIBILITY_SECRET);
                builder.setContentText("secret");
                break;
            default:
                builder.setVisibility(Notification.VISIBILITY_PUBLIC);
                builder.setContentText("public");
                break;
        }
    }
}