package cn.com.incito.classroom.ui.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.common.utils.ToastHelper;

public class IpSettingDialogFragment extends DialogFragment {
	private EditText editIP;
	ImageButton buttonAgree;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.edit_ip_info, container);
		editIP = (EditText) view.findViewById(R.id.edit_ip_name);
		editIP.setText(MyApplication.getInstance().getSharedPreferences()
				.getString(Constants.PREFERENCE_IP, Constants.IP));
		setCancelable(false);
		buttonAgree = (ImageButton) view.findViewById(R.id.btn_agree);
		buttonAgree.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ipMatch()) {
					Constants.setIP(editIP.getText().toString());
					Editor editor = MyApplication.getInstance()
							.getSharedPreferences().edit();
					editor.putString(Constants.PREFERENCE_IP, editIP.getText()
							.toString());
					editor.apply();
					dismiss();
				} else {
					ToastHelper.showCustomToast(
							IpSettingDialogFragment.this.getActivity(),
							"输入的IP不正确哦");
				}
			}
		});
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		getDialog().getWindow().setLayout(520, 420);
	}

	public boolean ipMatch() {
		Pattern pattern = Pattern
				.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(editIP.getText().toString()); // 以验证127.400.600.2为例
		System.out.println(matcher.matches());
		return matcher.matches();
	}

}
