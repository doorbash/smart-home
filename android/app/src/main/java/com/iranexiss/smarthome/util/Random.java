package com.iranexiss.smarthome.util;

/**
 * Created by Milad Doorbash on 5/31/16.
 */
public class Random {


    private static final String randomChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";


    public static String generateRand(int r) {
        String ret = "";
        for (int i = 0; i < r; i++) {
            ret += randomChars.charAt((int) (Math.random() * randomChars
                    .length()));
        }

        return ret;
    }
}
