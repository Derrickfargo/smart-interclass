/*
 * Copyright (C) 2013 www.418log.org
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
package com.popoy.widget.pulltorefresh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbScaleImageView.java 
 * 描述：根据宽度或高度设置图像尺寸，优先取决于图像尺寸
 * @author zhaoqp
 * @date：2013-9-3 下午4:09:16
 * @version v1.0
 */
public class AbScaleImageView extends ImageView {
    
    /** The current bitmap. */
    private Bitmap currentBitmap;
    
    /** The image change listener. */
    private ImageChangeListener imageChangeListener;
    
    /** The scale to width. */
    private boolean scaleToWidth = false; // this flag determines if should
                                          // measure height manually dependent
                                          // of width

    /**
     * Instantiates a new ab scale image view.
     *
     * @param context the context
     */
   public AbScaleImageView(Context context) {
        super(context);
        init();
    }

    /**
     * Instantiates a new ab scale image view.
     *
     * @param context the context
     * @param attrs the attrs
     * @param defStyle the def style
     */
    public AbScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Instantiates a new ab scale image view.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public AbScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Inits the.
     */
    private void init() {
        this.setScaleType(ScaleType.CENTER_INSIDE);
    }

    /**
     * Recycle.
     */
    public void recycle() {
        setImageBitmap(null);
        if ((this.currentBitmap == null) || (this.currentBitmap.isRecycled()))
            return;
        this.currentBitmap.recycle();
        this.currentBitmap = null;
    }

    /**
     * 描述：TODO
     * @see android.widget.ImageView#setImageBitmap(android.graphics.Bitmap)
     * @author: zhaoqp
     * @date：2013-9-4 下午4:06:34
     * @version v1.0
     */
    @Override
    public void setImageBitmap(Bitmap bm) {
        currentBitmap = bm;
        super.setImageBitmap(currentBitmap);
        if (imageChangeListener != null)
            imageChangeListener.changed((currentBitmap == null));
    }

    /**
     * 描述：TODO
     * @see android.widget.ImageView#setImageDrawable(android.graphics.drawable.Drawable)
     * @author: zhaoqp
     * @date：2013-9-4 下午4:06:34
     * @version v1.0
     */
    @Override
    public void setImageDrawable(Drawable d) {
        super.setImageDrawable(d);
        if (imageChangeListener != null)
            imageChangeListener.changed((d == null));
    }

    /**
     * 描述：TODO
     * @see android.widget.ImageView#setImageResource(int)
     * @author: zhaoqp
     * @date：2013-9-4 下午4:06:34
     * @version v1.0
     */
    @Override
    public void setImageResource(int id) {
        super.setImageResource(id);
    }

    /**
     * The listener interface for receiving imageChange events.
     * The class that is interested in processing a imageChange
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addImageChangeListener<code> method. When
     * the imageChange event occurs, that object's appropriate
     * method is invoked.
     *
     * @see ImageChangeEvent
     */
    public interface ImageChangeListener {
        // a callback for when a change has been made to this imageView
        /**
         * Changed.
         *
         * @param isEmpty the is empty
         */
        void changed(boolean isEmpty);
    }

    /**
     * Gets the image change listener.
     *
     * @return the image change listener
     */
    public ImageChangeListener getImageChangeListener() {
        return imageChangeListener;
    }

    /**
     * Sets the image change listener.
     *
     * @param imageChangeListener the new image change listener
     */
    public void setImageChangeListener(ImageChangeListener imageChangeListener) {
        this.imageChangeListener = imageChangeListener;
    }

    /** The image width. */
    private int imageWidth;
    
    /** The image height. */
    private int imageHeight;

    /**
     * Sets the image width.
     *
     * @param w the new image width
     */
    public void setImageWidth(int w) {
        imageWidth = w;
    }

    /**
     * Sets the image height.
     *
     * @param h the new image height
     */
    public void setImageHeight(int h) {
        imageHeight = h;
    }

    /**
     * 描述：TODO
     * @see android.widget.ImageView#onMeasure(int, int)
     * @author: zhaoqp
     * @date：2013-9-4 下午4:06:34
     * @version v1.0
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        /**
         * if both width and height are set scale width first. modify in future
         * if necessary
         */

        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            scaleToWidth = true;
        } else if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
            scaleToWidth = false;
        } else
            throw new IllegalStateException("width or height needs to be set to match_parent or a specific dimension");

        if (imageWidth == 0) {
            // nothing to measure
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        } else {
            if (scaleToWidth) {
                int iw = imageWidth;
                int ih = imageHeight;
                int heightC = width * ih / iw;
                if (height > 0)
                    if (heightC > height) {
                        // dont let hegiht be greater then set max
                        heightC = height;
                        width = heightC * iw / ih;
                    }

                this.setScaleType(ScaleType.CENTER_CROP);
                setMeasuredDimension(width, heightC);

            } else {
                // need to scale to height instead
                int marg = 0;
                if (getParent() != null) {
                    if (getParent().getParent() != null) {
                        marg += ((RelativeLayout) getParent().getParent()).getPaddingTop();
                        marg += ((RelativeLayout) getParent().getParent()).getPaddingBottom();
                    }
                }

                int iw = imageWidth;
                int ih = imageHeight;

                width = height * iw / ih;
                height -= marg;
                setMeasuredDimension(width, height);
            }

        }
    }

}
