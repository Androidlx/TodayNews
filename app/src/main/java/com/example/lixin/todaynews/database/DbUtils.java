package com.example.lixin.todaynews.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.andy.library.ChannelBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 2017/8/16.
 */

public class DbUtils {

    private final SQLiteDatabase db;
    private ChannelBean channelBean;

    public DbUtils(Context context){

        Sqlite sqlite = new Sqlite(context);
        db = sqlite.getWritableDatabase();
    }

    public void saveChnnels(List<ChannelBean> channelBeanList) {
        if (channelBeanList == null || channelBeanList.size() < 0) {
            return;
        }
        for (ChannelBean channelBean : channelBeanList) {
            ContentValues values = new ContentValues();
            values.put("name", channelBean.getName());
            values.put("isSelect", channelBean.isSelect());
            db.insert("drap", null, values);
        }
    }

    public List<ChannelBean> getAllChannels() {
        Cursor cursor = db.query("drap", null, null, null, null, null, null);

        List<ChannelBean> channelBeanList = new ArrayList<>();
        ChannelBean channelBean;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int selected = cursor.getInt(cursor.getColumnIndex("isSelect"));
            channelBeanList.add(new ChannelBean(name, selected == 0 ? false : true));
        }
        cursor.close();
        return channelBeanList;
    }

    public List<ChannelBean> getUserChannels() {
        Cursor cursor = db.query("drap", null, "isSelect=?", new String[]{"1"}, null, null, null);
        List<ChannelBean> channelBeanList = new ArrayList<>();
        ChannelBean channelBean;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int selected = cursor.getInt(cursor.getColumnIndex("isSelect"));
            channelBeanList.add(new ChannelBean(name, selected == 0 ? false : true));
        }
        cursor.close();
        return channelBeanList;
    }

    public void clearChannels() {
        db.delete("drap", null, null);
    }
}
