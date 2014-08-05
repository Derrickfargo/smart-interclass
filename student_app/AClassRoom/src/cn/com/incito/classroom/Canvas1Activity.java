package cn.com.incito.classroom;

import java.util.ArrayList;
import java.util.HashMap;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.canvas.PaletteView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Canvas1Activity extends BaseActivity {
    PaletteView paletteView = null;

    ListView picList = null;

    @Override
    protected void onAfterOnCreate(Bundle savedInstanceState) {
        super.onAfterOnCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

        paletteView = (PaletteView) findViewById(R.id.palette);
        picList = (ListView) findViewById(R.id.picList);

        ArrayList<HashMap<String, Object>> users = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 4; i++) {
            HashMap<String, Object> user = new HashMap<String, Object>();
            int picId = getResources().getIdentifier("pic" + (i + 1),
                    "drawable", "cn.com.incito.classroom");
            user.put("img", picId);
            user.put("imgname", "picture_" + (i + 1));
            users.add(user);
        }
        SimpleAdapter saImageItems = new SimpleAdapter(this, users,// 数据来源
                R.layout.mylistview,// ListView的每一行不
                new String[]{"img", "imgname"}, new int[]{R.id.img,
                R.id.imgname}
        );
        picList.setAdapter(saImageItems);

        picList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int picId = getResources().getIdentifier(
                        "pic" + (position + 1), "drawable", "com.tted.palette");
                Bitmap bgBitmap = ((BitmapDrawable) (getResources()
                        .getDrawable(picId))).getBitmap();
                paletteView.setBgBitmap(bgBitmap);
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return paletteView.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "退出");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}