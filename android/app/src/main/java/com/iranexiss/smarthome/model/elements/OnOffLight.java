package com.iranexiss.smarthome.model.elements;

import android.nfc.cardemulation.OffHostApduService;

import com.iranexiss.smarthome.model.Room;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by root on 8/9/16.
 */
public class OnOffLight extends RealmObject {
    @Ignore
    public static final int STATUS_OFF = 0;
    @Ignore
    public static final int STATUS_ON = 1;


    public int x;
    public int y;
    public int subnetID;
    public int deviceId;
    public int channelId;

    public int status = STATUS_OFF;

    public String room;
}
