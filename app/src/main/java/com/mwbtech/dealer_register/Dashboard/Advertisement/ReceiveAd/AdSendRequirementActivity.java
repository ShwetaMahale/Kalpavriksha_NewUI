package com.mwbtech.dealer_register.Dashboard.Advertisement.ReceiveAd;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;
import static com.mwbtech.dealer_register.Utils.Utility.getScreenHeight;
import static com.mwbtech.dealer_register.Utils.Utility.getScreenWidth;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.BusinessDemandAdaper;
import com.mwbtech.dealer_register.Adapter.BusinessTypeAdapter;
import com.mwbtech.dealer_register.Adapter.CityAdapter;
import com.mwbtech.dealer_register.Adapter.ProfessionalReqAdapter;
import com.mwbtech.dealer_register.Adapter.StateSelectorAdapter;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.PojoClass.AdDetailsModel;
import com.mwbtech.dealer_register.PojoClass.BusinessDemand;
import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.PojoClass.City;
import com.mwbtech.dealer_register.PojoClass.CustIDS;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.PojoClass.ProfessionalReqModel;
import com.mwbtech.dealer_register.PojoClass.State;
import com.mwbtech.dealer_register.PojoClass.SubmitQuery;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.FileCompressor;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.ProjectUtilsKt;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdSendRequirementActivity extends AppCompatActivity implements BusinessTypeAdapter.BusinessTypeClick, View.OnClickListener, CompoundButton.OnCheckedChangeListener, StateSelectorAdapter.StateAdapterListener, CityAdapter.CitySelectedListener,ProfessionalReqAdapter.ProfessionalTypeClick {

    //Image
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    File mPhotoFile1=null, mPhotoFile2=null;
    FileCompressor mCompressor;
    AlertDialog.Builder builder1;
    Dialog mdialogPhoto;
    Button btnPhoto, btnPhoto1;
    ImageView imgPhoto1, imgPhoto2;
    String fullScreenAdUrl,promoImage;
    Bundle bundle;
    public static ConnectivityReceiver connectivityReceiver;

    EditText edCustomerName, edProductName;
    EditText edCity, edRequirement, edDialogProduct, edSelectedCity, edSelectState, edDialogState;
    ListView listBusinessDemand, listProfessionalDemand, listViewBusinessType;
    CheckBox chReselling, chHome, chBusiness;
    Button btnSubmit;
    Dialog mDialogCitySelect, mDialogStateSelect;
    RecyclerView recyclerViewState, recyclerViewCity;
    LinearLayout llBusinessType;
    LinearLayout ProfessionalLayout, NeedLayout, linearLayoutDemand, llParentLayout,photoLayout;

    List<BusinessType> businessTypeList;
    List<BusinessType> selectedBusinessList = new ArrayList<>();
    List<State> stateList;
    List<City> cityList;
    List<BusinessDemand> businessDemandList;
    List<ProfessionalReqModel> professionalReqModels;
    List<CustIDS> custIDSList = new ArrayList<>();

    AdDetailsModel adDetailsModel=null;
    BusinessTypeAdapter businessTypeAdapter;
    BusinessDemandAdaper businessDemandAdaper;
    ProfessionalReqAdapter professionalReqAdapter;
    StateSelectorAdapter stateSelectorAdapter;
    CityAdapter cityAdapter;
    int key=0;
    Customer_Interface customer_interface;
    IntentFilter intentFilter;
    ImageView ivImage1,ivImage2;
    boolean isprofessional;
    String Token;
    List<BusinessType> BT_demo =new ArrayList<>();
    PrefManager prefManager;
    String customerName, productName, selectCheckBoxPurpose;
    int selectedProductId, selectedCustomerId;
    int CustID, StateID, CityId, imgCount, productReqID, DemandID;
    int height, width;
    DealerRegister dealerRegisterforad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void showContent() {
        setContentView(R.layout.activity_ad_send_requirement);

        initializeViews();
        initializeSharedData();
        initializeScreenHeightWidth();
        initializeBusinessTypes();
        GetBusinessDemand();
        GetProfessionalRequirements();
        setValues();
        initializeClickEvents();
    }

    private void showNoSignalContent() {
        setContentView(R.layout.no_signal_layout);

        Button tryButton = findViewById(R.id.tryBtn);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
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
        llParentLayout = findViewById(R.id.parentLayout);
        edCustomerName = findViewById(R.id.ed_selected_product);
        edProductName = findViewById(R.id.selected_Customer);
        edCity = findViewById(R.id.ed_select_city);
        edRequirement = findViewById(R.id.open_text);
        edSelectState = findViewById(R.id.select_state);
        photoLayout = findViewById(R.id.photoLayout);
        chReselling = findViewById(R.id.ckId5);
        chHome = findViewById(R.id.ckId6);
        chBusiness = findViewById(R.id.ckId4);
        ivImage1 = findViewById(R.id.imag1);
        listBusinessDemand = findViewById(R.id.listBusinesDemand);
        listProfessionalDemand = findViewById(R.id.listProfDemand);
        listViewBusinessType = findViewById(R.id.listBusines);

        llBusinessType = findViewById(R.id.linearBusiness);
        ProfessionalLayout = findViewById(R.id.linearBusinessProf);
        NeedLayout = findViewById(R.id.linearnedd);
        linearLayoutDemand = findViewById(R.id.linearBusinessDemand);
        btnSubmit = findViewById(R.id.bt_submit);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
    }

    private void initializeSharedData() {
        prefManager = new PrefManager(this);
        Token = prefManager.getToken().get(TOKEN);
        mCompressor = new FileCompressor(this);

        intentFilter = new IntentFilter();
        connectivityReceiver = new ConnectivityReceiver();
        CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        bundle = getIntent().getExtras();
        dealerRegisterforad = (DealerRegister) getIntent().getSerializableExtra("adUrl");
        if (dealerRegisterforad!=null) {
                businessTypeList = dealerRegisterforad.getBusinessTypeIDList();
                productName = dealerRegisterforad.getChildCategoryName();
                selectedProductId = dealerRegisterforad.getChildCategoryId();
                selectedCustomerId = dealerRegisterforad.getAdUserID();
                customerName = dealerRegisterforad.getAdFirmName();
                custIDSList.add(new CustIDS(selectedCustomerId, CityId));
        }
        else{
            adDetailsModel = (AdDetailsModel) getIntent().getSerializableExtra("object");
            businessTypeList = adDetailsModel.getBusinessTypes();
            productName = adDetailsModel.getProductName();
            selectedProductId = adDetailsModel.getProductID();
            selectedCustomerId = adDetailsModel.getCustID();
            customerName = adDetailsModel.getFirmName();
            custIDSList.add(new CustIDS(selectedCustomerId, CityId));
            photoLayout.setVisibility(View.GONE);
        }
    }

    private void initializeScreenHeightWidth() {
        height = getScreenHeight(AdSendRequirementActivity.this);
        width = getScreenWidth(AdSendRequirementActivity.this);
    }

    private void initializeBusinessTypes() {
        businessTypeAdapter = new BusinessTypeAdapter(AdSendRequirementActivity.this, businessTypeList, AdSendRequirementActivity.this);
        listViewBusinessType.setAdapter(businessTypeAdapter);
        if (businessTypeList.size() == 1) {
            ViewGroup.LayoutParams params = llBusinessType.getLayoutParams();
            params.height = 150;
            llBusinessType.setLayoutParams(params);

            linearLayoutDemand.setVisibility(View.GONE);
            NeedLayout.setVisibility(View.GONE);
            ProfessionalLayout.setVisibility(View.VISIBLE);
        } else {
            ViewGroup.LayoutParams params = llBusinessType.getLayoutParams();
            params.height = 520;
            llBusinessType.setLayoutParams(params);

            linearLayoutDemand.setVisibility(View.VISIBLE);
            NeedLayout.setVisibility(View.VISIBLE);
            ProfessionalLayout.setVisibility(View.GONE);
        }
    }

    private void GetBusinessDemand() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AdSendRequirementActivity.this,"Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<BusinessDemand>> call = customer_interface.GetBusinessDemand("bearer " + Token);
        call.enqueue(new Callback<List<BusinessDemand>>() {
            @Override
            public void onResponse(Call<List<BusinessDemand>> call, Response<List<BusinessDemand>> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        progressDialog.dismiss();
                        businessDemandList = response.body();
                        businessDemandAdaper = new BusinessDemandAdaper(AdSendRequirementActivity.this, businessDemandList);
                        listBusinessDemand.setAdapter(businessDemandAdaper);
                        businessDemandAdaper.notifyDataSetChanged();
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;


                    case 500:
                        CustomToast.showToast(AdSendRequirementActivity.this, "Server Error");
                        progressDialog.dismiss();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AdSendRequirementActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<BusinessDemand>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(AdSendRequirementActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AdSendRequirementActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void GetProfessionalRequirements() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AdSendRequirementActivity.this,"Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<ProfessionalReqModel>> call = customer_interface.GetProfessionalsReq("bearer " + Token);
        call.enqueue(new Callback<List<ProfessionalReqModel>>() {
            @Override
            public void onResponse(Call<List<ProfessionalReqModel>> call, Response<List<ProfessionalReqModel>> response) {
                int Status_code = response.code();
                switch (Status_code) {
                    case 200:
                        progressDialog.dismiss();
                        professionalReqModels = response.body();
                        professionalReqAdapter = new ProfessionalReqAdapter(AdSendRequirementActivity.this, professionalReqModels,AdSendRequirementActivity.this);
                        listProfessionalDemand.setAdapter(professionalReqAdapter);
                        professionalReqAdapter.notifyDataSetChanged();
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        CustomToast.showToast(AdSendRequirementActivity.this, "Server Error");
                        progressDialog.dismiss();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AdSendRequirementActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<ProfessionalReqModel>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(AdSendRequirementActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AdSendRequirementActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void initializeClickEvents() {
        edSelectState.setOnClickListener(this);
        edCity.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        chReselling.setOnCheckedChangeListener(this);
        chHome.setOnCheckedChangeListener(this);
        chBusiness.setOnCheckedChangeListener(this);
    }

    private void setValues() {
        edProductName.setText("" + productName);
        edCustomerName.setText("" + customerName);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_state:
                if (ConnectivityReceiver.isConnected()) {
                    getStateListFromServer();
                } else {
                    showError();
                }
                break;

            case R.id.ed_select_city:
                if (StateID == 0) {
                    CustomToast.showToast(AdSendRequirementActivity.this, "Please select state");
                } else {
                    if (ConnectivityReceiver.isConnected()) {
                        OpenDialogToSelectCity();
                    }else {
                        showError();
                    }
                }
                break;

            case R.id.bt_submit:
                selectedBusinessList.clear();
                if (!Validatestate() | !ValidateCity()) {
                    return;
                } else {
                    selectedBusinessList = businessTypeAdapter.getTrueResultList();
                    if (businessTypeAdapter.getTrueResultList().size() > 0 && !businessTypeAdapter.getResultList().isEmpty()) {
                        if (isprofessional) {
                            if (professionalReqAdapter.GetselectedProfBusinessDemand() != 0) {
                                productReqID = professionalReqAdapter.GetselectedProfBusinessDemand();
                                DemandID = 0;
                                selectCheckBoxPurpose = "";
                                String str = edRequirement.getText().toString().trim();
                                if (!str.startsWith(" ") && !str.isEmpty()) {
                                    takePermissionToSendPicture();
                                } else {
                                    CustomToast.showToast(AdSendRequirementActivity.this, "Brief your requirement");
                                }

                            } else {
                                CustomToast.showToast(AdSendRequirementActivity.this, "Please select Professional Business demand");
                            }
                        } else {
                            if (businessDemandAdaper.GetselectedBusinessDemand() != 0) {
                                DemandID = businessDemandAdaper.GetselectedBusinessDemand();
                                if (chReselling.isChecked() || chHome.isChecked() || chBusiness.isChecked()) {
                                    String str = edRequirement.getText().toString().trim();
                                    if (!str.startsWith(" ") && !str.isEmpty()) {
                                        takePermissionToSendPicture();
                                    } else {
                                        CustomToast.showToast(AdSendRequirementActivity.this, "Brief your requirement");
                                    }
                                } else {
                                    CustomToast.showToast(AdSendRequirementActivity.this, "Please select Business Purpose");
                                }
                            } else {
                                CustomToast.showToast(AdSendRequirementActivity.this, "Please select Business Demand");
                            }
                        }
                    } else {
                        CustomToast.showToast(AdSendRequirementActivity.this, "Please select Business Type");
                    }
                }
                break;

            case R.id.imag1:
                imgCount = 0;
                selectImage();
                break;

            case R.id.imag2:
                imgCount = 1;
                selectImage();
                break;

            case R.id.btnPhoto:
                if (mPhotoFile1 != null && mPhotoFile2 != null) {
                    if (ConnectivityReceiver.isConnected()) {
                        Log.e("custIDSList : ",custIDSList.toString());
                        SubmitQuery submitQuery = new SubmitQuery(0,
                                custIDSList, CustID,
                                CityId,
                                selectedProductId,
                                selectedBusinessList,
                                DemandID,
                                selectCheckBoxPurpose,
                                edRequirement.getText().toString(),
                                productReqID, true,"Advertisement Enquiry","Buy");
                        Log.e("submitQuery",submitQuery.toString());
                        SendEnquiryWithImageToServer(submitQuery);
                        mdialogPhoto.dismiss();
                    }else {
                        showError();
                    }
                } else {
                    CustomToast.showToast(AdSendRequirementActivity.this, "Please select image");
                }
                break;

            case R.id.btnPhoto1:
                mdialogPhoto.dismiss();
                break;
        }
    }

    private void showError() {
        Snackbar snackbar = Snackbar
                .make(llParentLayout, "You are not connected to Internet.!", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void takePermissionToSendPicture() {
        builder1 = new AlertDialog.Builder(AdSendRequirementActivity.this).setMessage(R.string.photo);
        builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mdialogPhoto = new Dialog(AdSendRequirementActivity.this);
                mdialogPhoto.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mdialogPhoto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mdialogPhoto.setContentView(R.layout.dialog_photo);
                mdialogPhoto.setCancelable(false);
                mdialogPhoto.show();
                imgPhoto1 = mdialogPhoto.findViewById(R.id.imag1);
                if(bundle.getString("adUrl")!=null)
                {
                    Glide.with(mdialogPhoto.getContext()).load(dealerRegisterforad.getFullScreenAdURL()).into(imgPhoto1);
                    File photoFile = new File(dealerRegisterforad.getFullScreenAdURL(), "");
                    mPhotoFile1=photoFile;
                }
                else{}
                imgPhoto2 = mdialogPhoto.findViewById(R.id.imag2);
                btnPhoto1 = mdialogPhoto.findViewById(R.id.btnPhoto1);
                btnPhoto = mdialogPhoto.findViewById(R.id.btnPhoto);
                imgPhoto1.setOnClickListener(AdSendRequirementActivity.this);
                imgPhoto2.setOnClickListener(AdSendRequirementActivity.this);
                btnPhoto.setOnClickListener(AdSendRequirementActivity.this);
                btnPhoto1.setOnClickListener(AdSendRequirementActivity.this);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (ConnectivityReceiver.isConnected()) {
                    SubmitQuery submitQuery = new SubmitQuery(0,
                            custIDSList, CustID,
                            CityId,
                            selectedProductId,
                            selectedBusinessList,
                            DemandID,
                            selectCheckBoxPurpose,
                            edRequirement.getText().toString(),
                            productReqID, true,"Advertisement Enquiry","Buy");
                    SendEnquiryToServer(submitQuery);
                    dialog.dismiss();
                }else {
                    showError();
                }
            }
        });
        AlertDialog alertDialog = builder1.create();
        alertDialog.show();
    }

    private void SendEnquiryToServer(SubmitQuery submitQuery) {
        Log.e("Send Data", "With out pic....." + submitQuery.toString());
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AdSendRequirementActivity.this,"Please wait");
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Sending Requirement.."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<SubmitQuery> call = customer_interface.sendDealerEnquiry("bearer " + Token, submitQuery);
        call.enqueue(new Callback<SubmitQuery>() {
            @Override
            public void onResponse(Call<SubmitQuery> call, Response<SubmitQuery> response) {
                try {
                    int statusCode = response.code();
                    switch (statusCode) {
                        case 200:
                            progressDialog.dismiss();
                            progressDialog.dismiss();
                            CustomToast.showToast(AdSendRequirementActivity.this, "Success");
                            startActivity(new Intent(AdSendRequirementActivity.this, DashboardActivity.class)
                                    .putExtra("isNewUser", false));
                            AdSendRequirementActivity.this.finish();
                            break;

                        case 404:
                            progressDialog.dismiss();
                            break;

                        case 406:
                            progressDialog.dismiss();
                            break;

                        case 500:
                            progressDialog.dismiss();
                            CustomToast.showToast(AdSendRequirementActivity.this, "Server Error");
                            break;

                        case 401:
                            progressDialog.dismiss();
                            SessionDialog.CallSessionTimeOutDialog(AdSendRequirementActivity.this);
                            break;
                    }

                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SubmitQuery> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(AdSendRequirementActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AdSendRequirementActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void SendEnquiryWithImageToServer(SubmitQuery submitQuery) {
        Log.e("Send Data", "With  pic....." + submitQuery.toString());
        MultipartBody.Part body = null, body1 = null;
        // File file1,file2;
        RequestBody requestBody1, requestBody2;
        try {
            //file1 = new File(ImagePath);
            requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile1);
            body = MultipartBody.Part.createFormData("file1", mPhotoFile1.getName(), requestBody1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //file2 = new File(ImagePath1);
            requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile2);
            body1 = MultipartBody.Part.createFormData("file2", mPhotoFile2.getName(), requestBody2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AdSendRequirementActivity.this,"Please wait");
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Sending Requirement.."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        Log.e("Body...",body.toString());
        Log.e("Body1...",body1.toString());
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<SubmitQuery> call = customer_interface.sendDealerEnquiryFile("bearer " + Token, submitQuery, body, body1);
        call.enqueue(new Callback<SubmitQuery>() {
            @Override
            public void onResponse(Call<SubmitQuery> call, Response<SubmitQuery> response) {
                try {
                    int statusCode = response.code();
                    switch (statusCode) {
                        case 200:
                            progressDialog.dismiss();
                            CustomToast.showToast(AdSendRequirementActivity.this, "Success");
                            startActivity(new Intent(AdSendRequirementActivity.this, DashboardActivity.class)
                                    .putExtra("isNewUser", false));
                            AdSendRequirementActivity.this.finish();

                            // to delete the stored images in package directory
                            clearApplicationData();

                            mdialogPhoto.dismiss();
                            break;

                        case 404:
                            progressDialog.dismiss();
                            break;

                        case 406:
                            progressDialog.dismiss();
                            break;

                        case 500:
                            progressDialog.dismiss();
                            CustomToast.showToast(AdSendRequirementActivity.this, "Server Error");
                            break;

                        case 401:
                            progressDialog.dismiss();
                            SessionDialog.CallSessionTimeOutDialog(AdSendRequirementActivity.this);
                            break;
                    }

                } catch (Exception e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SubmitQuery> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(AdSendRequirementActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AdSendRequirementActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    public void clearApplicationData() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File appDir = new File(storageDir.getParent());
        Log.e("cache", "path........." + appDir);
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("files")) {
                    deleteDir(new File(appDir, s));
                    Log.e("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
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

    private void getStateListFromServer() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AdSendRequirementActivity.this,"Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<State>> call = customer_interface.getAllStates(/*"bearer " + Token*/);
        call.enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        stateList = new ArrayList<>(response.body());
                        if (stateList.isEmpty()) {

                        } else {
                            openDialogToSelectState();
                        }
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;


                    case 500:
                        CustomToast.showToast(AdSendRequirementActivity.this, "Server Error");
                        progressDialog.dismiss();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AdSendRequirementActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(AdSendRequirementActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AdSendRequirementActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void openDialogToSelectState() {
        mDialogStateSelect = new Dialog(this);
        mDialogStateSelect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setting the dialog height and width to screen size
        mDialogStateSelect.getWindow().setLayout(width, height);

        mDialogStateSelect.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        mDialogStateSelect.setContentView(R.layout.state_dialog_layout);
        mDialogStateSelect.setCancelable(true);
        //set layout height and width to its screen size
        LinearLayout linearLayout = mDialogStateSelect.findViewById(R.id.mainLayout);
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.height = height;
        params.width = width;
        linearLayout.setLayoutParams(params);

        edDialogState = (EditText) mDialogStateSelect.findViewById(R.id.edState);
        Button Clear_txt_btn = mDialogStateSelect.findViewById(R.id.clear_txt_Prise);
        ImageView imageView = (ImageView) mDialogStateSelect.findViewById(R.id.cancel_category);

        recyclerViewState = (RecyclerView) mDialogStateSelect.findViewById(R.id.recyclerCategory);
        recyclerViewState.setHasFixedSize(true);
        recyclerViewState.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        stateSelectorAdapter = new StateSelectorAdapter(AdSendRequirementActivity.this, stateList, AdSendRequirementActivity.this);
        recyclerViewState.setAdapter(stateSelectorAdapter);

        mDialogStateSelect.show();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogStateSelect.dismiss();
            }
        });

        Clear_txt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edDialogProduct.getText().toString().isEmpty()) {
                    edDialogProduct.getText().clear();
                    Clear_txt_btn.setVisibility(View.GONE);

                }

            }
        });

        edDialogState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    stateSelectorAdapter.getFilter().filter(s);
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
                    stateSelectorAdapter.getFilter().filter(s);
                    Log.e("String lenght", "........." + s.length());
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


    // state item selected
    @Override
    public void onItemSelected(State state) {
        edSelectState.setText("" + state.getStateName());
        StateID = state.getStateID();
        mDialogStateSelect.dismiss();
        if (StateID != 0) {
            edCity.getText().clear();
            CityId = 0;
            edCity.setCursorVisible(true);
            if (ConnectivityReceiver.isConnected()) {
                OpenDialogToSelectCity();
            }else {
                showError();
            }

        }
    }

    private void getCitySpinnerFromServer(int stateID, String cityName) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AdSendRequirementActivity.this,"Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<City>> call = customer_interface.getAllCity(/*"bearer " + Token, */stateID, cityName);
        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                int statusCode = response.code();
                Log.e("status", "code...." + statusCode);
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        cityList = new ArrayList<>(response.body());
                        Log.e("City", "count........." + cityList);
                        if (cityList.isEmpty()) {

                        } else {
                            cityAdapter = new CityAdapter(AdSendRequirementActivity.this, cityList, AdSendRequirementActivity.this);
                            recyclerViewCity.setAdapter(cityAdapter);
                            cityAdapter.notifyDataSetChanged();

                        }

                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(AdSendRequirementActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AdSendRequirementActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(AdSendRequirementActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AdSendRequirementActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void OpenDialogToSelectCity() {
        mDialogCitySelect = new Dialog(this);
        mDialogCitySelect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setting the dialog height and width to screen size
        mDialogCitySelect.getWindow().setLayout(width, height);
        mDialogCitySelect.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialogCitySelect.setContentView(R.layout.dialog_city_item_layout);
        mDialogCitySelect.setCancelable(true);
        //set layout height and width to its screen size
        LinearLayout linearLayout = mDialogCitySelect.findViewById(R.id.mainLayout);
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.height = height;
        params.width = width;
        linearLayout.setLayoutParams(params);

        edSelectedCity = (EditText) mDialogCitySelect.findViewById(R.id.edCity);
        Button Clear_txt_btn1 = mDialogCitySelect.findViewById(R.id.clear_txt_Prise);
        ImageView imageView3 = (ImageView) mDialogCitySelect.findViewById(R.id.cancel_childcategory);
        recyclerViewCity = mDialogCitySelect.findViewById(R.id.recyclerCity);
        recyclerViewCity.setHasFixedSize(true);
        recyclerViewCity.setLayoutManager(new LinearLayoutManager(AdSendRequirementActivity.this));


        mDialogCitySelect.show();

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogCitySelect.dismiss();
            }
        });
        edSelectedCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {

                    String cityName = s.toString();
                    if (s.length() <= 0) {
                        Clear_txt_btn1.setVisibility(View.GONE);
                    } else if (cityName.length() >= 3) {
                        if (ConnectivityReceiver.isConnected()) {
                            getCitySpinnerFromServer(StateID, cityName);
                        }else {
                            Toast.makeText(AdSendRequirementActivity.this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
                        }

                        Clear_txt_btn1.setVisibility(View.VISIBLE);
                    } else if (s.length() > 4) {
                        cityAdapter.getFilter().filter(s);
                        Clear_txt_btn1.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (s.length() <= 0) {
                        Clear_txt_btn1.setVisibility(View.GONE);
                    } else if (s.length() == 3) {
                        Clear_txt_btn1.setVisibility(View.VISIBLE);

                    } else if (s.length() > 4) {
                        cityAdapter.getFilter().filter(s);
                        Clear_txt_btn1.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Clear_txt_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edSelectedCity.getText().toString().isEmpty()) {
                    edSelectedCity.getText().clear();
                    Clear_txt_btn1.setVisibility(View.GONE);

                }
            }
        });

    }

    //city selected
    @Override
    public void onCitySelected(City city) {
        CityId = city.getStatewithCityID();
        Log.e("City", "Selected ID.." + CityId);
        edCity.setText("" + city.getVillageLocalityname());
        edSelectedCity.setCursorVisible(false);
        mDialogCitySelect.dismiss();
    }

    @Override
    public void TypeOfBusinessClick(int pos, List<BusinessType> types) {
        if (types.get(pos).getNameOfBusiness().equals("Professionals And Services")) {
            Log.e("Types", "pro");
            isprofessional = true;
        } else {
            Log.e("Types", "..............");
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        selectedBusinessList.clear();
        switch (buttonView.getId()) {

            case R.id.ckId5:
                if (isChecked) {
                    selectCheckBoxPurpose = "Trading";
                    chHome.setChecked(false);
                    chBusiness.setChecked(false);
                } else {
                    chHome.setEnabled(true);
                    chBusiness.setEnabled(true);
                }
                break;

            case R.id.ckId6:
                if (isChecked) {
                    selectCheckBoxPurpose = "Home";
                    chReselling.setChecked(false);
                    chBusiness.setChecked(false);
                } else {
                    chReselling.setEnabled(true);
                    chBusiness.setEnabled(true);
                }
                break;

            case R.id.ckId4:
                if (isChecked) {
                    selectCheckBoxPurpose = "Business";
                    chHome.setChecked(false);
                    chReselling.setChecked(false);
                } else {
                    chHome.setEnabled(true);
                    chReselling.setEnabled(true);
                }
                break;
        }
    }

    private boolean Validatestate() {
        String InputName = edSelectState.getText().toString();
        if (InputName.isEmpty()) {
            CustomToast.showToast(AdSendRequirementActivity.this, "Please select state");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidateCity() {
        String InputName = edCity.getText().toString();
        if (InputName.isEmpty()) {
            CustomToast.showToast(AdSendRequirementActivity.this, "Please select city");
            return false;
        } else {
            return true;
        }
    }


    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AdSendRequirementActivity.this);
        builder.setTitle("Choose your picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    dispatchTakePictureIntent();
                } else if (options[item].equals("Choose from Gallery")) {
                    dispatchGalleryIntent();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Capture image from camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            File photoFile1 = null;
            try {
                if (imgCount == 0) {
                    photoFile = createImageFile();
                } else {
                    photoFile1 = createImageFile();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (imgCount == 0) {
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.mwbtech.dealer_register" + ".provider",
                            photoFile);
                    mPhotoFile1 = photoFile;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    ProjectUtilsKt.checkCameraPermission(AdSendRequirementActivity.this, (status -> {
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                        return null;
                    }));

                }
            } else {
                if (photoFile1 != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.mwbtech.dealer_register" + ".provider",
                            photoFile1);
                    mPhotoFile2 = photoFile1;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    ProjectUtilsKt.checkCameraPermission(AdSendRequirementActivity.this, (status -> {
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                        return null;
                    }));
                }

            }

        }
    }


    /**
     * Select image fro gallery
     */
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
            if (requestCode == REQUEST_TAKE_PHOTO) {

                try {
                    if (imgCount == 0) {
                        mPhotoFile1 = mCompressor.compressToFile(mPhotoFile1);
                        // imgPhoto1.setImageBitmap(mPhotoFile1);
                        Picasso.get()
                                .load(mPhotoFile1)
                                .into(imgPhoto1);
                    } else {
                        mPhotoFile2 = mCompressor.compressToFile(mPhotoFile2);
                        //imgPhoto2.setImageBitmap(BitmapFactory.decodeFile(ImagePath1));
                        Picasso.get()
                                .load(mPhotoFile2)
                                .into(imgPhoto2);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();
                try {
                    if (imgCount == 0) {
                        if (!TextUtils.isEmpty(getRealPathFromUri(selectedImage))){
                            mPhotoFile1 = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                            Picasso.get()
                                    .load(mPhotoFile1)
                                    .into(imgPhoto1);
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

                    } else {
                        if (!TextUtils.isEmpty(getRealPathFromUri(selectedImage))) {
                            mPhotoFile2 = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                            Picasso.get()
                                    .load(mPhotoFile2)
                                    .into(imgPhoto2);
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

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Create file with current timestamp name
     *
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
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
        /*Intent intent = new Intent(this, ReceivedAdvertisementActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        AdSendRequirementActivity.this.finish();*/
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
    public void TypeOfProfClick(int pos, List<ProfessionalReqModel> types) {

    }
}