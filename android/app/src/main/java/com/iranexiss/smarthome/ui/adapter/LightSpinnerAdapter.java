package com.iranexiss.smarthome.ui.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.util.Font;

/**
 * Created by Milad Doorbash on 8/14/16.
 */
// Creating an Adapter Class
public class LightSpinnerAdapter extends ArrayAdapter {

    String[] titles = new String[]{"روشن خاموش", "قرمز سبز آبی"};

    Context context;

    public LightSpinnerAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_light_type_spinner, parent, false);

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setTypeface(Font.getInstance(context).iranSans);

        title.setText(titles[position]);

        return view;
    }

    // It gets a View that displays in the drop down popup the data at the specified position
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // It gets a View that displays the data at the specified position
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}