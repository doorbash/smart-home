package com.iranexiss.smarthome.protocol.api;

import android.support.annotation.RequiresPermission;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/13/16.
 */
public class ReadAcStatus extends Command {
    public static final int OPCODE = 0xE0EC;

    public ReadAcStatus(byte[] payload) {
        this.operationCode = OPCODE;
        this.payload = payload;
    }

    public ReadAcStatus() {
        this.operationCode = OPCODE;
    }
}
