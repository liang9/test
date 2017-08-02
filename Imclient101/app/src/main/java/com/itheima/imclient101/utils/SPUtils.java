package com.itheima.imclient101.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fullcircle on 2017/7/23.
 */

public class SPUtils {
    public static SharedPreferences sp = null;

    public static void getSP(Context context){
        if(sp == null){
           sp = context.getSharedPreferences("user_info",Context.MODE_PRIVATE);
        }
    }

    public static void putString(Context context,String key,String value){
        getSP(context);
        sp.edit().putString(key,value).commit();
    }

    public static String getString(Context context,String key){
        getSP(context);
       return sp.getString(key,"");
    }
}
