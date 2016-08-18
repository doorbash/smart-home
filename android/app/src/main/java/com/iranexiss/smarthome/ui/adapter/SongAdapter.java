package com.iranexiss.smarthome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.model.elements.AudioPlayer;
import com.iranexiss.smarthome.ui.helper.ItemTouchHelperAdapter;
import com.iranexiss.smarthome.util.Font;
import com.iranexiss.smarthome.util.WordsCapitalizer;

import java.util.HashMap;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    public HashMap<Integer, AudioPlayer.Song> songs;
    private Context context;

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public ImageView playPause;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            playPause = (ImageView) v.findViewById(R.id.play_pause);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SongAdapter(Context context, HashMap<Integer, AudioPlayer.Song> songs) {
        this.songs = songs;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_song, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(songs.get(position + 1).name);

//        holder.name.setTypeface(Font.getInstance(context).iranSansBold);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return songs.size();
    }


}
