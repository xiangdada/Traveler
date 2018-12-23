package com.cijianyouqing.traveler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cijianyouqing.traveler.R;
import com.cijianyouqing.traveler.bean.MapSearchData;

import java.util.List;

/**
 * Created by xiangpengfei on 2018/9/26.
 */
public class MapSearchAdapter extends AbstractAdapter<MapSearchData> {

    public MapSearchAdapter(Context context, List<MapSearchData> datas) {
        super(context, datas);
    }

    @Override
    protected AbstractViewHolder onCreateViewHolder(int position, ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_map_search, parent, false));
    }

    @Override
    protected void onBindViewHolder(AbstractViewHolder abstractViewHolder, int position, MapSearchData data) {
        ViewHolder holder = (ViewHolder) abstractViewHolder;
        if (holder != null && data != null) {
            holder.tv_title.setText(data.getTitle());
            holder.tv_address.setText(data.getAddressDiscription());
        }
    }

    private static class ViewHolder extends AbstractViewHolder {
        private TextView tv_title;
        private TextView tv_address;

        public ViewHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_title);
            tv_address = view.findViewById(R.id.tv_address);
        }
    }

}
