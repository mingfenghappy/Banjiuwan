package com.ins.feast.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.ins.feast.R;
import com.ins.feast.entity.Card;
import com.ins.feast.ui.adapter.PagerAdapterCard;
import com.ins.feast.utils.AppHelper;
import com.ins.feast.utils.GlideUtil;
import com.ins.middle.common.AppData;
import com.ins.middle.common.CommonNet;
import com.ins.middle.entity.WebEvent;
import com.ins.middle.ui.activity.BaseFeastActivity;
import com.ins.middle.ui.dialog.DialogLoading;
import com.jaeger.library.StatusBarUtil;
import com.sobey.common.common.MyZoomOutPageTransformer;
import com.sobey.common.interfaces.OnPagerItemClickListener;
import com.sobey.common.interfaces.OnRecycleItemClickListener;
import com.sobey.common.utils.FontUtils;
import com.sobey.common.utils.StrUtils;
import com.sobey.common.view.DotView;

import org.greenrobot.eventbus.Subscribe;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 和CardActivity启动方式、参数等都一样，只不过客户需求另一种滑动样式，使用ViewPager实现，所以新建一个CardActivity2，原来的CardActivity依然保留，以防客户又要求改回来
 */
public class CardActivity2 extends BaseFeastActivity implements OnPagerItemClickListener<Card> {

    private ViewPager viewPager;
    private PagerAdapterCard pagerAdapter;
    private List<Card> results = new ArrayList<>();
    private DotView dotView;

    private DialogLoading dialogLoading;

    private final static String KEY_CARD_DETAIL = "cardType";
    private AppData.CardType cardType;

    public static void start(Context context, AppData.CardType cardType) {
        Intent starter = new Intent(context, CardActivity2.class);
        starter.putExtra(KEY_CARD_DETAIL, cardType);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card2);
        setToolbar(null, false);
        setEventBusSupport();

        initBase();
        initView();
        initData();
//        initCtrl();
        adjustBgAndTitle();
    }

    private void adjustBgAndTitle() {
        if (cardType != null) {
//            StatusBarUtil.setColorNoTranslucent(this, cardType.getBgColor());
//            findViewById(R.id.root).setBackgroundColor(cardType.getBgColor());
            StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(this,R.color.jw_red));
            findViewById(R.id.root).setBackgroundColor(ContextCompat.getColor(this,R.color.jw_red));
            TextView title = (TextView) findViewById(R.id.text_toolbar_title);
            title.setText(cardType.getTitle());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogLoading != null) dialogLoading.dismiss();
    }

    private void initBase() {
        if (getIntent().hasExtra(KEY_CARD_DETAIL)) {
            cardType = (AppData.CardType) getIntent().getSerializableExtra(KEY_CARD_DETAIL);
        }
        dialogLoading = new DialogLoading(this);
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        dotView = (DotView) findViewById(R.id.dotView);
        findViewById(R.id.toolbar_leftIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FontUtils.boldText(findViewById(R.id.text_toolbar_title));
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
//                initCtrl();
//                dialogLoading.hide();
//            }
//        }, 2000);
//        本地数据测试 end

        //请求服务器卡片数据
        netGetCards();
    }

    private void initCtrl() {
        pagerAdapter = new PagerAdapterCard(this, results);
        pagerAdapter.setOnPagerItemClickListener(this);
        //设置缓存数为展示的数目
        viewPager.setOffscreenPageLimit(results.size());
//        viewPager.setPageMargin(4);
        viewPager.setPageTransformer(true, new MyZoomOutPageTransformer(0.9f, 0.9f, 1));
        viewPager.setAdapter(pagerAdapter);

        dotView.setViewPager(viewPager);
    }

    @Override
    public void onItemClick(Card card, int position) {
        int id = card.getId();
        /*卡片式页面的详情页只会从下面这行代码跳转到，因此写死参数isOpen=4*/
        CommonWebActivity.start(this, cardType.getBaseUrl() + "?foodId=" + id + "&isOpen=4");
    }

    private void netGetCards() {
        RequestParams params = new RequestParams(AppData.Url.queryByCategory);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("foodCategoryId", cardType.getCategoryId());
//        params.addBodyParameter("foodCategoryId", "10");

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
                        //Collections.reverse(CardActivity2.this.results);
                        initCtrl();
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(CardActivity2.this, text, Toast.LENGTH_SHORT).show();
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


    @Subscribe
    public void onWebEvent(WebEvent webEvent) {
        if (webEvent == WebEvent.finishActivity) {
            finish();
        }
    }
}
