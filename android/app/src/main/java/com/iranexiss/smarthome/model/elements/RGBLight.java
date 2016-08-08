package com.iranexiss.smarthome.model.elements;

import com.iranexiss.smarthome.model.Room;

import io.realm.RealmObject;

/**
 * Created by root on 8/9/16.
 */
public class RGBLight extends RealmObject {

    public int x;
    public int y;

    public int rSubnetId;
    public int rDeviceId;
    public int rChannelNo;
    //--------------------
    public int gSubnetId;
    public int gDeviceId;
    public int gChannelNo;
    //--------------------
    public int bSubnetId;
    public int bDeviceId;
    public int bChannelNo;

    public String room;
}

