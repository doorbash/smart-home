package com.iranexiss.smarthome.model;

import com.iranexiss.smarthome.database.AppDatabase;
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
    @Column
    private int id;
    @Column
    private String name;
    @Column
    private String imagePath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


}
