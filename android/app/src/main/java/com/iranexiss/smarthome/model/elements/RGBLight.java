package com.iranexiss.smarthome.model.elements;

import com.iranexiss.smarthome.model.AppDatabase;
import com.iranexiss.smarthome.model.Room;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;


/**
 * Created by Milad Doorbash on 8/9/16.
 */

@Table(database = AppDatabase.class)
public class RGBLight extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public int x;
    @Column
    public int y;

    @Column
    public int rSubnetId;
    @Column
    public int rDeviceId;
    @Column
    public int rChannelNo;
    //--------------------
    @Column
    public int gSubnetId;
    @Column
    public int gDeviceId;
    @Column
    public int gChannelNo;
    //--------------------
    @Column
    public int bSubnetId;
    @Column
    public int bDeviceId;
    @Column
    public int bChannelNo;

    @Column
    public int room;

    @Override
    public boolean equals(Object obj) {
        try {
            return id == ((RGBLight) obj).id;
        } catch (Exception e) {

        }
        return false;
    }
}

