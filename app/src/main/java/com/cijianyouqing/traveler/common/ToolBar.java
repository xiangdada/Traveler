package com.cijianyouqing.traveler.common;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cijianyouqing.traveler.R;


/**
 * Created by xiangpengfei on 2018/3/6.
 * <p>
 * 工具栏
 */

public class ToolBar {
    private Activity activity;
    private View toolbarview;
    private RelativeLayout rl_title;
    private ImageView iv_left1, iv_left2, iv_left3, iv_right1, iv_right2, iv_right3;
    private TextView tv_title, tv_left1, tv_left2, tv_left3, tv_right1, tv_right2, tv_right3;

    private int backgroudResourceId;
    private int drawableIdL1, drawableIdL2, drawableIdL3, drawableIdR1, drawableIdR2, drawableIdR3;
    private String title, textL1, textL2, textL3, textR1, textR2, textR3;

    private OnToolBarClickListener onToolBarClickListener;

    public interface OnToolBarClickListener {
        void onToolBarClick(View clickView, ToolBar toolBar);
    }

    public ToolBar(Builder builder) {
        this.activity = builder.activity;
        this.backgroudResourceId = builder.backgroudResourceId;
        this.drawableIdL1 = builder.drawableIdL1;
        this.drawableIdL2 = builder.drawableIdL2;
        this.drawableIdL3 = builder.drawableIdL3;
        this.drawableIdR1 = builder.drawableIdR1;
        this.drawableIdR2 = builder.drawableIdR2;
        this.drawableIdR3 = builder.drawableIdR3;
        this.title = builder.title;
        this.textL1 = builder.textL1;
        this.textL2 = builder.textL2;
        this.textL3 = builder.textL3;
        this.textR1 = builder.textR1;
        this.textR2 = builder.textR2;
        this.textR3 = builder.textR3;
        this.onToolBarClickListener = builder.onToolBarClickListener;
        initView();
    }

    private void initView() {
        toolbarview = activity.findViewById(R.id.toolbarview);
        if (toolbarview == null) {
            throw new NullPointerException("工具栏视图空指针异常，请检查页面布局中是否引用了 layout_toolbar.xml");
        } else {
            rl_title = (RelativeLayout) activity.findViewById(R.id.rl_title);
            iv_left1 = (ImageView) activity.findViewById(R.id.iv_left1);
            iv_left2 = (ImageView) activity.findViewById(R.id.iv_left2);
            iv_left3 = (ImageView) activity.findViewById(R.id.iv_left3);
            iv_right1 = (ImageView) activity.findViewById(R.id.iv_right1);
            iv_right2 = (ImageView) activity.findViewById(R.id.iv_right2);
            iv_right3 = (ImageView) activity.findViewById(R.id.iv_right3);
            tv_title = (TextView) toolbarview.findViewById(R.id.tv_title);
            tv_left1 = (TextView) activity.findViewById(R.id.tv_left1);
            tv_left2 = (TextView) activity.findViewById(R.id.tv_left2);
            tv_left3 = (TextView) activity.findViewById(R.id.tv_left3);
            tv_right1 = (TextView) activity.findViewById(R.id.tv_right1);
            tv_right2 = (TextView) activity.findViewById(R.id.tv_right2);
            tv_right3 = (TextView) activity.findViewById(R.id.tv_right3);

            if (tv_title != null) {
                tv_title.setText(title);
            }
            if (backgroudResourceId != 0) {
                rl_title.setBackgroundResource(backgroudResourceId);
            }
            setImageView(iv_left1, drawableIdL1);
            setImageView(iv_left2, drawableIdL2);
            setImageView(iv_left3, drawableIdL3);
            setImageView(iv_right1, drawableIdR1);
            setImageView(iv_right2, drawableIdR2);
            setImageView(iv_right3, drawableIdR3);
            setTextView(tv_left1, textL1);
            setTextView(tv_left2, textL2);
            setTextView(tv_left3, textL3);
            setTextView(tv_right1, textR1);
            setTextView(tv_right2, textR2);
            setTextView(tv_right3, textR3);
        }
    }

