package com.iranexiss.smarthome.model;

import com.iranexiss.smarthome.database.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

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

    public List<Element> elements;

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "elements")
    public List<Element> getElements() {
        if (elements == null || elements.isEmpty()) {
            elements = SQLite.select()
                    .from(Element.class)
                    .where(Element_Table.room_id.eq(id))
                    .queryList();
        }
        return elements;
    }

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
            Room temp = new Room();

            temp.setFields(room);
            room.setFields(this);
            setFields(temp);

            save();
            room.save();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setFields(Room room) {
        setName(room.getName());
        setImagePath(room.getImagePath());
        setUuid(room.getUuid());
    }
}
