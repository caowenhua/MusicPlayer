package org.yekeqi.model;

import java.util.List;

/**
 * Created by yekeqi on 2015/9/28.
 */
public class SongSearchResponseData {
    private int albumId;
    private int songId;
    private String albumName;
    private String name;
    private String singerName;
    private int singerId;
    private int status;
    private List<SongSearchAudition> auditionList;

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public int getSingerId() {
        return singerId;
    }

    public void setSingerId(int singerId) {
        this.singerId = singerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SongSearchAudition> getAuditionList() {
        return auditionList;
    }

    public void setAuditionList(List<SongSearchAudition> auditionList) {
        this.auditionList = auditionList;
    }
}
