package com.example.cy.myapplication.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cy.myapplication.R;

public class FragmentActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
//        Button button = (Button)findViewById(R.id.button_fragment);
//        button.setOnClickListener(this);
       // replaceFragment(new RightFragment());
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_fragment:
                //replaceFragment(new Right1Fragment());
                break;
            default:
                break;
        }
    }
//    private void replaceFragment(Fragment fragment){
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.replace(R.id.right_layout,fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

}
