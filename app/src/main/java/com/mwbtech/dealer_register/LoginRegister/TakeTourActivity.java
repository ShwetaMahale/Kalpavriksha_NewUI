package com.mwbtech.dealer_register.LoginRegister;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;

public class TakeTourActivity extends AppCompatActivity {

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_tour_activity);
        prefManager = new PrefManager(this);

        VideoView videoView = findViewById(R.id.video_view);
        TextView btnSkip = findViewById(R.id.skip_button);

        btnSkip.setOnClickListener(v -> {
           moveToDashboard();
        });

        videoView.setBackgroundColor(Color.TRANSPARENT);

        String path = "android.resource://" + getPackageName() + "/raw/" + R.raw.kalpavriksha_tour;
        videoView.setVideoURI(Uri.parse(path));
        videoView.setVisibility(View.VISIBLE);
        videoView.setOnCompletionListener(mp -> {
            Log.d("PlayVideo", "Video completed");
            btnSkip.setVisibility(View.GONE);
            new Handler().postDelayed(this::moveToDashboard,500);
        });
        videoView.setOnErrorListener((mp, what, extra) -> {
            Log.e("PlayVideo", "An Error Occurred While Playing Video");
            return false;
        });
        videoView.setOnPreparedListener(mp -> {
            videoView.setVisibility(View.VISIBLE);
            videoView.setBackgroundColor(Color.TRANSPARENT);
            videoView.start();
        });
    }

    private void moveToDashboard(){
        Intent i = new Intent(TakeTourActivity.this, DashboardActivity.class).putExtra("isNewUser", false);
        startActivity(i);
        finish();
    }
}