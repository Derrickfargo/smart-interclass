package cn.com.incito.classroom.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.R.layout;
import cn.com.incito.classroom.vo.Student;

public class RandomGroupAdapter extends BaseAdapter {
	
	private Context context;
	private List<Student> studentList;
	
	public  RandomGroupAdapter(Context context,List<Student> studentList) {
		this.context = context;
		this.studentList = studentList;
	}

	@Override
	public int getCount() {
		return studentList.size();
	}

	@Override
	public Object getItem(int position) {
		return studentList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Student student = (Student) getItem(position);
		ViewHolder viewHolder = null;
		
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_group_mem, null);
			viewHolder = new ViewHolder();
			viewHolder.stduent_text = (TextView) convertView.findViewById(R.id.tv_num_name);
			viewHolder.rlayout = (RelativeLayout) convertView
					.findViewById(R.id.rlayout);
			viewHolder.rlayout.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 120));
			
			convertView.setTag(viewHolder);;
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(student != null){
			if(1 == student.getSex()){
				if(student.isLogin()){
					viewHolder.rlayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_logged_user_m_hover));
				}else{
					viewHolder.rlayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_not_logged_user_m_hover));
				}
			}else{
				if(student.isLogin()){
					viewHolder.rlayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_logged_user_w));
				}else{
					viewHolder.rlayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_not_logged_user_w));

				}
			}
			viewHolder.stduent_text.setText(student.getName());
		}
		return convertView;
	}
	
	private class ViewHolder{
		TextView stduent_text;
		RelativeLayout rlayout;
	}
	
	public void setData(List<Student> studentList){
		this.studentList = studentList;
		notifyDataSetInvalidated();
		notifyDataSetChanged();
	}

}
