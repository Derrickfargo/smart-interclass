package cn.com.incito.classroom.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.common.utils.UIHelper;


public class EvaluateActivity extends BaseActivity implements OnClickListener{
	private Button button_one;
	private Button button_two;
	private Button button_three;
	private Button pager_up;
	private Button pager_down;
	private Button number;
	private Map<String,Bitmap> picture=new HashMap<String, Bitmap>();//作业，key是作业ID，value是作业图片
	private ArrayList<Map<String,Bitmap>> paperlist=new ArrayList<Map<String,Bitmap>>();//存在所有作业的list
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setContentView(R.layout.evaluate_activity);
		UIHelper.getInstance().setEvaluateActivity(this);
		initView();
	}
	private void initView() {
		pager_up=(Button)findViewById(R.id.pager_up);
		pager_up.setOnClickListener(this);
		pager_down=(Button)findViewById(R.id.pager_down);
		pager_down.setOnClickListener(this);
		button_one=(Button)findViewById(R.id.number_one);
		button_one.setOnClickListener(this);
		button_two=(Button)findViewById(R.id.number_two);
		button_two.setOnClickListener(this);
		button_three=(Button)findViewById(R.id.number_three);
		button_three.setOnClickListener(this);
		number=(Button)findViewById(R.id.number);
	}
	@Override
	public void onClick(View v) {
		int viewID=v.getId();
		switch (viewID) {
		case R.id.pager_up:
			break;
		case R.id.pager_down:
			break;
		case R.id.number_one:
			number.setText("1");
			break;
		case R.id.number_two:
			number.setText("2");
			break;
		case R.id.number_three:
			number.setText("3");
			break;
		default:
			break;
		}
	}
	
	public Map<String, Bitmap> getPicture() {
		return picture;
	}
	
	public void setPicture(Map<String, Bitmap> picture) {
		this.picture = picture;
	}
	
}
