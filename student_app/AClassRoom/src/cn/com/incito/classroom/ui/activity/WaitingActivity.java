package cn.com.incito.classroom.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.GroupNumAdapter;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.LoginReqVo;
import cn.com.incito.classroom.vo.LoginRes2Vo;
import cn.com.incito.classroom.vo.LoginResVo;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.MessageInfo;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;
import cn.com.incito.wisdom.uicomp.widget.dialog.ProgressiveDialog;

/**
 * 用户其启动界面
 * 用户其启动界面时候的一个启动页面完成一些初始化工作
 *
 * @author liubo
 * @version V1.0
 */
public class WaitingActivity extends BaseActivity {
    public static final String TAG = "WaitingActivity";
    //自定义的弹出框类
    EditText et_stname;
    EditText et_stnumber;
    ImageButton btn_join;
    RadioGroup gender_group;
    RadioButton female;
    RadioButton male;
    GridView gv_group_member;
    LinearLayout llayout1;
    LinearLayout llayout2;
    List<LoginRes2Vo> list;
    GroupNumAdapter mAdapter;
    TranslateAnimation mShowAction;
    TranslateAnimation mHiddenAction;
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
        mProgressDialog = new ProgressiveDialog(this);
        initViews();
        initListener();
        list = new ArrayList<LoginRes2Vo>();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f);
        mHiddenAction.setDuration(500);
