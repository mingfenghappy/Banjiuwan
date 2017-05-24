package com.sobey.common.ui.adapter;

import android.content.Context;
import android.widget.BaseAdapter;


import com.sobey.common.interfaces.CharSort;
import com.sobey.common.interfaces.CharSortAdapter;

import java.util.List;

public abstract class BaseSelectListAdapter extends BaseAdapter implements CharSortAdapter {
	protected List<CharSort> results = null;
	protected Context mContext;

	public BaseSelectListAdapter(Context mContext, List<CharSort> results) {
		this.mContext = mContext;
		this.results = results;
	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 */
	@Override
	public void updateListView(List<CharSort> results){
		this.results = results;
		notifyDataSetChanged();
	}

	@Override
	public List<CharSort> getResults() {
		return results;
	}

	public int getCount() {
		return this.results.size();
	}

	public CharSort getItem(int position) {
		return results.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return results.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = results.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}