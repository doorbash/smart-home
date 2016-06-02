package com.iranexiss.smarthome.model;

import com.iranexiss.smarthome.database.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Milad Doorbash on 6/2/16.
 */
@Table(database = AppDatabase.class)
public class Element extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    private int id;

    @Column
    private int x;

    @Column
    private int y;

    @Column
    private int subnetID;

    @Column
    private int deviceID;

    @Column
    private int channelNo;

    @Column
    private String uuid;

    @Column
    private int type;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setChannelNo(int channelNo) {
        this.channelNo = channelNo;
    }

    public int getSubnetID() {
        return subnetID;
    }

    public void setSubnetID(int subnetID) {
        this.subnetID = subnetID;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public int getChannelNo() {
        return channelNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
