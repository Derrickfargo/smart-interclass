package cn.com.incito.classroom.ui.activity;

import com.alibaba.fastjson.JSONObject;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import cn.com.incito.classroom.R;
import cn.com.incito.classroom.base.BaseActivity;
import cn.com.incito.classroom.base.MyApplication;
import cn.com.incito.classroom.ui.widget.HorizontalListView;
import cn.com.incito.classroom.adapter.HorizontalListViewAdapter;
import cn.com.incito.classroom.adapter.HorizontalListViewAdapter.ViewHolder;
import cn.com.incito.common.utils.ToastHelper;
import cn.com.incito.common.utils.UIHelper;
import cn.com.incito.socket.core.CoreSocket;
import cn.com.incito.socket.core.Message;
import cn.com.incito.socket.message.DataType;
import cn.com.incito.socket.message.MessagePacking;
import cn.com.incito.socket.utils.BufferUtils;

/**
 * 修改分组信息activity Created by liguangming on 2014/7/28.
 */
public class EditGroupInfoActivity extends BaseActivity implements
		View.OnClickListener {

	private HorizontalListView mListView;
	private HorizontalListViewAdapter mListViewAdapter;

	private ImageView mPreviewImg;
	private EditText mGroupName = null;
	private ImageButton mBtnOk = null;
	private TypedArray mGroupIcons = null;
	private String mGroupIconName = "rainbow";
	private int mGroupID = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_group_info);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.containsKey("id")) {
			mGroupID = bundle.getInt("id");
		}
		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public void initView() {
		mGroupName = (EditText) findViewById(R.id.edit_group_name);
		mBtnOk = (ImageButton) findViewById(R.id.btn_ok);
		mBtnOk.setOnClickListener(this);
		mListView = (HorizontalListView) findViewById(R.id.horizon_listview);
		mPreviewImg = (ImageView) findViewById(R.id.image_preview);
		if (mGroupIcons == null)
			mGroupIcons = getResources().obtainTypedArray(R.array.groupIcons);

		String[] groupIconsName = getResources().getStringArray(
				R.array.groupicons_name);
		mListViewAdapter = new HorizontalListViewAdapter(
				getApplicationContext(), mGroupIcons, groupIconsName);
		mListView.setAdapter(mListViewAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ViewHolder tag = (ViewHolder) view.getTag();
				mGroupIconName = tag.mTitle;
				mPreviewImg.setImageDrawable(tag.mImage.getDrawable());
				mListViewAdapter.setSelectIndex(position);
				mListViewAdapter.notifyDataSetChanged();

			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
		mGroupIcons.recycle();
		mListViewAdapter = null;
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_ok:
			String groupName = mGroupName.getText().toString();
			if (TextUtils.isEmpty(groupName)) {
				int ecolor = R.color.black;
				String errorText = getResources().getText(
						R.string.group_name_null).toString();
				ForegroundColorSpan fgcSpan = new ForegroundColorSpan(ecolor);
				SpannableStringBuilder ssBuilder = new SpannableStringBuilder(
						errorText);
				ssBuilder.setSpan(fgcSpan, 0, errorText.length(), 0);
				mGroupName.setError(ssBuilder);

			} else {
				if (groupName.length() < 2) {
					ToastHelper.showCustomToast(
							this,
							getResources().getText(
									R.string.group_name_short_notice)
									.toString());
				} else {
					TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					JSONObject json = new JSONObject();
					json.put("id", mGroupID);
					json.put("imei", tm.getDeviceId());
					json.put("name", groupName);
					json.put("logo", mGroupIconName);
					MessagePacking messagePacking = new MessagePacking(
							Message.MESSAGE_GROUP_EDIT);
					messagePacking.putBodyData(DataType.INT,
							BufferUtils.writeUTFString(json.toJSONString()));
					CoreSocket.getInstance().sendMessage(messagePacking);
				}
			}
			break;
		}
	}
}
