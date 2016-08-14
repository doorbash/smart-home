package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/12/16.
 */
public class ReadChannelsStatus extends Command {
    public static final int OPCODE = 0x0033;

    public ReadChannelsStatus() {
        this.operationCode = OPCODE;
    }
}
