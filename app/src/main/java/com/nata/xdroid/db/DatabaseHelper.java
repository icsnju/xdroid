package com.nata.xdroid.db;

/**
 * Created by Calvin on 2016/11/25.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.nata.xdroid.db.beans.CrashInfo;
import com.nata.xdroid.db.beans.UserData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private Map<String, Dao> daos = new HashMap<String, Dao>();


    public DatabaseHelper(Context context) {
//        super(context, Environment.getExternalStorageDirectory().getAbsolutePath()
//                + File.separator + DBInfo.DB_NAME, null, DBInfo.VERSION);
                super(context, DBInfo.DB_NAME, null, DBInfo.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, CrashInfo.class);
            TableUtils.createTable(connectionSource, UserData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, CrashInfo.class, true);
            TableUtils.dropTable(connectionSource, UserData.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(database, connectionSource);
    }

    private static DatabaseHelper instance;


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



//    public Dao<CrashInfo, Integer> getRecordDao() {
//        ConnectionSource connectionSource = new AndroidConnectionSource(this);
//        Dao<CrashInfo, Integer> dao = null;
//        try {
//            dao = getDao(CrashInfo.class);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
////        try {
////            dao = BaseDaoImpl.createDao(connectionSource, UserData.class);
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
//        return dao;
//    }
}
