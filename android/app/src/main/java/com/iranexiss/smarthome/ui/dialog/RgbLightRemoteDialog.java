package com.iranexiss.smarthome.ui.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.model.elements.AirConditioner;
import com.iranexiss.smarthome.model.elements.RGBLight;
import com.iranexiss.smarthome.protocol.Netctl;
import com.iranexiss.smarthome.protocol.api.PanelControl;
import com.pavelsikun.vintagechroma.ChromaPreference;

import org.w3c.dom.Text;

public class RgbLightRemoteDialog extends Dialog {
    //_____________________________________________________ Properties  ____________________________

    Context context;
    CallBack callback;
    RGBLight input;

    ChromaPreference chromaPreference;

    public interface CallBack {
        void onCanceled();
    }

    //_____________________________________________________ Constructor ____________________________
    public RgbLightRemoteDialog(final Context context, RGBLight input, CallBack callback) {
        super(context);
        this.context = context;
        this.callback = callback;
        this.input = input;
    }

    //_____________________________________________________ onCreate Function ______________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.light_remote_dialog);

//        chromaPreference = findViewById(R.id.)


        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                callback.onCanceled();
            }
        });
    }

    public void updateUi() {
    }
}
