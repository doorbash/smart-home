package com.iranexiss.smarthome.model.elements;

import com.iranexiss.smarthome.model.AppDatabase;
import com.iranexiss.smarthome.model.Room;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by root on 8/9/16.
 */
@Table(database = AppDatabase.class)
public class AirConditioner extends BaseModel {

    public static final int FAN_AUTO = 0;
    public static final int FAN_HIGH = 1;
    public static final int FAN_MEDIUM = 2;
    public static final int FAN_LOW = 3;

    public static final int MODE_COOL = 0;
    public static final int MODE_HEAT = 1;
    public static final int MODE_FAN = 2;
    public static final int MODE_AUTO = 3;

    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public int x;
    @Column
    public int y;
    @Column
    public int subnetId;
    @Column
    public int deviceId;
    @Column
    public int acNo;
    @Column
    public int room;


    public int[] fan;
    public int[] mode;
    public int modeIndex;
    public int fanIndex;
    public boolean on;
    public int coolTempSetPoint;
    public int heatTempSetPoint;
    public int autoTempSetPoint;
    public int temp;

    @Override
    public boolean equals(Object obj) {
        try {
            return id == ((AirConditioner) obj).id;
        } catch (Exception e) {

        }
        return false;
    }

    public int getFanStatus() {
        return fan == null ? FAN_LOW : fan[fanIndex];
    }

    public int getCurrentMode() {
        return mode == null ? MODE_AUTO : modeIndex;
    }

//    public int getCurrentMinTemp() {
//
//    }
}
