package com.iranexiss.smarthome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.iranexiss.smarthome.protocol.Command;
import com.iranexiss.smarthome.protocol.Netctl;
import com.iranexiss.smarthome.protocol.SingleChannelControl;
import com.iranexiss.smarthome.ui.adapter.RoomsAdapter;
import com.iranexiss.smarthome.ui.dialog.SetNameDialog;
import com.melnykov.fab.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    boolean lightStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Netctl.init(new Netctl.IEventHandler() {
            @Override
            public void onCommand(Command command) {

            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        if (mAdapter == null) {

            String[] myDataset = new String[]{"اتاق خواب", "راهرو", "آشپزخانه", "دفتر کار"};

            mAdapter = new RoomsAdapter(this, myDataset);
            mRecyclerView.setAdapter(mAdapter);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.attachToRecyclerView(mRecyclerView);


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetNameDialog dialog = new SetNameDialog(MainActivity.this);
                    dialog.show();
                }
            });

        }

    }


    public void toggle(View v) {
        if (lightStatus) {
            Netctl.sendCommand(new SingleChannelControl(9, 100, 0).setTarget(1, 51));
        } else {
            Netctl.sendCommand(new SingleChannelControl(9, 0, 0).setTarget(1, 51));
        }
        lightStatus = !lightStatus;
    }

    @Override
    protected void onDestroy() {
        Netctl.destroy();
        super.onDestroy();
    }
}
