package com.cijianyouqing.traveler.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cijianyouqing.traveler.R;
import com.cijianyouqing.traveler.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangpengfei on 2018/12/6.
 */
public class SharePopupWindow extends PopupWindow {
    private Context mContext;
    private LayoutInflater mInflater;
    private View mRootView;
    private LinearLayout ll_content;
    private List<ShareData> datas = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private static int[] images = new int[]{
            R.drawable.ic_qq_share, R.drawable.ic_zone_share,
            R.drawable.ic_weixin_share, R.drawable.ic_circle_share,
            R.drawable.ic_weibo_share};
    private static String[] titles = new String[]{
            "QQ", "QQ空间", "微信好友", "朋友圈", "新浪微博"};

    private int height = 0;
    private int count = 0;
    private int column = 3;
    private int rowPaddingTop = 10;
    private int contentPaddingBottom = 10;

    public SharePopupWindow(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mRootView = mInflater.inflate(R.layout.share_popupwindow, null);
        ll_content = mRootView.findViewById(R.id.ll_content);
        ll_content.setPadding(0, 0, 0, (int) (contentPaddingBottom * Utils.getDensity(mContext)));
        setContentView(mRootView);
        setOutsideTouchable(true);
        setFocusable(true);
        count = images.length;
        measuredHeight();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(height);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        initData();
    }


    private void initData() {
        if (images.length == titles.length) {
            for (int i = 0; i < images.length; i++) {
//                ShareData data = new ShareData(images[i], titles[i]);
//                datas.add(data);
                addItem(i, images[i], titles[i]);
            }
        }
    }

    LinearLayout currentRow;
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    private void addItem(int position, int imageRes, String title) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        if (position % column == 0) {
            currentRow = new LinearLayout(mContext);
            currentRow.setLayoutParams(layoutParams);
            currentRow.setWeightSum(3);
            currentRow.setPadding(0, (int) (rowPaddingTop * Utils.getDensity(mContext)), 0, 0);
            currentRow.setOrientation(LinearLayout.HORIZONTAL);
            currentRow.addView(getItemView(position, imageRes, title), lp);
            ll_content.addView(currentRow);
        } else {
            if (currentRow != null) {
                currentRow.addView(getItemView(position, imageRes, title), lp);
            }
        }
    }

    private View getItemView(final int position, int imageRes, final String title) {
        View view = mInflater.inflate(R.layout.share_itemview, null);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.textView);
        imageView.setImageResource(imageRes);
        textView.setText(title);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(title, position);
                }
            }
        });
        return view;
    }

    public int measuredHeight() {
        int row = count % column == 0 ? count / column : count / column + 1;
        height = (int) ((row * (65 + rowPaddingTop) + contentPaddingBottom) * Utils.getDensity(mContext));
        return height;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        Rect rect = new Rect();
        anchor.getGlobalVisibleRect(rect);
        int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
        setHeight(h);
        super.showAsDropDown(anchor, xoff, yoff);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String title, int postion);
    }


    public class ShareData implements Serializable {
        private int imageRes;
        private String title;

        public ShareData() {
        }

        public ShareData(int imageRes, String title) {
            this.imageRes = imageRes;
            this.title = title;
        }

        public int getImageRes() {
            return imageRes;
        }

        public void setImageRes(int imageRes) {
            this.imageRes = imageRes;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
