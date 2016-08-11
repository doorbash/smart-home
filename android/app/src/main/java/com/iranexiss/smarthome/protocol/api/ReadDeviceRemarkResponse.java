package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 5/28/16.
 */
public class ReadDeviceRemarkResponse extends Command {

    public static final int OPCODE = 0x000F;

    String remark;

    public ReadDeviceRemarkResponse(byte[] payload) {
        this.payload = payload;
        this.operationCode = OPCODE;
        remark = "";
        for(byte b : payload)
        {
            remark += (char) b;
        }
        remark = remark.trim();
    }

}
