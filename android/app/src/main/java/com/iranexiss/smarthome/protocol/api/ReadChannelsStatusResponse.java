package com.iranexiss.smarthome.protocol.api;

import android.util.Log;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 8/12/16.
 */
public class ReadChannelsStatusResponse extends Command {
    public static final int OPCODE = 0x0034;

    int numChannels;
    public int[] channelsStatus;

    public ReadChannelsStatusResponse(byte[] payload) {
        this.operationCode = OPCODE;
        this.payload = payload;

        numChannels = payload[0];
        channelsStatus = new int[numChannels];

        Log.d("ReadChannelsStatusResp",subnetID + " , " + deviceID );

        for (int i = 0; i < numChannels; i++) {
            channelsStatus[i] = payload[i + 1];
            Log.d("ReadChannelsStatusResp","channel[" + i + "] = " + channelsStatus[i]);
        }


    }


}
