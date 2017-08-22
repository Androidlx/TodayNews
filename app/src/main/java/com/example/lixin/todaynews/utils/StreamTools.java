package com.example.lixin.todaynews.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by hua on 2017/8/11.
 */

public class StreamTools {
    public static String readFrom(InputStream is)throws Exception{

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = is.read(buffer))!=-1){
            baos.write(buffer,0,len);
        }
        is.close();
        baos.close();
        return baos.toString();
    }
}
