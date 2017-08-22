package com.example.lixin.todaynews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.andy.library.ChannelBean;
import com.example.lixin.todaynews.database.DbUtils;

import java.util.List;

/**
 * Created by hua on 2017/8/18.
 */

public class DownActivity extends AppCompatActivity {

    private ListView down_listview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down);
        down_listview = (ListView) findViewById(R.id.down_listview);
        DbUtils dbUtils = new DbUtils(this);
        List<ChannelBean> list = dbUtils.getAllChannels();
        down_listview.setAdapter(new MyBaseAdapter(list));

    }
    class MyBaseAdapter extends BaseAdapter{

        private  List<ChannelBean> list;
        private MyBaseAdapter(List<ChannelBean> list){
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null){
                convertView = View.inflate(DownActivity.this,R.layout.down_item,null);
                TextView textView = (TextView) convertView.findViewById(R.id.down_tv);
                textView.setText(list.get(position).getName());
            }
            return convertView;
        }
    }
}
