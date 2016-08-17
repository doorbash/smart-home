package com.iranexiss.smarthome.protocol.api;

import android.util.Log;

import com.iranexiss.smarthome.model.elements.AudioPlayer;
import com.iranexiss.smarthome.protocol.Command;
import com.iranexiss.smarthome.util.MathUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Milad Doorbash on 8/15/16.
 */
public class Zaudio2ReadSongPackageResponse extends Command {
    public static final int OPCODE = 0x02E7;

    public int musicSource;
    public int currentAlbumNumber;
    public int currentBigPackageNumber;
    public int numberOfSongs;
    public HashMap<Integer, AudioPlayer.Song> songs;

    public Zaudio2ReadSongPackageResponse(byte[] payload) {
        this.operationCode = OPCODE;
        this.payload = payload;


        if (payload.length != MathUtil.toInt(payload[0], payload[1])) return;

        musicSource = payload[2];
        currentAlbumNumber = payload[3];
        currentBigPackageNumber = payload[4];
        numberOfSongs = payload[5];


        int cnt = 6;
        songs = new HashMap<>();

        Log.d("Zaudio2ReadSongPacka...","*************** " + currentAlbumNumber + " ***************");
        for (int i = 0; i < numberOfSongs; i++) {
            AudioPlayer.Song song = new AudioPlayer.Song();
            song.num = MathUtil.toInt(payload[cnt++], payload[cnt++]);
            try {
                song.name = new String(payload, cnt + 1, payload[cnt], "UTF-16");
                Log.d("Zaudio2ReadSongPacka...",">>>> [" + song.num + "] " + song.name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cnt += payload[cnt] + 1;
            songs.put(song.num, song);
        }
        Log.d("Zaudio2ReadSongPacka...","....................................................");

    }
}
