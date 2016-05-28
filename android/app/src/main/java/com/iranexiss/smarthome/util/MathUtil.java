package com.iranexiss.smarthome.util;

/**
 * Created by Milad Doorbash on 5/29/16.
 */
public class MathUtil {

    public static int toInt(byte high,byte low)
    {
        return ((high << 8)  + (low & 0xFF)) & 0xFFFF;
    }
}
