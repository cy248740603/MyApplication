package com.example.cy.myapplication.BleSdk;

/**
 * Created by CY on 2018/4/18 0018.
 * //                      _
 * //                     | |
 * //    _ __     ___     | |__    _   _    __ _
 * //   | '_ \   / _ \    | '_ \  | | | |  / _` |
 * //   | | | | | (_) |   | |_) | | |_| | | (_| |
 * //   |_| |_|  \___/    |_.__/   \__,_|  \__, |
 * //                                       __/ |
 * //                                      |___/
 */
public interface BleStatusCallBack {
    void bleConnectCallBack();
    void bleGetData(String s);
}
