package com.ins.feast.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.ins.feast.R;
import com.sobey.common.interfaces.OnRecycleItemClickListener;

import java.util.List;

/**
 * author 边凌
 * date 2017/2/24 11:31
 * desc ${TODO}
 */

public class SearchLocationAdapter extends RecyclerView.Adapter<SearchLocationAdapter.Holder> {
    private List<PoiInfo> poiInfoList;

    private LayoutInflater inflater;
    private OnRecycleItemClickListener listener;
    public SearchLocationAdapter(Context context, List<PoiInfo> poiInfoList) {
        this.poiInfoList = poiInfoList;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = inflater.inflate(R.layout.item_searchlocation, parent, false);
        return new Holder(inflate);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        PoiInfo poiInfo = poiInfoList.get(position);
        holder.address.setText(poiInfo.address);
        holder.name.setText(poiInfo.name);
    }

    @Override
    public int getItemCount() {
        return poiInfoList != null ? poiInfoList.size() : 0;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;

        private TextView address;
        Holder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.searchLocation_name);
            address = (TextView) itemView.findViewById(R.id.searchLocation_address);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(this);
            }
        }

    }
    public List<PoiInfo> getPoiInfoList() {
        return poiInfoList;
    }

    public void resetData(@Nullable List<PoiInfo> poiInfoList) {
        this.poiInfoList = poiInfoList;
        notifyDataSetChanged();
    }
}
