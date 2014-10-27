package cn.com.incito.classroom.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GroupIconImageAdapter extends BaseAdapter {
	
	private Context context;
	private List<Integer> IconResourceIdList;
	
	public GroupIconImageAdapter(Context context,List<Integer> IconResourceIdList){
		this.context = context;
		this.IconResourceIdList = IconResourceIdList;
	}

	@Override
	public int getCount() {
		return IconResourceIdList.size();
	}

	@Override
	public Object getItem(int position) {
		return IconResourceIdList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		
		if(convertView == null){
			imageView = new ImageView(context);
			
			// 设置布局 图片120×120显示
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            // 设置显示比例类型
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            
            
		}else{
			imageView = (ImageView) convertView;
		}
		
		imageView.setImageResource(IconResourceIdList.get(position));
		
		return imageView;
	}

}
