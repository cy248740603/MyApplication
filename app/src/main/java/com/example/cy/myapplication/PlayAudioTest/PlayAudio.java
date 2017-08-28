package com.example.cy.myapplication.PlayAudioTest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.cy.myapplication.R;

import java.io.File;

public class PlayAudio extends AppCompatActivity implements View.OnClickListener{

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);
        Button play = (Button)findViewById(R.id.media_play);
        Button pause = (Button)findViewById(R.id.media_pause);
        Button stop = (Button)findViewById(R.id.media_stop);
        Button vplay = (Button)findViewById(R.id.video_play);
        Button vpause = (Button)findViewById(R.id.video_pause);
        Button vstop = (Button)findViewById(R.id.video_replay);
        videoView = (VideoView)findViewById(R.id.video_view);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        vplay.setOnClickListener(this);
        vpause.setOnClickListener(this);
        vstop.setOnClickListener(this);
        if(ContextCompat.checkSelfPermission(PlayAudio.this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PlayAudio.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
        }else {
            initMediaPlayer(); //初始化MediaPlayer
            initVideoPath();//初始化
        }
    }
    private void initVideoPath(){
        File file = new File(Environment.getExternalStorageDirectory(),
                "movie.mp4");
        videoView.setVideoPath(file.getPath());//指定视频文件的路径
    }
    private void initMediaPlayer(){
        try{
            File file = new File(Environment.getExternalStorageDirectory(),
                    "music.mp3");
            mediaPlayer.setDataSource(file.getPath()); //指定音频文件的路径
            mediaPlayer.prepare(); //让MediaPlayer 进入到准备状态
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager
                        .PERMISSION_GRANTED){
                    initMediaPlayer();
                }else {
                    Toast.makeText(this,"拒绝权限将无法使用程序",
                    Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.media_play:
                if (!mediaPlayer.isPlaying()){
                    mediaPlayer.start(); //开始播放
                }
                break;
            case R.id.media_pause:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();//暂停播放
                }
                break;
            case R.id.media_stop:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.reset();//停止播放
                    initMediaPlayer();
                }
                break;
            case R.id.video_play:
                if (!videoView.isPlaying()){
                    videoView.start();//开始播放
                }
                break;
            case R.id.video_pause:
                if (videoView.isPlaying()){
                    videoView.pause();//暂停播放
                }
                break;
            case R.id.video_replay:
                if (videoView.isPlaying()){
                    videoView.resume();//重新播放
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (videoView != null){
            videoView.suspend();
        }
    }
    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }
}
