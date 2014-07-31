package cn.com.incito.classroom;

import com.popoy.common.TAActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;


@SuppressLint("SetJavaScriptEnabled")
public class Canvas2Activity extends TAActivity {
    private WebView mWebView;
    private Handler mHandler = new Handler();

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new Object() {
            public void clickOnAndroid() {
                mHandler.post(new Runnable() {
                    public void run() {
                        mWebView.loadUrl("javascript:wave()");
                    }
                });
            }
        }, "demo");
        mWebView.loadUrl("file:///android_asset/rocanvasjs/canvas.html");
    }

}
