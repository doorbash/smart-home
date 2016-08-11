package com.iranexiss.smarthome.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Milad Doorbash on 5/30/16.
 */

@Table(database = AppDatabase.class)
public class Room extends BaseModel {

    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public long time;
    @Column
    public String name;
    @Column
    public String imagePath;
    @Column
    public int imageWidth;
    @Column
    public int imageHeight;
    @Column
    public String uuid;
    @Column
    public int place;


    @Override
    public boolean equals(Object obj) {
        try {
            return id == ((Room)obj).id;
        }
        catch (Exception e){

        }
        return false;
    }
}
