package com.nata.xdroid.db.beans;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Calvin on 2016/11/25.
 */

/**
 * 崩溃信息
 */
@DatabaseTable(tableName = "userdata")
public class UserData {

    public static final String PACKAGE_FIELD_NAME = "packageName";
    public static final String ACTIVITY_FIELD_NAME = "activityName";
    public static final String RESOURCEID_FIELD_NAME = "resourceId";
    public static final String CONTENT_FIELD_NAME = "content";
    public static final String TIME_FIELD_NAME = "stampTime";

    //--------表结构
    @DatabaseField(generatedId = true)
    private int _id;

    @DatabaseField(canBeNull = false, columnName = PACKAGE_FIELD_NAME)
    private String packageName;

    @DatabaseField(canBeNull = false, columnName = ACTIVITY_FIELD_NAME)
    private String activityName;

    @DatabaseField(canBeNull = false, columnName = RESOURCEID_FIELD_NAME)
    private int resourceId;

    @DatabaseField(canBeNull = false, columnName = CONTENT_FIELD_NAME)
    private String content;

    @DatabaseField(canBeNull = false, columnName = TIME_FIELD_NAME)
    private int stampTime;

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

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStampTime() {
        return stampTime;
    }

    public void setStampTime(int stampTime) {
        this.stampTime = stampTime;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "_id=" + _id +
                ", packageName='" + packageName + '\'' +
                ", activityName='" + activityName + '\'' +
                ", resourceId=" + resourceId +
                ", content='" + content + '\'' +
                '}';
    }
}
