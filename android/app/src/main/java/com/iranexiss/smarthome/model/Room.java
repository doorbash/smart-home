package com.iranexiss.smarthome.model;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Milad Doorbash on 5/30/16.
 */
public class Room extends RealmObject {

    @PrimaryKey
    public String uuid;

    public long time;
    public String name;
    public String imagePath;
    public int imageWidth;
    public int imageHeight;

    public int place;
}
