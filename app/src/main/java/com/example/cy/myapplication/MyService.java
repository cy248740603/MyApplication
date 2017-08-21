package com.example.cy.myapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {

    private DownloadBinder mBinder = new DownloadBinder();

    class DownloadBinder extends Binder {
        public void startDownload(){
            Log.e("MyService","startDownload executed");
        }
        public int getProgress(){
            Log.e("MyService","getProgress executed");
            return 0;
        }
    }
    public MyService() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("MyService","onCreate executed");
        Intent intent = new Intent(this,DisplayMessageActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("手机已启动自爆装置")
                .setContentText("非战斗人员请立即撤离")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .build();
        startForeground(1,notification);
    }
    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        Log.e("MyService","onStartCommand executed");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //处理集体的逻辑
                stopSelf();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("MyService","onDestroy executed");
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }
}
