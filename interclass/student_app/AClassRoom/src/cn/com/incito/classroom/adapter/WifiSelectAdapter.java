package cn.com.incito.classroom.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.incito.classroom.R;

public class WifiSelectAdapter extends BaseAdapter {
	private List<ScanResult> scanResults;
	private Context context;
	
	public WifiSelectAdapter(Context context,List<ScanResult> scanResults){
		this.context = context;
		this.scanResults = scanResults;
	}
	
	public void setScanresult(List<ScanResult> scanResults){
		this.scanResults = scanResults;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return scanResults.size();
	}

	@Override
	public Object getItem(int arg0) {
		return scanResults.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int arg0, View contentView, ViewGroup arg2) {
		
		ScanResult scanResult = scanResults.get(arg0);
		WifiHolder holder;
		if(contentView == null){
			holder = new WifiHolder();
			contentView = LayoutInflater.from(context)
					.inflate(R.layout.wifiselector_gridview_item, null);
			holder.textView_name = (TextView) contentView
					.findViewById(R.id.wifiselector_gridview_item_name);
			contentView.setTag(holder);
		}else{
			holder = (WifiHolder) contentView.getTag();
		}
		
		holder.textView_name.setText(scanResult.SSID);
		holder.textView_name.setTextColor(Color.BLACK);
		
		return contentView;
	}
	
	private class WifiHolder {

		TextView textView_name;

	}

}
