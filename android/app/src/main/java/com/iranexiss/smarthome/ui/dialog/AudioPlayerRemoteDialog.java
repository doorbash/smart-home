package com.iranexiss.smarthome.ui.dialog;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.RoomActivity;
import com.iranexiss.smarthome.model.Room;
import com.iranexiss.smarthome.model.Room_Table;
import com.iranexiss.smarthome.model.elements.AudioPlayer;
import com.iranexiss.smarthome.protocol.Command;
import com.iranexiss.smarthome.protocol.Netctl;
import com.iranexiss.smarthome.protocol.api.Zaudio2ReadAlbumPackage;
import com.iranexiss.smarthome.protocol.api.Zaudio2ReadAlbumPackageResponse;
import com.iranexiss.smarthome.protocol.api.Zaudio2ReadQtyOfAlbumBigPackages;
import com.iranexiss.smarthome.protocol.api.Zaudio2ReadQtyOfAlbumBigPackagesResponse;
import com.iranexiss.smarthome.protocol.api.Zaudio2ReadQtyOfSongBigPackages;
import com.iranexiss.smarthome.protocol.api.Zaudio2ReadQtyOfSongBigPackagesResponse;
import com.iranexiss.smarthome.protocol.api.Zaudio2ReadSongPackage;
import com.iranexiss.smarthome.protocol.api.Zaudio2ReadSongPackageResponse;
import com.iranexiss.smarthome.ui.adapter.AlbumAdapter;
import com.iranexiss.smarthome.ui.adapter.RoomsAdapter;
import com.iranexiss.smarthome.ui.helper.RecyclerItemClickListener;
import com.iranexiss.smarthome.ui.helper.SimpleItemTouchHelperCallback;
import com.melnykov.fab.FloatingActionButton;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AudioPlayerRemoteDialog extends DialogFragment {


    public static final String TAB_SDCARD = "SD Card";
    public static final String TAB_FTP = "FTP";
    public static final String TAB_FM = "FM";
    public static final String TAB_AUDIO_IN = "Audio In";

    //_____________________________________________________ Properties  ____________________________
//    Context context;
    CallBack callback;
    SmartTabLayout viewPagerTab;
    AudioPlayer input;
    CustomPagerAdapter adapter;
    ViewPager viewPager;
    Iterator<Integer> iterator;
    boolean timeout = false;
    boolean dataReadDone = false;
    private AlbumAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public interface CallBack {
        void onCanceled();
    }

    class SDCardFragment extends Fragment {

        View v;

        ProgressBar loading;
        private RecyclerView mRecyclerView;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            v = inflater.inflate(R.layout.audio_sdcard, null, false);

            loading = (ProgressBar) v.findViewById(R.id.loading);


            mRecyclerView = (RecyclerView) v.findViewById(R.id.album_recycler);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);


            if (mAdapter == null) {
                mAdapter = new AlbumAdapter(getActivity(), input.data.get(AudioPlayer.SOURCE_SDCARD).albums);
                mRecyclerView.setAdapter(mAdapter);
            }

            RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Log.d("AudioPlayerRemoteDialog", "go to album");
                }
            };

            mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), onItemClickListener));


            loadData();

            return v;
        }

        public void loadData() {

            AudioPlayer.AudioData data = input.data.get(AudioPlayer.SOURCE_SDCARD);

            if (data != null && !data.albums.isEmpty()) {
                loading.setVisibility(View.GONE);

                if (mAdapter != null) mAdapter.notifyDataSetChanged();

                mRecyclerView.setVisibility(View.VISIBLE);
            } else {
                loading.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }

        public void showTryAgain() {

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
                titles.add(TAB_SDCARD);
            }
            if (input.ftp) {
                titles.add(TAB_FTP);
            }
            if (input.fm) {
                titles.add(TAB_FM);
            }
            if (input.audio_in) {
                titles.add(TAB_AUDIO_IN);
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


                if (timeout) return;

//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }


                if (command instanceof Zaudio2ReadQtyOfAlbumBigPackagesResponse) {

                    Zaudio2ReadQtyOfAlbumBigPackagesResponse response = (Zaudio2ReadQtyOfAlbumBigPackagesResponse) command;


                    input.data.get(response.source).qtyAlbumPackages = response.number;
                    Netctl.sendCommand(new Zaudio2ReadAlbumPackage(response.source, 1).setTarget(input.subnetId, input.deviceId));


                } else if (command instanceof Zaudio2ReadAlbumPackageResponse) {
                    Zaudio2ReadAlbumPackageResponse response = (Zaudio2ReadAlbumPackageResponse) command;


                    // Add current big package to sound data
                    input.data.get(response.source).albums.putAll(response.albums);

                    if (response.packageNumber < input.data.get(response.source).qtyAlbumPackages) {
                        Netctl.sendCommand(new Zaudio2ReadAlbumPackage(response.source, response.packageNumber + 1).setTarget(input.subnetId, input.deviceId));
                    } else {
                        // we are done with album packages. let's process albums
                        Netctl.sendCommand(new Zaudio2ReadQtyOfSongBigPackages(response.source, 0).setTarget(input.subnetId, input.deviceId));
                    }

                } else if (command instanceof Zaudio2ReadQtyOfSongBigPackagesResponse) {

                    Zaudio2ReadQtyOfSongBigPackagesResponse response = (Zaudio2ReadQtyOfSongBigPackagesResponse) command;


                    input.data.get(response.musicSource).albums.get(response.currentAlbumNo).qtySongBigPackages = response.qtyOfSongBigPackages;

                    if (response.qtyOfSongBigPackages > 0)
                        Netctl.sendCommand(new Zaudio2ReadSongPackage(response.musicSource, response.currentAlbumNo, 1).setTarget(input.subnetId, input.deviceId));
                    else if (input.data.get(response.musicSource).albums.get(response.currentAlbumNo) != null) {


                        // We are done with this album let's go to another one

                        Netctl.sendCommand(new Zaudio2ReadQtyOfSongBigPackages(response.musicSource, response.currentAlbumNo + 1).setTarget(input.subnetId, input.deviceId));


                    } else if (iterator.hasNext()) {
                        Netctl.sendCommand(new Zaudio2ReadQtyOfAlbumBigPackages(iterator.next()).setTarget(input.subnetId, input.deviceId));
                    } else {


                        // there is no album to process.. seems we are done;

                        Log.d("AudioPlayerRemoteDialog", "DONE!");

                        doneReadData();
                    }

                } else if (command instanceof Zaudio2ReadSongPackageResponse) {
                    Zaudio2ReadSongPackageResponse response = (Zaudio2ReadSongPackageResponse) command;

                    input.data.get(response.musicSource).albums.get(response.currentAlbumNumber).songs.putAll(response.songs);

                    if (response.currentBigPackageNumber < input.data.get(response.musicSource).albums.get(response.currentAlbumNumber).qtySongBigPackages) {

                        // This album still has big packages to get

                        Netctl.sendCommand(new Zaudio2ReadSongPackage(response.musicSource, response.currentAlbumNumber, response.currentBigPackageNumber + 1).setTarget(input.subnetId, input.deviceId));
                    } else if (input.data.get(response.musicSource).albums.get(response.currentAlbumNumber + 1) != null) {


                        // We are done with this album let's go to another one

                        Netctl.sendCommand(new Zaudio2ReadQtyOfSongBigPackages(response.musicSource, response.currentAlbumNumber + 1).setTarget(input.subnetId, input.deviceId));


                    } else if (iterator.hasNext()) {
                        Netctl.sendCommand(new Zaudio2ReadQtyOfAlbumBigPackages(iterator.next()).setTarget(input.subnetId, input.deviceId));
                    } else {
                        // there is no album to process.. seems we are done;

                        Log.d("AudioPlayerRemoteDialog", "DONE!");

                        doneReadData();
                    }

                }
            }
        };

        readData();

        return v;
    }

    private void doneReadData() {

        Log.d("AudioPlayerRemoteDialog", "doneReadData()");

        dataReadDone = true;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (adapter.titles.indexOf(TAB_SDCARD) >= 0) {
                    SDCardFragment sdCardFragment = (SDCardFragment) adapter.frags.get(adapter.titles.indexOf(TAB_SDCARD));
                    sdCardFragment.loadData();
                }
            }
        });

    }

    public void readDataTimeout() {
        // Continue reading data

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Log.d("", "Starting again...");


    }

    private void readData() {

        dataReadDone = false;

        if (input.sdcard) {
            input.data.put(AudioPlayer.SOURCE_SDCARD, new AudioPlayer.AudioData());
        } else if (input.ftp) {
            input.data.put(AudioPlayer.SOURCE_FTP, new AudioPlayer.AudioData());
        }

        iterator = input.data.keySet().iterator();

        timeout = false;

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    if (iterator.hasNext()) {
                        Netctl.sendCommand(new Zaudio2ReadQtyOfAlbumBigPackages(iterator.next()).setTarget(input.subnetId, input.deviceId));
                    }

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!dataReadDone && getDialog().isShowing()) {

                        // Timeout!

                        Log.d("AudioPlayerRemoteDialog", "Timeout!");

                        timeout = true;

                        readDataTimeout();
                    }
                } catch (Exception e) {

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
