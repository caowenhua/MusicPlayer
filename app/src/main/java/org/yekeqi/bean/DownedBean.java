package org.yekeqi.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2015/9/30.
 */
@DatabaseTable(tableName = "tb_downed")
public class DownedBean {
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
    @DatabaseField(columnName = "path")
    private String path;
    @DatabaseField(columnName = "duration")
    private int duration;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
