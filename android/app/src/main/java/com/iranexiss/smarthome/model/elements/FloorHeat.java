package com.iranexiss.smarthome.model.elements;

import com.iranexiss.smarthome.model.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by root on 8/9/16.
 */
@Table(database = AppDatabase.class)
public class FloorHeat extends BaseModel {

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

    @Override
    public boolean equals(Object obj) {
        try {
            return id == ((FloorHeat)obj).id;
        }
        catch (Exception e){

        }
        return false;
    }
}
