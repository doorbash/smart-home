package com.iranexiss.smarthome.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.model.elements.AudioPlayer;
import com.iranexiss.smarthome.model.elements.AudioPlayer_Table;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

public class AudioPlayerRemoteDialog extends DialogFragment {
    //_____________________________________________________ Properties  ____________________________
//    Context context;
    CallBack callback;
    SmartTabLayout viewPagerTab;
    AudioPlayer input;
    CustomPagerAdapter adapter;
    ViewPager viewPager;

    public interface CallBack {
        void onCanceled();
    }

    class SDCardFragment extends Fragment {

        View v;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.audio_sdcard, null, false);
            return v;
        }
    }

    class FTPFragment extends Fragment {

        View v;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.audio_sdcard, null, false);
            return v;
        }
    }

    class FMFragment extends Fragment {

        View v;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.audio_sdcard, null, false);
            return v;
        }
    }

    class AudioInFragment extends Fragment {

        View v;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.audio_sdcard, null, false);
            return v;
        }
    }


    private class CustomPagerAdapter extends FragmentStatePagerAdapter {

        public List<Fragment> frags;
        public List<String> titles;

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
            titles = new ArrayList<>();
            if (input.sdcard) {
                titles.add("SD Card");
            }
            if (input.ftp) {
                titles.add("FTP");
            }
            if (input.fm) {
                titles.add("FM");
            }
            if (input.audio_in) {
                titles.add("Audio In");
            }
        }

        @Override
        public Fragment getItem(int position) {

            if (frags == null) {
                frags = new ArrayList<>();

                if (input.sdcard) {
                    frags.add(new SDCardFragment());
                }
                if (input.ftp) {
                    frags.add(new FTPFragment());
                }
                if (input.fm) {
                    frags.add(new FMFragment());
                }
                if (input.audio_in) {
                    frags.add(new AudioInFragment());
                }
            }

            return frags.get(position);
        }

        @Override
        public int getCount() {
            return input.getSourceInputCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles == null ? "" : titles.get(position);
        }
    }

    public static AudioPlayerRemoteDialog newInstance(int audioPlayerId, AudioPlayerRemoteDialog.CallBack callback) {
        AudioPlayerRemoteDialog f = new AudioPlayerRemoteDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();

        f.callback = callback;
        args.putInt("audiplayerid", audioPlayerId);

        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int audioPlayerId = getArguments().getInt("audiplayerid");
        this.input = SQLite.select().from(AudioPlayer.class).where(AudioPlayer_Table.id.is(audioPlayerId)).querySingle();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.audio_player_remote, container, false);

        viewPager = (ViewPager) v.findViewById(R.id.vp);

        adapter = new CustomPagerAdapter(getChildFragmentManager());

        viewPager.setAdapter(adapter);

        viewPagerTab = (SmartTabLayout) v.findViewById(R.id.viewpagertab);


        viewPagerTab.setViewPager(viewPager);


        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        return v;
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        callback.onCanceled();
    }
}
