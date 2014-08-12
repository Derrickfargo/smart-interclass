package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
/**
 * 无线WIFI选择页面.
 * @author 陈正
 */
public class WifiSelectorActivity extends BaseActivity {
	
	private GridView gridView_wifi; 
	private ArrayList<IWifiItem> mWifiItems  = new ArrayList<IWifiItem>();
	private IWifiListAdapter mWifiListAdapter ;
	private String TAG = "WifiSelectorActivity";
	private WifiInfo mWifiInfo;
	private mWifiIntentReceiver mWifiIntentReceiver;
	private WifiManager mWifiManager;  
	private Handler mHandler ;
	private boolean isAutoInvalidate = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifiselector_main);
		initWifi();
		initViews();
	}

	
	/**
	 * 初始化UI
	 */
	private void initViews(){
		gridView_wifi = (GridView) findViewById(R.id.wifiselector_main_gridview);
		mWifiListAdapter = new IWifiListAdapter();
		gridView_wifi.setAdapter(mWifiListAdapter);
		gridView_wifi.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(getBaseContext(), "试图连接wifi", 1).show();
			}
		});
		
		isAutoInvalidate = true;
		mHandler = new IFlushHander();
		mHandler.sendMessageDelayed(mHandler.obtainMessage(), 500);
		
	}
	
	/**
	 * 刷新页面
	 *
	 */
	private class IFlushHander extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			invalidateWifiListData();
			mWifiListAdapter.notifyDataSetChanged();
			if(isAutoInvalidate){
				mHandler.sendMessageDelayed(mHandler.obtainMessage(), 10*1000);
			}
			
		}
		
	}
	
	
	
	/**
	 * 初始化WIFI
	 */
	private void initWifi(){
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE); 
		IntentFilter	mWifiIntentFilter = new IntentFilter();  
        mWifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);  
        mWifiIntentReceiver = new mWifiIntentReceiver();  
        registerReceiver(mWifiIntentReceiver, mWifiIntentFilter);  
	}
	
	
	
	/**
	 * 刷新wifi列表数据
	 */
	private void invalidateWifiListData(){
		mWifiManager.startScan();  
		mWifiItems.clear();
		if(mWifiManager.isWifiEnabled()){
		ArrayList<ScanResult> scanResults =	(ArrayList<ScanResult>) mWifiManager.getScanResults();
		//TODO 是否应该根据SSID进行过滤
		IWifiItem wifiItem ;
		ScanResult scanResult ;
		
		Collections.sort(scanResults, new Comparator<ScanResult>() {

			@Override
			public int compare(ScanResult arg0, ScanResult arg1) {
				return Math.abs(arg0.level)-Math.abs(arg1.level);
			}
			
		});
		
		for (int i = 0; i < scanResults.size(); i++) {
			wifiItem = new IWifiItem();
			scanResult = scanResults.get(i);
			wifiItem.setScanResult(scanResult);
			wifiItem.setWifiName(scanResult.SSID);
			wifiItem.setWifiType(IWifiType.WIFITYPE_NORMAL);
			mWifiItems.add(wifiItem);
		}
		
		}else {
			mWifiItems.clear();
		}
		
	
	}
	
	
	/**
	 * 当wifi发送变化时触发
	 * @author 陈正
	 *
	 */
	 private class mWifiIntentReceiver extends BroadcastReceiver{  
		  
	        public void onReceive(Context context, Intent intent) {  
	              
	            switch (intent.getIntExtra("wifi_state", 0)) {  
	            case WifiManager.WIFI_STATE_DISABLING:  
	                break;  
	            case WifiManager.WIFI_STATE_DISABLED:  
	                break;  
	            case WifiManager.WIFI_STATE_ENABLING:  
	                break;  
	            case WifiManager.WIFI_STATE_ENABLED:  
	                break;  
	            case WifiManager.WIFI_STATE_UNKNOWN:  
	                break;  
	        }  
	            
	            mHandler.sendMessageDelayed(mHandler.obtainMessage(), 1000);
	    }  
	
	 }
	
	private class IWifiListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			
			return mWifiItems.size();
		}

		@Override
		public Object getItem(int arg0) {
			
			return mWifiItems.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			
			return arg0;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup viewGroup) {
			IWifiItem wifiItem = mWifiItems.get(position);
			IWifiHolder mWifiHolder ;
			if(contentView==null){
				mWifiHolder = new IWifiHolder();
				contentView = LayoutInflater.from(WifiSelectorActivity.this).inflate(R.layout.wifiselector_gridview_item, null);
				mWifiHolder.imageView_icon = (ImageView) contentView.findViewById(R.id.wifiselector_gridview_item_img);
				mWifiHolder.textView_name = (TextView)contentView.findViewById(R.id.wifiselector_gridview_item_name);
				contentView.setTag(mWifiHolder);
			}else {
				mWifiHolder = (IWifiHolder) contentView.getTag();
			}
			
			mWifiHolder.imageView_icon.setImageResource(R.drawable.cat);
			mWifiHolder.textView_name.setText(wifiItem.getScanResult().SSID+"\n"+Math.abs(wifiItem.getScanResult().level));
			
			//当前连接的网络设为红色字体
			mWifiInfo = mWifiManager.getConnectionInfo();
			if(mWifiInfo.getBSSID().equals(wifiItem.getScanResult().BSSID)){
				mWifiHolder.textView_name.setTextColor(Color.RED);
			}else {
				mWifiHolder.textView_name.setTextColor(Color.BLACK);
			}
			return contentView;
		}
		
		
		
	}
	
	private class IWifiHolder{
		
		 ImageView imageView_icon ;
		 TextView textView_name ;
		
	}
	
	/**
	 * WIFI显示时的对象
	 * @author 陈正
	 *
	 */
	private class IWifiItem{
		
		private String wifiName = "" ;
		private IWifiType wifiType  = IWifiType.WIFITYPE_NORMAL;
		private ScanResult scanResult ;
		
		public String getWifiName() {
			return wifiName;
		}
		public void setWifiName(String wifiName) {
			this.wifiName = wifiName;
		}
		public IWifiType getWifiType() {
			return wifiType;
		}
		public void setWifiType(IWifiType wifiType) {
			this.wifiType = wifiType;
		}
		public ScanResult getScanResult() {
			return scanResult;
		}
		public void setScanResult(ScanResult scanResult) {
			this.scanResult = scanResult;
		}
		
		
		
		
		
	}
	
	/**
	 * WIFI的分类
	 * @author 陈正
	 *
	 */
	private enum IWifiType{
		/**
		 *课堂类型
		 */
		WIFITYPE_CLASS,
		/**
		 * 3G网络
		 */
		WIFITYPE_3G,
		/**
		 * 家庭或者其他的网络类型
		 */
		WIFITYPE_NORMAL ,
		/**
		 * 未知类型
		 */
		WIFITYPE_NONE ;
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mWifiIntentReceiver);
	}
	
	@Override
	protected void onStart() {
		isAutoInvalidate = true;
		super.onStart();
	}
	@Override
	protected void onStop() {
		super.onStop();
		isAutoInvalidate = false;
	}
	
}
