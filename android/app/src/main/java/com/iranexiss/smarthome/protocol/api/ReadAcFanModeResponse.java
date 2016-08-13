package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by root on 8/13/16.
 */
public class ReadAcFanModeResponse extends Command {
    public static final int OPCODE = 0xE125;

    public int[] fan;
    public int[] mode;

    public ReadAcFanModeResponse() {
        this.operationCode = OPCODE;
    }

    public ReadAcFanModeResponse(byte[] payload) {
        this.operationCode = OPCODE;
        this.payload = payload;

        fan = new int[payload[0]];
        int i = 1;
        for (int cnt = 0; i <= fan.length; i++, cnt++) {
            fan[cnt] = payload[i];
        }
        mode = new int[payload[i++]];
        for (int cnt = 0; i <= mode.length; i++, cnt++) {
            mode[cnt] = payload[i];
        }
        // payload[i] = 0 ?
    }
}
