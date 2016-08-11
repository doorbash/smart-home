package com.iranexiss.smarthome.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iranexiss.smarthome.R;

/**
 * Created by root on 8/11/16.
 */
public class PlaybackFragment extends android.app.Fragment {

    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.playback, container, false);
        return v;
    }
}
