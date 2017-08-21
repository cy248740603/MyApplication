package com.example.cy.myapplication.ServiceBestPractice;

/**
 * Created by CY on 2017/8/19.
 */

public interface DownloadListener {
    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onPaused();
    void onCanceled();
}
