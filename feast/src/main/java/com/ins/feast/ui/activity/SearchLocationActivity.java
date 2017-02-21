package com.ins.feast.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.ins.feast.R;
import com.ins.feast.entity.Position;
import com.ins.feast.ui.adapter.ChooseLocationAdapter;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.sobey.common.interfaces.OnRecycleItemClickListener;
import com.sobey.common.utils.L;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rx.functions.Action1;

public class SearchLocationActivity extends BaseAppCompatActivity implements OnGetPoiSearchResultListener, OnRecycleItemClickListener {
    private final static String CITY_KEY = "key";
    private String city;
    private PoiSearch poiSearch;
    private ChooseLocationAdapter adapter;
    private RecyclerView searchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        initSetting();
        initView();
    }

    private void initSetting() {
        city = getIntent().getStringExtra(CITY_KEY);
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(this);
    }

    private void initView() {
        EditText editText = (EditText) findViewById(R.id.editText);
        searchResult = (RecyclerView) findViewById(R.id.searchResult);
        RxTextView.textChanges(editText).subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                search(charSequence);
            }
        });
    }

    private void search(CharSequence charSequence) {
        PoiCitySearchOption poiCitySearchOption = new PoiCitySearchOption();
        poiCitySearchOption.city(city).keyword(charSequence.toString());
        poiSearch.searchInCity(poiCitySearchOption);
    }

    public static void start(Context context, String city) {
        Intent starter = new Intent(context, SearchLocationActivity.class);
        starter.putExtra(CITY_KEY, city);
        context.startActivity(starter);
    }

    public void onClick_back(View view) {
        onBackPressed();
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        List<PoiInfo> allPoi = poiResult.getAllPoi();
        L.d("searchResultSize:" + (allPoi != null ? allPoi.size() : 0));
        if (adapter == null) {
            adapter = new ChooseLocationAdapter(this, allPoi);
            searchResult.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            adapter.setOnItemClickListener(this);
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
    public void onItemClick(RecyclerView.ViewHolder viewHolder) {
        PoiInfo poiInfo = adapter.getPoiInfoList().get(viewHolder.getAdapterPosition());
        if (poiInfo == null) {
            return;
        }
        Position position = new Position(poiInfo);
        EventBus.getDefault().post(position);
        finish();
    }
}
