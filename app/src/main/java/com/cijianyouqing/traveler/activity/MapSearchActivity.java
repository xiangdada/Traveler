package com.cijianyouqing.traveler.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.cijianyouqing.traveler.R;
import com.cijianyouqing.traveler.adapter.MapSearchAdapter;
import com.cijianyouqing.traveler.bean.MapSearchData;
import com.cijianyouqing.traveler.view.IToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiangpengfei on 2018/9/10.
 */
public class MapSearchActivity extends BaseActivity implements View.OnClickListener, OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
    @BindView(R.id.iv_topBackPage)
    ImageView iv_topBackPage;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_search)
    TextView tv_search;
    @BindView(R.id.lv)
    ListView lv;

    private MapSearchAdapter adapter;
    private List<MapSearchData> mDatas = new ArrayList<>();

    private PoiSearch mPoiSearch;
    private SuggestionSearch mSuggestionSearch;

    private String city = "深圳";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        ButterKnife.bind(this);
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        adapter = new MapSearchAdapter(this,mDatas);

        String cls = "cn.mastercom.pajjjj.ClassName";


        addListener();
    }

    private void addListener() {
        iv_topBackPage.setOnClickListener(this);
        tv_search.setOnClickListener(this);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {

                } else {
                    mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                            .keyword(s.toString()).city(city));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_topBackPage:
                finish();
                break;
            case R.id.tv_search:
                if (checkSearch()) {
                    search(et_search.getText().toString().trim());
                }
                break;
        }
    }

    private boolean checkSearch() {
        if ("".equals(et_search.getText().toString().trim())) {
            IToast.show(this, "搜索内容不能为空");
            return false;
        }
        return true;
    }

    private int loadIndex = 0;

    private void search(String content) {
        mPoiSearch.searchInCity(new PoiCitySearchOption()
                .city(city).keyword(content).pageNum(loadIndex));
    }

    /**
     * 获取POI搜索结果，包括searchInCity，searchNearby，searchInBound返回的搜索结果
     *
     * @param poiResult
     */
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            IToast.show(MapSearchActivity.this, "未搜索到内容");
//            Log.e("测试", "未搜索到内容");
            return;
        }
        List<PoiInfo> allPoi = poiResult.getAllPoi();
        List<PoiAddrInfo> allAddr = poiResult.getAllAddr();
        List<CityInfo> cityList = poiResult.getSuggestCityList();
        if (allPoi != null && allPoi.size() > 0) {
            PoiInfo poiInfo = allPoi.get(0);
//            Log.e("测试", "poiInfo = " + poiInfo.toString());
        }
        if (allAddr != null && allAddr.size() > 0) {
            PoiAddrInfo poiAddrInfo = allAddr.get(0);
//            Log.e("测试", "poiAddrInfo = " + poiAddrInfo.toString());
        }
        if (cityList != null && cityList.size() > 0) {
            CityInfo cityInfo = cityList.get(0);
//            Log.e("测试", "cityInfo = " + cityInfo.toString());
        }

        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
//            Log.e("测试", "NO_ERROR = " + poiResult.toString());
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
//            Log.e("测试", "AMBIGUOUS_KEYWORD = " + poiResult.toString());
            return;
        }

    }

    private PoiResult selectPoiResult;

    /**
     * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
     *
     * @param poiDetailResult
     */
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (poiDetailResult.error == SearchResult.ERRORNO.NO_ERROR) {
            bundle.putBoolean("isDetail", false);
            bundle.putParcelable("poi", selectPoiResult);
        } else {
            bundle.putBoolean("isDetail", true);
            bundle.putParcelable("poi", poiDetailResult);
        }
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    /**
     * 获取在线建议搜索结果，得到requestSuggestion返回的搜索结果
     *
     * @param suggestionResult
     */
    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }
//        Log.e("测试", "onGetSuggestionResult = " + suggestionResult.toString());
    }


    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        super.onDestroy();
    }
}
