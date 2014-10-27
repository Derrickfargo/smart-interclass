package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.SelectGroupAdapter;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.vo.Group;

public class SelectGroupActivity extends BaseActivity implements OnClickListener {
	
	private Button btn_create_group;
	private ListView select_group_listview;
	private SelectGroupAdapter selectGroupAdapter;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_group);
		
		//初始化UI组件
		initViews();
		
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_create_group:
				Intent intent = new Intent(this,GroupCreatActivity.class);
				startActivity(intent);			
			break;
		default:
			break;
		}
	}
	
	

	
	/**
	 * 初始化UI组件
	 */
	private void initViews(){
		
		btn_create_group = (Button) findViewById(R.id.btn_create_group);
		btn_create_group.setOnClickListener(this);
		
		select_group_listview = (ListView) findViewById(R.id.select_group_listview);
		
		//初始化假数据
		List<String> memberNames = new ArrayList<String>();
		memberNames.add("张三 ");
		memberNames.add("李四");
		memberNames.add("王五 ");
		
		Group g = new Group();
		g.setMemberNames(memberNames);
		g.setName("潜水小组");
		
		List<Group> gourpList = new ArrayList<Group>();
		gourpList.add(g);
		gourpList.add(g);
		gourpList.add(g);
		gourpList.add(g);
		gourpList.add(g);
		gourpList.add(g);
		gourpList.add(g);
		gourpList.add(g);
		gourpList.add(g);
		gourpList.add(g);
		gourpList.add(g);
		gourpList.add(g);
		
		selectGroupAdapter = new SelectGroupAdapter(this,gourpList);
		
		select_group_listview.setAdapter(selectGroupAdapter);
		
		
	}





	
	

}
