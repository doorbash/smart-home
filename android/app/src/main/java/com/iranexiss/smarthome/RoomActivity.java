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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iranexiss.smarthome.model.Room;
import com.iranexiss.smarthome.model.Room_Table;
import com.iranexiss.smarthome.model.elements.AdjustableLight;
import com.iranexiss.smarthome.model.elements.AirConditioner;
import com.iranexiss.smarthome.model.elements.AirConditioner_Table;
import com.iranexiss.smarthome.model.elements.AudioPlayer;
import com.iranexiss.smarthome.model.elements.AudioPlayer_Table;
import com.iranexiss.smarthome.model.elements.FloorHeat;
import com.iranexiss.smarthome.model.elements.OnOffLight;
import com.iranexiss.smarthome.model.elements.OnOffLight_Table;
import com.iranexiss.smarthome.model.elements.RGBLight;
import com.iranexiss.smarthome.protocol.Command;
import com.iranexiss.smarthome.protocol.ForwardlyReportStatus;
import com.iranexiss.smarthome.protocol.Netctl;
import com.iranexiss.smarthome.protocol.SingleChannelControl;
import com.iranexiss.smarthome.ui.dialog.AddAirCondDialog;
import com.iranexiss.smarthome.ui.dialog.AudioPlayerDialog;
import com.iranexiss.smarthome.ui.dialog.DeleteDialog;
import com.iranexiss.smarthome.ui.dialog.LightDialog;
import com.iranexiss.smarthome.ui.dialog.AirCondDialog;
import com.iranexiss.smarthome.ui.dialog.RoomPopup;
import com.iranexiss.smarthome.util.Font;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.List;
;

public class RoomActivity extends AppCompatActivity {


    Room room; // Room data
    RelativeLayout toolbar;  // Bottom toolbar
    public static final long TIMER_INIT = 10000; // Toolbar show time
    long timer = 0; // Toolbar show timer
    boolean stopIdleThread = false; // Toolbar thread stop
    boolean pauseTimer = false; // Toolbar thread pause
    boolean toolbarIsUp = true; // toolbar is up or not
    int roomId; // room id that is passed to this activity
    TextView name; // Room name (top right corner of screen)
    UiState uiState = UiState.NORMAL; // UiState (normal, set point..)
    RelativeLayout roomLayout; // main layout
    View testCircle; // circle to get Width Height of selected element
    ElementOnClickListener elementOnClickListener = new ElementOnClickListener();
    ElementOnLongClickListener elementOnLongClickListener = new ElementOnLongClickListener(); // Long click handler for elements
    Object selectedElement;

