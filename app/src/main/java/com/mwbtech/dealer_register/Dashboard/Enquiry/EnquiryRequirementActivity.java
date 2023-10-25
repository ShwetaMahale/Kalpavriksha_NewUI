package com.mwbtech.dealer_register.Dashboard.Enquiry;


import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;
import static com.mwbtech.dealer_register.Utils.Utility.getScreenHeight;
import static com.mwbtech.dealer_register.Utils.Utility.getScreenWidth;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.AutoSearchStateAdapter;
import com.mwbtech.dealer_register.Adapter.BusinessDemandAdaper;
import com.mwbtech.dealer_register.Adapter.BusinessTypeAdapter;
import com.mwbtech.dealer_register.Adapter.ChildCatgoryAdapter;
import com.mwbtech.dealer_register.Adapter.CityAdapter;
import com.mwbtech.dealer_register.Adapter.ProfessionalReqAdapter;
import com.mwbtech.dealer_register.Adapter.ShowDealerRegisterAdapter;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.PojoClass.BusinessDemand;
import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.PojoClass.ChildCategoryProduct;
import com.mwbtech.dealer_register.PojoClass.City;
import com.mwbtech.dealer_register.PojoClass.ProfessionalReqModel;
import com.mwbtech.dealer_register.PojoClass.SearchProductDealer;
import com.mwbtech.dealer_register.PojoClass.SearchProductDealerRequest;
import com.mwbtech.dealer_register.PojoClass.SearchProductDealerResponse;
import com.mwbtech.dealer_register.PojoClass.State;
import com.mwbtech.dealer_register.Profile.ProfileMain.UpdateMainActivity;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.DialogUtilsKt;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.mwbtech.dealer_register.databinding.DealerPageBinding;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;
import com.mwbtech.dealer_register.internet_connection.MyInternetCheck;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EnquiryRequirementActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener, ChildCatgoryAdapter.ChildAdapterListener, BusinessTypeAdapter.BusinessTypeClick, CityAdapter.CitySelectedListener, ConnectivityReceiver.ConnectivityReceiverListener, ProfessionalReqAdapter.ProfessionalTypeClick {
    private DealerPageBinding binding;
    public static ConnectivityReceiver connectivityReceiver;
    IntentFilter intentFilter;
    PrefManager prefManager;

    Boolean isRegistered;
    TextView brief;
    ScrollView cityChipScroll;
    Button btnCustomer, btnSubmit, btnChild;
    AppCompatImageView Clear_txt_btn;
    Button btnSearch, btnSubmitCity;
    RecyclerView recyclerView, recyclerViewCity, recyclerViewChild;
    Dialog mDialogChildCategory;
    GridView listBusiness, listBusinessDemand;
    GridView listProfessionalDemand;
    LinearLayout chHome, chBusiness;
    AutoCompleteTextView EdState;
    Dialog mDialogCity;
    ImageView imageView, businessUseSelector, selfUseSelector;
    EditText edCity, dialogCity, atProduct, edOpenText, edProduct1;
    TextView SearchProductHeader, Nocity_textview, Tvbrief;
    HorizontalScrollView hsvCity;
    ChipGroup chipGroupCity;
    int KeyForProfReq;

    BusinessTypeAdapter spinnerBusinessTypeAdapter;
    BusinessDemandAdaper businessDemandAdaper;
    List<BusinessDemand> businessDemandList;
    ShowDealerRegisterAdapter showDealerRegisterAdapter;
    ProfessionalReqAdapter professionalReqAdapter;
    CityAdapter cityAdapter;
    AutoSearchStateAdapter adapter;
    ChildCatgoryAdapter childProductAdapter;
    private LinearLayout businessOwnerCheck, professionalCheck, layoutCategories, chBuy, chSell;
    private ImageView imgBusinessOwner, imgProfessional, buySelector, sellSelector;
    private TextView txtBusinessOwner, txtProfessional, txtBuy, txtSell, txtBusinessUse, txtSelfUse;
    LinearLayout mainLinearLayout, linearLayoutType, linearLayoutDemand, linearLayout, ProfessionalLayout, NeedLayout, llParentLayout, linear_transcationType;
    Customer_Interface customer_interface;

    List<State> stateList;
    List<BusinessType> spinBusiness;
    List<ProfessionalReqModel> professionalReqModels;
    List<City> cityList = new ArrayList<>();
    List<City> selectedCityList = new ArrayList<>();
    List<City> selectedCitiesInDialog = new ArrayList<>();
    List<BusinessType> selectedBusinessList = new ArrayList<>();
    List<ChildCategoryProduct> childCategoryProducts;
    List<SearchProductDealer> searchProductDealerList;

    int height, width, CityId, StateID, ProductID, DemandID, productReqID, Nocity_key = 0, pageNumber = 0, pageSize, totalPages, totalRecords;
    String Token, CityName, selectCheckboxPurpose, path, path1, selectedTranscationType;
    boolean isProfessional, isBuying, isSelling, isBusinessUse, isSelfUse;

    boolean productBeingSearched = false;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setTitle("Your Market Place");


        initializeSharedData();
        initializeScreenHeightWidth();

        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }

       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
    }

    private void showContent() {
        binding = DataBindingUtil.setContentView(this, R.layout.dealer_page);
        initializeViews();
        getBusinessListFromWebServer();
        getStateListFromServer();
        GetBusinessDemand();
        GetProfessionalRequirements();
        initializeClickEvents();
    }

    private void showNoSignalContent() {
        setContentView(R.layout.no_signal_layout);
        Button tryButton = findViewById(R.id.tryBtn);
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
       */
        tryButton.setOnClickListener(view -> checkInternetConnected());
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
        atProduct = findViewById(R.id.edProduct);
        brief = findViewById(R.id.brief);
        edOpenText = findViewById(R.id.open_text);
        linearLayoutType = findViewById(R.id.linearBusiness);
        linearLayout = findViewById(R.id.linear);
        businessOwnerCheck = findViewById(R.id.bownerCk);
        professionalCheck = findViewById(R.id.professionalCk);
        imgBusinessOwner = findViewById(R.id.img_business_owner);
        layoutCategories = findViewById(R.id.layout_categories);
        imgProfessional = findViewById(R.id.img_professional);
        txtBusinessOwner = findViewById(R.id.txt_business_owner);
        txtProfessional = findViewById(R.id.txt_professional);
        edCity = findViewById(R.id.edCity);
        EdState = findViewById(R.id.searchState);
        btnSubmit = findViewById(R.id.submit);
        listBusiness = findViewById(R.id.listBusines);
        listBusinessDemand = findViewById(R.id.listBusinesDemand);
        listProfessionalDemand = findViewById(R.id.listProfDemand);
        ProfessionalLayout = findViewById(R.id.linearBusinessProf);
        linearLayoutDemand = findViewById(R.id.linearBusinessDemand);
        linear_transcationType = findViewById(R.id.linearTranscation);
        NeedLayout = findViewById(R.id.linearnedd);
        SearchProductHeader = findViewById(R.id.productsearch_header);
//        chReselling = findViewById(R.id.ckId5);
        chBuy = findViewById(R.id.ckBuy);
        chSell = findViewById(R.id.ckSell);
        txtBuy = findViewById(R.id.txt_buy);
        txtSell = findViewById(R.id.txt_sell);
        buySelector = findViewById(R.id.img_buy_selector);
        sellSelector = findViewById(R.id.img_sell_selector);
        chHome = findViewById(R.id.ckId6);
        chBusiness = findViewById(R.id.ckId4);
        businessUseSelector = findViewById(R.id.img_business_use_selector);
        selfUseSelector = findViewById(R.id.img_self_use_selector);
        txtBusinessUse = findViewById(R.id.txt_business_use);
        txtSelfUse = findViewById(R.id.txt_self_use);
        Tvbrief = findViewById(R.id.brief);
        hsvCity = findViewById(R.id.horizontalScrollView_city);
        chipGroupCity = findViewById(R.id.chipgroup_city);
        binding.back.setOnClickListener(view -> onBackPressed());
        professionalCheck.setOnClickListener(this);
        businessOwnerCheck.setOnClickListener(this);

     /*   ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
 */
    }

    private void updateUIForBusinessOwner() {
        linearLayoutDemand.setVisibility(View.VISIBLE);
        NeedLayout.setVisibility(View.VISIBLE);
        linear_transcationType.setVisibility(View.VISIBLE);
        atProduct.getText().clear();
        SearchProductHeader.setText("Name of Product");
        atProduct.setHint(R.string.text_select_product);
        isProfessional = false;
        ProfessionalLayout.setVisibility(View.GONE);
        //brief.setText("Name of Product");
        Log.e("Types", "..............");
        professionalCheck.setBackgroundResource(0);
        businessOwnerCheck.setBackgroundResource(R.drawable.shape_rectangle_red);
        imgProfessional.setImageResource(R.drawable.ic_professional_unselected);
        imgBusinessOwner.setImageResource(R.drawable.ic_business_owner_selected);
        txtBusinessOwner.setTextColor(getResources().getColor(R.color.white));
        txtProfessional.setTextColor(getResources().getColor(R.color.black));
        layoutCategories.setVisibility(View.VISIBLE);
        txtBusinessOwner.setTypeface(null, Typeface.BOLD);
        txtProfessional.setTypeface(null, Typeface.NORMAL);
        getBusinessListFromWebServer();
        if (businessDemandList != null) {
            ArrayList<BusinessDemand> demands = new ArrayList<>();
            for (BusinessDemand demand : businessDemandList) {
                demand.setChecked(false);
                demands.add(demand);
            }
            businessDemandList = demands;
            businessDemandAdaper.notifyDataSetChanged();
        }
        if (professionalReqModels != null) {
            ArrayList<ProfessionalReqModel> requirements = new ArrayList<>();
            for (ProfessionalReqModel reqModel : professionalReqModels) {
                reqModel.setChecked(false);
                requirements.add(reqModel);
            }
            professionalReqModels = requirements;
            professionalReqAdapter.notifyDataSetChanged();
        }
        updateUIForDeselection();
    }

    private void updateUIForProfessional() {
        isProfessional = true;
        linearLayoutDemand.setVisibility(View.GONE);
        NeedLayout.setVisibility(View.GONE);
        linear_transcationType.setVisibility(View.GONE);
        ProfessionalLayout.setVisibility(View.VISIBLE);
        SearchProductHeader.setText("Name of Service");
        atProduct.getText().clear();
        atProduct.setHint(R.string.text_select_service);
        businessOwnerCheck.setBackgroundResource(0);
        professionalCheck.setBackgroundResource(R.drawable.shape_rectangle_red);
        imgBusinessOwner.setImageResource(R.drawable.ic_business_owner_unselected);
        imgProfessional.setImageResource(R.drawable.ic_professional_selected);
        txtProfessional.setTextColor(getResources().getColor(R.color.white));
        txtBusinessOwner.setTextColor(getResources().getColor(R.color.black));
        layoutCategories.setVisibility(View.GONE);
        txtProfessional.setTypeface(null, Typeface.BOLD);
        txtBusinessOwner.setTypeface(null, Typeface.NORMAL);
        updateUIForDeselection();
    }

    private void updateUIForBuy() {
        isBuying = true;
        isSelling = false;
        chBuy.setBackgroundResource(R.drawable.shape_rectangle_red);
        chSell.setBackgroundResource(0);
        buySelector.setImageResource(R.drawable.buy_selected);
        sellSelector.setImageResource(R.drawable.sell_unselected);
        txtBuy.setTextColor(getResources().getColor(R.color.white));
        txtSell.setTextColor(getResources().getColor(R.color.black));
        txtBuy.setTypeface(null, Typeface.BOLD);
        txtSell.setTypeface(null, Typeface.NORMAL);
    }

    private void updateUIForSell() {
        isBuying = false;
        isSelling = true;
        chBuy.setBackgroundResource(0);
        chSell.setBackgroundResource(R.drawable.shape_rectangle_red);
        buySelector.setImageResource(R.drawable.buy_unselected);
        sellSelector.setImageResource(R.drawable.sell_selected);
        txtBuy.setTextColor(getResources().getColor(R.color.black));
        txtSell.setTextColor(getResources().getColor(R.color.white));
        txtBuy.setTypeface(null, Typeface.NORMAL);
        txtSell.setTypeface(null, Typeface.BOLD);
    }

    private void updateUIForSelfUse() {
        isBusinessUse = false;
        isSelfUse = true;
        chBusiness.setBackgroundResource(0);
        chHome.setBackgroundResource(R.drawable.shape_rectangle_red);
        businessUseSelector.setImageResource(R.drawable.business_use_unselected);
        selfUseSelector.setImageResource(R.drawable.self_use_selected);
        txtBusinessUse.setTextColor(getResources().getColor(R.color.black));
        txtSelfUse.setTextColor(getResources().getColor(R.color.white));
        txtBusinessUse.setTypeface(null, Typeface.NORMAL);
        txtSelfUse.setTypeface(null, Typeface.BOLD);
    }

    private void updateUIForBusinessUse() {
        isSelfUse = false;
        isBusinessUse = true;
        chBusiness.setBackgroundResource(R.drawable.shape_rectangle_red);
        chHome.setBackgroundResource(0);
        businessUseSelector.setImageResource(R.drawable.business_use_selected);
        selfUseSelector.setImageResource(R.drawable.self_use_unselected);
        txtBusinessUse.setTextColor(getResources().getColor(R.color.white));
        txtSelfUse.setTextColor(getResources().getColor(R.color.black));
        txtBusinessUse.setTypeface(null, Typeface.BOLD);
        txtSelfUse.setTypeface(null, Typeface.NORMAL);
    }

    private void updateUIForDeselection() {
        isBuying = false;
        isSelling = false;
        isSelfUse = false;
        isBusinessUse = false;
        selectedTranscationType = "";
        selectCheckboxPurpose = "";
        chBuy.setBackgroundResource(0);
        chSell.setBackgroundResource(0);
        chBusiness.setBackgroundResource(0);
        chHome.setBackgroundResource(0);
        buySelector.setImageResource(R.drawable.buy_unselected);
        sellSelector.setImageResource(R.drawable.sell_unselected);
        businessUseSelector.setImageResource(R.drawable.business_use_unselected);
        selfUseSelector.setImageResource(R.drawable.self_use_unselected);
        txtBuy.setTextColor(getResources().getColor(R.color.black));
        txtSell.setTextColor(getResources().getColor(R.color.black));
        txtBusinessUse.setTextColor(getResources().getColor(R.color.black));
        txtSelfUse.setTextColor(getResources().getColor(R.color.black));
        txtBuy.setTypeface(null, Typeface.NORMAL);
        txtSell.setTypeface(null, Typeface.NORMAL);
        txtBusinessUse.setTypeface(null, Typeface.NORMAL);
        txtSelfUse.setTypeface(null, Typeface.NORMAL);
    }

    private void initializeScreenHeightWidth() {
        height = getScreenHeight(EnquiryRequirementActivity.this);
        width = getScreenWidth(EnquiryRequirementActivity.this);
    }


    private void initializeSharedData() {
        intentFilter = new IntentFilter();
        connectivityReceiver = new ConnectivityReceiver();
        prefManager = new PrefManager(this);
        isRegistered=prefManager.isIsRegistered();
        Token = prefManager.getToken().get(TOKEN);
    }

    private void initializeClickEvents() {
        atProduct.setOnClickListener(this);
        edCity.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
//        binding.ckId5.setOnCheckedChangeListener(this);
        binding.ckId6.setOnClickListener(this);
        binding.ckId4.setOnClickListener(this);
        binding.ckSell.setOnClickListener(this);
        binding.ckBuy.setOnClickListener(this);
        EdState.setOnClickListener(v -> EdState.setCursorVisible(true));
        EdState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                State state = (State) adapterView.getItemAtPosition(i);
                StateID = state.getStateID();
                if (StateID != 0) {
                    edCity.getText().clear();
                    CityId = 0;
                    edCity.setCursorVisible(true);
                    if (selectedCityList != null) selectedCityList.clear();
                    if (chipGroupCity != null && chipGroupCity.getChildCount() > 0) {
                        chipGroupCity.removeAllViews();
                    }
                    if (ConnectivityReceiver.isConnected()) {
                        OpenDialogToSelectCity();
                    } else {
                        showConnectionError();
                    }
                    hideSoftKeyboard(view);
                } else {
                    CustomToast.showToast(getApplicationContext(), "Select state");
                }

            }
        });
    }

    private void GetProfessionalRequirements() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(EnquiryRequirementActivity.this, "Please wait");
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
                        Log.e("failure ", "error..." + professionalReqModels);
                        professionalReqAdapter = new ProfessionalReqAdapter(EnquiryRequirementActivity.this, professionalReqModels, EnquiryRequirementActivity.this);
                        listProfessionalDemand.setAdapter(professionalReqAdapter);
                        professionalReqAdapter.notifyDataSetChanged();
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        CustomToast.showToast(EnquiryRequirementActivity.this, "Server Error");
                        progressDialog.dismiss();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(EnquiryRequirementActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<ProfessionalReqModel>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(EnquiryRequirementActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EnquiryRequirementActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void GetBusinessDemand() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(EnquiryRequirementActivity.this, "Please wait");
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
                        businessDemandAdaper = new BusinessDemandAdaper(EnquiryRequirementActivity.this, businessDemandList);
                        listBusinessDemand.setAdapter(businessDemandAdaper);
                        Log.e("list...", businessDemandList.toString());
                        businessDemandAdaper.notifyDataSetChanged();
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;


                    case 500:
                        CustomToast.showToast(EnquiryRequirementActivity.this, "Server Error");
                        progressDialog.dismiss();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(EnquiryRequirementActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<BusinessDemand>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(EnquiryRequirementActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EnquiryRequirementActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }


    private void getStateListFromServer() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(EnquiryRequirementActivity.this, "Please wait");
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
                        adapter = new AutoSearchStateAdapter(EnquiryRequirementActivity.this, R.layout.statelist_layout, stateList);
                        EdState.setAdapter(adapter);
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;


                    case 500:
                        CustomToast.showToast(EnquiryRequirementActivity.this, "Server Error");
                        progressDialog.dismiss();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(EnquiryRequirementActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(EnquiryRequirementActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EnquiryRequirementActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }


    private void getBusinessListFromWebServer() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(EnquiryRequirementActivity.this, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<BusinessType>> call = customer_interface.getBusinessTypes("bearer " + Token);
        call.enqueue(new Callback<List<BusinessType>>() {
            @Override
            public void onResponse(Call<List<BusinessType>> call, Response<List<BusinessType>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        spinBusiness = new ArrayList<>(response.body());
                        spinnerBusinessTypeAdapter = new BusinessTypeAdapter(EnquiryRequirementActivity.this, spinBusiness, EnquiryRequirementActivity.this);
                        listBusiness.setAdapter(spinnerBusinessTypeAdapter);
                        spinnerBusinessTypeAdapter.notifyDataSetChanged();
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(EnquiryRequirementActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(EnquiryRequirementActivity.this);
                        break;

                }

            }

            @Override
            public void onFailure(Call<List<BusinessType>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(EnquiryRequirementActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EnquiryRequirementActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });

    }

    // business type  selected event

    @Override
    public void TypeOfBusinessClick(int pos, List<BusinessType> types) {

        /*if (types.get(pos).getNameOfBusiness().equals("Professionals And Services")) {

            Log.e("Types", "pro");

            linearLayoutDemand.setVisibility(View.GONE);
            NeedLayout.setVisibility(View.GONE);
            linear_transcationType.setVisibility(View.GONE);
            ProfessionalLayout.setVisibility(View.VISIBLE);
            SearchProductHeader.setText("Professionals And Services");
            atProduct.setHint("Select Professionals And Service");
            atProduct.getText().clear();
            isProfessional = true;
        }
        else {*/
        Log.e("Types", "..............");
        linearLayoutDemand.setVisibility(View.VISIBLE);
        linear_transcationType.setVisibility(View.VISIBLE);
        NeedLayout.setVisibility(View.VISIBLE);
        ProfessionalLayout.setVisibility(View.GONE);
        SearchProductHeader.setText("Name of Product");
        atProduct.setHint(R.string.text_select_product);
        atProduct.getText().clear();
        isProfessional = false;
//        }
    }

    @Override
    public void TypeOfProfClick(int pos, List<ProfessionalReqModel> types) {
        KeyForProfReq = 1;
        if (types.get(pos).getRequirementName().equals("Admission")) {
            //edOpenText.setHint(R.string.txt_briefprofessional);
        } else{
            //edOpenText.setHint(R.string.txt_briefbusiness);
        }

    }

    private void getCitySpinnerFromServer(int stateID, String cityName) {
        //ProgressDialog progressDialog = customProgressDialog(EnquiryRequirementActivity.this, "Loading cities please wait..");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<City>> call = customer_interface.getAllCity(/*"bearer " + Token,*/ stateID, cityName);
        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                int statusCode = response.code();
                Log.e("status", "code...." + statusCode);
                switch (statusCode) {
                    case 200:
                        //progressDialog.dismiss();
                        cityList = new ArrayList<>(response.body());
                        Gson gson = new Gson();
                        String json = gson.toJson(cityList);
                        Log.e("stateCity_list", "...." + json);
                        if (cityList.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "No city found", Toast.LENGTH_LONG).show();
                        } else {
                            //Nocity_textview.setVisibility(View.GONE);
                            if (selectedCityList != null) {
                                for (int i = 0; i < cityList.size(); i++) {
                                    if (selectedCityList.contains(cityList.get(i))) {
                                        cityList.get(i).setChecked(true);
                                    }
                                }
                            }
                            cityAdapter = new CityAdapter(EnquiryRequirementActivity.this, cityList, EnquiryRequirementActivity.this);
                            recyclerViewCity.setAdapter(cityAdapter);
                        }
                        break;

                    case 404:
                        //progressDialog.dismiss();
                        CustomToast.showToast(EnquiryRequirementActivity.this, "No city found");
                        break;

                    case 500:
                        //progressDialog.dismiss();
                        CustomToast.showToast(EnquiryRequirementActivity.this, "Server Error");
                        break;

                    case 401:
                        //progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(EnquiryRequirementActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                //progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(EnquiryRequirementActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EnquiryRequirementActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);
        MyInternetCheck.getInstance().setConnectivityListener(EnquiryRequirementActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bownerCk:
                if (isProfessional) {
                    DialogUtilsKt.confirm(this, getString(R.string.do_you_really_want_to_switch), getString(R.string.yes), getString(R.string.no), v1 -> {
//                            linearLayoutBusinessType.setVisibility(View.GONE);
                        for (int i = 0; i < businessDemandList.size(); i++) {
                            businessDemandList.get(i).setChecked(false);
                        }
                        businessDemandAdaper = new BusinessDemandAdaper(this, businessDemandList);
                        listBusinessDemand.setAdapter(businessDemandAdaper);

                        for (int i = 0; i < professionalReqModels.size(); i++) {
                            professionalReqModels.get(0).setChecked(false);
                        }
                        professionalReqAdapter = new ProfessionalReqAdapter(this, professionalReqModels, this);
                        listProfessionalDemand.setAdapter(professionalReqAdapter);

                        layoutCategories.setVisibility(View.VISIBLE);
                        ProfessionalLayout.setVisibility(View.GONE);
                        ProductID = 0;
                        updateUIForDeselection();
                        updateUIForBusinessOwner();
                    }, null);
                } else updateUIForBusinessOwner();
                break;

            case R.id.professionalCk:
                if (!isProfessional) {
                    DialogUtilsKt.confirm(this, getString(R.string.do_you_really_want_to_switch), getString(R.string.yes), getString(R.string.no), v1 -> {
                        atProduct.setText("");
                        for (int i = 0; i < businessDemandList.size(); i++) {
                            businessDemandList.get(0).setChecked(false);
                        }
                        businessDemandAdaper = new BusinessDemandAdaper(this, businessDemandList);
                        listBusinessDemand.setAdapter(businessDemandAdaper);

                        for (int i = 0; i < professionalReqModels.size(); i++) {
                            professionalReqModels.get(0).setChecked(false);
                        }
                        professionalReqAdapter = new ProfessionalReqAdapter(this, professionalReqModels, this);
                        listProfessionalDemand.setAdapter(professionalReqAdapter);

                        layoutCategories.setVisibility(View.GONE);
                        ProfessionalLayout.setVisibility(View.VISIBLE);
                        ProductID = 0;
                        updateUIForDeselection();
                        updateUIForProfessional();
                    }, null);
                } else updateUIForProfessional();
                break;

            case R.id.ckBuy:
                selectedTranscationType = "Buy";
                updateUIForBuy();
                break;

            case R.id.ckSell:
                selectedTranscationType = "Sell";
                Log.e("isRegisteredvalue...",String.valueOf(isRegistered));
                if(isRegistered)
                  updateUIForSell();
                else{
                    DialogUtilsKt.confirm(
                            EnquiryRequirementActivity.this,
                            getString(R.string.buytosell),
                            getString(R.string.Cancel),
                            getString(R.string.UpdateKYC),
                            null,
                            view ->{
                                startActivity(new Intent(getApplicationContext(), UpdateMainActivity.class));
                                EnquiryRequirementActivity.this.finish();
                            }
                    );
                }

                break;

            case R.id.ckId4:
                selectCheckboxPurpose = "Business";
                updateUIForBusinessUse();
                break;

            case R.id.ckId6:
                selectCheckboxPurpose = "Self Use";
                updateUIForSelfUse();
                break;

            case R.id.edProduct:
                if (!isProfessional && spinnerBusinessTypeAdapter.getTrueResultList().size() == 0) {
                    CustomToast.showToast(EnquiryRequirementActivity.this, "Please select business type");
                } else {

                    mDialogChildCategory = new Dialog(this);
                    mDialogChildCategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //setting the dialog height and width to screen size
                    mDialogChildCategory.getWindow().setLayout(width, height);

                    mDialogChildCategory.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    mDialogChildCategory.setContentView(R.layout.child_product_list);
                    mDialogChildCategory.setCancelable(true);

                    //set layout height and width to its screen size
                    LinearLayout linearLayout = mDialogChildCategory.findViewById(R.id.mainLayout);
                    ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
                    params.height = height;
                    params.width = width;
                    linearLayout.setLayoutParams(params);

                    edProduct1 = (EditText) mDialogChildCategory.findViewById(R.id.edState);
                    Clear_txt_btn = mDialogChildCategory.findViewById(R.id.clear_txt_Prise);
                    if (isProfessional) {
                        edProduct1.setHint("Professionals And Service");
                    } else {
                        edProduct1.setHint(R.string.text_search_product);
                    }
                    btnSearch = (Button) mDialogChildCategory.findViewById(R.id.btnSearch);
                    recyclerViewChild = (RecyclerView) mDialogChildCategory.findViewById(R.id.recyclerCategory);
                    recyclerViewChild.setHasFixedSize(true);
                    recyclerViewChild.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    ViewGroup.LayoutParams params1 = recyclerViewChild.getLayoutParams();
                    params1.height = height;
                    recyclerViewChild.setLayoutParams(params1);

                    imageView = (ImageView) mDialogChildCategory.findViewById(R.id.cancel_category);
                    btnChild = (Button) mDialogChildCategory.findViewById(R.id.submit);
                    mDialogChildCategory.show();
                    childProductAdapter = new ChildCatgoryAdapter(EnquiryRequirementActivity.this, childCategoryProducts, EnquiryRequirementActivity.this);
                    btnChild.setOnClickListener(this::onClick);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialogChildCategory.dismiss();
                        }
                    });
                    Clear_txt_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!edProduct1.getText().toString().isEmpty()) {
                                edProduct1.getText().clear();
                                Clear_txt_btn.setVisibility(View.GONE);

                            }

                        }
                    });
                    btnSearch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (edProduct1.getText().toString().isEmpty()) {
                                CustomToast.showToast(EnquiryRequirementActivity.this, "Enter product/service name");
                            } else {
                                if (ConnectivityReceiver.isConnected()) {
                                    getChildCategoryListFromServer(edProduct1.getText().toString().trim(), true);
                                } else {
                                    Toast.makeText(EnquiryRequirementActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
                                    //showConnectionError();
                                }
                                hideSoftKeyboard(view);
                            }
                        }
                    });

                    edProduct1.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                           /* try {
                                childProductAdapter.getFilter().filter(s);
                                if (s.length() <= 0) {
                                    Clear_txt_btn.setVisibility(View.GONE);
                                } else {
                                    Clear_txt_btn.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                           /* try {
                                childProductAdapter.getFilter().filter(s);
                                if (s.length() <= 0) {
                                    Clear_txt_btn.setVisibility(View.GONE);
                                } else {
                                    Clear_txt_btn.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/
                            String searchedFor = edProduct1.getText().toString().trim();
                            if (productBeingSearched || searchedFor.length() < 3) return;
                            if (edProduct1.getText().toString().isEmpty()) {
                                CustomToast.showToast(EnquiryRequirementActivity.this, "Enter product/service name");
                            } else {
                                if (ConnectivityReceiver.isConnected()) {
                                    getChildCategoryListFromServer(searchedFor, searchedFor.length() <= 3);
                                } else {
                                    Toast.makeText(EnquiryRequirementActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
                                    //showConnectionError();
                                }
                            }
                        }
                    });
                }

                break;

            case R.id.submit:
                pageNumber = 0;
                selectedBusinessList.clear();
                if (!ValidateState() | !ValidateCity() | !ValidateProduct()) {
                    return;
                } else {
                    if (isProfessional || (spinnerBusinessTypeAdapter.getTrueResultList().size() > 0 && !spinnerBusinessTypeAdapter.getResultList().isEmpty())) {
                        if (isProfessional) {
                            if (professionalReqAdapter.GetselectedProfBusinessDemand() != 0) {
                                Log.e("professionaldemand...", String.valueOf(professionalReqAdapter.GetselectedProfBusinessDemand()));
                                productReqID = professionalReqAdapter.GetselectedProfBusinessDemand();
                                Log.e("id", String.valueOf(productReqID));
                                DemandID = 0;
                                selectCheckboxPurpose = "";
                                selectedTranscationType = "";
                                String str = edOpenText.getText().toString().trim();
                                if (!str.startsWith(" ") && !str.isEmpty()) {
                                    if (ConnectivityReceiver.isConnected()) {
                                        int CustomerId=Integer.valueOf(prefManager.getCustId().get(CUST_ID));
                                        selectedBusinessList.add(new BusinessType(6,6,CustomerId,"Professionals And Services",true));
                                        sendProductRequirementToServer(prefManager.getCustId().get(CUST_ID), CityId, ProductID, selectedBusinessList,
                                                DemandID, selectCheckboxPurpose, edOpenText.getText().toString().trim(), productReqID);
                                    } else {
                                        showConnectionError();
                                    }
                                } else {
                                    CustomToast.showToast(EnquiryRequirementActivity.this, "Brief your requirement");
                                }
                            } else {
                                CustomToast.showToast(EnquiryRequirementActivity.this, "Please select Professional");
                            }
                        } else {
                            selectedBusinessList = spinnerBusinessTypeAdapter.getTrueResultList();
                            if (isBuying || isSelling) {
                                if (businessDemandAdaper.GetselectedBusinessDemand() != 0) {
                                    DemandID = businessDemandAdaper.GetselectedBusinessDemand();
                                    if (/*chReselling.isChecked() || */isSelfUse || isBusinessUse) {
                                        String str = edOpenText.getText().toString().trim();
                                        if (!str.startsWith(" ") && !str.isEmpty()) {
                                            if (ConnectivityReceiver.isConnected()) {
                                                sendProductRequirementToServer(prefManager.getCustId().get(CUST_ID), CityId, ProductID, selectedBusinessList,
                                                        DemandID, selectCheckboxPurpose, edOpenText.getText().toString().trim(), productReqID);
                                            } else {
                                                showConnectionError();
                                            }

                                        } else {
                                            CustomToast.showToast(EnquiryRequirementActivity.this, "Brief your requirement");
                                        }
                                    } else {
                                        CustomToast.showToast(EnquiryRequirementActivity.this, "Please select Business Purpose");
                                    }
                                } else {
                                    CustomToast.showToast(EnquiryRequirementActivity.this, "Please select Business Demand");
                                }
                            } else {
                                CustomToast.showToast(EnquiryRequirementActivity.this, "Please select Transaction Type");
                            }
                        }

                    } else {
                        CustomToast.showToast(EnquiryRequirementActivity.this, "Please select Business Type");
                    }
                }
                break;


            case R.id.edCity:
                if (StateID == 0) {
                    CustomToast.showToast(EnquiryRequirementActivity.this, "Please select state");
                } else {
                    if (ConnectivityReceiver.isConnected()) {
                        OpenDialogToSelectCity();
                    } else {
                        showConnectionError();
                    }
                }
                break;
        }
    }

    private void OpenDialogToSelectCity() {
        mDialogCity = new Dialog(this);
        mDialogCity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setting the dialog height and width to screen size
        mDialogCity.getWindow().setLayout(width, height);

        mDialogCity.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialogCity.setContentView(R.layout.dialog_city_item_layout);
        mDialogCity.setCancelable(true);

        //set layout height and width to its screen size
        LinearLayout linearLayout = mDialogCity.findViewById(R.id.mainLayout);
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.height = height;
        params.width = width;
        linearLayout.setLayoutParams(params);
        dialogCity = (EditText) mDialogCity.findViewById(R.id.edCity);
        btnSubmitCity = (Button) mDialogCity.findViewById(R.id.btn_city_submit);
        AppCompatImageView Clear_txt_btn1 = mDialogCity.findViewById(R.id.clear_txt_Prise);
        cityChipScroll = mDialogCity.findViewById(R.id.chipScrollView);

        recyclerViewCity = mDialogCity.findViewById(R.id.recyclerCity);
        recyclerViewCity.setHasFixedSize(true);
        recyclerViewCity.setLayoutManager(new LinearLayoutManager(EnquiryRequirementActivity.this));


        ImageView imageView3 = (ImageView) mDialogCity.findViewById(R.id.cancel_childcategory);
        mDialogCity.show();

        imageView3.setOnClickListener(v -> mDialogCity.dismiss());

        if(selectedCityList.size()>0){
            for (City cityItem : selectedCityList) {
                showChipsForSelectedCities(cityItem);
            }
        }

        btnSubmitCity.setOnClickListener(v -> {
            if (selectedCityList.size() == 0) {
                CustomToast.showToast(EnquiryRequirementActivity.this, "Please select city first");
            } else if (selectedCityList.size() < 4) {
                DialogUtilsKt.confirm(
                        EnquiryRequirementActivity.this,
                        getString(R.string.multiple_city_selection),
                        getString(R.string.yes),
                        getString(R.string.no),
                        null,
                        view -> updateUIForSelectedCities()
                );
            } else updateUIForSelectedCities();
        });

        dialogCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String cityName = s.toString();
                    if (s.length() <= 0) {
                        Clear_txt_btn1.setVisibility(View.GONE);

                    } else if (cityName.length() == 3) {
                        getCitySpinnerFromServer(StateID, cityName);
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
        Clear_txt_btn1.setOnClickListener(view -> {
            if (!dialogCity.getText().toString().isEmpty()) {
                dialogCity.getText().clear();
                Clear_txt_btn1.setVisibility(View.GONE);

            }
        });

    }

    private void updateUIForSelectedCities() {
        if (selectedCityList == null) return;
        if (selectedCityList.size() > 4) {
            CustomToast.showToast(EnquiryRequirementActivity.this, "You can select only 4 cities");
            return;
        }
        StringBuilder selectedCities = new StringBuilder();
        for (int i = 0; i < selectedCityList.size(); i++) {
            if (i != selectedCityList.size() - 1) {
                selectedCities.append(selectedCityList.get(i).getVillageLocalityname()).append(",");
            } else {
                selectedCities.append(selectedCityList.get(i).getVillageLocalityname());
            }
        }
//        edCity.setText(selectedCities);
        EdState.setCursorVisible(false);
        atProduct.setFocusable(true);
        mDialogCity.dismiss();
        if (chipGroupCity.getChildCount() > 0) {
            chipGroupCity.removeAllViews();
        }

        if (selectedCityList.size() > 0) {
            hsvCity.setVisibility(View.VISIBLE);
            for (City city : selectedCityList) {

                Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.chip_grp_item, chipGroupCity, false);
                mChip.findViewById(R.id.chips);
                mChip.setText(city.getVillageLocalityname());
                Chip chip = mChip;
                mChip.setOnCloseIconClickListener(view -> {
                            city.setChecked(false);
                            chipGroupCity.removeView(chip);
                            selectedCityList.remove(city);
                        }
                );
                chipGroupCity.addView(mChip);
            }

//            if (selectedCityList.size() < 4) {
//                CustomToast.showToast(EnquiryRequirementActivity.this, "You can select only 4 cities");
//            }
        } else hsvCity.setVisibility(View.GONE);
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            selectedCityList.removeIf(city -> (!city.isChecked()));
        } else {
            for (int i = 0; i < selectedCityList.size(); i++) {
                if (!selectedCityList.get(i).isChecked()) {
                    selectedCityList.remove(i);
                }
            }
        }
        selectedCities.setLength(0);
        for (int i = 0; i < selectedCityList.size(); i++) {
            if (i != selectedCityList.size() - 1) {
                selectedCities.append(selectedCityList.get(i).getVillageLocalityname()).append(",");
            } else {
                selectedCities.append(selectedCityList.get(i).getVillageLocalityname());
            }
        }*/
