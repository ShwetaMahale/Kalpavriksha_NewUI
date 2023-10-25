package com.mwbtech.dealer_register.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.R;

public class WelcomeScreen extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        //DealerRegister adUrl = (DealerRegister) getIntent().getSerializableExtra("adUrl");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(WelcomeScreen.this, DashboardActivity.class).putExtra("isNewUser", false);
                //Intent i = new Intent(WelcomeScreen.this, ThankyouScreen.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
