package com.example.cy.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by CY on 2017/8/8.
 */

public class NetWorkReceiver  extends BroadcastReceiver {

    private Context context;
    private static final String TAG= "NetWork";
    public void onReceive(Context context, Intent intent){

        this.context = context;
            if (isNetworkConnected(context)) {
                //Toast.makeText(Broadcast.this,"发送测试的广播",Toast.LENGTH_LONG).show();
                Log.e(NetWorkReceiver.TAG, "网络已连接");
                Toast.makeText(context,"网络已连接",Toast.LENGTH_LONG).show();
                if (isWifiConnceted(context)) {
                       Log.e(NetWorkReceiver.TAG, "当前为wifi连接");
                    Toast.makeText(context,"当前为wifi连接",Toast.LENGTH_LONG).show();
                } else {
                    String c = isFastMobileNetwork(context);
                    Log.e(NetWorkReceiver.TAG, "当前网络为" + c);
                    Toast.makeText(context,"当前网络为" + c,Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(NetWorkReceiver.TAG, "网络无连接");
                Toast.makeText(context,"网络无连接",Toast.LENGTH_LONG).show();
            }
    }
//是否网络连接
    public boolean isNetworkConnected(Context context){
        if(context != null){
            ConnectivityManager mConnectivityManager =
                    (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()){
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
//是否wifi连接
    public boolean isWifiConnceted(Context context) {
        if(context != null){
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifiNetworkInfo = mConnectivityManager.
                    getActiveNetworkInfo();
            if (mWifiNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                return mWifiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static String isFastMobileNetwork(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.
                getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()){
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case 17://TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
            case 18: // TelephonyManager.NETWORK_TYPE_IWLAN:
                return "4G";
            default:
                return "NETWORK_CLASS_UNKNOWN";
        }
    }
}
