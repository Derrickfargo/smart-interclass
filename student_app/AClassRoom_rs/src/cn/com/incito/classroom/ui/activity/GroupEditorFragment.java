package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.constants.Constant;
import cn.com.incito.classroom.entry.IGroupInfo;

/**
 * 小组信息编辑页面
 * @author 陈正
 *
 */
public class GroupEditorFragment extends CommonFragment {
	private Button button_ok;
	private EditText editText_name ;
	private GridView gridView_icons; 
	private ArrayList<IIconItem> iconItems = new ArrayList<IIconItem>();
	private IGroupInfo groupInfo ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mContainerView = LayoutInflater.from(mActivity).inflate(R.layout.group_editor, null);
		initData();
		initViews();
		return mContainerView;
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		iconItems.clear();
		IIconItem iconItem ;
		
		iconItem = new IIconItem(R.drawable.icon);
		iconItems.add(iconItem);
		
		iconItem = new IIconItem(R.drawable.icon);
		iconItems.add(iconItem);
		
		iconItem = new IIconItem(R.drawable.icon);
		iconItems.add(iconItem);
		
		iconItem = new IIconItem(R.drawable.icon);
		iconItems.add(iconItem);
		
		iconItem = new IIconItem(R.drawable.icon);
		iconItems.add(iconItem);
		
		iconItem = new IIconItem(R.drawable.icon);
		iconItems.add(iconItem);
		
		iconItem = new IIconItem(R.drawable.icon);
		iconItems.add(iconItem);
		
		iconItem = new IIconItem(R.drawable.icon);
		iconItems.add(iconItem);
		
		iconItem = new IIconItem(R.drawable.icon);
		iconItems.add(iconItem);
		
		iconItem = new IIconItem(R.drawable.icon);
		iconItems.add(iconItem);
		
		iconItem = new IIconItem(R.drawable.icon);
		iconItems.add(iconItem);
		
		
		groupInfo = getArguments().getParcelable(Constant.GROUP_INTENT_BUNDLE_NAME);
		
	}
	
	/**
	 * 初始化UI
	 */
	private void initViews(){
		button_ok = (Button) mContainerView.findViewById(R.id.group_editor_ok);
		editText_name = (EditText)mContainerView.findViewById(R.id.group_editor_name);
		gridView_icons = (GridView)mContainerView.findViewById(R.id.group_editor_gridview);
		
		button_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				sure();
			}
		});
		
	
		
		if(groupInfo!=null){
			editText_name.setText(groupInfo.getGroupName());
		}
		
		
		IIconAdapter iconAdapter = new IIconAdapter();
		gridView_icons.setAdapter(iconAdapter);
	}
	
	/**
	 * 图标的每一项
	 */
	private class IIconItem{
		int resourceId ;
		String iconName;
		
		
		
		public IIconItem(int resourceId) {
			super();
			this.resourceId = resourceId;
		}
		public int getResourceId() {
			return resourceId;
		}
		public void setResourceId(int resourceId) {
			this.resourceId = resourceId;
		}
		public String getIconName() {
			return iconName;
		}
		public void setIconName(String iconName) {
			this.iconName = iconName;
		} 
		
		
		
	}
	

	private class IIconAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return iconItems.size();
		}

		@Override
		public Object getItem(int arg0) {
			return iconItems.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup viewGroup) {
			IIconHolder iconHolder ;
			IIconItem iconItem = iconItems.get(position);
			if(contentView==null){
				iconHolder = new IIconHolder();
				contentView = LayoutInflater.from(mActivity).inflate(R.layout.group_editor_gridview_item, null);
				iconHolder.imageView_icon = (ImageView) contentView.findViewById(R.id.group_editor_gridview_item_icon);
				contentView.setTag(iconHolder);
			}else {
				iconHolder = (IIconHolder)	contentView.getTag();
			}
			
			iconHolder.imageView_icon.setImageResource(iconItem.getResourceId());
			
			return contentView;
		}
		
		
		
	}
	
	private class IIconHolder{
		ImageView imageView_icon ;
		
	}
	
	/**
	 * 确定
	 */
	private void sure(){
		if(groupInfo==null)
			groupInfo = new IGroupInfo();
		
		groupInfo.setGroupName(editText_name.getText().toString().trim());
		
		
		if(mActivity instanceof GroupActivity){
			((GroupActivity)mActivity).skipToGroupInfoShowPage(groupInfo);
			
		}
		
		
	}
	
}
