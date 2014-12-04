package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.common.utils.UIHelper;


public class EvaluateHandlerActivity extends BaseActivity{
	private ArrayList mImageResourceIds=new ArrayList<Drawable>();
	private Gallery mGallery;
	private ImageView mImageView;
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setContentView(R.layout.evaluate_activity);
		UIHelper.getInstance().setEvaluateHandlerActivity(this);
		for (int i = 0; i < 10; i++) {
			mImageResourceIds.add(getResources().getDrawable(R.drawable.ic_launcher));
		}
		initView();
		
	}

	private void initView() {
		mGallery=(Gallery)findViewById(R.id.gallery);
		mGallery.setAdapter(new ImageAdapter(this));
		mImageView=(ImageView)findViewById(R.id.imageView);
		mGallery.onFling(null, null, 0, 0);
		mGallery.setOnItemClickListener(new OnItemClickListener() {
			     public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			             mImageView.setBackground((Drawable)mImageResourceIds.get(arg2));
			        }
			    });
	}
    public class ImageAdapter extends BaseAdapter {

        Context mContext;        //上下文对象
		
        public ImageAdapter(Context context) {
            this.mContext = context;
        }
        
        public int getCount() {
            return mImageResourceIds.size();
        }

        //获取图片在库中的位置
        public Object getItem(int position) {
            return mImageResourceIds.get(position);
        }

        //获取图片在库中的位置
        public long getItemId(int position) {
            return position;
        }

        //获取适配器中指定位置的视图对象
        public View getView(int position, View convertView, ViewGroup parent) {
//        	View view=null;
//        	if(convertView==null){
//        		view
//        	}else{
//        		view=convertView;
//        	}
        	
            ImageView imageView = new ImageView(mContext);
            imageView.setBackground((Drawable)mImageResourceIds.get(position));
//            imageView.setLayoutParams(new Gallery.LayoutParams(120, 120));
//            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return imageView;
        }
    }
	
	
}
