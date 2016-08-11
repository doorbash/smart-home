package com.iranexiss.smarthome.protocol.api;

import com.iranexiss.smarthome.protocol.Command;

/**
 * Created by Milad Doorbash on 5/28/16.
 */
public class ForwardlyReportStatus extends Command {
    public static final int OPCODE = 0xefff;

    int numZones;
    int[] zonesStatus;
    int numChannels;
    public boolean[] channelsStatus;

    public ForwardlyReportStatus(byte[] payload) {
        this.payload = payload;
        this.operationCode = OPCODE;

        numZones = payload[0];
        zonesStatus = new int[numZones];
        for (int i = 0; i < numZones; i++) {
            zonesStatus[i] = payload[i + 1];
        }
        numChannels = payload[numZones + 1];
        channelsStatus = new boolean[numChannels];
        int cnt = 0;
        for (int i = 0; i < numChannels / 8 + 1; i++) {
            for (int j = 0; j < 8 && cnt < numChannels; j++) {
                channelsStatus[cnt++] = ((payload[2 + numZones + i] >> j) & 1) == 1;
            }
        }
    }
}
