package org.yekeqi.model;

/**
 * Created by yekeqi on 2015/9/28.
 */
public class SongSearchAudition {
    private int bitRate;
    private int duration;
    private int size;
    private String suffix;
    private String typeDescription;
    private String url;

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
