package com.iranexiss.smarthome.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.util.Font;

public class PickImageDialog extends Dialog {
    //_____________________________________________________ Properties  ____________________________
    Context context;
    TextView takeImage;
    TextView pickImage;

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

        takeImage = (TextView) findViewById(R.id.take_image);
        pickImage = (TextView) findViewById(R.id.pick_image);

        takeImage.setTypeface(Font.getInstance(context).iranSans);
        pickImage.setTypeface(Font.getInstance(context).iranSans);

    }
}
