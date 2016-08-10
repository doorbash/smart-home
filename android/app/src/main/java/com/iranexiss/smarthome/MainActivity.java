package com.iranexiss.smarthome;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iranexiss.smarthome.model.Room;
import com.iranexiss.smarthome.model.Room_Table;
import com.iranexiss.smarthome.ui.adapter.RoomsAdapter;
import com.iranexiss.smarthome.ui.customview.DrawerArrowDrawable;
import com.iranexiss.smarthome.ui.dialog.SetNameDialog;
import com.iranexiss.smarthome.ui.helper.RecyclerItemClickListener;
import com.iranexiss.smarthome.ui.helper.SimpleItemTouchHelperCallback;
import com.kbeanie.multipicker.api.CacheLocation;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.melnykov.fab.FloatingActionButton;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ImagePickerCallback {


    private DrawerLayout mDrawerLayout;
    //    private ListView mDrawerList;
    private RelativeLayout homeLayout;


    public static final int RESULT_OK = -1;

    private RecyclerView mRecyclerView;
    private RoomsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Room> rooms;
    private ItemTouchHelper mItemTouchHelper;
    private String pickerPath;
    private TextView toolbarTitle;
    private ImageView homeButton;
    DrawerArrowDrawable DAD_Home;
//    RealmChangeListener<RealmResults<Room>> roomsChangeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(myToolbar);


        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DAD_Home = new DrawerArrowDrawable(getResources(), true);

        DAD_Home.setStrokeColor(Color.WHITE);

        toolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        homeButton = (ImageView) findViewById(R.id.iv_t_home);
        homeLayout = (RelativeLayout) findViewById(R.id.rl_t_home);

        homeButton.setImageDrawable(DAD_Home);

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                DAD_Home.setParameter(slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        // Set the adapter for the list view
//        mDrawerList.setAdapter(new DrawerMenuAdapter(this));
        // Set the list's click listener
//        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(MainActivity.this, "Item " + i + " clicked!", Toast.LENGTH_SHORT).show();
//            }
//        });


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        if (mAdapter == null) {


            rooms = SQLite.select().from(Room.class).orderBy(Room_Table.time,true).queryList();

            mAdapter = new RoomsAdapter(this, rooms);
            mRecyclerView.setAdapter(mAdapter);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.attachToRecyclerView(mRecyclerView);


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetNameDialog dialog = new SetNameDialog(MainActivity.this);
                    dialog.show();
                }
            });

        }


        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("Room Activity","onItemClick()");
                Intent i = new Intent(MainActivity.this, RoomActivity.class);
                i.putExtra("room", rooms.get(position).id);
                startActivity(i);
            }
        };

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, onItemClickListener));
    }

    @Override
    protected void onDestroy() {

//        rooms.removeChangeListener(roomsChangeListener);

        super.onDestroy();
    }

    private ImagePicker imagePicker;

    public void pickImageSingle() {
        imagePicker = new ImagePicker(this);
        imagePicker.setRequestId(1234);
        imagePicker.ensureMaxSize(5090, 5090);
        imagePicker.shouldGenerateMetadata(true);
        imagePicker.shouldGenerateThumbnails(false);
        imagePicker.setImagePickerCallback(this);
        Bundle bundle = new Bundle();
        bundle.putInt("android.intent.extras.CAMERA_FACING", 1);
        imagePicker.setCacheLocation(CacheLocation.EXTERNAL_CACHE_DIR);
        imagePicker.pickImage();
    }

    public void pickImageMultiple() {
        imagePicker = new ImagePicker(this);
        imagePicker.setImagePickerCallback(this);
        imagePicker.allowMultiple();
        imagePicker.pickImage();
    }

    private CameraImagePicker cameraPicker;

    public void takePicture() {
        cameraPicker = new CameraImagePicker(this);
        cameraPicker.setImagePickerCallback(this);
        cameraPicker.shouldGenerateMetadata(true);
        cameraPicker.shouldGenerateThumbnails(false);
        pickerPath = cameraPicker.pickImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                    imagePicker.setImagePickerCallback(this);
                }
                imagePicker.submit(data);
            } else if (requestCode == Picker.PICK_IMAGE_CAMERA) {
                if (cameraPicker == null) {
                    cameraPicker = new CameraImagePicker(this);
                    cameraPicker.setImagePickerCallback(this);
                    cameraPicker.reinitialize(pickerPath);
                }
                cameraPicker.submit(data);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Log.d("ChosenImage", "path : " + resultUri.getPath());

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;

                    //Returns null, sizes are in the options variable
                    BitmapFactory.decodeFile(resultUri.getPath(), options);
                    int width = options.outWidth;
                    int height = options.outHeight;


                    Intent i = new Intent("image_choose");
                    i.putExtra("image_path", resultUri.getPath());
                    i.putExtra("image_width", width);
                    i.putExtra("image_height", height);
                    LocalBroadcastManager.getInstance(MainActivity.this).sendBroadcast(i);

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(MainActivity.this, "متاسفانه خطایی رخ داد.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onImagesChosen(List<ChosenImage> images) {
        ChosenImage image = images.get(0);
        Log.d("ChosenImage", image.toString());
        Log.d("ChosenImage", image.getOriginalPath());
        Log.d("ChosenImage", image.getMimeType());


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
//
//        int max = Math.max(width, height);
//        int min = Math.min(width, height);

        CropImage.activity(Uri.fromFile(new File(image.getOriginalPath())))
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .setAspectRatio(width, height)
                .setMinCropResultSize(width / 2, height / 2)
                .setAutoZoomEnabled(false)
                .start(this);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else
            super.onBackPressed();
    }

    public void notifyRoomsUpdated(Room room) {
        rooms.add(room);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mAdapter.reloadAllRooms();
            mAdapter.notifyDataSetChanged();
        } catch (Exception e){

        }
    }



}
