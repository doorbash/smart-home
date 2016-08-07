package com.iranexiss.smarthome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iranexiss.smarthome.R;

/**
 * Created by root on 8/7/16.
 */
public class DrawerMenuAdapter extends BaseAdapter {

    String[] list = new String[]{"Home", "Settings", "Help & feedback",};
    Context context;
    private static LayoutInflater inflater = null;

    public DrawerMenuAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = ((LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_drawer_list, null);

        TextView title = (TextView) v.findViewById(R.id.title);

        title.setText(list[i]);

        return v;
    }
}
