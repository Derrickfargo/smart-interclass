package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.GroupIconImageAdapter;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.Device;
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

public class GroupCreatActivity extends BaseActivity implements OnClickListener {

	private EditText edit_group_name;
	private GridView grid_group_icon;
	private Button btn_back;
	private Button btn_create_group_ok;
	private List<Integer> iconResourceIdList;
	private int iconResourceId = -1;  					//用于标记哪个图标被选择 的图标的资源编号 默认值哪个都不选中
	private ImageView historyView;                		//上次被点击的图标的位置

	private GroupIconImageAdapter groupIconAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.creat_group_activity);

		initView();
	}

	public static void startSelf(Context context) {
		Intent intent = new Intent(context, GroupCreatActivity.class);
		context.startActivity(intent);
	}

	private void initView() {
		edit_group_name = (EditText) findViewById(R.id.edit_group_name);
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		
		btn_create_group_ok = (Button) findViewById(R.id.btn_create_group_ok);
		btn_create_group_ok.setOnClickListener(this);

		grid_group_icon = (GridView) findViewById(R.id.grid_gourp_icon);

		setIconData();

		groupIconAdapter = new GroupIconImageAdapter(this, iconResourceIdList);

		grid_group_icon.setAdapter(groupIconAdapter);

		// 设置某一项的点击 及该项被选中
		grid_group_icon.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(historyView != null){
					historyView.setBackgroundResource(R.drawable.bg_ico_group);
				}
				historyView = (ImageView) view;
				iconResourceId = iconResourceIdList.get(position);
				historyView.setBackgroundResource(R.drawable.bg_ico_group_hover);
				
			}
		});
	}

	// 设置图标数据
	private void setIconData() {
		iconResourceIdList = new ArrayList<Integer>();
		iconResourceIdList.add(R.drawable.badges_support);
		iconResourceIdList.add(R.drawable.badges_support);
		iconResourceIdList.add(R.drawable.badges_support);
		iconResourceIdList.add(R.drawable.badges_support);
		iconResourceIdList.add(R.drawable.badges_support);
		iconResourceIdList.add(R.drawable.badges_support);
		iconResourceIdList.add(R.drawable.badges_support);
		iconResourceIdList.add(R.drawable.badges_support);
		iconResourceIdList.add(R.drawable.badges_support);
		iconResourceIdList.add(R.drawable.badges_support);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_create_group_ok:
			//TODO 如果没有选择图标与小组名称需要进行判断
			String group_name = edit_group_name.getText().toString();
			if(TextUtils.isEmpty(group_name)){
				ToastHelper.showCustomToast(this, "请输入组名");
				return;
			}
			if(iconResourceId == -1){
				ToastHelper.showCustomToast(this, "请选择小组图标");
				return;
			}
			
			
			//封装一个group对象
			Group group = new Group();
			group.setName(group_name);
			group.setLogo(iconResourceId);
			List<Device> devices = new ArrayList<Device>();
			Device device = new Device();
			device.setImei(MyApplication.deviceId);
			devices.add(device);
			group.setDevices(devices);
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("group", group);
			
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_CREAT);
			messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(jsonObject.toJSONString()));
			CoreSocket.getInstance().sendMessage(messagePacking);
			
			MyApplication.Logger.debug("发送创建分组请求");
			
			//返回的group带到下一个界面
//			WaitForOtherMembersActivity.startSelf(this, edit_group_name
//					.getText().toString(), iconResourceId);
			break;

		default:
			break;
		}
	}

}
