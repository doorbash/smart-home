package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/15/16.
 */
public class Zaudio2ReadSongPackage extends Command {
    public static final int OPCODE = 0x02E6;

    public Zaudio2ReadSongPackage(int source,int album, int packageNumber) {
        this.operationCode = OPCODE;

        this.payload = new byte[]{(byte) source, (byte) album ,(byte) packageNumber};
    }
}
