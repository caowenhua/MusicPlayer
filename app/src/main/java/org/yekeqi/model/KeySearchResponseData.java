package org.yekeqi.model;

import java.util.List;

/**
 * Created by yekeqi on 2015/9/28.
 */
public class KeySearchResponseData {

    private int order;
    private List<KeySearchResponseSong> song;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<KeySearchResponseSong> getSong() {
        return song;
    }

    public void setSong(List<KeySearchResponseSong> song) {
        this.song = song;
    }
}
