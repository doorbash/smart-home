package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/15/16.
 */
public class Zaudio2ReadQtyOfAlbumBigPackages extends Command {
    public static final int OPCODE = 0x02E0;

    public static final int SDCARD = 1;
    public static final int FTP = 2;

    public Zaudio2ReadQtyOfAlbumBigPackages(int source) {
        this.operationCode = OPCODE;

        this.payload = new byte[]{(byte) source};
    }

}
