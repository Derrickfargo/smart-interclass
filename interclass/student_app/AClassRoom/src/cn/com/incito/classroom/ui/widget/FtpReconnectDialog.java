package cn.com.incito.classroom.ui.widget;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.AppManager;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.constants.Constants;
import cn.com.incito.classroom.utils.FTPUtils;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FtpReconnectDialog extends AlertDialog{

	private Context context;
	private String tittle;
	private String buttonTittle;
	private Button button_cancle;
	private int DialogType;

	public FtpReconnectDialog(Context context, int theme) {
	    super(context, theme);
	    this.context=context;
	}

	public FtpReconnectDialog(Context context,String tittle,String buttonTittle,int DialogType) {
	    super(context);
	    this.DialogType=DialogType;
	    this.tittle=tittle;
	    this.buttonTittle=buttonTittle;
	    this.context=context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.network_dialog);
	    TextView title = (TextView)findViewById(R.id.title);
	    title.setText(tittle);
	    Button button_entrue = (Button)findViewById(R.id.entrue);
	    button_entrue.setText(buttonTittle);
	    button_entrue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(DialogType==1){
					if(!FTPUtils.downLoadFile(Constants.FILE_PATH, Constants.FILE_NAME)){
						FtpReconnectDialog.this.show();
					};
				}else if(DialogType==2){
					String filePath="/"+MyApplication.getInstance().getDeviceId();
					String fileName=MyApplication.getInstance().getDeviceId()+".jpg";
					if(FTPUtils.getInstance().uploadFile(filePath, fileName)){
						MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "作业提交成功");
						MyApplication.getInstance().lockScreen(true);
						MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_SAVE_PAPER);
						// 测试ID
						messagePacking.putBodyData(DataType.INT, BufferUtils
								.writeUTFString(MyApplication.getInstance().getQuizID()));
						// 设备ID
						messagePacking.putBodyData(DataType.INT, BufferUtils
								.writeUTFString(MyApplication.getInstance().getDeviceId()));
						messagePacking.putBodyData(DataType.INT,BufferUtils.writeUTFString(Constants.FILE_PATH+filePath+"/"+fileName));
						CoreSocket.getInstance().sendMessage(messagePacking);
					}else{
						FtpReconnectDialog.this.show();
					}
				}
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
