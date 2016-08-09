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
public class AdjustableLight extends BaseModel {
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public int x;
    @Column
    public int y;
    @Column
    public int subnetID;
    @Column
    public int deviceId;
    @Column
    public int channelId;
    @Column
    public int room;
}
