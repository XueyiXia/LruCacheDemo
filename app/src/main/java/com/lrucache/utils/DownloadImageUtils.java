package com.lrucache.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;

import com.lrucache.interfac.OnImageListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: xiaxueyi
 * @date: 2017-05-05
 * @time: 09:30
 * @说明: 图片下载类,获取图片的BitMap 使用LruCache 缓存起来
 */

public class DownloadImageUtils {

    private Context mContext=null;

    private ExecutorService mImageThreadPool = null;    //线程池

    private LruCache<String ,Bitmap> mLruCache=null;

    private FileUtils mFileUtils=null;




    public DownloadImageUtils(Context context) {

        this.mContext=context;

        int maxMemory=(int)Runtime.getRuntime().maxMemory();    //获取应用最大内存

        int imageCacheSize=maxMemory/8;     //设置图片的缓存大小为最大内存的1/8，即设置


        if(mLruCache==null){
            mLruCache=new LruCache<String ,Bitmap>(imageCacheSize){
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();    //获取bitmap 大小
                }
            };
        }

        mFileUtils=new FileUtils(mContext);
    }


    /**
     * 获取线程池的方法，因为涉及到并发的问题，我们加上同步锁
     * @return
     */
    public ExecutorService getThreadPoolInstance (){
        if(mImageThreadPool==null){
            synchronized (ExecutorService.class){   //涉及并发问题，所以设置了同步锁
                if(mImageThreadPool==null){
                    mImageThreadPool=  Executors.newFixedThreadPool(2); //开启两个线程
                }
            }
        }

        return mImageThreadPool;
    }

    /**
     * 从网络获取图片，返回的是一个bitmap
     * @param url
     * @return
     */
    public static Bitmap getBitmapFormUrl(String url){
        Bitmap bitmap=null;
        HttpURLConnection connection=null;
        try {
            URL imageUrl=new URL(url);
            connection=(HttpURLConnection)imageUrl.openConnection();    //打开网络连接
            connection.setConnectTimeout(10*1000);  //设置连接的超时时间
            connection.setRequestMethod("POST");    //设置请求方式
            connection.setDoInput(true);    //设置是否输入
            connection.setDoOutput(true);   //设置是否输出

            InputStream inputStream=connection.getInputStream();    //获取输入流
            bitmap= BitmapFactory.decodeStream(inputStream);

        }catch (Exception e){
            e.getMessage();
        }finally {
            if(connection!=null){
                connection.disconnect();    //销毁链接
            }
        }

        return bitmap;

    }


    /**
     * 设置Bitmap 到缓存中
     * @param bitmap
     */
    public  void addBitmapCache(String key,Bitmap bitmap){
        if (bitmap!=null){
            mLruCache.put(key,bitmap);

        }
    }


    /**
     * 下载图片,先从缓存里面寻找,没有就开启线程下载图片(PS:最开始的设计：先从缓存找，找不到，从SD卡找，如果两者都没有，那就开启线程下载，因为时间问题)
     * @param url
     * @param onImageListener
     */
    public Bitmap downLoadImage(final String url,final OnImageListener onImageListener){
        try {
            final String subUrl = url.replaceAll("[^\\w]", "");
            Bitmap bitmap=getCacheBitmap(subUrl);
            if(bitmap!=null){
                return bitmap;
            }else{

                //在子线程里面发数据回调
                final Handler handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if(msg.what==200){
                            Bitmap tempBitmap=(Bitmap)msg.obj;
                            if(onImageListener!=null){
                                onImageListener.onImageListener(tempBitmap);
                            }
                        }

                    }
                };


                getThreadPoolInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap  bitmap=getBitmapFormUrl(url);
                        Message message=handler.obtainMessage();
                        message.obj=bitmap;
                        message.what=200;
                        handler.sendMessage(message);
                        try {
                            //保存在SD卡或者手机目录
                            mFileUtils.savaBitmap(subUrl, bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        addBitmapCache(url,bitmap);     //增加到缓存里面
                    }
                });
            }



        }catch (Exception e){
            e.getMessage();
        }


        return null;
    }


    /**
     * 从缓存读取图片
     * @param key
     * @return
     */
    public Bitmap getCacheBitmap(String key){
        Bitmap  bitmap=null;
        try {
            if( mLruCache.get(key)!=null){
                return   mLruCache.get(key); //根据key 获取缓存
            }else if(mFileUtils.isFileExists(key) && mFileUtils.getFileSize(key) != 0){
                //从SD卡获取手机里面获取Bitmap
                bitmap = mFileUtils.getBitmap(key);
                //将Bitmap 加入内存缓存
                addBitmapCache(key, bitmap);
                return bitmap;
            }

        }catch (Exception e){
            e.getMessage();
        }
        return bitmap;
    }

    /**
     * 停止下载
     */
    public void cancelTask(){
        getThreadPoolInstance().shutdown();
    }

}
