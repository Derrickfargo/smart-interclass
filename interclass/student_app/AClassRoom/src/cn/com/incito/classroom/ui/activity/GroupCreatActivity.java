package cn.com.incito.classroom.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;

public class GroupCreatActivity extends BaseActivity{
	private GridView gridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.creat_group_activity);
		initView();
	}
	public void initView(){
		gridView=(GridView)findViewById(R.id.gridview);
		MyAdapter myAdapter=new MyAdapter();
		gridView.setAdapter(myAdapter);
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 0;
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
			return null;
		}
		
	}
}
