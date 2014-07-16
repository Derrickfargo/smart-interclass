package cn.com.incito.classroom.ui.activity;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import cn.com.incito.classroom.R;

public class ImageActivity extends Activity {

    private static ImageActivity instance = null;
    private ImageView ivImage;
    private Bitmap bmp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        instance = this;
        initView();
        initDate();
    }

    private void initView() {
        ivImage = (ImageView) findViewById(R.id.ivImage);
    }

    private void initDate() {
        byte[] data = getIntent().getExtras().getByteArray("data");
        ByteArrayOutputStream outPut = new ByteArrayOutputStream();
        bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        bmp.compress(CompressFormat.PNG, 100, outPut);
        ivImage.setImageBitmap(bmp);
    }

    public static ImageActivity getInstance() {
        return instance;
    }

}
