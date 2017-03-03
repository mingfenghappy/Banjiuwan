package com.ins.feast.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.ins.feast.R;
import com.ins.feast.common.AppData;
import com.ins.feast.common.CardLayoutManager;
import com.ins.feast.common.CommonNet;
import com.ins.feast.common.ItemTouchCardCallback;
import com.ins.feast.entity.Card;
import com.ins.feast.ui.adapter.RecycleAdapterCard;
import com.ins.feast.ui.dialog.DialogLoading;
import com.ins.feast.utils.AppHelper;
import com.sobey.common.interfaces.OnRecycleItemClickListener;
import com.sobey.common.utils.FontUtils;
import com.sobey.common.utils.StrUtils;
import com.sobey.common.view.DotView;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardActivity extends BaseAppCompatActivity implements OnRecycleItemClickListener {

    private RecyclerView recyclerView;
    private List<Card> results = new ArrayList<>();
    private RecycleAdapterCard adapter;

    private DotView dotView;

    private DialogLoading dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        setToolbar(null, false);

        initBase();
        initView();
        initCtrl();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogLoading != null) dialogLoading.dismiss();
    }

    private void initBase() {
        dialogLoading = new DialogLoading(this);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        dotView = (DotView) findViewById(R.id.dotView);
        FontUtils.boldText(findViewById(R.id.text_toolbar_title));
        FontUtils.boldText(findViewById(R.id.text_card_title));
        FontUtils.boldText(findViewById(R.id.text_card_price));
    }

    private void initData() {
//        本地数据测试 start
//        dialogLoading.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                results.clear();
//                results.add(new Card(0));
//                results.add(new Card(1));
//                results.add(new Card(2));
//                results.add(new Card(3));
//                results.add(new Card(4));
//                results.add(new Card(5));
//                results.add(new Card(6));
//                Collections.reverse(results);
//                freshCtrl();
//                dialogLoading.hide();
//            }
//        }, 2000);
//        本地数据测试 end

        //请求服务器卡片数据
        netGetCards();
    }

    private void initCtrl() {
        adapter = new RecycleAdapterCard(this, results);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new CardLayoutManager());
        recyclerView.setAdapter(adapter);

        final ItemTouchCardCallback callback = new ItemTouchCardCallback(recyclerView, results);
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
        callback.setOnSwipedListener(new ItemTouchCardCallback.OnSwipedListener<Card>() {
            @Override
            public void onSwiped(Card card, int direction) {
                dotView.setIndicator(card.getIndex());
            }
        });
    }

    private void freshCtrl() {
        adapter.notifyDataSetChanged();
        dotView.setDots(results.size());
    }

    private void netGetCards() {
        RequestParams params = new RequestParams(AppData.Url.queryByCategory);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("foodCategoryId", "10");

        CommonNet.samplepost(params, new TypeToken<List<Card>>() {
        }.getType(), new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "接口异常");
                else {
                    List<Card> users = (ArrayList<Card>) pojo;
                    if (!StrUtils.isEmpty(users)) {
                        results.clear();
                        results.addAll(users);
                        //给数据设置Index标志，标示dotview的位置
                        AppHelper.initIndex(results);
                        Collections.reverse(CardActivity.this.results);
                        freshCtrl();
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(CardActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void netStart(int status) {
                dialogLoading.show();
            }

            @Override
            public void netEnd(int status) {
                dialogLoading.hide();
            }
        });
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder) {
        Card card = adapter.getResults().get(viewHolder.getLayoutPosition());
        Toast.makeText(this, "click:" + card.getName(), Toast.LENGTH_SHORT).show();
    }
}
