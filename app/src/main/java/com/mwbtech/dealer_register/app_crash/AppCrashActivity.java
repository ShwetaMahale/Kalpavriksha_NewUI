package com.mwbtech.dealer_register.app_crash;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mwbtech.dealer_register.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;

public class AppCrashActivity extends AppCompatActivity {

    TextView tvDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_crash);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
        String dateTime = simpleDateFormat.format(calendar.getTime());
        TextView errorDetailsText = findViewById(R.id.error_details);
        tvDate = findViewById(R.id.date);
        tvDate.setTextColor(Color.RED);
        tvDate.setTextSize(25);
        tvDate.setText("Error Log : " + dateTime);
        errorDetailsText.setText(CustomActivityOnCrash.getStackTraceFromIntent(getIntent()));
        generateNoteOnSD(this,"JBN_Log",CustomActivityOnCrash.getStackTraceFromIntent(getIntent()));
        Button restartButton = findViewById(R.id.restart_button);
        final CaocConfig config = CustomActivityOnCrash.getConfigFromIntent(getIntent());
        if (config == null) {
            //This should never happen - Just finish the activity to avoid a recursive crash.
            finish();
            return;
        }
        if (config.isShowRestartButton() && config.getRestartActivityClass() != null) {
            restartButton.setText(R.string.restart_app);
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.restartApplication(AppCrashActivity.this, config);
                }
            });
        } else {
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomActivityOnCrash.closeApplication(AppCrashActivity.this, config);
                }
            });
        }
    }
    public void generateNoteOnSD(Context context, String sFileName, String creation) {
        File file = new File(Environment.getExternalStorageDirectory(), sFileName);
        BufferedWriter bufferedWriter;
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            File gpxfile = new File(file, "DealerRegister.txt");
            bufferedWriter = new BufferedWriter(new FileWriter(gpxfile,true));
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String dateTime = simpleDateFormat.format(calendar.getTime());
            bufferedWriter.append(""+tvDate.getText().toString()+"\n");
            bufferedWriter.append("Error : " + creation + ", Date : " + dateTime );
            bufferedWriter.append("\n");
            bufferedWriter.flush();
            bufferedWriter.close();
            //Toast.makeText(HomeActivity.this, "Saved your text", Toast.LENGTH_LONG).show();
        } catch (Exception e) { }
    }

}
