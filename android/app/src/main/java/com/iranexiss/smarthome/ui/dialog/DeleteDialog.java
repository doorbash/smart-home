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
import com.iranexiss.smarthome.util.Font;

public class DeleteDialog extends Dialog {
    //_____________________________________________________ Properties  ____________________________
    Context context;
    TextView title;
    Button yes;
    Button no;
    CallBack callback;

    public interface CallBack {
        void onSubmitted(boolean result);

        void onCanceled();
    }

    //_____________________________________________________ Constructor ____________________________
    public DeleteDialog(Context context, CallBack callback) {
        super(context);
        this.context = context;
        this.callback = callback;
    }

    //_____________________________________________________ onCreate Function ______________________
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete);


        title = (TextView) findViewById(R.id.title);
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);

        title.setTypeface(Font.getInstance(context).iranSans);
        yes.setTypeface(Font.getInstance(context).iranSans);
        no.setTypeface(Font.getInstance(context).iranSans);

        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    dismiss();

                    callback.onSubmitted(true);

                } catch (Exception e) {
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {
                    dismiss();

                    callback.onSubmitted(false);

                } catch (Exception e) {
                }
            }
        });

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                callback.onCanceled();
            }
        });
    }
}
