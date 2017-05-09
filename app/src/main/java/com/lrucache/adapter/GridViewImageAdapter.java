package com.lrucache.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lrucache.R;
import com.lrucache.bean.ImageBean;
import com.lrucache.interfac.OnImageListener;
import com.lrucache.utils.DownloadImageUtils;

import java.util.ArrayList;

/**
 * @author: xiaxueyi
 * @date: 2017-05-04
 * @time: 13:29
 * @说明:
 */

public class GridViewImageAdapter extends BaseAdapter  {

    private Context mContext=null;

    private DownloadImageUtils mDownloadImageUtils=null;

    private Bitmap bitmap;

    private ArrayList<Object> dataList=new ArrayList<>();
    public GridViewImageAdapter(Context context, ArrayList<Object> dataList) {
        this.mContext=context;
        this.dataList=dataList;

        if(mDownloadImageUtils==null){
            mDownloadImageUtils=new DownloadImageUtils(mContext);

        }

    }

    @Override
    public int getCount() {
        return dataList.size()>0?dataList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.activity_main_item,parent,false);
            holder.image=(ImageView)convertView.findViewById(R.id.image);
            convertView.setTag(holder);

        }else{
            holder=(ViewHolder)convertView.getTag();
        }
       final ImageBean bean=(ImageBean)dataList.get(position);
        bitmap= mDownloadImageUtils.downLoadImage(bean.getUrl(), new OnImageListener() {
            @Override
            public void onImageListener(Object object) {
                if(bitmap==null){
                    bitmap=(Bitmap)object;
                }
            }
        });
        holder.image.setImageBitmap(bitmap);
        return convertView;
    }



    private static final class ViewHolder{
        private ImageView image;
    }
}
