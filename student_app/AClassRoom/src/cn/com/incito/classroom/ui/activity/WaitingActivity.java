package cn.com.incito.classroom.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.popoy.annotation.TAInjectView;
import com.popoy.common.TAApplication;
import com.popoy.common.core.AsyncTask;
import com.popoy.tookit.helper.ToastHelper;

import java.util.ArrayList;
import java.util.List;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.GroupNumAdapter;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.vo.LoginReqVo;
import cn.com.incito.classroom.vo.LoginRes2Vo;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

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
    @TAInjectView(id = R.id.et_stnumber)
    EditText et_stnumber;
    @TAInjectView(id = R.id.btn_join)
    ImageButton btn_join;
    @TAInjectView(id = R.id.gender_group)
    RadioGroup gender_group;
    @TAInjectView(id = R.id.female)
    RadioButton female;
    @TAInjectView(id = R.id.male)
    RadioButton male;
    @TAInjectView(id = R.id.gv_group_member)
    GridView gv_group_member;
    @TAInjectView(id = R.id.llayout1)
    LinearLayout llayout1;
    @TAInjectView(id = R.id.llayout2)
    LinearLayout llayout2;
    List<LoginRes2Vo> list;
    GroupNumAdapter mAdapter;
    TranslateAnimation mShowAction;
    TranslateAnimation mHiddenAction;
    InputMethodManager imm;
    /**
     * 0只显示增加按钮，1显示姓名2显示姓名、学号、性别
     */
    private int addState = 0;

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);
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
        llayout1.setVisibility(View.GONE);
        et_stnumber.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addState == 0) {
                    llayout1.setAnimation(mShowAction);
                    llayout1.setVisibility(View.VISIBLE);
                    llayout2.setVisibility(View.GONE);
                    addState = 1;
                } else {
                    if (checkNameRepeat()) {
                        LoginRes2Vo groupNumberListRes = new LoginRes2Vo();
                        groupNumberListRes.setSex(male.isChecked() ? "1" : "2");
                        groupNumberListRes.setName(et_stname.getText().toString());
                        groupNumberListRes.setNumber(et_stnumber.getText().toString());
                        list.add(groupNumberListRes);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                        llayout1.setVisibility(View.GONE);
                        addState = 0;
                        task.execute();
                    }

                }
                mAdapter.notifyDataSetChanged();
            }
        });
        et_stname = (EditText) findViewById(R.id.et_stname);

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
//        gv_group_member.setSelector(R.drawable.selector_groupnumber);
        list = new ArrayList<LoginRes2Vo>();
        LoginRes2Vo groupNumberListRes = new LoginRes2Vo();
        groupNumberListRes.setSex("1");
        groupNumberListRes.setName("lisan");
        groupNumberListRes.setNumber("111");
        list.add(groupNumberListRes);
        mAdapter = new GroupNumAdapter(this, list);
        gv_group_member.setAdapter(mAdapter);
        gv_group_member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (list.get(position).getIslogin() == "1") {
                    list.get(position).setIslogin("0");
//                    login(list.get(position).getName(), list.get(position).getNumber(), list.get(position).getSex());
                } else {
                    list.get(position).setIslogin("1");
//                    logout(list.get(position).getName(), list.get(position).getNumber(), list.get(position).getSex());
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        exitApp();
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
        MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
        CoreSocket.getInstance().sendMessage(messagePacking);

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
        loginReqVo.setType("0");
        String json = JSON.toJSONString(loginReqVo);
        MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
        messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
        CoreSocket.getInstance().sendMessage(messagePacking);
    }

    /**
     * 检查新增的学生是否为重复录入(客户端检查)
     */
    private boolean checkNameRepeat() {
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
        for (int i = 0; i < list.size(); i++) {
            if (stName.equals(list.get(i).getName()) || stNumber.equals(list.get(i).getNumber())) {
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
        }
        return imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    AsyncTask task = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {
            LoginReqVo loginReqVo = new LoginReqVo();
            loginReqVo.setImei(MyApplication.deviceId);
            loginReqVo.setName("liubo");
            loginReqVo.setNumber("111");
            loginReqVo.setSex("1");
            loginReqVo.setType("2");
            String json = JSON.toJSONString(loginReqVo);

            MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_LOGIN);
            messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(json));
            CoreSocket.getInstance().sendMessage(messagePacking);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    };
}