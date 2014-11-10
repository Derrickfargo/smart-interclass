package cn.com.incito.classroom.adapter;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.ui.activity.SelectGroupActivity;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.classroom.vo.Student;

@SuppressLint("Recycle")
public class SelectGroupAdapter extends BaseAdapter {

	private SelectGroupActivity activity;
	private List<Group> groupList = new ArrayList<Group>();
	private TypedArray groupIcons;
	private String[] iconsName;


	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
		this.notifyDataSetChanged();
		this.notifyDataSetInvalidated();
	}

	public SelectGroupAdapter(Context context, List<Group> groupList2) {
		this.activity = (SelectGroupActivity) context;
		this.groupList = groupList2;
		groupIcons = activity.getResources().obtainTypedArray(R.array.groupIcons);
		iconsName = activity.getResources().getStringArray(R.array.groupicons_name);
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

		Group group = (Group) getItem(position);

		ViewHolder viewHolder;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(
					R.layout.item_select_group, null);
			viewHolder.text_name = (TextView) convertView
					.findViewById(R.id.text_group_name);
			viewHolder.img_group_icon = (ImageView) convertView
					.findViewById(R.id.img_group_icon);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		List<Student> memberNames = group.getStudents();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < memberNames.size(); i++) {
			sb.append(memberNames.get(i).getName() + " ");
		}
		// 设置小组图标
		viewHolder.img_group_icon.setBackground(Utils.getGroupIconByName(groupIcons, iconsName, group.getLogo()));
		// 设置小组成员以及名称
		viewHolder.text_name.setText(Html.fromHtml(group.getName()
				+ "<br><font color='#ecf2fc'> " + sb.toString() + "</font>&nbsp"));
		return convertView;
	}

	private class ViewHolder {
		TextView text_name;
		ImageView img_group_icon;
	}

}
