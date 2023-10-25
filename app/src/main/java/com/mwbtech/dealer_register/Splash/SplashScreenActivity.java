package com.mwbtech.dealer_register.Splash;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.Save_Language;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.VolumeShaper;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mwbtech.dealer_register.LoginRegister.LoginActivity;
import com.mwbtech.dealer_register.LoginRegister.RegisterActivity;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;

import java.util.Locale;

public class SplashScreenActivity extends AppCompatActivity {
    PrefManager prefManager;

    Button btnSignUp,btnLogIn;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        prefManager = new PrefManager(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //prefManager.logout();
        ImageView imageView = findViewById(R.id.img_logo);
        LinearLayout layoutAuth = findViewById(R.id.layout_auth);
        btnSignUp = findViewById(R.id.btn_sign_up);
        btnLogIn = findViewById(R.id.btn_login);
 //comment once multilingual implemented
        changeLanguage(prefManager.getLanguage().get(Save_Language));
        //changeLanguage("en");

        btnLogIn.setOnClickListener(v -> {
            Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(i);
        });

        btnSignUp.setOnClickListener(v -> {
            //Intent i = new Intent(SplashScreenActivity.this, Splash_Sec_Activity.class);
            Intent i = new Intent(SplashScreenActivity.this, RegisterActivity.class);
            startActivity(i);
        });

        //to give animation to the
      /*  int shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        new Handler().postDelayed(() -> {
            layoutAuth.setAlpha(0f);
            layoutAuth.setVisibility(View.VISIBLE);
            layoutAuth.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);
            CheckPermission();
        }, 3000);*/
    }
    private void CheckPermission() {
        if (CheckingPermissionIsEnabledOrNot()) {

        } else {
            RequestMultiplePermission();
        }
    }

    private void RequestMultiplePermission() {
        ActivityCompat.requestPermissions(SplashScreenActivity.this, new String[]
                {
                        CAMERA,
                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE,
                        RECEIVE_SMS,
                }, RequestPermissionCode);
    }

    private boolean CheckingPermissionIsEnabledOrNot() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalPermisssion = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadSMSPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && WriteExternalPermisssion && ReadExternalPermission && ReadSMSPermission) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                    }
                }
                break;
        }
    }
    public void changeLanguage(String language){
        Locale locale=new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        btnSignUp.setText(getString(R.string.text_new_user));
        btnLogIn.setText(getString(R.string.text_existing_user));
    }
}