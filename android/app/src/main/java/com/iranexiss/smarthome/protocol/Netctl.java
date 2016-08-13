package com.iranexiss.smarthome.protocol;

import android.app.Activity;
import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

/**
 * Created by Milad Doorbash on 5/28/16.
 */
public class Netctl {

    public static final String TAG = "NETCTL";
    private static final int PORT = 6000;
    public static final String DEFAULT_IP = "192.168.1.255";
    private static final int SOCKET_TIMEOUT = 10000;
    public static IEventHandler eventHandler;
    private static DatagramSocket serverSocket;
//    private static boolean stop = false;

    public static void init(final Context context, IEventHandler iEventHandler) {
//        if (eventHandler != null) return;
        eventHandler = iEventHandler;

//        if (serverSocket != null) {
//            Log.e(TAG, "Already connected.");
//            return;
//        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "Creating new socket...");
                    serverSocket = new DatagramSocket(PORT);
                    serverSocket.setBroadcast(true);
                    serverSocket.setSoTimeout(SOCKET_TIMEOUT);

                    while (true) {
//                        if (stop) return;
                        byte[] receiveData = new byte[1024];
                        try {
                            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                            Log.d(TAG, "Waiting for data...");
                            serverSocket.receive(receivePacket);
                            Command input = Command.input(receivePacket.getData(), receivePacket.getLength());
                            if (input != null)
                                eventHandler.onCommand(input);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Netctl", e.getMessage());

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ee) {
                                e.printStackTrace();
                            }

                        }
                    }


                } catch (Exception e) {
                    if (e instanceof BindException) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "Address already in use", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    e.printStackTrace();
                    Log.d(TAG, e.getMessage());
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public static void destroy() {
        try {
            serverSocket.close();
        } catch (Exception e) {

        }
        serverSocket = null;
        eventHandler = null;
//        stop = true;
    }

    public static void sendCommand(Command command) {
        sendData(command.output(), DEFAULT_IP, PORT);
    }

    public static void sendCommand(Command command, String ip, int port) {
        sendData(command.output(), ip, port);
    }

    public static void sendData(final byte[] data, final String ip, final int port) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "data >> " + new String(data));
                    SocketAddress sockaddr = new InetSocketAddress(ip, port);
                    DatagramPacket sendPacket = new DatagramPacket(data, data.length, sockaddr);
                    serverSocket.send(sendPacket);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Netctl", e.getMessage());
                }
            }
        }).start();
    }


    public interface IEventHandler {
        void onCommand(Command command);
    }

}
