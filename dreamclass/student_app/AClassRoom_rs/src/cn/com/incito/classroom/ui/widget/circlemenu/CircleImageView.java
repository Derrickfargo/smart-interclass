package cn.com.incito.classroom.ui.widget.circlemenu;

/*
 * Copyright 2013 Csaba Szugyiczki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.incito.classroom.R;

/**
 * 
 * @author Szugyi Custom ImageView for the CircleLayout class. Makes it possible
 *         for the image to have an angle, position and a name. Angle is used
 *         for the positioning in the circle menu.
 */
public class CircleImageView extends LinearLayout {

	private float angle = 0;
	private int position = 0;
	private String name;
    TextView textView;
    ImageView drawable1;
	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param context
	 */
	public CircleImageView(Context context) {
		this(context, null);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		init(context);
	}
    public void setDrawable(Drawable drawable) {
        drawable1.setImageDrawable(drawable);
    }
    public void setText(String text) {
        this.name = text;

        textView.setText(text);

    }

    /**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
//		if (attrs != null) {
//			TypedArray a = getContext().obtainStyledAttributes(attrs,
//					R.styleable.CircleImageView);
//
//			name = a.getString(R.styleable.CircleImageView_name);
//		}
		init(context);
	}

	private void init(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.item, null);
        textView = (TextView) view.findViewById(R.id.tv_wifi);
        drawable1 = (ImageView) view.findViewById(R.id.iv_wifi);
		addView(view);
	}

}
