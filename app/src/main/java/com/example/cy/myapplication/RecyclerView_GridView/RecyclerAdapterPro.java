package com.example.cy.myapplication.RecyclerView_GridView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cy.myapplication.Book;
import com.example.cy.myapplication.R;

import java.util.List;


/**
 * Created by CY on 2017/8/15.
 */

public class RecyclerAdapterPro extends RecyclerView.Adapter<RecyclerAdapterPro.ViewHolder>{
    private List<Book> mBookList;

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

    public RecyclerAdapterPro(List<Book> bookList){
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
