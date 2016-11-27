package com.nata.xdroid.db;


import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Calvin on 2016/11/25.
 */


public class RecordDao {
    private Dao<CrashInfo, Integer> dao;

    public RecordDao(Context context) {
        RecordDBOpenHelper openHelper = new RecordDBOpenHelper(context, DBInfo.DB_NAME, null, DBInfo.VERSION);
        dao = openHelper.getRecordDao();
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
