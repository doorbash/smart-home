package com.iranexiss.smarthome.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.model.Room;
import com.iranexiss.smarthome.util.Font;

import io.realm.Realm;

public class EditNameDialog extends Dialog {
    //_____________________________________________________ Properties  ____________________________
    Context context;
    EditText name;
    TextView title;
    Button submit;
    Room room;
    Runnable callback;

    //_____________________________________________________ Constructor ____________________________
    public EditNameDialog(Context context, Room room, Runnable runnable) {
        super(context);
        this.context = context;
        this.room = room;
        this.callback = runnable;
    }

    //_____________________________________________________ onCreate Function ______________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_input_name);


        title = (TextView) findViewById(R.id.title);
        name = (EditText) findViewById(R.id.name);
        submit = (Button) findViewById(R.id.submit);

        title.setTypeface(Font.getInstance(context).iranSans);
        name.setTypeface(Font.getInstance(context).iranSans);
        submit.setTypeface(Font.getInstance(context).iranSans);

        name.setText(room.name);


        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String nameString = name.getText().toString();
                if (nameString.length() > 0) {
                    dismiss();

                    Realm realm = Realm.getDefaultInstance();



                    realm.beginTransaction();
                    room.name = nameString;
//                    realm.insertOrUpdate(room);
                    realm.commitTransaction();

                    realm.close();

                    Toast.makeText(context, "نام با موفقیت تعیین شد", Toast.LENGTH_SHORT).show();

                    callback.run();

                } else {
                    Toast.makeText(context, "نام را وارد کنید", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                callback.run();
            }
        });
    }
}
