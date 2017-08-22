package com.example.lixin.todaynews.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.andy.library.ChannelBean;
import com.example.lixin.todaynews.NewsFragment;
import com.example.lixin.todaynews.fragment.CaijingFragment;
import com.example.lixin.todaynews.fragment.GuoneiFragment;
import com.example.lixin.todaynews.fragment.JunshiFragment;
import com.example.lixin.todaynews.fragment.KejiFragment;
import com.example.lixin.todaynews.fragment.ShehuiFragment;
import com.example.lixin.todaynews.fragment.ShishangFragment;
import com.example.lixin.todaynews.fragment.TiyuFragment;
import com.example.lixin.todaynews.fragment.TuijianFragment;
import com.example.lixin.todaynews.fragment.YuleFragment;

import java.util.List;

/**
 * Created by hua on 2017/8/2.
 */

public class MyPageAdapter extends FragmentPagerAdapter {

//    private String[] titles = {"推荐", "国内", "体育", "时尚", "社会", "娱乐", "科技", "财经", "军事"};
    //这个为开启事务
    private FragmentManager mFragmentManager;
    List<ChannelBean> mChannelBeanList;
    public MyPageAdapter(FragmentManager fm, List<ChannelBean> channelBeanList) {
        super(fm);
        mFragmentManager = fm;
        mChannelBeanList = channelBeanList;
    }
    @Override
    public Fragment getItem(int position) {
//
//        //new 我对应得fragment
//        NewsFragment newsFragment = new NewsFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("text", mChannelBeanList.get(position).getName());
//        newsFragment.setArguments(bundle);
//        return newsFragment;


        String name = mChannelBeanList.get(position).getName();
        switch (name) {

            case "推荐":
                return new TuijianFragment();
            case "国内":
                return new GuoneiFragment();
            case "体育":
                return new TiyuFragment();
            case "时尚":
                return new ShishangFragment();
            case "社会":
                return new ShehuiFragment();
            case "娱乐":
                return new YuleFragment();
            case "科技":
                return new KejiFragment();
            case "财经":
                return new CaijingFragment();
            case "军事":
                return new JunshiFragment();
        }
        return null;
    }

//        if (position == 1) {
//            return new GuoneiFragment();
//        } else if (position == 2) {
//            return new TiyuFragment();
//        }else if (position==3){
//            return new ShishangFragment();
//        }else if (position==4){
//            return new ShehuiFragment();
//        }else if (position==5){
//            return new YuleFragment();
//        }else if (position==6){
//            return new KejiFragment();
//        }else if (position==7){
//            return new CaijingFragment();
//        }else if (position==8){
//            return new JunshiFragment();
//        }
//        return new TuijianFragment();
        //记得盘空
        @Override
        public int getCount() {
            return mChannelBeanList == null ? 0 : mChannelBeanList.size();
    }
    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    //设置tablayout的每个tab的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mChannelBeanList.get(position).getName();
    }

}