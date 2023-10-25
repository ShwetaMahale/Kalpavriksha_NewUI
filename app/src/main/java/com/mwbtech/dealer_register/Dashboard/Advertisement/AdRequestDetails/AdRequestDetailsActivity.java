package com.mwbtech.dealer_register.Dashboard.Advertisement.AdRequestDetails;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.AutoSearchStateAdapter;
import com.mwbtech.dealer_register.Adapter.BusinessDemandAdaper;
import com.mwbtech.dealer_register.Adapter.ChildCatgoryAdapter;
import com.mwbtech.dealer_register.Adapter.CityAdapter;
import com.mwbtech.dealer_register.Adapter.ProfessionalReqAdapter;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.PojoClass.AdDetailsModel;
import com.mwbtech.dealer_register.PojoClass.BusinessDemand;
import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.PojoClass.ChildCategoryProduct;
import com.mwbtech.dealer_register.PojoClass.City;
import com.mwbtech.dealer_register.PojoClass.CustIDS;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.PojoClass.ProfessionalReqModel;
import com.mwbtech.dealer_register.PojoClass.SlotBookImages;
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

public class AdRequestDetailsActivity extends AppCompatActivity implements View.OnClickListener, CityAdapter.CitySelectedListener,ProfessionalReqAdapter.ProfessionalTypeClick {

    //Image
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    File mPhotoFile1,mPhotoFile2;
    FileCompressor mCompressor;
    public static ConnectivityReceiver connectivityReceiver;
    IntentFilter intentFilter;
    TextView BTypes;
    EditText edSelectProduct,edCity,edRequirement,edDialogProduct,edSelectedCity,edcustomerName;
    AutoCompleteTextView autotvState;
    CheckBox chReselling, chHome, chBusiness;
    GridView listBusinessDemand, listProfessionalDemand;
    Button btnSubmit,Clear_txt_btn,btnProductSearch;
    Dialog mDialogProduct,mDialogCitySelect;
    RecyclerView recyclerViewProduct,recyclerViewCity;
    ImageView imageView,ivImage1,ivImage2, imgBack;
    List<BusinessType> Btypelist;
    int key=0;
    String fullScreenAdUrl,promoImage;
    String Token;
    PrefManager prefManager;
    int DemandID;
    String Termsofuse;

    int ProductID,StateID,CityId,imgCount,CustID;
    List<ChildCategoryProduct> childCategoryProducts;
    List<State> stateList;
    List<City> cityList;
    List<BusinessDemand> businessDemandList;
    List<BusinessType> businessTypes;
    List<ProfessionalReqModel> professionalReqModels;
    List<CustIDS> custIDSList = new ArrayList<>();
    List<String> custId=new ArrayList<>();

    String productPhoto;

    boolean isprofessional;
    ChildCatgoryAdapter childProductAdapter;
    AutoSearchStateAdapter adapter;
    CityAdapter cityAdapter;
    BusinessDemandAdaper businessDemandAdaper;
    ProfessionalReqAdapter professionalReqAdapter;

