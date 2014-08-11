package cn.com.incito.classroom.adapter;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.utils.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HorizontalListViewAdapter extends BaseAdapter{
        private static final String TAG = "HorizontalListViewAdapter";
	private int[] mIconIDs;
	private Context mContext;
	private LayoutInflater mInflater;
	private Bitmap iconBitmap;
	private int selectIndex = -1;
	private TypedArray mGroupIcons;
	private String[] mGroupIconsName;

	public HorizontalListViewAdapter(Context context, int[] ids){
		this.mContext = context;
		this.mIconIDs = ids;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public HorizontalListViewAdapter(Context context, TypedArray groupIcons,String[] groupIconsName){
		mContext = context;
		mGroupIcons = groupIcons;
		mGroupIconsName = groupIconsName;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		if(mIconIDs != null)
			return mIconIDs.length;
		else
			return mGroupIcons.length();
	}
	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.horizontal_list_item, null);
			holder.mImage=(ImageView)convertView.findViewById(R.id.img_list_item);
			holder.mTitle = mGroupIconsName[position];
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		if(position == selectIndex){
			convertView.setSelected(true);
		}else{
			convertView.setSelected(false);
		}
		if(mIconIDs != null)
			iconBitmap = getPropThumnail(mIconIDs[position]);
		else
			iconBitmap = getPropThumnail(mGroupIcons.getResourceId(position, -1));
		holder.mImage.setImageBitmap(iconBitmap);

		return convertView;
	}

	public static class ViewHolder {
		public String mTitle ;
		public ImageView mImage;
	}
	private Bitmap getPropThumnail(int id){
		Drawable d = mContext.getResources().getDrawable(id);
		Bitmap b = Utils.drawableToBitmap(d);
		int w = mContext.getResources().getDimensionPixelOffset(R.dimen.thumnail_default_width);
		int h = mContext.getResources().getDimensionPixelSize(R.dimen.thumnail_default_height);
		Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);
		return thumBitmap;
	}

	public void setSelectIndex(int i){
		selectIndex = i;
	}
}
