package com.example.lixin.todaynews.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hua on 2017/8/16.
 */

public class Sqlite extends SQLiteOpenHelper {
    public Sqlite(Context context) {
        super(context, "drap.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table drap(_id Integer primary key autoincrement,name varchar(20),isSelect Integer(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
