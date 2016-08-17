package com.iranexiss.smarthome.protocol.api;

import android.util.Log;

import com.bumptech.glide.ListPreloader;
import com.iranexiss.smarthome.model.elements.AudioPlayer;
import com.iranexiss.smarthome.protocol.Command;
import com.iranexiss.smarthome.util.MathUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Milad Doorbash on 8/15/16.
 */
public class Zaudio2ReadAlbumPackageResponse extends Command {
    public static final int OPCODE = 0x02E3;

    public int source;
    public int packageNumber;
    public int numberOfAlbums;
    public HashMap<Integer, AudioPlayer.Album> albums;

    public Zaudio2ReadAlbumPackageResponse(byte[] payload) {
        this.operationCode = OPCODE;
        this.payload = payload;

        if (MathUtil.toInt(payload[0], payload[1]) != payload.length) return;

        source = payload[2];
        packageNumber = payload[3];
        numberOfAlbums = payload[4];
        int cnt = 5;
        albums = new HashMap<>();
        for (int i = 0; i < numberOfAlbums; i++) {
            AudioPlayer.Album album = new AudioPlayer.Album();
            album.num = payload[cnt++];
            try {
                album.name = new String(payload, cnt + 1, payload[cnt], "UTF-16");
            } catch (Exception e) {
                e.printStackTrace();
            }
            cnt += payload[cnt] + 1;
            albums.put(album.num, album);
        }
    }
}
