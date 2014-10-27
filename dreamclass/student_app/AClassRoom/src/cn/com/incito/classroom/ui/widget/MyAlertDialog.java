package cn.com.incito.classroom.ui.widget;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.AppManager;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MyAlertDialog extends AlertDialog{

	private Button button_entrue;
	private Button button_cancle;
	private Context context;

	public MyAlertDialog(Context context, int theme) {
	    super(context, theme);
	    this.context=context;
	}

	public MyAlertDialog(Context context) {
	    super(context);
	    this.context=context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dialog);
	    button_entrue=(Button)findViewById(R.id.entrue);
	    button_entrue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppManager.getAppManager().AppExit(context);
			}
		});
	    button_cancle=(Button)findViewById(R.id.dismiss);
	    button_cancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}
