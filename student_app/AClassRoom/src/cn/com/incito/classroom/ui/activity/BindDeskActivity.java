package cn.com.incito.classroom.ui.activity;

import android.os.Bundle;
import android.widget.GridView;
import com.popoy.annotation.TAInjectView;

import java.util.ArrayList;
import java.util.List;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.adapter.DeskNumberAdapter;
import cn.com.incito.classroom.base.BaseActivity;

/**
 * Created by popoy on 2014/7/28.
 */
public class BindDeskActivity extends BaseActivity {
    @TAInjectView(id = R.id.gv_desk_number)
    private GridView gv_desk_number;

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            list.add(String.valueOf(i + 1));
        }
        DeskNumberAdapter deskNumberAdapter = new DeskNumberAdapter(this, list);
        gv_desk_number.setAdapter(deskNumberAdapter);
    }
}
