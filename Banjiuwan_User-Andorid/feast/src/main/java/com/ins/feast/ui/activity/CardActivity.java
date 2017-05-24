package com.ins.feast.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.ins.feast.R;
import com.ins.feast.common.CardLayoutManager;
import com.ins.feast.common.ItemTouchCardCallback;
import com.ins.feast.entity.Card;
import com.ins.feast.ui.adapter.RecycleAdapterCard;
import com.ins.feast.utils.AppHelper;
import com.ins.middle.common.AppData;
import com.ins.middle.common.CommonNet;
import com.ins.middle.entity.WebEvent;
import com.ins.middle.ui.activity.BaseFeastActivity;
import com.ins.middle.ui.dialog.DialogLoading;
import com.jaeger.library.StatusBarUtil;
import com.sobey.common.interfaces.OnRecycleItemClickListener;
import com.sobey.common.utils.FontUtils;
import com.sobey.common.utils.StrUtils;
import com.sobey.common.view.DotView;

import org.greenrobot.eventbus.Subscribe;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardActivity extends BaseFeastActivity implements OnRecycleItemClickListener {

    private final static String KEY_CARD_DETAIL = "cardType";
    private RecyclerView recyclerView;
    private List<Card> results = new ArrayList<>();
    private RecycleAdapterCard adapter;
    private DotView dotView;
    private DialogLoading dialogLoading;
    private AppData.CardType cardType;

    public static void start(Context context, AppData.CardType cardType) {
        Intent starter = new Intent(context, CardActivity.class);
        starter.putExtra(KEY_CARD_DETAIL, cardType);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        setToolbar(null, false);
        setEventBusSupport();

        initBase();
        initView();
        initCtrl();
        initData();
        adjustBgAndTitle();
    }

    private void adjustBgAndTitle() {
//        StatusBarUtil.setColorNoTranslucent(this, cardType.getBgColor());
//        findViewById(R.id.root).setBackgroundColor(cardType.getBgColor());
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this,R.color.jw_red));
        findViewById(R.id.root).setBackgroundColor(ContextCompat.getColor(this,R.color.jw_red));
        TextView title = (TextView) findViewById(R.id.text_toolbar_title);
        title.setText(cardType.getTitle());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogLoading != null) dialogLoading.dismiss();
    }

    private void initBase() {
        cardType = (AppData.CardType) getIntent().getSerializableExtra(KEY_CARD_DETAIL);
        dialogLoading = new DialogLoading(this);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        dotView = (DotView) findViewById(R.id.dotView);
        findViewById(R.id.toolbar_leftIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        params.addBodyParameter("foodCategoryId", cardType.getCategoryId());

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
        int id = card.getId();
        /*卡片式页面的详情页只会从下面这行代码跳转到，因此写死参数isOpen=4*/
        CommonWebActivity.start(this, cardType.getBaseUrl() + "?foodId=" + id + "&isOpen=4");
    }

    @Subscribe
    public void onWebEvent(WebEvent webEvent) {
        if (webEvent == WebEvent.finishActivity) {
            finish();
        }
    }
}
