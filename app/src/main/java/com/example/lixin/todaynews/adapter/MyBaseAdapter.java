package com.example.lixin.todaynews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.lixin.todaynews.NewsActivity;
import com.example.lixin.todaynews.R;
import com.example.lixin.todaynews.bean.ToutiaoInfo;
import com.limxing.xlistview.view.XListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by hua on 2017/8/11.
 */

public class MyBaseAdapter extends BaseAdapter {

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .showImageOnLoading(R.mipmap.ic_launcher)
            .build();

    ImageOptions imageOptions = new ImageOptions.Builder()
            .setLoadingDrawableId(R.mipmap.ic_launcher)
            .setUseMemCache(true)
//            .setCircular(true)
            .build();
    private List<ToutiaoInfo.ResultBean.DataBean> data;
    private Context context;
    private XListView xListView;
    private LayoutInflater mLayoutInflater;
    private PopupWindow mPopupWindow;
    private TextView deleteView;
    private ImageView closeView;
    public MyBaseAdapter(List<ToutiaoInfo.ResultBean.DataBean> data, Context context,XListView xListView){
        this.data = data;
        this.context = context;
        this.xListView=xListView;
        mLayoutInflater = LayoutInflater.from(context);
        initPopView();
    }


    public void loadMore(List<ToutiaoInfo.ResultBean.DataBean> datas,boolean flag){

        //新闻为例，数据要添加到最前面
        for (ToutiaoInfo.ResultBean.DataBean bean: datas) {
            //flag -> true 添加到卡面  false ->添加到末尾
            if(flag) {
                //将数据添加到最前面
                data.add(0, bean);
            }else{
                //默认将数据添加到末尾
                data.add(bean);
            }
        }

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position%2==0){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        ViewHolder1 holder1 = null;
        switch (getItemViewType(position)){

            case 0:
                if (convertView == null){
                    convertView = View.inflate(context, R.layout.item,null);
                    holder = new ViewHolder();
                    holder.tv = (TextView) convertView.findViewById(R.id.item_tv1);
                    holder.tv2 = (TextView) convertView.findViewById(R.id.item_tv2);
                    holder.tv3 = (TextView) convertView.findViewById(R.id.item_tv3);
                    holder.iv = (ImageView) convertView.findViewById(R.id.item_iv);
                    holder.pop = (ImageView) convertView.findViewById(R.id.pop);
                    convertView.setTag(holder);
                }else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.tv.setText(data.get(position).getTitle());
                holder.tv2.setText(data.get(position).getAuthor_name());
                holder.tv3.setText(data.get(position).getDate());
                holder.pop.setOnClickListener(new popAction(position));
//                x.image().bind(holder.iv,data.get(position).getThumbnail_pic_s(),imageOptions);
                ImageLoader.getInstance().displayImage(data.get(position).getThumbnail_pic_s(),holder.iv,options);
                break;
            case 1:
                if (convertView == null){
                    convertView = View.inflate(context, R.layout.item1,null);
                    holder1 = new ViewHolder1();
                    holder1.tv1 = (TextView) convertView.findViewById(R.id.item1_tv1);
                    holder1.tv2 = (TextView) convertView.findViewById(R.id.item1_tv2);
                    holder1.tv3 = (TextView) convertView.findViewById(R.id.item1_tv3);
                    holder1.iv1 = (ImageView) convertView.findViewById(R.id.item1_iv);
                    holder1.iv2 = (ImageView) convertView.findViewById(R.id.item1_iv1);
                    holder1.iv3 = (ImageView) convertView.findViewById(R.id.item1_iv2);
                    holder1.pop1 = (ImageView) convertView.findViewById(R.id.pop1);
                    convertView.setTag(holder1);
                }else {
                    holder1 = (ViewHolder1) convertView.getTag();
                }
                holder1.tv1.setText(data.get(position).getTitle());
                holder1.tv2.setText(data.get(position).getAuthor_name());
                holder1.tv3.setText(data.get(position).getDate());
                holder1.pop1.setOnClickListener(new popAction(position));
//                x.image().bind(holder1.iv1,data.get(position).getThumbnail_pic_s(),imageOptions);
//                x.image().bind(holder1.iv2,data.get(position).getThumbnail_pic_s02(),imageOptions);
//                x.image().bind(holder1.iv3,data.get(position).getThumbnail_pic_s03(),imageOptions);

                ImageLoader.getInstance().displayImage(data.get(position).getThumbnail_pic_s(),holder1.iv1,options);
                ImageLoader.getInstance().displayImage(data.get(position).getThumbnail_pic_s02(),holder1.iv2,options);
                ImageLoader.getInstance().displayImage(data.get(position).getThumbnail_pic_s03(),holder1.iv3,options);


                break;
        }

            xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra("url",data.get(position-1).getUrl());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView tv,tv2,tv3;
        ImageView iv,pop;
    }
    class ViewHolder1{

        TextView tv1,tv2,tv3;
        ImageView iv1,iv2,iv3,pop1;

    }

    private class popAction implements View.OnClickListener {

        private int position;

        public popAction(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            //操作对应positiion的数据
            int[] array = new int[2];
            v.getLocationOnScreen(array);
            int x = array[0];
            int y = array[1];
            showPop(v, position, x, y);
        }
    }
    private void initPopView() {
        View popwindowLayout = mLayoutInflater.inflate(R.layout.popwindow_layout, null);
        mPopupWindow = new PopupWindow(popwindowLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        mPopupWindow.setAnimationStyle(R.style.popWindowAnimation);

        //知道popwindow中间的控件 ,去做点击
        deleteView = (TextView) popwindowLayout.findViewById(R.id.delete_tv);
        closeView = (ImageView) popwindowLayout.findViewById(R.id.close_iv);
    }

    private void showPop(View parent, final int position, int x, int y) {
//根据view的位置显示popupwindow的位置
        mPopupWindow.showAtLocation(parent, 0, x, y);

        //根据view的位置popupwindow将显示到他的下面 , 可以通过x ,y 参数修正这个位置
        // mPopupWindow.showAsDropDown(parent,0,-50);

        //设置popupwindow可以获取焦点,不获取焦点的话 popupwiondow点击无效
        mPopupWindow.setFocusable(true);

        //点击popupwindow的外部,popupwindow消失
        mPopupWindow.setOutsideTouchable(true);

        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });

        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });




    }

}
