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
import com.iranexiss.smarthome.util.Font;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {
    private String[] mDataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public ImageView image;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            image = (ImageView) v.findViewById(R.id.image);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RoomsAdapter(Context context, String[] myDataset) {
        mDataset = myDataset;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RoomsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_rooms_list, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(mDataset[position]);

        holder.name.setTypeface(Font.getInstance(context).iranSansBold);

        switch (position) {
            case 0:

                Glide
                        .with(context)
                        .load("http://efdreams.com/data_images/dreams/bedroom/bedroom-06.jpg")
                        .centerCrop()
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.image);

                break;

            case 1:

                Glide
                        .with(context)
                        .load("http://www.johncullenlighting.co.uk/site/wp-content/uploads/2011/10/Hallway-John-Cullen-Lighting.jpg")
                        .centerCrop()
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.image);

                break;

            case 2:

                Glide
                        .with(context)
                        .load("http://photos1.zillowstatic.com/i_g/IShnhoatrwzcnb0000000000.jpg")
                        .centerCrop()
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.image);

                break;


            case 3:

                Glide
                        .with(context)
                        .load("http://wagonerdds.com/wp-content/gallery/office-tour/wagoner-reception-room.jpg")
                        .centerCrop()
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.image);

                break;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
