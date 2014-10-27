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
import cn.com.incito.classroom.vo.Group;

public class SelectGroupAdapter extends BaseAdapter {
	
	private Context context;
	private List<Group> groupList;
	
	public SelectGroupAdapter(Context context,List<Group> groupList){
		this.context = context;
		this.groupList = groupList;
	}

	@Override
	public int getCount() {
		return groupList.size();
	}

	@Override
	public Object getItem(int position) {
		return groupList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Group group = groupList.get(position);
		
		ViewHolder viewHolder;
		
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_select_group, null);
			
			viewHolder = new ViewHolder();
			
			viewHolder.text_name = (TextView) convertView.findViewById(R.id.text_group_name);
			viewHolder.img_group_icon = (ImageView) convertView.findViewById(R.id.img_group_icon);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		List<String> memberNames = group.getMemberNames();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < memberNames.size(); i++){
			sb.append(memberNames.get(i) + " ");
		}
		
		viewHolder.text_name.setText( Html.fromHtml(group.getName()+"<br><font color='#ecf2fc'> "+ sb.toString() + "</font>"));
		
		return convertView;
	}
	
	private class ViewHolder{
		TextView text_name;
		ImageView img_group_icon;
		
	}

}
