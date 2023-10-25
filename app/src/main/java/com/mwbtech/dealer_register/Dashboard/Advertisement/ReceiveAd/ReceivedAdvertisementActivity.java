package com.mwbtech.dealer_register.Dashboard.Advertisement.ReceiveAd;

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
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.ReceivedAdAdapter;
import com.mwbtech.dealer_register.Dashboard.Advertisement.AdRequestDetails.AdRequestDetailsActivity;
import com.mwbtech.dealer_register.Dashboard.Advertisement.AdvertisementMenuActivity;
import com.mwbtech.dealer_register.PojoClass.AdDetailsModel;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceivedAdvertisementActivity extends AppCompatActivity implements ReceivedAdAdapter.AdClickEvent {

    RecyclerView recyclerView;
    EditText edSearch;
    Button Clear_txt_btn;
    LinearLayoutCompat constraintLayout;

    List<AdDetailsModel> adDetailsModels;
    List<AdDetailsModel> adDetailsModelsList = new ArrayList<>();
    TextView Txt_NotFound;
    int CustID;

    String Token;
    PrefManager prefManager;

    Customer_Interface customer_interface;
    ReceivedAdAdapter receivedAdAdapter;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }
    /*    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
    }

    private void showContent() {
        setContentView(R.layout.activity_received_advertisement);
        initializeViews();
        initializeSharedData();
        initializeRecyclerView();
        initializeClickEvent();
        initializeSearchTextWatcher();
        getUserReceivedAdvertisement();
    }

    private void showNoSignalContent() {
        setContentView(R.layout.no_signal_layout);

        Button tryButton = findViewById(R.id.tryBtn);
      /*  ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
      */
        tryButton.setOnClickListener(new View.OnClickListener() {
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
        constraintLayout = findViewById(R.id.parentLayout);
        edSearch = findViewById(R.id.searchAd);
        Clear_txt_btn = findViewById(R.id.clear_txt_Prise);
        Txt_NotFound = findViewById(R.id.not_found_txt);
        back = findViewById(R.id.back);
      /*  ActionBar actionBar = getSupportActionBar();
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
        recyclerView.setAdapter(receivedAdAdapter);
    }


    private void initializeClickEvent() {
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
        back.setOnClickListener(view -> onBackPressed());
    }


    private void initializeSearchTextWatcher() {
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                try {
                    receivedAdAdapter.getFilter().filter(s);
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
                    receivedAdAdapter.getFilter().filter(s);
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

    private void getUserReceivedAdvertisement() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(ReceivedAdvertisementActivity.this,"Please wait");
        progressDialog.setMessage("Loading...."); // Setting Message
        // progressDialog.setTitle("Loading details"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<AdDetailsModel>> call = customer_interface.GetReceivedAds("bearer " + Token, CustID);
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
                                Log.e("addetails...",adDetailsModel.toString());
                                if (adDetailsModels.get(i).getAdvertisementType().equals("TextAd")) {
                                    adDetailsModelsList.add(new AdDetailsModel(AdDetailsModel.TEXT_TYPE,
                                            adDetailsModel.getProductName(),
                                            adDetailsModel.getAdvertisementMainID(), adDetailsModel.getAdvertisementName(), adDetailsModel.getAdvertisementType(),
                                            adDetailsModel.getToDateStr(), adDetailsModel.getAdImageURL(), adDetailsModel.getAdText(),adDetailsModel.getCustomerDetails(),adDetailsModel.getProductID()));
                                    Log.e("type....",String.valueOf(AdDetailsModel.TEXT_TYPE));

                                }
                                else {
                                    adDetailsModelsList.add(new AdDetailsModel(AdDetailsModel.IMAGE_TYPE,
                                            adDetailsModel.getProductName(),
                                            adDetailsModel.getAdvertisementMainID(), adDetailsModel.getAdvertisementName(), adDetailsModel.getAdvertisementType(),
                                            adDetailsModel.getToDateStr(), adDetailsModel.getAdImageURL(), adDetailsModel.getAdText(),adDetailsModel.getCustomerDetails(),adDetailsModel.getProductID()));
                                }
                            }
                            recyclerView.setVisibility(View.VISIBLE);
                            Gson gson=new Gson();
                            String json=gson.toJson(adDetailsModelsList);
                            Log.e("inTextad..",json);
                            receivedAdAdapter = new ReceivedAdAdapter(ReceivedAdvertisementActivity.this, adDetailsModelsList, ReceivedAdvertisementActivity.this);
                            recyclerView.setAdapter(receivedAdAdapter);
                            receivedAdAdapter.notifyDataSetChanged();
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
                        CustomToast.showToast(ReceivedAdvertisementActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(ReceivedAdvertisementActivity.this);
                        break;
                }

            }

            @Override
            public void onFailure(Call<List<AdDetailsModel>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(ReceivedAdvertisementActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(ReceivedAdvertisementActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    @Override
    public void adclick(AdDetailsModel adDetailsModel) {
        //Intent intent = new Intent(ReceivedAdvertisementActivity.this, AdSendRequirementActivity.class);
        Intent intent = new Intent(ReceivedAdvertisementActivity.this, AdRequestDetailsActivity.class);
        intent.putExtra("object", (Serializable) adDetailsModel);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AdvertisementMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        ReceivedAdvertisementActivity.this.finish();
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