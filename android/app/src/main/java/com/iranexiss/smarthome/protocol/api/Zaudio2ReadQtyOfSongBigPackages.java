package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/15/16.
 */
public class Zaudio2ReadQtyOfSongBigPackages extends Command {
    public static final int OPCODE = 0x02E4;

    public Zaudio2ReadQtyOfSongBigPackages(int source,int currentAlbumNumber) {
        this.operationCode = OPCODE;

        this.payload = new byte[]{(byte) source,(byte)currentAlbumNumber};
    }

}
