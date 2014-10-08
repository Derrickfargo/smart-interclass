package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
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
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.GroupNumAdapter;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.vo.GroupNumberRes2Vo;

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
    List<GroupNumberRes2Vo> list;
    GroupNumAdapter mAdapter;
    TranslateAnimation mShowAction;
    TranslateAnimation mHiddenAction;
    InputMethodManager imm ;
    /**
     * 0只显示增加按钮，1显示姓名2显示姓名、学号、性别
     */
    private int addState = 0;

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);
        initViews();
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
                    GroupNumberRes2Vo groupNumberListRes = new GroupNumberRes2Vo();
                    groupNumberListRes.setMembergender("12");
                    groupNumberListRes.setMembername("刘博");
                    groupNumberListRes.setMembernumber("10203");
                    list.add(groupNumberListRes);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        et_stname = (EditText) findViewById(R.id.et_stname);

        et_stname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_stname.getText().length() > 1 && addState == 1) {
                    llayout2.setAnimation(mShowAction);
                    llayout2.setVisibility(View.VISIBLE);

                }
            }
        });
        et_stname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (et_stname.getText().length() > 1 && addState == 1) {
                    llayout2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_stname.getText().length() > 1 && addState == 1) {
                    llayout2.setVisibility(View.VISIBLE);
                }
            }
        });
//        gv_group_member.setSelector(R.drawable.selector_groupnumber);
        list = new ArrayList<GroupNumberRes2Vo>();
        GroupNumberRes2Vo groupNumberListRes = new GroupNumberRes2Vo();
        groupNumberListRes.setMembergender("1");
        groupNumberListRes.setMembername("lisan");
        groupNumberListRes.setMembernumber("111");
        list.add(groupNumberListRes);
        mAdapter = new GroupNumAdapter(this, list);
        gv_group_member.setAdapter(mAdapter);
        gv_group_member.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!list.get(position).isLogon()) {
                    list.get(position).setLogon(true);
                    login();
                } else {
                    list.get(position).setLogon(false);
                    logout();
                }
                mAdapter.notifyDataSetChanged();
            }
        });


    }


    private void initViews(){
    	
    	et_stnumber = (EditText) findViewById(R.id.et_stnumber);
    	btn_join = (ImageButton) findViewById(R.id.btn_join);
    	gender_group = (RadioGroup) findViewById(R.id.gender_group);
    	female = (RadioButton) findViewById(R.id.female);
    	male = (RadioButton) findViewById(R.id.male);
    	gv_group_member = (GridView) findViewById(R.id.gv_group_member);
    	llayout1 = (LinearLayout)findViewById(R.id.llayout1);
    	llayout2 = (LinearLayout) findViewById(R.id.llayout2);
    }
    
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
     //   exitApp();
    }

    /**
     * 与后台服务建立连接，并实现登陆
     */
    private void login() {
//        CoreSocket.getInstance().startConnection();

    }

    /**
     * 取消登陆
     */
    private void logout() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (WaitingActivity.this.getCurrentFocus() != null) {
//                if (WaitingActivity.this.getCurrentFocus().getWindowToken() != null) {
//                    imm.hideSoftInputFromWindow(WaitingActivity.this.getCurrentFocus().getWindowToken(),
//                            InputMethodManager.HIDE_NOT_ALWAYS);
//                }
//            }
//        }
//        return super.onTouchEvent(event);
        return imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }
}
