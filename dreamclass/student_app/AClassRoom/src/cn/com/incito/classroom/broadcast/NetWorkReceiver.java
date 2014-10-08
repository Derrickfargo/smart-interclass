//package cn.com.incito.classroom.broadcast;
//
//import com.alibaba.fastjson.JSONObject;
//
//import cn.com.incito.classroom.base.MyApplication;
//import cn.com.incito.classroom.ui.activity.SplashActivity;
//import cn.com.incito.classroom.ui.widget.MyAlertDialog;
//import cn.com.incito.socket.core.CoreSocket;
//import cn.com.incito.socket.core.Message;
//import cn.com.incito.socket.message.DataType;
//import cn.com.incito.socket.message.MessagePacking;
//import cn.com.incito.socket.utils.BufferUtils;
//import cn.com.incito.wisdom.sdk.log.WLog;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.provider.Settings;
//
//public class NetWorkReceiver extends BroadcastReceiver {
//
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
//			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//			NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//			NetworkInfo activeInfo = manager.getActiveNetworkInfo();
//			if (activeInfo == null || wifiInfo == null){
//				MyAlertDialog mAlertDialog=new MyAlertDialog(context);
//				mAlertDialog.show();
////				showErrorNetDialog(context);
//			}	
//		}
//	}
//
//	public void showErrorNetDialog(final Context context) {
//		new AlertDialog.Builder(context).setTitle("网络异常，请进行网络设置或重试哦！！").setPositiveButton("设置", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Intent intent = new Intent(Settings.ACTION_SETTINGS);
//				context.startActivity(intent);
//				dialog.dismiss();
//			}
//		}).setNegativeButton("重试", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				CoreSocket.getInstance().restartConnection();
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//				startMainAct();
//				dialog.dismiss();
//			}
//		}).show();
//	}
//
//	/**
//	 * 发送连接请求
//	 */
//	public void startMainAct() {
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("imei", MyApplication.deviceId);
//		MessagePacking messagePacking = new MessagePacking(Message.MESSAGE_DEVICE_HAS_BIND);
//		messagePacking.putBodyData(DataType.INT, BufferUtils.writeUTFString(jsonObject.toJSONString()));
//		CoreSocket.getInstance().sendMessage(messagePacking);
//		WLog.i(SplashActivity.class, "开始判定设备是否绑定..." + "request:" + jsonObject.toJSONString());
//	}
//}
