package com.nata.xdroid.db.daos;


import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.nata.xdroid.db.DatabaseHelper;
import com.nata.xdroid.db.beans.CrashInfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Calvin on 2016/11/25.
 */


public class RecordDao {
    private Dao<CrashInfo, Integer> dao;
    private DatabaseHelper helper;

    public RecordDao(Context context) {
        try
        {
            helper = DatabaseHelper.getHelper(context);
            dao = helper.getDao(CrashInfo.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<CrashInfo> getAll() {
        try {
            return dao.queryBuilder().orderBy("stampTime", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<CrashInfo>();
    }

    public int insert(CrashInfo crashInfo) {
        try {
            return dao.create(crashInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int deleteAll() {
        try {
            return TableUtils.clearTable(dao.getConnectionSource(), CrashInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(CrashInfo crashInfo) {
        return delete(crashInfo.getId());
    }

    public int delete(int id) {
        try {
            return dao.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
