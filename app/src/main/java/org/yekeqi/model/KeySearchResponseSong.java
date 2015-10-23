package org.yekeqi.model;

/**
 * Created by yekeqi on 2015/9/28.
 */
public class KeySearchResponseSong {
    private int _id;
    private String name;
    private String singer_name;
    private double weight;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger_name() {
        return singer_name;
    }

    public void setSinger_name(String singer_name) {
        this.singer_name = singer_name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
