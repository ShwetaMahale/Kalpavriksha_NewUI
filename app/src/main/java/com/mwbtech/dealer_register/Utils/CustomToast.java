package com.mwbtech.dealer_register.Utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mwbtech.dealer_register.R;

public class CustomToast {

    private Context context;
    public static void showToast(final Context context,String msg){

        LayoutInflater inflater = LayoutInflater.from(context);
        View toastRoot = inflater.inflate(R.layout.toast_msg, null);
        ((TextView)toastRoot.findViewById(R.id.driver)).setText(msg);
        Toast toast = new Toast(context);
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL  | Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }


}
