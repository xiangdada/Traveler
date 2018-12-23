package com.cijianyouqing.traveler.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangpengfei on 2018/9/26.
 */
public abstract class AbstractAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;

    public AbstractAdapter(Context context, List<T> datas) {
        mContext = context;
        if (datas == null) {
            mDatas = new ArrayList<>();
        } else {
            mDatas = datas;
        }
    }

    public void notifyDataSetChanged(List<T> datas) {
        if (datas == null) {
            mDatas = new ArrayList<>();
        } else {
            mDatas = datas;
        }
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mDatas;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AbstractViewHolder abstractViewHolder = null;
        if (convertView == null) {
            abstractViewHolder = onCreateViewHolder(position, parent);
            convertView = abstractViewHolder.getView();
            convertView.setTag(abstractViewHolder);
        } else {
            abstractViewHolder = (AbstractViewHolder) convertView.getTag();
        }
        onBindViewHolder(abstractViewHolder, position, mDatas.get(position));
        return convertView;
    }

    protected abstract AbstractViewHolder onCreateViewHolder(int position, ViewGroup parent);

    protected abstract void onBindViewHolder(AbstractViewHolder abstractViewHolder, int position, T data);

    public static class AbstractViewHolder {
        private View view;

        public AbstractViewHolder(View view) {
            this.view = view;
        }

        public View getView() {
            return view;
        }

    }

}
