package com.sobey.common.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sobey.common.R;
import com.sobey.common.common.LoadingViewUtil;
import com.sobey.common.entity.SelectEntity;
import com.sobey.common.interfaces.CharSort;
import com.sobey.common.interfaces.CharSortAdapter;
import com.sobey.common.common.CharacterParser;
import com.sobey.common.view.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2016/6/2 0002.
 */
public abstract class BaseSelectFragment extends Fragment {

    protected int position;
    protected ViewGroup showingroup;
    protected View showin;

    protected ListView sortListView;
    protected SideBar sideBar;
    protected TextView dialog;
    protected CharSortAdapter adapter;
    protected EditText mClearEditText;

    protected View text_search;
    /**
     * 汉字转换成拼音的类
     */
    protected CharacterParser characterParser;
    protected List<CharSort> SourceDateList = new ArrayList<CharSort>();

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    protected PinyinComparatorCharSort pinyinComparator;

    public static Fragment newInstance(int position) {
        //必须要实现的方法
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.position = getArguments().getInt("position");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contactold, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBase();
        initView();
        initData();
        initCtrl();
    }

    protected abstract void initBaseMe();

    protected abstract void initViewMe();

    protected abstract void initCtrlMe();

    private void initBase() {
        initBaseMe();
    }

    private void initView() {
        showingroup = (ViewGroup) getView().findViewById(R.id.showingroup);
        text_search = getView().findViewById(R.id.text_comp_search);
        mClearEditText = (EditText) getView().findViewById(R.id.filter_edit);
        initViewMe();
    }

    protected void initData() {
        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading_common, showin);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载成功
                ArrayList<CharSort> CharSorts = new ArrayList<>();
                CharSorts.add(new SelectEntity("张全蛋", "啥时候还我钱啊", "http://img0.imgtn.bdimg.com/it/u=407111158,1901948900&fm=21&gp=0.jpg"));
                CharSorts.add(new SelectEntity("张三", "李四在哪了？", "http://img3.imgtn.bdimg.com/it/u=126707278,3863598239&fm=21&gp=0.jpg"));
                CharSorts.add(new SelectEntity("王尼玛", "蛋蛋打麻将去了", "http://img3.imgtn.bdimg.com/it/u=2966925534,3303838549&fm=21&gp=0.jpg"));
                CharSorts.add(new SelectEntity("美尼玛", "兔子，约不约", "http://img5.imgtn.bdimg.com/it/u=3417293762,3718020601&fm=21&gp=0.jpg"));
                CharSorts.add(new SelectEntity("暴尼玛1", "之所以给你留言，是因为老板还没发工资，导致心情不好", "http://v1.qzone.cc/avatar/201407/23/11/01/53cf257510369254.jpg!200x200.jpg"));
                SourceDateList.clear();
                SourceDateList.addAll(CharSorts);

                freshCtrl();
                LoadingViewUtil.showout(showingroup, showin);

                //加载失败
//                LoadingViewUtil.showin(showingroup,R.layout.layout_lack,showin,new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//                        initData();
//                    }
//                });
            }
        }, 1000);

