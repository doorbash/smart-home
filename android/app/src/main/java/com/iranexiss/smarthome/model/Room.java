package com.iranexiss.smarthome.model;

import com.iranexiss.smarthome.database.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
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

    @Column
    private String uuid;

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


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean swap(Room room) {
        try {
            String tempName = room.getName();
            String tempImagePath = room.getImagePath();
            String tempUuid = room.getUuid();

            room.setName(getName());
            room.setImagePath(getImagePath());
            room.setUuid(getUuid());

            setName(tempName);
            setImagePath(tempImagePath);
            setUuid(tempUuid);

            save();
            room.save();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
