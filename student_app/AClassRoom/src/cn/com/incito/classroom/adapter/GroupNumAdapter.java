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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.vo.LoginRes2Vo;

/**
 * 组成员列表适配器
 * Created by popoy on 2014/7/28.
 */
public class GroupNumAdapter extends BaseAdapter {

    /**
     * The m context.
     */
    private Context mContext;

    /**
     * The m views.
     */
    private List<LoginRes2Vo> datas;

    /**
     * The m reflected.
     */
    private boolean mReflected = true;

    LayoutInflater mInflater;

    public GroupNumAdapter(Context context, List<LoginRes2Vo> datas) {
        this.mContext = context;
        this.datas = datas;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.item_group_mem, parent, false);
            holder = new ViewHolder();
            holder.tv_num_name = (TextView) convertView.findViewById(R.id.tv_num_name);
            holder.rlayout = (RelativeLayout) convertView.findViewById(R.id.rlayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (datas.get(position).isLogin()) {
            holder.rlayout.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_logged_user_m));
        } else {
            holder.rlayout.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_not_logged_user_m));
        }
        holder.tv_num_name.setText(datas.get(position).getName());
        return convertView;
    }

    static class ViewHolder {
        public TextView tv_num_name;
        public RelativeLayout rlayout;
    }

    public void setDatas(List<LoginRes2Vo> datas) {
        this.datas = datas;
    }
}
