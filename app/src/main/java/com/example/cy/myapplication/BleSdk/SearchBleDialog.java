package com.example.cy.myapplication.BleSdk;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.cy.myapplication.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by CY on 2018/4/17 0017.
 * //                      _
 * //                     | |
 * //    _ __     ___     | |__    _   _    __ _
 * //   | '_ \   / _ \    | '_ \  | | | |  / _` |
 * //   | | | | | (_) |   | |_) | | |_| | | (_| |
 * //   |_| |_|  \___/    |_.__/   \__,_|  \__, |
 * //                                       __/ |
 * //                                      |___/
 */
public class SearchBleDialog extends DialogFragment {

    private List<BluetoothDevice> list = new ArrayList<>();
    public BleRecyclerViewAdapter adapter = new BleRecyclerViewAdapter();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 去掉留白的标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_ble,container);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return  view;
    }

    class BleRecyclerViewAdapter extends RecyclerView.Adapter<BleRecyclerViewAdapter.ViewHolder>{

        private OnItemClickListener  mClickListener;

        public void setOnItemClickListener(OnItemClickListener  listener) {
            this.mClickListener = listener;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_item,parent,false);
            final ViewHolder holder = new ViewHolder(view,mClickListener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.ble.setText((list.get(position).getName() + "\r\n" + list.get(position).getAddress()));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView ble;
            OnItemClickListener mListener;
            public ViewHolder(View itemView, OnItemClickListener itemClickListener) {
                super(itemView);
                mListener = itemClickListener;
                ble = itemView.findViewById(R.id.news_title_item);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, getAdapterPosition());
            }

        }

    }

    public void refresh(){
        adapter.notifyDataSetChanged();
    }
    public List<BluetoothDevice> getBleList() {
        return list;
    }

    public void setBleList(List<BluetoothDevice> bleList) {
        this.list = bleList;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int postion);
    }
}
