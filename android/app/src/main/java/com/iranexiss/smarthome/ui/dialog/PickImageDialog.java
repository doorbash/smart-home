package com.iranexiss.smarthome.ui.dialog;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iranexiss.smarthome.MainActivity;
import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.util.Font;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;

public class PickImageDialog extends Dialog {
    //_____________________________________________________ Properties  ____________________________
    Context context;
    TextView takePicture;
    TextView pickImage;
    LinearLayout takePictureHolder;
    LinearLayout pickImageHolder;

    //_____________________________________________________ Constructor ____________________________
    public PickImageDialog(Context context) {
        super(context);
        this.context = context;
    }

    //_____________________________________________________ onCreate Function ______________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_pick_image);

        takePicture = (TextView) findViewById(R.id.take_image);
        pickImage = (TextView) findViewById(R.id.pick_image);

        takePicture.setTypeface(Font.getInstance(context).iranSans);
        pickImage.setTypeface(Font.getInstance(context).iranSans);

        takePictureHolder = (LinearLayout) findViewById(R.id.take_picture_holder);
        pickImageHolder = (LinearLayout) findViewById(R.id.pick_image_holder);

        takePictureHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof MainActivity) {
                    dismiss();
                    ((MainActivity)context).takePicture();
                }
            }
        });

        pickImageHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ((MainActivity)context).pickImageSingle();
            }
        });

    }
}
