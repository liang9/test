package com.itheima.imclient101.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by fullcircle on 2017/7/22.
 */

public class ThreadUtils {
    //创建单线程的线程池
    private static Executor executor = Executors.newSingleThreadExecutor();
    //创建主线程的handler对象
    private static Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 在子线程中运行Runnable的工具方法
     * @param runnable
     */
    public static void RunOnSubThread(Runnable runnable){
        executor.execute(runnable);
    }

    public static void RunOnUIThread(Runnable runnable){
        handler.post(runnable);
    }
}
