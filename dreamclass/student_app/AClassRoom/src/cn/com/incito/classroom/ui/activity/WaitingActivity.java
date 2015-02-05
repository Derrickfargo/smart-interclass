package cn.com.incito.classroom.ui.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.GroupNumAdapter;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.utils.Utils;
import cn.com.incito.classroom.vo.LoginReqVo;
import cn.com.incito.classroom.vo.LoginRes2Vo;
import cn.com.incito.classroom.vo.LoginResVo;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.util.SendMessageUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 用户登录等待界面 Created by popoy on 2014/7/28.
 */

public class WaitingActivity extends BaseActivity {
	public static final int STUDENT_LIST = 1;
	public static final int STUDENT_LOGIN = 2;
	public static final int STUDENT_OFFLINE = 3;

	
	public int itemPosition;
	private EditText et_stname;
	private EditText et_stnumber;
	private ImageButton btn_join;
	private RadioGroup gender_group;
	private RadioButton female;
	private RadioButton male;
	private GridView gv_group_member;
	private LinearLayout llayout1;
	private LinearLayout llayout;
	private List<LoginRes2Vo> loginResList;
	private GroupNumAdapter mAdapter;
	private InputMethodManager imm;
	private RelativeLayout join_relative;
	private TextView join_text_notice;
	
	private WaitingActivityHandler handler;
	
