package org.yekeqi.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.yekeqi.bean.DownedBean;
import org.yekeqi.bean.DowningBean;
import org.yekeqi.bean.MusicListItemBean;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


	private static final String TABLE_NAME = "player.db";
	private static DatabaseHelper instance;
	private Map<String, Dao> daos = new HashMap<String, Dao>();
	
	private DatabaseHelper(Context context) {
		super(context, TABLE_NAME, null, 1);
	}


	/**
	 * 单例获取该Helper
	 * 
	 * @param context
	 * @return
	 */
	public static synchronized DatabaseHelper getHelper(Context context)
	{
		context = context.getApplicationContext();
		if (instance == null)
		{
			synchronized (DatabaseHelper.class)
			{
				if (instance == null)
					instance = new DatabaseHelper(context);
			}
		}

		return instance;
	}
	
	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource)
	{
		try
		{
			TableUtils.createTable(connectionSource, MusicListItemBean.class);
			TableUtils.createTable(connectionSource, DowningBean.class);
			TableUtils.createTable(connectionSource, DownedBean.class);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		try
		{
			TableUtils.dropTable(connectionSource, MusicListItemBean.class, true);
			TableUtils.dropTable(connectionSource, DowningBean.class, true);
			TableUtils.dropTable(connectionSource, DownedBean.class, true);
			onCreate(database, connectionSource);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized Dao getDao(Class clazz) throws SQLException
	{
		Dao dao = null;
		String className = clazz.getSimpleName();

		if (daos.containsKey(className))
		{
			dao = daos.get(className);
		}
		if (dao == null)
		{
			dao = super.getDao(clazz);
			daos.put(className, dao);
		}
		return dao;
	}

	/**
	 * 释放资源
	 */
	@Override
	public void close()
	{
		super.close();

		for (String key : daos.keySet())
		{
			Dao dao = daos.get(key);
			dao = null;
		}
	}
}
