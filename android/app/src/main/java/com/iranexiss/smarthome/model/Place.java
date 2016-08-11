package com.iranexiss.smarthome.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by root on 8/9/16.
 */
@Table(database = AppDatabase.class)
public class Place extends BaseModel {
    @PrimaryKey(autoincrement = true)
    public int id;
    @Column
    public String name;


    @Override
    public boolean equals(Object obj) {
        try {
            return id == ((Place)obj).id;
        }
        catch (Exception e){

        }
        return false;
    }
}
