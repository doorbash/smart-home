package com.iranexiss.smarthome.model.elements;

import io.realm.RealmObject;

/**
 * Created by root on 8/9/16.
 */
public class FloorHeat extends RealmObject {
    public int x;
    public int y;
    public int subnetId;
    public int deviceId;

    public String room;
}
