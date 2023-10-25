package com.mwbtech.dealer_register.Dashboard.Advertisement.SentAd;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.SentAdAdapter;
import com.mwbtech.dealer_register.Dashboard.Advertisement.AdvertisementMenuActivity;
import com.mwbtech.dealer_register.PojoClass.AdDetailsModel;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SentAdvertisementActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText edSearch;
    Button Clear_txt_btn;
    ImageView imgBack;

    List<AdDetailsModel> adDetailsModels;
    List<AdDetailsModel> adDetailsModelsList = new ArrayList<>();
    TextView Txt_NotFound;
    int CustID;
    String Token;
    PrefManager prefManager;

    Customer_Interface customer_interface;
    SentAdAdapter sentAdAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
    }

    private void showContent() {
        setContentView(R.layout.activity_sent_advertisement);
        findViewById(R.id.back).setOnClickListener(view -> onBackPressed());

        initializeViews();
        initializeSharedData();
        initializeRecyclerView();
        initializeClickEvent();
        initializeSearchTextWatcher();
        getUserSentAdvertisement();
    }

    private void showNoSignalContent() {
        setContentView(R.layout.no_signal_layout);

        Button tryButton = findViewById(R.id.tryBtn);
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
       */ tryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInternetConnected();
            }
        });
    }

    private void checkInternetConnected() {
        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }
    }

    private void initializeViews() {
        edSearch = findViewById(R.id.searchAd);
        Clear_txt_btn = findViewById(R.id.clear_txt_Prise);
        Txt_NotFound = findViewById(R.id.not_found_txt);
        imgBack = findViewById(R.id.back);

       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
*/
    }


    private void initializeSharedData() {
        prefManager = new PrefManager(this);
        CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        Token = prefManager.getToken().get(TOKEN);
    }


    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewAd);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initializeClickEvent() {
        imgBack.setOnClickListener((v) -> {
            onBackPressed();
            finish();
        });
        Clear_txt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edSearch.getText().toString().isEmpty()) {
                    edSearch.getText().clear();
                    Clear_txt_btn.setVisibility(View.GONE);
                    //hide keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                }
            }
        });
    }


    private void initializeSearchTextWatcher() {
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                try {
                    sentAdAdapter.getFilter().filter(s);
                    recyclerView.getRecycledViewPool().clear();
                    if (s.length() <= 0) {
                        Clear_txt_btn.setVisibility(View.GONE);
                    } else {
                        Clear_txt_btn.setVisibility(View.VISIBLE);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    sentAdAdapter.getFilter().filter(s);
                    recyclerView.getRecycledViewPool().clear();
                    if (s.length() <= 0) {
                        Clear_txt_btn.setVisibility(View.GONE);

                    } else {
                        Clear_txt_btn.setVisibility(View.VISIBLE);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getUserSentAdvertisement() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(SentAdvertisementActivity.this,"Please wait");
        progressDialog.setMessage("Loading...."); // Setting Message
        // progressDialog.setTitle("Loading details"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<AdDetailsModel>> call  = customer_interface.GetSentAds("bearer " + Token,CustID);
        call.enqueue(new Callback<List<AdDetailsModel>>() {
            @Override
            public void onResponse(Call<List<AdDetailsModel>> call, Response<List<AdDetailsModel>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        adDetailsModels = new ArrayList<>(response.body());

                        if (adDetailsModels.size() != 0) {
                            for (int i = 0; i < adDetailsModels.size(); i++) {
                                AdDetailsModel adDetailsModel = adDetailsModels.get(i);
                                if (adDetailsModels.get(i).getAdvertisementType().equals("TextAd")) {

                                    adDetailsModelsList.add(new AdDetailsModel(AdDetailsModel.TEXT_TYPE,
                                            adDetailsModel.getProductName(),
                                            adDetailsModel.getAdvertisementMainID(), adDetailsModel.getAdvertisementName(), adDetailsModel.getAdvertisementType(),
                                            adDetailsModel.getToDateStr(), adDetailsModel.getAdImageURL(), adDetailsModel.getAdText()));

                                } else {

                                    adDetailsModelsList.add(new AdDetailsModel(AdDetailsModel.IMAGE_TYPE,
                                            adDetailsModel.getProductName(),
                                            adDetailsModel.getAdvertisementMainID(), adDetailsModel.getAdvertisementName(), adDetailsModel.getAdvertisementType(),
                                            adDetailsModel.getToDateStr(), adDetailsModel.getAdImageURL(), adDetailsModel.getAdText()));
                                }
                            }
                            recyclerView.setVisibility(View.VISIBLE);
                            sentAdAdapter = new SentAdAdapter(SentAdvertisementActivity.this,adDetailsModelsList);
                            Gson gson=new Gson();
                            String json=gson.toJson(adDetailsModelsList);
                            Log.e("inTextad..",json);
                            recyclerView.setAdapter(sentAdAdapter);
                            sentAdAdapter.notifyDataSetChanged();
                            Txt_NotFound.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            Txt_NotFound.setVisibility(View.VISIBLE);

                        }
                        break;

                    case 404:
                        progressDialog.dismiss();
                        recyclerView.setVisibility(View.GONE);
                        Txt_NotFound.setVisibility(View.VISIBLE);

                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(SentAdvertisementActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(SentAdvertisementActivity.this);
                        break;
                }

            }

            @Override
            public void onFailure(Call<List<AdDetailsModel>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(SentAdvertisementActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(SentAdvertisementActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AdvertisementMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        SentAdvertisementActivity.this.finish();
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