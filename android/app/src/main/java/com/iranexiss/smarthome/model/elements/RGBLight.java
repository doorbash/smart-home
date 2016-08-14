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
    public int subnetId;
    @Column
    public int deviceId;

    @Column
    public int room;

    public int red; // 0-100
    public int green; // 0-100
    public int blue; // 0-100
    public int white; // 0-100

    @Override
    public boolean equals(Object obj) {
        try {
            return id == ((RGBLight) obj).id;
        } catch (Exception e) {

        }
        return false;
    }
}

