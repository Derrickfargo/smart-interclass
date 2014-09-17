package cn.com.incito.classroom.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.AppManager;


public class NetWorkDialog extends AlertDialog{
	private Button button_entrue;
	private Button button_cancle;
	private Context context;
	private TextView title;

	public NetWorkDialog(Context context, int theme) {
	    super(context, theme);
	    this.context=context;
	}

	public NetWorkDialog(Context context) {
	    super(context);
	    this.context=context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.network_dialog);
	    title=(TextView)findViewById(R.id.title);
	    title.setText("网络异常哦，请检查网络哦");
	    button_entrue=(Button)findViewById(R.id.entrue);
	    button_entrue.setText("设置");
	    button_entrue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				Intent intent = new Intent(Settings.ACTION_SETTINGS);
				context.startActivity(intent);
			}
			
		});
	    button_cancle=(Button)findViewById(R.id.dismiss);
	    button_cancle.setText("退出");
	    button_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppManager.getAppManager().AppExit(null);
			}
		});
	}
}
