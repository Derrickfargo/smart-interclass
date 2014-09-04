package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.GroupNumAdapter;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.vo.LoginReqVo;
import cn.com.incito.classroom.vo.LoginRes2Vo;
import cn.com.incito.classroom.vo.LoginResVo;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.sdk.log.WLog;
import cn.com.incito.wisdom.uicomp.widget.dialog.ProgressiveDialog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 用户登录等待界面 Created by popoy on 2014/7/28.
 */

public class WaitingActivity extends BaseActivity {
	public static final int STUDENT_LIST = 1;
	public static final int STUDENT_LOGIN = 2;
	EditText et_stname;
	EditText et_stnumber;
	ImageButton btn_join;
	RadioGroup gender_group;
	RadioButton female;
	RadioButton male;
	GridView gv_group_member;
	LinearLayout llayout1;
	LinearLayout llayout;
	List<LoginRes2Vo> loginResList;
	GroupNumAdapter mAdapter;
	InputMethodManager imm;
	private ProgressiveDialog mProgressDialog;
	protected long mExitTime;
	/**
	 * 0只显示增加按钮，1显示姓名2显示姓名、学号、性别
	 */
	private int addState = 0;

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setContentView(R.layout.waiting);
		UIHelper.getInstance().setWaitingActivity(this);
		mProgressDialog = new ProgressiveDialog(this);
		initViews();
		initListener();
		loginResList = new ArrayList<LoginRes2Vo>();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		mAdapter = new GroupNumAdapter(WaitingActivity.this);
		et_stnumber.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		getGroupUserList();
		// LockScreenReceiver mLockScreenReceiver = new LockScreenReceiver();
		// mLockScreenReceiver.registerScreenActionReceiver(this);
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
	}

	private void initListener() {
		btn_join.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (addState == 0) {
					llayout1.setAnimation(AnimationUtils.loadAnimation(
							WaitingActivity.this, R.anim.push_bottom_in));
					llayout1.setVisibility(View.VISIBLE);
					addState = 1;
				} else {
					if (validate()) {
						
						LoginRes2Vo groupNumberListRes = new LoginRes2Vo();
						groupNumberListRes.setSex(male.isChecked() ? "1" : "2");
						groupNumberListRes.setName(et_stname.getText()
								.toString());
						groupNumberListRes.setNumber(et_stnumber.getText()
								.toString());
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘

						registerStudent();
						mProgressDialog
								.setMessage(R.string.load_dialog_default_text);
						mProgressDialog.show();
						
					}

				}

			}
		});
		gv_group_member
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long l) {
//						addState = 0;
						mProgressDialog
								.setMessage(R.string.load_dialog_default_text);
						if(!mProgressDialog.isShowing()){
							mProgressDialog.show();
						}
						
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

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	public void onBackPressed() {
		showDialog();
	}

	public void showDialog() {
		new AlertDialog.Builder(this)
				.setMessage("确定退出？")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AppManager.getAppManager()
								.AppExit(WaitingActivity.this);
					}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	/**
	 * 与后台服务建立连接，并实现登陆
	 *
	 * @param name
	 * @param number
	 * @param sex
	 */
	private void login(String name, String number, String sex) {
		LoginReqVo loginReqVo = new LoginReqVo();
		loginReqVo.setImei(MyApplication.deviceId);
		loginReqVo.setName(name);
		loginReqVo.setNumber(number);
		loginReqVo.setSex(sex);
		loginReqVo.setType("0");
		String json = JSON.toJSONString(loginReqVo);
		MessagePacking messagePacking = new MessagePacking(
				Message.MESSAGE_STUDENT_LOGIN);
		messagePacking.putBodyData(DataType.INT,
				BufferUtils.writeUTFString(json));
		CoreSocket.getInstance().sendMessage(messagePacking);
		WLog.i(WaitingActivity.class, "启动登录..." + "request:" + json);
	}

	/**
	 * 取消登陆
	 *
	 * @param name
	 * @param number
	 * @param sex
	 */
	private void logout(String name, String number, String sex) {
		LoginReqVo loginReqVo = new LoginReqVo();
		loginReqVo.setImei(MyApplication.deviceId);
		loginReqVo.setName(name);
		loginReqVo.setNumber(number);
		loginReqVo.setSex(sex);
		loginReqVo.setType("1");
		String json = JSON.toJSONString(loginReqVo);
		MessagePacking messagePacking = new MessagePacking(
				Message.MESSAGE_STUDENT_LOGIN);
		messagePacking.putBodyData(DataType.INT,
				BufferUtils.writeUTFString(json));
		CoreSocket.getInstance().sendMessage(messagePacking);
		WLog.i(WaitingActivity.class, "启动取消登录...");
	}

	/**
	 * 检查新增的学生是否为重复录入(客户端检查)
	 */
	private boolean validate() {
		String stName = et_stname.getText().toString();
		String stNumber = et_stnumber.getText().toString();
		if (TextUtils.isEmpty(stName)) {
			ToastHelper.showCustomToast(getApplicationContext(),
					R.string.toast_stname_notnull);
			return false;
		} else if (stName.length() < 2) {
			ToastHelper.showCustomToast(getApplicationContext(),
					R.string.toast_stname_tooshort);
			return false;
		} else if (stName.contains(" ")) {
			ToastHelper.showCustomToast(getApplicationContext(),
					R.string.toast_stname_blank);
			return false;
		}
		if (TextUtils.isEmpty(stNumber)) {
			ToastHelper.showCustomToast(getApplicationContext(),
					R.string.toast_stnumber_notnull);
			return false;
		}
		if (loginResList.size() > Constants.STUDENT_MAX_NUM) {
			ToastHelper.showCustomToast(getApplicationContext(),
					R.string.toast_group_isfull);
			return false;
		}
		for (int i = 0; i < loginResList.size(); i++) {
			if (stNumber.equals(loginResList.get(i).getNumber())) {
				ToastHelper.showCustomToast(getApplicationContext(),
						R.string.toast_stname_repeat);
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (addState == 1) {
			llayout1.setVisibility(View.GONE);
			addState = 0;
			imm.hideSoftInputFromWindow(llayout.getWindowToken(), 0);
			return false;
		} else {
			return super.onTouchEvent(event);
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 登陆
			case STUDENT_LOGIN: {
				mProgressDialog.hide();
				JSONObject jsonObject = (JSONObject) msg.getData()
						.getSerializable("data");
				WLog.i(WaitingActivity.class, "获取登录信息..." + jsonObject);
				if (!"0".equals(jsonObject.getString("code"))) {
					return;
				} else if (jsonObject.getJSONObject("data") == null) {
				} else {
					LoginResVo loginResVo = JSON.parseObject(jsonObject
							.getJSONObject("data").toJSONString(),
							LoginResVo.class);
					if (loginResVo.getStudents() != null) {
						loginResList = loginResVo.getStudents();
					}
					((MyApplication) getApplication())
							.setLoginResVo(loginResVo);
					if (loginResList != null && loginResList.size() > 0) {
						mAdapter.setDatas(loginResList);
						gv_group_member.setAdapter(mAdapter);
					}
//					addState = 0;
					et_stnumber.setText("");
					et_stname.setText("");
					male.setChecked(true);
//					llayout1.setVisibility(View.GONE);
				}

				break;
			}
			// 获取分组
			case STUDENT_LIST: {
				mProgressDialog.hide();
				JSONObject jsonObject = (JSONObject) msg.getData()
						.getSerializable("data");
				WLog.i(WaitingActivity.class,
						"得到分组学生信息..." + jsonObject.toString());
				if (!"0".equals(jsonObject.getString("code"))) {
					return;
				} else if (jsonObject.getJSONObject("data") == null) {
				} else {
					LoginResVo loginResVo = JSON.parseObject(jsonObject
							.getJSONObject("data").toJSONString(),
							LoginResVo.class);
					if (loginResVo.getStudents() != null) {
						loginResList = loginResVo.getStudents();
					}
					((MyApplication) getApplication())
							.setLoginResVo(loginResVo);
					if (loginResList != null && loginResList.size() > 0) {
						mAdapter.setDatas(loginResList);
						gv_group_member.setAdapter(mAdapter);
					}
					llayout1.setVisibility(View.GONE);
				}

				break;
			}
			default:
				break;
			}
		}
	};

	public void doResult(JSONObject jsonObject, int type) {

		android.os.Message message = new android.os.Message();
		message.what = type;
		Bundle data = new Bundle();
		data.putSerializable("data", jsonObject);
		message.setData(data);
		mHandler.sendMessage(message);
	}

	/**
	 * 注册成员
	 */
	private void registerStudent() {
		LoginReqVo loginReqVo = new LoginReqVo();
		loginReqVo.setImei(MyApplication.deviceId);
		loginReqVo.setName(et_stname.getText().toString());
		loginReqVo.setNumber(et_stnumber.getText().toString());
		loginReqVo.setSex(female.isChecked() ? "2" : "1");
		loginReqVo.setType("2");
		String json = JSON.toJSONString(loginReqVo);

		MessagePacking messagePacking = new MessagePacking(
				Message.MESSAGE_STUDENT_LOGIN);
		messagePacking.putBodyData(DataType.INT,
				BufferUtils.writeUTFString(json));
		CoreSocket.getInstance().sendMessage(messagePacking);
		WLog.i(SplashActivity.class, "启动注册学生...");
	}

	/**
	 * 获取组成员列表
	 */
	private void getGroupUserList() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("imei", MyApplication.deviceId);
		MessagePacking messagePacking = new MessagePacking(
				Message.MESSAGE_GROUP_LIST);
		messagePacking.putBodyData(DataType.INT,
				BufferUtils.writeUTFString(jsonObject.toJSONString()));
		CoreSocket.getInstance().sendMessage(messagePacking);
		WLog.i(SplashActivity.class, "启动获取组成员列表...");

	}
}
