package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by Milad Doorbash on 5/28/16.
 */
public class ReadDeviceRemark extends Command {

    public static final int OPCODE = 0x000E;

    public ReadDeviceRemark() {
        this.operationCode = OPCODE;
    }

}
