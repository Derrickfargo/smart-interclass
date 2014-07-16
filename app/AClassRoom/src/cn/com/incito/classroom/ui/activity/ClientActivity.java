package cn.com.incito.classroom.ui.activity;

import java.io.ByteArrayOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.popoy.common.TAActivity;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.service.MessageService;

public class ClientActivity extends TAActivity implements View.OnClickListener {
    private EditText txtServerIP;
    private EditText txtServerPort;
    private Button btnConnection;
    private Button btnSendPicture;

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);
        txtServerIP = (EditText) findViewById(R.id.txtServerIP);
        txtServerPort = (EditText) findViewById(R.id.txtServerPort);
        btnConnection = (Button) findViewById(R.id.btnConnection);
        btnConnection.setOnClickListener(this);
        btnSendPicture = (Button) findViewById(R.id.btnSendPicture);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConnection:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(this, MessageService.class);
                if (btnConnection.getText().toString().equals("Connection")) {
                    bundle.putBoolean("FLAG", true);
                    bundle.putString("IP", txtServerIP.getText().toString());
                    bundle.putString("PORT", txtServerPort.getText().toString());
                    intent.putExtras(bundle);
                    btnConnection.setText("Disconnection");
                    startService(intent);
                    btnSendPicture.setEnabled(true);
                    btnSendPicture.setOnClickListener(this);
                    txtServerIP.setEnabled(false);
                    txtServerPort.setEnabled(false);
                    return;
                }
//                bundle.putBoolean("FLAG", false);
                btnConnection.setText("Connection");
//			stopService(intent);
                btnSendPicture.setEnabled(false);
                btnSendPicture.setOnClickListener(null);
                txtServerIP.setEnabled(true);
                txtServerPort.setEnabled(true);
                break;
            case R.id.btnSendPicture:
                View view = v.getRootView();
                view.setDrawingCacheEnabled(true);
                view.buildDrawingCache();
                Bitmap bitmap = view.getDrawingCache();
                if (bitmap != null) {
                    Toast.makeText(this, "图片", Toast.LENGTH_LONG).show();
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        // ��ȡͼƬ��ByteArrayOutputStream
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();
                        MessageService.getInstance().sendPicture(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "test", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sys_exit) {
            finish();
        }
        return super.onMenuItemSelected(featureId, item);
    }
}