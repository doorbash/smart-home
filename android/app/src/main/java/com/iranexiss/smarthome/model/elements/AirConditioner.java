package com.iranexiss.smarthome.model.elements;

import com.iranexiss.smarthome.model.Room;

import io.realm.RealmObject;

/**
 * Created by root on 8/9/16.
 */
public class AirConditioner extends RealmObject {
    public int x;
    public int y;
    public int subnetId;
    public int deviceId;
    public int acNo;

    public String room;
}
