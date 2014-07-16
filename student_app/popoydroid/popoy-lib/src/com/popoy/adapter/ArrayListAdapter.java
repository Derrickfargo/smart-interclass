package com.popoy.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public abstract class ArrayListAdapter<T> extends BaseAdapter {
	protected Activity mContext;
	protected List<T> mList;
	protected ListView mListView;

	public ArrayListAdapter(Activity paramActivity) {
		this.mContext = paramActivity;
		this.mList = new ArrayList();
	}

	public ArrayListAdapter(Activity paramActivity, List<T> paramList) {
		this.mContext = paramActivity;
		this.mList = paramList;
	}

	public Activity getActivity() {
		return this.mContext;
	}

	public int getCount() {
		if (this.mList != null)
			return this.mList.size();
		return 0;
	}

	public Object getItem(int paramInt) {
		if (this.mList == null)
			return null;
		return this.mList.get(paramInt);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public List<T> getList() {
		return this.mList;
	}

	public ListView getListView() {
		return this.mListView;
	}

	public abstract View getView(int paramInt, View paramView,
			ViewGroup paramViewGroup);

	@SuppressWarnings("unchecked")
	public void setCopyList(List<T> paramList) {
		if (this.mList != null)
			this.mList.clear();
		while (true) {
			if (paramList != null) {
				if (this.mList == null) {
					this.mList = new ArrayList();
				}
				this.mList.addAll(paramList);
			}
			notifyDataSetChanged();
			return;

		}
	}

	public void setList(List<T> paramList) {
		this.mList = paramList;
		notifyDataSetChanged();
	}

	public void setList(T[] paramArrayOfT) {
		ArrayList localArrayList = new ArrayList(paramArrayOfT.length);
		int i = paramArrayOfT.length;
		for (int j = 0; j < i; j++)
			localArrayList.add(paramArrayOfT[j]);
		setList(localArrayList);
	}

	public void setListView(ListView paramListView) {
		this.mListView = paramListView;
	}

	public void setMoreList(List<T> paramList) {
		if (paramList != null)
			this.mList.addAll(paramList);
		notifyDataSetChanged();
	}
}