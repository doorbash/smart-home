package com.iranexiss.smarthome.ui.dialog;


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

public class AirCondRemoteDialog extends Dialog {
    //_____________________________________________________ Properties  ____________________________
    Context context;
    CallBack callback;

    ImageButton autoMode;
    ImageButton coolMode;
    ImageButton fanMode;
    ImageButton heatMode;

    ImageView fan0;
    ImageView fan1;
    ImageView fan2;
    TextView fanAuto;

    ImageView tempC;
    TextView tempTxt;

    ImageButton power_btn;
    ImageButton fan_btn;
    ImageButton mode_btn;
    ImageButton tmp_down_btn;
    ImageButton tmp_up_btn;


    AirConditioner input;

    public interface CallBack {
        void onCanceled();
    }

    //_____________________________________________________ Constructor ____________________________
    public AirCondRemoteDialog(Context context, AirConditioner input, CallBack callback) {
        super(context);
        this.context = context;
        this.callback = callback;
        this.input = input;
    }

    //_____________________________________________________ onCreate Function ______________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.MATCH_PARENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.air_conditioner_remote);


        autoMode = (ImageButton) findViewById(R.id.imgMode0);
        coolMode = (ImageButton) findViewById(R.id.imgMode1);
        fanMode = (ImageButton) findViewById(R.id.imgMode3);
        heatMode = (ImageButton) findViewById(R.id.imgMode4);

        fan0 = (ImageView) findViewById(R.id.imgFan0);
        fan1 = (ImageView) findViewById(R.id.imgFan1);
        fan2 = (ImageView) findViewById(R.id.imgFan2);
        fanAuto = (TextView) findViewById(R.id.txtFanAuto);

        tempC = (ImageView) findViewById(R.id.temp_c);
        tempTxt = (TextView) findViewById(R.id.txtTemp);

        power_btn = (ImageButton) findViewById(R.id.power_btn);
        fan_btn = (ImageButton) findViewById(R.id.fan_btn);
        mode_btn = (ImageButton) findViewById(R.id.mode_btn);
        tmp_down_btn = (ImageButton) findViewById(R.id.tmp_down_btn);
        tmp_up_btn = (ImageButton) findViewById(R.id.tmp_up_btn);

        power_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                input.on = !input.on;
                // TODO: send command here
                updateUi();
            }
        });

        tmp_down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                if (!input.on) return;
                if (input.temp <= 18) return;
                input.temp--;
                updateUi();
            }
        });

        tmp_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                if (!input.on) return;
                if (input.temp >= 30) return;
                input.temp++;
                updateUi();
            }
        });

        fan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                if (!input.on && input.fan != null) return;
                input.fanIndex--;
                input.fanIndex = input.fanIndex < 0 ? (input.fan.length - 1) : input.fanIndex % input.fan.length;
                Log.d("AirCondRemote", "fan = " + input.fanIndex);
                updateUi();
            }
        });

        mode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                if (!input.on && input.mode != null) return;
                input.modeIndex++;
                input.modeIndex = input.modeIndex % input.mode.length;
                updateUi();
            }
        });

        updateUi();

        RotateAnimation rotate = new RotateAnimation(0, 358, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(900);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);

        fan0.startAnimation(rotate);
        fan1.startAnimation(rotate);
        fan2.startAnimation(rotate);


        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                callback.onCanceled();
            }
        });
    }

    public void updateUi() {
        tempTxt.setText(input.temp + "");
        updateFanUi();
        updateModeUi();
        updatePowerUi();
    }

    public void updatePowerUi() {
        if (!input.on) {
            autoMode.setVisibility(View.INVISIBLE);
            coolMode.setVisibility(View.INVISIBLE);
            fanMode.setVisibility(View.INVISIBLE);
            heatMode.setVisibility(View.INVISIBLE);
            fan0.setAlpha(0.0f);
            fan1.setAlpha(0.0f);
            fan2.setAlpha(0.0f);
            fanAuto.setVisibility(View.INVISIBLE);
            tempC.setVisibility(View.INVISIBLE);
            tempTxt.setVisibility(View.INVISIBLE);
        } else {
            tempC.setVisibility(View.VISIBLE);
            tempTxt.setVisibility(View.VISIBLE);
            autoMode.setVisibility(View.VISIBLE);
            coolMode.setVisibility(View.VISIBLE);
            fanMode.setVisibility(View.VISIBLE);
            heatMode.setVisibility(View.VISIBLE);
        }
    }

    public void updateFanUi() {
        Log.d("AirCondRemote", "updateFanUi");
        Log.d("AirCondRemote", "fan is " + input.fanIndex);
        switch (input.fanIndex) {
            case AirConditioner.FAN_AUTO:
                fan0.setAlpha(1.0f);
                fan1.setAlpha(1.0f);
                fan2.setAlpha(1.0f);
                fanAuto.setVisibility(View.VISIBLE);
                break;
            case AirConditioner.FAN_LOW:
                fan0.setAlpha(1.0f);
                fan1.setAlpha(0.0f);
                fan2.setAlpha(0.0f);
                fanAuto.setVisibility(View.INVISIBLE);
                break;
            case AirConditioner.FAN_MEDIUM:
                fan0.setAlpha(1.0f);
                fan1.setAlpha(1.0f);
                fan2.setAlpha(0.0f);
                fanAuto.setVisibility(View.INVISIBLE);
                break;
            case AirConditioner.FAN_HIGH:
                fan0.setAlpha(1.0f);
                fan1.setAlpha(1.0f);
                fan2.setAlpha(1.0f);
                fanAuto.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void updateModeUi() {
        switch (input.modeIndex) {
            case AirConditioner.MODE_AUTO:
                autoMode.setBackgroundResource(R.drawable.air_mode_auto_sel);
                coolMode.setBackgroundResource(R.drawable.air_mode_cool);
                fanMode.setBackgroundResource(R.drawable.air_mode_fan);
                heatMode.setBackgroundResource(R.drawable.air_mode_heat);
                break;
            case AirConditioner.MODE_COOL:
                autoMode.setBackgroundResource(R.drawable.air_mode_auto);
                coolMode.setBackgroundResource(R.drawable.air_mode_cool_sel);
                fanMode.setBackgroundResource(R.drawable.air_mode_fan);
                heatMode.setBackgroundResource(R.drawable.air_mode_heat);
                break;
            case AirConditioner.MODE_FAN:
                autoMode.setBackgroundResource(R.drawable.air_mode_auto);
                coolMode.setBackgroundResource(R.drawable.air_mode_cool);
                fanMode.setBackgroundResource(R.drawable.air_mode_fan_sel);
                heatMode.setBackgroundResource(R.drawable.air_mode_heat);
                break;
            case AirConditioner.MODE_HEAT:
                autoMode.setBackgroundResource(R.drawable.air_mode_auto);
                coolMode.setBackgroundResource(R.drawable.air_mode_cool);
                fanMode.setBackgroundResource(R.drawable.air_mode_fan);
                heatMode.setBackgroundResource(R.drawable.air_mode_heat_sel);
                break;
        }
    }

    private void vibrate() {
        Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(50);
    }
}
