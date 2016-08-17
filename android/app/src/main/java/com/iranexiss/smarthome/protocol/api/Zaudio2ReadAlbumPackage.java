package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/15/16.
 */
public class Zaudio2ReadAlbumPackage extends Command {
    public static final int OPCODE = 0x02E2;

    public Zaudio2ReadAlbumPackage(int source, int packageNumber) {
        this.operationCode = OPCODE;

        this.payload = new byte[]{(byte) source, (byte) packageNumber};
    }
}
