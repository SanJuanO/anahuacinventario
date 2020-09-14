package com.gesdes.android.puebla.anahuac;

import android.app.Application;
import android.os.Handler;

import com.gesdes.android.puebla.anahuac.Manaer.SharedPreferencesManager;
import com.gesdes.android.puebla.anahuac.Utils.Util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyApplication extends Application {

    ExecutorService cachedThreadPool = null;

    public ExecutorService getCachedThreadPool() {
        return cachedThreadPool;
    }


    private Handler handler = new Handler();

    public Handler getHandler() {
        return handler;
    }

    static MyApplication instance = null;

    public static MyApplication getInstance(){

        return instance;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        cachedThreadPool = Executors.newCachedThreadPool();
        Util.changLanguage(instance, SharedPreferencesManager.getLanguageId(instance));
    }
}
