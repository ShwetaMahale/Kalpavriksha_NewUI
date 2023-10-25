package com.mwbtech.dealer_register.Dashboard.Advertisement.MyAd;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;
import static com.mwbtech.dealer_register.Utils.Utility.getScreenHeight;
import static com.mwbtech.dealer_register.Utils.Utility.getScreenWidth;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Dashboard.Advertisement.Payment;
import com.mwbtech.dealer_register.PojoClass.AdvertisementSlotModel;
import com.mwbtech.dealer_register.PojoClass.AvailableSlotModel;
import com.mwbtech.dealer_register.PojoClass.City;
import com.mwbtech.dealer_register.PojoClass.District;
import com.mwbtech.dealer_register.PojoClass.Mailmodule;
import com.mwbtech.dealer_register.PojoClass.NewAdvertisementModule;
import com.mwbtech.dealer_register.PojoClass.PostPayment;
import com.mwbtech.dealer_register.PojoClass.State;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.FileCompressor;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdDetailsActivity extends AppCompatActivity {
    TextView tvAdProduct, tvAdName, tvAdType, tvAdArea, tvSelectedStates, tvSelectedDistrict,
            tvSelectedCity, tvAdCreated, tvAdExpire, tvAdApproval, tvAdPayment, tvSelectedSlot,
            tvAdStartDate, tvAdEndDate, tvAdText,tvAmount,tvAdRemark,dueDate,tvtranID;
    ImageView ivAdImage;
    AppCompatImageView imgBack;
    LinearLayout llParentLayout, llStateLayout, llDistrictLayout, llCityLayout, llImageLayout, llExpireLayout, llTextAd,llRemarks,llChangeImage,dueLayout,transLayout,llChangeText,lltextField;
    int adMainId;
    String adName,emailID,mobileNumber,customerName,advertisementType,textAd;
    int height, width;
    String Token;
    String imageUrl;
    Button cancel;
    Button make_payment;
    Button submit;
    AlertDialog.Builder builder,builder1;
    Customer_Interface customer_interface;
    PrefManager prefManager;
    //payment
    Double taxAmount,adAmount,totalDiscount,totalAmount;
    EditText edText;
    //image
    static final int REQUEST_GALLERY_PHOTO = 2;
    static final long image_size = 2097152;
    File mPhotoFile1;
    FileCompressor mCompressor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void showContent() {
        setContentView(R.layout.activity_ad_details);
        initializeViews();
        initializeSharedData();
        initializeClickEvent();
        initializeScreenHeightWidth();
    }

    private void showNoSignalContent() {
        setContentView(R.layout.no_signal_layout);

        Button tryButton = findViewById(R.id.tryBtn);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
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
        llParentLayout = findViewById(R.id.parent);
        tvAdProduct = findViewById(R.id.adProductTxt);
        imgBack = findViewById(R.id.back);
        tvAdName = findViewById(R.id.adNameTxt);
        tvAdType = findViewById(R.id.adTypeTxt);
        tvAdArea = findViewById(R.id.adAreaTxt);
        tvSelectedStates = findViewById(R.id.adStateTxt);
        tvSelectedDistrict = findViewById(R.id.adDistrictTxt);
        tvSelectedCity = findViewById(R.id.adCityTxt);
        tvAdCreated = findViewById(R.id.adCreatedTxt);
        tvAdExpire = findViewById(R.id.adExpireTxt);
        tvAdApproval = findViewById(R.id.adApprovalTxt);
        tvAdStartDate = findViewById(R.id.adStartTxt);
        tvAdEndDate = findViewById(R.id.adEndTxt);
        tvAdPayment = findViewById(R.id.adPaymentTxt);
        tvAdRemark=findViewById(R.id.adRemarks);
        tvAdText = findViewById(R.id.tvTextScrollAd);
        ivAdImage = findViewById(R.id.ivImage);
        tvSelectedSlot = findViewById(R.id.adSlotTxt);
        llStateLayout = findViewById(R.id.llSelState);
        llDistrictLayout = findViewById(R.id.llSelDistrict);
        llCityLayout = findViewById(R.id.llSelCity);
        llImageLayout = findViewById(R.id.llImage);
        llExpireLayout = findViewById(R.id.llExpire);
        llTextAd = findViewById(R.id.llText);
        lltextField=findViewById(R.id.llTxtLayout);
        edText=findViewById(R.id.text);
        llChangeImage=findViewById(R.id.changeImage);
        llChangeText=findViewById(R.id.changeText);
        dueLayout=findViewById(R.id.duelayout);
        transLayout=findViewById(R.id.transcationID);
        make_payment=findViewById(R.id.bt_makePayment);
        submit=findViewById(R.id.bt_submit);
        cancel=findViewById(R.id.bt_delete);
        dueDate=findViewById(R.id.duedate);
        tvtranID=findViewById(R.id.tranID);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(advertisementType.equals("TextAd")){
                    if (edText.getText().toString().isEmpty()) {
                        Toast.makeText(AdDetailsActivity.this, "Please upload the text", Toast.LENGTH_SHORT).show();
                    }else {
                        sendTextToServer(edText.getText().toString());
                    }
                }else{
                    if (ivAdImage==null) {
                        Toast.makeText(getApplicationContext(), "Please upload the Image", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        sendImageToServer();
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(AdDetailsActivity.this);
                builder.setMessage("Do you want to cancel the Ad?");
                builder.setTitle("");
                builder.setCancelable(false);
                builder
                        .setPositiveButton(
                                "No",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {
                                        dialog.dismiss();
                               /* startActivity(new Intent(UploadImageForAdActivity.this, EstimationPriceActivity.class)
                                        .putExtra("data", (Serializable) availableSlotModel));
                                UploadImageForAdActivity.this.finish();*/
                                    }
                                })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAd();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        tvAmount=findViewById(R.id.adAmount);
        make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdDetailsActivity.this, Payment.class)
                        .putExtra("advertisemntName",adName)
                        .putExtra("adMainId",adMainId)
                        .putExtra("customerName",customerName)
                        .putExtra("Emailid",emailID)
                        .putExtra("mobileNumber",mobileNumber)
                        .putExtra("AdTotal",adAmount)
                        .putExtra("DisTotal",totalDiscount)
                        .putExtra("TaxTotal",taxAmount)
                        .putExtra("FinalTotal",totalAmount)
                        .putExtra("imgUrl",imageUrl)
                        .putExtra("adtext",textAd));
            }
        });
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
    }


    private void initializeSharedData() {
        prefManager = new PrefManager(this);
        Token = prefManager.getToken().get(TOKEN);
        adMainId = getIntent().getExtras().getInt("adMainId");
        adName=getIntent().getExtras().getString("advertisemntName");
        emailID=getIntent().getExtras().getString("Emailid");
        mobileNumber=getIntent().getExtras().getString("mobileNumber");
        customerName=getIntent().getExtras().getString("customerName");
        getAdDetails(adMainId);
    }

    private void initializeClickEvent() {
        imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
        ivAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(imageUrl)){
                    showFullImage();
                }
                else{
                    selectImage();
                }
            }
        });
        llChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                llImageLayout.setVisibility(View.VISIBLE);
            }
        });
        llChangeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lltextField.setVisibility(View.VISIBLE);
                llImageLayout.setVisibility(View.GONE);
            }
        });
    }
    private void initializeScreenHeightWidth() {
        height = getScreenHeight(AdDetailsActivity.this);
        width = getScreenWidth(AdDetailsActivity.this);
    }

    private void getAdDetails(int adMainId) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AdDetailsActivity.this,"Please wait");
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        //customer_interface = Advertisement_Client.getAdClient().create(Customer_Interface.class);
        Log.e("mainid...",String.valueOf(adMainId));
        Call<AvailableSlotModel> call = customer_interface.GetAdDetails("bearer " + Token, adMainId);
        call.enqueue(new Callback<AvailableSlotModel>() {
            @Override
            public void onResponse(Call<AvailableSlotModel> call, Response<AvailableSlotModel> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        AvailableSlotModel slotModelResponse = response.body();
                        taxAmount=slotModelResponse.getTaxAmount();
                        assert slotModelResponse != null;
                        setValues(slotModelResponse);
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(AdDetailsActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AdDetailsActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<AvailableSlotModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(AdDetailsActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AdDetailsActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }
    private void sendTextToServer(String message) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AdDetailsActivity.this,"Please wait");
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        AvailableSlotModel slotModel = new AvailableSlotModel(adMainId,message);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Gson gson=new Gson();
        String json=gson.toJson(slotModel);
        Log.e("model....",json);
        Call<AvailableSlotModel> call = customer_interface.SaveText("bearer " + Token,slotModel);
        call.enqueue(new Callback<AvailableSlotModel>() {
            @Override
            public void onResponse(Call<AvailableSlotModel> call, Response<AvailableSlotModel> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        CustomToast.showToast(AdDetailsActivity.this, "Advertisement Submited Successfully");
                        navigateToDashboard();
                        break;
                    case 404:
                        progressDialog.dismiss();
                        break;
                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(AdDetailsActivity.this, "Server Error");
                        break;
                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AdDetailsActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<AvailableSlotModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(AdDetailsActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AdDetailsActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });

    }
    private void deleteAd() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        // customer_interface = Advertisement_Client.getAdClient().create(Customer_Interface.class);
        Call<Mailmodule> call = customer_interface.DeleteAdvertisement("bearer " + Token, adMainId);
        call.enqueue(new Callback<Mailmodule>() {
            @Override
            public void onResponse(Call<Mailmodule> call, Response<Mailmodule> response) {
                int statusCode=response.code();
                switch (statusCode) {
                    case 200:
                        Toast.makeText(AdDetailsActivity.this, "Advertisement Deleted Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AdDetailsActivity.this, ShowAdsActivity.class));
                        break;
                    case 404:
                        Toast.makeText(AdDetailsActivity.this, "Not found", Toast.LENGTH_LONG).show();
                        break;

                    case 500:
                        CustomToast.showToast(AdDetailsActivity.this, "Server Error");
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(AdDetailsActivity.this);
                        break;
                }
            }
            @Override
            public void onFailure(Call<Mailmodule > call, Throwable t) {

            }

        });

    }

   /* private void sendImageToServer() {
        MultipartBody.Part body = null;
        try {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile1);
            body = MultipartBody.Part.createFormData("picture", mPhotoFile1.getName(), requestFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AdDetailsActivity.this,"Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        AvailableSlotModel slotModel = new AvailableSlotModel(adMainId);
        try{
            customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
            Call<AvailableSlotModel> call = customer_interface.upLoadAdImage("bearer " + Token, slotModel, body);
            call.enqueue(new Callback<AvailableSlotModel>() {
                @Override
                public void onResponse(Call<AvailableSlotModel> call, Response<AvailableSlotModel> response) {
                    int statusCode = response.code();
                    switch (statusCode) {
                        case 200:
                            progressDialog.dismiss();
                            mPhotoFile1=null;
                            CustomToast.showToast(AdDetailsActivity.this, "Advertisement Submited Successfully");
                            navigateToDashboard();
                            break;
                        case 401:
                            progressDialog.dismiss();
                            SessionDialog.CallSessionTimeOutDialog(AdDetailsActivity.this);
                            break;
                    }
                }

                @Override
                public void onFailure(Call<AvailableSlotModel> call, Throwable t) {
                    progressDialog.dismiss();
                    if (t instanceof IOException) {
                        Toast.makeText(AdDetailsActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                    } else {
                        CustomToast.showToast(AdDetailsActivity.this, "Bad response from server.. Try again later ");
                    }
                }
            });
        }
        catch (Exception e){
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Please change the image",Toast.LENGTH_LONG).show();
        }
    }*/
    private void sendImageToServer() {
        MultipartBody.Part body = null;
        RequestBody requestBody_custId;
        if(mPhotoFile1!=null){
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile1);
            body = MultipartBody.Part.createFormData("file", mPhotoFile1.getName(), requestFile);
            ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AdDetailsActivity.this,"Please wait");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
            AvailableSlotModel slotModel = new AvailableSlotModel(adMainId);
            requestBody_custId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(adMainId));
            try{
                customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
                Call<NewAdvertisementModule> call = customer_interface.upLoadAd("bearer " + Token, body,requestBody_custId);
                call.enqueue(new Callback<NewAdvertisementModule>() {
                    @Override
                    public void onResponse(Call<NewAdvertisementModule> call, Response<NewAdvertisementModule> response) {
                        int statusCode = response.code();
                        switch (statusCode) {
                            case 200:
                                progressDialog.dismiss();
                                mPhotoFile1=null;
                                CustomToast.showToast(AdDetailsActivity.this, "Advertisement Submited Successfully");
                                navigateToDashboard();
                                break;
                            case 401:
                                progressDialog.dismiss();
                                SessionDialog.CallSessionTimeOutDialog(AdDetailsActivity.this);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<NewAdvertisementModule> call, Throwable t) {
                        progressDialog.dismiss();
                        if (t instanceof IOException) {
                            Toast.makeText(AdDetailsActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                        } else {
                            CustomToast.showToast(AdDetailsActivity.this, "Bad response from server.. Try again later ");
                        }
                    }
                });
            }
            catch (Exception e){
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Please Upload the image",Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Please Upload the image",Toast.LENGTH_LONG).show();
        }

    }
    private void setValues(AvailableSlotModel slotModelResponse) {
        List<State> states = new ArrayList<>();
        List<District> districts = new ArrayList<>();
        List<City> cities = new ArrayList<>();
        List<AdvertisementSlotModel> slotModels = new ArrayList<>();
        List<PostPayment> paymentSlots = new ArrayList<>();
        String adType = slotModelResponse.getAdvertisementType();
        advertisementType=adType;
        String adText = slotModelResponse.getAdText();
        textAd=adText;
        if (adType.equals("TextAd")) {
            llTextAd.setVisibility(View.VISIBLE);
            llImageLayout.setVisibility(View.GONE);

        } else {
            llImageLayout.setVisibility(View.VISIBLE);
            llTextAd.setVisibility(View.GONE);
        }
        tvAdProduct.setText("" + slotModelResponse.getProductName());
        tvAdName.setText("" + slotModelResponse.getAdvertisementName());
        tvAdType.setText("" + slotModelResponse.getAdvertisementType());

        tvAdArea.setText("" + slotModelResponse.getAdvertisementArea());
        tvAdCreated.setText("" + slotModelResponse.getCreatedDateStr());
        tvAdExpire.setText("" + slotModelResponse.getBookingExpiryDateStr());
        tvAdApproval.setText("" + slotModelResponse.getApprovalStatus());
        tvAdPayment.setText("" + slotModelResponse.getPaymentStatus());
        tvAdRemark.setText("" + slotModelResponse.getRemarks());
        tvAmount.setText(""+slotModelResponse.getFinalPrice());
        dueDate.setText(""+slotModelResponse.getPaymentDueDate());
        adAmount=slotModelResponse.getTotalPrice();
        totalDiscount=slotModelResponse.getTotalDiscountAmount();
        totalAmount=slotModelResponse.getFinalPrice();
        states = slotModelResponse.getStates();
        districts = slotModelResponse.getDistricts();
        cities = slotModelResponse.getCities();
        slotModels = slotModelResponse.getSlotModel();
        paymentSlots=slotModelResponse.getPaymentDetails();
        imageUrl = slotModelResponse.getAdImageURL();
        String startDate = slotModelResponse.getFromDateStr();
        String endDate = slotModelResponse.getToDateStr();
        tvSelectedSlot.setText("" + slotModels.toString().replace("[", "").replace("]", ""));
        tvAdStartDate.setText("" + startDate);
        tvAdEndDate.setText("" + endDate);
        if (slotModelResponse.getAdvertisementArea().equals("National Level")) {
            llStateLayout.setVisibility(View.GONE);
            llDistrictLayout.setVisibility(View.GONE);
            llCityLayout.setVisibility(View.GONE);
        } else if (slotModelResponse.getAdvertisementArea().equals("State Level")) {
            llStateLayout.setVisibility(View.VISIBLE);
            llDistrictLayout.setVisibility(View.GONE);
            llCityLayout.setVisibility(View.GONE);
            tvSelectedStates.setText("" + states.toString().replace("[", "").replace("]", ""));

        } else if (slotModelResponse.getAdvertisementArea().equals("District Level")) {
            llStateLayout.setVisibility(View.VISIBLE);
            llDistrictLayout.setVisibility(View.VISIBLE);
            llCityLayout.setVisibility(View.GONE);
            tvSelectedDistrict.setText("" + districts.toString().replace("[", "").replace("]", ""));
            tvSelectedStates.setText("" + states.toString().replace("[", "").replace("]", ""));
        } else if (slotModelResponse.getAdvertisementArea().equals("Tier 1 Cities")) {
            llStateLayout.setVisibility(View.GONE);
            llDistrictLayout.setVisibility(View.GONE);
            llCityLayout.setVisibility(View.VISIBLE);
            tvSelectedStates.setText("" + states.toString().replace("[", "").replace("]", ""));
            tvSelectedCity.setText("" + cities.toString().replace("[", "").replace("]", ""));
        }
        else if (slotModelResponse.getAdvertisementArea().equals("Other Cities")) {
            llStateLayout.setVisibility(View.VISIBLE);
            llDistrictLayout.setVisibility(View.GONE);
            llCityLayout.setVisibility(View.VISIBLE);
            tvSelectedStates.setText("" + states.toString().replace("[", "").replace("]", ""));
            tvSelectedCity.setText("" + cities.toString().replace("[", "").replace("]", ""));
        }

        if (TextUtils.isEmpty(imageUrl)) {
            llImageLayout.setVisibility(View.VISIBLE);
            Glide.with(AdDetailsActivity.this)
                    .load(R.drawable.plus)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivAdImage);
        } else {
            llImageLayout.setVisibility(View.VISIBLE);
            Glide.with(AdDetailsActivity.this)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivAdImage);
        }
        if (!TextUtils.isEmpty(adText)) {
            tvAdText.setText("" + adText);
            tvAdText.setSelected(true);
            llImageLayout.setVisibility(View.GONE);
        }

        if (slotModelResponse.getPaymentStatus().equals("Approved")) {
            llExpireLayout.setVisibility(View.GONE);
        } else {
            llExpireLayout.setVisibility(View.VISIBLE);
        }

        if(slotModelResponse.getApprovalStatus().equals("Approved")&&slotModelResponse.isMakePaymentAllowed()==true){
                make_payment.setVisibility(View.VISIBLE);
                dueLayout.setVisibility(View.VISIBLE);

        }
        if(slotModelResponse.getApprovalStatus().equals("Approved")&&slotModelResponse.isMakePaymentAllowed()==false){
                make_payment.setVisibility(View.GONE);
                dueLayout.setVisibility(View.GONE);
        }
        if(slotModelResponse.getPaymentStatus().equals("Approved")){
            make_payment.setVisibility(View.GONE);
            dueLayout.setVisibility(View.GONE);
            transLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < paymentSlots.size(); i++){
                tvtranID.setText(""+paymentSlots.get(i).getTxnReferenceID());
            }

        }
        if(slotModelResponse.getApprovalStatus().equals("Pending")){
            if(TextUtils.isEmpty(slotModelResponse.getAdImageURL())&& slotModelResponse.getAdvertisementType().equals("BannerAd")){
                llChangeImage.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
            }
            else if(TextUtils.isEmpty(slotModelResponse.getAdImageURL())&& slotModelResponse.getAdvertisementType().equals("FullPageAd")){
                llChangeImage.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
            }
            else if(TextUtils.isEmpty(slotModelResponse.getAdText())&& slotModelResponse.getAdvertisementType().equals("TextAd")){
                llChangeText.setVisibility(View.VISIBLE);
                llImageLayout.setVisibility(View.GONE);
                llChangeImage.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
            }
            else{
                cancel.setVisibility(View.VISIBLE);
            }
           // cancel.setVisibility(View.VISIBLE);
        }
        if(slotModelResponse.getApprovalStatus().equals("Rejected")){
            if(slotModelResponse.getAdvertisementType().equals("TextAd")){
                llChangeText.setVisibility(View.VISIBLE);
                llImageLayout.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
            }
            else{
                llChangeImage.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                llImageLayout.setVisibility(View.VISIBLE);
            }

        }

    }
    private void showError() {
        Snackbar snackbar = Snackbar
                .make(llParentLayout, "You are not connected to Internet.!", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void navigateToDashboard() {
        startActivity(new Intent(AdDetailsActivity.this, ShowAdsActivity.class));
        AdDetailsActivity.this.finish();
    }
    public void clearApplicationData() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File appDir = new File(storageDir.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("files")) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AdDetailsActivity.this);
        builder.setTitle("Choose your picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                } else if (options[item].equals("Choose from Gallery")) {
                    dispatchGalleryIntent();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void showFullImage() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setting the height and width to dialog
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.view_image_ad_item);
        dialog.setCancelable(true);
        //set layout height and width to its screen size
        LinearLayout linearLayout = dialog.findViewById(R.id.parent);
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.height = height;
        params.width = width;
        linearLayout.setLayoutParams(params);

        ImageView imageView = (PhotoView) dialog.findViewById(R.id.photo_view);
        Glide.with(AdDetailsActivity.this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);


        ImageView ivClose = dialog.findViewById(R.id.closeIcon);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();

                if (!TextUtils.isEmpty(getRealPathFromUri(selectedImage))) {
                    mPhotoFile1 = new File(getRealPathFromUri(selectedImage));
                    long len_img = mPhotoFile1.length();
                    if (len_img < image_size) {
                        ivAdImage.setImageURI(selectedImage);
                    } else {
                        Toast.makeText(AdDetailsActivity.this, "Please select the image within 2MB", Toast.LENGTH_LONG);
                    }
                }
                else{
                    CustomToast.showToast(getApplicationContext(), "Please select the image from Gallery");
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        }
                    }, 5000);
                }
            }
        }
    }
    /**
     * Get real file path from URI
     */
    public String getRealPathFromUri(Uri contentUri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ShowAdsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        AdDetailsActivity.this.finish();
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