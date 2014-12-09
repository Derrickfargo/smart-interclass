package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import android.R.integer;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;


public class EvaluateActivity extends BaseActivity implements OnClickListener{
	private Button button_one;
	private Button button_two;
	private Button button_three;
	private Button button_four;
	private Button button_five;
	private Button button_confirm;
	private Button pager_up;
	private Button pager_down;
	private Button number;
	List<PaperVo> paperList=new ArrayList<PaperVo>();
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setContentView(R.layout.evaluate_activity);
		UIHelper.getInstance().setEvaluateActivity(this);
		initView();
		initData();
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
		for (int i = 0; i < 6; i++) {
			
		}
		
	}
	/**
	 * 初始化界面
	 */
	private void initView() {
		pager_up=(Button)findViewById(R.id.pager_up);
		pager_up.setOnClickListener(this);
		pager_down=(Button)findViewById(R.id.pager_down);
		pager_down.setOnClickListener(this);
		button_one=(Button)findViewById(R.id.number_one);
		button_one.setOnClickListener(this);
		button_two=(Button)findViewById(R.id.number_two);
		button_two.setOnClickListener(this);
		button_three=(Button)findViewById(R.id.number_three);
		button_three.setOnClickListener(this);
		button_four=(Button)findViewById(R.id.number_four);
		button_four.setOnClickListener(this);
		button_five=(Button)findViewById(R.id.number_five);
		button_five.setOnClickListener(this);
		button_confirm=(Button)findViewById(R.id.btn_confirm);
		button_confirm.setOnClickListener(this);
		number=(Button)findViewById(R.id.number);
	}
	@Override
	public void onClick(View v) {
		int viewID=v.getId();
		switch (viewID) {
		case R.id.pager_up:
			break;
		case R.id.pager_down:
			break;
		case R.id.number_one:
			number.setText("1");
			break;
		case R.id.number_two:
			number.setText("2");
			break;
		case R.id.number_three:
			number.setText("3");
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * 发送评价结果
	 */
	public void sendScore(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("imei", MyApplication.deviceId);
		jsonObject.put("feedback", "作业部分json id是quizId和score是分数");
		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_EVALUATE);
		messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(jsonObject.toJSONString()));
		CoreSocket.getInstance().sendMessage(messagePacking);
	}
	
	 class PaperVo{
		 Bitmap paperBitmap;
		 int Score;
		 String quizId;
		 
	}
}
