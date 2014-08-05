package cn.com.incito.classroom.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by popoy on 2014/8/5.
 */
public class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onAfterOnCreate(savedInstanceState);
    }

    protected void onAfterOnCreate(Bundle savedInstanceState) {
    }
}
