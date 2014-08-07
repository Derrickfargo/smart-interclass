package cn.com.incito.classroom.ui.activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.ui.widget.HorizontalListView;
import cn.com.incito.classroom.adapter.HorizontalListViewAdapter;


public class EditGroupInfoActivity extends BaseActivity {

	private static final String TAG = "EditGroupInfoActivity";

	private HorizontalListView mListView;
	private HorizontalListViewAdapter mListViewAdapter;
	
	private ImageView mPreviewImg;
	private EditText mGroupName = null;
	private ImageButton mBtnOk = null;
	private TypedArray mGroupIcons = null;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_group_info);
		initView();
	}

	@Override
	protected void onStart(){
		super.onStart();
		initView();
	}
	
	public void initView(){
		mGroupName = (EditText)findViewById(R.id.edit_group_name);
		mBtnOk = (ImageButton)findViewById(R.id.btn_ok);
		mListView = (HorizontalListView)findViewById(R.id.horizon_listview);
		mPreviewImg = (ImageView)findViewById(R.id.image_preview);
		if(mGroupIcons != null)
			mGroupIcons = getResources().obtainTypedArray(R.array.groupIcons);

		mListViewAdapter = new HorizontalListViewAdapter(getApplicationContext(),mGroupIcons);
		mListView.setAdapter(mListViewAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mPreviewImg.setImageDrawable(mGroupIcons.getDrawable(position));
				mListViewAdapter.setSelectIndex(position);
				mListViewAdapter.notifyDataSetChanged();
				
			}
		});
	}

	@Override
	protected void onStop(){
		super.onStop();
		mGroupIcons.recycle();
		mListViewAdapter = null;
	}
}
