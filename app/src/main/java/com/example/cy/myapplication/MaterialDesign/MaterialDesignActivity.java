package com.example.cy.myapplication.MaterialDesign;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.cy.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MaterialDesignActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;

    private Fruit[] fruits = {new Fruit("Apple",R.drawable.account),
            new Fruit("Banana",R.drawable.email),new Fruit("Orange",R.drawable.jixieqimo)};
    private List<Fruit> fruitList = new ArrayList<>();
    private FruitAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_design);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.jixieqimo);
        }
        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.
                OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"Data deleted",Snackbar.LENGTH_SHORT)
                        .setAction("Undo",new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MaterialDesignActivity.this,"Data restored",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
        initFruits();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.
                fruits_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FruitAdapter(this,fruitList);
        recyclerView.setAdapter(adapter);
    }
    private void initFruits(){
        fruitList.clear();
        for (int i = 0;i < 50;i++){
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }
}
