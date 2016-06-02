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
import com.iranexiss.smarthome.util.Font;

public class AddLampDialog extends Dialog {
    //_____________________________________________________ Properties  ____________________________
    Context context;
    EditText subnetID;
    EditText deviceID;
    EditText channelNo;
    TextView title;
    Button submit;
    CallBack callback;

    public interface CallBack {
        void onSubmited(int subnet, int device, int channel);

        void onCanceled();
    }

    //_____________________________________________________ Constructor ____________________________
    public AddLampDialog(Context context, CallBack callback) {
        super(context);
        this.context = context;
        this.callback = callback;
    }

    //_____________________________________________________ onCreate Function ______________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_lump);


        title = (TextView) findViewById(R.id.title);
        subnetID = (EditText) findViewById(R.id.subnet_id);
        deviceID = (EditText) findViewById(R.id.device_id);
        channelNo = (EditText) findViewById(R.id.channel_no);
        submit = (Button) findViewById(R.id.submit);

        title.setTypeface(Font.getInstance(context).iranSans);
        submit.setTypeface(Font.getInstance(context).iranSans);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {

                    int sid = Integer.parseInt(subnetID.getText().toString());
                    int did = Integer.parseInt(deviceID.getText().toString());
                    int chno = Integer.parseInt(channelNo.getText().toString());

                    dismiss();

                    callback.onSubmited(sid, did, chno);

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
