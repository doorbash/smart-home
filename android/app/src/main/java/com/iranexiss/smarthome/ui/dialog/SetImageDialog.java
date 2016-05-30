package com.iranexiss.smarthome.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.util.Font;

public class SetImageDialog extends Dialog {
    //_____________________________________________________ Properties  ____________________________
    Context context;
    TextView title;
    Button submit;
    LinearLayout image_holder;

    //_____________________________________________________ Constructor ____________________________
    public SetImageDialog(Context context) {
        super(context);
        this.context = context;
    }

    //_____________________________________________________ onCreate Function ______________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_input_image);


        title = (TextView) findViewById(R.id.title);
        submit = (Button) findViewById(R.id.submit);
        image_holder = (LinearLayout) findViewById(R.id.image_holder);


        title.setTypeface(Font.getInstance(context).iranSans);
        submit.setTypeface(Font.getInstance(context).iranSans);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });


        image_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog dialog = new PickImageDialog(context);
                dialog.show();
            }
        });
    }
}
