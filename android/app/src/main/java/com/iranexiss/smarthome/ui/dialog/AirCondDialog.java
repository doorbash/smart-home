package com.iranexiss.smarthome.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.util.Font;

public class AirCondDialog extends Dialog {
    //_____________________________________________________ Properties  ____________________________
    Context context;
    EditText subnetID;
    EditText deviceID;
    EditText channelNo;
    CallBack callback;

    public interface CallBack {
        void onCanceled();
    }

    //_____________________________________________________ Constructor ____________________________
    public AirCondDialog(Context context, CallBack callback) {
        super(context);
        this.context = context;
        this.callback = callback;
    }

    //_____________________________________________________ onCreate Function ______________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.air_conditioner_remote);
    }
}
