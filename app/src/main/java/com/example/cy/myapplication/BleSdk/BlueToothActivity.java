package com.example.cy.myapplication.BleSdk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cy.myapplication.R;

/**
 * Created by CY on 2018/4/16 0016.
 * //                      _
 * //                     | |
 * //    _ __     ___     | |__    _   _    __ _
 * //   | '_ \   / _ \    | '_ \  | | | |  / _` |
 * //   | | | | | (_) |   | |_) | | |_| | | (_| |
 * //   |_| |_|  \___/    |_.__/   \__,_|  \__, |
 * //                                       __/ |
 * //                                      |___/
 */
public class BlueToothActivity extends AppCompatActivity implements BleStatusCallBack{

    byte[] sendBuff;
    Button close ;
    Button send;
    TextView bleData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);

        final BleBase ble = BleBase.getBle(this);
        ble.setCallBack(this);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ble.showDialog(getSupportFragmentManager());
                ble.getDevice();
            }
        });
        final EditText text = findViewById(R.id.device_id);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ble.sendData(PublicProtocol.packagePublicProtocol(PublicProtocol.setOpenDoorBean(text.getText().toString())));
            }
        });
        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ble.disconnect();
                send.setEnabled(false);
            }
        });

        bleData = findViewById(R.id.ble_data);
    }

    @Override
    public void bleConnectCallBack() {
        send.post(new Runnable() {
            @Override
            public void run() {
                send.setEnabled(true);
            }
        });
    }

    @Override
    public void bleGetData(String s) {
        bleData.setText(s + "\r\n" + bleData.getText().toString());
    }
}
