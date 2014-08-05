package cn.com.incito.classroom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.DeskNumberAdapter;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.MessageInfo;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

/**
 * Created by popoy on 2014/7/28.
 */
public class BindDeskActivity extends BaseActivity {
    private GridView gv_desk_number;
    private ImageButton btn_join;
    private int currentPos;

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);
        initViews();
        initEvent();
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 12; i++) {
            list.add(String.valueOf(i + 1));
        }
        DeskNumberAdapter deskNumberAdapter = new DeskNumberAdapter(this, list);
        gv_desk_number.setAdapter(deskNumberAdapter);
        gv_desk_number.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentPos = i + 1;
            }
        });

    }

    @Override
    public void onBackPressed() {
    }

    private void initViews() {
        gv_desk_number = (GridView) findViewById(R.id.gv_group_member);
        btn_join = (ImageButton) findViewById(R.id.btn_join);
    }

    private void initEvent() {
        btn_join.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (currentPos < 1) {
                                                ToastHelper.showCustomToast(BindDeskActivity.this, R.string.toast_choose_atleast_one_desk);
                                                return;
                                            }
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("imei", MyApplication.deviceId);
                                            jsonObject.put("number", currentPos);
                                            MessagePacking messagePacking = new MessagePacking(MessageInfo.MESSAGE_DEVICE_BIND);
                                            messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
                                            CoreSocket.getInstance().sendMessage(messagePacking, new MessageHandler() {
                                                @Override
                                                public void handleMessage(Bundle bundle) {
                                                    Message message = new Message();
                                                    message.what = 1;
                                                    message.setData(bundle);
                                                    mHandler.sendMessage(message);
                                                }

                                            });
                                        }

                                    }
        );
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    JSONObject jsonObject = (JSONObject) msg.getData().getSerializable("data");
                    if (!"0".equals(jsonObject.getString("code"))) {
                        return;
                    } else {
                        startActivity(new Intent(BindDeskActivity.this,
                                WaitingActivity.class));
                        finish();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
