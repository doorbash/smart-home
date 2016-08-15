package com.iranexiss.smarthome.model.elements;

import android.graphics.Color;

import com.iranexiss.smarthome.model.AppDatabase;
import com.pavelsikun.vintagechroma.util.ColorUtil;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;


/**
 * Created by Milad Doorbash on 8/9/16.
 */

@Table(database = AppDatabase.class)
public class RGBLight extends BaseModel {

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
    public int room;

    public int red; // 0-100
    public int green; // 0-100
    public int blue; // 0-100
    public int white = 100; // 0-100
    public int color;

    @Override
    public boolean equals(Object obj) {
        try {
            return id == ((RGBLight) obj).id;
        } catch (Exception e) {

        }
        return false;
    }

    public void setColor(int color) {
        red = (int) (((float) Color.red(color) / 0xFF) * 100);
        green = (int) (((float) Color.green(color) / 0xFF) * 100);
        blue = (int) (((float) Color.blue(color) / 0xFF) * 100);
        white = (int) (((float) Color.alpha(color) / 0xFF) * 100);
        this.color = color;
    }

    public void setColor(int red, int green, int blue, int white) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.white = white;
        this.color = ColorUtil.mixTwoColors(Color.rgb((int) (((float)red/100) * 0xFF), (int) (((float)green/100) * 0xFF), (int) (((float)blue/100) * 0xFF)),Color.BLACK, (float) white / 100);
    }

    public int getColor() {
        return color;
    }
}

