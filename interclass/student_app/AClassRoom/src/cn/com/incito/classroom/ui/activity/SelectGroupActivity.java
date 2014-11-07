package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.SelectGroupAdapter;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.classroom.vo.Student;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class SelectGroupActivity extends BaseActivity implements
		OnClickListener {

	private Button btn_create_group;
	private ListView select_group_listview;
	private SelectGroupAdapter selectGroupAdapter;
	private List<Group> groupList = new ArrayList<Group>();
	private TextView text_empty;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
//			if(groupList == null || groupList.size() == 0){
				//没有小组出现提示信息
				selectGroupAdapter.setGroupList(groupList);
//			}else{
//				selectGroupAdapter.setGroupList(groupList);
//			}
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_group);
		UIHelper.getInstance().setmSelectGroupActivity(this);
		if (getIntent().getStringExtra("group") != null && !"".equals(getIntent().getStringExtra("group"))){
			groupList = JSON.parseArray(getIntent().getStringExtra("group"),Group.class);
		}
		// 初始化UI组件
		initViews();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_create_group:
			GroupCreatActivity.startSelf(this);
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化UI组件
	 */
	private void initViews() {
		
		text_empty = (TextView) findViewById(R.id.text_empty);
		btn_create_group = (Button) findViewById(R.id.btn_create_group);
		btn_create_group.setOnClickListener(this);
		select_group_listview = (ListView) findViewById(R.id.select_group_listview);
		// 设置每项点击事件 发送请求 并且更新界面
		select_group_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 1.封装要发送的数据 小组ID 学生ID
				Student me = MyApplication.getInstance().getStudent();
				// 2.判断当前设备是否已经加入该分组
				Group group = groupList.get(position);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("groupId", group.getCaptainid());
				jsonObject.put("student", me);
				MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_JOIN);
				messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(jsonObject.toJSONString()));
				MyApplication.Logger.debug(System.currentTimeMillis()+ "开始发送加入请求: " + jsonObject.toJSONString());
				CoreSocket.getInstance().sendMessage(messagePacking);

			}
		});

		selectGroupAdapter = new SelectGroupAdapter(this, groupList);
		select_group_listview.setAdapter(selectGroupAdapter);
		select_group_listview.setEmptyView(text_empty);

	}

	public void setData(List<Group> groupList) {
		this.groupList = groupList;
		handler.sendEmptyMessage(0);

	}
}
