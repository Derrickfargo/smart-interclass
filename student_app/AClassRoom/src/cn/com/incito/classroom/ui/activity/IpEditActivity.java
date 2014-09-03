package cn.com.incito.classroom.ui.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.common.utils.ToastHelper;


public class IpEditActivity extends BaseActivity{
	private EditText editIP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_ip_info);
		 editIP = (EditText)findViewById(R.id.edit_ip_name);
		 Button buttonAgree=(Button) findViewById(R.id.btn_agree);
		 buttonAgree.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(ipMatch()){
					Constants.setIP(editIP.getText().toString());
					Intent mIntent=new Intent();
					mIntent.setClass(IpEditActivity.this, SplashActivity.class);
					startActivity(mIntent);
				}else{
					ToastHelper.showCustomToast(IpEditActivity.this, "输入的IP不正确哦");
				}
			}
		});
	}
	public boolean ipMatch(){
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(editIP.getText().toString()); //以验证127.400.600.2为例
		System.out.println(matcher.matches());
		return matcher.matches();
	}
	
}
