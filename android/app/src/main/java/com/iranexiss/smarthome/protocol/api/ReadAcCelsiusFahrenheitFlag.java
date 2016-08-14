package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/13/16.
 */
public class ReadAcCelsiusFahrenheitFlag extends Command {

    public static final int OPCODE = 0xE120;

    public ReadAcCelsiusFahrenheitFlag() {
        this.operationCode = OPCODE;
    }
}
