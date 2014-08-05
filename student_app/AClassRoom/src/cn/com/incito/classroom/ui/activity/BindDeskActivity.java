package cn.com.incito.classroom.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.alibaba.fastjson.JSONObject;
import com.popoy.annotation.TAInjectView;
import com.popoy.common.TAActivity;
import com.popoy.tookit.helper.ToastHelper;

import java.util.ArrayList;
import java.util.List;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.DeskNumberAdapter;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.MessageHandler;
import cn.com.incito.socket.core.MessageInfo;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

/**
 * Created by popoy on 2014/7/28.
 */
public class BindDeskActivity extends TAActivity {
    @TAInjectView(id = R.id.gv_desk_number)
    private GridView gv_desk_number;
    @TAInjectView(id = R.id.btn_join)
    private ImageButton btn_join;
    private int currentPos;

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);
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
    @Override
    public void onBackPressed() {
        exitApp();
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
                        doActivity(R.string.waitingactivity);
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}
