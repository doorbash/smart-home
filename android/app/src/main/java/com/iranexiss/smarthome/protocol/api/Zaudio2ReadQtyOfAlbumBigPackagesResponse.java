package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/15/16.
 */
public class Zaudio2ReadQtyOfAlbumBigPackagesResponse extends Command {
    public static final int OPCODE = 0x02E1;

    public static final int SOURCE_SDCARD = 1;
    public static final int SOURCE_FTP = 2;

    public int source;
    public int number;

    public Zaudio2ReadQtyOfAlbumBigPackagesResponse(byte[] payload) {
        this.operationCode = OPCODE;

        source = payload[0];
        number = payload[1];
    }

}
