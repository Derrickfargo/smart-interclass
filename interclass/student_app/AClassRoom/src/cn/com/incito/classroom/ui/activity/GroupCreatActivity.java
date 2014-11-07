package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
import cn.com.incito.classroom.vo.Group;
import cn.com.incito.classroom.vo.Student;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

@SuppressLint("Recycle")
public class GroupCreatActivity extends BaseActivity implements OnClickListener {

	private EditText edit_group_name;
	private GridView grid_group_icon;
	private Button btn_back;
	private Button btn_create_group_ok;
	private List<Map<String,Drawable>> iconResourceIdList = new ArrayList<Map<String,Drawable>>();
	private ImageView historyView;                		//上次被点击的图标的位置
	private TypedArray groupIcons;
	private String logo = "";

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
		
		groupIcons = getResources().obtainTypedArray(R.array.groupIcons);
		String[] iconsName = getResources().getStringArray(R.array.groupicons_name);
		
		for (int i = 0; i < iconsName.length; i++) {
			Map<String,Drawable> map=new HashMap<String,Drawable>();
			map.put(iconsName[i], groupIcons.getDrawable(i));
			iconResourceIdList.add(map);
		}
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
				
				Map<String,Drawable> map = iconResourceIdList.get(position);
				Iterator<String> it = map.keySet().iterator();
				while(it.hasNext()){
					logo = it.next();
				}
				historyView.setBackgroundResource(R.drawable.bg_ico_group_hover);
			}
		});
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_create_group_ok:
			String group_name = edit_group_name.getText().toString();
			if(TextUtils.isEmpty(group_name) || group_name.length() < 2){
				ToastHelper.showCustomToast(this, "请输入组名");
				return;
			}
			if(TextUtils.isEmpty(logo)){
				ToastHelper.showCustomToast(this, "请选择小组图标");
				return;
			}
			
			//封装一个group对象
			Group group = new Group();
			group.setName(group_name);
			group.setLogo(logo);
			Student student = MyApplication.getInstance().getStudent();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("student", student);
			jsonObject.put("group", group);
		
			MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_GROUP_SUBMIT);
			messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(jsonObject.toJSONString()));
			MyApplication.Logger.debug("发送创建分组请求："+jsonObject.toJSONString());
			CoreSocket.getInstance().sendMessage(messagePacking);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		groupIcons.recycle();
	}

}
