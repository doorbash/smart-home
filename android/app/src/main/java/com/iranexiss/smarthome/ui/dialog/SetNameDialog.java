package com.iranexiss.smarthome.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iranexiss.smarthome.R;
import com.iranexiss.smarthome.util.Font;

public class SetNameDialog extends Dialog {
    //_____________________________________________________ Properties  ____________________________
    Context context;
    EditText name;
    TextView title;
    Button submit;

    //_____________________________________________________ Constructor ____________________________
    public SetNameDialog(Context context) {
        super(context);
        this.context = context;
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

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String nameString = name.getText().toString();
                if (nameString.length() > 0) {
                    dismiss();
                    SetImageDialog dialog = new SetImageDialog(context,nameString);
                    dialog.show();
                } else {
                    Toast.makeText(context, "نام را وارد کنید", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
