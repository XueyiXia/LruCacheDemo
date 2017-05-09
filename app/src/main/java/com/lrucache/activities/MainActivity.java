package com.lrucache.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.lrucache.R;
import com.lrucache.adapter.GridViewImageAdapter;
import com.lrucache.bean.ImageBean;
import com.lrucache.utils.FileUtils;
import com.lrucache.utils.ImageUrlUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private GridView mGridView;

    private Button mRefresh;

    private Button mDelete;

    private GridViewImageAdapter mGridViewImageAdapter=null;

    private ArrayList<Object> dataList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        initWidget();

        initAdapter();
    }


    private void initData(){
        String image[]= ImageUrlUtils.image;
        if(image!=null&&image.length>0){
            for (String url:image){
                ImageBean bean=new ImageBean();
                bean.setUrl(url);
                dataList.add(bean);
            }

        }
    }

    private void initWidget(){
        mGridView=(GridView)super.findViewById(R.id.grid_view);
        mRefresh=(Button)super.findViewById(R.id.refresh);
        mDelete=(Button)super.findViewById(R.id.delete);
        mRefresh.setOnClickListener(this);
        mDelete.setOnClickListener(this);
    }


    private void initAdapter(){
        mGridViewImageAdapter=new GridViewImageAdapter(this,dataList);
        mGridView.setAdapter(mGridViewImageAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refresh:
                if(mGridViewImageAdapter!=null){
                    mGridViewImageAdapter.notifyDataSetChanged();
                }
                break;

            case R.id.delete:
                FileUtils fileUtils=new FileUtils(this);
                fileUtils.deleteFile();
                break;
        }
    }
}
