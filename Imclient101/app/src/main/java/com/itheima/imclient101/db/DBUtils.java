package com.itheima.imclient101.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fullcircle on 2017/7/23.
 */

public class DBUtils {
    private static Context context = null;

    public static void initDBUtils(Context context1){
        context = context1.getApplicationContext();
    }

    public static List<String> initContact(String username){
        if(context == null){
            throw new IllegalStateException("未调用initDBUtils 请先初始化当前工具类");
        }
        MyOpenHelper  openHelper = new MyOpenHelper(context);
        SQLiteDatabase sqLiteDatabase = openHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("contact_info", new String[]{"contact"}, "username = ?", new String[]{username}, null, null, null);

        if(cursor == null){
            return null;
        }else{
            List<String> contacts = new ArrayList<>();
            while (cursor.moveToNext()){
                contacts.add(cursor.getString(0));
            }
            return contacts;
        }
    }


    public static void updateContactsDB(List<String> contacts,String username){
        if(context == null){
            throw new IllegalStateException("未调用initDBUtils 请先初始化当前工具类");
        }
        MyOpenHelper  openHelper = new MyOpenHelper(context);
        SQLiteDatabase db = openHelper.getReadableDatabase();
        db.beginTransaction();
        try {
            //①删除所有的联系人
            db.delete("contact_info","username = ?",new String[]{username});
            //②保存最新的联系人数据到数据库中
            ContentValues values = new ContentValues();
            values.put("username",username);
            for(String contact:contacts){
                values.put("contact",contact);
                db.insert("contact_info",null,values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }
}
