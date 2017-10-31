package com.example.cy.myapplication.RecyclerView_GridView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.cy.myapplication.Book;
import com.example.cy.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GirdViewActivity extends AppCompatActivity {
    private GridView gridView;
    private List<Map<String,Object>> data_list;
    private SimpleAdapter simpleAdapter;
    //图片封装为一个数组
    private int[] icon = {R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
    private String[] iconName = {"编码","审批","钥匙","日志","维修","故障申报","统计","工程",
    "足迹","光交箱","工程勘测","任务"};
    private  List<Book> bookList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gird_view);

        initBooks();//初始化水果数据
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,GridLayoutManager.VERTICAL);
        layoutManager.setSpanCount(4);
//        StaggeredGridLayoutManager layoutManager= new
//                StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);//瀑布流
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerAdapterPro adapter = new RecyclerAdapterPro(this,bookList);
        recyclerView.setAdapter(adapter);

        gridView = (GridView) findViewById(R.id.gridView);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String[] from = {"image","text"};
        int [] to = {R.id.item_image,R.id.item_text};
        simpleAdapter = new SimpleAdapter(this,data_list,R.layout.griditem,from,to);
        //配置适配器
        gridView.setAdapter(simpleAdapter);
    }

    private void initBooks(){
        for(int i=0;i<iconName.length;i++){
            Book book = new Book(iconName[i],icon[i]);
            bookList.add(book);
        }
    }
    private String getRandomLengthName(String name){
        Random random = new Random();
        int length = random.nextInt(20)+1;
        StringBuffer builder = new StringBuffer();
        for (int i=0;i<length;i++){
            builder.append(name);
        }
        return builder.toString();
    }

    public List<Map<String,Object>>getData(){
        //icon和iconName的长度是相同的,
        for (int i=0;i<icon.length;i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("image",icon[i]);
            map.put("text",iconName[i]);
            data_list.add(map);
        }
        return data_list;
    }
}
