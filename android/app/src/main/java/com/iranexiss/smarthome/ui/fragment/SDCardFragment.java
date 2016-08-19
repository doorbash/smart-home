package com.iranexiss.smarthome.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.model.elements.AudioPlayer;
import com.iranexiss.smarthome.ui.adapter.AlbumAdapter;
import com.iranexiss.smarthome.ui.adapter.SongAdapter;
import com.iranexiss.smarthome.ui.helper.RecyclerItemClickListener;
import com.iranexiss.smarthome.util.WordsCapitalizer;

import java.util.HashMap;

/**
 * Created by Milad Doorbash on 8/19/16.
 */
public class SDCardFragment extends Fragment implements AudioFragmentsInterface {

    View v;

    ProgressBar loading;
    RecyclerView albumRecyclerView;
    LinearLayout songsLayout;
    RecyclerView songsRecyclerView;
    TextView albumTitle;
    TextView albumNumSongs;
    TextView tracksText;
    LinearLayout horizontalLine;

    private AlbumAdapter albumAdapter;
    private RecyclerView.LayoutManager albumLayoutManager;
    private SongAdapter songAdapter;
    private RecyclerView.LayoutManager songLayoutManager;
    private AudioPlayer input;

    public enum UiState {
        TRY_AGAIN,
        LOADING,
        ALBUMS,
        SONGS
    }

    UiState uiState = null;

    public void setUiState(UiState state) {
        if (this.uiState == state) return;

        switch (state) {

            case ALBUMS:


                loading.setVisibility(View.GONE);
                albumRecyclerView.setVisibility(View.VISIBLE);
                songsLayout.setVisibility(View.GONE);
                tracksText.setVisibility(View.GONE);
                horizontalLine.setVisibility(View.GONE);

                break;
            case SONGS:

                loading.setVisibility(View.GONE);
                albumRecyclerView.setVisibility(View.GONE);
                songsLayout.setVisibility(View.VISIBLE);
                tracksText.setVisibility(View.VISIBLE);
                horizontalLine.setVisibility(View.VISIBLE);

                break;

            case LOADING:
                loading.setVisibility(View.VISIBLE);
                albumRecyclerView.setVisibility(View.GONE);
                songsLayout.setVisibility(View.GONE);
                tracksText.setVisibility(View.GONE);
                horizontalLine.setVisibility(View.GONE);
                break;

            case TRY_AGAIN:
                break;
        }

        uiState = state;
    }

    public SDCardFragment(AudioPlayer input) {
        this.input = input;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.audio_sdcard, null, false);

        loading = (ProgressBar) v.findViewById(R.id.loading);
        albumRecyclerView = (RecyclerView) v.findViewById(R.id.album_recycler);
        songsLayout = (LinearLayout) v.findViewById(R.id.songs_layout);
        songsRecyclerView = (RecyclerView) v.findViewById(R.id.song_recycler);
        albumTitle = (TextView) v.findViewById(R.id.album_title);
        albumNumSongs = (TextView) v.findViewById(R.id.album_numsongs);
        tracksText = (TextView) v.findViewById(R.id.tracks_text);
        horizontalLine = (LinearLayout) v.findViewById(R.id.horizontal_line);


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        albumRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        albumLayoutManager = new LinearLayoutManager(getActivity());
        albumRecyclerView.setLayoutManager(albumLayoutManager);


        if (albumAdapter == null) {
            albumAdapter = new AlbumAdapter(getActivity(), input.data.get(AudioPlayer.SOURCE_SDCARD).albums);
            albumRecyclerView.setAdapter(albumAdapter);
        }

        RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("AudioPlayerRemoteDialog", "go to album");
                gotoAlbum(albumAdapter.albums.get(position));
            }
        };

        albumRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), onItemClickListener));

        // Song

        songsRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        songLayoutManager = new LinearLayoutManager(getActivity());
        songsRecyclerView.setLayoutManager(songLayoutManager);

        if (songAdapter == null) {
            songAdapter = new SongAdapter(getActivity(), new HashMap<Integer, AudioPlayer.Song>());
            songsRecyclerView.setAdapter(songAdapter);
        }

        RecyclerItemClickListener.OnItemClickListener onSongItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("AudioPlayerRemoteDialog", "play song!");
                playSong(songAdapter.songs.get(position + 1));
            }
        };

        songsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), onSongItemClickListener));

        loadData();

        return v;
    }

    private void playSong(AudioPlayer.Song song) {
        Toast.makeText(getActivity(), "You clicked on song!", Toast.LENGTH_SHORT).show();
    }

    private void gotoAlbum(AudioPlayer.Album album) {
        setUiState(UiState.SONGS);
        albumTitle.setText(WordsCapitalizer.capitalizeString(album.name.substring(0, album.name.lastIndexOf('.'))));
        albumNumSongs.setText((album.songs == null || album.songs.size() == 0) ? "Empty" : (album.songs.size() == 1 ? " 1 song" : (album.songs.size() + " songs")));
        songAdapter.songs = album.songs;
        songAdapter.notifyDataSetChanged();
    }

    public void loadData() {

        AudioPlayer.AudioData data = input.data.get(AudioPlayer.SOURCE_SDCARD);

        if (input.dataSynced) {
            setUiState(UiState.ALBUMS);
            if (albumAdapter != null) albumAdapter.notifyDataSetChanged();
        } else {
            setUiState(UiState.LOADING);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (uiState == UiState.SONGS) {
            setUiState(UiState.ALBUMS);
            return true;
        }
        return false;
    }
}
