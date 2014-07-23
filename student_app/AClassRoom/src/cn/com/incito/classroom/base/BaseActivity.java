package cn.com.incito.classroom.base;

import android.os.Bundle;
import android.view.View;

import com.popoy.common.TAActivity;

/**
 * Created by popoy on 2014/7/3.
 */
public class BaseActivity extends TAActivity {
    @Override
    protected void onPreOnCreate(Bundle savedInstanceState) {
        super.onPreOnCreate(savedInstanceState);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_SHOW_FULLSCREEN);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        getWindow().getDecorView().setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
    }

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);

    }
}
