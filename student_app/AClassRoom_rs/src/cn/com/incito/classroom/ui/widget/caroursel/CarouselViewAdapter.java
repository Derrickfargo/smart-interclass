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
package cn.com.incito.classroom.ui.widget.caroursel;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import cn.com.incito.socket.utils.ImageUtils;

/**
 * © 2012 amsoft.cn
 * 名称：CarouselViewAdapter.java
 * 描述：自定义View适配的Carousel
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-8-22 下午4:05:09
 */
public class CarouselViewAdapter extends BaseAdapter {

    /**
     * The m context.
     */
    private Context mContext;

    /**
     * The m views.
     */
    private List<View> mViews;

    /**
     * The m reflected.
     */
    private boolean mReflected = true;

    /**
     * The m carousel image views.
     */
    private CarouselItemView[] mCarouselItemViews = null;

    /**
     * Instantiates a new carousel view adapter.
     *
     * @param c         the c
     * @param views     the views
     * @param reflected 反射镜面效果
     */
    public CarouselViewAdapter(Context c, List<View> views, boolean reflected) {
        mContext = c;
        mViews = views;
        mReflected = reflected;
        setImages();
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
    public int getCount() {
        if (mViews == null) {
            return 0;
        } else {
            return mViews.size();
        }
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
    public Object getItem(int position) {
        return position;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mCarouselItemViews[position];
        return convertView;
    }

    /**
     * Sets the images.
     */
    public void setImages() {
        mCarouselItemViews = new CarouselItemView[mViews.size()];
        for (int i = 0; i < mViews.size(); i++) {
            final int index = i;
            View view = mViews.get(i);

            CarouselItemView itemView = new CarouselItemView(mContext);
            itemView.setIndex(i);

            if (mReflected) {
                Bitmap originalImage = ImageUtils.view2Bitmap(view);
                ImageView imageView = new ImageView(mContext);
                LayoutParams mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                mLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                imageView.setScaleType(ScaleType.CENTER);
                imageView.setImageBitmap(ImageUtils.toReflectionBitmap(originalImage));
                itemView.addView(imageView, mLayoutParams);
                imageView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        //((AbActivity)mContext).showToast("点击了:"+index);
                    }
                });

            } else {
             measureView(view);
                LayoutParams mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                mLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                itemView.addView(view, mLayoutParams);
                view.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        //((AbActivity)mContext).showToast("点击了:"+index);
                    }
                });
            }

            mCarouselItemViews[i] = itemView;

        }

    }

    void setViews(List<View> views) {
        mViews = views;
    }
    /**
     * 测量这个view，最后通过getMeasuredWidth()获取宽度和高度.
     *
     * @param v
     *            要测量的view
     * @return 测量过的view
     */
    public static void measureView(View v) {
        ViewGroup.LayoutParams p = v.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight,
                    View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
        }
        v.measure(childWidthSpec, childHeightSpec);
    }

}
