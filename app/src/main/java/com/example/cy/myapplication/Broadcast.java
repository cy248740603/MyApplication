package com.example.cy.myapplication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cy.myapplication.RecyclerView_GridView.GirdViewActivity;

import java.util.ArrayList;
import java.util.List;

public class Broadcast extends AppCompatActivity {
    private final String ACTION_NAME = "发送广播";
    public final static String EXTRA_MESSAGE1 = "com.example.cy.myapplication.MESSAGE1";
    private Button mBtnMsgEvent = null;

    ArrayAdapter<String> adapter;
    List<String> contactsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        //注册广播
//        registerBoradcastReceiver();

//        LinearLayout broadcastLinearLayout = new LinearLayout(this);
//        mBtnMsgEvent = new Button(this);
//        mBtnMsgEvent.setText("SendBroadcast");
//        broadcastLinearLayout.addView(mBtnMsgEvent);
//        setContentView(broadcastLinearLayout);

//        mBtnMsgEvent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent mIntent = new Intent(ACTION_NAME);
//                mIntent.putExtra(EXTRA_MESSAGE1,"SendBroadcast");
//                //发送广播
//                sendBroadcast(mIntent);
//            }
//        });
        ListView contactsView = (ListView) findViewById(R.id.contacts_view);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,contactsList);
        contactsView.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.READ_CONTACTS},1);
        }else {
            readContacts();
        }
    }

    private void readContacts(){
        Cursor cursor = null;
        try {
            //查询联系人数据
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.
                    Phone.CONTENT_URI,null,null,null,null);
            if (cursor != null){
                while (cursor.moveToNext()){
                    //获取联系人姓名
                    String displayName = cursor.getString(cursor.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    //获取联系人手机号
                    String number = cursor.getString(cursor.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactsList.add(displayName + "\n" + number);
                }
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
        int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED){
                    readContacts();
                }else{
                    Toast.makeText(this,"You denied the permission",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private BroadcastReceiver netBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_NAME)){
                Toast.makeText(Broadcast.this,"发送测试的广播",Toast.LENGTH_LONG).show();
            }
        }
    };

    public void registerBoradcastReceiver(){
        NetWorkReceiver nwr = new NetWorkReceiver();//注册
        IntentFilter registerIntentFilter = new IntentFilter();
        registerIntentFilter.addAction(ACTION_NAME);
        //注册广播
        registerReceiver(netBroadcastReceiver,registerIntentFilter);
    }

    public void getDatabaseLitePalOnClick(View view){
        Toast.makeText(Broadcast.this,"xxxxxx",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,GirdViewActivity.class);
        startActivity(intent);
        //Connector.getDatabase();
    }
}
