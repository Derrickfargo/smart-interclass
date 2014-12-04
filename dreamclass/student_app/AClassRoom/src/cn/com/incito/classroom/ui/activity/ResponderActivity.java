package cn.com.incito.classroom.ui.activity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.common.utils.AndroidUtil;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;
import com.alibaba.fastjson.JSONObject;

/**
 * 抢答界面
 * @author hm
 */
public class ResponderActivity extends BaseActivity {

	private ImageButton imageButton;
	private AlertDialog dialog;
	private int count = 4;
	private Timer timer;
	private TimerTask timerTask;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if(1 == msg.what){
				dialog.dismiss();
				timer.cancel();
				showImageButton();
			}else{
				dialog.setMessage((msg.what - 1) + "");
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 dialog = new AlertDialog.Builder(this).setMessage("").create();
		 dialog.setCancelable(false);
		 dialog.show();
		 
		 timer = new Timer();
			timerTask = new TimerTask() {

				@Override
				public void run() {
					android.os.Message message = handler.obtainMessage();
					message.what = count;
					handler.sendMessage(message);
					count = count - 1;
				}
			};
		timer.schedule(timerTask, 0, 1 * 1000);
	}
	
	private void showImageButton(){
		RelativeLayout relativeLayout = new RelativeLayout(this);
		relativeLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		imageButton = new ImageButton(this);
		imageButton.setBackgroundResource(R.drawable.bg_confirm_group_ok);
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		layoutParams.leftMargin = randomX();
		layoutParams.topMargin = randomY();
		
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "ResponderActivity:发送抢答消息开始");
				
				MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_STUDENT_RESPONDER);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("imei", MyApplication.deviceId);
				messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
				CoreSocket.getInstance().sendMessage(messagePacking);
				
				MyApplication.Logger.debug(AndroidUtil.getCurrentTime() + "ResponderActivity:发送抢答消息成功");
				ResponderActivity.this.finish();
			}
		});
		
		relativeLayout.addView(imageButton,layoutParams);
		setContentView(relativeLayout);
	}
	
	private int randomX(){
		Random random = new Random();
		return random.nextInt(1001) + 200;
	}
	
	private int randomY(){
		Random random = new Random();
		return random.nextInt(501) + 100;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(timer != null){
			timer.cancel();
		}
	}
}
