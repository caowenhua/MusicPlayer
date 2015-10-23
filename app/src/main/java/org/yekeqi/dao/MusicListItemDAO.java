package org.yekeqi.dao;

import android.content.Context;

import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import org.yekeqi.bean.MusicListItemBean;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yekeqi on 2015/9/25.
 */
public class MusicListItemDAO  {

    private Dao<MusicListItemBean, Integer> musicDao;
    private DatabaseHelper helper;
    private static MusicListItemDAO dao;

    public static MusicListItemDAO getInstance(Context context){
        if(dao == null){
            dao = new MusicListItemDAO(context);
        }
        return dao;
    }

    private MusicListItemDAO(Context context) {
        try
        {
            helper = DatabaseHelper.getHelper(context);
            musicDao = helper.getDao(MusicListItemBean.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MusicListItemBean> getMusicList(){
        try {
            QueryBuilder builder = musicDao.queryBuilder();
            builder.orderBy("name", true);
            return builder.query();
//            return musicDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

//    public int getWhichIsPlaying(){
//        QueryBuilder builder = musicDao.queryBuilder();
//        try {
//            List<MusicListItemBean> ids = builder.where().eq("isPlaying", true).query();
//            if(ids == null || ids.size() == 0){
//                return -1;
//            }
//            return ids.get(0).getId();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }

    public long getLength(){
        try {
            return musicDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteAll(){
        try {
            DeleteBuilder builder = musicDao.deleteBuilder();
            builder.where().gt("id", 0);
            builder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMusic(MusicListItemBean bean){
        try {
            musicDao.create(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMusicList(List<MusicListItemBean> list){
        Savepoint savepoint = null;
        AndroidDatabaseConnection connection = new AndroidDatabaseConnection(helper.getWritableDatabase(), true);
        connection.setAutoCommit(false);
        try {
            musicDao.startThreadConnection();
            savepoint = connection.setSavePoint("add");
            for (MusicListItemBean bean : list){
                musicDao.create(bean);
            }
            connection.commit(savepoint);
            musicDao.commit(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void setWhichIsPlaying(int preNo, int curNo){
//        UpdateBuilder builder = musicDao.updateBuilder();
//        if(preNo != -1){
//            try {
//                builder.where().eq("id", preNo);
//                builder.updateColumnValue("isPlaying", false);
//                builder.update();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            builder.where().eq("id", curNo);
//            builder.updateColumnValue("isPlaying", true);
//            builder.update();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void setWhichIsUnvaild(int id){
        UpdateBuilder builder = musicDao.updateBuilder();
        try {
            builder.where().eq("id", id);
            builder.updateColumnValue("isVaild", false);
            builder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isAllUnVaild(){
        try {
            QueryBuilder builder = musicDao.queryBuilder();
            builder.where().eq("isVaild", true);
            MusicListItemBean bean = (MusicListItemBean) builder.queryForFirst();
            if(bean == null){
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
