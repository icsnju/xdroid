package com.nata.xdroid.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Calvin on 2016/11/25.
 */

/**
 * 崩溃信息
 */
@DatabaseTable(tableName = "testrecord")
public class TestRecord implements Serializable{

    public static final String PACKAGE_FIELD_NAME = "packageName";
    public static final String ALL_ACTIVITY_LIST = "allActivityList";
    public static final String COVERED_ACTIVITY_LIST = "coveredActivityList";

    public static final String ALL_TIME = "allTime";
    public static final String TEST_TIME = "testTime";
    public static final String MANUAL_TIME = "manualTime";

    //--------表结构
    @DatabaseField(generatedId = true)
    private int _id;

    @DatabaseField(canBeNull = false, columnName = PACKAGE_FIELD_NAME)
    private String packageName;

    @ForeignCollectionField ()
    private Collection<String> allActivities = new ArrayList<String>();

    @ForeignCollectionField
    private Collection<String> coveredActivities = new ArrayList<String>();

    @DatabaseField(defaultValue = "0", columnName = ALL_TIME)
    private int allTime;

    @DatabaseField(defaultValue = "0", columnName = TEST_TIME)
    private int testTime;

    @DatabaseField(defaultValue = "0", columnName = MANUAL_TIME)
    private int manualTime;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Collection<String> getAllActivities() {
        return allActivities;
    }

    public void setAllActivities(Collection<String> allActivities) {
        this.allActivities = allActivities;
    }

    public Collection<String> getCoveredActivities() {
        return coveredActivities;
    }

    public void setCoveredActivities(Collection<String> coveredActivities) {
        this.coveredActivities = coveredActivities;
    }

    public int getAllTime() {
        return allTime;
    }

    public void setAllTime(int allTime) {
        this.allTime = allTime;
    }

    public int getTestTime() {
        return testTime;
    }

    public void setTestTime(int testTime) {
        this.testTime = testTime;
    }

    public int getManualTime() {
        return manualTime;
    }

    public void setManualTime(int manualTime) {
        this.manualTime = manualTime;
    }

    @Override
    public String toString() {
        return "TestRecord{" +
                "_id=" + _id +
                ", packageName='" + packageName + '\'' +
                ", allActivities=" + allActivities +
                ", coveredActivities=" + coveredActivities +
                ", allTime=" + allTime +
                ", testTime=" + testTime +
                ", manualTime=" + manualTime +
                '}';
    }
}
