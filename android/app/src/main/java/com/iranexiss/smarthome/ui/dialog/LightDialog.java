package com.iranexiss.smarthome.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.model.Room;
import com.iranexiss.smarthome.model.elements.OnOffLight;
import com.iranexiss.smarthome.model.elements.RGBLight;
import com.iranexiss.smarthome.util.Font;

public class LightDialog extends Dialog {
    //_____________________________________________________ Properties  ____________________________
    Context context;
    EditText subnetID;
    EditText deviceID;
    TextView title;
    Button submit;
    CallBack callback;
    Room room;
    Object input;
    EditText channelNo;
    TextView typeTitle;
    RadioButton typeOnOff;
    RadioButton typeRgb;
    RadioGroup typeGroup;
    TextView addressTitle;
    LinearLayout typeLayout;

    public interface CallBack {
        void onSubmitted(Object output);

        void onCanceled();
    }

    //_____________________________________________________ Constructor ____________________________
    public LightDialog(Context context, Object input, Room room, CallBack callback) {
        super(context);
        this.context = context;
        this.callback = callback;
        this.room = room;
        this.input = input;
    }

    //_____________________________________________________ onCreate Function ______________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.light_dialog);


        title = (TextView) findViewById(R.id.title);
        subnetID = (EditText) findViewById(R.id.subnet_id);
        deviceID = (EditText) findViewById(R.id.device_id);
        channelNo = (EditText) findViewById(R.id.channel_no);
        submit = (Button) findViewById(R.id.submit);
        typeTitle = (TextView) findViewById(R.id.light_type_title);
        addressTitle = (TextView) findViewById(R.id.light_type_title);
        typeOnOff = (RadioButton) findViewById(R.id.type_on_off);
        typeRgb = (RadioButton) findViewById(R.id.type_rgb);
        typeGroup = (RadioGroup) findViewById(R.id.type_radio_group);
        addressTitle = (TextView) findViewById(R.id.address_title);
        typeLayout = (LinearLayout) findViewById(R.id.type_layout);

        if (input != null) {
            title.setText("ویرایش روشنایی");
            if (input instanceof OnOffLight) {
                OnOffLight onOffLight = (OnOffLight) input;
                subnetID.setText(onOffLight.subnetID + "");
                deviceID.setText(onOffLight.deviceId + "");
                channelNo.setText(onOffLight.channelId + "");
                typeOnOff.setChecked(true);
            } else if (input instanceof RGBLight) {
                RGBLight rgbLight = (RGBLight) input;
                subnetID.setText(rgbLight.subnetId + "");
                deviceID.setText(rgbLight.deviceId + "");
                typeRgb.setChecked(true);
            }
            typeLayout.setVisibility(View.GONE);
        }


        title.setTypeface(Font.getInstance(context).iranSans);
        submit.setTypeface(Font.getInstance(context).iranSans);
        typeTitle.setTypeface(Font.getInstance(context).iranSans);
        typeRgb.setTypeface(Font.getInstance(context).iranSans);
        typeOnOff.setTypeface(Font.getInstance(context).iranSans);
        addressTitle.setTypeface(Font.getInstance(context).iranSans);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {

                    if (input != null) {

                        if (input instanceof OnOffLight) {
                            OnOffLight onOffLight = (OnOffLight) input;
                            onOffLight.subnetID = Integer.parseInt(subnetID.getText().toString());
                            onOffLight.deviceId = Integer.parseInt(deviceID.getText().toString());
                            onOffLight.channelId = Integer.parseInt(channelNo.getText().toString());
                            onOffLight.save();
                            callback.onSubmitted(onOffLight);
                        } else if (input instanceof RGBLight) {
                            RGBLight rgbLight = (RGBLight) input;
                            rgbLight.subnetId = Integer.parseInt(subnetID.getText().toString());
                            rgbLight.deviceId = Integer.parseInt(deviceID.getText().toString());
                            rgbLight.save();
                            callback.onSubmitted(rgbLight);
                        }
                    } else {

                        if (typeOnOff.isChecked()) {
                            Log.d("LightDialog","typeOnOff is checked!");
                            OnOffLight onOffLight = new OnOffLight();
                            onOffLight.subnetID = Integer.parseInt(subnetID.getText().toString());
                            onOffLight.deviceId = Integer.parseInt(deviceID.getText().toString());
                            onOffLight.channelId = Integer.parseInt(channelNo.getText().toString());
                            onOffLight.room = room.id;
                            onOffLight.save();
                            callback.onSubmitted(onOffLight);

                        } else if (typeRgb.isChecked()) {

                            Log.d("LightDialog","typeRgb is checked!");

                            RGBLight rgbLight = new RGBLight();
                            rgbLight.subnetId = Integer.parseInt(subnetID.getText().toString());
                            rgbLight.deviceId = Integer.parseInt(deviceID.getText().toString());
                            rgbLight.room = room.id;
                            rgbLight.save();
                            callback.onSubmitted(rgbLight);

                        }


                    }
                    dismiss();

                } catch (Exception e) {
                    Toast.makeText(context, "لطفا ورودی ها را کنترل کنید", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateUi();

        typeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                updateUi();
            }
        });

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                callback.onCanceled();
            }
        });
    }

    public void updateUi() {
        if (typeOnOff.isChecked()) {
            channelNo.setVisibility(View.VISIBLE);
        } else if (typeRgb.isChecked()) {
            channelNo.setVisibility(View.GONE);
        }
    }
}
