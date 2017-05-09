package com.lrucache.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * @author: xiaxueyi
 * @date: 2017-05-04
 * @time: 13:04
 * @说明:
 */

public class LruCacheUtils extends LruCache<String,Bitmap>{


    private int maxMemory=(int)Runtime.getRuntime().maxMemory();    //获取应用的最大内存

    private int imageCache=maxMemory/8;     //设置缓存图片为内存的八分之一



    public LruCacheUtils(int maxSize) {
        super(maxSize);
    }


    @Override
    protected int sizeOf(String key, Bitmap value) {
        return super.sizeOf(key, value);
    }
}
