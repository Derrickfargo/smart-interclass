package cn.com.incito.classroom.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import cn.com.incito.classroom.R;

/**
 * Created by popoy on 2014/7/3.
 */
public class LoginDialog extends DialogFragment {
    public Context mContext;
    EditText et_username;
    EditText et_password;

    public static LoginDialog newInstance() {
        LoginDialog frag = new LoginDialog();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.logindialog, container,
                false);
        et_username = (EditText) v.findViewById(R.id.et_ssid);
        et_password = (EditText) v.findViewById(R.id.et_password);

        return v;
    }
}
