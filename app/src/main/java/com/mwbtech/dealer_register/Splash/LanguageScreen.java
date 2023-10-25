package com.mwbtech.dealer_register.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mwbtech.dealer_register.LoginRegister.LoginActivity;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;

public class LanguageScreen extends AppCompatActivity  implements View.OnClickListener{
    Button _btnEnglish,_btnHindi,_btnKannada;

    PrefManager prefManager;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_screen2);
        prefManager = new PrefManager(this);
        _btnEnglish=findViewById(R.id.btnEnglish);
        _btnKannada=findViewById(R.id.btnKannada);
        _btnHindi=findViewById(R.id.btnHindi);
        _btnEnglish.setOnClickListener(this);
        _btnHindi.setOnClickListener(this);
        _btnKannada.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEnglish:
                prefManager.SaveLangauge("en");
                _btnEnglish.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.colorPrimary));
                _btnEnglish.setTextColor(getResources().getColor(R.color.white));
                _btnHindi.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
                _btnHindi.setTextColor(getResources().getColor(R.color.colorPrimary));
                _btnKannada.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
                _btnKannada.setTextColor(getResources().getColor(R.color.colorPrimary));
                Intent i = new Intent(LanguageScreen.this, SplashScreenActivity.class);
                startActivity(i);
                break;

            case R.id.btnKannada:
                prefManager.SaveLangauge("kn");
                _btnKannada.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.colorPrimary));
                _btnKannada.setTextColor(getResources().getColor(R.color.white));
                _btnEnglish.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
                _btnEnglish.setTextColor(getResources().getColor(R.color.colorPrimary));
                _btnHindi.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
                _btnHindi.setTextColor(getResources().getColor(R.color.colorPrimary));
                Intent btnKannada = new Intent(LanguageScreen.this, SplashScreenActivity.class);
                startActivity(btnKannada);
                break;

            case R.id.btnHindi:
                prefManager.SaveLangauge("hi");
                _btnHindi.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.colorPrimary));
                _btnHindi.setTextColor(getResources().getColor(R.color.white));
                _btnEnglish.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
                _btnEnglish.setTextColor(getResources().getColor(R.color.colorPrimary));
                _btnKannada.setBackgroundTintList(getApplicationContext().getResources().getColorStateList(R.color.white));
                _btnKannada.setTextColor(getResources().getColor(R.color.colorPrimary));
                Intent btnHindi = new Intent(LanguageScreen.this, SplashScreenActivity.class);
                startActivity(btnHindi);
                break;

        }
    }
}