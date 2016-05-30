package com.iranexiss.smarthome.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Milad Doorbash on 5/30/16.
 */
public class Font {

    public Typeface iranSansBold;
    public Typeface iranSans;
    public Typeface koodak;

    private Font() {

    }

    static Font INSTANCE;

    public static Font getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Font();
            INSTANCE.iranSans = Typeface.createFromAsset(context.getAssets(), "Font/IRANSans.ttf");
            INSTANCE.iranSansBold = Typeface.createFromAsset(context.getAssets(), "Font/IRANSansBold.ttf");
            INSTANCE.koodak = Typeface.createFromAsset(context.getAssets(), "Font/SKOODKBD.TTF");
        }
        return INSTANCE;
    }
}
