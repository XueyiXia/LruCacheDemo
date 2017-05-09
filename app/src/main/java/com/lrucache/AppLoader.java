package com.lrucache;

import android.app.Application;

/**
 * @author: xiaxueyi
 * @date: 2017-05-04
 * @time: 13:01
 * @说明:
 */

public class AppLoader extends Application{

    private static AppLoader mInstance=null;

    public static AppLoader getInstance(){
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance=this;
    }
}
