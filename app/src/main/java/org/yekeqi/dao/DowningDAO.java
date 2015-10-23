package org.yekeqi.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.yekeqi.bean.DowningBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/30.
 */
public class DowningDAO {
    private Dao<DowningBean, Integer> dao;
    private DatabaseHelper helper;
    private static DowningDAO downingDAO;

    public static DowningDAO getInstance(Context context){
        if(downingDAO == null){
            downingDAO = new DowningDAO(context);
        }
        return downingDAO;
    }

    private DowningDAO(Context context) {
        try
        {
            helper = DatabaseHelper.getHelper(context);
            dao = helper.getDao(DowningBean.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DowningBean> getList(){
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public DowningBean getBean(){
        try {
            QueryBuilder builder = dao.queryBuilder();
            return (DowningBean) builder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void add(DowningBean bean){
        try {
            dao.create(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String url){
        try {
            QueryBuilder builder = dao.queryBuilder();
            builder.where().eq("url", url);
            DowningBean data = (DowningBean) builder.queryForFirst();
            if(data != null){
                dao.delete(data);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
