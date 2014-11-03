package cn.com.incito.classroom.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.incito.classroom.R;

public class WaitForOtherMembersActivity extends Activity implements OnClickListener{
	
	
	private Button btn_back;
	private ImageView image_group_icon;
	private TextView text_group_name;
	private TextView text_group_members_name;
	private Button btn_waiting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_wait_for_other_members);
		
		
	
		 
		initView();
	}
	
	private void initView(){
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		
		btn_waiting = (Button) findViewById(R.id.btn_waiting);
		
		//获取传递过来的数据
		Intent intent = getIntent();
		int iconSrourceId = intent.getIntExtra("iconSrourceId", 0);
		String groupName = intent.getStringExtra("groupName");
		
		image_group_icon = (ImageView) findViewById(R.id.image_group_icon);
		image_group_icon.setImageResource(iconSrourceId);
		
		text_group_name = (TextView) findViewById(R.id.text_gourp_name);
		text_group_name.setText(groupName);
		
		text_group_members_name = (TextView) findViewById(R.id.text_group_members_name);
	}
	
	/**
	 * 启动自身的方法
	 * @param context    从哪个地方启动
	 * @param groupName  小组名称
	 * @param iconSourceId   小组图标Id
	 */
	public static void startSelf(Context context,String groupName,int iconSourceId){
		
		Intent intent = new Intent(context,WaitForOtherMembersActivity.class);
		intent.putExtra("groupName", groupName);
		intent.putExtra("iconSrourceId", iconSourceId);
		
		context.startActivity(intent);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;

		default:
			break;
		}
	}
}
