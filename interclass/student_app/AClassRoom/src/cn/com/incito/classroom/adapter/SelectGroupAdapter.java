package cn.com.incito.classroom.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.ui.activity.SelectGroupActivity;
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.classroom.vo.Student;

public class SelectGroupAdapter extends BaseAdapter {
	
	private SelectGroupActivity activity;
	
	public SelectGroupAdapter(Context context){
		this.activity = (SelectGroupActivity) context;
	}

	@Override
	public int getCount() {
		return activity.getGroupList().size();
	}

	@Override
	public Object getItem(int position) {
		return activity.getGroupList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Group group = (Group) getItem(position);
		
		ViewHolder viewHolder;
		
		if(convertView == null){
			convertView = LayoutInflater.from(activity).inflate(R.layout.item_select_group, null);
			
			viewHolder = new ViewHolder();
			
			viewHolder.text_name = (TextView) convertView.findViewById(R.id.text_group_name);
			viewHolder.img_group_icon = (ImageView) convertView.findViewById(R.id.img_group_icon);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		List<Student> memberNames = group.getStudents();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < memberNames.size(); i++){
			sb.append(memberNames.get(i) + " ");
		}
		
		//设置小组图标 
//		viewHolder.img_group_icon.setBackgroundResource(group.getIconSourceId());
		//设置小组成员以及名称 
		viewHolder.text_name.setText( Html.fromHtml(group.getName()+"<br><font color='#ecf2fc'> "+ sb.toString() + "</font>"));
		
		return convertView;
	}
	
	private class ViewHolder{
		TextView text_name;
		ImageView img_group_icon;
	}

}
