package cn.com.incito.classroom.adapter;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.popoy.tookit.Enum.Enumeration;

import cn.com.incito.classroom.R;

public class WifiListAdapter extends BaseAdapter {


    private Context context;
    private List<ScanResult> menuParentItems;
    private int selectPos = -1;
    private ConnectState state;
    LayoutInflater mInflater;

    public static enum ConnectState {
        CONNECTING, CONNECTED, CONNECT_FAIL
    }

    public WifiListAdapter(Context context,
                           List<ScanResult> item) {
        this.context = context;
        this.menuParentItems = item;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return menuParentItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuParentItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder data = new ViewHolder();
        convertView = mInflater.inflate(
                R.layout.item_wifilist, parent,false);
        data.tv_ssid = (TextView) convertView.findViewById(R.id.tv_ssid);
        final String name = menuParentItems.get(position).SSID;
        data.iv_wifi_list = (ImageView) convertView.findViewById(R.id.iv_wifi_list);
        if (selectPos == position && state == ConnectState.CONNECTING) {
            data.tv_ssid.setText("正在连接...");
            data.iv_wifi_list.setImageDrawable(context.getResources().getDrawable(R.drawable.logo));
        } else {
            data.tv_ssid.setText(name);
            data.iv_wifi_list.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher));
        }

        return convertView;
    }

    public void changeWifiState(int position, ConnectState state) {
        this.selectPos = position;
        this.state = state;
    }

    static class ViewHolder {
        public TextView tv_ssid;
        public ImageView iv_wifi_list;
    }


}