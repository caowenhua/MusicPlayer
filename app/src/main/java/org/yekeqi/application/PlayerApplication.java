package org.yekeqi.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import org.yekeqi.bean.MusicListItemBean;
import org.yekeqi.dao.MusicListItemDAO;
import org.yekeqi.service.MusicService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yekeqi on 2015/9/25.
 */
public class PlayerApplication extends Application{

//    private static PlayerApplication instance;
    // 运用list来保存们每一个activity是关键
    private List<Activity> mList = new LinkedList<Activity>();
    private List<MusicListItemBean> musicList;
    private MusicListItemDAO dao;

//    // 实例化一次
//    public synchronized static PlayerApplication getInstance() {
//        if (null == instance) {
//            instance = new PlayerApplication();
//        }
//        return instance;
//    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (mList.get(mList.size() - 1).equals(activity)) {
            mList.remove(mList.size() - 1);
        } else {
            for (int i = mList.size() - 1; i >= 0; i--) {
                if (mList.get(i).equals(activity)) {
                    mList.remove(i);
                    break;
                }
            }
        }
    }

    // 关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public void cleanBottom() {
        if (mList != null) {
            if (mList.size() > 1) {
                for (int i = mList.size() - 2; i >= 0; i--) {
                    mList.get(i).finish();
                    mList.remove(i);
                }
            }
        }
    }

    // 杀进程
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        musicList = new ArrayList<>();
        notifyMusicListChanged();
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
    }

    public List<MusicListItemBean> getMusicList() {
//        if(musicList == null){
//            musicList = dao.getMusicList();
//        }
        return musicList;
    }

    public MusicListItemDAO getMusicDao() {
//        if(dao == null){
//            dao = MusicListItemDAO.getInstance(this);
//        }
        return dao;
    }

    public void notifyMusicListChanged(){
        musicList.clear();
        dao = MusicListItemDAO.getInstance(this);
        musicList.addAll(dao.getMusicList());
        Intent intent = new Intent("musicListChanged");
        sendBroadcast(intent);
    }
}
