package org.yekeqi.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yekeqi on 2015/9/25.
 */
@DatabaseTable(tableName = "tb_list_item")
public class MusicListItemBean {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "path")
    private String path;
    @DatabaseField(columnName = "name")
    private String name;
//    @DatabaseField(columnName = "isPlaying")
//    private boolean isPlaying;
    @DatabaseField(columnName = "isVaild")
    private boolean isVaild;
    @DatabaseField(columnName = "length")
    private int length;
//    @DatabaseField(columnName = "no")
//    private int no;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public boolean isPlaying() {
//        return isPlaying;
//    }
//
//    public void setIsPlaying(boolean isPlaying) {
//        this.isPlaying = isPlaying;
//    }

    public boolean isVaild() {
        return isVaild;
    }

    public void setIsVaild(boolean isVaild) {
        this.isVaild = isVaild;
    }

//    public int getNo() {
//        return no;
//    }
//
//    public void setNo(int no) {
//        this.no = no;
//    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
