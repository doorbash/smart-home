package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by root on 8/13/16.
 */
public class ReadAcStatusResponse extends Command {
    public static final int OPCODE = 0xE0ED;

    public boolean on;
    public int coolTempSetPoint;
    public int heatTempSetPoint;
    public int autoTempSetPoint;

    public int fanIndex;
    public int modeIndex;

    public int temp;

    public ReadAcStatusResponse(byte[] payload) {
        this.operationCode = OPCODE;
        this.payload = payload;

        on = payload[0] > 0;
        coolTempSetPoint = payload[1];
        heatTempSetPoint = payload[5];
        autoTempSetPoint = payload[7];

        fanIndex = payload[2] & 0x0F;
        modeIndex = (payload[2] >> 4) & 0x0F;

        temp = payload[4];
    }

    public ReadAcStatusResponse() {
        this.operationCode = OPCODE;
    }
}
