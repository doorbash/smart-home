package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/13/16.
 */
public class ReadAcTempRangeResponse extends Command {

    public static final int OPCODE = 0x1901;

    public int[] coolRange;
    public int[] heatRange;
    public int[] autoRange;

    public ReadAcTempRangeResponse(byte[] payload) {
        this.operationCode = OPCODE;
        this.payload = payload;

        coolRange = new int[2];
        heatRange = new int[2];
        autoRange = new int[2];

        coolRange[0] = payload[0];
        coolRange[1] = payload[1];
        heatRange[0] = payload[2];
        heatRange[1] = payload[3];
        autoRange[0] = payload[4];
        autoRange[1] = payload[5];
    }
}
