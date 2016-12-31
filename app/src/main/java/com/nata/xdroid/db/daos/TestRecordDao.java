package com.nata.xdroid.db.daos;


import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.nata.xdroid.db.DatabaseHelper;
import com.nata.xdroid.db.beans.TestRecord;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Calvin on 2016/11/25.
 */


public class TestRecordDao {
    private Dao<TestRecord, Integer> dao;
    private DatabaseHelper helper;

    public TestRecordDao(Context context) {
        try
        {
            helper = DatabaseHelper.getHelper(context);
            dao = helper.getDao(TestRecord.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<TestRecord> getAll() {
        try {
            return dao.queryBuilder().orderBy(TestRecord.PACKAGE_FIELD_NAME, false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<TestRecord>();
    }


//    public int setAllActivityList() {
//
//    }

    public int insert(TestRecord testRecord) {
        try {
            return dao.create(testRecord);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int deleteAll() {
        try {
            return TableUtils.clearTable(dao.getConnectionSource(), TestRecord.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int delete(TestRecord testRecord) {
        return delete(testRecord.get_id());
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