//        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading, showin);
//
//        RequestParams params = new RequestParams(AppData.Url.getTv);
//        params.addBodyParameter("officeId", officeid + "");
//        CommonNet.samplepost(params, TVStationPojo.class, new CommonNet.SampleNetHander() {
//            @Override
//            public void netGo(int code, Object pojo, String text, Object obj) {
//                if (pojo == null) netSetError(code, "接口异常");
//                else {
//                    TVStationPojo tvStationPojo = (TVStationPojo) pojo;
//                    List<TVStation> tvStations = tvStationPojo.getDataList();
//                    SourceDateList.clear();
//                    SourceDateList.addAll(tvStations);
//
//                    freshCtrl();
//                    LoadingViewUtil.showout(showingroup, showin);
//                }
//            }
//
//            @Override
//            public void netSetError(int code, String text) {
//                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
//                showin = LoadingViewUtil.showin(showingroup, R.layout.layout_fail, showin, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        initData();
//                    }
//                });
//            }
//        });
    }

    private void initCtrl() {

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparatorCharSort();

        sideBar = (SideBar) getView().findViewById(R.id.sidrbar);
        dialog = (TextView) getView().findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        sortListView = (ListView) getView().findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                CharSort charSort = adapter.getResults().get(pos);
                Log.e("liao", "click");
            }
        });

        //根据输入框输入值的改变来过滤搜索
        if (mClearEditText != null) {
            mClearEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                    filterData(s.toString());
                    if ("".equals(s.toString())) {
                        if (text_search != null) text_search.setVisibility(View.VISIBLE);
                    } else {
                        if (text_search != null) text_search.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        SourceDateList = filledData(SourceDateList);

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);

        initCtrlMe();
    }

    public void freshCtrl() {
        SourceDateList = filledData(SourceDateList);
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter.updateListView(SourceDateList);
//        adapter.notifyDataSetChanged();
    }

    /**
     * 为ListView填充数据
     *
     * @return
     */
    private List<CharSort> filledData(List<CharSort> data) {
        for (int i = 0; i < data.size(); i++) {
            CharSort sortModel = data.get(i);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(data.get(i).getCar_title());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
        }
        return data;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<CharSort> filterDateList = new ArrayList<CharSort>();

        if (TextUtils.isEmpty(filterStr)) {
            for (CharSort carType : SourceDateList) {
                if (carType.getCar_title_html() != null) {
                    carType.setCar_title_html(null);
                }
            }
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (CharSort sortModel : SourceDateList) {
                String name = sortModel.getCar_title();
                match(filterDateList, sortModel, filterStr);
            }
        }

//        if (StrUtils.isEmpty(filterDateList)) {
//            showin = LoadingViewUtil.showinlack(this, R.drawable.icon_theme_search, "没有搜索结果", showin);
//        }else{
//            LoadingViewUtil.showout(this, showin);
//        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    /**
     * 匹配字符串
     *
     * @param sortModel
     * @param filterStr
     */
    private int matchText(CharSort sortModel, String filterStr) {
        int sellingcount = 0;
        String name = sortModel.getCar_title();
        String[] sellingArray = characterParser.getSellingArray(name);
        for (String selling : sellingArray) {
            if ("".equals(filterStr)) {
                return sellingcount;
            }
            if (filterStr.startsWith(selling)) {
                sellingcount++;
                filterStr = filterStr.substring(selling.length(), filterStr.length());
            } else if (selling.startsWith(filterStr)) {
                sellingcount++;
                return sellingcount;
            } else {
                return 0;
            }
        }
        return sellingcount;
    }

    private void match(List<CharSort> filterDateList, CharSort sortModel, String filterStr) {
        boolean isMatch = false;
        String car_title = sortModel.getCar_title();
        int sellingCount = matchText(sortModel, filterStr);
        if (sellingCount != 0) {
            isMatch = true;
            sortModel.setCar_title_html("<font color='#f08519'><b>" + car_title.substring(0, sellingCount) + "</b></font>" + car_title.substring(sellingCount));
        }

        int index = car_title.toLowerCase().indexOf(filterStr.toLowerCase().toString());
        int length = filterStr.length();
        if (index != -1) {
            isMatch = true;
            sortModel.setCar_title_html(car_title.substring(0, index) + "<font color='#f08519'><b>" + filterStr + "</b></font>" + car_title.substring(index + length));
        }

        if (isMatch) {
            filterDateList.add(sortModel);
        }
    }

    public class PinyinComparatorCharSort implements Comparator<CharSort> {

        public int compare(CharSort o1, CharSort o2) {
            if (o1.getSortLetters().equals("@")
                    || o2.getSortLetters().equals("#")) {
                return -1;
            } else if (o1.getSortLetters().equals("#")
                    || o2.getSortLetters().equals("@")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }

    }
}
