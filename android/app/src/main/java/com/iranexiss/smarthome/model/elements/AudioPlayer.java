package com.iranexiss.smarthome.model.elements;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by root on 8/9/16.
 */
public class AudioPlayer extends RealmObject {

    public static final int SOURCE_SDCARD = 1;
    public static final int SOURCE_FTP = 2;
    public static final int SOURCE_FM = 4;
    public static final int SOURCE_AUDIO_IN = 8;

    public int x;
    public int y;
    public int subnetId;
    public int deviceId;
    public int src;

    public String room;
}
