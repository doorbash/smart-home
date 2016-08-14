package com.iranexiss.smarthome.protocol.api;

import android.util.Log;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/13/16.
 */
public class ReadAcCelsiusFahrenheitFlagResponse extends Command {

    public static final int OPCODE = 0xE121;

    public boolean fahrenheit;

    public ReadAcCelsiusFahrenheitFlagResponse(byte[] payload) {
        this.operationCode = OPCODE;
        this.payload = payload;

        fahrenheit = payload[0] > 0;

        Log.d("ReadAcCelsiusFahrenheit", "fahrenheit = " + fahrenheit);
    }
}
