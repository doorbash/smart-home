package com.iranexiss.smarthome;

import android.content.ClipData;
import android.content.pm.ActivityInfo;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iranexiss.smarthome.model.Room;
import com.iranexiss.smarthome.model.elements.AdjustableLight;
import com.iranexiss.smarthome.model.elements.AirConditioner;
import com.iranexiss.smarthome.model.elements.AudioPlayer;
import com.iranexiss.smarthome.model.elements.FloorHeat;
import com.iranexiss.smarthome.model.elements.OnOffLight;
import com.iranexiss.smarthome.model.elements.RGBLight;
import com.iranexiss.smarthome.protocol.Command;
import com.iranexiss.smarthome.protocol.ForwardlyReportStatus;
import com.iranexiss.smarthome.protocol.Netctl;
import com.iranexiss.smarthome.protocol.SingleChannelControl;
import com.iranexiss.smarthome.ui.dialog.AddAirCondDialog;
import com.iranexiss.smarthome.ui.dialog.AddLampDialog;
import com.iranexiss.smarthome.ui.dialog.AirCondDialog;
import com.iranexiss.smarthome.ui.dialog.RoomPopup;
import com.iranexiss.smarthome.util.Font;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class RoomActivity extends AppCompatActivity {


    Room room; // Room data
    RelativeLayout toolbar;  // Bottom toolbar
    public static final long INIT_IDLE_TIME = 10000; // Toolbar show time
    long idleTime = 0; // Toolbar show timer
    boolean stopIdleThread = false; // Toolbar thread stop
    boolean pauseIdeThread = false; // Toolbar thread pause
    boolean toolbarIsUp = true; // toolbar is up or not
    String roomID; // room id that is passed to this activity
    TextView name; // Room name (top right corner of screen)
    UiState uiState = UiState.NORMAL; // UiState (normal, set point..)
    RelativeLayout roomLayout; // main layout
    View testCircle; // circle to get Width Height of selected element
    ElementOnClickListener elementOnClickListener = new ElementOnClickListener();
    ElementOnLongClickListener elementOnLongClickListener = new ElementOnLongClickListener(); // Long click handler for elements
    Object selectedElement;

    // Current Ui sate
    private enum UiState {
        NORMAL, // Normal state
        SET_POINT // In this state user have to select a point on screen for element's position
    }

    // Setter for uiState
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

        roomID = getIntent().getStringExtra("room");

        Realm realm = Realm.getDefaultInstance();

        room = realm.where(Room.class).equalTo("uuid", roomID).findFirst();

        realm.close();

        if (room.imageWidth < room.imageHeight) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        final ImageView image = (ImageView) findViewById(R.id.image);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        name = (TextView) findViewById(R.id.name);
        roomLayout = (RelativeLayout) findViewById(R.id.room_layout);
        testCircle = findViewById(R.id.test_circle);

        name.setTypeface(Font.getInstance(this).iranSansBold);
        name.setText(room.name);

        Glide
                .with(this)
                .load(room.imagePath)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image);

        Netctl.init(new Netctl.IEventHandler() {
            @Override
            public void onCommand(Command command) {
                Log.d("FREPORT", command.toString());
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

                            if (selectedElement instanceof AdjustableLight) {

                            } else if (selectedElement instanceof AirConditioner) {

                                Realm realm = Realm.getDefaultInstance();
                                AirConditioner airConditioner = (AirConditioner) selectedElement;
                                airConditioner.x = (int) (event.getX() - testCircle.getWidth() / 2);
                                airConditioner.y = (int) (event.getY() - testCircle.getHeight() / 2);

                                realm.beginTransaction();
                                realm.copyFromRealm(airConditioner);
                                realm.commitTransaction();

                                realm.close();

                            } else if (selectedElement instanceof AudioPlayer) {

                            } else if (selectedElement instanceof FloorHeat) {

                            } else if (selectedElement instanceof OnOffLight) {
                                Realm realm = Realm.getDefaultInstance();

                                OnOffLight onOffLight = (OnOffLight) selectedElement;
                                onOffLight.x = (int) (event.getX() - testCircle.getWidth() / 2);
                                onOffLight.y = (int) (event.getY() - testCircle.getHeight() / 2);

                                realm.beginTransaction();
                                realm.insertOrUpdate(onOffLight);
                                realm.commitTransaction();

                                realm.close();
                            } else if (selectedElement instanceof RGBLight) {

                            }


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

        if (!toolbarIsUp) {
            toolbarIn();
            return;
        }

        pauseIdeThread = true;
        idleTime = INIT_IDLE_TIME;
        final RoomPopup roomPopup = new RoomPopup(this, v, room, new Runnable() {
            public void run() {
                idleTime = INIT_IDLE_TIME;
                pauseIdeThread = false;
                name.setText(room.name); // TODO: aya esm khodesh update mishe? baeed midunm
            }
        });
        roomPopup.show();
    }

    // is called when a tool on toolbar selected
    public void onToolClicked(View v) {

        if (!toolbarIsUp) {
            toolbarIn();
            return;
        }

        pauseIdeThread = true;
        idleTime = INIT_IDLE_TIME;

        switch (v.getId()) {
            case R.id.tool_lamp:
                AddLampDialog dialog = new AddLampDialog(RoomActivity.this, new AddLampDialog.CallBack() {
                    @Override
                    public void onSubmited(int subnet, int device, int channel) {
                        OnOffLight onOffLight = new OnOffLight();
                        onOffLight.subnetID = subnet;
                        onOffLight.deviceId = device;
                        onOffLight.channelId = channel;
                        onOffLight.room = roomID;
                        selectedElement = onOffLight;
                        idleTime = INIT_IDLE_TIME;
                        pauseIdeThread = false;
                        setUiState(UiState.SET_POINT);
                    }

                    @Override
                    public void onCanceled() {
                        idleTime = INIT_IDLE_TIME;
                        pauseIdeThread = false;
                    }
                });
                dialog.show();
                break;
            case R.id.tool_aircond:
                AddAirCondDialog airCondDialog = new AddAirCondDialog(RoomActivity.this, new AddAirCondDialog.CallBack() {
                    @Override
                    public void onSubmited(int subnet, int device, int acNo) {
                        AirConditioner airConditioner = new AirConditioner();
                        airConditioner.subnetId = subnet;
                        airConditioner.deviceId = device;
                        airConditioner.acNo = acNo;
                        airConditioner.room = roomID;
                        selectedElement = airConditioner;
                        idleTime = INIT_IDLE_TIME;
                        pauseIdeThread = false;
                        setUiState(UiState.SET_POINT);
                    }

                    @Override
                    public void onCanceled() {
                        idleTime = INIT_IDLE_TIME;
                        pauseIdeThread = false;
                    }
                });
                airCondDialog.show();
                break;
            case R.id.tool_music:
                Toast.makeText(RoomActivity.this, "You clicked on music!!!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tool_edit:
                Toast.makeText(RoomActivity.this, "You clicked on edit!!!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tool_delete:
                Toast.makeText(RoomActivity.this, "You clicked on delete!!!", Toast.LENGTH_SHORT).show();
                break;
        }


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
        idleTime = INIT_IDLE_TIME;
        if (toolbarIsUp) return;
        toolbarIsUp = true;
        TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE, toolbar.getHeight(), Animation.ABSOLUTE, 0);
        anim.setDuration(225);
        anim.setFillAfter(true);
        anim.setInterpolator(new LinearOutSlowInInterpolator());
        toolbar.startAnimation(anim);
    }

    public void toolbarOut(final Runnable callback) {
        if (!toolbarIsUp) return;
        toolbarIsUp = false;
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
        if (toolbarIsUp) {
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

        Realm realm = Realm.getDefaultInstance();

        List<OnOffLight> onOffLightList = realm.where(OnOffLight.class).equalTo("room", roomID).findAll();
        for (OnOffLight element : onOffLightList) {
            View elementView = getLayoutInflater().inflate(R.layout.element, null, false);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            params.leftMargin = element.x;
            params.topMargin = element.y;

            elementView.setTag(element);

            elementView.setOnClickListener(elementOnClickListener);
            elementView.setOnLongClickListener(elementOnLongClickListener);

            ((ImageView) elementView).setImageResource(R.drawable.light_off);

            elementView.setLayoutParams(params);

            roomLayout.addView(elementView);
        }

        List<AirConditioner> aircondList = realm.where(AirConditioner.class).equalTo("room", roomID).findAll();
        for (AirConditioner element : aircondList) {
            View elementView = getLayoutInflater().inflate(R.layout.element, null, false);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            params.leftMargin = element.x;
            params.topMargin = element.y;

            elementView.setTag(element);

            elementView.setOnClickListener(elementOnClickListener);
            elementView.setOnLongClickListener(elementOnLongClickListener);

            ((ImageView) elementView).setImageResource(R.drawable.ac);

            elementView.setLayoutParams(params);

            roomLayout.addView(elementView);
        }

        List<AudioPlayer> audioPlayers = realm.where(AudioPlayer.class).equalTo("room", roomID).findAll();
        for (AudioPlayer element : audioPlayers) {
            View elementView = getLayoutInflater().inflate(R.layout.element, null, false);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            params.leftMargin = element.x;
            params.topMargin = element.y;

            elementView.setTag(element);

            elementView.setOnClickListener(elementOnClickListener);
            elementView.setOnLongClickListener(elementOnLongClickListener);

            ((ImageView) elementView).setImageResource(R.drawable.music);

            elementView.setLayoutParams(params);

            roomLayout.addView(elementView);
        }

        realm.close();
    }


    class ElementOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (v.getTag() instanceof OnOffLight) {
                OnOffLight element = (OnOffLight) v.getTag();

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();


                if (element.status == OnOffLight.STATUS_OFF) {
                    element.status = OnOffLight.STATUS_ON;
                    ((ImageView) v).setImageResource(R.drawable.light_on);
                    Netctl.sendCommand(new SingleChannelControl(element.channelId, 100, 0).setTarget(element.subnetID, element.deviceId));
                } else if (element.status == OnOffLight.STATUS_ON) {
                    element.status = OnOffLight.STATUS_OFF;
                    ((ImageView) v).setImageResource(R.drawable.light_off);
                    Netctl.sendCommand(new SingleChannelControl(element.channelId, 0, 0).setTarget(element.subnetID, element.deviceId));
                }

                realm.commitTransaction();
                realm.close();

            } else if (v.getTag() instanceof AirConditioner) {
                pauseIdeThread = true;
                idleTime = INIT_IDLE_TIME;
                AirCondDialog dialog = new AirCondDialog(RoomActivity.this, new AirCondDialog.CallBack() {
                    @Override
                    public void onCanceled() {
                        idleTime = INIT_IDLE_TIME;
                        pauseIdeThread = false;
                    }
                });
                dialog.show();
            }

        }
    }

    class ElementOnLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
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
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) elementView.getLayoutParams();
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();


                    if (v.getTag() instanceof OnOffLight) {
                        OnOffLight element = (OnOffLight) v.getTag();
                        element.x = (int) (event.getX() - testCircle.getWidth() / 2);
                        element.y = (int) (event.getY() - testCircle.getHeight() / 2);
                        realm.insertOrUpdate(element);
                        params.leftMargin = element.x;
                        params.topMargin = element.y;
                    } else if (v.getTag() instanceof AirConditioner) {
                        AirConditioner element = (AirConditioner) v.getTag();
                        element.x = (int) (event.getX() - testCircle.getWidth() / 2);
                        element.y = (int) (event.getY() - testCircle.getHeight() / 2);
                        realm.insertOrUpdate(element);
                        params.leftMargin = element.x;
                        params.topMargin = element.y;
                    }

                    realm.commitTransaction();
                    realm.close();


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
