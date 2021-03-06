package com.ins.feast.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.ins.feast.R;
import com.ins.feast.entity.Position;
import com.ins.feast.ui.adapter.SearchLocationAdapter;
import com.ins.feast.utils.NetCouldOrderHelper;
import com.ins.middle.ui.activity.BaseFeastActivity;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.sobey.common.interfaces.OnRecycleItemClickListener;
import com.sobey.common.utils.StrUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class SearchLocationActivity extends BaseFeastActivity implements OnGetPoiSearchResultListener, OnRecycleItemClickListener {
    private final static String CITY_KEY = "key";
    private final static String TRANSITION_RED_BG = "redBg";
    private final static String TRANSITION_WHITE_BG = "whiteBg";
    private static final String TRANSITION_TEXT = "text";
    private String city;
    private PoiSearch poiSearch;
    private SearchLocationAdapter adapter;
    private RecyclerView searchResult;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);

        initBase();
        initSetting();
        initView();
    }

    private void initBase() {
        if (getIntent().hasExtra("type")) {
            type = getIntent().getIntExtra("type", 0);
        }
    }

    private void initSetting() {
        city = getIntent().getStringExtra(CITY_KEY);
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);
    }

    private void initView() {
        EditText search = (EditText) findViewById(R.id.editText);
        searchResult = (RecyclerView) findViewById(R.id.searchResult);
        RxTextView.
                textChanges(search).
                debounce(300, TimeUnit.MILLISECONDS).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        search(charSequence);
                    }
                });
    }

    private void search(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            setAdapterData(null);
            return;
        }
        PoiCitySearchOption poiCitySearchOption = new PoiCitySearchOption();
        poiCitySearchOption.city(city).keyword(charSequence.toString());
        poiSearch.searchInCity(poiCitySearchOption);
    }

    public static void start(ChooseLocationActivity activity, String city ,int type) {
        Intent starter = new Intent(activity, SearchLocationActivity.class);
        starter.putExtra("type", type);
        starter.putExtra(CITY_KEY, city);
        View redBg = activity.findViewById(R.id.searchLocation);
        View whiteBg = activity.findViewById(R.id.transition_whiteBg);
        View text = activity.findViewById(R.id.transition_text);
        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity,
                        Pair.create(redBg, TRANSITION_RED_BG),
                        Pair.create(whiteBg, TRANSITION_WHITE_BG),
                        Pair.create(text, TRANSITION_TEXT));

        ActivityCompat.startActivity(activity, starter, optionsCompat.toBundle());
    }

    public void onClick_back(View view) {
        onBackPressed();
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        List<PoiInfo> allPoi = poiResult.getAllPoi();
        setAdapterData(allPoi);
        if (StrUtils.isEmpty(allPoi)) {
            Toast.makeText(this, "没有搜索该地址", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapterData(@Nullable List<PoiInfo> allPoi) {
        if (adapter == null) {
            adapter = new SearchLocationAdapter(this, allPoi);
            searchResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            adapter.setListener(this);
            searchResult.setAdapter(adapter);
        } else {
            adapter.resetData(allPoi);
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onItemClick(final RecyclerView.ViewHolder viewHolder) {
        PoiInfo poiInfo = adapter.getPoiInfoList().get(viewHolder.getAdapterPosition());
        if (poiInfo == null) {
            return;
        }
        final Position position = new Position(poiInfo);
        position.setType(type);
        if (type==2) {
            //新需求导致的畸形逻辑，返回前请求服务器是否可以点餐
            NetCouldOrderHelper.netCouldOrder(this, position.getLatLng(), new NetCouldOrderHelper.CouldOrderCallback() {
                @Override
                public void succese(int couldOrder) {
                    if (couldOrder == 1) {
                        EventBus.getDefault().post(position);
                        finish();
                    } else {
                        Toast.makeText(SearchLocationActivity.this, "当前位置无法点餐", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            EventBus.getDefault().post(position);
            finish();
        }
    }

    public void onClick_cancel(View view) {
        onBackPressed();
    }

}
