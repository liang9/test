package com.itheima.imclient101.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fullcircle on 2017/7/22.
 */

public class Utils {

    public static boolean checkUsername(String username){
        if(TextUtils.isEmpty(username)){
            return false;
        }else{
            return username.matches("^[a-zA-Z][0-9a-zA-Z]{4,15}$");
        }
    }

    public static boolean checkPwd(String pwd){
        if(TextUtils.isEmpty(pwd)){
            return false;
        }else{
            return pwd.matches("^[0-9a-zA-Z]{5,15}$");
        }
    }

    public static String getFirstChar(String str){
        if(str ==null ){
            return null;
        }else{
            return str.substring(0,1).toUpperCase();
        }
    }

    public static String getDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
}
