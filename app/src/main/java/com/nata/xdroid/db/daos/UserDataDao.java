package com.nata.xdroid.db.daos;


import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.nata.xdroid.db.DatabaseHelper;
import com.nata.xdroid.db.beans.UserData;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Calvin on 2016/11/25.
 */


public class UserDataDao implements Serializable{
    private Dao<UserData, Integer> dao;
    private DatabaseHelper helper;

    public UserDataDao(Context context) {
        try
        {
            helper = DatabaseHelper.getHelper(context);
            dao = helper.getDao(UserData.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<UserData> getAll() {
        try {
            return dao.queryBuilder().orderBy(UserData.PACKAGE_FIELD_NAME, false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<UserData>();
    }

    public UserData getUserDataByQuery(String packageName, String activityName,int resourceId) {
        try {
            return dao.queryBuilder()
                    .orderBy(UserData.TIME_FIELD_NAME,false)
                    .where()
                    .eq(UserData.PACKAGE_FIELD_NAME, packageName)
                    .and()
                    .eq(UserData.ACTIVITY_FIELD_NAME, activityName)
                    .and()
                    .eq(UserData.RESOURCE_ID_FIELD_NAME, resourceId)
                    .queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new UserData();
    }

//    public int insertOrUpdate(UserData userData) {
//        try {
//            QueryBuilder<UserData, Integer> qb = dao.queryBuilder();
//            qb.where().eq(UserData.PACKAGE_FIELD_NAME, userData.getPackageName()).and()
//                      .eq(UserData.ACTIVITY_FIELD_NAME, userData.getActivityName()).and()
//                      .eq(UserData.RESOURCEID_FIELD_NAME, userData.getResourceId());
//            if (qb.queryForFirst() != null) {
//                // tell the user to enter unique values
//               return  dao.update(userData);
//            } else {
//                return dao.create(userData);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }

    public int insert(UserData userData) {
        try {
            return dao.create(userData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int delete(UserData userData) {
        return delete(userData.get_id());
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
