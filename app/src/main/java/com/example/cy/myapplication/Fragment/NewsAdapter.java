package com.example.cy.myapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cy.myapplication.MaterialDesign.Fruit;
import com.example.cy.myapplication.MaterialDesign.FruitActivity;
import com.example.cy.myapplication.R;

import java.util.List;


/**
 * Created by CY on 2017/8/15.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private List<News> mNewsList;
    private Context mContext;
    private NewsTitleFragment mnewsTitleFragment;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView newsTitleText;
        public ViewHolder(View view){
            super(view);
            newsTitleText = (TextView)view.findViewById(R.id.news_title_item);
        }
    }

    public NewsAdapter(Context context, List<News> newsList,NewsTitleFragment newsTitleFragment){
        mContext = context;
        mNewsList = newsList;
        mnewsTitleFragment = newsTitleFragment;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_item,
                parent,false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                News news = mNewsList.get(holder.getAdapterPosition());
                if (mnewsTitleFragment.isTwoPane){
                    //如果是双页模式,则刷新NewsContentFragment中的内容
                    NewsContentFragment newsContentFragment =
                            (NewsContentFragment)mnewsTitleFragment.getFragmentManager()
                            .findFragmentById(R.id.news_content_fragment);
                    newsContentFragment.refresh(news.getTitle(),news.getContent());
                }else {
                    //如果是单页模式,则直接启动NewsContentActivity
                    NewsContentActivity.actionStart(mContext,news.getTitle(),news.getContent());
                }
            }
        });
        //ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    public void onBindViewHolder(ViewHolder holder,int position){
        News news = mNewsList.get(position);
        holder.newsTitleText.setText(news.getTitle());
    }
    public int getItemCount(){
        return mNewsList.size();
    }
}
