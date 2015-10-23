package org.yekeqi.model;

/**
 * Created by yekeqi on 2015/9/28.
 */
public class KeySearchResponse {
    private int code;
    private KeySearchResponseData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public KeySearchResponseData getData() {
        return data;
    }

    public void setData(KeySearchResponseData data) {
        this.data = data;
    }
}
