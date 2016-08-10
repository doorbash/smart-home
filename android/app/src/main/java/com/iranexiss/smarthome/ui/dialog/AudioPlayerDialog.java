package com.iranexiss.smarthome.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.model.Room;
import com.iranexiss.smarthome.model.elements.AudioPlayer;
import com.iranexiss.smarthome.util.Font;

public class AudioPlayerDialog extends Dialog {
    //_____________________________________________________ Properties  ____________________________
    Context context;
    EditText subnetID;
    EditText deviceID;
    TextView title;
    Button submit;
    CallBack callback;

    CheckBox sdcard;
    CheckBox ftp;
    CheckBox audioIn;
    CheckBox fm;

    AudioPlayer input;
    Room room;

    TextView inputSourceTitle;
    TextView addressTitle;


    public interface CallBack {
        void onSubmitted(AudioPlayer output);

        void onCanceled();
    }

    //_____________________________________________________ Constructor ____________________________
    public AudioPlayerDialog(Context context, AudioPlayer input, Room room, CallBack callback) {
        super(context);
        this.context = context;
        this.callback = callback;
        this.input = input;
        this.room = room;
    }

    //_____________________________________________________ onCreate Function ______________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_audio_player);


        title = (TextView) findViewById(R.id.title);
        subnetID = (EditText) findViewById(R.id.subnet_id);
        deviceID = (EditText) findViewById(R.id.device_id);
        submit = (Button) findViewById(R.id.submit);

        sdcard = (CheckBox) findViewById(R.id.sd_card);
        ftp = (CheckBox) findViewById(R.id.ftp);
        fm = (CheckBox) findViewById(R.id.fm);
        audioIn = (CheckBox) findViewById(R.id.audio_in);

        inputSourceTitle = (TextView) findViewById(R.id.input_source_title);
        addressTitle = (TextView) findViewById(R.id.address_title);

        title.setTypeface(Font.getInstance(context).iranSans);
        submit.setTypeface(Font.getInstance(context).iranSans);
        inputSourceTitle.setTypeface(Font.getInstance(context).iranSans);
        addressTitle.setTypeface(Font.getInstance(context).iranSans);

        if (input != null) {
            title.setText("ویرایش پخش کننده صدا");
            subnetID.setText(input.subnetId + "");
            deviceID.setText(input.deviceId + "");
            sdcard.setChecked(input.sdcard);
            ftp.setChecked(input.ftp);
            fm.setChecked(input.fm);
            audioIn.setChecked(input.audio_in);
        }

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {

                    int subnet = Integer.parseInt(subnetID.getText().toString());
                    int device = Integer.parseInt(deviceID.getText().toString());

                    AudioPlayer audioPlayer;

                    if (input != null) {
                        audioPlayer = input;
                    } else {
                        audioPlayer = new AudioPlayer();
                    }
                    audioPlayer.subnetId = subnet;
                    audioPlayer.deviceId = device;
                    audioPlayer.sdcard = sdcard.isChecked();
                    audioPlayer.ftp = ftp.isChecked();
                    audioPlayer.fm = fm.isChecked();
                    audioPlayer.audio_in = audioIn.isChecked();
                    audioPlayer.room = room.id;

                    audioPlayer.save();

                    dismiss();

                    callback.onSubmitted(audioPlayer);

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
