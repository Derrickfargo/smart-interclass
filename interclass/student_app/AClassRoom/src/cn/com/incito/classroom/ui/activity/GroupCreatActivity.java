package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.GroupIconImageAdapter;
import cn.com.incito.classroom.base.BaseActivity;

public class GroupCreatActivity extends BaseActivity implements OnClickListener{
	
	private EditText edit_group_name;
	private GridView grid_group_icon;
	private Button btn_back;
	private Button btn_create_group_ok;
	
	private GroupIconImageAdapter groupIconAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.creat_group_activity);
		
		initView();
	}
	
	public static void startSelf(Context context){
		Intent intent = new Intent(context,GroupCreatActivity.class);
		context.startActivity(intent);
	}
	
	private void initView(){
		edit_group_name = (EditText) findViewById(R.id.edit_group_name);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		btn_create_group_ok = (Button) findViewById(R.id.btn_create_group_ok);
		btn_create_group_ok.setOnClickListener(this);
		
		grid_group_icon = (GridView) findViewById(R.id.grid_gourp_icon);
		
		List<Integer> iconResourceIdList = new ArrayList<Integer>();
		iconResourceIdList.add(R.drawable.btn_bind_desk);
		iconResourceIdList.add(R.drawable.btn_bind_desk);
		iconResourceIdList.add(R.drawable.btn_bind_desk);
		iconResourceIdList.add(R.drawable.btn_bind_desk);
		iconResourceIdList.add(R.drawable.btn_bind_desk);
		iconResourceIdList.add(R.drawable.btn_bind_desk);
		iconResourceIdList.add(R.drawable.btn_bind_desk);
		iconResourceIdList.add(R.drawable.btn_bind_desk);
		iconResourceIdList.add(R.drawable.btn_bind_desk);
		iconResourceIdList.add(R.drawable.btn_bind_desk);
		
		groupIconAdapter = new GroupIconImageAdapter(this, iconResourceIdList);
		
		grid_group_icon.setAdapter(groupIconAdapter);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			
			break;
		case R.id.btn_create_group_ok:
			String groupName = edit_group_name.getText().toString();
			
			//TODO 跳转至哪个界面不清楚
			break;

		default:
			break;
		}
	}
	
}
