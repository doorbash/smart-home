package com.iranexiss.smarthome.ui.dialog;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iranexiss.smarthome.MainActivity;
import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.model.Room;
import com.iranexiss.smarthome.util.Font;
import com.iranexiss.smarthome.util.Random;

public class SetImageDialog extends Dialog {
    //_____________________________________________________ Properties  ____________________________
    Context context;
    TextView title;
    Button submit;
    LinearLayout image_holder;
    ImageChooseBroadcast imageChooseBroadcast;
    ImageView selectedImage;
    ImageView newImage;
    String path;
    String roomName;

    //_____________________________________________________ Constructor ____________________________
    public SetImageDialog(Context context, String roomName) {
        super(context);
        this.context = context;
        this.roomName = roomName;
    }

    //_____________________________________________________ onCreate Function ______________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_input_image);


        title = (TextView) findViewById(R.id.title);
        submit = (Button) findViewById(R.id.submit);
        image_holder = (LinearLayout) findViewById(R.id.image_holder);
        selectedImage = (ImageView) findViewById(R.id.selected_image);
        newImage = (ImageView) findViewById(R.id.new_image);


        title.setTypeface(Font.getInstance(context).iranSans);
        submit.setTypeface(Font.getInstance(context).iranSans);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (path == null) {
                    Toast.makeText(context, "تصویر را انتخاب کنید", Toast.LENGTH_SHORT).show();
                    return;
                }
                dismiss();


                // Add room to database

                Room room = new Room();
                room.setImagePath(path);
                room.setName(roomName);
                room.setUuid(Random.generateRand(50));
                room.insert();

                // refresh MainActiviyty's adapter

                ((MainActivity) context).addRoomToList(room);

            }
        });


        image_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog dialog = new PickImageDialog(context);
                dialog.show();
            }
        });

        startListeningToImageChooseBroadcasts();
    }


    public void startListeningToImageChooseBroadcasts() {
        imageChooseBroadcast = new ImageChooseBroadcast();
        try {
            LocalBroadcastManager.getInstance(context).
                    registerReceiver(imageChooseBroadcast, new IntentFilter("image_choose"));
        } catch (Exception e) {
        }
    }

    public void stoplisteningToImageChooseBroadcasts() {

        try {
            LocalBroadcastManager.getInstance(context).
                    unregisterReceiver(imageChooseBroadcast);
        } catch (Exception e) {

        }

    }


    public class ImageChooseBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            path = intent.getStringExtra("image_path");

            Log.d("SetImageDialog", "image_path : " + path);

            selectedImage.setVisibility(View.VISIBLE);
            newImage.setVisibility(View.GONE);

            Glide
                    .with(context)
                    .load(path)
                    .centerCrop()
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(selectedImage);
        }
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        stoplisteningToImageChooseBroadcasts();
    }
}
