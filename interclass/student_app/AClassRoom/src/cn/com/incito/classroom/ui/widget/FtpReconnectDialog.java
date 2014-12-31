package cn.com.incito.classroom.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.utils.FTPUtils;

public class FtpReconnectDialog extends AlertDialog {

	private Context context;
	private Button button_cancle;

	public FtpReconnectDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public FtpReconnectDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network_dialog);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("收取失败，重新收取作业");
		Button button_entrue = (Button) findViewById(R.id.entrue);
		button_entrue.setText("重新收取");
		button_entrue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					FTPUtils.getInstance();
					if (FTPUtils.downLoadFile(Constants.FILE_PATH,
							Constants.FILE_NAME)) {
						dismiss();
					}
			}
		});
		button_cancle = (Button) findViewById(R.id.dismiss);
		button_cancle.setText("退出");
		button_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppManager.getAppManager().AppExit(null);
			}
		});
	}

}
