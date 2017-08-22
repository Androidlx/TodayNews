package com.example.lixin.todaynews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.lixin.todaynews.NewsActivity;
import com.example.lixin.todaynews.R;
import com.example.lixin.todaynews.adapter.MyBaseAdapter;
import com.example.lixin.todaynews.bean.ToutiaoInfo;
import com.google.gson.Gson;
import com.limxing.xlistview.view.XListView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by hua on 2017/8/10.
 */

public class JunshiFragment extends Fragment implements XListView.IXListViewListener {

    private List<ToutiaoInfo.ResultBean.DataBean> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.activity_junshi,container,false);
        return view;
    }
    private XListView xListView;
    private MyBaseAdapter adapter;
    private boolean flag;
    private int index = 0;
    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        xListView = (XListView) getView().findViewById(R.id.junshi_xlistview);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);
        getData();

    }
    private void getData(){

        String url = "http://v.juhe.cn/toutiao/index";
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("key", "5b6258c74f4346147b12fe38490a12b2");
        params.addBodyParameter("type", "junshi");
//        params.addHeader("head","android");

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ToutiaoInfo toutiaoInfo = gson.fromJson(result, ToutiaoInfo.class);
                data = toutiaoInfo.getResult().getData();

                if(adapter == null){
                    adapter = new MyBaseAdapter(data,getActivity(),xListView);
                    xListView.setAdapter(adapter);
                }else{
                    adapter.loadMore(data,flag);
                    adapter.notifyDataSetChanged();
                }

                System.out.println("------------------"+result);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void onRefresh() {
        ++index;
        getData();
        flag = true;
        xListView.stopRefresh(true);
    }

    @Override
    public void onLoadMore() {
        ++index;
        getData();
        flag = false;
        xListView.stopLoadMore();
    }
}
