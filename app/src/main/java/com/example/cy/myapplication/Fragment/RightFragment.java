package com.example.cy.myapplication.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cy.myapplication.R;

/**
 * Created by CY on 2017/8/23.
 */

public class RightFragment extends Fragment{
    public static final String FRAGTAG = "RightFragment";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(FRAGTAG,"onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(FRAGTAG,"onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.e(FRAGTAG,"onCreateView");
        View view = inflater.inflate(R.layout.right_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(FRAGTAG,"onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(FRAGTAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(FRAGTAG,"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(FRAGTAG,"onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(FRAGTAG,"onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(FRAGTAG,"onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(FRAGTAG,"onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(FRAGTAG,"onDetach");
    }
}
