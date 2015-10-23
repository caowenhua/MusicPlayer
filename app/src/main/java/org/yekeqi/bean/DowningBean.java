package org.yekeqi.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/30.
 */
@DatabaseTable(tableName = "tb_downing")
public class DowningBean implements Serializable{

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "singer")
    private String singer;
    @DatabaseField(columnName = "url")
    private String url;
    @DatabaseField(columnName = "length")
    private int length;
    @DatabaseField(columnName = "suffix")
    private String suffix;
    @DatabaseField(columnName = "duration")
    private int duration;
//    @DatabaseField(columnName = "range")
//    private int range;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
//    public int getRange() {
//        return range;
//    }
//
//    public void setRange(int range) {
//        this.range = range;
//    }
}
