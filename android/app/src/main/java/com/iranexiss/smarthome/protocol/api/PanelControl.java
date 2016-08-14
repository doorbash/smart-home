package com.iranexiss.smarthome.protocol.api;

import android.util.Log;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/13/16.
 */
public class PanelControl extends Command {
    public static final int OPCODE = 0xE3D8;


    public static final int IR_RECEIVER_FUNCTION = 1;
    public static final int BUTTON_LOCK = 2;
    public static final int AC_ON_OFF = 3;
    public static final int COOL_TEMP_SET_POINT = 4;
    public static final int FAN_SPEED = 5;
    public static final int AC_MODE = 6;
    public static final int HEAT_TEMP_SET_POINT = 7;
    public static final int AUTO_TEMP_SET_POINT = 8;
    public static final int INVOKING_DDP_BUTTON = 12;
    public static final int GO_TO_PAGE = 16;


    public PanelControl(int type, int value) {
        this.operationCode = OPCODE;
        this.payload = new byte[]{(byte) type, (byte) value};
        Log.d("PanelControl",type + " " + value);
    }

    public PanelControl(byte[] payload) {
        this.operationCode = OPCODE;
        this.payload = payload;
    }
}
