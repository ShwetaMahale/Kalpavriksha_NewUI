package com.mwbtech.dealer_register.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.mwbtech.dealer_register.LoginRegister.LoginActivity;
import com.mwbtech.dealer_register.R;

public class SessionDialog {
    public static void CallSessionTimeOutDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Session expired. Login again")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(context, LoginActivity.class);
                        context.startActivity(i);
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Alert!");
        alert.setIcon(R.drawable.session);
        alert.show();
    }
}
