package com.ins.feast.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ins.feast.R;
import com.ins.feast.entity.Card;
import com.ins.feast.ui.activity.CardActivity2;
import com.ins.feast.utils.AppHelper;
import com.ins.feast.utils.GlideUtil;
import com.sobey.common.interfaces.OnPagerItemClickListener;
import com.sobey.common.interfaces.OnRecycleItemClickListener;

import java.util.List;


public class PagerAdapterCard extends PagerAdapter {

    private Context context;
    private List<Card> results;

    public PagerAdapterCard(Context context, List<Card> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final Card card = results.get(position);
        //设置一大堆演示用的数据，麻里麻烦~~
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_viewpager_card, null);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPagerItemClickListener != null)
                    onPagerItemClickListener.onItemClick(card, position);
            }
        });
        ImageView img_card_img = (ImageView) itemView.findViewById(R.id.img_card_img);
        //狗日的又不要内容了
//        TextView text_title = (TextView) itemView.findViewById(R.id.text_card_title);
//        TextView text_card_content = (TextView) itemView.findViewById(R.id.text_card_content);
//        TextView text_card_salecount = (TextView) itemView.findViewById(R.id.text_card_salecount);
//        TextView text_card_price = (TextView) itemView.findViewById(R.id.text_card_price);
//        TextView text_card_unit = (TextView) itemView.findViewById(R.id.text_card_unit);
//        TextView text_card_star = (TextView) itemView.findViewById(R.id.text_card_star);

//        GlideUtil.loadImg(context, img_card_img, R.drawable.default_bk_img, "http://h.hiphotos.baidu.com/image/pic/item/f31fbe096b63f624ec82b20a8544ebf81b4ca3d8.jpg");
        GlideUtil.loadImg(context, img_card_img, R.drawable.default_bk_img, AppHelper.getRealImgPath(card.getTopBanner()));
//        text_title.setText(card.getName());
//        text_card_content.setText(card.getIntroduction());
//        text_card_salecount.setText(card.getSales() + "");
//        text_card_price.setText("￥" + card.getPrice());
//        text_card_unit.setText("/" + card.getUnits());
//        text_card_star.setText(AppHelper.getStarsStr(card.getRecommendationIndex()));

        container.addView(itemView);
        return itemView;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    private OnPagerItemClickListener<Card> onPagerItemClickListener;

    public void setOnPagerItemClickListener(OnPagerItemClickListener<Card> onPagerItemClickListener) {
        this.onPagerItemClickListener = onPagerItemClickListener;
    }
}