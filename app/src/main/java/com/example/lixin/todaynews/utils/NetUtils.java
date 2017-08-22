package com.example.lixin.todaynews.utils;

import android.content.Context;

import com.example.lixin.todaynews.app.MyApplication;

/**
 * Created by hua on 2017/8/10.
 */

public class NetUtils {

    public static final String SP_NAME = "SP_NAME";
    public static final String PICTURE_LOAD_MODE_KEY = "PICTURE_LOAD_MODE_KEY";



    private boolean isMobileConnection = true;
    public static final String BASE_URL_BIG_PICTURE = "http://www.big.picture";
    public static final String BASE_URL_SMALL_PICTURE = "http://www.small.picture";
    public static final String BASE_URL_NO_PICTURE = "http://www.no.picture";
    private String BASE_URL = BASE_URL_BIG_PICTURE;

    private static NetUtils netUtils;
    private NetUtils(){

    }
    //单例模式
    public static NetUtils getInstance(){
        if (netUtils == null){
            synchronized (NetUtils.class) {
                if (netUtils == null) {
                    netUtils = new NetUtils();
                }
            }
        }
        return netUtils;
    }

    public String getBASE_URL(){

        if (isMobileConnection){

            int mode = MyApplication.getAppcontext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getInt(PICTURE_LOAD_MODE_KEY,0);

            switch (mode){

                case 0:
                    BASE_URL = BASE_URL_BIG_PICTURE;
                    break;
                case 1:
                    BASE_URL = BASE_URL_SMALL_PICTURE;
                    break;
                case 2:
                    BASE_URL = BASE_URL_NO_PICTURE;
                    break;
            }
        }
        return BASE_URL;
    }
    public void changeNetState(boolean isMobileConnection){
        this.isMobileConnection = isMobileConnection;
    }
}
