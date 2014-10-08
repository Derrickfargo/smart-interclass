package cn.com.incito.classroom.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;

/**
 * 功能模块选择页面
 * 
 * @author 陈正
 * 
 */
public class MoudleSelectorActivity extends BaseActivity {

	private Button button_backToAp;
	private View view_startClass, view_appCenter, view_canvas, view_internet,
			view_music;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moudleselector);
		initViews();
	}

	private void initViews() {

		button_backToAp = (Button) findViewById(R.id.moudleselector_backToAp);
		view_appCenter = findViewById(R.id.moudleselector_appCenter);
		view_canvas = findViewById(R.id.moudleselector_canvas);
		view_internet = findViewById(R.id.moudleselector_internet);
		view_music = findViewById(R.id.moudleselector_music);
		view_startClass = findViewById(R.id.moudleselector_startClass);

		button_backToAp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MoudleSelectorActivity.this,
						WifiSelectorActivity.class);
				startActivity(intent);
				finish();

			}
		});
		view_startClass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startClass();

			}
		});

		view_appCenter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				appCenter();
				
			}
		});
		
		view_canvas.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				canvas();
				
			}
		});
		
		view_internet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				internet();
				
			}
		});
		view_music.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				music();
				
			}
		});
	}

	/**
	 * 开始上课
	 */
	private void startClass() {

		Intent intent = new Intent(this,ClassReadyActivity.class);
		startActivity(intent);
		finish();
		
		
	}

	/**
	 * 应用中心
	 */
	private void appCenter() {

	}

	/**
	 * 电子画板
	 */
	private void canvas() {

	}

	/**
	 * 互联网
	 */
	private void internet() {
		//暂时调整到小组选择页面
		Intent intent = new Intent(this,GroupActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 音乐
	 */
	private void music() {

	}
}
