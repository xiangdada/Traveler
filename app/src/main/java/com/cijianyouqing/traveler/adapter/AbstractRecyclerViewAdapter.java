package com.cijianyouqing.traveler.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangpengfei on 2018/12/6.
 */
public abstract class AbstractRecyclerViewAdapter<T> extends RecyclerView.Adapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;
    protected AbstractRecyclerViewAdapter mAdapter;

    public AbstractRecyclerViewAdapter(Context context, List<T> datas) {
        mContext = context;
        if (datas != null) {
            mDatas = datas;
        } else {
            mDatas = new ArrayList<>();
        }
        mAdapter = this;
    }

    /**
     * 动态刷新
     *
     * @param datas
     */
    public void notifyDataSetChanged(List<T> datas) {
        if (datas != null) {
            this.mDatas.clear();
            for (T data : datas) {
                mDatas.add(data);
            }
            notifyDataSetChanged();
        }
    }

    public Object getItem(int position) {
        return mDatas.get(position);
    }

    public List<T> getDatas() {
        return mDatas;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        AbstractViewHolder abstractViewHolder = (AbstractViewHolder) viewHolder;
        abstractViewHolder.mRootView.setTag(position);
        onBindViewHolder(abstractViewHolder, position);
    }

    public abstract void onBindViewHolder(@NonNull AbstractViewHolder viewHolder, int position);

    abstract class AbstractViewHolder extends RecyclerView.ViewHolder {
        public View mRootView;

        public AbstractViewHolder(@NonNull View itemView) {
            super(itemView);
            mRootView = itemView.getRootView();
            mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mAdapter, v, ((Integer) v.getTag()).intValue());
                    }
                }
            });
            mRootView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mOnItemLongClickListener != null) {
                        mOnItemLongClickListener.onItemLongClick(mAdapter, v, ((Integer) v.getTag()).intValue());
                    }
                    return true;
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(AbstractRecyclerViewAdapter adapter, View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(AbstractRecyclerViewAdapter adapter, View view, int position);
    }


}
