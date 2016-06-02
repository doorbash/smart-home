package com.iranexiss.smarthome;

import android.os.Handler;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iranexiss.smarthome.model.Room;
import com.iranexiss.smarthome.model.Room_Table;
import com.iranexiss.smarthome.protocol.Command;
import com.iranexiss.smarthome.protocol.ForwardlyReportStatus;
import com.iranexiss.smarthome.protocol.Netctl;
import com.iranexiss.smarthome.protocol.SingleChannelControl;
import com.iranexiss.smarthome.ui.dialog.RoomPopup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

public class RoomActivity extends AppCompatActivity {

    boolean lightStatus;
    Room room;
    RelativeLayout toolbar;
    public static final long INIT_IDLE_TIME = 7000;
    long idleTime = 0;
    boolean stopIdleThread = false;
    boolean pauseIdeThread = false;
    boolean toolbarStatus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        int roomID = getIntent().getIntExtra("room", 0);

        room = SQLite.select()
                .from(Room.class)
                .where(Room_Table.id.is(roomID))
                .queryList().get(0);

        final ImageView image = (ImageView) findViewById(R.id.image);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);

        Glide
                .with(this)
                .load(room.getImagePath())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image);

        Netctl.init(new Netctl.IEventHandler() {
            @Override
            public void onCommand(Command command) {
                if (command instanceof ForwardlyReportStatus) {
                    Toast.makeText(RoomActivity.this, ((ForwardlyReportStatus) command).channelsStatus[8] + "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toolbarOut(new Runnable() {
                    @Override
                    public void run() {
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                idleTime = INIT_IDLE_TIME;
                                toolbarIn();
                            }
                        });
                    }
                });
            }
        }, 300);

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    if (stopIdleThread) return;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (idleTime == 0) continue;

                    if (!pauseIdeThread) {

                        Log.d("IdleTime", idleTime + "");

                        idleTime -= 1000;

                        if (idleTime == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toolbarOut(null);
                                }
                            });
                        }
                    }

                }
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        Netctl.destroy();
        stopIdleThread = true;
        super.onDestroy();
    }

    public void toggle(View v) {
        if (lightStatus) {
            Netctl.sendCommand(new SingleChannelControl(9, 100, 0).setTarget(1, 51));
        } else {
            Netctl.sendCommand(new SingleChannelControl(9, 0, 0).setTarget(1, 51));
        }
        lightStatus = !lightStatus;
    }


    public void popup(View v) {
        pauseIdeThread = true;
        idleTime = INIT_IDLE_TIME;
        RoomPopup roomPopup = new RoomPopup(this, v, room,new Runnable() {
            public void run() {
                pauseIdeThread = false;
            }
        });
        roomPopup.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pauseIdeThread = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseIdeThread = true;
    }

    public void toolbarIn() {
        Log.d("ToolbarStatus", toolbarStatus + "");
        if (toolbarStatus) return;
        toolbarStatus = true;
        TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, toolbar.getHeight(), Animation.ABSOLUTE, 0);
        anim.setDuration(225);
        anim.setFillAfter(true);
        anim.setInterpolator(new LinearOutSlowInInterpolator());
        toolbar.startAnimation(anim);
    }

    public void toolbarOut(final Runnable callback) {
        if (!toolbarStatus) return;
        toolbarStatus = false;
        idleTime = 0;
        TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, toolbar.getHeight());
        anim.setDuration(300);
        anim.setFillAfter(true);
        anim.setInterpolator(new LinearInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                toolbar.setAlpha(0.87f);
                if (callback != null) callback.run();
            }
        });
        toolbar.startAnimation(anim);
    }

    @Override
    public void onBackPressed() {
        if(toolbarStatus) {
            toolbarOut(null);
        } else {
            super.onBackPressed();
        }
    }
}
