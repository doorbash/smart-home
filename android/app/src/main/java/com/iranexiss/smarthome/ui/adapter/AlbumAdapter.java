package com.iranexiss.smarthome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.model.Room;
import com.iranexiss.smarthome.model.Room_Table;
import com.iranexiss.smarthome.model.elements.AudioPlayer;
import com.iranexiss.smarthome.ui.helper.ItemTouchHelperAdapter;
import com.iranexiss.smarthome.util.Font;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private HashMap<Integer, AudioPlayer.Album> albums;
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
        public ImageView image;
        public TextView numSongs;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            image = (ImageView) v.findViewById(R.id.image);
            numSongs = (TextView) v.findViewById(R.id.num_songs);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AlbumAdapter(Context context, HashMap<Integer, AudioPlayer.Album> albums) {
        this.albums = albums;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_album, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(capitalizeString(albums.get(position).name.substring(0, albums.get(position).name.lastIndexOf('.'))));

        holder.numSongs.setText((albums.get(position).songs == null || albums.get(position).songs.isEmpty()) ? "empty" : (albums.get(position).songs.size() == 1 ? "1 song" : albums.get(position).songs.size() + " songs"));

        holder.name.setTypeface(Font.getInstance(context).iranSansBold);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return albums.size();
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }


}
