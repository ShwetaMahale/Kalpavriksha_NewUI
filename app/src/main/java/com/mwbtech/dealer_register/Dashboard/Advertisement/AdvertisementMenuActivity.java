package com.mwbtech.dealer_register.Dashboard.Advertisement;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.mwbtech.dealer_register.Dashboard.Advertisement.MyAd.ShowAdsActivity;
import com.mwbtech.dealer_register.Dashboard.Advertisement.QuoteAd.AddRequestUserActivity;
import com.mwbtech.dealer_register.Dashboard.Advertisement.ReceiveAd.ReceivedAdvertisementActivity;
import com.mwbtech.dealer_register.Dashboard.Advertisement.SentAd.SentAdvertisementActivity;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.R;

public class AdvertisementMenuActivity extends AppCompatActivity implements View.OnClickListener {
    ListView listView;
    Toolbar toolbar;
    String[] maintitle = {"My Ads", "Book your Ads", "Sent Ads", "Received Ads"};
    ConstraintLayout cardViewMyAd, cardViewBookAd, cardViewSentAd, cardViewReceivedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_menu);
        initializeViews();
        initializeClickEvents();
        changeStatusBarColor();
      /*  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
    }

    private void initializeViews() {
        //setTitle("Advertise With Us");
        cardViewMyAd = findViewById(R.id.cardAd);
        cardViewBookAd = findViewById(R.id.cardBookAd);
        cardViewSentAd = findViewById(R.id.cardSentAd);
        cardViewReceivedAd = findViewById(R.id.cardReceivedAd);

       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
*/
    }

    private void initializeClickEvents() {
        cardViewMyAd.setOnClickListener(this);
        cardViewBookAd.setOnClickListener(this);
        cardViewSentAd.setOnClickListener(this);
        cardViewReceivedAd.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(view -> {
          onBackPressed();
            finish();
        });
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardAd:
                startActivity(new Intent(AdvertisementMenuActivity.this, ShowAdsActivity.class));
                AdvertisementMenuActivity.this.finish();
                break;

            case R.id.cardBookAd:
                startActivity(new Intent(AdvertisementMenuActivity.this, AddRequestUserActivity.class));
                AdvertisementMenuActivity.this.finish();
                break;

            case R.id.cardSentAd:
                startActivity(new Intent(AdvertisementMenuActivity.this, SentAdvertisementActivity.class));
                AdvertisementMenuActivity.this.finish();
                break;

            case R.id.cardReceivedAd:
                startActivity(new Intent(AdvertisementMenuActivity.this, ReceivedAdvertisementActivity.class));
                AdvertisementMenuActivity.this.finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class).putExtra("isNewUser", false);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        AdvertisementMenuActivity.this.finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}