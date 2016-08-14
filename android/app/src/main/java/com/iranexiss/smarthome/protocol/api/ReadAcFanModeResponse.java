package com.iranexiss.smarthome.protocol.api;

import android.util.Log;

import com.iranexiss.smarthome.protocol.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Milad Doorbash on 8/13/16.
 */
public class ReadAcFanModeResponse extends Command {
    public static final int OPCODE = 0xE125;

    public List<Integer> fan = new ArrayList<>();
    public List<Integer> mode = new ArrayList<>();

    public ReadAcFanModeResponse() {
        this.operationCode = OPCODE;
    }

    public ReadAcFanModeResponse(byte[] payload) {
        this.operationCode = OPCODE;
        this.payload = payload;

        int fanSize = payload[0];
        int i = 1;
        for (; i <= fanSize; i++) {
            fan.add(((int) payload[i]));
        }
        int modeSize = payload[i++];
        for (int cnt = 0; cnt < modeSize; i++, cnt++) {
            mode.add((int) payload[i]);
        }

        Log.d("ReadAcFanModeResponse", "fan : " + fan.toString());
        Log.d("ReadAcFanModeResponse", "mode : " + mode.toString());

        // payload[i] = 0 ?
    }
}
