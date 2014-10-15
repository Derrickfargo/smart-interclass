package cn.com.incito.classroom.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;

/**
 * @author lgm
 * 加入小组界面
 */
public class GroupJoinActvity extends BaseActivity implements OnClickListener{
	/**
	 * 加入按钮
	 */
	private View GroupCreatButton;
	private ListView mListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join_group_activity);
		initView();
	}
	public void initView(){
		 GroupCreatButton=findViewById(R.id.creat_group);
		 GroupCreatButton.setOnClickListener(this);
		 mListView=(ListView)findViewById(R.id.listview);
		 MyAdapter myAdapter=new MyAdapter();
		 mListView.setAdapter(myAdapter);
		 mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.creat_group://创建小组按钮
			break;
		default:
			break;
		}
	}
	
	class MyAdapter extends BaseAdapter{
		
		private LayoutInflater mInflater;

		public MyAdapter(){
			 mInflater = (LayoutInflater) GroupJoinActvity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			  final ViewHolder holder;
		        if (convertView == null) {
		            convertView = mInflater.inflate(
		                    R.layout.join_group_activity_item, parent, false);
		            holder = new ViewHolder();
		            holder.group_image=(ImageView) convertView.findViewById(R.id.group_image);
		            holder.group_name = (TextView) convertView.findViewById(R.id.group_name);
		            holder.group_member = (TextView) convertView.findViewById(R.id.group_person);
		            convertView.setTag(holder);
		        } else {
		            holder = (ViewHolder) convertView.getTag();
		        }
//		        if (position == curPos) {
//		            holder.iv_num_bg.setBackgroundResource(R.drawable.bg_desk_num_hover);
//		        } else {
//		            holder.iv_num_bg.setBackgroundResource(R.drawable.bg_desk_num_nomal);
//		        }
		          holder.group_name.setText("123");
		          holder.group_member.setText("123,123,123,123,123");
		          holder.group_image.setBackground(getResources().getDrawable(R.drawable.badges_diamond));
		        return convertView;
		}
		
	}
	 static class ViewHolder {

		/**
		 * 小组图标
		 */
		public ImageView group_image;
		/**
		 * 小组成员
		 */
		public TextView group_member;
		/**
		 * 小组姓名
		 * 
		 */
		public TextView group_name;
	    }
}
