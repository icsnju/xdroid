package com.nata.xdroid.db;

/**
 * Created by Calvin on 2016/11/25.
 */

/**
 */
public class DBInfo {

    public static final String DB_NAME = "xdroid";
    public static final String TABLE_NAME = "record";
    public static final int VERSION = 1;

    public static final String CREATE_TABLE = "create table " + TABLE_NAME +
            "(_id Integer,timestamp text,package_name text,crash_info text,simple_info text)";

}