	/**
	 * 0只显示增加按钮，1显示姓名2显示姓名、学号、性别
	 */
	private int addState = 0;
	

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setContentView(R.layout.waiting);
		handler = new WaitingActivityHandler(this);
		UIHelper.getInstance().setWaitingActivity(this);
		initViews();
		initListener();
		loginResList = new ArrayList<LoginRes2Vo>();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mAdapter = new GroupNumAdapter(WaitingActivity.this);
		et_stnumber.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		SendMessageUtil.sendGroupList();
	}

	private void initViews() {
		et_stnumber = (EditText) findViewById(R.id.et_stnumber);
		btn_join = (ImageButton) findViewById(R.id.btn_join);
		gender_group = (RadioGroup) findViewById(R.id.gender_group);
		female = (RadioButton) findViewById(R.id.female);
		male = (RadioButton) findViewById(R.id.male);
		gv_group_member = (GridView) findViewById(R.id.gv_group_member);
		llayout1 = (LinearLayout) findViewById(R.id.llayout1);
		llayout = (LinearLayout) findViewById(R.id.llayout);
		et_stname = (EditText) findViewById(R.id.et_stname);
		join_relative = (RelativeLayout) findViewById(R.id.join_relativelayout);
		join_text_notice = (TextView) findViewById(R.id.text_notice);
		join_relative.setPaddingRelative(200, 0, 0, 0);
	}

	private void initListener() {
		btn_join.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (addState == 0) {
					llayout1.setAnimation(AnimationUtils.loadAnimation(WaitingActivity.this, R.anim.push_bottom_in));
					gender_group.clearCheck();
					llayout1.setVisibility(View.VISIBLE);
					join_text_notice.setVisibility(View.GONE);
					join_relative.setPaddingRelative(0, 0, 0, 0);
					addState = 1;
				} else {
					if (validate()) {
						addState = 0;
						LoginRes2Vo groupNumberListRes = new LoginRes2Vo();
						groupNumberListRes.setSex(male.isChecked() ? "1" : "2");
						groupNumberListRes.setName(et_stname.getText().toString());
						groupNumberListRes.setNumber(et_stnumber.getText().toString());
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
						showProgress(R.string.load_dialog_default_text);
						registerStudent();
						et_stnumber.setText("");
						et_stname.setText("");
						llayout1.setVisibility(View.GONE);
						join_text_notice.setVisibility(View.VISIBLE);
						join_relative.setPaddingRelative(200, 0, 0, 0);
					}
				}
			}
		});
		gv_group_member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView,View view, int position, long l) {
				itemPosition = position;
				showProgress(R.string.load_dialog_default_text);
				if (loginResList.get(position).isLogin() == false) {
					login(loginResList.get(position).getName(),
					loginResList.get(position).getNumber(),
					loginResList.get(position).getSex());
				} else {
					logout(loginResList.get(position).getName(),
					loginResList.get(position).getNumber(),
					loginResList.get(position).getSex());
				}
			}
		});
	}
	
	/**
	 * 学生登录
	 * @param name
	 * @param number
	 * @param sex
	 */
	private void login(String name, String number, String sex) {
		LoginReqVo loginReqVo = new LoginReqVo();
		loginReqVo.setName(name);
		loginReqVo.setNumber(number);
		loginReqVo.setSex(sex);
		loginReqVo.setType("0");
		sendMessage(loginReqVo);
	}

	/**
	 * 取消登陆
	 * @param name
	 * @param number
	 * @param sex
	 */
	private void logout(String name, String number, String sex) {
		LoginReqVo loginReqVo = new LoginReqVo();
		loginReqVo.setName(name);
		loginReqVo.setNumber(number);
		loginReqVo.setSex(sex);
		loginReqVo.setType("1");
		sendMessage(loginReqVo);
	}
	
	/**
	 * 学生注册
	 */
	private void registerStudent() {
		LoginReqVo loginReqVo = new LoginReqVo();
		loginReqVo.setName(et_stname.getText().toString());
		loginReqVo.setNumber(et_stnumber.getText().toString());
		loginReqVo.setSex(female.isChecked() ? "2" : "1");
		loginReqVo.setType("2");
		sendMessage(loginReqVo);
	}

	/**
	 * 发送学生信息
	 * @param loginReqVo
	 */
	private void sendMessage(LoginReqVo loginReqVo) {
		loginReqVo.setImei(MyApplication.getInstance().getDeviceId());
		SendMessageUtil.sendStudentInfo(JSON.toJSONString(loginReqVo));
	}

	/**
	 * 检查新增的学生是否为重复录入(客户端检查)
	 */
	private boolean validate() {
		String stName = et_stname.getText().toString();
		String stNumber = et_stnumber.getText().toString();
		if (TextUtils.isEmpty(stName)) {
			ToastHelper.showCustomToast(getApplicationContext(),R.string.toast_stname_notnull);
			return false;
		}
		if(stName.contains(" ")){
			ToastHelper.showCustomToast(getApplicationContext(),R.string.toast_not_space);
			return false;
		}else if (stName.length() < 2) {
			ToastHelper.showCustomToast(getApplicationContext(),R.string.toast_stname_tooshort);
			return false;
		}
		if (TextUtils.isEmpty(stNumber)) {
			ToastHelper.showCustomToast(getApplicationContext(),R.string.toast_stnumber_notnull);
			return false;
		}
		if (loginResList.size() > Constants.STUDENT_MAX_NUM) {
			ToastHelper.showCustomToast(getApplicationContext(),R.string.toast_group_isfull);
			return false;
		}
		for (int i = 0; i < loginResList.size(); i++) {
			if (stNumber.equals(loginResList.get(i).getNumber())) {
				String msg = getResources().getString(R.string.toast_stname_repeat);
				ToastHelper.showCustomToast(getApplicationContext(),String.format(msg, loginResList.get(i).getNumber()));
				return false;
			}
		}
		if (!Utils.isNumberOrChinese(stName)) {
			ToastHelper.showCustomToast(getApplicationContext(),R.string.tost_name_is_not_english_chinese);
			return false;
		}
		// 判断是否已经选择男女
		if (!male.isChecked() && !female.isChecked()) {
			ToastHelper.showCustomToast(getApplicationContext(),R.string.toast_choose_sex);
			return false;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (addState == 1) {
			llayout1.setVisibility(View.GONE);
			join_text_notice.setVisibility(View.VISIBLE);
			join_relative.setPaddingRelative(200, 0, 0, 0);
			addState = 0;
			imm.hideSoftInputFromWindow(llayout.getWindowToken(), 0);
			return false;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 更新小组成员
	 * @param jsonObject
	 * @param type
	 */
	public void doResult(JSONObject jsonObject, int type) {
		android.os.Message message = new android.os.Message();
		message.what = type;
		Bundle data = new Bundle();
		data.putSerializable("data", jsonObject);
		message.setData(data);
		handler.sendMessage(message);
	}
	
	/**
	 * 本台pad掉线时
	 * 改变本台pad所有学生的状态
	 */
	public void notifyStudentOffline() {
		List<LoginRes2Vo> students = MyApplication.getInstance().getLoginResVo().getStudents();
		if(students != null){
			for(LoginRes2Vo s : students){
				s.setLogin(false);
			}
		}
		handler.sendEmptyMessage(STUDENT_OFFLINE);
	}
	
	/**
	 * 防止内存泄漏的handler
	 * @author hm
	 */
	private static class WaitingActivityHandler extends Handler{
		
		private WeakReference<WaitingActivity> weak;
		
		public WaitingActivityHandler(WaitingActivity waitingActivity) {
			this.weak = new WeakReference<WaitingActivity>(waitingActivity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			WaitingActivity activity = this.weak.get();
			switch (msg.what) {
			case STUDENT_LIST:
				activity.closeProgress();
				JSONObject json = (JSONObject) msg.getData().getSerializable("data");
				if ("-2".equals(json.getString("code"))) {
					String number = json.getString("data");
					ToastHelper.showCustomToast(activity, "学号"+ number + "已注册");
					return;
				}
				if ("0".equals(json.getString("code")) && json.getJSONObject("data") != null) {
					LoginResVo loginResVo = JSON.parseObject(json.getJSONObject("data").toJSONString(),LoginResVo.class);
					if (loginResVo.getStudents() != null) {
						activity.loginResList = loginResVo.getStudents();
					}
					MyApplication.getInstance().setLoginResVo(loginResVo);
					if (activity.loginResList != null && activity.loginResList.size() > 0) {
						activity.mAdapter.setDatas(activity.loginResList);
						activity.gv_group_member.setAdapter(activity.mAdapter);
					}
					activity.llayout1.setVisibility(View.GONE);
				} 
				break;
			case STUDENT_OFFLINE:
				activity.loginResList = MyApplication.getInstance().getLoginResVo().getStudents();
				activity.mAdapter.setDatas(MyApplication.getInstance().getLoginResVo().getStudents());
				activity.gv_group_member.setAdapter(activity.mAdapter);
				break;
			}
		}
	}
}
