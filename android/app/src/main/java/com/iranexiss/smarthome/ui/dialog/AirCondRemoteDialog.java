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
import com.iranexiss.smarthome.protocol.Netctl;
import com.iranexiss.smarthome.protocol.api.PanelControl;

import org.w3c.dom.Text;

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

    TextView tempC;
    TextView tempTxt;

    ImageButton power_btn;
    ImageButton fan_btn;
    ImageButton mode_btn;
    ImageButton tmp_down_btn;
    ImageButton tmp_up_btn;

//    private int temp;


    AirConditioner input;

    public interface CallBack {
        void onCanceled();
    }

    //_____________________________________________________ Constructor ____________________________
    public AirCondRemoteDialog(final Context context, AirConditioner input, CallBack callback) {
        super(context);
        this.context = context;
        this.callback = callback;
        this.input = input;
        input.listener = new AirConditioner.DataChanged() {
            @Override
            public void onDataChanged() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUi();
                    }
                });
            }
        };
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

        tempC = (TextView) findViewById(R.id.temp_c);
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
//                input.togglePower();
                Netctl.sendCommand(new PanelControl(PanelControl.AC_ON_OFF, input.isOn() ? 0 : 1).setTarget(input.subnetId, input.deviceId));
                //updateUi();
            }
        });

        tmp_down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                if (!input.isOn() || input.getCurrentMode() == AirConditioner.MODE_FAN) return;
                if (input.getCurrentSetPoint() <= input.getCurrentMinTemp()) return;
                switch (input.getCurrentMode()) {
                    case AirConditioner.MODE_AUTO:
//                        input.autoTempSetPoint = temp;
                        Netctl.sendCommand(new PanelControl(PanelControl.AUTO_TEMP_SET_POINT, input.getCurrentSetPoint() - 1).setTarget(input.subnetId, input.deviceId));
                        break;
                    case AirConditioner.MODE_COOL:
//                        input.coolTempSetPoint = temp;
                        Netctl.sendCommand(new PanelControl(PanelControl.COOL_TEMP_SET_POINT, input.getCurrentSetPoint() - 1).setTarget(input.subnetId, input.deviceId));
                        break;
                    case AirConditioner.MODE_HEAT:
//                        input.heatTempSetPoint = temp;
                        Netctl.sendCommand(new PanelControl(PanelControl.HEAT_TEMP_SET_POINT, input.getCurrentSetPoint() - 1).setTarget(input.subnetId, input.deviceId));
                        break;
                }
                //updateUi();
            }
        });

        tmp_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                if (!input.isOn() || input.getCurrentMode() == AirConditioner.MODE_FAN) return;
                if (input.getCurrentSetPoint() >= input.getCurrentMaxTemp()) return;
//                temp++;
                switch (input.getCurrentMode()) {
                    case AirConditioner.MODE_AUTO:
//                        input.autoTempSetPoint = temp;
                        Netctl.sendCommand(new PanelControl(PanelControl.AUTO_TEMP_SET_POINT, input.getCurrentSetPoint() + 1).setTarget(input.subnetId, input.deviceId));
                        break;
                    case AirConditioner.MODE_COOL:
//                        input.coolTempSetPoint = temp;
                        Netctl.sendCommand(new PanelControl(PanelControl.COOL_TEMP_SET_POINT, input.getCurrentSetPoint() + 1).setTarget(input.subnetId, input.deviceId));
                        break;
                    case AirConditioner.MODE_HEAT:
//                        input.heatTempSetPoint = temp;
                        Netctl.sendCommand(new PanelControl(PanelControl.HEAT_TEMP_SET_POINT, input.getCurrentSetPoint() + 1).setTarget(input.subnetId, input.deviceId));
                        break;
                }
                //updateUi();
            }
        });

        fan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                if (!input.isOn() || input.fan.isEmpty()) return;
                int fi = input.fanIndex;
                fi--;
                fi = fi < 0 ? (input.fan.size() - 1) : fi % input.fan.size();
                Netctl.sendCommand(new PanelControl(PanelControl.FAN_SPEED, input.fan.get(fi)).setTarget(input.subnetId, input.deviceId));
//                Log.d("AirCondRemote", "fan = " + input.fanIndex);
                //updateUi();
            }
        });

        mode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrate();
                if (!input.isOn() || input.mode.isEmpty()) return;
                int mi = input.modeIndex;
                mi++;
                mi = mi % input.mode.size();

                Netctl.sendCommand(new PanelControl(PanelControl.AC_MODE, input.mode.get(mi)).setTarget(input.subnetId, input.deviceId));

//                temp = input.getCurrentSetPoint();

                //updateUi();
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
        Log.d("AirConRemote", "updateUi()");
        tempTxt.setText(input.getCurrentSetPoint() + "");
        if (input.fahrenheit) {
            tempC.setText("℉");
        } else {
            tempC.setText("℃");
        }
        updateFanUi();
        updateModeUi();
        updatePowerUi();
    }

    public void updatePowerUi() {
        if (!input.isOn()) {
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
