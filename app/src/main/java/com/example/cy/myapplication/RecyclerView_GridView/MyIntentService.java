package com.example.cy.myapplication.RecyclerView_GridView;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by CY on 2017/8/18.
 */

public class MyIntentService extends IntentService {
    public MyIntentService(){
        super("MyIntentService");//调用父类的有参构造
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        //打印当前线程的ID
        Log.e("MyIntentService","Thread id is"+Thread.currentThread().getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("MyIntentService","onDestroy executed");
    }
}