    LinearLayout editDeleteLayout;
    RelativeLayout editLayout;
    RelativeLayout deleteLayout;
    TextView editText;
    TextView deleteText;

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
                Toast.makeText(RoomActivity.this, "یک نقطه را روی صفحه انتخاب کنید.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Log.d("Room Activity", "onCreate()");

        roomId = getIntent().getIntExtra("room", 0);

        room = SQLite.select().from(Room.class).where(Room_Table.id.is(roomId)).queryList().get(0);

//        if (room.imageWidth < room.imageHeight) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        } else {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }

        final ImageView image = (ImageView) findViewById(R.id.image);
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        name = (TextView) findViewById(R.id.name);
        roomLayout = (RelativeLayout) findViewById(R.id.room_layout);
        testCircle = findViewById(R.id.test_circle);

        editDeleteLayout = (LinearLayout) findViewById(R.id.edit_delete_layout);
        editLayout = (RelativeLayout) findViewById(R.id.edit_layout);
        deleteLayout = (RelativeLayout) findViewById(R.id.delete_layout);
        editText = (TextView) findViewById(R.id.edit_text);
        deleteText = (TextView) findViewById(R.id.delete_text);

        editText.setTypeface(Font.getInstance(this).iranSansBold);
        deleteText.setTypeface(Font.getInstance(this).iranSansBold);

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
                                    if (toolbarIsUp) toolbarOut(null);
                                    else toolbarIn();
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


                                AirConditioner element = (AirConditioner) selectedElement;
                                element.x = (int) (event.getX() - testCircle.getWidth() / 2);
                                element.y = (int) (event.getY() - testCircle.getHeight() / 2);
                                element.save();

                            } else if (selectedElement instanceof AudioPlayer) {

                                AudioPlayer element = (AudioPlayer) selectedElement;
                                element.x = (int) (event.getX() - testCircle.getWidth() / 2);
                                element.y = (int) (event.getY() - testCircle.getHeight() / 2);
                                element.save();

                            } else if (selectedElement instanceof FloorHeat) {

                            } else if (selectedElement instanceof OnOffLight) {

                                OnOffLight element = (OnOffLight) selectedElement;
                                element.x = (int) (event.getX() - testCircle.getWidth() / 2);
                                element.y = (int) (event.getY() - testCircle.getHeight() / 2);

                                element.save();
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

                    if (timer == 0) continue;

                    if (!pauseTimer) {

                        Log.d("IdleTime", timer + "");

                        timer -= 1000;

                        if (timer == 0) {
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

        Log.d("Room Activity", "Caller1");
        showElementsOnScreen();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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

        pauseTimer = true;
        timer = TIMER_INIT;
        final RoomPopup roomPopup = new RoomPopup(this, v, room, new Runnable() {
            public void run() {
                timer = TIMER_INIT;
                pauseTimer = false;
                name.setText(room.name);
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

        pauseTimer = true;
        timer = TIMER_INIT;

        switch (v.getId()) {
            case R.id.tool_lamp:
                LightDialog dialog = new LightDialog(RoomActivity.this, null, room, new LightDialog.CallBack() {
                    @Override
                    public void onSubmitted(Object light) {
                        selectedElement = light;
                        pauseTimer = false;
                        setUiState(UiState.SET_POINT);
                    }

                    @Override
                    public void onCanceled() {
                        pauseTimer = false;
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
                        airConditioner.room = roomId;
                        selectedElement = airConditioner;
                        pauseTimer = false;
                        setUiState(UiState.SET_POINT);
                    }

                    @Override
                    public void onCanceled() {
                        pauseTimer = false;
                    }
                });
                airCondDialog.show();
                break;
            case R.id.tool_music:
                AudioPlayerDialog musicPlayer = new AudioPlayerDialog(RoomActivity.this, null, room, new AudioPlayerDialog.CallBack() {

                    @Override
                    public void onSubmitted(AudioPlayer output) {
                        selectedElement = output;
                        pauseTimer = false;
                        setUiState(UiState.SET_POINT);
                    }

                    @Override
                    public void onCanceled() {
                        pauseTimer = false;
                    }
                });
                musicPlayer.show();
                break;
//            case R.id.tool_edit:
//                Toast.makeText(RoomActivity.this, "Select an element to edit.", Toast.LENGTH_LONG).show();
//                break;
//            case R.id.tool_delete:
//                Toast.makeText(RoomActivity.this, "یک المان را برای ", Toast.LENGTH_SHORT).show();
//                break;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        pauseTimer = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTimer = true;
    }

    public void toolbarIn() {
        timer = TIMER_INIT;
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
        timer = 0;
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

        Log.d("Room Activity", "showElementsOnScreen()");

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


        List<OnOffLight> onOffLightList = SQLite.select().from(OnOffLight.class).where(OnOffLight_Table.room.is(roomId)).queryList();
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

        List<AirConditioner> aircondList = SQLite.select().from(AirConditioner.class).where(AirConditioner_Table.room.is(roomId)).queryList();
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

        List<AudioPlayer> audioPlayers = SQLite.select().from(AudioPlayer.class).where(AudioPlayer_Table.room.is(roomId)).queryList();
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
    }


    class ElementOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (v.getTag() instanceof OnOffLight) {
                OnOffLight element = (OnOffLight) v.getTag();


                if (!element.status) {
                    element.status = true;
                    ((ImageView) v).setImageResource(R.drawable.light_on);
                    Netctl.sendCommand(new SingleChannelControl(element.channelId, 100, 0).setTarget(element.subnetID, element.deviceId));
                } else {
                    element.status = false;
                    ((ImageView) v).setImageResource(R.drawable.light_off);
                    Netctl.sendCommand(new SingleChannelControl(element.channelId, 0, 0).setTarget(element.subnetID, element.deviceId));
                }

            } else if (v.getTag() instanceof AirConditioner) {
                pauseTimer = true;
                timer = TIMER_INIT;
                AirCondDialog dialog = new AirCondDialog(RoomActivity.this, new AirCondDialog.CallBack() {
                    @Override
                    public void onCanceled() {
                        pauseTimer = false;
                    }
                });
                dialog.show();
            } else if(v.getTag() instanceof AudioPlayer) {
                pauseTimer = true;
                timer = TIMER_INIT;
                AirCondDialog dialog = new AirCondDialog(RoomActivity.this, new AirCondDialog.CallBack() {
                    @Override
                    public void onCanceled() {
                        pauseTimer = false;
                    }
                });
                dialog.show();
            }

        }
    }

    class ElementOnLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            editDeleteLayout.setVisibility(View.VISIBLE);
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
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:


                    int elementX = (int) (event.getX());
                    int elementY = (int) (event.getY());

                    final View elementView = (View) event.getLocalState();
                    final Object tag = elementView.getTag();


                    Log.d("Room Activity", elementX + "," + elementY);
                    Log.d("Room Activity", (editLayout.getX() + editDeleteLayout.getX()) + " - " + editLayout.getWidth() + " ::: " + (editLayout.getY() + editDeleteLayout.getY()) + " - " + editLayout.getHeight());
                    Log.d("Room Activity", (editLayout.getX() + deleteLayout.getX()) + " - " + deleteLayout.getWidth() + " ::: " + (editLayout.getY() + deleteLayout.getY()) + " - " + deleteLayout.getHeight());


                    if (elementX > (editDeleteLayout.getX() + editLayout.getX()) && elementX < (editDeleteLayout.getX() + editLayout.getX() + editLayout.getWidth()) && elementY > (editDeleteLayout.getY() + editLayout.getY()) && elementY < (editDeleteLayout.getY() + editLayout.getY() + editLayout.getHeight())) {
                        // dropped in edit area
                        Log.d("Room Activity", "Dropped on Edit Layout");

                        if(tag instanceof BaseModel) {

                            pauseTimer = true;
                            timer = TIMER_INIT;

                            if (tag instanceof OnOffLight) {
                                LightDialog dialog = new LightDialog(RoomActivity.this, tag, room, new LightDialog.CallBack() {
                                    @Override
                                    public void onSubmitted(Object output) {
                                        // edit finished
                                        Toast.makeText(RoomActivity.this, "روشنایی با موفقیت ویرایش شد.", Toast.LENGTH_SHORT).show();
                                        pauseTimer = false;
                                    }

                                    @Override
                                    public void onCanceled() {
                                        // edit canceled
                                        pauseTimer = false;
                                    }
                                });
                                dialog.show();
                            } else if (tag instanceof AirConditioner) {

                            } else if (tag instanceof AudioPlayer) {
                                AudioPlayer audioPlayer = (AudioPlayer) tag;
                                AudioPlayerDialog dialog = new AudioPlayerDialog(RoomActivity.this, audioPlayer, room, new AudioPlayerDialog.CallBack() {
                                    @Override
                                    public void onSubmitted(AudioPlayer output) {
                                        Toast.makeText(RoomActivity.this, "پخش کننده صدا با موفقیت ویرایش شد.", Toast.LENGTH_SHORT).show();
                                        pauseTimer = false;
                                    }

                                    @Override
                                    public void onCanceled() {
                                        pauseTimer = false;
                                    }
                                });
                                dialog.show();
                            }
                        }

                    } else if (elementX > (editDeleteLayout.getX() + deleteLayout.getX()) && elementX < (editDeleteLayout.getX() + deleteLayout.getX() + deleteLayout.getWidth()) && elementY > (editDeleteLayout.getY() + deleteLayout.getY()) && elementY < (editDeleteLayout.getY() + deleteLayout.getY() + deleteLayout.getHeight())) {
                        // dropped in delete area
                        Log.d("Room Activity", "Dropped on Delete Layout");
                        DeleteDialog deleteDialog = new DeleteDialog(RoomActivity.this, new DeleteDialog.CallBack() {
                            @Override
                            public void onSubmitted(boolean result) {
                                if (result) {

                                    if (tag instanceof BaseModel) {
                                        ((BaseModel) tag).delete();
                                    }

                                    showElementsOnScreen();
                                    Toast.makeText(RoomActivity.this, "با موفقیت حذف شد.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            @Override
                            public void onCanceled() {

                            }
                        });
                        deleteDialog.show();
                    } else if (elementY > toolbar.getY()) {
                        // dropped in toolbar
                        Log.d("Room Activity", "Dropped on Toolbar");
                    } else {
                        // somewhere else
                        Log.d("Room Activity", "Dropped somewhere else");
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) elementView.getLayoutParams();

                        if (tag instanceof OnOffLight) {
                            OnOffLight element = (OnOffLight) tag;
                            element.x = (int) (event.getX() - testCircle.getWidth() / 2);
                            element.y = (int) (event.getY() - testCircle.getHeight() / 2);
                            element.save();
                            params.leftMargin = element.x;
                            params.topMargin = element.y;
                        } else if (tag instanceof AirConditioner) {
                            AirConditioner element = (AirConditioner) tag;
                            element.x = (int) (event.getX() - testCircle.getWidth() / 2);
                            element.y = (int) (event.getY() - testCircle.getHeight() / 2);
                            element.save();
                            params.leftMargin = element.x;
                            params.topMargin = element.y;
                        } else if (tag instanceof AudioPlayer) {
                            AudioPlayer element = (AudioPlayer) tag;
                            element.x = (int) (event.getX() - testCircle.getWidth() / 2);
                            element.y = (int) (event.getY() - testCircle.getHeight() / 2);
                            element.save();
                            params.leftMargin = element.x;
                            params.topMargin = element.y;
                        }
                        elementView.setLayoutParams(params);
                    }

                    editDeleteLayout.setVisibility(View.INVISIBLE);
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
