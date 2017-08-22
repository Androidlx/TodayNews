package com.example.lixin.todaynews.app;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by hua on 2017/8/2.
 */

public class MyApplication extends Application {
    {

        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }
    public static MyApplication myApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        x.Ext.init(this);
        UMShareAPI.get(this);
        Config.DEBUG = true;
        MobSDK.init(this, "1ff5b3afb1cee", "40b1c5ea90b2599b7ef04551a9042327");
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

//         String path = Environment.getExternalStorageDirectory().getPath()+"/"+"getCacheDir"+"/"+"Pictrues";
//         File file = new File(path);
//         String path1 = context.getExternalCacheDir()+"/getCacheDir/pictures";
//         File file = new File(path1);
           File file=new File(getCacheDir(),"pictures");
                 ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                         .memoryCacheExtraOptions(400,800)//配置内存缓存图片的尺寸
                         //.diskCacheExtraOptions() bug 不推介你手动去配置
                         .memoryCacheSize(2 * 1024 * 1024)//配置内存缓存的大小  例如 : 2* 1024 * 1024 = 2MB
                         .threadPoolSize(3)//配置加载图片的线程数
                         .threadPriority(100)//配置线程的优先级
                         .diskCache(new UnlimitedDiskCache(file))//UnlimitedDiskCache 限制这个图片的缓存路径
                         .diskCacheSize(50 * 1024 * 1024)//在sdcard缓存50MB
                         .diskCacheFileNameGenerator(new Md5FileNameGenerator())//MD5这种方式生成缓存文件的名字
                         .diskCacheFileCount(20)//配置sdcard缓存文件的数量
                         .build();//配置构建完成

                 ImageLoader.getInstance().init(config);

    }
    public static Context getAppcontext(){
        return myApplication;
    }
}
