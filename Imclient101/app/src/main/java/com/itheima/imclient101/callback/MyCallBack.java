package com.itheima.imclient101.callback;

import com.hyphenate.EMCallBack;
import com.itheima.imclient101.utils.ThreadUtils;

/**
 * Created by fullcircle on 2017/7/22.
 */

public abstract class MyCallBack implements EMCallBack {
    public abstract void success();
    public abstract void error(int i, String s);
    public void progress(int i, String s){

    }

    @Override
    public void onSuccess() {
        ThreadUtils.RunOnUIThread(new Runnable() {
            @Override
            public void run() {
                success();
            }
        });
    }

    @Override
    public void onError(final int i, final String s) {
        ThreadUtils.RunOnUIThread(new Runnable() {
            @Override
            public void run() {
                error(i,s);
            }
        });
    }

    @Override
    public void onProgress(final int i, final String s) {
        ThreadUtils.RunOnUIThread(new Runnable() {
            @Override
            public void run() {
               progress(i,s);
            }
        });
    }
}
