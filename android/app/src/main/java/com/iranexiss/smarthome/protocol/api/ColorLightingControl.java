package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/15/16.
 */
public class ColorLightingControl extends Command {
    public static final int OPCODE = 0xF080;

    public ColorLightingControl(int red, int green, int blue, int white, int runningTime) {
        this.operationCode = OPCODE;
        this.payload = new byte[]{(byte) red, (byte) green, (byte) blue, (byte) white, (byte) ((runningTime >> 8) & 0xFF), (byte) (runningTime & 0xFF)};
    }
}
