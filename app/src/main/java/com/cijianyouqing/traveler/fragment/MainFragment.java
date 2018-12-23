package com.cijianyouqing.traveler.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cijianyouqing.traveler.R;
import com.cijianyouqing.traveler.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiangpengfei on 2018/8/29.
 */
public class MainFragment extends Fragment {
    @BindView(R.id.ll_content)
    LinearLayout ll_content;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private Context mContext;
    private LayoutInflater mInflater;
    private MapFragment mapFragment;
    private RecommendFragment recommendFragment;
    private PersonalFragment personalFragment;
    private List<Fragment> fragments;
    private FragmentManager fragmentManager;

    private int currentPosition = -1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        init();

        addListener();

        return view;
    }

    private void init() {
        mapFragment = new MapFragment();
        recommendFragment = new RecommendFragment();
        personalFragment = new PersonalFragment();
        fragments = new ArrayList<>();
        fragments.add(mapFragment);
        fragments.add(recommendFragment);
        fragments.add(personalFragment);

        fragmentManager = getChildFragmentManager();

        initTabLayoutForCustom();

        currentPosition = Utils.showFragment(tabLayout.getSelectedTabPosition(), currentPosition, fragmentManager,fragments,R.id.ll_content);

    }

    private void addListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentPosition = Utils.showFragment(tab.getPosition(), currentPosition, fragmentManager,fragments,R.id.ll_content);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 加载自定义布局的TabLayout
     */
    private void initTabLayoutForCustom() {
        int[] drawables = new int[]{R.drawable.tablayout_map, R.drawable.tablayout_recommend, R.drawable.tablayout_personal};
        final String[] texts = new String[]{"地图", "推荐", "我的"};

        for (int i = 0; i < 3; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = mInflater.inflate(R.layout.item_tablayout, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.tablayout_imageview);
            TextView textView = (TextView) view.findViewById(R.id.tablayout_textview);
            imageView.setBackgroundResource(drawables[i]);
            textView.setText(texts[i]);
            textView.setTextColor(getResources().getColorStateList(R.color.tablayout_color_selector));
            tab.setCustomView(view);
            if (i == 0) {
                tabLayout.addTab(tab, true);
            } else {
                tabLayout.addTab(tab);
            }
        }
        tabLayout.setSelectedTabIndicatorHeight(0);
        // 设置整个tabLayout的背景图片
        Drawable drawable = getResources().getDrawable(R.drawable.bg);
        drawable.setAlpha(100);
        tabLayout.setBackground(drawable);
    }

    public DrawerLayout getMapDrawerLayout(){
        if(mapFragment!=null) {
            return mapFragment.getMapDrawerLayout();
        }
        return null;
    }

}
