package com.itheima.imclient101.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fullcircle on 2017/7/23.
 */

public class MyOpenHelper extends SQLiteOpenHelper {
    public MyOpenHelper(Context context) {
        super(context, "contact.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table contact_info (_id integer primary key autoincrement, username varchar(20), contact varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
