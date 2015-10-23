package org.yekeqi.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import org.yekeqi.bean.DownedBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/30.
 */
public class DownedDAO {
    private Dao<DownedBean, Integer> dao;
    private DatabaseHelper helper;
    private static DownedDAO DownedDAO;

    public static DownedDAO getInstance(Context context){
        if(DownedDAO == null){
            DownedDAO = new DownedDAO(context);
        }
        return DownedDAO;
    }

    private DownedDAO(Context context) {
        try
        {
            helper = DatabaseHelper.getHelper(context);
            dao = helper.getDao(DownedBean.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DownedBean> getList(){
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void add(DownedBean bean){
        try {
            dao.create(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
