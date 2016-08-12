package com.iranexiss.smarthome;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.iranexiss.smarthome.protocol.Command;
import com.iranexiss.smarthome.protocol.Netctl;
import com.iranexiss.smarthome.protocol.api.ReadChannelsStatus;
import com.iranexiss.smarthome.protocol.api.ReadChannelsStatusResponse;
import com.iranexiss.smarthome.protocol.api.ReadDeviceRemark;
import com.iranexiss.smarthome.protocol.api.ReadDeviceRemarkResponse;
import com.iranexiss.smarthome.protocol.api.SingleChannelControl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

/**
 * Created by Milad Doorbash on 5/28/16.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ProtocolTest {

    private Context context;
    int counter = 0;

    class A {
        protected int a = 2;
    }

    class B extends A {
        protected int a = 3;
    }

    @Before
    public void setup() {
        context = InstrumentationRegistry.getTargetContext();
        counter = 0;
    }


    @Test
    public void testNumbers() {
        byte a = -1;
        byte b = (byte) 0xEE;
        int c = ((a << 8) | b) & 0xFFFF;
        Log.d("TAG", c + "");
    }

    @Test
    public void testClasses() {

        A a = new B();
        assertEquals(a.a, 2);
        B b = new B();
        assertEquals(b.a, 3);
    }

    @Test
    public void queryOnlineDeviecs() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        Netctl.init(new Netctl.IEventHandler() {
            @Override
            public void onCommand(Command command) {
                Log.d("ProtocolTest", "new command : " + command);

                if (command instanceof ReadDeviceRemarkResponse) {
                    Log.d("ProtocolTest", "new device found!");
                    counter++;
                }

                if (counter == 6) {
                    assertEquals(true, true);
                    signal.countDown();
                }
            }
        });
        Netctl.sendCommand(new ReadDeviceRemark());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (signal.getCount() == 1) {
                    Log.d("ProtocolTest", "Timeout!");
                    Netctl.destroy();
                    assertEquals(true, false);
                }
            }
        }).start();
        signal.await();
    }

    @Test
    public void testLight() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        Netctl.init(new Netctl.IEventHandler() {
            @Override
            public void onCommand(Command command) {
                Log.d("ProtocolTest", "new command : " + command);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {


                Netctl.sendCommand(new SingleChannelControl(9, 100, 0).setTarget(1, 51));

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Netctl.sendCommand(new SingleChannelControl(9, 0, 0).setTarget(1, 51));


                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Netctl.sendCommand(new SingleChannelControl(9, 100, 0).setTarget(1, 51));

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Netctl.sendCommand(new SingleChannelControl(9, 0, 0).setTarget(1, 51));


                assertEquals(true, true);
                signal.countDown();

            }
        }).start();

        signal.await();
    }

    @Test
    public void readChannelStatusTest() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);
        Netctl.init(new Netctl.IEventHandler() {
            @Override
            public void onCommand(Command command) {
                Log.d("ProtocolTest", "new command : " + command);

                if (command instanceof ReadChannelsStatusResponse) {
                    Log.d("ProtocolTest", "light status : " + (((ReadChannelsStatusResponse) command).channelsStatus[8] > 0));
                    assertEquals(true, true);
                    signal.countDown();
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {


                Netctl.sendCommand(new ReadChannelsStatus().setTarget(1, 51));

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                assertEquals(true, false);
                signal.countDown();

            }
        }).start();

        signal.await();
    }

}
