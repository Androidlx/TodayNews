package com.example.lixin.todaynews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.library.ChannelActivity;
import com.andy.library.ChannelBean;
import com.example.city_picker.CityListActivity;
import com.example.lixin.todaynews.adapter.MyPageAdapter;
import com.example.lixin.todaynews.database.DbUtils;
import com.example.lixin.todaynews.utils.NetUtils;
import com.example.lixin.todaynews.utils.Themeutils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SlidingMenu slidingMenu;
    private ImageOptions imageOptions;
    private SlidingMenu slidingMenu1;
    private ConnectivityBroadcastReceiver connectivityBroadcastReceiver;
    private NetworkInfo networkInfo;
    //所有频道
    List<ChannelBean> allChannelList;
    //我的频道
    List<ChannelBean> userChannelList;
    private DbUtils dbUtils;
    private MyPageAdapter myPageAdapter;
    private ViewPager viewPager;
    private ImageView sliding_menu;
    private DisplayImageOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置对应的主题 ，在ui创建好之后设置主题无效，所以要放到setContentView（）方法前面setTheme()
        Themeutils.onActivityCreatedSetTheme(this);
        setContentView(R.layout.activity_main);

        //动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        connectivityBroadcastReceiver = new ConnectivityBroadcastReceiver();
        registerReceiver(connectivityBroadcastReceiver, filter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        sliding_menu = (ImageView) findViewById(R.id.sliding_menu);
        ImageView drag = (ImageView) findViewById(R.id.drag);
        drag.setOnClickListener(this);
        sliding_menu.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(9);
        //将viewpager和tablayout关联
        tabLayout.setupWithViewPager(viewPager);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(90))
                .showImageOnLoading(R.mipmap.ic_launcher)
                .build();

        initData();
        slidingMenu = new SlidingMenu(this);
        //设置一下侧滑菜单的位置
        slidingMenu.setMode(SlidingMenu.LEFT);
        //设置触摸的区域
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //设置菜单打开时，内容区域的宽度
        slidingMenu.setBehindOffset(200);
        //范围是 0 - 1f ，当设置成1的时候菜单栏有明显的褪色效果
        slidingMenu.setFadeDegree(0f);
        //将slidingMenu和Activity关联起来
        slidingMenu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.sliding_menu_layout);
        Button btn_qq = (Button)findViewById(R.id.qq);
        btn_qq.setOnClickListener(this);
        Button moblie_reg = (Button) findViewById(R.id.moblie_reg);
        moblie_reg.setOnClickListener(this);
        ImageView btn_night = (ImageView) findViewById(R.id.btn_night);
        btn_night.setOnClickListener(this);
        ImageView shezhi = (ImageView) findViewById(R.id.shezhi);
        shezhi.setOnClickListener(this);
        ImageView sousuo = (ImageView) findViewById(R.id.sousuo);
        sousuo.setOnClickListener(this);
        Button btn = (Button) findViewById(R.id.gengduo);
        btn.setOnClickListener(this);
        ImageView loaddown = (ImageView) findViewById(R.id.loaddown);
        loaddown.setOnClickListener(this);

        slidingMenu1 = new SlidingMenu(this);
        //设置一下侧滑菜单的位置
        slidingMenu1.setMode(SlidingMenu.RIGHT);
        //设置触摸的区域
        slidingMenu1.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //设置菜单打开时，内容区域的宽度
        slidingMenu1.setBehindOffset(200);
        //范围是 0 - 1f ，当设置成1的时候菜单栏有明显的褪色效果
        slidingMenu1.setFadeDegree(0f);
        //将slidingMenu和Activity关联起来
        slidingMenu1.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
        slidingMenu1.setMenu(R.layout.sliding_menu_layout1);

        ImageView qq_login = (ImageView) findViewById(R.id.qq_login);
        qq_login.setOnClickListener(this);

    }


    private void initData() {
        //// TODO: 2017/8/16 查询数据库
        allChannelList = new ArrayList<>();
        userChannelList = new ArrayList<>();

        dbUtils = new DbUtils(this);
        List<ChannelBean> allChannels = dbUtils.getAllChannels();
        if (allChannels == null || allChannels.size() < 1) {
            ChannelBean channelBean = new ChannelBean("推荐", true);
            ChannelBean channelBean1 = new ChannelBean("国内", true);
            ChannelBean channelBean2 = new ChannelBean("体育", true);
            ChannelBean channelBean3 = new ChannelBean("时尚", true);
            ChannelBean channelBean4 = new ChannelBean("社会", true);
            ChannelBean channelBean5 = new ChannelBean("娱乐", true);
            ChannelBean channelBean6 = new ChannelBean("科技", false);
            ChannelBean channelBean7 = new ChannelBean("财经", false);
            ChannelBean channelBean8 = new ChannelBean("军事", false);
            userChannelList.add(channelBean);
            userChannelList.add(channelBean1);
            userChannelList.add(channelBean2);
            userChannelList.add(channelBean3);
            userChannelList.add(channelBean4);
            userChannelList.add(channelBean5);
            allChannelList.add(channelBean);
            allChannelList.add(channelBean1);
            allChannelList.add(channelBean2);
            allChannelList.add(channelBean3);
            allChannelList.add(channelBean4);
            allChannelList.add(channelBean5);
            allChannelList.add(channelBean6);
            allChannelList.add(channelBean7);
            allChannelList.add(channelBean8);

            //// TODO: 2017/8/16 保存数据库
            dbUtils.saveChnnels(allChannelList);
        } else {

            //如果有数据的话 把查询出来的数据赋值到这两个集合中
            allChannelList.addAll(allChannels);

            //查询我的频道数据
            List<ChannelBean> userChannels = dbUtils.getUserChannels();
            userChannelList.addAll(userChannels);
        }

        myPageAdapter = new MyPageAdapter(getSupportFragmentManager(),userChannelList);
        //传入我们自定义的pagerAdapter
        viewPager.setAdapter(myPageAdapter);
       //判断登陆状态
        SharedPreferences sp = MainActivity.this.getSharedPreferences("key",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        boolean isfer = sp.getBoolean("isfer", true);
        if (isfer){
            edit.putBoolean("isfer",false);
            edit.commit();
        }else {
            UMShareAPI.get(MainActivity.this).getPlatformInfo(MainActivity.this, SHARE_MEDIA.QQ, umAuthListener);
        }


    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sliding_menu:
                //自动控制开关，当前是开会关闭，如果是关闭就会打开
//                slidingMenu.toggle();
                //第二种方式，通过判断是否正在显示
                if (slidingMenu.isMenuShowing()){
                    slidingMenu.showContent();
                }else {
                    slidingMenu.showMenu();
                }
                break;
            case R.id.gengduo:
                if (slidingMenu1.isMenuShowing()){
                    slidingMenu1.showContent();
                }else {
                    slidingMenu1.showMenu();
                }
                break;

            case R.id.qq:
                Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                UMShareAPI.get(MainActivity.this).getPlatformInfo(MainActivity.this, SHARE_MEDIA.QQ, umAuthListener);


                break;
            case R.id.qq_login:
                Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                UMShareAPI.get(MainActivity.this).getPlatformInfo(MainActivity.this, SHARE_MEDIA.QQ, umAuthListener);

                break;
            case R.id.btn_night:
                //切换日夜间模式
                Themeutils.ChangeCurrentTheme(this);
                break;
            case R.id.moblie_reg:
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
                break;
            case R.id.shezhi:
                Intent intent1 = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent1);
                break;
            case R.id.drag:
                ChannelActivity.startChannelActivity(this, allChannelList);
                break;
            case R.id.loaddown:
                Intent intent2 = new Intent(MainActivity.this, DownActivity.class);
                startActivity(intent2);
                break;
            case R.id.sousuo:
                CityListActivity.startCityActivityForResult(this);
                break;
        }
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            TextView tvtv = (TextView) findViewById(R.id.qq_login_tv);
            ImageView iviv = (ImageView) findViewById(R.id.qq_login_iv);
            String name = data.get("name");
            String gender = data.get("gender");
            String iconurl = data.get("iconurl");
            System.out.println("--------------"+name+gender+iconurl);
            tvtv.setText("用户名 ："+name  + "  性别 ："+gender);
            ImageLoader.getInstance().displayImage(iconurl,iviv,options);
            ImageLoader.getInstance().displayImage(iconurl,sliding_menu,options);

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == CityListActivity.REQUEST_CODE&&resultCode==CityListActivity.RESULT_CODE){
            String stringExtra = data.getStringExtra(CityListActivity.RESULT_KEY);
            Toast.makeText(this, "当前定位："+stringExtra, Toast.LENGTH_SHORT).show();
        }
        if (requestCode == ChannelActivity.REQUEST_CODE && resultCode == ChannelActivity.RESULT_CODE) {
            String stringExtra = data.getStringExtra(ChannelActivity.RESULT_JSON_KEY);
            if (TextUtils.isEmpty(stringExtra)) {
                return;
            }
            List<ChannelBean> list = new Gson().fromJson(stringExtra, new TypeToken<List<ChannelBean>>() {
            }.getType());
            if (list == null || list.size() < 1) {
                return;
            }
            allChannelList.clear();
            userChannelList.clear();

            //将返回的数据,添加到我们的集合中
            allChannelList.addAll(list);
            for (ChannelBean channelBean : list) {
                boolean select = channelBean.isSelect();
                if (select) {
                    userChannelList.add(channelBean);
                }
            }

            myPageAdapter.notifyDataSetChanged();

            //保存数据库
            dbUtils.clearChannels();
            dbUtils.saveChnnels(allChannelList);

            FragmentManager supportFragmentManager = getSupportFragmentManager();
            List<Fragment> fragments = supportFragmentManager.getFragments();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            for (Fragment fragment :fragments){
                fragmentTransaction.remove(fragment);
            }
            fragmentTransaction.commitAllowingStateLoss();

            recreate();
        }
    }



    private class ConnectivityBroadcastReceiver extends BroadcastReceiver {
        private NetworkInfo networkInfo;

        @Override
        public void onReceive(Context context, Intent intent) {

            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {

                ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo = manager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    boolean isMobileConnection = true;
                    if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
//                        Toast.makeText(context, "wife可用,下载吧", Toast.LENGTH_SHORT).show();
                        isMobileConnection = false;
                    } else if (ConnectivityManager.TYPE_MOBILE == networkInfo.getType()) {
//                        Toast.makeText(context, "现在是,移动网络！", Toast.LENGTH_SHORT).show();
                        isMobileConnection = true;
                    } else {
//                        Toast.makeText(context, "网络不可用请设置网络", Toast.LENGTH_SHORT).show();
                    }
                    NetUtils.getInstance().changeNetState(isMobileConnection);
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //解注册
        unregisterReceiver(connectivityBroadcastReceiver);
    }

}
