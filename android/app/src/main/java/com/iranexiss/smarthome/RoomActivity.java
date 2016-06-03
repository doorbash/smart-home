package com.iranexiss.smarthome;

import android.content.ClipData;
import android.graphics.Point;
import android.os.Handler;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iranexiss.smarthome.model.Element;
import com.iranexiss.smarthome.model.Room;
import com.iranexiss.smarthome.model.Room_Table;
import com.iranexiss.smarthome.protocol.Command;
import com.iranexiss.smarthome.protocol.ForwardlyReportStatus;
import com.iranexiss.smarthome.protocol.Netctl;
import com.iranexiss.smarthome.protocol.SingleChannelControl;
import com.iranexiss.smarthome.ui.dialog.AddLampDialog;
import com.iranexiss.smarthome.ui.dialog.RoomPopup;
import com.iranexiss.smarthome.util.Font;
import com.iranexiss.smarthome.util.Random;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {

    boolean lightStatus;
    Room room;
    RelativeLayout toolbar;
    public static final long INIT_IDLE_TIME = 7000;
    long idleTime = 0;
    boolean stopIdleThread = false;
    boolean pauseIdeThread = false;
    boolean toolbarStatus = true;
    int roomID;
    TextView name;
    UiState uiState = UiState.NORMAL;
    int subnet;
    int device;
    int channel;
    RelativeLayout roomLayout;
    View testCircle;
    ElementOnLongClickListener elementOnLongClickListener = new ElementOnLongClickListener();

    private enum UiState {
        NORMAL,
        SET_POINT
    }

    public void setUiState(UiState state) {
        if (uiState == state) return;
        uiState = state;
        switch (state) {
            case NORMAL:
                break;
            case SET_POINT:
                // TODO: Alert user to select a point on screen
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        roomID = getIntent().getIntExtra("room", 0);

        room = SQLite.select()
                .from(Room.class)
                .where(Room_Table.id.is(roomID))
                .queryList().get(0);

        final ImageView image = (ImageView) findViewById(R.id.image);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        name = (TextView) findViewById(R.id.name);
        roomLayout = (RelativeLayout) findViewById(R.id.room_layout);
        testCircle = findViewById(R.id.test_circle);

        name.setTypeface(Font.getInstance(this).iranSansBold);
        name.setText(room.getName());

        Glide
                .with(this)
                .load(room.getImagePath())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image);

        Netctl.init(new Netctl.IEventHandler() {
            @Override
            public void onCommand(Command command) {
                Log.d("FREPORT",command.toString());
                if (command instanceof ForwardlyReportStatus) {
                    Log.d("FREPORT", ((ForwardlyReportStatus) command).channelsStatus[8] + "");
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
                                if (uiState == UiState.NORMAL) {
                                    idleTime = INIT_IDLE_TIME;
                                    toolbarIn();
                                }
                            }
                        });
                    }
                });
            }
        }, 300);

        image.setOnDragListener(new ElementDragListener());

        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (uiState == UiState.SET_POINT) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            Log.d("TouchTest", "ACTION_DOWN" + " (" + event.getX() + "," + event.getY() + ")");
                            Log.d("PointSelected", event.getX() + " , " + event.getY());
                            setUiState(UiState.NORMAL);

                            // Add the element to database
                            Element element = new Element();
                            element.setUuid(Random.generateRand(25));
                            element.setChannelNo(channel);
                            element.setSubnetID(subnet);
                            element.setDeviceID(device);
                            element.setX((int) (event.getX() - testCircle.getWidth() / 2));
                            element.setY((int) (event.getY() - testCircle.getHeight() / 2));
                            element.setType(Element.TYPE_LAMP);
                            element.setRoom(room.getId());

                            element.insert();

                            room.elements.add(element);

                            showElementsOnScreen();

                            break;
                        case MotionEvent.ACTION_MOVE:
                            Log.d("TouchTest", "ACTION_MOVE" + " (" + event.getX() + "," + event.getY() + ")");
                            break;
                        case MotionEvent.ACTION_UP:
                            Log.d("TouchTest", "ACTION_UP" + " (" + event.getX() + "," + event.getY() + ")");
                            break;
                        default:
                            Log.d("TouchTest", event.getAction() + " (" + event.getX() + "," + event.getY() + ")");
                    }

                    return true;
                }
                return false;
            }
        });

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

        showElementsOnScreen();
    }

    @Override
    protected void onDestroy() {
        Netctl.destroy();
        stopIdleThread = true;
        super.onDestroy();
    }


    public void popup(View v) {
        pauseIdeThread = true;
        idleTime = INIT_IDLE_TIME;
        final RoomPopup roomPopup = new RoomPopup(this, v, room, new Runnable() {
            public void run() {
                pauseIdeThread = false;
                room = SQLite.select()
                        .from(Room.class)
                        .where(Room_Table.id.is(roomID))
                        .queryList().get(0);
                name.setText(room.getName());
            }
        });
        roomPopup.show();
    }

    public void onLampClicked(View v) {
        AddLampDialog dialog = new AddLampDialog(RoomActivity.this, new AddLampDialog.CallBack() {
            @Override
            public void onSubmited(int subnet, int device, int channel) {
                RoomActivity.this.subnet = subnet;
                RoomActivity.this.device = device;
                RoomActivity.this.channel = channel;
                pauseIdeThread = false;
                setUiState(UiState.SET_POINT);
            }

            @Override
            public void onCanceled() {
                pauseIdeThread = false;
            }
        });
        dialog.show();
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
        if (toolbarStatus) {
            toolbarOut(null);
        } else {
            super.onBackPressed();
        }
    }

    private void showElementsOnScreen() {

        // Remove all elements from screen
        List<View> removeList = new ArrayList<>();
        for (int i = 0; i < roomLayout.getChildCount(); i++) {
            View v = roomLayout.getChildAt(i);
            if (v instanceof ImageButton) {
                // Add view to remove list
                removeList.add(v);
            }
        }

        for (View v : removeList) {
            roomLayout.removeView(v);
        }

        // Add all room's elements to screen
        List<Element> elements = room.getElements();
        for (Element element : elements) {
            View elementView = getLayoutInflater().inflate(R.layout.element, null, false);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            params.leftMargin = element.getX();
            params.topMargin = element.getY();

            elementView.setTag(element);

            elementView.setOnClickListener(new ElementOnClickListener());
            elementView.setOnLongClickListener(elementOnLongClickListener);


            roomLayout.addView(elementView, params);
        }
    }


    class ElementOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Element element = (Element) v.getTag();
            element.channels[element.getChannelNo() - 1] = !element.channels[element.getChannelNo() - 1];


            if (element.channels[element.getChannelNo() - 1]) {
                ((ImageView) v).setImageResource(R.drawable.light_on);
                Netctl.sendCommand(new SingleChannelControl(element.getChannelNo(), 100, 0).setTarget(element.getSubnetID(), element.getDeviceID()));
            } else {
                ((ImageView) v).setImageResource(R.drawable.light_off);
                Netctl.sendCommand(new SingleChannelControl(element.getChannelNo(), 0, 0).setTarget(element.getSubnetID(), element.getDeviceID()));
            }
        }
    }

    class ElementOnLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            Log.d("DRAG_TEST", "ALRIGHT DRAG STARTED!!!!!");
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new ElementDragShadowBuilder(v);
            v.startDrag(data, shadowBuilder, v, 0);
            v.setVisibility(View.INVISIBLE);
            return true;
        }
    }


    class ElementDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:

                    View elementView = (View) event.getLocalState();
                    Element element = (Element) elementView.getTag();

                    element.setX((int) (event.getX() - testCircle.getWidth() / 2));
                    element.setY((int) (event.getY() - testCircle.getHeight() / 2));
                    element.save();


                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) elementView.getLayoutParams();
                    params.leftMargin = element.getX();
                    params.topMargin = element.getY();
                    elementView.setLayoutParams(params);

                    elementView.setVisibility(View.VISIBLE);

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }
    }

    class ElementDragShadowBuilder extends View.DragShadowBuilder {

        public ElementDragShadowBuilder(View v) {
            super(v);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {

            shadowTouchPoint.offset(-testCircle.getWidth() / 2, -testCircle.getHeight() / 2);

            super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
        }
    }


}
