package com.ins.feast.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ins.feast.R;
import com.sobey.common.utils.StrUtils;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public class BaseFragment extends Fragment {

    protected Toolbar toolbar;

    public void setToolbar(){
        setToolbar(null);
    }

    public void setToolbar(String title){
        View root = getView();
        if (root==null){
            return;
        }
        toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        if (toolbar!=null) {
            toolbar.setNavigationIcon(R.drawable.icon_back);
            toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.sb_text_blank));
            toolbar.setTitle("");
            //设置toobar居中文字
            TextView text_title = (TextView) root.findViewById(R.id.text_toolbar_title);
            if (text_title != null) {
                if (!StrUtils.isEmpty(title)) {
                    text_title.setText(title);
                }
            }
        }
    }

    /**
     * <TextView
     android:layout_width="wrap_content"
     android:layout_height="wrap_content"
     android:layout_gravity="center"
     android:text="工作台"
     android:textColor="@color/sb_text_blank_deep"
     android:textSize="@dimen/text_subig" />
     */
}
