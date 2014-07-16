package cn.com.incito.classroom.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsSpinner;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.popoy.common.TAApplication;
import com.popoy.tookit.Enum.Dictionary;
import com.popoy.tookit.Enum.Enumeration;

import cn.com.incito.classroom.R;


public class CustomSpinner extends Spinner implements IDictionarizable {

	public class DictAdapter extends BaseAdapter {

		public DictAdapter(Enumeration[] dict) {
			this.dict = dict;
		}

		private Enumeration[] dict;

		@Override
		public int getCount() {
			return dict.length;
		}

		@Override
		public Object getItem(int position) {
			return dict[position];
		}

		@Override
		public long getItemId(int position) {
			return dict[position].internalId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			TextView view = (TextView) inflater.inflate(
					android.R.layout.simple_spinner_item, parent, false);
			view.setText(position == CustomSpinner.FORCE_NO_SELECTION_POSITION ? ""
					: dict[position].getName());
			return view;
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			TextView view = (TextView) inflater.inflate(
					android.R.layout.simple_spinner_dropdown_item, parent,
					false);
			view.setText(position == CustomSpinner.FORCE_NO_SELECTION_POSITION ? ""
					: dict[position].getName());
			return view;
		}

		public Enumeration[] getDict() {
			return dict;
		}
	}

