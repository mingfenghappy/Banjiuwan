package com.sobey.common.interfaces;

import android.widget.SectionIndexer;

import java.util.List;

/**
 * Created by Administrator on 2016/7/6 0006.
 */
public interface CharSortAdapter extends SectionIndexer {
    public List<CharSort> getResults();
    public void updateListView(List<CharSort> results);
}
