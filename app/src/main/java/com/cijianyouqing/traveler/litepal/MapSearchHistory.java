package com.cijianyouqing.traveler.litepal;

import org.litepal.crud.LitePalSupport;

/**
 * Created by xiangpengfei on 2018/9/11.
 */
public class MapSearchHistory extends LitePalSupport {
    private String searchContent;
    private String searchResult;

    public MapSearchHistory() {

    }

    public MapSearchHistory(String searchContent, String searchResult) {
        this.searchContent = searchContent;
        this.searchResult = searchResult;
    }

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public String getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(String searchResult) {
        this.searchResult = searchResult;
    }
}
