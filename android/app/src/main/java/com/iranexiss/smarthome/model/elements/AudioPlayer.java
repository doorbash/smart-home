package com.iranexiss.smarthome.model.elements;

import com.iranexiss.smarthome.model.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * Created by Milad Doorbash on 8/9/16.
 */
@Table(database = AppDatabase.class)
public class AudioPlayer extends BaseModel {

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
            return id == ((AudioPlayer)obj).id;
        }
        catch (Exception e){

        }
        return false;
    }
}