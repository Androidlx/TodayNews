package com.example.lixin.todaynews;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lixin.todaynews.app.MyApplication;
import com.example.lixin.todaynews.utils.NetUtils;

import org.w3c.dom.Text;

import java.io.File;
import java.math.BigDecimal;

import cn.jpush.android.api.JPushInterface;

import static com.example.lixin.todaynews.R.id.total;

/**
 * Created by hua on 2017/8/9.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences.Editor edit;
    private TextView total;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shezhi);
        CheckBox cc = (CheckBox) findViewById(R.id.cc);
        ImageView setting_back = (ImageView) findViewById(R.id.setting_back);
        TextView t5 = (TextView) findViewById(R.id.t5);
        t5.setOnClickListener(this);
        total = (TextView) findViewById(R.id.total);
        try {
            //去计算缓存大小
            String totalCacheSize = getTotalCacheSize();
            total.setText(totalCacheSize);

        } catch (Exception e) {
            e.printStackTrace();
        }
        setting_back.setOnClickListener(this);
        boolean key = getSharedPreferences("FLAG", MODE_PRIVATE).getBoolean("key", true);
        SharedPreferences sp = getSharedPreferences("FLAG",MODE_PRIVATE);
        edit = sp.edit();

        cc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    edit.putBoolean("key",true).commit();
                    JPushInterface.resumePush(SettingActivity.this);
                }else {
                    edit.putBoolean("key",false).commit();
                    JPushInterface.stopPush(SettingActivity.this);
                }
            }
        });
    if (key){
        cc.setChecked(true);
    }else {
        cc.setChecked(false);
    }

    }
    public void tiaoshi(View view){
        Toast.makeText(SettingActivity.this, NetUtils.getInstance().getBASE_URL(), Toast.LENGTH_SHORT).show();
    }
    public void onclick(View view){
        // TODO: 2017/8/10 做一个alertDialog
        String[] strings = {"最佳效果", "较省流量", "极省流量"};
        int mode = MyApplication.getAppcontext().getSharedPreferences(NetUtils.SP_NAME, Context.MODE_PRIVATE).getInt(NetUtils.PICTURE_LOAD_MODE_KEY, 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("非wifi网络流量");
        builder.setSingleChoiceItems(strings, mode, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //// TODO: 2017/8/10  要保存我们的图片加载模式
                MyApplication.getAppcontext().getSharedPreferences(NetUtils.SP_NAME, Context.MODE_PRIVATE).edit().putInt(NetUtils.PICTURE_LOAD_MODE_KEY, which).commit();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.setting_back:
                this.finish();
                break;
            case R.id.t5:
                showtotalSettingDialog();
                break;
        }
    }


    /**
     * 清理app的缓存 其实是清除的getCacheDir 和getExternalCacheDir这两个文件
     *
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    /**
     * 删除一个文件夹
     *
     * @param dir
     * @return
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 计算app的缓存大小其实计算的是 getCacheDir()这个file和getExternalCacheDir()这个file大小的和
     */
    public String getTotalCacheSize() throws Exception {
        long cacheSize = getFolderSize(this.getCacheDir());

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(this.getExternalCacheDir());
        }
//        File cacheDir = StorageUtils.getOwnCacheDirectory(this, "universalimageloader/Cache");
        return getFormatSize(cacheSize);
    }

    /**
     * 计算文件夹的大小
     */
    public static long getFolderSize(File file) throws Exception {
        if (!file.exists()) {
            return 0;
        }
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
    private void showtotalSettingDialog(){


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SettingActivity.this);
        builder.setMessage("是否清除缓存");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //imageLoader清除缓存大小的放大
//                ImageLoader.getInstance().clearDiskCache();

                clearAllCache(SettingActivity.this);
                String totalCacheSize = null;
                try {
                    totalCacheSize = getTotalCacheSize();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                total.setText(totalCacheSize);
            }
        });
        builder.create().show();
    }
}
