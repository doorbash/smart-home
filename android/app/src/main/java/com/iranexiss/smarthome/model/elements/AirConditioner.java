package com.iranexiss.smarthome.model.elements;

import com.iranexiss.smarthome.model.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Milad Doorbash on 8/9/16.
 */
@Table(database = AppDatabase.class)
public class AirConditioner extends BaseModel {

    public static final int DEFAULT_MIN_TEMP = 18;
    public static final int DEFAULT_MAX_TEMP = 30;

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


    public List<Integer> fan = new ArrayList<>();
    public List<Integer> mode = new ArrayList<>();
    public int modeIndex;
    public int fanIndex;
    private boolean on_old;
    private boolean on;
    public int coolTempSetPoint;
    public int heatTempSetPoint;
    public int autoTempSetPoint;
    public int temp;
    public int minCoolTemp;
    public int maxCoolTemp;
    public int minHeatTemp;
    public int maxHeatTemp;
    public int minAutoTemp;
    public int maxAutoTemp;
    public boolean fahrenheit;

    @Override
    public boolean equals(Object obj) {
        try {
            return id == ((AirConditioner) obj).id;
        } catch (Exception e) {

        }
        return false;
    }

    public int getFanStatus() {
        return fan.isEmpty() ? FAN_LOW : fan.get(fanIndex);
    }

    public int getCurrentMode() {
        return mode.isEmpty() ? MODE_AUTO : mode.get(modeIndex);
    }

    public int getCurrentMinTemp() {
        switch (getCurrentMode()) {
            case MODE_AUTO:
                return minAutoTemp;
            case MODE_COOL:
                return minCoolTemp;
            case MODE_HEAT:
                return minHeatTemp;
        }
        return DEFAULT_MIN_TEMP;
    }

    public int getCurrentMaxTemp() {
        switch (getCurrentMode()) {
            case MODE_AUTO:
                return maxAutoTemp;
            case MODE_COOL:
                return maxCoolTemp;
            case MODE_HEAT:
                return maxHeatTemp;
        }
        return DEFAULT_MAX_TEMP;
    }

    public int getCurrentSetPoint() {
        switch (getCurrentMode()) {
            case MODE_AUTO:
                return autoTempSetPoint;
            case MODE_COOL:
                return coolTempSetPoint;
            case MODE_HEAT:
                return heatTempSetPoint;
        }
        return temp;
    }

    public boolean isOn() {
        return on;
    }

    public boolean isPowerStateChanged() {
        return on_old != on;
    }

    public void resetPowerState() {
        on_old = on;
    }

    public void togglePower() {
        setOn(!on);
    }

    public void setOn(boolean status) {
        on_old = on;
        on = status;
    }

    public interface DataChanged {
        void onDataChanged();
    };
    public DataChanged listener;
}
