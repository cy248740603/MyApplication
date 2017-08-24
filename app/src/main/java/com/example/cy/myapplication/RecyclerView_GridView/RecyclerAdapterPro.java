package com.example.cy.myapplication.RecyclerView_GridView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cy.myapplication.Book;
import com.example.cy.myapplication.Fragment.FragmentActivity;
import com.example.cy.myapplication.MaterialDesign.MaterialDesignActivity;
import com.example.cy.myapplication.R;

import java.util.List;


/**
 * Created by CY on 2017/8/15.
 */

public class RecyclerAdapterPro extends RecyclerView.Adapter<RecyclerAdapterPro.ViewHolder>{
    private List<Book> mBookList;
    private Activity maActivity;
    public final static String EXTRA_MESSAGE = "com.example.cy.myapplication.RecyclerView.MESSAGE";
    static class ViewHolder extends RecyclerView.ViewHolder{
        View bookView;
        ImageView bookImage;
        TextView bookName;

        public ViewHolder(View view){
            super(view);
            bookView = view;
            bookImage = (ImageView) view.findViewById(R.id.item_image);
            bookName = (TextView) view.findViewById(R.id.item_text);
        }
    }

    public RecyclerAdapterPro(Activity activity, List<Book> bookList){
        maActivity = activity;
        mBookList = bookList;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.griditem,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.bookView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                Book book = mBookList.get(position);
                Toast.makeText(v.getContext(),"you clicked view" + book.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        holder.bookImage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                Book book = mBookList.get(position);
                Toast.makeText(v.getContext(),"you clicked image" + book.getName(),
                        Toast.LENGTH_SHORT).show();
                if (book.getName().equals("编码")){
                    Intent mainIntent = new Intent(maActivity,
                            MaterialDesignActivity.class);
                    maActivity.startActivity(mainIntent);
                }else if (book.getName().equals("审批")){
                    Intent mainIntent = new Intent(maActivity,
                            FragmentActivity.class);
                    maActivity.startActivity(mainIntent);
                }
            }
        });
        holder.bookName.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                Book book = mBookList.get(position);
                Toast.makeText(v.getContext(),"you clicked name" + book.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        //ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    public void onBindViewHolder(ViewHolder holder,int position){
        Book book = mBookList.get(position);
        holder.bookImage.setImageResource(book.getImageId());
        holder.bookName.setText(book.getName());
    }
    public int getItemCount(){
        return mBookList.size();
    }
}
