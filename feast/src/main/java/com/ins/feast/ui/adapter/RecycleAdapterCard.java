package com.ins.feast.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ins.feast.R;
import com.ins.feast.entity.Card;
import com.ins.feast.utils.AppHelper;
import com.ins.feast.utils.GlideUtil;
import com.sobey.common.interfaces.OnRecycleItemClickListener;
import com.sobey.common.utils.SpannableStringUtils;
import com.sobey.common.utils.StrUtils;

import java.util.List;


public class RecycleAdapterCard extends RecyclerView.Adapter<RecycleAdapterCard.Holder> {

    private Context context;
    private List<Card> results;

    public List<Card> getResults() {
        return results;
    }

    public RecycleAdapterCard(Context context, List<Card> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public RecycleAdapterCard.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_card, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleAdapterCard.Holder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(holder);
            }
        });
        Card card = results.get(position);

        holder.text_title.setText(card.getName());
        GlideUtil.loadImg(context, holder.img_card_img, R.drawable.default_bk_img, AppHelper.getRealImgPath(card.getTopBanner()));
        holder.text_card_content.setText(card.getIntroduction());
        holder.text_card_salecount.setText(card.getSales() + "");
        holder.text_card_price.setText("￥" + card.getPrice());
        holder.text_card_unit.setText("/" + card.getUnits());
        holder.text_card_star.setText(AppHelper.getStarsStr(card.getRecommendationIndex()));

//        SpannableStringUtils.create()
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView img_card_img;
        private TextView text_title;
        private TextView text_card_content;
        private TextView text_card_salecount;
        private TextView text_card_price;
        private TextView text_card_unit;
        private TextView text_card_star;

        public Holder(View itemView) {
            super(itemView);
            img_card_img = (ImageView) itemView.findViewById(R.id.img_card_img);
            text_title = (TextView) itemView.findViewById(R.id.text_card_title);
            text_card_content = (TextView) itemView.findViewById(R.id.text_card_content);
            text_card_salecount = (TextView) itemView.findViewById(R.id.text_card_salecount);
            text_card_price = (TextView) itemView.findViewById(R.id.text_card_price);
            text_card_unit = (TextView) itemView.findViewById(R.id.text_card_unit);
            text_card_star = (TextView) itemView.findViewById(R.id.text_card_star);
        }
    }

    private OnRecycleItemClickListener listener;

    public void setOnItemClickListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }
}
