package com.mwbtech.dealer_register.LoginRegister;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.mwbtech.dealer_register.LoginRegister.ValidateUserForFirstTime.FingerprintValidate;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private final Context context;
    String unameCheck, pswdCheck, Token, FCMtoken,imageurl;

    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject, String uname, String pswd,
                          String getToken, String Fcmtoken) {
        unameCheck = uname;
        pswdCheck = pswd;
        Token = getToken;
        FCMtoken = Fcmtoken;
        Log.e("FingerPrintHandler",Fcmtoken);
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        Toast.makeText(context, "Fingerprint Authentication help\n" + helpString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(context, "Fingerprint Authentication failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        ((Activity) context).finish();
        Intent intent = new Intent(context, FingerprintValidate.class);
        intent.putExtra("fpuname", unameCheck);
        intent.putExtra("fppswd", pswdCheck);
        intent.putExtra("getToken", Token);
        intent.putExtra("FCMtoken", FCMtoken);
        context.startActivity(intent);

    }
}