//        edCity.setText(selectedCities);
    }

    // city selected event
    @Override
    public void onCitySelected(City city) {
        CityId = city.getStatewithCityID();

        if (selectedCityList == null) selectedCityList = new ArrayList<>();

        if (!selectedCityList.contains(city)) {
            if (city.isChecked()) selectedCityList.add(city);
            else selectedCityList.remove(city);
        } else {
            if (city.isChecked()) {

            }
        }
        if (city.isChecked()) {
            if (!selectedCityList.contains(city)) {
                selectedCityList.add(city);
            }
        } else {
            selectedCityList.remove(city);
        }
/*
        if (selectedCitiesInDialog == null) selectedCitiesInDialog = new ArrayList<>();
*//*
        if (!selectedCitiesInDialog.contains(city)) {
            if (city.isChecked()) selectedCitiesInDialog.add(city);
            else selectedCitiesInDialog.remove(city);
        } else {
            if (city.isChecked()) {

            }
        }*//*
        if (city.isChecked()) {
            if (!selectedCitiesInDialog.contains(city)) selectedCitiesInDialog.add(city);
        } else selectedCitiesInDialog.remove(city);*/

        Log.e("City", "Selected ID.." + CityId);
        CityName = city.getVillageLocalityname();
        showChipsForSelectedCities(city);
//        edCity.setText("" + CityName);
//        EdState.setCursorVisible(false);
//        atProduct.setFocusable(true);
//        mDialogCity.dismiss();
    }

    private void showChipsForSelectedCities(City city) {
        cityChipScroll.setVisibility(View.VISIBLE);

        ChipGroup chipGroupSearchDialog = mDialogCity.findViewById(R.id.chipgroup);
        Chip mChipp = (Chip) this.getLayoutInflater().inflate(R.layout.chip_grp_fragment, chipGroupSearchDialog, false);
        mChipp.findViewById(R.id.chips);

        if (city.isChecked()) {
            mChipp.setText(city.getVillageLocalityname());

            Chip finalMChipp = mChipp;
            mChipp.setOnCloseIconClickListener(view -> {
                city.setChecked(false);
                chipGroupSearchDialog.removeView(finalMChipp);
                if(selectedCitiesInDialog.size() > 0){
                    for (City child : selectedCitiesInDialog) {
                        if (child.getID() == city.getID()) {
                            selectedCitiesInDialog.remove(child);
                            break;
                        }
                    }
                }

                if(selectedCityList.size() > 0){
                    for (City child : selectedCityList) {
                        if (child.getID() == city.getID()){
                            selectedCityList.remove(child);
                            break;
                        }
                    }
                }
            });
            chipGroupSearchDialog.addView(mChipp);
            if(!selectedCitiesInDialog.contains(city))
                selectedCitiesInDialog.add(city);

        } else {
            for (int i = 0; i < chipGroupSearchDialog.getChildCount(); i++) {
                mChipp = (Chip) chipGroupSearchDialog.getChildAt(i);

                if (mChipp.getText().toString().equals(city.getVillageLocalityname())) {
                    if (!city.isChecked()) {
                        city.setChecked(false);
                        chipGroupSearchDialog.removeView(mChipp);

                        if(selectedCitiesInDialog.size() > 0){
                            for (City child : selectedCitiesInDialog) {
                                if (child.getID() == city.getID()) {
                                    selectedCitiesInDialog.remove(child);
                                    break;
                                }
                            }
                        }

                        if(selectedCityList.size() > 0){
                            for (City child : selectedCityList) {
                                if (child.getID() == city.getID()){
                                    selectedCityList.remove(child);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void getChildCategoryListFromServer(String childName, boolean showLoader) {
        productBeingSearched = true;
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(EnquiryRequirementActivity.this, "Please wait");
        if (!showLoader) progressDialog.dismiss();
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<ChildCategoryProduct>> call = customer_interface.getChildCategoryProduct("bearer " + Token, childName, isProfessional, false);
        call.enqueue(new Callback<List<ChildCategoryProduct>>() {
            @Override
            public void onResponse(Call<List<ChildCategoryProduct>> call, Response<List<ChildCategoryProduct>> response) {
                productBeingSearched = false;
                int statusCode = response.code();
                Log.e("status..inchild..", String.valueOf(statusCode));
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        childCategoryProducts = new ArrayList<>(response.body());
                        if (childCategoryProducts.isEmpty()) {
                            CustomToast.showToast(EnquiryRequirementActivity.this, "No such product found..");
                        } else {
                            childProductAdapter = new ChildCatgoryAdapter(EnquiryRequirementActivity.this, childCategoryProducts, EnquiryRequirementActivity.this);
                            recyclerViewChild.setAdapter(childProductAdapter);
                            childProductAdapter.notifyDataSetChanged();
                        }

                        break;
                    case 404:
                        progressDialog.dismiss();
                        CustomToast.showToast(EnquiryRequirementActivity.this, "No such product found.");
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(EnquiryRequirementActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(EnquiryRequirementActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<ChildCategoryProduct>> call, Throwable t) {
                progressDialog.dismiss();
                productBeingSearched = false;
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(EnquiryRequirementActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EnquiryRequirementActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void sendProductRequirementToServer(String cust_id, int cityId, int productID, List<BusinessType> selectedBusinessList1, int demandList, String selectchechboxpurpose, String requirement, int productReqID) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(EnquiryRequirementActivity.this, "Please wait");
        SearchProductDealerRequest searchProductDealer = new SearchProductDealerRequest(Integer.parseInt(cust_id), cityId, selectedCityList, productID, selectedBusinessList1, demandList, selectchechboxpurpose, requirement, path, path1, productReqID, pageNumber + 1, 0, null);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Gson gson = new Gson();
        String json = gson.toJson(searchProductDealer);
        Log.e("productreq...", json);
        Call<SearchProductDealerResponse> call = customer_interface.searchProductDealerDetails("bearer " + Token, searchProductDealer);
        call.enqueue(new Callback<SearchProductDealerResponse>() {
            @Override
            public void onResponse(Call<SearchProductDealerResponse> call, Response<SearchProductDealerResponse> response) {
                int statusCode = response.code();
                Log.e("Status", "code....." + statusCode);
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        pageNumber = response.body().getPageNumber();
                        pageSize = response.body().getPageSize();
                        totalRecords = response.body().getTotalRecords();
                        totalPages = response.body().getTotalPages();
                        searchProductDealerList = new ArrayList<>(response.body().getData());
                        if (searchProductDealerList.size() != 0) {
                            Log.e("type..", selectedTranscationType);

                            /*startActivity(new Intent(EnquiryRequirementActivity.this, SendEnquiryActivity.class)
                                    .putExtra("object", (Serializable) searchProductDealer)
                                    .putExtra("BusinessList", (Serializable) selectedBusinessList)
                                    .putExtra("Dealer", (Serializable) searchProductDealerList));

                            EnquiryRequirementActivity.this.finish();*/
                            Intent i = new Intent(EnquiryRequirementActivity.this, SendEnquiryActivity.class);
                            i.putExtra("object", (Serializable) searchProductDealer);
                            i.putExtra("BusinessList", (Serializable) selectedBusinessList);
                            i.putExtra("Dealer", (Serializable) searchProductDealerList);
                            i.putExtra("Transcation Type", (String) selectedTranscationType);
                            i.putExtra("pageNumber", (int) pageNumber);
                            i.putExtra("pageSize", (int) pageSize);
                            i.putExtra("totalRecords", (int) totalRecords);
                            i.putExtra("totalPages", (int) totalPages);
                            startActivity(i);
                            // startActivityForResult(i, 1);
                        } else {
                            CallAlertDialog();
                        }
                        break;

                    case 404:
                        progressDialog.dismiss();
                        CallAlertDialog();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(EnquiryRequirementActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(EnquiryRequirementActivity.this);
                        break;

                }

            }

            @Override
            public void onFailure(Call<SearchProductDealerResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(EnquiryRequirementActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EnquiryRequirementActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });

    }


    private void CallAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EnquiryRequirementActivity.this);
        builder.setTitle("Not found.!");
        builder.setMessage("Result not found for your requirement");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        try {
            showDealerRegisterAdapter.getFilter().filter(s);
            recyclerView.getRecycledViewPool().clear();
            if (showDealerRegisterAdapter.getItemCount() <= 0) {
                linearLayout.setVisibility(View.GONE);
                btnCustomer.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.VISIBLE);
                btnCustomer.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            showDealerRegisterAdapter.getFilter().filter(s);
            recyclerView.getRecycledViewPool().clear();
            if (showDealerRegisterAdapter.getItemCount() <= 0) {
                linearLayout.setVisibility(View.GONE);
                btnCustomer.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.VISIBLE);
                btnCustomer.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        selectedBusinessList.clear();
        switch (buttonView.getId()) {

            /*case R.id.ckId5:
                if (isChecked) {
                    selectCheckboxPurpose = "Trading";
                    chHome.setChecked(false);
                    chBusiness.setChecked(false);
//                    chReselling.setTypeface(null, Typeface.BOLD);
                    chHome.setTypeface(null, Typeface.NORMAL);
                    chBusiness.setTypeface(null, Typeface.NORMAL);
                } else {
                    chHome.setEnabled(true);
                    chBusiness.setEnabled(true);
//                    chReselling.setTypeface(null, Typeface.NORMAL);
                    chHome.setTypeface(null, Typeface.NORMAL);
                    chBusiness.setTypeface(null, Typeface.NORMAL);
                }
                break;

            case R.id.ckId6:
                selfUseSelector.setImageResource(isChecked? R.drawable.self_use_selected : R.drawable.self_use_unselected);
                if (isChecked) {
                    selectCheckboxPurpose = "Self Use";
//                    chReselling.setChecked(false);
                    chBusiness.setChecked(false);
//                    chReselling.setTypeface(null, Typeface.NORMAL);
                    chHome.setTypeface(null, Typeface.BOLD);
                    chBusiness.setTypeface(null, Typeface.NORMAL);
                } else {
//                    chReselling.setEnabled(true);
                    chBusiness.setEnabled(true);
//                    chReselling.setTypeface(null, Typeface.NORMAL);
                    chHome.setTypeface(null, Typeface.NORMAL);
                    chBusiness.setTypeface(null, Typeface.NORMAL);
                }
                break;

            case R.id.ckId4:
                businessUseSelector.setImageResource(isChecked? R.drawable.business_use_selected : R.drawable.business_use_unselected);
                if (isChecked) {
                    selectCheckboxPurpose = "Business";
                    chHome.setChecked(false);
//                    chReselling.setChecked(false);
//                    chReselling.setTypeface(null, Typeface.NORMAL);
                    chHome.setTypeface(null, Typeface.NORMAL);
                    chBusiness.setTypeface(null, Typeface.BOLD);
                } else {
                    chHome.setEnabled(true);
//                    chReselling.setEnabled(true);
//                    chReselling.setTypeface(null, Typeface.NORMAL);
                    chHome.setTypeface(null, Typeface.NORMAL);
                    chBusiness.setTypeface(null, Typeface.NORMAL);
                }
                break;
*/
            /*case R.id.ckBuy:
                buySelector.setImageResource(isChecked? R.drawable.buy_selected : R.drawable.buy_unselected);
                if (isChecked) {
                    selectedTranscationType = "Buy";
                    Log.e("type..", selectedTranscationType);
                    chSell.setChecked(false);
                    chBuy.setTypeface(null, Typeface.BOLD);
                    chSell.setTypeface(null, Typeface.NORMAL);
                } else {
                    chSell.setEnabled(true);
                    chBuy.setTypeface(null, Typeface.NORMAL);
                    chSell.setTypeface(null, Typeface.NORMAL);
                }
                break;
            case R.id.ckSell:
                sellSelector.setImageResource(isChecked? R.drawable.sell_selected : R.drawable.sell_unselected);
                if (isChecked) {
                    selectedTranscationType = "Sell";
                    chBuy.setChecked(false);
                    chBuy.setTypeface(null, Typeface.NORMAL);
                    chSell.setTypeface(null, Typeface.BOLD);
                } else {
                    chBuy.setEnabled(true);
                    chBuy.setTypeface(null, Typeface.NORMAL);
                    chSell.setTypeface(null, Typeface.NORMAL);
                }
                break;*/
        }
    }


    @Override
    public void onItemSelected(ChildCategoryProduct childCreation) {
        ProductID = childCreation.getChildCategoryId();
        atProduct.setText("" + childCreation.getChildCategoryName());
        EdState.setCursorVisible(false);
//        chReselling.requestFocus();
        mDialogChildCategory.dismiss();
    }

    private boolean ValidateState() {
        String InputName = EdState.getText().toString();
        if (InputName.isEmpty()) {
            CustomToast.showToast(EnquiryRequirementActivity.this, "Please select state");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidateCity() {
        if (selectedCityList.size() <= 0) {
//        String InputName = edCity.getText().toString();
//        if (InputName.isEmpty()) {
            CustomToast.showToast(EnquiryRequirementActivity.this, "Please select city");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidateProduct() {
        String InputName = atProduct.getText().toString();
        if (InputName.isEmpty()) {
            CustomToast.showToast(EnquiryRequirementActivity.this, "Please select product");
            return false;
        } else {
            return true;
        }
    }

    private void showConnectionError() {
        Snackbar snackbar = Snackbar
                .make(llParentLayout, "Not connected to internet", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
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
        EnquiryRequirementActivity.this.finish();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle", "onDestroy invoked");

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


}
