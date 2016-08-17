package com.iranexiss.smarthome.ui.dialog;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.model.elements.AudioPlayer;
import com.iranexiss.smarthome.protocol.Command;
import com.iranexiss.smarthome.protocol.Netctl;
import com.iranexiss.smarthome.protocol.api.Zaudio2ReadAlbumPackage;
import com.iranexiss.smarthome.protocol.api.Zaudio2ReadAlbumPackageResponse;
import com.iranexiss.smarthome.protocol.api.Zaudio2ReadQtyOfAlbumBigPackages;
import com.iranexiss.smarthome.protocol.api.Zaudio2ReadQtyOfAlbumBigPackagesResponse;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

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

    public static AudioPlayerRemoteDialog newInstance(AudioPlayer ap, AudioPlayerRemoteDialog.CallBack callback) {
        AudioPlayerRemoteDialog f = new AudioPlayerRemoteDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();

        f.callback = callback;
        f.input = ap;

        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        input.listener = new AudioPlayer.OnCommandReceivedListener() {
            @Override
            public void onCommandReceived(Command command) {
                if (command instanceof Zaudio2ReadQtyOfAlbumBigPackagesResponse) {
                    Zaudio2ReadQtyOfAlbumBigPackagesResponse response = (Zaudio2ReadQtyOfAlbumBigPackagesResponse) command;

                    if (response.source == Zaudio2ReadQtyOfAlbumBigPackagesResponse.SOURCE_SDCARD && input.sdcard) {
                        input.sdcard_data = new AudioPlayer.AudioData(response.number);
                        Netctl.sendCommand(new Zaudio2ReadAlbumPackage(Zaudio2ReadAlbumPackage.SOURCE_SDCARD, 1).setTarget(input.subnetId, input.deviceId));
                    } else if (response.source == Zaudio2ReadQtyOfAlbumBigPackagesResponse.SOURCE_FTP && input.ftp) {
                        input.ftp_data = new AudioPlayer.AudioData(response.number);
                        Netctl.sendCommand(new Zaudio2ReadAlbumPackage(Zaudio2ReadAlbumPackage.SOURCE_FTP, 1).setTarget(input.subnetId, input.deviceId));
                    }
                } else if (command instanceof Zaudio2ReadAlbumPackageResponse) {
                    Zaudio2ReadAlbumPackageResponse response = (Zaudio2ReadAlbumPackageResponse) command;
                    if (response.source == Zaudio2ReadAlbumPackageResponse.SOURCE_SDCARD) {


                        if(response.packageNumber < input.sdcard_data.qtyAlbumPackages) {
                            Netctl.sendCommand(new Zaudio2ReadAlbumPackage(Zaudio2ReadAlbumPackage.SOURCE_SDCARD, response.packageNumber + 1).setTarget(input.subnetId, input.deviceId));
                        } else {
                            // we are done with album packages. let's process albums
                            //Netctl.sendCommand(n.setTarget(input.subnetId, input.deviceId));
                        }

                    } else if (response.source == Zaudio2ReadAlbumPackageResponse.SOURCE_FTP) {


                        if(response.packageNumber < input.ftp_data.qtyAlbumPackages) {
                            Netctl.sendCommand(new Zaudio2ReadAlbumPackage(Zaudio2ReadAlbumPackage.SOURCE_FTP, response.packageNumber + 1).setTarget(input.subnetId, input.deviceId));
                        } else {
                            // we are done with album packages. let's process albums
                        }

                    }

                }
            }
        };

        readData();

        return v;
    }

    private void readData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (input.sdcard) {
                    Netctl.sendCommand(new Zaudio2ReadQtyOfAlbumBigPackages(Zaudio2ReadQtyOfAlbumBigPackages.SDCARD).setTarget(input.subnetId, input.deviceId));
                } else if (input.ftp) {
                    Netctl.sendCommand(new Zaudio2ReadQtyOfAlbumBigPackages(Zaudio2ReadQtyOfAlbumBigPackages.FTP).setTarget(input.subnetId, input.deviceId));
                }
            }
        }).start();
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        callback.onCanceled();
    }
}
