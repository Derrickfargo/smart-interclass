/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.incito.classroom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.wifi.ScanResult;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.popoy.tookit.utils.ImageUtils;
import com.popoy.tookit.utils.ViewUtil;

import java.util.List;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.ui.widget.caroursel.CarouselItemView;
// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：CarouselViewAdapter.java
 * 描述：自定义View适配的Carousel
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-8-22 下午4:05:09
 */
public class WifiListAdapter1 extends BaseAdapter {

    /**
     * The m context.
     */
    private Context mContext;

    /**
     * The m views.
     */
    private List<ScanResult> wifis;

    /**
     * The m reflected.
     */
    private boolean mReflected = true;

    LayoutInflater mInflater;

    /**
     * Instantiates a new carousel view adapter.
     *
     * @param context   the c
     * @param wifis     the views
     * @param reflected 反射镜面效果
     */
    public WifiListAdapter1(Context context, List<ScanResult> wifis, boolean reflected) {
        this.mContext = context;
        this.wifis = wifis;
        mReflected = reflected;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 描述：TODO.
     *
     * @return the count
     * @version v1.0
     * @author: amsoft.cn
     * @date：2013-8-22 下午4:07:39
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return wifis.size();
    }

    /**
     * 描述：TODO.
     *
     * @param position the position
     * @return the item
     * @version v1.0
     * @author: amsoft.cn
     * @date：2013-8-22 下午4:07:39
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        return wifis.get(position);
    }

    /**
     * 描述：TODO.
     *
     * @param position the position
     * @return the item id
     * @version v1.0
     * @author: amsoft.cn
     * @date：2013-8-22 下午4:07:39
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 描述：TODO.
     *
     * @param position    the position
     * @param convertView the convert view
     * @param parent      the parent
     * @return the view
     * @version v1.0
     * @author: amsoft.cn
     * @date：2013-8-22 下午4:07:39
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.item_wifilist, parent, false);
            holder = new ViewHolder();
            holder.iv_wifi_list = (ImageView) convertView.findViewById(R.id.iv_wifi_list);
            holder.tv_ssid = (TextView) convertView.findViewById(R.id.tv_ssid);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CarouselItemView itemView = new CarouselItemView(mContext);
        itemView.setIndex(position);
        holder.iv_wifi_list.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_classroom));
        holder.tv_ssid.setText(wifis.get(position).SSID);
        ViewUtil.measureView(convertView);
        LayoutParams mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        itemView.addView(convertView, mLayoutParams);
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //((AbActivity)mContext).showToast("点击了:"+index);
            }
        });
        return itemView;
    }

    static class ViewHolder {
        public TextView tv_ssid;
        public ImageView iv_wifi_list;
    }


}
