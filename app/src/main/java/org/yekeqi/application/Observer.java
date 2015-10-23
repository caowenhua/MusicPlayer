package org.yekeqi.application;

import org.yekeqi.bean.MusicListItemBean;

import java.util.List;

/**
 * Created by yekeqi on 2015/9/26.
 */
public interface Observer {
    public void update(List<MusicListItemBean> list);
}