	private final OnLongClickListener customSpinnerOnLongClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			CustomSpinner view = (CustomSpinner) v;
			view.setSelection(CustomSpinner.FORCE_NO_SELECTION_POSITION);
			return true;
		}
	};
	public static final int FORCE_NO_SELECTION_POSITION = -2;
	public static final Long FORCE_NO_SELECTION_ITEM_ID = Long.MIN_VALUE;
	public static final Object FORCE_NO_SELECTION_ITEM = new Object();
	protected boolean noSelection = true;
	/**
	 * 该属性仅记录从正常选择到FORCE_NO_SELECTION改变时正常选择的位置。 仅当noSelection=true时才有效
	 */
	protected int lastSelectedPosition = FORCE_NO_SELECTION_POSITION;
	/**
	 * 记录是否从FORCE_NO_SELECTION到正常选择，此时noSelection为false 使用时优先进行!noSelection判断
	 */
	protected boolean noSelection2Selection = false;
	/*
	 * 该类需要手动处理三种选择情况，其余情况均交由系统自动处理：
	 * 1、当该Spinner接收长按事件从FORCE_NO_SELECTION到FORCE_NO_SELECTION时，不发生任何事件
	 * 2、当该Spinner接收长按事件从正常选择到FORCE_NO_SELECTION时，发送FORCE_NO_SELECTION选择事件
	 * 3、当该Spinner从FORCE_NO_SELECTION选择到正常时，发送正常选择事件
	 */
	private OnItemSelectedListener mOnItemSelectedListener;

	public CustomSpinner(Context context) {
		this(context, null);
	}

	public CustomSpinner(Context context, int mode) {
		this(context, null, android.R.attr.spinnerStyle, mode);
	}

	public CustomSpinner(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.spinnerStyle);
	}

	public CustomSpinner(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs, defStyle, -1);
	}

	public CustomSpinner(Context context, AttributeSet attrs, int defStyle,
			int mode) {
		super(context, attrs, defStyle, mode);
		if (isInEditMode()) {
			return;
		}
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.customAttribute);
		final String dictName = a.getString(R.styleable.customAttribute_dict);
		a.recycle();
		Enumeration[] enus = Dictionary.getDictionary(TAApplication.getApplication().getApplicationContext(),dictName);
		if (enus != null) {
			setDictionary(enus);
			super.setOnLongClickListener(customSpinnerOnLongClickListener);
		}

	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		throw new IllegalArgumentException("");
	}

	/**
	 * {@inheritDoc}
	 */
	public void setOnItemSelectedListener(OnItemSelectedListener listener) {
		super.setOnItemSelectedListener(listener);
		mOnItemSelectedListener = listener;
	}

	@Override
	public void setSelection(int position, boolean animate) {
		if (position == FORCE_NO_SELECTION_POSITION) {
			if (noSelection) {
				// 说明从FORCE_NO_SELECTION到FORCE_NO_SELECTION所以不用做任何事情
				return;
			}
			noSelection = true;
			noSelection2Selection = false;
			requestLayout();
		} else {
			noSelection2Selection = noSelection;
			noSelection = false;
			removeAllViewsInLayout();
			if (noSelection2Selection) {
				// 注意此时position==mOldSelectedPosition,
				// super.setSelection(position, animate);调用无效
				super.setSelection(position);
			} else {
				super.setSelection(position, animate);
			}
		}
	}

	@Override
	public void setSelection(int position) {
		if (position == FORCE_NO_SELECTION_POSITION) {
			if (noSelection) {
				// 说明从FORCE_NO_SELECTION到FORCE_NO_SELECTION所以不用做任何事情
				return;
			}
			noSelection = true;
			noSelection2Selection = false;
			requestLayout();
		} else {
			noSelection2Selection = noSelection;
			noSelection = false;
			removeAllViewsInLayout();
			super.setSelection(position);
		}
	}

	@Override
	public View getSelectedView() {
		if (noSelection) {
			return getAdapter()
					.getView(FORCE_NO_SELECTION_POSITION, null, this);
		} else {
			return super.getSelectedView();
		}
	}

	@Override
	public int getSelectedItemPosition() {
		if (noSelection) {
			return FORCE_NO_SELECTION_POSITION;
		} else {
			return super.getSelectedItemPosition();
		}
	}

	@Override
	public long getSelectedItemId() {
		if (noSelection) {
			return FORCE_NO_SELECTION_ITEM_ID;
		} else {
			return super.getSelectedItemId();
		}
	}

	@Override
	public Object getSelectedItem() {
		if (noSelection) {
			return FORCE_NO_SELECTION_ITEM;
		} else {
			return super.getSelectedItem();
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (isInEditMode()) {
			super.onLayout(changed, l, t, r, b);
			return;
		}
		if (noSelection) {
			layoutOnForceNoSelection();
		} else {
			super.onLayout(changed, l, t, r, b);
		}
		checkSelectionChanged1();
		if (!noSelection) {
			lastSelectedPosition = getSelectedItemPosition();
		}
	}

	protected void layoutOnForceNoSelection() {
		removeAllViewsInLayout();
		View child = getAdapter().getView(FORCE_NO_SELECTION_POSITION, null,
				this);
		ViewGroup.LayoutParams lp = child.getLayoutParams();
		if (lp == null) {
			lp = generateDefaultLayoutParams();
		}
		addViewInLayout(child, 0, lp);
		child.setSelected(hasFocus());
		// Get measure specs
		int childHeightSpec = ViewGroup.getChildMeasureSpec(
				getMeasuredHeight(), getPaddingTop() + getPaddingBottom(),
				lp.height);
		int childWidthSpec = ViewGroup.getChildMeasureSpec(getMeasuredWidth(),
				getPaddingLeft() + getPaddingRight(), lp.width);
		// Measure child
		child.measure(childWidthSpec, childHeightSpec);
		int childLeft;
		int childRight;
		// Position vertically based on gravity setting
		int childTop = getPaddingTop()
				+ ((getMeasuredHeight() - getPaddingBottom() - getPaddingTop() - child
						.getMeasuredHeight()) / 2);
		int childBottom = childTop + child.getMeasuredHeight();
		int width = child.getMeasuredWidth();
		childLeft = 0;
		childRight = childLeft + width;
		child.layout(childLeft, childTop, childRight, childBottom);
	}

	private void fireOnSelectionChanged() {
		if (mOnItemSelectedListener == null)
			return;
		int selection = this.getSelectedItemPosition();
		if (selection >= 0) {
			View v = getSelectedView();
			mOnItemSelectedListener.onItemSelected(this, v, selection,
					getAdapter().getItemId(selection));
		} else {
			mOnItemSelectedListener.onNothingSelected(this);
		}
	}

	/**
	 * 主要检查Spinner基类不能处理的两种情况： 1、从正常选择到FORCE_NO_SELECTION
	 * 2、从FORCE_NO_SELECTION到正常选择时，和之前从正常选择到FORCE_NO_SELECTION时，正常选择的是同一个
	 */
	protected void checkSelectionChanged1() {
		// 从FORCE_NO_SELECTION到FORCE_NO_SELECTION的情况已由setSelection方法进行排除
		if (noSelection && lastSelectedPosition != FORCE_NO_SELECTION_POSITION) {
			fireOnSelectionChanged();
		}
		if (!noSelection && noSelection2Selection
				&& lastSelectedPosition == getSelectedItemPosition()) {
			fireOnSelectionChanged();
		}
		noSelection2Selection = false;
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState ss = new SavedState(superState);
		ss.noSelection = noSelection;
		ss.lastSelectedPosition = lastSelectedPosition;
		ss.noSelection2Selection = noSelection2Selection;
		return ss;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		noSelection = ss.noSelection;
		lastSelectedPosition = ss.lastSelectedPosition;
		noSelection2Selection = ss.noSelection2Selection;
		super.onRestoreInstanceState(ss.getSuperState());
		//
		if (noSelection) {
			requestLayout();
		}
	}

	static class SavedState extends BaseSavedState {

		boolean noSelection;
		int lastSelectedPosition;
		boolean noSelection2Selection;

		/**
		 * Constructor called from {@link AbsSpinner#onSaveInstanceState()}
		 */
		SavedState(Parcelable superState) {
			super(superState);
		}

		/**
		 * Constructor called from {@link #CREATOR}
		 */
		private SavedState(Parcel in) {
			super(in);
			boolean[] noSelectionArr = new boolean[1];
			in.readBooleanArray(noSelectionArr);
			noSelection = noSelectionArr[0];
			lastSelectedPosition = in.readInt();
			boolean[] noSelection2SelectionArr = new boolean[1];
			in.readBooleanArray(noSelection2SelectionArr);
			noSelection2Selection = noSelection2SelectionArr[0];
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			boolean[] noSelectionArr = new boolean[1];
			noSelectionArr[0] = noSelection;
			out.writeBooleanArray(noSelectionArr);
			out.writeInt(lastSelectedPosition);
			boolean[] noSelection2SelectionArr = new boolean[1];
			noSelection2SelectionArr[0] = noSelection2Selection;
			out.writeBooleanArray(noSelection2SelectionArr);
		}

		@Override
		public String toString() {
			return "CustomSpinner.SavedState{"
					+ Integer.toHexString(System.identityHashCode(this))
					+ " noSelection=" + noSelection + " lastSelectedPosition="
					+ lastSelectedPosition + " noSelection2Selection="
					+ noSelection2Selection + "}";
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	@Override
	public void setDictionary(Enumeration[] dict) {
		setAdapter(new DictAdapter(dict));
	}

	@Override
	public Enumeration[] getDictionary() {
		return ((DictAdapter) getAdapter()).getDict();
	}

	public void setSelected(String... code) {
		if (code == null || code.length < 1 || code[0] == null) {
			setSelection(FORCE_NO_SELECTION_POSITION);
			return;
		}
		Adapter adapter = getAdapter();
		for (int i = 0; i < adapter.getCount(); i++) {
			if (code[0].equals(((Enumeration) adapter.getItem(i)).getCode())) {
				setSelection(i);
				return;
			}
		}
		setSelection(FORCE_NO_SELECTION_POSITION);
	}

	public Enumeration getSelected() {
		int p = getSelectedItemPosition();
		return p == FORCE_NO_SELECTION_POSITION ? new Enumeration()
				: (Enumeration) getAdapter().getItem(p);
	}
}