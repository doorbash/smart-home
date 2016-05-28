package com.iranexiss.smarthome.protocol;

/**
 * Created by Milad Doorbash on 5/28/16.
 */
public class ForwardlyReportStatus extends Command {
    public static final int OPCODE = 0xefff;

    int numZones;
    int[] zonesStatus;
    int numChannels;
    boolean[] channelsStatus;

    public ForwardlyReportStatus(byte[] payload) {
        this.operationCode = OPCODE;

        numZones = payload[0];
        zonesStatus = new int[numZones];
        for (int i = 0; i < numZones; i++) {
            zonesStatus[i] = payload[i + 1];
        }
        numChannels = payload[numZones + 1];
        channelsStatus = new boolean[numChannels];
        for (int i = 0; i < numChannels; i++) {
            channelsStatus[i] = payload[2 + numZones + i] > 0;
        }
    }
}