    private void setImageView(final ImageView imageView, int drawableId) {
        if (imageView != null) {
            if (drawableId != 0) {
                imageView.setImageResource(drawableId);
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onToolBarClickListener != null) {
                        onToolBarClickListener.onToolBarClick(imageView, ToolBar.this);
                    }
                }
            });

        }
    }

    private void setTextView(final TextView textView, String text) {
        if (textView != null) {
            if (text != null && !"".equals(text)) {
                textView.setText(text);
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onToolBarClickListener != null) {
                        onToolBarClickListener.onToolBarClick(textView, ToolBar.this);
                    }
                }
            });
        }
    }

    public View getToolBarView() {
        return toolbarview;
    }

    public ImageView getImageViewL1() {
        return iv_left1;
    }

    public ImageView getImageViewL2() {
        return iv_left2;
    }

    public ImageView getImageViewL3() {
        return iv_left3;
    }

    public ImageView getImageViewR1() {
        return iv_right1;
    }

    public ImageView getImageViewR2() {
        return iv_right2;
    }

    public ImageView getImageViewR3() {
        return iv_right3;
    }

    public TextView getTextViewL1() {
        return tv_left1;
    }

    public TextView getTextViewL2() {
        return tv_left2;
    }

    public TextView getTextViewL3() {
        return tv_left3;
    }

    public TextView getTextViewR1() {
        return tv_right1;
    }

    public TextView getTextViewR2() {
        return tv_right2;
    }

    public TextView getTextViewR3() {
        return tv_right3;
    }

    public TextView getTextViewTitle() {
        return tv_title;
    }


    public static class Builder {
        private Activity activity;
        private int backgroudResourceId;
        private int drawableIdL1, drawableIdL2, drawableIdL3, drawableIdR1, drawableIdR2, drawableIdR3;
        private String title = "", textL1 = "", textL2 = "", textL3 = "", textR1 = "", textR2 = "", textR3 = "";
        private OnToolBarClickListener onToolBarClickListener;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setBackgroudResourceId(int backgroudResourceId) {
            this.backgroudResourceId = backgroudResourceId;
            return this;
        }

        public Builder setImageViewL1(int drawableId) {
            this.drawableIdL1 = drawableId;
            return this;
        }

        public Builder setImageViewL2(int drawableId) {
            this.drawableIdL2 = drawableId;
            return this;
        }

        public Builder setImageViewL3(int drawableId) {
            this.drawableIdL3 = drawableId;
            return this;
        }

        public Builder setImageViewR1(int drawableId) {
            this.drawableIdR1 = drawableId;
            return this;
        }

        public Builder setImageViewR2(int drawableId) {
            this.drawableIdR2 = drawableId;
            return this;
        }

        public Builder setImageViewR3(int drawableId) {
            this.drawableIdR3 = drawableId;
            return this;
        }

        public Builder setTextViewL1(String text) {
            this.textL1 = text;
            return this;
        }

        public Builder setTextViewL2(String text) {
            this.textL2 = text;
            return this;
        }

        public Builder setTextViewL3(String text) {
            this.textL3 = text;
            return this;
        }

        public Builder setTextViewR1(String text) {
            this.textR1 = text;
            return this;
        }

        public Builder setTextViewR2(String text) {
            this.textR2 = text;
            return this;
        }

        public Builder setTextViewR3(String text) {
            this.textR3 = text;
            return this;
        }

        public Builder setOnToolBarClickListener(OnToolBarClickListener onToolBarClickListener) {
            this.onToolBarClickListener = onToolBarClickListener;
            return this;
        }

        public Builder getDefaultBuild() {
            this.title = "";
            this.backgroudResourceId = R.drawable.bg_title;
            this.drawableIdL1 = R.drawable.bg_btn_back;
            this.drawableIdL2 = 0;
            this.drawableIdL3 = 0;
            this.drawableIdR1 = 0;
            this.drawableIdR2 = 0;
            this.drawableIdR3 = 0;
            this.textL1 = "";
            this.textL2 = "";
            this.textL3 = "";
            this.textR1 = "";
            this.textR2 = "";
            this.textR3 = "";
            this.onToolBarClickListener = new OnToolBarClickListener() {
                @Override
                public void onToolBarClick(View clickView, ToolBar toolBar) {
                    if (clickView != null && toolBar != null) {
                        if (clickView.getId() == toolBar.getImageViewL1().getId()) {
                            activity.finish();
                        }
                    }
                }
            };
            return this;
        }

        public ToolBar build() {
            return new ToolBar(this);
        }

        public ToolBar buildDefault(String title) {
            getDefaultBuild();
            this.title = title;
            return new ToolBar(this);
        }

    }
}
