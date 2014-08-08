package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.DeskNumberAdapter;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by popoy on 2014/7/28.
 */
public class BindDeskActivity extends BaseActivity {
    private GridView gv_desk_number;
    private ImageButton btn_join;
    protected long mExitTime;
    DeskNumberAdapter deskNumberAdapter;
    private int curentDesk;

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);
        setContentView(R.layout.binddesk);
        UIHelper.getInstance().setBindDeskActivity(this);
        initViews();
        initEvent();
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 12; i++) {
            list.add(String.valueOf(i + 1));
        }
        deskNumberAdapter = new DeskNumberAdapter(this, list);
        gv_desk_number.setAdapter(deskNumberAdapter);
        gv_desk_number.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                curentDesk = i + 1;
                deskNumberAdapter.setSelectPos(i);
                deskNumberAdapter.notifyDataSetChanged();
            }
        });

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

    private void initViews() {
        gv_desk_number = (GridView) findViewById(R.id.gv_desk_number);
        btn_join = (ImageButton) findViewById(R.id.btn_join);
    }

    private void initEvent() {
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curentDesk < 1) {
                    ToastHelper.showCustomToast(BindDeskActivity.this, R.string.toast_choose_atleast_one_desk);
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("imei", MyApplication.deviceId);
                jsonObject.put("number", curentDesk);
                MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_DEVICE_BIND);
                messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
                CoreSocket.getInstance().sendMessage(messagePacking);
            }

        });
    }
}
