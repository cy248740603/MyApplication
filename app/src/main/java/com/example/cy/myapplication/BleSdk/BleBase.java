package com.example.cy.myapplication.BleSdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
public class BleBase extends BluetoothGattCallback{

    /**
     * 获取描述值的uuid  通用
     */
    public static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private static BleBase bleBase;
    private BluetoothGatt bluetoothGatt;
    private BluetoothAdapter adapter;
    private static Context context;
    private List<BluetoothDevice> devices = new ArrayList<>();
    private String[] strings = {""};
    private SearchBleDialog dialog = new SearchBleDialog();
    private BleStatusCallBack callBack;

    private BleBase(){
        adapter = BluetoothAdapter.getDefaultAdapter();
        openBle();
        dialog.adapter.setOnItemClickListener(new SearchBleDialog.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                bleConnect(devices.get(postion));
                dialog.dismiss();
            }
        });
//        getDevice();
//        bleConnect();
    }

    public void setCallBack(BleStatusCallBack callBack) {
        this.callBack = callBack;
    }

    public static BleBase getBle(Context c){
        context = c;
        if (bleBase == null)
            bleBase = new BleBase();
        return bleBase;
    }

    public void bleConnect(BluetoothDevice device){
        bluetoothGatt = device.connectGatt(context,true,this);
    }

    public void openBle(){
        if (!adapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(intent);
        }
    }

    public void disconnect(){
        if (bluetoothGatt != null)
            bluetoothGatt.disconnect();
    }

    public void showDialog(FragmentManager fragmentManager){
        dialog.show(fragmentManager,"ble");
    }

    public void getDevice(){
        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (BluetoothDevice.ACTION_FOUND.equals(action)){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getName() != null && !"".equals(device.getName())) {
                        for ( BluetoothDevice device1 : devices){
                            if (device1.getAddress().equals(device.getAddress())){
                                return;
                            }
                        }
                        devices.add(device);
                        dialog.setBleList(devices);
                        dialog.refresh();
                    }
//                    if ("D8:B0:4C:B2:C5:C7".equals(device.getAddress())) {
//                        bleConnect(device);
//                        Log.w("BLE", list.toString());
//                    }
                }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                    Log.w("BLEover",devices.toString());
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(receiver,filter);
        devices.clear();
        adapter.startDiscovery();
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        if (newState == BluetoothGatt.STATE_DISCONNECTED){
            if (gatt == null){
                return;
            }
            gatt.close();
        } else if (newState == BluetoothGatt.STATE_CONNECTED){
            gatt.discoverServices();
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        if (status == BluetoothGatt.GATT_SUCCESS){
            callBack.bleConnectCallBack();
            Log.w("ble","--onServicesDiscovered called--");
            for (int i = 0;i < gatt.getServices().size() ;i++){
                Log.w("ble",gatt.getServices().get(i).getUuid().toString());
                if ("6e400001-b5a3-f393-e0a9-e50e24dcca9e".equals(gatt.getServices().get(i).getUuid().toString())){
                    enableNotification();
                }
            }
        }else {
            Log.w("ble","onServicesDiscovered received: " + status);
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
        Log.w("ble","--onCharacteristicRead called--");
        if (status == BluetoothGatt.GATT_SUCCESS){
            byte[] sucString = characteristic.getValue();
            String string = new String(sucString);
            Log.w("ble",string);
            callBack.bleGetData(string);
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        Log.w("ble","--onCharacteristicWrite--: ");
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorRead(gatt, descriptor, status);

        Log.w("ble","--onDescriptorRead--: ");
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
        Log.w("ble","--onDescriptorWrite--: " + status);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        Log.w("ble","--onCharacteristicChanged--: ");
        // 从特征值获取数据
        final byte[] data = characteristic.getValue();
        // Ble_Activity.revDataForCharacteristic = data;
        Log.w("ble",data.toString());
    }

    private void enableNotification(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }//发送20字节后停止3毫秒

        BluetoothGattCharacteristic tx = bluetoothGatt.getService(UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e"))
                .getCharacteristic(UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e"));
        bluetoothGatt.setCharacteristicNotification(tx,true);
        BluetoothGattDescriptor descriptor = tx.getDescriptor(CCCD);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        boolean tag=bluetoothGatt.writeDescriptor(descriptor);
        if (!tag) {
            Log.e("ble","通知失败");
        }
    }

    private void  sendBuff(byte [] buff){
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }//发送20字节后停止3毫秒

        BluetoothGattCharacteristic tx = bluetoothGatt.getService(UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e"))
                .getCharacteristic(UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e"));
        tx.setValue(buff);
        boolean status = bluetoothGatt.writeCharacteristic(tx);
        if (!status) {
            Log.e("ble","写入特征值失败");
        }
    }

    public void sendData(byte[] bytes){
        byte[] buff = new byte[20];
        for (int i = 0;i < (bytes.length/20 + 1);i++){
            if(bytes.length - i*20 > 20) {
                System.arraycopy(bytes, i * 20, buff, 0, 20);
            }else {
                System.arraycopy(bytes, i * 20, buff, 0, bytes.length - i*20);
            }
            sendBuff(buff);
        }
    }
}
