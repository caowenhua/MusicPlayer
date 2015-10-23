package org.yekeqi.model;

import java.util.List;

/**
 * Created by yekeqi on 2015/9/28.
 */
public class SongSearchResponse {
    private int totalCount;
    private int pageCount;
    private List<SongSearchResponseData> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<SongSearchResponseData> getData() {
        return data;
    }

    public void setData(List<SongSearchResponseData> data) {
        this.data = data;
    }
}
