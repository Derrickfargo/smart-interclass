package cn.com.incito.classroom.ui.activity;

import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.classroom.vo.Student;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class WaitForOtherMembersActivity extends Activity implements
		OnClickListener {

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

	private void initView() {
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		btn_waiting = (Button) findViewById(R.id.btn_waiting);
		btn_waiting.setOnClickListener(this);

		// 获取传递过来的数据
		Intent intent = getIntent();
		String data = intent.getStringExtra("group");
		List<Group> groupList = JSON.parseArray(data, Group.class);
		Student student = MyApplication.getInstance().getStudent();
		
		for (int i = 0; i < groupList.size(); i++) {
			if(groupList.get(i).getStudents().contains(student)){
				this.group = groupList.get(i);
				break;
			}
		}
		image_group_icon = (ImageView) findViewById(R.id.image_group_icon);
		image_group_icon.setImageResource(R.drawable.ico_powder_blue);

		text_group_name = (TextView) findViewById(R.id.text_gourp_name);
		text_group_name.setText(group.getName());

		text_group_members_name = (TextView) findViewById(R.id.text_group_members_name);

		// 循环显示学生姓名
		StringBuilder sb = new StringBuilder();
		List<Student> students = group.getStudents();
		if (students != null && students.size() > 0) {
			for (int i = 0; i < students.size(); i++) {
				sb.append(students.get(i).getName() + "\t");
			}
		}
		text_group_members_name.setText(sb.toString());
	}
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				text_group_members_name.setText(msg.getData().get("studentNames").toString());
				break;

			default:
				break;
			}
		};
	};
	/**
	 * 设置界面数据
	 * @param students  该小组的成员
	 */
	public void setText(String data){
		StringBuilder sb = new StringBuilder();
//		if (students != null && students.size() > 0) {
//			for (int i = 0; i < students.size(); i++) {
//				sb.append(students.get(i).getName() + "\t");
//			}
//		}
		Bundle bundle = new Bundle();
		bundle.putString("studentNames", sb.toString());
		android.os.Message message = new android.os.Message();
		message.setData(bundle);
		message.what = 0;
		handler.sendMessage(message);
	}

	/**
	 * 启动自身的方法
	 * 
	 * @param context
	 *            从哪个地方启动
	 * @param groupName
	 *            小组名称
	 * @param iconSourceId
	 *            小组图标Id
	 */
	public static void startSelf(Context context, String group) {

		Intent intent = new Intent(context, WaitForOtherMembersActivity.class);
		intent.putExtra("group", group);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("studentId", group.getCaptainid());
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_DELETE);
			messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
			MyApplication.Logger.debug("删除小组信息："+jsonObject.toJSONString());
			CoreSocket.getInstance().sendMessage(messagePacking);
			break;
		case R.id.btn_waiting:
//			JSONObject json = new JSONObject();
//			json.put("group", group);
//
//			MessagePacking message = new MessagePacking(
//					Message.MESSAGE_GROUP_CONFIRM);
//			message.putBodyData(DataType.INT,
//					BufferUtils.writeUTFString(json.toJSONString()));
//			MyApplication.Logger.debug("停止其他小组成员添加："+json.toJSONString());
//			CoreSocket.getInstance().sendMessage(message);
			Intent mIntent=new Intent(this,ClassReadyActivity.class);
			this.startActivity(mIntent);
		default:
			break;
		}
	}
}