//        llayout1.setVisibility(View.GONE);
        mAdapter = new GroupNumAdapter(WaitingActivity.this, list);
        et_stnumber.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        getGroupUserList();
    }

    private void initViews() {
        et_stnumber = (EditText) findViewById(R.id.et_stnumber);
        btn_join = (ImageButton) findViewById(R.id.btn_join);
        gender_group = (RadioGroup) findViewById(R.id.gender_group);
        female = (RadioButton) findViewById(R.id.female);
        male = (RadioButton) findViewById(R.id.male);
        gv_group_member = (GridView) findViewById(R.id.gv_group_member);
        llayout1 = (LinearLayout) findViewById(R.id.llayout1);
        llayout2 = (LinearLayout) findViewById(R.id.llayout2);
        et_stname = (EditText) findViewById(R.id.et_stname);
    }

    private void initListener() {
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addState == 0) {
                    llayout1.setAnimation(mShowAction);
                    llayout1.setVisibility(View.VISIBLE);
//                    llayout2.setVisibility(View.GONE);
                    addState = 1;
                } else {
                    if (validate()) {
                        LoginRes2Vo groupNumberListRes = new LoginRes2Vo();
                        groupNumberListRes.setSex(male.isChecked() ? "1" : "2");
                        groupNumberListRes.setName(et_stname.getText().toString());
                        groupNumberListRes.setNumber(et_stnumber.getText().toString());
                        list.add(groupNumberListRes);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘

                        addState = 0;
                        mProgressDialog.setMessage(R.string.load_dialog_default_text);
                        mProgressDialog.show();
                        registerStudent();
                    }

                }

            }
        });
        et_stname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_stname.getText().length() > 1 && addState == 1) {
                    llayout2.setAnimation(mShowAction);
                    llayout2.setVisibility(View.VISIBLE);
                }
            }
        });
        gv_group_member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mProgressDialog.setMessage(R.string.load_dialog_default_text);
                mProgressDialog.show();
                if (list.get(position).isLogin() == false) {
                    login(list.get(position).getName(), list.get(position).getNumber(), list.get(position).getSex());
                } else {
                    logout(list.get(position).getName(), list.get(position).getNumber(), list.get(position).getSex());
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
        if ((System.currentTimeMillis() - mExitTime) > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();// 更新mExitTime
        } else {
            AppManager.getAppManager().AppExit(this);
        }
    }


    /**
     * 与后台服务建立连接，并实现登陆
     */
    private void login(String name, String number, String sex) {
        LoginReqVo loginReqVo = new LoginReqVo();
        loginReqVo.setImei(MyApplication.deviceId);
        loginReqVo.setName(name);
        loginReqVo.setNumber(number);
        loginReqVo.setSex(sex);
        loginReqVo.setType("0");
        String json = JSON.toJSONString(loginReqVo);
        MessagePacking messagePacking = new MessagePacking(MessageInfo.MESSAGE_STUDENT_LOGIN);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
        CoreSocket.getInstance().sendMessage(messagePacking, new MessageHandler() {
            @Override
            protected void handleMessage(Bundle data) {
                Message message = new Message();
                message.what = 1;
                message.setData(data);
//                Log.i(message.toString());
                mHandler.sendMessage(message);
            }
        });

    }

    /**
     * 取消登陆
     */
    private void logout(String name, String number, String sex) {
        LoginReqVo loginReqVo = new LoginReqVo();
        loginReqVo.setImei(MyApplication.deviceId);
        loginReqVo.setName(name);
        loginReqVo.setNumber(number);
        loginReqVo.setSex(sex);
        loginReqVo.setType("1");
        String json = JSON.toJSONString(loginReqVo);
        MessagePacking messagePacking = new MessagePacking(MessageInfo.MESSAGE_STUDENT_LOGIN);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
        CoreSocket.getInstance().sendMessage(messagePacking, new MessageHandler() {
            @Override
            protected void handleMessage(Bundle data) {
                Message message = new Message();
                message.what = 2;
                message.setData(data);
                mHandler.sendMessage(message);
            }
        });
    }

    /**
     * 检查新增的学生是否为重复录入(客户端检查)
     */
    private boolean validate() {
        String stName = et_stname.getText().toString();
        String stNumber = et_stnumber.getText().toString();
        if (TextUtils.isEmpty(stName)) {
            ToastHelper.showCustomToast(getApplicationContext(), R.string.toast_stname_notnull);
            return false;
        } else if (stName.length() < 2) {
            ToastHelper.showCustomToast(getApplicationContext(), R.string.toast_stname_tooshort);
            return false;
        }
        if (TextUtils.isEmpty(stNumber)) {
            ToastHelper.showCustomToast(getApplicationContext(), R.string.toast_stnumber_notnull);
            return false;
        }
        if (list.size() > 16) {
            ToastHelper.showCustomToast(getApplicationContext(), R.string.toast_group_isfull);
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (stNumber.equals(list.get(i).getNumber())) {
                ToastHelper.showCustomToast(getApplicationContext(), R.string.toast_stname_repeat);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (addState == 1 || addState == 2) {
            llayout1.setVisibility(View.GONE);
            addState = 0;
            return imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        } else {
            return super.onTouchEvent(event);
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //登陆
                case 1: {
                    mProgressDialog.hide();
                    JSONObject jsonObject = (JSONObject) msg.getData().getSerializable("data");
                    if (!"0".equals(jsonObject.getString("code"))) {
                        return;
                    } else if (jsonObject.getJSONObject("data") == null) {
                    } else {
                        LoginResVo loginResVo = JSON.parseObject(jsonObject.getJSONObject("data").toJSONString(), LoginResVo.class);
                        list = loginResVo.getStudents();
                        ((MyApplication) getApplication()).setLoginResVo(loginResVo);
                        if (list != null && list.size() > 0) {
                            mAdapter = new GroupNumAdapter(WaitingActivity.this, list);
                            gv_group_member.setAdapter(mAdapter);
                        }
                    }
                    break;
                }
                //取消登陆
                case 2: {
                    mProgressDialog.hide();
                    JSONObject jsonObject = (JSONObject) msg.getData().getSerializable("data");
                    if (!"0".equals(jsonObject.getString("code"))) {
                        return;
                    } else if (jsonObject.getJSONObject("data") == null) {
                    } else {
                        LoginResVo loginResVo = JSON.parseObject(jsonObject.getJSONObject("data").toJSONString(), LoginResVo.class);
                        list = loginResVo.getStudents();
                        ((MyApplication) getApplication()).setLoginResVo(loginResVo);
                        if (list != null && list.size() > 0) {
                            mAdapter = new GroupNumAdapter(WaitingActivity.this, list);
                            gv_group_member.setAdapter(mAdapter);
                        }
                    }
                    break;
                }
                //注册
                case 3: {
                    mProgressDialog.hide();
                    JSONObject jsonObject = (JSONObject) msg.getData().getSerializable("data");
                    if (!"0".equals(jsonObject.getString("code"))) {
                        return;
                    } else if (jsonObject.getJSONObject("data") == null) {
                    } else {
                        LoginResVo loginResVo = JSON.parseObject(jsonObject.getJSONObject("data").toJSONString(), LoginResVo.class);
                        if (loginResVo.getStudents() != null) {
                            list = loginResVo.getStudents();
                        }
                        ((MyApplication) getApplication()).setLoginResVo(loginResVo);
                        if (list != null && list.size() > 0) {
                            mAdapter = new GroupNumAdapter(WaitingActivity.this, list);
                            gv_group_member.setAdapter(mAdapter);
                        }
                        et_stnumber.setText("");
                        et_stname.setText("");
                        male.setChecked(true);
                        llayout1.setVisibility(View.GONE);
                    }

                    break;
                }
                //获取分组
                case 4: {
                    mProgressDialog.hide();
                    JSONObject jsonObject = (JSONObject) msg.getData().getSerializable("data");
                    if (!"0".equals(jsonObject.getString("code"))) {
                        return;
                    } else if (jsonObject.getJSONObject("data") == null) {
                    } else {
                        LoginResVo loginResVo = JSON.parseObject(jsonObject.getJSONObject("data").toJSONString(), LoginResVo.class);
                        if (loginResVo.getStudents() != null) {
                            list = loginResVo.getStudents();
                        }
                        ((MyApplication) getApplication()).setLoginResVo(loginResVo);
                        if (list != null && list.size() > 0) {
                            mAdapter = new GroupNumAdapter(WaitingActivity.this, list);
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

    /**
     * 注册成员
     */
    private void registerStudent() {
        LoginReqVo loginReqVo = new LoginReqVo();
        loginReqVo.setImei(MyApplication.deviceId);
        loginReqVo.setName(et_stname.getText().toString());
        loginReqVo.setNumber(et_stnumber.getText().toString());
        loginReqVo.setSex(female.isChecked() ? "1" : "2");
        loginReqVo.setType("2");
        String json = JSON.toJSONString(loginReqVo);

        MessagePacking messagePacking = new MessagePacking(MessageInfo.MESSAGE_STUDENT_LOGIN);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
        CoreSocket.getInstance().sendMessage(messagePacking, new MessageHandler() {
            @Override
            protected void handleMessage(Bundle data) {
                Message message = new Message();
                message.what = 3;
                message.setData(data);
                mHandler.sendMessage(message);
            }
        });
    }

    /**
     * 获取组成员列表
     */
    private void getGroupUserList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imei", MyApplication.deviceId);

        MessagePacking messagePacking = new MessagePacking(MessageInfo.MESSAGE_GROUP_LIST);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
        CoreSocket.getInstance().sendMessage(messagePacking, new MessageHandler() {
            @Override
            protected void handleMessage(Bundle data) {
                Message message = new Message();
                message.what = 4;
                message.setData(data);
                mHandler.sendMessage(message);
            }
        });
    }
}
