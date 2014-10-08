package cn.com.incito.classroom.ui.dialog;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.com.incito.classroom.R;

public class ConfigWifiWindow extends PopupWindow {

    Activity mContext;
    private TextView et_username;
    private EditText et_password;
    private Button btn_access_wifi;
    private View mMenuView;
    String ssid;
    WifiConfigCallBack wifiConfigCallBack;

    public ConfigWifiWindow(final Activity context, String ssidname, WifiConfigCallBack configCallBack) {
        super(context);
        this.ssid = ssidname;
        this.wifiConfigCallBack = configCallBack;
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.logindialog, null);
        et_username = (TextView) mMenuView.findViewById(R.id.et_ssid);
        et_username.setText(ssid);
        et_password = (EditText) mMenuView.findViewById(R.id.et_password);
        btn_access_wifi = (Button) mMenuView.findViewById(R.id.btn_access_wifi);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
//        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        btn_access_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                wifiConfigCallBack.connectWifi(ssid, et_password.getText().toString());

            }
        });
    }

    public interface WifiConfigCallBack extends Serializable {
        public void connectWifi(String ssid, String password);
    }
}
