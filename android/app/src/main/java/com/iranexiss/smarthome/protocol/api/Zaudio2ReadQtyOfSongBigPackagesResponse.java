package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/15/16.
 */
public class Zaudio2ReadQtyOfSongBigPackagesResponse extends Command {
    public static final int OPCODE = 0x02E5;

    public int musicSource;
    public int currentAlbumNo;
    public int qtyOfSongBigPackages;

    public Zaudio2ReadQtyOfSongBigPackagesResponse(byte[] payload) {
        this.operationCode = OPCODE;
        this.payload = payload;

        musicSource = payload[0];
        currentAlbumNo = payload[1];
        qtyOfSongBigPackages = payload[2];

    }

}
