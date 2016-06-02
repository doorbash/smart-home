package com.iranexiss.smarthome.ui.dialog;

import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.model.Room;

/**
 * Created by Milad Doorbash on 6/2/16.
 */
public class RoomPopup extends PopupMenu {

    Room room;
    boolean noItemIsSelected = true;

    public RoomPopup(final Context context, View anchor, final Room room, final Runnable callback) {
        super(context, anchor);
        this.room = room;
        inflate(R.menu.room_tools);


        //registering popup with OnMenuItemClickListener
        setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_name:
                        noItemIsSelected = false;
                        EditNameDialog editNameDialog = new EditNameDialog(context, room, callback);
                        editNameDialog.show();
                        break;
                }
                return true;
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                if(noItemIsSelected) callback.run();
            }
        });
    }

}
