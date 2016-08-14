package com.iranexiss.smarthome.protocol.api;

import android.util.Log;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/13/16.
 */
public class PanelControlResponse extends Command {
    public static final int OPCODE = 0xE3D9;

    public int type;
    public int value;

    public PanelControlResponse(byte[] payload) {
        this.operationCode = OPCODE;
        this.payload = payload;

        type = payload[0];
        value = payload[1];

        Log.d("PanelControl","response : " + type + " " + value);
    }
}
