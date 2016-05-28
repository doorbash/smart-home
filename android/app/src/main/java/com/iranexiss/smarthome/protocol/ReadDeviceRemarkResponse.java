package com.iranexiss.smarthome.protocol;

/**
 * Created by Milad Doorbash on 5/28/16.
 */
public class ReadDeviceRemarkResponse extends Command {

    public static final int OPCODE = 0x000F;

    String remark;

    public ReadDeviceRemarkResponse(byte[] payload) {
        this.operationCode = OPCODE;
        remark = "";
        for(byte b : payload)
        {
            remark += (char) b;
        }
        remark = remark.trim();
    }

}