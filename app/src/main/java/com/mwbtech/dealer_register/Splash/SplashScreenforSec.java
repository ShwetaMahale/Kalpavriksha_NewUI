package com.mwbtech.dealer_register.Splash;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mwbtech.dealer_register.R;

import java.io.IOException;
import java.io.InputStream;


import pl.droidsonroids.gif.GifImageView;

public class SplashScreenforSec extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screenfor_sec);
        ImageView gifImageView = findViewById(R.id.img_logo);
        Glide.with(this)
                    .asGif()
                    .load(R.raw.logo_final)
                    .listener(new RequestListener<com.bumptech.glide.load.resource.gif.GifDrawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<com.bumptech.glide.load.resource.gif.GifDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(com.bumptech.glide.load.resource.gif.GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                            resource.setLoopCount(1);
                            return false;
                        }
                    })
                    .into(gifImageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashScreenforSec.this.startActivity(new Intent(SplashScreenforSec.this, LanguageScreen.class));
                //SplashScreenforSec.this.startActivity(new Intent(SplashScreenforSec.this, SplashScreenActivity.class));
                SplashScreenforSec.this.finish();
            }
        }, 2500);
    }
}