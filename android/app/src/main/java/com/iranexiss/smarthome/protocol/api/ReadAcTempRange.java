package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/13/16.
 */
public class ReadAcTempRange extends Command {

    public static final int OPCODE = 0x1900;

    public ReadAcTempRange() {
        this.operationCode = OPCODE;
    }
}