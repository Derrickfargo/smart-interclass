package cn.com.incito.classroom.ui.activity;

import java.util.List;

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
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.Device;
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.classroom.vo.Student;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

public class WaitForOtherMembersActivity extends Activity implements OnClickListener{
	
	
	private Button btn_back;
	private ImageView image_group_icon;
	private TextView text_group_name;
	private TextView text_group_members_name;
	private Button btn_waiting;
	private Group group;
	
	
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
		btn_waiting.setOnClickListener(this);
		
		//获取传递过来的数据
		Intent intent = getIntent();
		String data = intent.getStringExtra("group");
		JSONObject jsonObject = JSONObject.parseObject(data);
		
		List<Group> groupList = (List<Group>) jsonObject.get("data");
		
		for(int i = 0; i < groupList.size(); i++){
			List<Device> devices = groupList.get(i).getDevices();
			for(int j = 0; j < devices.size(); j++){
				if(devices.get(j).getImei().equals(MyApplication.deviceId)){
					group = groupList.get(i);
					break;
				}
			}
		}
		
		
		image_group_icon = (ImageView) findViewById(R.id.image_group_icon);
		image_group_icon.setImageResource(group.getLogo());
		
		text_group_name = (TextView) findViewById(R.id.text_gourp_name);
		text_group_name.setText(group.getName());
		
		text_group_members_name = (TextView) findViewById(R.id.text_group_members_name);
		
		//循环显示学生姓名
		StringBuilder sb = new StringBuilder();
		List<Student> students = group.getStudents();
		for(int i = 0; i < students.size(); i++){
			sb.append(students.get(i).getName() + "\t");
		}
		
		text_group_members_name.setText(sb.toString());
	}
	
	/**
	 * 启动自身的方法
	 * @param context    从哪个地方启动
	 * @param groupName  小组名称
	 * @param iconSourceId   小组图标Id
	 */
	public static void startSelf(Context context,String group){
		
		Intent intent = new Intent(context,WaitForOtherMembersActivity.class);
		intent.putExtra("group", group);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("groupId", group.getId());
			
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_DELETE);
			messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(jsonObject.toJSONString()));
			CoreSocket.getInstance().sendMessage(messagePacking);
			MyApplication.Logger.debug("删除小组");
			
			break;
		case R.id.btn_waiting:
			JSONObject json = new JSONObject();
			json.put("group", group);
			
			MessagePacking message = new MessagePacking(Message.MESSAGE_GROUP_CONFIRM);
			message.putBodyData(DataType.INT,BufferUtils.writeUTFString(json.toJSONString()));
			CoreSocket.getInstance().sendMessage(message);
			
			MyApplication.Logger.debug("停止其他小组成员添加");

		default:
			break;
		}
	}
}
