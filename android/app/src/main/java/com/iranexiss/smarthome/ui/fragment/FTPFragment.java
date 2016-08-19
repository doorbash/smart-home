package com.iranexiss.smarthome.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iranexiss.smarthome.R;

/**
 * Created by Milad Doorbash on 8/19/16.
 */
public class FTPFragment extends Fragment implements AudioFragmentsInterface {

    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.audio_sdcard, null, false);
        return v;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
