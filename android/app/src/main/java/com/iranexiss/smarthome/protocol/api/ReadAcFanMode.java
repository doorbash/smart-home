package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by root on 8/13/16.
 */
public class ReadAcFanMode extends Command {
    public static final int OPCODE = 0xE124;

    public ReadAcFanMode() {
        this.operationCode = OPCODE;
    }
}
