package com.iranexiss.smarthome.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.model.Room;
import com.iranexiss.smarthome.model.elements.OnOffLight;
import com.iranexiss.smarthome.util.Font;

public class LightDialog extends Dialog {
    //_____________________________________________________ Properties  ____________________________
    Context context;
    EditText subnetID;
    EditText deviceID;
    EditText channelNo;
    TextView title;
    Button submit;
    CallBack callback;
    Room room;
    Object input;

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

        if (input != null) {
            if (input instanceof OnOffLight) {
                title.setText("ویرایش لامپ");
                OnOffLight onOffLight = (OnOffLight) input;
                subnetID.setText(onOffLight.subnetID + "");
                deviceID.setText(onOffLight.deviceId + "");
                channelNo.setText(onOffLight.channelId+"");
            }
        }


        title.setTypeface(Font.getInstance(context).iranSans);
        submit.setTypeface(Font.getInstance(context).iranSans);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {

                    int sid = Integer.parseInt(subnetID.getText().toString());
                    int did = Integer.parseInt(deviceID.getText().toString());
                    int chNo = Integer.parseInt(channelNo.getText().toString());

                    if(input != null) {
                        if(input instanceof OnOffLight) {
                            OnOffLight onOffLight = (OnOffLight) input;
                            onOffLight.subnetID = sid;
                            onOffLight.deviceId = did;
                            onOffLight.channelId = chNo;
                            callback.onSubmitted(onOffLight);
                        }
                    }
                    else {
                        OnOffLight onOffLight = new OnOffLight();
                        onOffLight.subnetID = sid;
                        onOffLight.deviceId = did;
                        onOffLight.channelId = chNo;
                        onOffLight.room = room.id;
                        onOffLight.save();
                        callback.onSubmitted(onOffLight);
                    }
                    dismiss();

                } catch (Exception e) {
                    Toast.makeText(context, "لطفا ورودی ها را کنترل کنید", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                callback.onCanceled();
            }
        });
    }
}
