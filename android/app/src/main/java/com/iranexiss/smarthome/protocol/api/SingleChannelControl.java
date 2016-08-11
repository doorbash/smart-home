package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;
import com.iranexiss.smarthome.util.MathUtil;

/**
 * Created by Milad Doorbash on 5/28/16.
 */
public class SingleChannelControl extends Command {
    public static final int OPCODE = 0x0031;

    int lightChannelNumber;
    int brightnessLevel;
    int runningTime;

    public SingleChannelControl(byte[] payload) {
        this.operationCode = OPCODE;
        this.payload = payload;
        lightChannelNumber = payload[0];
        brightnessLevel = payload[1];
        runningTime = MathUtil.toInt(payload[3], payload[4]);
    }

    public SingleChannelControl(int lightChannelNumber, int brightnessLevel, int runningTime) {
        this.operationCode = OPCODE;
        this.lightChannelNumber = lightChannelNumber;
        this.brightnessLevel = brightnessLevel;
        this.runningTime = runningTime;

        this.payload = new byte[]{(byte) (lightChannelNumber & 0xFF), (byte) (brightnessLevel & 0xFF), (byte) ((runningTime >> 8) & 0xFF), (byte) (runningTime & 0xFF)};
    }

}
