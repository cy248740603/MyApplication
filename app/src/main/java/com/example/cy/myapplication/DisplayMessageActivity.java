package com.example.cy.myapplication;


import android.Manifest;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cy.myapplication.ServiceBestPractice.DownloadService;



public class DisplayMessageActivity extends AppCompatActivity {

    private static final String TAG= "Database";
    private MyDatabaseHelper dbHelper;

    private MyService.DownloadBinder downloadBinder;
    private DownloadService.DownloadBinder downloadServiceBinder;

    private byte isBinded = 0;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder =(MyService.DownloadBinder) iBinder;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
            isBinded = 1;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private ServiceConnection downloadconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadServiceBinder = (DownloadService.DownloadBinder) iBinder;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new MyDatabaseHelper(this,"BookStore.db",null,4);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = (TextView)findViewById(R.id.activity_display_message_text);
//        textView.setTextSize(40);
        textView.setText("你好："+ message);

        final Button startService = (Button)findViewById(R.id.startService);
        final Button stopService = (Button)findViewById(R.id.stopService);

        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(DisplayMessageActivity.
                        this,MyService.class);
                startService(startIntent);//启动服务
            }
        });
        stopService.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent stopIntent = new Intent(DisplayMessageActivity.
                        this,MyService.class);
                stopService(stopIntent);//停止服务
            }
        });

        final Button bindService =(Button)findViewById(R.id.bind_service);
        final Button unbindService =(Button)findViewById(R.id.unbind_service);
        bindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bindIntent = new Intent(DisplayMessageActivity.this,MyService.class);
                bindService(bindIntent,connection,BIND_AUTO_CREATE);//绑定服务
            }
        });
        unbindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBinded != 0)
                    unbindService(connection);//解绑服务
            }
        });

        final Button startDownload = (Button) findViewById(R.id.startDownload);
        final Button pauseDownload = (Button) findViewById(R.id.pauseDownload);
        final Button cancleDownload = (Button) findViewById(R.id.cancelDownload);
        startDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (downloadServiceBinder == null){
                    return;
                }
                String url = "https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe";
                downloadServiceBinder.startDownload(url);
            }
        });
        pauseDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (downloadServiceBinder == null){
                    return;
                }
                downloadServiceBinder.pauseDownload();
            }
        });
        cancleDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (downloadServiceBinder == null){
                    return;
                }
                downloadServiceBinder.cancelDownload();
            }
        });
        Intent intent1 = new Intent(this,DownloadService.class);
        startService(intent1); //启动服务
        bindService(intent1,downloadconnection,BIND_AUTO_CREATE);//绑定服务
        if(ContextCompat.checkSelfPermission(DisplayMessageActivity.this,Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DisplayMessageActivity.this,new
                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        //timer.start();

//        ViewGroup layout = (ViewGroup)findViewById(R.id.activity_display_message);
//        layout.addView(textView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
            String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.
                        PERMISSION_GRANTED){
                    Toast.makeText(this,"拒绝权限将无法使用程序",Toast.LENGTH_SHORT).
                            show();
                    finish();
                }
                break;
            default:
                break;
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        if (isBinded != 0)
            unbindService(connection);
        super.onDestroy();
    }

    public void sendMessage(View view){
        dbHelper.getWritableDatabase();
        Intent intent = new Intent(this,Broadcast.class);
        startActivity(intent);
    }

    public void addData(View view){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始组装第一条数据
        values.put("name","The Da Vinci Code");
        values.put("author","Dan Brown");
        values.put("pages",454);
        values.put("price",16.96);
        db.insert("Book",null,values);//插入第一条数据
        values.clear();
        //?开始组装第二条数据
        values.put("name","The Lost Symbol");
        values.put("author","Dan Brown");
        values.put("pages",510);
        values.put("price",19.95);
        db.insert("Book",null,values);//插入第二条数据
        Log.e(TAG, "添加数据");
    }

    public void updataonClick(View view){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("price",10.99);
        db.update("Book",values,"name = ?",new String[]{ "The Da Vinci Code"});
        Log.e(TAG, "更新数据");
    }

    public void deleteDataonClick(View view){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Book","pages > ?",new String[]{"500"});
        Log.e(TAG, "删除数据");
    }

    public void queryDataonClick(View view){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Book",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                //遍历Cursor对象,去出数据打印
                String name = cursor.getString(cursor.getColumnIndex
                        ("name"));
                String author = cursor.getString(cursor.getColumnIndex
                        ("author"));
                int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                Log.e(TAG,"book name is "+ name);
                Log.e(TAG,"book author is "+ author);
                Log.e(TAG,"book pages is "+ pages);
                Log.e(TAG,"book price is "+ price);
            }while(cursor.moveToNext());
        }
    }

    public void gitCodeonClick (View view){
        CountDownTimerPro timer = new CountDownTimerPro(10000,1000,
                (Button)findViewById(R.id.gitcode_button));
        timer.start();
    }
}
