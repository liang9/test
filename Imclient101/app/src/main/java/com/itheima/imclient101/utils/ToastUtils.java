package com.itheima.imclient101.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by fullcircle on 2017/7/22.
 */

public class ToastUtils {
    private static Toast toast = null;
    public static void showToast(Context context,String msg){
        if(toast == null){
            toast = Toast.makeText(context.getApplicationContext(),msg,Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }
}
