package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.SelectGroupAdapter;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.Device;
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

public class SelectGroupActivity extends BaseActivity implements
		OnClickListener {

	private Button btn_create_group;
	private ListView select_group_listview;
	private SelectGroupAdapter selectGroupAdapter;
	private List<Group> groupList = new ArrayList<Group>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_group);

		if (getIntent().getStringExtra("group") != null
				&& !"".equals(getIntent().getStringExtra("group")))
			setGroupData(getIntent().getStringExtra("group"));
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

		btn_create_group = (Button) findViewById(R.id.btn_create_group);
		btn_create_group.setOnClickListener(this);

		select_group_listview = (ListView) findViewById(R.id.select_group_listview);

		// 设置每项点击事件 发送请求 并且更新界面
		select_group_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 1.封装要发送的数据 小组ID 学生ID
				int groupId = groupList.get(position).getId();
				String deviceId = MyApplication.deviceId;

				// 2.判断当前设备是否已经加入该分组
				for (int i = 0; i < groupList.size(); i++) {
					Group group = groupList.get(i);
					List<Device> deviceList = group.getDevices();
					for (int j = 0; i < deviceList.size(); j++) {
						Device device = deviceList.get(j);
						if (!device.getImei().equals(deviceId)) {// 没有加入分组才发送请求
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("groupId", groupId);
							jsonObject.put("imei", deviceId);

							MessagePacking messagePacking = new MessagePacking(
									Message.MESSAGE_GROUP_JOIN);
							messagePacking.putBodyData(DataType.INT,
									BufferUtils.writeUTFString(jsonObject
											.toJSONString()));
							CoreSocket.getInstance()
									.sendMessage(messagePacking);

							MyApplication.Logger.debug(System
									.currentTimeMillis() + "开始发送加入请求 ");
						}
					}
				}

			}
		});

		selectGroupAdapter = new SelectGroupAdapter(this);
		selectGroupAdapter.notifyDataSetChanged();

		select_group_listview.setAdapter(selectGroupAdapter);

	}

	public List<Group> getGroupList() {

		return groupList;
	}

	/**
	 * 设置数据
	 */
	@SuppressWarnings("unchecked")
	private void setGroupData(String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		groupList = (List<Group>) jsonObject.get("data");

	}
}
