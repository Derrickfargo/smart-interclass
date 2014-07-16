package cn.com.incito.classroom.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.popoy.common.TAApplication;

import java.io.Serializable;

import cn.com.incito.classroom.R;

/**
 * Created by popoy on 2014/7/3.
 */
public class WifiConfigDialog extends DialogFragment {
    TextView tv_ssid_name;
    EditText et_password;
    Button btn_quit;
    Button btn_access_wifi;
    WifiConfigCallBack wifiConfigCallBack;
    String ssid;

    public static WifiConfigDialog newInstance(WifiConfigCallBack configCallBack, String ssid) {
        WifiConfigDialog wifiConfigDialog = new WifiConfigDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("configCallBack", configCallBack);
        bundle.putString("ssid", ssid);
        wifiConfigDialog.setArguments(bundle);
        return wifiConfigDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.wifiConfigCallBack = (WifiConfigCallBack) getArguments().getSerializable("configCallBack");
        this.ssid = getArguments().getString("ssid");
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        View v = inflater.inflate(R.layout.wificonfigdialog, container, false);
//        initView(v);
//        initListener();
//        return v;
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Dialog);

        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.wificonfigdialog, null, false);
        initView(v);
        initListener();
        dialog.setContentView(v);

        return dialog;
    }

    private void initView(View v) {
        tv_ssid_name = (TextView) v.findViewById(R.id.tv_ssid_name);
        tv_ssid_name.setText(ssid);
        et_password = (EditText) v.findViewById(R.id.et_password);
        btn_quit = (Button) v.findViewById(R.id.btn_quit);
        btn_access_wifi = (Button) v.findViewById(R.id.btn_access_wifi);
    }

    private void initListener() {
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
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
