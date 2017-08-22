package com.example.cy.myapplication.MaterialDesign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cy.myapplication.Book;
import com.example.cy.myapplication.R;

import java.util.List;


/**
 * Created by CY on 2017/8/15.
 */

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder>{
    private List<Fruit> mFruitList;
    private Context mContext;
    public final static String EXTRA_MESSAGE =
            "com.example.cy.myapplication.MaterialDesign.MESSAGE";
    static class ViewHolder extends RecyclerView.ViewHolder{
        View cardView;
        ImageView fruitImage;
        TextView fruitName;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            fruitName = (TextView) view.findViewById(R.id.fruit_name);
        }
    }

    public FruitAdapter(Context context, List<Fruit> fruitList){
        mContext = context;
        mFruitList = fruitList;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fruit_item,
                parent,false);

        //ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);
    }
    public void onBindViewHolder(ViewHolder holder,int position){
        Fruit fruit = mFruitList.get(position);
        holder.fruitName.setText(fruit.getName());
        Glide.with(mContext).load(fruit.getImageId()).into(holder.fruitImage);
    }
    public int getItemCount(){
        return mFruitList.size();
    }
}
