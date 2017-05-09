package com.lrucache.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;

/**
 * @author: xiaxueyi
 * @date: 2017-05-04
 * @time: 13:40
 * @说明: 自定义异步线程
 */

public class CustomAsyncTask extends AsyncTask<String ,Integer ,Bitmap>{
    private Context mContext=null;
    private DownloadImageUtils mDownloadImageUtils=null;
    public CustomAsyncTask(Context context) {
        this.mContext=context;
        if(mDownloadImageUtils==null){
            mDownloadImageUtils=new DownloadImageUtils(mContext);
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
//        Bitmap bitmap=DownloadImageUtils.getBitmapFormUrl
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Bitmap bitmap) {
        super.onCancelled(bitmap);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
