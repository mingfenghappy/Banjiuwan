package com.ins.feast.ui.adapter;

import android.content.Context;
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
 * date 2017/2/21 14:23
 * desc ${TODO}
 */

public class ChooseLocationAdapter extends RecyclerView.Adapter<ChooseLocationAdapter.Holder> {
    private List<PoiInfo> poiInfoList;

    private LayoutInflater inflater;
    private OnRecycleItemClickListener listener;

    public List<PoiInfo> getPoiInfoList() {
        return poiInfoList;
    }

    public ChooseLocationAdapter(Context context, List<PoiInfo> poiInfoList) {
        this.poiInfoList = poiInfoList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = inflater.inflate(R.layout.item_chooselocation, parent, false);
        return new Holder(inflate);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        PoiInfo poiInfo = poiInfoList.get(position);
        holder.location.setText(poiInfo.name);
    }

    @Override
    public int getItemCount() {
        return poiInfoList != null ? poiInfoList.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView location;

        public Holder(View itemView) {
            super(itemView);
            location = (TextView) itemView.findViewById(R.id.location);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(this);
            }
        }
    }

    public void resetData(List<PoiInfo> poiInfoList) {
        this.poiInfoList = poiInfoList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }
}