    Customer_Interface customer_interface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_request_details);
        prefManager = new PrefManager(this);
        try {
            CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }        initializeViews();
        initializeClickListener();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if(bundle.getString("loginurl")!=null){
                CustID=bundle.getInt("loginurl");
                GetBusinessType();
                initializeSharedData();
            }
            else{
                CustID=bundle.getInt("CustID");
                GetBusinessType();
                initializeSharedData();
            }


            //CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
            //CustID=bundle.getInt("CustID");
            //CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
            GetBusinessType();
            initializeSharedData();
        }
        getStateListFromServer();
        GetBusinessDemand();

        GetProfessionalRequirements();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void initializeViews() {
        autotvState = findViewById(R.id.select_state);
        edSelectProduct = findViewById(R.id.ed_select_product);
        edCity = findViewById(R.id.ed_select_city);
        edRequirement = findViewById(R.id.open_text);
        edcustomerName = findViewById(R.id.ed_customerName);
        ivImage1 = findViewById(R.id.imag1);
        ivImage2 = findViewById(R.id.imag2);
        imgBack = findViewById(R.id.back);
       // BTypes = findViewById(R.id.BTypes);
        chReselling = findViewById(R.id.ckId5);
        chHome = findViewById(R.id.ckId6);
        chBusiness =  findViewById(R.id.ckId4);

        listBusinessDemand = findViewById(R.id.listBusinesDemand);
        listProfessionalDemand = findViewById(R.id.listProfDemand);

        btnSubmit =  findViewById(R.id.bt_submit);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
    }

    private void initializeSharedData() {
        prefManager = new PrefManager(this);
        Token = prefManager.getToken().get(TOKEN);
        mCompressor = new FileCompressor(this);

        intentFilter = new IntentFilter();
        connectivityReceiver = new ConnectivityReceiver();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            AdDetailsModel rcvAd=(AdDetailsModel)bundle.getSerializable("object");
            Gson gson=new Gson();
            Log.e("json..",gson.toJson(rcvAd));
            if(bundle.getString("promoImage")!=null)
            {
                promoImage = bundle.getString("promoImage");
                Glide.with(this).load(promoImage).into(ivImage1);
                File photoFile = new File(promoImage, "");
                imgCount = 0;
                mPhotoFile1 = photoFile;
                edSelectProduct.setText(bundle.getString("Product"));
                edcustomerName.setText(bundle.getString("FirmName"));
                autotvState.setText(bundle.getString("state"));
                edCity.setText(bundle.getString("city"));
                SlotBookImages test=(SlotBookImages) bundle.getSerializable("admodel");
                //Btypelist=test.getBusinessTypes();
                Log.e("businesstypes..",prefManager.getBusinessTypes().toString());
                Btypelist=prefManager.getBusinessTypes();
                ivImage1.setEnabled(false);
                //CustID=test.getCustomerInfo().getCustID();
                CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
                Log.e("custid...",String.valueOf(CustID));
                CityId=test.getCustomerInfo().getCityID();
                for(int i=0;i<1;i++){
                    custIDSList.add(new CustIDS(test.getCustID(),CityId));
                }
                ProductID=test.getProductID();
                //productPhoto=new File(promoImage).getName();
                String filename1 = promoImage.substring(promoImage.lastIndexOf("s/AdImages")+1);
                productPhoto=filename1;
                String path = new File(promoImage).getParent();
                String path1 = new File(promoImage).getAbsolutePath();
                String path2 = new File(promoImage).getPath();

            }
            else if(bundle.getString("textad")!=null){
                SlotBookImages test=(SlotBookImages) bundle.getSerializable("textModel");
                edSelectProduct.setText(test.getProductName());
                edCity.setText(test.getCustomerInfo().getCityName());
                autotvState.setText(test.getCustomerInfo().getStateName());
                //CustID=test.getCustomerInfo().getCustID();
                CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
                CityId=test.getCustomerInfo().getCityID();
                ProductID=test.getProductID();
                edcustomerName.setText(test.getCustomerInfo().getFirmName());
                Btypelist=prefManager.getBusinessTypes();
                edRequirement.setText(test.getAdText());
                File photoFile = new File(mPhotoFile1, "");
                for(int i=0;i<1;i++){
                    custIDSList.add(new CustIDS(test.getCustID(),CityId));
                }
            }
            else if(rcvAd!=null){
                edSelectProduct.setText(rcvAd.getProductName());
                Btypelist=rcvAd.getBusinessTypes();
                ProductID=rcvAd.getProductID();
                if(rcvAd.getAdvertisementType().equals("TextAd")){
                    edRequirement.setText(rcvAd.getAdText());
                }
                else{
                    Glide.with(this).load(rcvAd.getAdImageURL()).into(ivImage1);
                    File photoFile = new File(rcvAd.getAdImageURL(), "");
                    imgCount = 0;
                    mPhotoFile1 = photoFile;
                    //productPhoto=new File(rcvAd.getAdImageURL()).getName();
                    //promoImage=productPhoto;
                    String filename1 = rcvAd.getAdImageURL().substring(rcvAd.getAdImageURL().lastIndexOf("s/AdImages")+1);
                    productPhoto=filename1;
                    Log.e("image...",productPhoto.toString());
                    ivImage1.setEnabled(false);
                }
                edCity.setText(rcvAd.getCustomerDetails().getCityName());
                autotvState.setText(rcvAd.getCustomerDetails().getStateName());
                //CustID=rcvAd.getCustomerDetails().getCustID();
                CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
                CityId=rcvAd.getCustomerDetails().getCityID();
                edcustomerName.setText(rcvAd.getCustomerDetails().getFirmName());

                for(int i=0;i<1;i++){
                    //SearchProductDealer sarch = dealerListAdapter.getResultDealerList().get(i);
                    custIDSList.add(new CustIDS(rcvAd.getCustomerDetails().getCustID(),CityId));
                }

            }
            else {
                DealerRegister test=(DealerRegister) bundle.getSerializable("adUrl");
                Gson gson1 =new Gson();
                Log.e("jsonresponse...",gson1.toJson(test));
                fullScreenAdUrl=test.getFullScreenAdURL();
                key = 1;
                edCity.setText("" + test.getVillageLocalityName());
                autotvState.setText("" + test.getStateName());
                edSelectProduct.setText("" + test.getChildCategoryName());
                edcustomerName.setText("" +test.getAdFirmName());
                CustID = test.getCustID();
                CityId = test.getCityID();
                ProductID = test.getChildCategoryId();
                Glide.with(this).load(fullScreenAdUrl).into(ivImage1);
                File photoFile = new File(fullScreenAdUrl);
                //productPhoto=new File(fullScreenAdUrl).getName();
                String filename1 = fullScreenAdUrl.substring(fullScreenAdUrl.lastIndexOf("s/AdImages")+1);
                productPhoto=filename1;
                for(int i=0;i<1;i++){
                    custIDSList.add(new CustIDS(test.getAdUserID(),CityId));
                }
                imgCount = 0;
                mPhotoFile1 =photoFile;
                Btypelist=test.getBusinessTypeIDList();
                ivImage1.setEnabled(false);
            }
        }
    }

    private void initializeClickListener() {
        edSelectProduct.setOnClickListener(this);
        ivImage1.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        ivImage2.setOnClickListener(this);

        imgBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        autotvState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                State state = (State) adapterView.getItemAtPosition(i);
                StateID = state.getStateID();
                if (StateID != 0) {
                    edCity.getText().clear();
                    CityId = 0;
                    edCity.setCursorVisible(true);
                    OpenDialogToSelectCity(StateID);
                } else {
                    CustomToast.showToast(getApplicationContext(), "Select state");
                }
            }
        });
    }
    private void getStateListFromServer() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AdRequestDetailsActivity.this,"Please wait");
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
                        adapter = new AutoSearchStateAdapter(AdRequestDetailsActivity.this, R.layout.statelist_layout, stateList);
                        autotvState.setAdapter(adapter);
                        break;
                    case 404:
                        progressDialog.dismiss();
                        break;
                    case 500:
                        CustomToast.showToast(AdRequestDetailsActivity.this, "Server Error");
                        progressDialog.dismiss();
                        break;
                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AdRequestDetailsActivity.this);
                        break;
                }
            }
            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(AdRequestDetailsActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AdRequestDetailsActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }
    private void GetProfessionalRequirements() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AdRequestDetailsActivity.this,"Please wait");
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
                        professionalReqAdapter = new ProfessionalReqAdapter(AdRequestDetailsActivity.this, professionalReqModels,AdRequestDetailsActivity.this);
                        listProfessionalDemand.setAdapter(professionalReqAdapter);
                        professionalReqAdapter.notifyDataSetChanged();
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        CustomToast.showToast(AdRequestDetailsActivity.this, "Server Error");
                        progressDialog.dismiss();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AdRequestDetailsActivity.this);
                        break;
                }
            }
            @Override
            public void onFailure(Call<List<ProfessionalReqModel>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(AdRequestDetailsActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AdRequestDetailsActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }
    private void GetBusinessDemand() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AdRequestDetailsActivity.this,"Please wait");
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
                        businessDemandAdaper = new BusinessDemandAdaper(AdRequestDetailsActivity.this, businessDemandList);
                        listBusinessDemand.setAdapter(businessDemandAdaper);
                        businessDemandAdaper.notifyDataSetChanged();
                        break;
                    case 404:
                        progressDialog.dismiss();
                        break;
                    case 500:
                        CustomToast.showToast(AdRequestDetailsActivity.this, "Server Error");
                        progressDialog.dismiss();
                        break;
                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AdRequestDetailsActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<BusinessDemand>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(AdRequestDetailsActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AdRequestDetailsActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }
    private void GetBusinessType() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AdRequestDetailsActivity.this,"Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        //int businessTypeCustid=Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        Call<List<BusinessType>> call = customer_interface.GetBusinessTypesofCustomer("bearer " + Token,CustID);
        Log.e("custid...",String.valueOf(CustID));
        call.enqueue(new Callback<List<BusinessType>>() {
            @Override
            public void onResponse(Call<List<BusinessType>> call, Response<List<BusinessType>> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        progressDialog.dismiss();
                        businessTypes = response.body();
                        Log.e("Businesstypes....",businessTypes.toString());
                        prefManager.setBusinessTypes(businessTypes);
                        //businessDemandAdaper = new BusinessDemandAdaper(AdRequestDetailsActivity.this, businessDemandList);
                        //listBusinessDemand.setAdapter(businessDemandAdaper);
                        //businessDemandAdaper.notifyDataSetChanged();
                        break;
                    case 404:
                        progressDialog.dismiss();
                        break;
                    case 500:
                        CustomToast.showToast(AdRequestDetailsActivity.this, "Server Error");
                        progressDialog.dismiss();
                        break;
                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AdRequestDetailsActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<BusinessType>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(AdRequestDetailsActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AdRequestDetailsActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void OpenDialogToSelectCity(int stateID) {
        mDialogCitySelect = new Dialog(this);
        mDialogCitySelect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogCitySelect.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialogCitySelect.setContentView(R.layout.dialog_city_item_layout);
        mDialogCitySelect.setCancelable(true);
        edSelectedCity = (EditText) mDialogCitySelect.findViewById(R.id.edCity);
        AppCompatImageView Clear_txt_btn1 = mDialogCitySelect.findViewById(R.id.clear_txt_Prise);
        recyclerViewCity = mDialogCitySelect.findViewById(R.id.recyclerCity);
        recyclerViewCity.setHasFixedSize(true);
        recyclerViewCity.setLayoutManager(new LinearLayoutManager(AdRequestDetailsActivity.this));

        ImageView imageView3 = (ImageView) mDialogCitySelect.findViewById(R.id.cancel_childcategory);
        mDialogCitySelect.show();

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
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
                    } else if (cityName.length()>=3){
                        getCitySpinnerFromServer(stateID,cityName);
                        Clear_txt_btn1.setVisibility(View.VISIBLE);
                    }else if (s.length()>4){
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
                    }  else if (s.length()==3){
                        Clear_txt_btn1.setVisibility(View.VISIBLE);

                    }else if (s.length()>4){
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

    @Override
    public void onCitySelected(City city) {
        CityId = city.getStatewithCityID();
        edCity.setText(""+city.getVillageLocalityname());
        edSelectedCity.setCursorVisible(false);
        mDialogCitySelect.dismiss();
    }

    private void getCitySpinnerFromServer(int stateID,String cityName) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AdRequestDetailsActivity.this,"Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<City>> call = customer_interface.getAllCity(/*"bearer " + Token,*/ stateID,cityName);
        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        cityList = new ArrayList<>(response.body());
                        cityAdapter = new CityAdapter(AdRequestDetailsActivity.this, cityList,AdRequestDetailsActivity.this);
                        recyclerViewCity.setAdapter(cityAdapter);
                        cityAdapter.notifyDataSetChanged();
                        break;
                    case 404:
                        CustomToast.showToast(AdRequestDetailsActivity.this, "No city found");
                        progressDialog.dismiss();
                        break;
                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(AdRequestDetailsActivity.this, "Server Error");
                        break;
                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AdRequestDetailsActivity.this);
                        break;
                }
            }
            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(AdRequestDetailsActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AdRequestDetailsActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imag1:
                imgCount = 0;
                selectImage();
                break;

            case R.id.imag2:
                imgCount = 1;
                selectImage();
                break;
            case R.id.bt_submit:
                    if (ConnectivityReceiver.isConnected()) {
                        if (businessDemandAdaper.GetselectedBusinessDemand() != 0) {
                            DemandID = businessDemandAdaper.GetselectedBusinessDemand();
                            Log.e("DemandID...",String.valueOf(DemandID));
                            if (chReselling.isChecked() || chHome.isChecked() || chBusiness.isChecked()) {
                             if(chReselling.isChecked())
                                 Termsofuse="Trading";
                             else if(chHome.isChecked())
                                 Termsofuse="Self Use";
                             else
                                 Termsofuse="Business Use";
                             if(!TextUtils.isEmpty(edRequirement.getText())){
                                    Bundle bundle = getIntent().getExtras();
                                    if (bundle != null) {
                                        AdDetailsModel rcvAd=(AdDetailsModel)bundle.getSerializable("object");
                                        if (bundle.getString("promoImage") != null) {
                                            SubmitQuery submitQuery = new SubmitQuery(0,
                                                    custIDSList, CustID,
                                                    CityId,
                                                    ProductID,
                                                    prefManager.getBusinessTypes(),
                                                    DemandID,
                                                    Termsofuse,
                                                    edRequirement.getText().toString(),
                                                    productPhoto,
                                                    "",0, true, "Advertisement Enquiry","Buy");
                                            SendEnquiryWithImageToServer(submitQuery);
                                        }
                                        else if(bundle.getString("textad")!=null){
                                            SubmitQuery submitQuery = new SubmitQuery(0,
                                                    custIDSList, CustID,
                                                    CityId,
                                                    ProductID,
                                                    prefManager.getBusinessTypes(),
                                                    DemandID,
                                                    Termsofuse,
                                                    edRequirement.getText().toString(),
                                                    "",
                                                    "",
                                                    0, true, "Advertisement Enquiry","Buy");
                                            Gson gson=new Gson();
                                            Log.e("beforehit....",gson.toJson(submitQuery));
                                            SendEnquiryWithImageToServer(submitQuery);
                                        }
                                        else if(rcvAd!=null){
                                            SubmitQuery submitQuery = new SubmitQuery(0,
                                                    custIDSList, CustID,
                                                    CityId,
                                                    ProductID,
                                                    prefManager.getBusinessTypes(),
                                                    DemandID,
                                                    Termsofuse,
                                                    edRequirement.getText().toString(),
                                                    productPhoto,
                                                    "",
                                                    0, true, "Advertisement Enquiry","Buy");
                                            SendEnquiryWithImageToServer(submitQuery);
                                        }
                                        else{
                                            SubmitQuery submitQuery = new SubmitQuery(0,
                                                    custIDSList, CustID,
                                                    CityId,
                                                    ProductID,
                                                    prefManager.getBusinessTypes(),
                                                    DemandID,
                                                    Termsofuse,
                                                    edRequirement.getText().toString(),
                                                    productPhoto,
                                                    "",
                                                    0, true, "Advertisement Enquiry","Buy");
                                            SendEnquiryWithImageToServer(submitQuery);
                                        }

                                    }
                                }
                                else{
                                    Toast.makeText(AdRequestDetailsActivity.this,"Please enter fileds",Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(AdRequestDetailsActivity.this,"Please select Business Purpose",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(AdRequestDetailsActivity.this,"Please select Business Demand",Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(AdRequestDetailsActivity.this,"Sorry! Not Connected to Internet",Toast.LENGTH_LONG).show();
                    }
        }
    }
    private void SendEnquiryWithImageToServer(SubmitQuery submitQuery) {
        MultipartBody.Part body = null, body1 = null;
        RequestBody requestBody1, requestBody2;
        Bundle bundle = getIntent().getExtras();
        AdDetailsModel rcvAd=(AdDetailsModel)bundle.getSerializable("object");
        if (bundle != null) {
            if (bundle.getString("promoImage") != null) {
                try {
                    //to get bannerid
                    File mPhotoFileImage = new File(promoImage, "");
                    mPhotoFileImage = createImageFile();
                    requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFileImage);
                    body = MultipartBody.Part.createFormData("file1", mPhotoFileImage.getName(), requestBody1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile2);
                    body1 = MultipartBody.Part.createFormData("file2", mPhotoFile2.getName(), requestBody2);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (bundle.getString("textad") != null) {
                try {
                    requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile1);
                    body = MultipartBody.Part.createFormData("file1", mPhotoFile1.getName(), requestBody1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile2);
                    body1 = MultipartBody.Part.createFormData("file2", mPhotoFile2.getName(), requestBody2);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(rcvAd!=null){
                if(!rcvAd.getAdvertisementType().equals("TextAd")){
                    try {
                        File mPhotoFileImage = new File(rcvAd.getAdImageURL(), "");
                        mPhotoFileImage = createImageFile();
                        requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFileImage);
                        body = MultipartBody.Part.createFormData("file1", mPhotoFile1.getName(), requestBody1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile2);
                        body1 = MultipartBody.Part.createFormData("file2", mPhotoFile2.getName(), requestBody2);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile1);
                        body = MultipartBody.Part.createFormData("file1", mPhotoFile1.getName(), requestBody1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile2);
                        body1 = MultipartBody.Part.createFormData("file2", mPhotoFile2.getName(), requestBody2);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
            else {
                try {
                    File mPhotoFileImage = new File(fullScreenAdUrl, "");
                    mPhotoFileImage = createImageFile();
                    requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFileImage);
                    body = MultipartBody.Part.createFormData("file1", mPhotoFile1.getName(), requestBody1);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile2);
                    body1 = MultipartBody.Part.createFormData("file2", mPhotoFile2.getName(), requestBody2);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Sending Requirement.."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        Gson gson=new Gson();
        Log.e("gson....",gson.toJson(submitQuery));
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
                            CustomToast.showToast(AdRequestDetailsActivity.this, "Enquiry Sent Successfully");
                            startActivity(new Intent(AdRequestDetailsActivity.this, DashboardActivity.class)
                                    .putExtra("isNewUser", false));
                            AdRequestDetailsActivity.this.finish();
                            break;
                        case 404:
                            progressDialog.dismiss();
                            break;
                        case 500:
                            progressDialog.dismiss();
                            CustomToast.showToast(AdRequestDetailsActivity.this, "Server Error");
                            break;

                        case 401:
                            progressDialog.dismiss();
                            SessionDialog.CallSessionTimeOutDialog(AdRequestDetailsActivity.this);
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
                if (t instanceof IOException) {
                    Toast.makeText(AdRequestDetailsActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AdRequestDetailsActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AdRequestDetailsActivity.this);
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
                if (imgCount==0)
                {
                    photoFile = createImageFile();
                }else
                {
                    photoFile1 = createImageFile();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (imgCount==0)
            {
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.mwbtech.dealer_register" + ".provider",
                            photoFile);
                    mPhotoFile1 = photoFile;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    ProjectUtilsKt.checkCameraPermission(AdRequestDetailsActivity.this, (status -> {
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                        return null;
                    }));

                }
            }else
            {
                if (photoFile1!=null)
                {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.mwbtech.dealer_register" + ".provider",
                            photoFile1);
                    mPhotoFile2 = photoFile1;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    ProjectUtilsKt.checkCameraPermission(AdRequestDetailsActivity.this, (status -> {
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
                    if (imgCount==0)
                    {
                        mPhotoFile1 = mCompressor.compressToFile(mPhotoFile1);
                        Picasso.get()
                                .load(mPhotoFile1)
                                .into(ivImage1);
                    }else
                    {
                        mPhotoFile2 = mCompressor.compressToFile(mPhotoFile2);
                        Picasso.get()
                                .load(mPhotoFile2)
                                .into(ivImage2);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }



            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();
                try {
                    if (imgCount==0)
                    {
                        if (!TextUtils.isEmpty(getRealPathFromUri(selectedImage))){
                            mPhotoFile1 = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                            Picasso.get()
                                    .load(mPhotoFile1)
                                    .into(ivImage1);
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

                    }else{
                        if (!TextUtils.isEmpty(getRealPathFromUri(selectedImage))) {
                            mPhotoFile2 = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                            Picasso.get()
                                    .load(mPhotoFile2)
                                    .into(ivImage2);
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
        Intent intent = new Intent(this, DashboardActivity.class).putExtra("isNewUser", false);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        AdRequestDetailsActivity.this.finish();
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