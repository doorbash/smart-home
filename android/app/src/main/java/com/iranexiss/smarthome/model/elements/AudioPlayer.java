package com.iranexiss.smarthome.model.elements;

import com.iranexiss.smarthome.model.AppDatabase;
import com.iranexiss.smarthome.protocol.Command;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Milad Doorbash on 8/9/16.
 */
@Table(database = AppDatabase.class)
public class AudioPlayer extends BaseModel {

    public static final int SOURCE_SDCARD = 1;
    public static final int SOURCE_FTP = 2;

    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public int x;
    @Column
    public int y;
    @Column
    public int subnetId;
    @Column
    public int deviceId;
    @Column
    public boolean sdcard;
    @Column
    public boolean ftp;
    @Column
    public boolean fm;
    @Column
    public boolean audio_in;
    @Column
    public int room;

    public static class Song {
        public int num;
        public String name;

        @Override
        public boolean equals(Object obj) {
            try {
                return num == ((Song) obj).num;
            } catch (Exception e) {

            }
            return false;
        }
    }

    public static class Album {
        public int num;
        public int qtySongBigPackages;
        public String name;
        public HashMap<Integer, Song> songs = new HashMap<>();

        @Override
        public boolean equals(Object obj) {
            try {
                return num == ((Album) obj).num;
            } catch (Exception e) {

            }
            return false;
        }

        public Album() {
            songs = new HashMap<>();
        }
    }

    public static class AudioData {
        public int qtyAlbumPackages;
        public HashMap<Integer, Album> albums = new HashMap<>();

        public AudioData() {
            albums = new HashMap<>();
        }
    }

    public HashMap<Integer,AudioData> data = new HashMap<>();


    public int getSourceInputCount() {
        int cnt = 0;
        if (sdcard) cnt++;
        if (ftp) cnt++;
        if (fm) cnt++;
        if (audio_in) cnt++;
        return cnt;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return id == ((AudioPlayer) obj).id;
        } catch (Exception e) {

        }
        return false;
    }

    public interface OnCommandReceivedListener {
        void onCommandReceived(Command command);
    }

    public OnCommandReceivedListener listener;
}