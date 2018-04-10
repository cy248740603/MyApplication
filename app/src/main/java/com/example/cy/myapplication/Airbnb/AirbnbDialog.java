package com.example.cy.myapplication.Airbnb;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.cy.myapplication.R;

/**
 * Created by CY on 2018/4/9 0009.
 * //                      _
 * //                     | |
 * //    _ __     ___     | |__    _   _    __ _
 * //   | '_ \   / _ \    | '_ \  | | | |  / _` |
 * //   | | | | | (_) |   | |_) | | |_| | | (_| |
 * //   |_| |_|  \___/    |_.__/   \__,_|  \__, |
 * //                                       __/ |
 * //                                      |___/
 */
public class AirbnbDialog extends android.app.DialogFragment {
    private ValueAnimator mAnimator;
    private int repeatCount = 0;

    public AirbnbDialog(){
        mAnimator = ValueAnimator.ofFloat(0f,0.5f).setDuration(1000);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog,container);
        final LottieAnimationView animationView = v.findViewById(R.id.lav);

//        animationView.playAnimation();//播放动画
//        animationView.setProgress(0.5f);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animationView.setProgress((float)animation.getAnimatedValue());
            }
        });
        return v;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        //隐藏dialog边上的灰色
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0f;
        window.setAttributes(layoutParams);
        mAnimator.start();
    }

    public void setWait(float start,float end,long time,int count){
        repeatCount = count;
        mAnimator = ValueAnimator.ofFloat(start,end).setDuration(time);
        mAnimator.setRepeatCount(repeatCount);
    }
}
