package com.mwbtech.dealer_register.LoginRegister;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.PojoClass.TermsAndCondition;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsCond extends AppCompatActivity {
    TextView webView;
    String Token;
    Button btAgree;
    ImageView navBack;
    PrefManager prefManager;
    Customer_Interface customer_interface;
    String descriptionUsingWebView,termsVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_cond);
        prefManager = new PrefManager(this);
        webView = (TextView) findViewById(R.id.webView);
        btAgree=findViewById(R.id.btnagree);
        navBack=findViewById(R.id.back);
        btAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        navBack.setOnClickListener(view -> {
            finish();
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Token = bundle.getString("token");
        }
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(TermsCond.this,"Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
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
                        progressDialog.dismiss();
                        termsVersion=termsAndCondition.getTAndCVersion();
                        prefManager.SaveTandCversion(termsVersion);
                        webView.setText(descriptionUsingWebView);
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        break;
                    default:
                        progressDialog.dismiss();
                        break;
                }
            }

            @Override
            public void onFailure(Call<TermsAndCondition> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                } else {
                }
            }
        });
    }

}