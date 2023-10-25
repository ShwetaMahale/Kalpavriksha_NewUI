package com.mwbtech.dealer_register.Dashboard.Support;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.PojoClass.TermsAndCondition;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TermsConditionActivity extends AppCompatActivity {

    Customer_Interface customer_interface;
    PrefManager prefManager;
    String Token;
    TextView webView;
    String descriptionUsingWebView,termsVersion;

    //TextView text1,text2,text3,text4,text5,text6,text7,text8,text9,text10,text11,text12,text13,text14,text15,text16,text17,text18,text19,text20,text21,text22,
           // text23,text24,text25,text26,text27;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        findViewById(R.id.back).setOnClickListener(view -> {
            startActivity(new Intent(TermsConditionActivity.this,DashboardActivity.class).putExtra("isNewUser", false));
            finish();
        });
        webView = (TextView) findViewById(R.id.webView);
        PrefManager prefManager = new PrefManager(this);
        Token = prefManager.getToken().get(TOKEN);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<TermsAndCondition> call1 = customer_interface.getAgreement("bearer " + Token);
        call1.enqueue(new Callback<TermsAndCondition>() {
            @Override
            public void onResponse(Call<TermsAndCondition> call, Response<TermsAndCondition> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        Log.e("status...",String.valueOf(statusCode));
                        TermsAndCondition termsAndCondition = response.body();
                        descriptionUsingWebView=termsAndCondition.getTAndC();
                        Log.e("conditionTerms..",termsAndCondition.getTAndC());
                        termsVersion=termsAndCondition.getTAndCVersion();
                        Log.e("version",termsVersion);
                        prefManager.SaveTandCversion(termsVersion);

                        // webView.loadDataWithBaseURL(null, descriptionUsingWebView, "text/html", "utf-8", null);
                        webView.setText(descriptionUsingWebView);
                        break;

                    case 404:
                        break;

                    case 500:
                        break;

                    case 401:
                        break;
                }
            }

            @Override
            public void onFailure(Call<TermsAndCondition> call, Throwable t) {
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                } else {
                }
            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class).putExtra("isNewUser", false);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        TermsConditionActivity.this.finish();
    }
}