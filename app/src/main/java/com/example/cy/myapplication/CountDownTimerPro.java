package com.example.cy.myapplication;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by CY on 2017/8/11.
 */

public class CountDownTimerPro extends CountDownTimer{
    private  Button tv;
    public  CountDownTimerPro(long millisInFuture, long countDownInerval,Button button){
        super(millisInFuture,countDownInerval);
        this.tv = button;
        tv.setEnabled(false);
    }
    public void onTick(long l) {
        tv.setText((l/1000) + "秒后可重发");
    }

    @Override
    public void onFinish() {
        tv.setEnabled(true);
        tv.setText("获取验证码");
    }
}
