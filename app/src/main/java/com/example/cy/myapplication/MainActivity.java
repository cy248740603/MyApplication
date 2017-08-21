package com.example.cy.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.cy.myapplication.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象 
        SharedPreferences sharedPreferences= getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String name =sharedPreferences.getString("name", "");
        String check =sharedPreferences.getString("check", "");
        if (check.equals("true")){
                /* Create an Intent that will start the Main WordPress Activity. */
            Intent mainIntent = new Intent(MainActivity.this, DisplayMessageActivity.class);
            mainIntent.putExtra(EXTRA_MESSAGE,name);
            MainActivity.this.startActivity(mainIntent);
            MainActivity.this.finish();
        }
        CheckBox cbx = (CheckBox) findViewById(R.id.checkbox);
        cbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //do xxx
            }
        });
    }
    public void sendMessage(View view){
        Intent intent = new Intent(this,DisplayMessageActivity.class);
        EditText editText = (EditText)findViewById(R.id.edit_Text);
        String message = editText.getText().toString();
        CheckBox cbx = (CheckBox) findViewById(R.id.checkbox);
        if (cbx.isChecked()) {
            //实例化SharedPreferences对象（第一步）
            SharedPreferences mySharedPreferences = getSharedPreferences("test",Activity.MODE_PRIVATE);
            //实例化SharedPreferences.Editor对象（第二步）
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            //用putString的方法保存数据
            editor.putString("name", message);
            editor.putString("check","true");
            //提交当前数据
            editor.commit();
        }
        intent.putExtra(EXTRA_MESSAGE,message);
        startActivity(intent);
    }
}
