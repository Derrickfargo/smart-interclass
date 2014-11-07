package cn.com.incito.classroom.adapter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import cn.com.incito.classroom.R;

public class GroupIconImageAdapter extends BaseAdapter {
	
	private Context context;
	private List<Map<String,Drawable>> IconResourceIdList;
	
	
	public GroupIconImageAdapter(Context context,List<Map<String,Drawable>> IconResourceIdList){
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
		Map<String,Drawable> map = IconResourceIdList.get(position);
		
		if(convertView == null){
			imageView = new ImageView(context);
			
			// 设置布局 图片120×120显示
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setBackgroundResource(R.drawable.bg_ico_group);
            imageView.setPadding(8, 8, 8, 8); 
            // 设置显示比例类型
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            
            
		}else{
			imageView = (ImageView) convertView;
		}
		
		Set<String> set = map.keySet();
		
		Iterator<String> it = set.iterator();
		while(it.hasNext()){
			imageView.setImageDrawable(map.get(it.next()));
		}
		
		
		return imageView;
	}

}
