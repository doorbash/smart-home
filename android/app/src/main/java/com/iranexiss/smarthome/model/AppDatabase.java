package com.iranexiss.smarthome.model;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by root on 8/9/16.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "AppDatabase"; // we will add the .db extension

    public static final int VERSION = 1;
}