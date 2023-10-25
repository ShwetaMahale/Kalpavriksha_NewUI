package com.mwbtech.dealer_register.Dashboard.OnetoOne;

//package com.mwbtech.dealer_register.Dashboard.OnetoOne;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CITY_NAME;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.STATE_NAME;
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
import android.graphics.Typeface;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.BusinessDemandAdaper;
import com.mwbtech.dealer_register.Adapter.BusinessDemandCustomer;
import com.mwbtech.dealer_register.Adapter.BusinessTypeAdapter;
import com.mwbtech.dealer_register.Adapter.CityAdapter;
import com.mwbtech.dealer_register.Adapter.CustomerSelectorAdapter;
import com.mwbtech.dealer_register.Adapter.OneFilterCityAdapter;
import com.mwbtech.dealer_register.Adapter.OneToOneProductAdapter;
import com.mwbtech.dealer_register.Adapter.ProfessionalReqAdapter;
import com.mwbtech.dealer_register.Adapter.StateFilterAdapter;
import com.mwbtech.dealer_register.Adapter.StateSelectorAdapter;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.Dashboard.Enquiry.EnquiryRequirementActivity;
import com.mwbtech.dealer_register.PojoClass.BusinessDemand;
import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.PojoClass.ChildCategoryProduct;
import com.mwbtech.dealer_register.PojoClass.City;
import com.mwbtech.dealer_register.PojoClass.CustIDS;
import com.mwbtech.dealer_register.PojoClass.CustomerModel;
import com.mwbtech.dealer_register.PojoClass.ProfessionalReqModel;
import com.mwbtech.dealer_register.PojoClass.State;
import com.mwbtech.dealer_register.PojoClass.SubCategoryProduct;
import com.mwbtech.dealer_register.PojoClass.SubmitQuery;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.DialogUtilsKt;
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
import java.util.LinkedHashSet;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OnetoOneActivity extends AppCompatActivity implements View.OnClickListener, OneToOneProductAdapter.ChildAdapterListener, BusinessTypeAdapter.BusinessTypeClick, StateSelectorAdapter.StateAdapterListener, CustomerSelectorAdapter.CustomerItemSelecet, CompoundButton.OnCheckedChangeListener, CityAdapter.CitySelectedListener, ProfessionalReqAdapter.ProfessionalTypeClick {

    //Image
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    static final long image_size = 2097152;//restricting to 2MB

    File mPhotoFile1, mPhotoFile2;
    FileCompressor mCompressor;

    public static ConnectivityReceiver connectivityReceiver;
    EditText edSelectProduct, edCity, edRequirement, edDialogProduct,
            edSelectedCity, edSelectState, edDialogState, edSelectCustomer;

    LinearLayout /*chReselling,*/ chHome, chBusiness, chBuy, chSell;
    ;
    GridView listBusinessDemand, listViewBusinessType;
    GridView listProfessionalDemand;
    Button btnSubmit, BtnCustomerSubmit, btnSubmitCity;
    AppCompatImageView Clear_txt_btn;
    IntentFilter intentFilter;
    String Token, selectedTranscationType;
    PrefManager prefManager;
    HorizontalScrollView hsv, hsvCity;
    Dialog mDialogProduct, mDialogCitySelect, mDialogStateSelect, mDialogSelectCustomer;
    RecyclerView recyclerViewProduct, recyclerViewState, recyclerViewCustomer, recyclerViewCity;
    ImageView imageView, ivImage1, ivImage2;
    LinearLayout llBusinessType;
    ChipGroup chipGroupDemo, chipGroupCity;

    ScrollView cityChipScroll;
    LinearLayout ProfessionalLayout, NeedLayout, llParentLayout, linearTransaction, linearLayoutBusinessType;
    ConstraintLayout linearLayoutDemand;
    AlertDialog.Builder builder1;
    Dialog mdialogPhoto;
    Button btnPhoto, btnPhoto1;
    ImageView imgPhoto1, imgPhoto2, imgBack;
    TextView brief, NamePrd;
    int ProductID, StateID, CityId, imgCount, CustID, productReqID, DemandID, BusinessTypeID = 0;
    int height, width;
    String selectCheckBoxPurpose;
    //List<OneToOneProductModel> childCategoryProducts;
    List<ChildCategoryProduct> childCategoryProducts;
    Dialog mDialogChildCategory;
    EditText edProduct1;
    Button btnSearch, btnChild;
    RecyclerView recyclerViewChild;


    List<State> stateList, stateFilter;
    List<City> cityList = new ArrayList<>();
    List<City> selectedCityList = new ArrayList<>();
    List<City> selectedCitiesInDialog = new ArrayList<>();
    List<BusinessDemand> businessDemandList;
    List<BusinessType> selectedBusinessType = new ArrayList<>();
    List<BusinessType> selectedBusinessType1 = new ArrayList<>();
    List<ProfessionalReqModel> professionalReqModels;
    List<BusinessType> businessTypeList = new ArrayList<>();
    List<BusinessType> businessTypeListt = new ArrayList<>();
    List<BusinessType> selectedBusinessList = new ArrayList<>();
    List<CustomerModel> customerModels;
    List<CustomerModel> customerSelectedModels = new ArrayList<>();
    List<CustomerModel> finalCustomerSelectedModels = new ArrayList<>();
    List<CustIDS> custIDSList = new ArrayList<>();
    int key = 0;
    String fullScreenAdUrl;
    boolean isProfessional, isBuying, isSelling, isBusinessUse, isSelfUse, isBusinessTypeSelected;
    CustomerSelectorAdapter customerSelectorAdapter;
    BusinessTypeAdapter businessTypeAdapter;
    OneToOneProductAdapter childProductAdapter;
    StateSelectorAdapter stateSelectorAdapter, stateSelectorFilterAdapter;
    CityAdapter cityAdapter;
    BusinessDemandAdaper businessDemandAdaper;
    ProfessionalReqAdapter professionalReqAdapter;
    private LinearLayout businessOwnerCheck, professionalCheck/*, layoutCategories*/;
    private ImageView imgBusinessOwner, imgProfessional;
    private TextView txtBusinessOwner, txtProfessional, txtBuy, txtSell, txtBusinessUse, txtSelfUse;

    Customer_Interface customer_interface;
    private static final String[] filterModel = {"State", "City", "Business Type"};
    ListView lv, lv2;

    //List<BusinessType> businessTypeList = new ArrayList<>();
    com.mwbtech.dealer_register.Adapter.FdChild businessTypeAdapterr;
    List<City> cityListt = new ArrayList<>();
    OneFilterCityAdapter filterCityAdapter;
    List<State> stateListt = new ArrayList<>();
    StateFilterAdapter filterStateAdapter;
    Button ApplyFilter;
    ImageView businessUseSelector, selfUseSelector, buySelector, sellSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initializeSharedData();
        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }
      /*  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
    }

    private void showContent() {
        setContentView(R.layout.activity_oneto_one);
        initializeViews();
        initializeScreenHeightWidth();
        initializeSharedData();
        initializeClickListener();
        GetBusinessDemand();
        GetProfessionalRequirements();
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
        llParentLayout = findViewById(R.id.parentLayout);
        edSelectState = findViewById(R.id.select_state);
        edSelectCustomer = findViewById(R.id.select_Customer);
        edSelectProduct = findViewById(R.id.ed_select_product);
        edCity = findViewById(R.id.ed_select_city);
        edRequirement = findViewById(R.id.open_text);
        brief = findViewById(R.id.brief);
        //NamePrd = findViewById(R.id.NamePrd);
        ivImage1 = findViewById(R.id.imag1);
        ivImage2 = findViewById(R.id.imag2);
        hsv = findViewById(R.id.horizontalScrollView1);
        chipGroupDemo = findViewById(R.id.chipgroup);
        hsvCity = findViewById(R.id.horizontalScrollView_city);
        chipGroupCity = findViewById(R.id.chipgroup_city);
        imgBack = findViewById(R.id.back);
        businessOwnerCheck = findViewById(R.id.bownerCk);
        professionalCheck = findViewById(R.id.professionalCk);
        imgBusinessOwner = findViewById(R.id.img_business_owner);
//        layoutCategories = findViewById(R.id.layout_categories);
        imgProfessional = findViewById(R.id.img_professional);
        txtBusinessOwner = findViewById(R.id.txt_business_owner);
        txtProfessional = findViewById(R.id.txt_professional);

//        chReselling = findViewById(R.id.ckId5);
        chHome = findViewById(R.id.ckId6);
        chBusiness = findViewById(R.id.ckId4);

        chBuy = findViewById(R.id.ckBuy);
        chSell = findViewById(R.id.ckSell);
        txtBuy = findViewById(R.id.txt_buy);
        txtSell = findViewById(R.id.txt_sell);
        txtBusinessUse = findViewById(R.id.txt_business_use);
        txtSelfUse = findViewById(R.id.txt_self_use);
        buySelector = findViewById(R.id.img_buy_selector);
        sellSelector = findViewById(R.id.img_sell_selector);
        businessUseSelector = findViewById(R.id.img_business_use_selector);
        selfUseSelector = findViewById(R.id.img_self_use_selector);
        buySelector = findViewById(R.id.img_buy_selector);
        sellSelector = findViewById(R.id.img_sell_selector);

        listBusinessDemand = findViewById(R.id.listBusinesDemand);
        listProfessionalDemand = findViewById(R.id.listProfDemand);
        listViewBusinessType = findViewById(R.id.listBusines);

        llBusinessType = findViewById(R.id.linearBusiness);
        ProfessionalLayout = findViewById(R.id.linearBusinessProf);
        NeedLayout = findViewById(R.id.linearnedd);
        linearTransaction = findViewById(R.id.linearTranscation);
        linearLayoutDemand = findViewById(R.id.linearBusinessDemand);
        linearLayoutBusinessType = findViewById(R.id.layout_business_type);

        btnSubmit = findViewById(R.id.bt_submit);
     /*   ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
    */
    }

    private void initializeScreenHeightWidth() {
        height = getScreenHeight(OnetoOneActivity.this);
        width = getScreenWidth(OnetoOneActivity.this);
    }

    private void initializeSharedData() {
        prefManager = new PrefManager(this);
        Token = prefManager.getToken().get(TOKEN);
        mCompressor = new FileCompressor(this);

        intentFilter = new IntentFilter();
        connectivityReceiver = new ConnectivityReceiver();
        CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
    }

    private void initializeClickListener() {
        edSelectProduct.setOnClickListener(this);
        edSelectState.setOnClickListener(this);
        edCity.setOnClickListener(this);
        edSelectCustomer.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        professionalCheck.setOnClickListener(this);
        businessOwnerCheck.setOnClickListener(this);

//        chReselling.setOnCheckedChangeListener(this);
        chHome.setOnClickListener(this);
        chBusiness.setOnClickListener(this);
        chBuy.setOnClickListener(this);
        chSell.setOnClickListener(this);
        imgBack.setOnClickListener((v) -> {
            onBackPressed();
            finish();
        });
    }

    @Override
    protected void onResume() {
        if (key == 1) {
            edCity.setText(prefManager.getsaveLoginDetails().get(CITY_NAME));
            edSelectState.setText(prefManager.getsaveLoginDetails().get(STATE_NAME));
        }
        super.onResume();
    }

    private void GetBusinessDemand() {
        //  ProgressDialog progressDialog = createProgressDialog(OnetoOneActivity.this);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<BusinessDemand>> call = customer_interface.GetBusinessDemand("bearer " + Token);
        call.enqueue(new Callback<List<BusinessDemand>>() {
            @Override
            public void onResponse(Call<List<BusinessDemand>> call, Response<List<BusinessDemand>> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        //  progressDialog.dismiss();
                        businessDemandList = response.body();
                        businessDemandAdaper = new BusinessDemandAdaper(OnetoOneActivity.this, businessDemandList);
                        break;

                    case 404:
                        //  progressDialog.dismiss();
                        break;

                    case 500:
                        CustomToast.showToast(OnetoOneActivity.this, "Server Error");
                        //  progressDialog.dismiss();
                        break;

                    case 401:
                        // progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(OnetoOneActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<BusinessDemand>> call, Throwable t) {
                //  progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(OnetoOneActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(OnetoOneActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void GetProfessionalRequirements() {
        //  ProgressDialog progressDialog = createProgressDialog(OnetoOneActivity.this);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<ProfessionalReqModel>> call = customer_interface.GetProfessionalsReq("bearer " + Token);
        call.enqueue(new Callback<List<ProfessionalReqModel>>() {
            @Override
            public void onResponse(Call<List<ProfessionalReqModel>> call, Response<List<ProfessionalReqModel>> response) {
                int Status_code = response.code();
                switch (Status_code) {
                    case 200:
                        //   progressDialog.dismiss();
                        professionalReqModels = response.body();
                        professionalReqAdapter = new ProfessionalReqAdapter(OnetoOneActivity.this, professionalReqModels, OnetoOneActivity.this);
                        listProfessionalDemand.setAdapter(professionalReqAdapter);
                        professionalReqAdapter.notifyDataSetChanged();
                        break;

                    case 404:
                        //  progressDialog.dismiss();
                        break;

                    case 500:
                        CustomToast.showToast(OnetoOneActivity.this, "Server Error");
                        //  progressDialog.dismiss();
                        break;

                    case 401:
                        //  progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(OnetoOneActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<ProfessionalReqModel>> call, Throwable t) {
                // progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(OnetoOneActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(OnetoOneActivity.this, "Bad response from server.. Try again later ");
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
        btnSubmitCity = (Button) mDialogCitySelect.findViewById(R.id.btn_city_submit);
        AppCompatImageView Clear_txt_btn1 = mDialogCitySelect.findViewById(R.id.clear_txt_Prise);
        ImageView imageView3 = (ImageView) mDialogCitySelect.findViewById(R.id.cancel_childcategory);
        cityChipScroll = mDialogCitySelect.findViewById(R.id.chipScrollView);
        recyclerViewCity = mDialogCitySelect.findViewById(R.id.recyclerCity);
        recyclerViewCity.setHasFixedSize(true);
        recyclerViewCity.setLayoutManager(new LinearLayoutManager(OnetoOneActivity.this));

        mDialogCitySelect.show();

        btnSubmitCity.setOnClickListener(v -> {
            if (selectedCityList.size() == 0) {
                Toast.makeText(this, "Please select city first.", Toast.LENGTH_SHORT).show();
            } else if (selectedCityList.size() < 4) {
                DialogUtilsKt.confirm(
                        OnetoOneActivity.this,
                        getString(R.string.multiple_city_selection),
                        getString(R.string.yes),
                        getString(R.string.no),
                        null,
                        view -> updateUIForSelectedCities()
                );
            } else updateUIForSelectedCities();
        });

        imageView3.setOnClickListener(v -> mDialogCitySelect.dismiss());
        edSelectedCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {

                    String cityName = s.toString();
                    if (s.length() <= 0) {
                        Clear_txt_btn1.setVisibility(View.GONE);
                    } else if (cityName.length() == 3) {
                        if (ConnectivityReceiver.isConnected()) {
                            getCitySpinnerFromServer(StateID, cityName);
                        } else {
                            Toast.makeText(getApplicationContext(), "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
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
        Clear_txt_btn1.setOnClickListener(view -> {
            if (!edSelectedCity.getText().toString().isEmpty()) {
                edSelectedCity.getText().clear();
                Clear_txt_btn1.setVisibility(View.GONE);

            }
        });

    }


    private void getCitySpinnerFromServer(int stateID, String cityName) {
        //ProgressDialog progressDialog = customProgressDialog(OnetoOneActivity.this, "Loading cities please wait..");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<City>> call = customer_interface.getAllCity(/*"bearer " + Token, */stateID, cityName);
        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                int statusCode = response.code();
                Log.e("status", "code...." + statusCode);
                switch (statusCode) {
                    case 200:
                        //progressDialog.dismiss();
                        cityList = new ArrayList<>(response.body());
                        Log.e("City", "count........." + cityList);
                            // OpenDialogToSelectCity();
                            if (selectedCityList != null) {
                                for (int i = 0; i < cityList.size(); i++) {
                                    if (selectedCityList.contains(cityList.get(i))) {
                                        cityList.get(i).setChecked(true);
                                    }
                                }
                            }
                            cityAdapter = new CityAdapter(OnetoOneActivity.this, cityList, OnetoOneActivity.this);
                            recyclerViewCity.setAdapter(cityAdapter);
                            cityAdapter.notifyDataSetChanged();

                        break;

                    case 404:
                        CustomToast.showToast(OnetoOneActivity.this, "No city found");
                        //progressDialog.dismiss();
                        break;

                    case 500:
                        //progressDialog.dismiss();
                        CustomToast.showToast(OnetoOneActivity.this, "Server Error");
                        break;

                    case 401:
                        //progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(OnetoOneActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                //progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(OnetoOneActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(OnetoOneActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void updateUIForSelectedCities() {
        if (selectedCityList == null) return;
        if (selectedCityList.size()>4) {
            CustomToast.showToast(OnetoOneActivity.this, "You can select only 4 cities");
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
        edSelectedCity.setCursorVisible(false);
        mDialogCitySelect.dismiss();
        if(chipGroupCity.getChildCount()>0){
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
//                CustomToast.showToast(OnetoOneActivity.this, "You can select only 4 cities");
//            }
        } else hsvCity.setVisibility(View.GONE);
    }

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
        }
        Log.e("City", "Selected ID.." + CityId);
        showChipsForSelectedCities(city);
//        edCity.setText("" + city.getVillageLocalityname());
//        edSelectedCity.setCursorVisible(false);
//        mDialogCitySelect.dismiss();
    }

    private void showChipsForSelectedCities(City city) {
        cityChipScroll.setVisibility(View.VISIBLE);

        ChipGroup chipGroupSearchDialog = mDialogCitySelect.findViewById(R.id.chipgroup);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bownerCk:
                if (ProductID == 0)
                    CustomToast.showToast(OnetoOneActivity.this, "Please select product");
                else if (!isBusinessTypeSelected) {
                    for (int i = 0; i < businessDemandList.size(); i++) {
                        businessDemandList.get(i).setChecked(false);
                    }
                    businessDemandAdaper = new BusinessDemandAdaper(this, businessDemandList);
                    listBusinessDemand.setAdapter(businessDemandAdaper);
                    updateUIForBusinessOwner();
                } else {
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

                            llBusinessType.setVisibility(View.VISIBLE);
                            ProfessionalLayout.setVisibility(View.GONE);
                            ProductID = 0;
                            updateUIForDeselection();
                        }, null);
                    }
                }
                break;

            case R.id.professionalCk:
                if (ProductID == 0)
                    CustomToast.showToast(OnetoOneActivity.this, "Please select service");
                else if (!isBusinessTypeSelected) {
                    for (int i = 0; i < businessDemandList.size(); i++) {
                        businessDemandList.get(0).setChecked(false);
                    }
                    businessDemandAdaper = new BusinessDemandAdaper(this, businessDemandList);
                    listBusinessDemand.setAdapter(businessDemandAdaper);
                    updateUIForProfessional();
                } else {
                    if (!isProfessional) {
                        DialogUtilsKt.confirm(this, getString(R.string.do_you_really_want_to_switch), getString(R.string.yes), getString(R.string.no), v1 -> {
                            edSelectProduct.setText("");
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

                            llBusinessType.setVisibility(View.GONE);
                            ProfessionalLayout.setVisibility(View.VISIBLE);
                            ProductID = 0;
                            updateUIForDeselection();
                        }, null);
                    }
                }
                break;

            case R.id.ckBuy:
                selectedTranscationType = "Buy";
                updateUIForBuy();
                break;

            case R.id.ckSell:
                selectedTranscationType = "Sell";
                updateUIForSell();
                break;

            case R.id.ckId4:
                selectCheckBoxPurpose = "Business";
                updateUIForBusinessUse();
                break;

            case R.id.ckId6:
                selectCheckBoxPurpose = "Self Use";
                updateUIForSelfUse();
                break;

            case R.id.ed_select_product:
                if (ConnectivityReceiver.isConnected()) {
                    //getChildCategoryListFromServer("ply");


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
                        edProduct1.setHint(R.string.text_search_service);
                    } else {
                        edProduct1.setHint(R.string.text_serach_product_service);
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
                    childProductAdapter = new OneToOneProductAdapter(OnetoOneActivity.this, childCategoryProducts, OnetoOneActivity.this);
                    //getChildCategoryListFromServer();
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
                                CustomToast.showToast(OnetoOneActivity.this, "Enter product/service name");
                            } else {
                                if (ConnectivityReceiver.isConnected()) {
                                    getChildCategoryListFromServer(edProduct1.getText().toString().trim());
                                } else {
                                    Toast.makeText(OnetoOneActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
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
                            try {
                                childProductAdapter.getFilter().filter(s);
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
                                childProductAdapter.getFilter().filter(s);

                                if (s.length() <= 0) {

                                    Clear_txt_btn.setVisibility(View.GONE);
                                } else {
                                    Clear_txt_btn.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (edProduct1.getText().toString().trim().length() < 3) return;
                            if (ConnectivityReceiver.isConnected()) {
                                getChildCategoryListFromServer(edProduct1.getText().toString().trim());
                            } else {
                                Toast.makeText(OnetoOneActivity.this, "Not connected to internet", Toast.LENGTH_SHORT).show();
                                //showConnectionError();
                            }
                        }
                    });

                } else {
                    showError();
                }
                break;

            case R.id.select_state:
                if (ConnectivityReceiver.isConnected()) {
                    getStateListFromServer();
                } else {
                    showError();
                }
                break;

            case R.id.ed_select_city:
                if (StateID == 0) {
                    CustomToast.showToast(OnetoOneActivity.this, "Please select state");
                } else {
                    if (ConnectivityReceiver.isConnected()) {
                        OpenDialogToSelectCity();
                    } else {
                        showError();
                    }
                }
                break;

            case R.id.select_Customer:

                if (isProfessional || !selectedBusinessType.isEmpty()) {
                    if (ProductID == 0) {
                        CustomToast.showToast(OnetoOneActivity.this, "Please select product");
                    } else if (StateID == 0) {
                        CustomToast.showToast(OnetoOneActivity.this, "Please select state");
                    } else if (CityId == 0) {
                        CustomToast.showToast(OnetoOneActivity.this, "Please select city");
                    } else {
                        if (ConnectivityReceiver.isConnected()) {
                            getCustomerList();
                        } else {
                            showError();
                        }
                    }
                } else {
                    CustomToast.showToast(OnetoOneActivity.this, "Please select business type");
                }

                break;

            case R.id.submit_customer:
                hsv.setVisibility(View.VISIBLE);
                for (int i = 0; i < customerSelectorAdapter.getResultList().size(); i++) {
                    CustomerModel customerModel = customerSelectorAdapter.getResultList().get(i);
                    customerSelectedModels.add(new CustomerModel(customerModel.getCustID(), customerModel.getFirmName(), customerModel.getCityID()));
                }
                LinkedHashSet<CustomerModel> listRepatedArryList = new LinkedHashSet<CustomerModel>();
                listRepatedArryList.addAll(customerSelectedModels);
                customerSelectedModels.clear();
                customerSelectedModels.addAll(listRepatedArryList);
                mDialogSelectCustomer.dismiss();
                if (customerSelectedModels.size() != 0 | !customerSelectedModels.isEmpty()) {
                    callChip(customerSelectedModels);
                }
                break;


            case R.id.bt_submit:
                selectedBusinessList.clear();
                if (!Validatestate() | !ValidateCity() | !ValidateProduct()) {
                    return;
                } else if (key == 1) {
                    String str = edRequirement.getText().toString().trim();
                    if (!str.startsWith(" ") && !str.isEmpty()) {
                        takePermissionToSendPicture();
                    } else {
                        CustomToast.showToast(OnetoOneActivity.this, "Brief your requirement");
                    }
                } else {
                    if(businessTypeAdapter == null){
                        businessTypeAdapter = new BusinessTypeAdapter(OnetoOneActivity.this, businessTypeList, OnetoOneActivity.this);
                        listViewBusinessType.setAdapter(businessTypeAdapter);
                        businessTypeAdapter.notifyDataSetChanged();
                    }
                    businessTypeAdapter.shouldSelectProfessional(isProfessional);
                    selectedBusinessList = businessTypeAdapter.getTrueResultList();
                    if (isProfessional || (businessTypeAdapter.getTrueResultList().size() > 0 && !businessTypeAdapter.getResultList().isEmpty())) {
                        if (isProfessional) {
                            if (professionalReqAdapter.GetselectedProfBusinessDemand() != 0) {
                                productReqID = professionalReqAdapter.GetselectedProfBusinessDemand();
                                DemandID = 0;
                                selectCheckBoxPurpose = "";
                                selectedTranscationType = "";
                                String str = edRequirement.getText().toString().trim();
                                if (!finalCustomerSelectedModels.isEmpty() | finalCustomerSelectedModels.size() != 0) {
                                    if (!str.startsWith(" ") && !str.isEmpty()) {
                                        takePermissionToSendPicture();
                                    } else {
                                        CustomToast.showToast(OnetoOneActivity.this, "Brief your requirement");
                                    }
                                } else {
                                    CustomToast.showToast(OnetoOneActivity.this, "Please select customers");
                                }

                            } else {
                                CustomToast.showToast(OnetoOneActivity.this, "Please select Professional Business demand");
                            }
                        } else {
                            if (isBuying || isSelling) {
                                if (businessDemandAdaper.GetselectedBusinessDemand() != 0) {
                                    DemandID = businessDemandAdaper.GetselectedBusinessDemand();
                                    if (/*chReselling.isChecked() || */isSelfUse || isBusinessUse) {
                                        if (!finalCustomerSelectedModels.isEmpty() | finalCustomerSelectedModels.size() != 0) {
                                            String str = edRequirement.getText().toString().trim();
                                            if (!str.startsWith(" ") && !str.isEmpty()) {
                                                takePermissionToSendPicture();
                                            } else {
                                                CustomToast.showToast(OnetoOneActivity.this, "Brief your requirement");
                                            }
                                        } else {
                                            CustomToast.showToast(OnetoOneActivity.this, "Please select customers");
                                        }

                                    } else {
                                        CustomToast.showToast(OnetoOneActivity.this, "Please select Business Purpose");
                                    }
                                } else {
                                    CustomToast.showToast(OnetoOneActivity.this, "Please select Business Demand");
                                }
                            } else {
                                CustomToast.showToast(OnetoOneActivity.this, "Please select Transaction Type");
                            }
                        }
                    } else {
                        CustomToast.showToast(OnetoOneActivity.this, "Please select Business Type");
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
                if (mPhotoFile1 == null && mPhotoFile2 == null) {
                    CustomToast.showToast(OnetoOneActivity.this, "Please select image");
                } else {

                    if (ConnectivityReceiver.isConnected()) {
                        SubmitQuery submitQuery = new SubmitQuery(0,
                                custIDSList, CustID,
                                CityId,
                                ProductID,
                                selectedBusinessList,
                                DemandID,
                                selectCheckBoxPurpose,
                                edRequirement.getText().toString(),
                                productReqID, false, "Group Enquiry", selectedTranscationType);

                        SendEnquiryWithImageToServer(submitQuery);
                        mdialogPhoto.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.btnPhoto1:
                mdialogPhoto.dismiss();
                break;


        }
    }

    private void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    private void callChip(List<CustomerModel> customerSelectedModels) {
        if (chipGroupDemo.getChildCount() > 0) {
            chipGroupDemo.removeAllViews();
        }

        List<CustomerModel> withoutDuplicate = new ArrayList<>();
        int i = 0;

        for (CustomerModel customerModel : customerSelectedModels) {
            if (i == 0) {
                withoutDuplicate.add(customerModel);
            } else {
                int k = 0;
                for (CustomerModel cat : withoutDuplicate) {
                    if (cat.getCustID() == customerModel.getCustID()) {
                        k = 1;
                        break;
                    }
                }
                if (k != 1) {
                    withoutDuplicate.add(customerModel);
                }
            }
            i++;
        }

        for (CustomerModel category : withoutDuplicate) {
            Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.chip_one, chipGroupDemo, false);
            mChip.findViewById(R.id.chips);
            mChip.setText(category.getFirmName());
            Chip finalChip = mChip;
            mChip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    category.setChecked(false);
                    //customerSelectedModels.remove(category);
                    chipGroupDemo.removeView(finalChip);
                }
            });
            chipGroupDemo.addView(mChip);
        }
        finalCustomerSelectedModels = withoutDuplicate;
        for (int j = 0; j < finalCustomerSelectedModels.size(); j++) {
//            for (City city : selectedCityList) {
                custIDSList.add(new CustIDS(finalCustomerSelectedModels.get(j).getCustID(), finalCustomerSelectedModels.get(j).getCityID()));
//            }
        }
        //Log.e("Total", "count...." + withoutDuplicate.toString());
        //Log.e("Total", "ids...." + custIDSList.toString());
    }

    private void openDialogToSelectCustomer() {
        mDialogSelectCustomer = new Dialog(this);
        mDialogSelectCustomer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setting the dialog height and width to screen size
        mDialogSelectCustomer.getWindow().setLayout(width, height);
        mDialogSelectCustomer.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        mDialogSelectCustomer.setContentView(R.layout.customer_layout_dialog);
        mDialogSelectCustomer.setCancelable(true);
        //set layout height and width to its screen size
        LinearLayout linearLayout = mDialogSelectCustomer.findViewById(R.id.mainLayout);
        CheckBox selectAll = mDialogSelectCustomer.findViewById(R.id.selectAll);
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.height = height;
        params.width = width;
        linearLayout.setLayoutParams(params);
        EditText edDialogCustomer = (EditText) mDialogSelectCustomer.findViewById(R.id.edCustomer);
        AppCompatImageView Clear_txt_btn = mDialogSelectCustomer.findViewById(R.id.clear_txt_Prise);
        BtnCustomerSubmit = (Button) mDialogSelectCustomer.findViewById(R.id.submit_customer);
        ImageView imageView = (ImageView) mDialogSelectCustomer.findViewById(R.id.cancel_category);
        Button filterBtn = (Button) mDialogSelectCustomer.findViewById(R.id.filter);
        recyclerViewCustomer = (RecyclerView) mDialogSelectCustomer.findViewById(R.id.recyclerCategory);
        recyclerViewCustomer.setHasFixedSize(true);
        recyclerViewCustomer.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ViewGroup.LayoutParams params1 = recyclerViewCustomer.getLayoutParams();
        params1.height = height - 280;
        recyclerViewCustomer.setLayoutParams(params1);

        customerSelectorAdapter = new CustomerSelectorAdapter(OnetoOneActivity.this, customerModels, OnetoOneActivity.this);

        recyclerViewCustomer.setAdapter(customerSelectorAdapter);
        mDialogSelectCustomer.show();
        BtnCustomerSubmit.setOnClickListener(this);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectAll.isChecked()) {
                    customerSelectorAdapter.selectAll();
                } else {
                    customerSelectorAdapter.DeselectAll();
                }
            }
        });
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStateFilterList();
                getBusinessTypeFilterList();
                getCityFilterList();
                Dialog mDialog = new Dialog(OnetoOneActivity.this);
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialog.setContentView(R.layout.filter_inbox);
                mDialog.setCancelable(true);
                lv = (ListView) mDialog.findViewById(R.id.listView1);
                lv.setOnItemClickListener(OnetoOneActivity.this::onItemClick);
                //lv.setAdapter(filterModel);
                lv.setAdapter(new ArrayAdapter<String>(OnetoOneActivity.this, android.R.layout.simple_list_item_1, filterModel));                //lv.setOnItemClickListener(this);
                lv2 = (ListView) mDialog.findViewById(R.id.listView2);
                ApplyFilter = mDialog.findViewById(R.id.Apply);
                ApplyFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (ConnectivityReceiver.isConnected()) {
                                if (businessTypeAdapter.getTrueResultList().isEmpty() | businessTypeAdapter.getTrueResultList() == null &&
                                        filterCityAdapter.GetSelectedCity().isEmpty() | filterCityAdapter.GetSelectedCity() == null &&
                                        filterStateAdapter.GetSelectedState().isEmpty() | filterStateAdapter.GetSelectedState() == null
                                ) {
                                    Toast.makeText(OnetoOneActivity.this, "Please select at least one type", Toast.LENGTH_SHORT).show();
                                } else {

                                    // CityId, StateID
                                    //to get the checked business type
                                    businessTypeList = businessTypeAdapter.getResultList();
                                    if (businessTypeList.size() >= 1) {
                                        for (int k = 0; k < businessTypeList.size(); k++) {
                                            if (businessTypeList.get(k).isChecked()) {
                                                BusinessTypeID = businessTypeList.get(k).getBusinessTypeID();
                                            }
                                        }
                                    }
                                    //to get the checked city list
                                    cityList = filterCityAdapter.GetResultCity();
                                    if (cityList.size() >= 1) {
                                        for (int i = 0; i < cityList.size(); i++) {
                                            if (cityList.get(i).isChecked()) {
                                                CityId = cityList.get(i).getStatewithCityID();
                                            }
                                        }
                                    }

                                    stateList = filterStateAdapter.GetResultState();
                                    if (stateList.size() >= 1) {
                                        for (int i = 0; i < stateList.size(); i++) {
                                            if (stateList.get(i).isChecked()) {
                                                StateID = stateList.get(i).getStateID();
                                            }
                                        }
                                    }

                                    //to get the checked business demand list
                                    int businessDemand = businessDemandAdaper.GetselectedBusinessDemand();
                                    //getCustomerList();
                                    getFilterCustomerList();
                                    //ApplyFilterMethod(businessTypes, businessDemand, getCityList, ConvertedFromDate, ConvertedToDate);
                                    mDialog.dismiss();
                                    mDialog.cancel();
                                    // progressDialog.dismiss();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });


//                DateFilterLayout = mDialog.findViewById(R.id.DateLayout);
//                SelectFromDate = mDialog.findViewById(R.id.FromdateSelect);
//                SelectToDate = mDialog.findViewById(R.id.TodateSelect);
//                SelectFromDate.setOnClickListener(this);
//                SelectToDate.setOnClickListener(this);
                mDialog.show();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogSelectCustomer.dismiss();
            }
        });

        Clear_txt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edDialogCustomer.getText().toString().isEmpty()) {
                    edDialogCustomer.getText().clear();
                    Clear_txt_btn.setVisibility(View.GONE);
                }

            }
        });
        edDialogCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    customerSelectorAdapter.getFilter().filter(s);
                    stateSelectorAdapter.getFilter().filter(s);
                    if (s.length() <= 0) {
                        Clear_txt_btn.setVisibility(View.GONE);
                        selectAll.setChecked(false);
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
                    customerSelectorAdapter.getFilter().filter(s);
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

    //@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            if (stateListt.size() != 0) {
                filterStateAdapter = new StateFilterAdapter(OnetoOneActivity.this, stateListt);
                lv2.setAdapter(filterStateAdapter);
                filterStateAdapter.notifyDataSetChanged();
            }

//            lv2.setAdapter((ListAdapter) stateSelectorFilterAdapter.getFilter());

        } else if (position == 1) {
            if (cityListt.size() != 0) {
                filterCityAdapter = new OneFilterCityAdapter(OnetoOneActivity.this, cityListt);
                lv2.setAdapter(filterCityAdapter);
                filterCityAdapter.notifyDataSetChanged();
            }

        } else if (position == 2) {

            if (businessTypeListt.size() != 0) {
                businessTypeAdapterr = new com.mwbtech.dealer_register.Adapter.FdChild(OnetoOneActivity.this, businessTypeListt);
                lv2.setAdapter(businessTypeAdapter);
                businessTypeAdapter.notifyDataSetChanged();
            }
        }
    }

    private void getCustomerList() {
        // ProgressDialog progressDialog = createProgressDialog(OnetoOneActivity.this);
        CustomerModel customerModel;
        if(isProfessional) {
            businessTypeAdapter.shouldSelectProfessional(true);
            customerModel = new CustomerModel(CustID, CityId, StateID, ProductID, businessTypeAdapter.getTrueResultList());
        } else
            customerModel = new CustomerModel(CustID, CityId, StateID, ProductID, selectedBusinessType1);
        Log.e("Selected Types...", selectedBusinessType1.toString());
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<CustomerModel>> call = customer_interface.GetCustomerList("bearer " + Token, customerModel);
        Gson gson = new Gson();
        String json = gson.toJson(customerModel);
        Log.e("customerList...", json);
        call.enqueue(new Callback<List<CustomerModel>>() {
            @Override
            public void onResponse(Call<List<CustomerModel>> call, Response<List<CustomerModel>> response) {
                int statusCode = response.code();
                Log.e("CustomerList...", String.valueOf(statusCode));
                switch (statusCode) {
                    case 200:
                        //progressDialog.dismiss();
                        customerModels = response.body();
                        StringBuilder sb = new StringBuilder();
                        for (CustomerModel CM : customerModels) {
                            Log.e(".......", CM.toString());
                            if (CM.getBusinessTypeID() != null) {
                                for (BusinessType BT : CM.getBusinessTypeID()) {
                                    //CM.getBusinessTypeForFilter().concat(BT.getNameOfBusiness()+",");
                                    sb.append(BT.getNameOfBusiness() + ",");
                                    CM.setBusinessTypeForFilter(sb.toString());
                                    Log.e("ListedTypes....", CM.getBusinessTypeForFilter());
                                }
                            }
                        }
                        if (customerModels.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Result not found for your requirement", Toast.LENGTH_LONG).show();
                            //BtnCustomerSubmit.setVisibility(View.GONE);
                        } else {
                            openDialogToSelectCustomer();
                            BtnCustomerSubmit.setVisibility(View.VISIBLE);
                        }
                        break;

                    case 404:
                        //progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Result not found for your requirement", Toast.LENGTH_LONG).show();
                        break;


                    case 500:
                        CustomToast.showToast(OnetoOneActivity.this, "Server Error");
                        //  progressDialog.dismiss();
                        break;

                    case 401:
                        //  progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(OnetoOneActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<CustomerModel>> call, Throwable t) {
                //progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(OnetoOneActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(OnetoOneActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });

    }

    private void getFilterCustomerList() {
        // ProgressDialog progressDialog = createProgressDialog(OnetoOneActivity.this);
        CustomerModel customerModel = new CustomerModel(CustID, CityId, StateID, selectedBusinessType);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Gson gson = new Gson();
        String json = gson.toJson(customerModel);
        Log.e("getFilteredList...", json);
        Call<List<CustomerModel>> call = customer_interface.FilterEnquiryList("bearer " + Token, customerModel);
        call.enqueue(new Callback<List<CustomerModel>>() {
            @Override
            public void onResponse(Call<List<CustomerModel>> call, Response<List<CustomerModel>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        //progressDialog.dismiss();
                        // customerSelectorAdapter = response.body();
                        customerModels = response.body();
                        if (customerModels.isEmpty()) {
                            BtnCustomerSubmit.setVisibility(View.GONE);
                        } else {
                            openDialogToSelectCustomer();
                            BtnCustomerSubmit.setVisibility(View.VISIBLE);
                        }
                        break;

                    case 404:
                        //  progressDialog.dismiss();
                        break;


                    case 500:
                        CustomToast.showToast(OnetoOneActivity.this, "Server Error");
                        //  progressDialog.dismiss();
                        break;

                    case 401:
                        //  progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(OnetoOneActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<CustomerModel>> call, Throwable t) {
                //progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(OnetoOneActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(OnetoOneActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });

    }

    private void getStateFilterList() {
        // ProgressDialog progressDialog = createProgressDialog(OnetoOneActivity.this);
        CustomerModel customerModel = new CustomerModel(CustID);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<State>> call = customer_interface.GetLoadEnquiryStatelistOnePlus("bearer " + Token, customerModel);
        call.enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        // progressDialog.dismiss();
                        //customerModels = response.body();
                        stateListt = response.body();
                        break;

                    case 404:
                        // progressDialog.dismiss();
                        break;


                    case 500:
                        CustomToast.showToast(OnetoOneActivity.this, "Server Error");
                        //  progressDialog.dismiss();
                        break;

                    case 401:
                        //  progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(OnetoOneActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                //progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(OnetoOneActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(OnetoOneActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void getBusinessTypeFilterList() {
        // ProgressDialog progressDialog = createProgressDialog(OnetoOneActivity.this);
        CustomerModel customerModel = new CustomerModel(CustID);
        Gson gson = new Gson();
        String json = gson.toJson(customerModel);
        Log.e("btypes", json);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<BusinessType>> call = customer_interface.GetBusinessTypelistFor_OnePlus("bearer " + Token, customerModel);
        call.enqueue(new Callback<List<BusinessType>>() {
            @Override
            public void onResponse(Call<List<BusinessType>> call, Response<List<BusinessType>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        // progressDialog.dismiss();
                        //customerModels = response.body();
                        businessTypeListt = response.body();
                        businessTypeList = response.body();
                        break;

                    case 404:
                        //  progressDialog.dismiss();
                        break;


                    case 500:
                        CustomToast.showToast(OnetoOneActivity.this, "Server Error");
                        //  progressDialog.dismiss();
                        break;

                    case 401:
                        //   progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(OnetoOneActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<BusinessType>> call, Throwable t) {
                //  progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(OnetoOneActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(OnetoOneActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });

    }

    private void getCityFilterList() {
        // ProgressDialog progressDialog = createProgressDialog(OnetoOneActivity.this);
        CustomerModel customerModel = new CustomerModel(CustID);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<City>> call = customer_interface.GetLoadEnquiry_CitylistOnePlus("bearer " + Token, customerModel);
        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        //  progressDialog.dismiss();
                        //customerModels = response.body();
                        cityListt = response.body();
                        break;

                    case 404:
                        //  progressDialog.dismiss();
                        break;


                    case 500:
                        CustomToast.showToast(OnetoOneActivity.this, "Server Error");
                        //  progressDialog.dismiss();
                        break;

                    case 401:
                        //  progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(OnetoOneActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                //  progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(OnetoOneActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(OnetoOneActivity.this, "Bad response from server.. Try again later ");
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
        AppCompatImageView Clear_txt_btn = mDialogStateSelect.findViewById(R.id.clear_txt_Prise);
        ImageView imageView = (ImageView) mDialogStateSelect.findViewById(R.id.cancel_category);

        recyclerViewState = (RecyclerView) mDialogStateSelect.findViewById(R.id.recyclerCategory);
        recyclerViewState.setHasFixedSize(true);
        recyclerViewState.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        stateSelectorAdapter = new StateSelectorAdapter(OnetoOneActivity.this, stateList, OnetoOneActivity.this);
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

    private void getStateListFromServer() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(OnetoOneActivity.this, "Please wait");
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
                            /*stateSelectorAdapter = new StateSelectorAdapter(OnetoOneActivity.this, stateList, OnetoOneActivity.this);
                            recyclerViewState.setAdapter(stateSelectorAdapter);*/

                        }
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;


                    case 500:
                        CustomToast.showToast(OnetoOneActivity.this, "Server Error");
                        progressDialog.dismiss();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(OnetoOneActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(OnetoOneActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(OnetoOneActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }


//    private void getChildCategoryListFromServer() {
//        ProgressDialog progressDialog = createProgressDialog(this);
//        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
//        Call<List<OneToOneProductModel>> call = customer_interface.GetItemList("bearer " + Token, CustID);
//
//        call.enqueue(new Callback<List<OneToOneProductModel>>() {
//            @Override
//            public void onResponse(Call<List<OneToOneProductModel>> call, Response<List<OneToOneProductModel>> response) {
//                int statusCode = response.code();
//                switch (statusCode) {
//                    case 200:
//                        progressDialog.dismiss();
//                        childCategoryProducts = new ArrayList<OneToOneProductModel>(response.body());
//                        if (childCategoryProducts.isEmpty()) {
//                            CustomToast.showToast(OnetoOneActivity.this, "No such product found..");
//                        } else {
//                            openDialogToSelectProduct();
//                            /*childProductAdapter = new OneToOneProductAdapter(OnetoOneActivity.this, childCategoryProducts, OnetoOneActivity.this);
//                            recyclerViewProduct.setAdapter(childProductAdapter);
//                            childProductAdapter.notifyDataSetChanged();*/
//                        }
//
//                        break;
//                    case 404:
//                        progressDialog.dismiss();
//                        CustomToast.showToast(OnetoOneActivity.this, "No such product found.");
//                        break;
//
//                    case 500:
//                        progressDialog.dismiss();
//                        CustomToast.showToast(OnetoOneActivity.this, "Server Error");
//                        break;
//
//                    case 401:
//                        progressDialog.dismiss();
//                        SessionDialog.CallSessionTimeOutDialog(OnetoOneActivity.this);
//                        break;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<OneToOneProductModel>> call, Throwable t) {
//                progressDialog.dismiss();
//                Log.e("failure ", "error..." + t.getMessage());
//                if (t instanceof IOException) {
//                    Toast.makeText(OnetoOneActivity.this, "Time out..", Toast.LENGTH_LONG).show();
//                } else {
//                    CustomToast.showToast(OnetoOneActivity.this, "Bad response from server.. Try again later ");
//                }
//            }
//        });
//    }

    private void getChildCategoryListFromServer(String childName) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(OnetoOneActivity.this, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        // Call<List<ChildCategoryProduct>> call = customer_interface.getChildCategoryProduct("bearer " + Token, childName, false, false);
        Call<List<ChildCategoryProduct>> call = customer_interface.getGESubCategoryChildCategoryList("bearer " + Token, childName);
        //Call<List<ChildCategoryProduct>> call = customer_interface.getChildCategoryProduct("bearer " + Token, childName, isProfessional, false);
        call.enqueue(new Callback<List<ChildCategoryProduct>>() {
            @Override
            public void onResponse(Call<List<ChildCategoryProduct>> call, Response<List<ChildCategoryProduct>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        childCategoryProducts = new ArrayList<ChildCategoryProduct>(response.body());
                        if (childCategoryProducts.isEmpty()) {
                            CustomToast.showToast(OnetoOneActivity.this, "No such product found..");
                        } else {
                            childProductAdapter = new OneToOneProductAdapter(OnetoOneActivity.this, childCategoryProducts, OnetoOneActivity.this);
                            recyclerViewChild.setAdapter(childProductAdapter);
                            childProductAdapter.notifyDataSetChanged();
                        }

                        break;
                    case 404:
                        progressDialog.dismiss();
                        CustomToast.showToast(OnetoOneActivity.this, "No such product found.");
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(OnetoOneActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(OnetoOneActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<ChildCategoryProduct>> call, Throwable t) {
                 progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(OnetoOneActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(OnetoOneActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }


    private void openDialogToSelectProduct() {
        mDialogProduct = new Dialog(this);
        mDialogProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setting the dialog height and width to screen size
        mDialogProduct.getWindow().setLayout(width, height);

        mDialogProduct.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        mDialogProduct.setContentView(R.layout.one_to_ont_dialog_layout);
        mDialogProduct.setCancelable(true);
        //set layout height and width to its screen size
        LinearLayout linearLayout = mDialogProduct.findViewById(R.id.mainLayout);
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.height = height;
        params.width = width;
        linearLayout.setLayoutParams(params);

        edDialogProduct = (EditText) mDialogProduct.findViewById(R.id.edProduct);
        Clear_txt_btn = mDialogProduct.findViewById(R.id.clear_txt_Prise);
        imageView = (ImageView) mDialogProduct.findViewById(R.id.cancel_category);
        recyclerViewProduct = (RecyclerView) mDialogProduct.findViewById(R.id.recyclerCategory);
        recyclerViewProduct.setHasFixedSize(true);
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        childProductAdapter = new OneToOneProductAdapter(OnetoOneActivity.this, childCategoryProducts, OnetoOneActivity.this);
        recyclerViewProduct.setAdapter(childProductAdapter);
        childProductAdapter.notifyDataSetChanged();


        mDialogProduct.show();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogProduct.dismiss();
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


        edDialogProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    childProductAdapter.getFilter().filter(s);
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
                    childProductAdapter.getFilter().filter(s);
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

    private void takePermissionToSendPicture() {
        builder1 = new AlertDialog.Builder(OnetoOneActivity.this).setMessage(R.string.photo);
        builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mdialogPhoto = new Dialog(OnetoOneActivity.this);
                mdialogPhoto.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mdialogPhoto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mdialogPhoto.setContentView(R.layout.dialog_photo);
                mdialogPhoto.setCancelable(false);
                mdialogPhoto.show();
                imgPhoto1 = mdialogPhoto.findViewById(R.id.imag1);
                imgPhoto2 = mdialogPhoto.findViewById(R.id.imag2);
                btnPhoto1 = mdialogPhoto.findViewById(R.id.btnPhoto1);
                btnPhoto = mdialogPhoto.findViewById(R.id.btnPhoto);
                imgPhoto1.setOnClickListener(OnetoOneActivity.this);
                imgPhoto2.setOnClickListener(OnetoOneActivity.this);
                btnPhoto.setOnClickListener(OnetoOneActivity.this);
                btnPhoto1.setOnClickListener(OnetoOneActivity.this);

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (ConnectivityReceiver.isConnected()) {
                    SubmitQuery submitQuery = new SubmitQuery(0,
                            custIDSList, CustID,
                            CityId,
                            ProductID,
                            selectedBusinessList,
                            DemandID,
                            selectCheckBoxPurpose,
                            edRequirement.getText().toString(),
                            productReqID, false, "Group Enquiry", selectedTranscationType);
                    SendEnquiryToServer(submitQuery);
                    dialog.dismiss();
                } else {
                    showError();
                }

            }

        });
        AlertDialog alertDialog = builder1.create();
        alertDialog.show();
    }

    private void SendEnquiryToServer(SubmitQuery submitQuery) {
        Log.e("Send Data", "With out pic....." + submitQuery.toString());
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(OnetoOneActivity.this, "Please wait");
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Sending Requirement.."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        Gson gson = new Gson();
        String json = gson.toJson(submitQuery);
        Log.e("json.......", json);
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
                            CustomToast.showToast(OnetoOneActivity.this, "Enquiry Sent Successfully to "+ custIDSList.size() + " dealers");
                            startActivity(new Intent(OnetoOneActivity.this, DashboardActivity.class)
                                    .putExtra("isNewUser", false));
                            OnetoOneActivity.this.finish();
                            break;

                        case 404:
                            progressDialog.dismiss();
                            break;

                        case 406:
                            progressDialog.dismiss();
                            break;

                        case 500:
                            progressDialog.dismiss();
                            CustomToast.showToast(OnetoOneActivity.this, "Server Error");
                            break;

                        case 401:
                            progressDialog.dismiss();
                            SessionDialog.CallSessionTimeOutDialog(OnetoOneActivity.this);
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
                    Toast.makeText(OnetoOneActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(OnetoOneActivity.this, "Bad response from server.. Try again later ");
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
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(OnetoOneActivity.this, "Please wait");
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Sending Requirement.."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

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
                            CustomToast.showToast(OnetoOneActivity.this, "Enquiry Sent Successfully to "+ custIDSList.size() + " dealers");
                            startActivity(new Intent(OnetoOneActivity.this, DashboardActivity.class)
                                    .putExtra("isNewUser", false));
                            OnetoOneActivity.this.finish();

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
                            CustomToast.showToast(OnetoOneActivity.this, "Server Error");
                            break;

                        case 401:
                            progressDialog.dismiss();
                            SessionDialog.CallSessionTimeOutDialog(OnetoOneActivity.this);
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
                    Toast.makeText(OnetoOneActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(OnetoOneActivity.this, "Bad response from server.. Try again later ");
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

    // state item selected
    @Override
    public void onItemSelected(State state) {
        edSelectState.setText("" + state.getStateName());
        StateID = state.getStateID();
        mDialogStateSelect.dismiss();
        if (StateID != 0) {
            edCity.getText().clear();
            if (selectedCityList != null) selectedCityList.clear();
            if(chipGroupCity != null && chipGroupCity.getChildCount()>0){
                chipGroupCity.removeAllViews();
            }
            CityId = 0;
            edCity.setCursorVisible(true);
            if (ConnectivityReceiver.isConnected()) {
                OpenDialogToSelectCity();
            } else {
                showError();
            }


        }
    }


    //customer select
    @Override
    public void onItemSelected(CustomerModel customerModel) {
        Log.e("Customer", "....." + customerModel.getFirmName());
    }

    private void getBusinessType(int productID) {
        //   ProgressDialog progressDialog = createProgressDialog(this);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<BusinessType>> call = customer_interface.GetBusinessType("bearer " + Token, productID);
        call.enqueue(new Callback<List<BusinessType>>() {
            @Override
            public void onResponse(Call<List<BusinessType>> call, Response<List<BusinessType>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        //  progressDialog.dismiss();
                        businessTypeList = new ArrayList<BusinessType>(response.body());
                        businessTypeAdapter = new BusinessTypeAdapter(OnetoOneActivity.this, businessTypeList, OnetoOneActivity.this);
                        listViewBusinessType.setAdapter(businessTypeAdapter);
                        businessTypeAdapter.notifyDataSetChanged();
                        break;

                    case 404:
                        // progressDialog.dismiss();
                        break;

                    case 500:
                        // progressDialog.dismiss();
                        CustomToast.showToast(OnetoOneActivity.this, "Server Error");
                        break;

                    case 401:
                        // progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(OnetoOneActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<BusinessType>> call, Throwable t) {

            }
        });
    }

    private void showError() {
        Snackbar snackbar = Snackbar
                .make(llParentLayout, "You are not connected to Internet.!", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    //business type item checked
    @Override
    public void TypeOfBusinessClick(int pos, List<BusinessType> types) {
        List<BusinessType> FinalBt = new ArrayList<>();
        for (BusinessType bt : types) {
            if (bt.isChecked()) {
                FinalBt.add(bt);
            }
        }
        selectedBusinessType = FinalBt;
        selectedBusinessType1 = FinalBt;
        /*if (types.get(pos).getNameOfBusiness().equals("Professionals And Services")) {
            Log.e("Types", "pro");
            isProfessional = true;
            linearLayoutDemand.setVisibility(View.GONE);
            NeedLayout.setVisibility(View.GONE);
            linearTransaction.setVisibility(View.GONE);
            ProfessionalLayout.setVisibility(View.VISIBLE);
        } else {*/
        linearLayoutDemand.setVisibility(View.VISIBLE);
        NeedLayout.setVisibility(View.VISIBLE);
        linearTransaction.setVisibility(View.VISIBLE);
        isProfessional = false;
        ProfessionalLayout.setVisibility(View.GONE);
        //brief.setText("Name of Product");
        Log.e("Types", "..............");
//        }
    }

    private void updateUIForBusinessOwner() {
        updateUIForDeselection();
        isBusinessTypeSelected = true;
        linearLayoutDemand.setVisibility(View.VISIBLE);
        NeedLayout.setVisibility(View.VISIBLE);
        linearTransaction.setVisibility(View.VISIBLE);
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
        llBusinessType.setVisibility(View.VISIBLE);
        txtBusinessOwner.setTypeface(null, Typeface.BOLD);
        txtProfessional.setTypeface(null, Typeface.NORMAL);
        getBusinessType(ProductID);
        hsv.setVisibility(View.GONE);
        if (customerSelectedModels != null) {
            customerSelectedModels.clear();
            callChip(customerSelectedModels);
        }
        if (businessDemandList != null) {
            ArrayList<BusinessDemand> demands = new ArrayList<>();
            for (BusinessDemand demand : businessDemandList) {
                demand.setChecked(false);
                demands.add(demand);
            }
            businessDemandList = demands;
            businessDemandAdaper = new BusinessDemandAdaper(OnetoOneActivity.this, businessDemandList);
            listBusinessDemand.setAdapter(businessDemandAdaper);
            businessDemandAdaper.notifyDataSetChanged();
        }
//        chBuy.setChecked(false);
//        chSell.setChecked(false);
//        chReselling.setChecked(false);
//        chHome.setChecked(false);
//        chBusiness.setChecked(false);
        if (professionalReqModels != null) {
            ArrayList<ProfessionalReqModel> requirements = new ArrayList<>();
            for (ProfessionalReqModel reqModel : professionalReqModels) {
                reqModel.setChecked(false);
                requirements.add(reqModel);
            }
            professionalReqModels = requirements;
            professionalReqAdapter = new ProfessionalReqAdapter(OnetoOneActivity.this, professionalReqModels, OnetoOneActivity.this);
            listProfessionalDemand.setAdapter(professionalReqAdapter);
            professionalReqAdapter.notifyDataSetChanged();
        }

//        edSub.setHint("Select product");
    }

    private void updateUIForProfessional() {
        updateUIForDeselection();
        Log.e("Types", "pro");
        isBusinessTypeSelected = true;
        isProfessional = true;
        linearLayoutDemand.setVisibility(View.GONE);
        NeedLayout.setVisibility(View.GONE);
        linearTransaction.setVisibility(View.GONE);
        ProfessionalLayout.setVisibility(View.VISIBLE);
        businessOwnerCheck.setBackgroundResource(0);
        professionalCheck.setBackgroundResource(R.drawable.shape_rectangle_red);
        imgBusinessOwner.setImageResource(R.drawable.ic_business_owner_unselected);
        imgProfessional.setImageResource(R.drawable.ic_professional_selected);
        txtProfessional.setTextColor(getResources().getColor(R.color.white));
        txtBusinessOwner.setTextColor(getResources().getColor(R.color.black));
        llBusinessType.setVisibility(View.GONE);
        txtProfessional.setTypeface(null, Typeface.BOLD);
        txtBusinessOwner.setTypeface(null, Typeface.NORMAL);
        getBusinessType(ProductID);
        hsv.setVisibility(View.GONE);
        if (customerSelectedModels != null) {
            customerSelectedModels.clear();
            callChip(customerSelectedModels);
        }
//        chBuy.setChecked(false);
//        chSell.setChecked(false);
//        chReselling.setChecked(false);
//        chHome.setChecked(false);
//        chBusiness.setChecked(false);
//        edSub.setHint("Select Services");
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
        selectCheckBoxPurpose = "";
        ProfessionalLayout.setVisibility(View.GONE);
        professionalCheck.setBackgroundResource(0);
        businessOwnerCheck.setBackgroundResource(0);
        imgProfessional.setImageResource(R.drawable.ic_professional_unselected);
        imgBusinessOwner.setImageResource(R.drawable.ic_business_owner_unselected);
        txtBusinessOwner.setTextColor(getResources().getColor(R.color.black));
        txtProfessional.setTextColor(getResources().getColor(R.color.black));
        llBusinessType.setVisibility(View.GONE);
        txtBusinessOwner.setTypeface(null, Typeface.NORMAL);
        txtProfessional.setTypeface(null, Typeface.NORMAL);
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

    private boolean Validatestate() {
        String InputName = edSelectState.getText().toString();
        if (InputName.isEmpty()) {
            CustomToast.showToast(OnetoOneActivity.this, "Please select state");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidateCity() {
//        String InputName = edCity.getText().toString();
//        if (InputName.isEmpty()) {
        if(selectedCityList.size()<=0){
            CustomToast.showToast(OnetoOneActivity.this, "Please select city");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidateProduct() {
        String InputName = edSelectProduct.getText().toString();
        if (InputName.isEmpty()) {
            CustomToast.showToast(OnetoOneActivity.this, "Please select product");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        selectedBusinessList.clear();
        switch (buttonView.getId()) {

            /*case R.id.ckId5:
                if (isChecked) {
                    selectCheckBoxPurpose = "Trading";
                    chHome.setChecked(false);
                    chBusiness.setChecked(false);
                    chReselling.setTypeface(null, Typeface.BOLD);
                    chHome.setTypeface(null, Typeface.NORMAL);
                    chBusiness.setTypeface(null, Typeface.NORMAL);
                } else {
                    chHome.setEnabled(true);
                    chBusiness.setEnabled(true);
                    chReselling.setTypeface(null, Typeface.NORMAL);
                    chHome.setTypeface(null, Typeface.NORMAL);
                    chBusiness.setTypeface(null, Typeface.NORMAL);
                }
                break;

            case R.id.ckId6:
                selfUseSelector.setImageResource(isChecked? R.drawable.self_use_selected : R.drawable.self_use_unselected);
                if (isChecked) {
                    selectCheckBoxPurpose = "Home";
                    chReselling.setChecked(false);
                    chBusiness.setChecked(false);
                    chReselling.setTypeface(null, Typeface.NORMAL);
                    chHome.setTypeface(null, Typeface.BOLD);
                    chBusiness.setTypeface(null, Typeface.NORMAL);
                } else {
                    chReselling.setEnabled(true);
                    chBusiness.setEnabled(true);
                    chReselling.setTypeface(null, Typeface.NORMAL);
                    chHome.setTypeface(null, Typeface.NORMAL);
                    chBusiness.setTypeface(null, Typeface.NORMAL);
                }
                break;

            case R.id.ckId4:
                businessUseSelector.setImageResource(isChecked? R.drawable.business_use_selected : R.drawable.business_use_unselected);
                if (isChecked) {
                    selectCheckBoxPurpose = "Business";
                    chHome.setChecked(false);
                    chReselling.setChecked(false);
                    chReselling.setTypeface(null, Typeface.NORMAL);
                    chHome.setTypeface(null, Typeface.NORMAL);
                    chBusiness.setTypeface(null, Typeface.BOLD);
                } else {
                    chHome.setEnabled(true);
                    chReselling.setEnabled(true);
                    chReselling.setTypeface(null, Typeface.NORMAL);
                    chHome.setTypeface(null, Typeface.NORMAL);
                    chBusiness.setTypeface(null, Typeface.NORMAL);
                }
                break;
            case R.id.ckBuy:
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
                    chSell.setTypeface(null, Typeface.BOLD);
                    chBuy.setTypeface(null, Typeface.NORMAL);
                } else {
                    chBuy.setEnabled(true);
                    chBuy.setTypeface(null, Typeface.NORMAL);
                    chSell.setTypeface(null, Typeface.NORMAL);
                }
                break;*/
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OnetoOneActivity.this);
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
                    ProjectUtilsKt.checkCameraPermission(OnetoOneActivity.this, (status -> {
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
                    ProjectUtilsKt.checkCameraPermission(OnetoOneActivity.this, (status -> {
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
                        long len_img = mPhotoFile1.length();
                        if (len_img < image_size) {
                            Picasso.get()
                                    .load(mPhotoFile1)
                                    .into(imgPhoto1);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please select the image within 2MB", Toast.LENGTH_LONG);
                        }
                        // imgPhoto1.setImageBitmap(mPhotoFile1);

                    } else {
                        mPhotoFile2 = mCompressor.compressToFile(mPhotoFile2);
                        //imgPhoto2.setImageBitmap(BitmapFactory.decodeFile(ImagePath1));
                        long len_img = mPhotoFile2.length();
                        if (len_img < image_size) {
                            Picasso.get()
                                    .load(mPhotoFile2)
                                    .into(imgPhoto2);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please select the image within 2MB", Toast.LENGTH_LONG);
                        }

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();
                try {
                    if (imgCount == 0) {

                        if (!TextUtils.isEmpty(getRealPathFromUri(selectedImage))) {
                            mPhotoFile1 = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                            long len_img = mPhotoFile1.length();
                            if (len_img < image_size) {
                                Picasso.get()
                                        .load(mPhotoFile1)
                                        .into(imgPhoto1);
                            } else {
                                Toast.makeText(getApplicationContext(), "Please select the image within 2MB", Toast.LENGTH_LONG);
                            }
                        } else {
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
                            long len_img = mPhotoFile2.length();
                            if (len_img < image_size) {
                                Picasso.get()
                                        .load(mPhotoFile2)
                                        .into(imgPhoto2);
                            } else {
                                Toast.makeText(getApplicationContext(), "Please select the image within 2MB", Toast.LENGTH_LONG);
                            }
                        } else {
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
        OnetoOneActivity.this.finish();
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

//    //item selected
//    @Override
//    public void onItemSelected(OneToOneProductModel childCreation) {
//        ProductID = childCreation.getItemId();
//        edSelectProduct.setText("" + childCreation.getItemName());
//        Log.e("ProductID", "......" + ProductID);
//        edSelectState.setEnabled(true);
//        edSelectState.requestFocus();
//        mDialogProduct.dismiss();
//        getBusinessType(ProductID);
//        llBusinessType.setVisibility(View.VISIBLE);
//        isProfessional = childCreation.isProfessional();
//        if (isProfessional) {
//            ViewGroup.LayoutParams params = llBusinessType.getLayoutParams();
//            params.height = 150;
//            llBusinessType.setLayoutParams(params);
//
//            linearLayoutDemand.setVisibility(View.GONE);
//            NeedLayout.setVisibility(View.GONE);
//            ProfessionalLayout.setVisibility(View.VISIBLE);
//        } else {
//            ViewGroup.LayoutParams params = llBusinessType.getLayoutParams();
//            params.height = 520;
//            llBusinessType.setLayoutParams(params);
//
//            linearLayoutDemand.setVisibility(View.VISIBLE);
//            NeedLayout.setVisibility(View.VISIBLE);
//            ProfessionalLayout.setVisibility(View.GONE);
//        }
//    }

    @Override
    public void onItemSelected(ChildCategoryProduct childCreation) {
        ProductID = childCreation.getChildCategoryId();
        edSelectProduct.setText("" + childCreation.getChildCategoryName());
        Log.e("ProductID", "......" + ProductID);
        edSelectState.setEnabled(true);
        edSelectState.requestFocus();
        mDialogChildCategory.dismiss();
        getBusinessType(ProductID);
        llBusinessType.setVisibility(View.VISIBLE);
        isProfessional = childCreation.isProfessional();
        if (isProfessional) {
            ViewGroup.LayoutParams params = llBusinessType.getLayoutParams();
            //params.height = 150;
            llBusinessType.setLayoutParams(params);
            updateUIForProfessional();

            linearLayoutDemand.setVisibility(View.GONE);
            NeedLayout.setVisibility(View.GONE);
            linearTransaction.setVisibility(View.GONE);
            llBusinessType.setVisibility(View.GONE);
            ProfessionalLayout.setVisibility(View.VISIBLE);
        } else {
            ViewGroup.LayoutParams params = llBusinessType.getLayoutParams();
            //params.height = 620;
            llBusinessType.setLayoutParams(params);
            updateUIForBusinessOwner();
            linearLayoutDemand.setVisibility(View.VISIBLE);
            NeedLayout.setVisibility(View.VISIBLE);
            linearTransaction.setVisibility(View.VISIBLE);
            ProfessionalLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void TypeOfProfClick(int pos, List<ProfessionalReqModel> types) {
        if (types.get(pos).getRequirementName().equals("Admission")) {
            brief.setText("Enter details of the person enquired for");
        } else
            brief.setText("Brief About Your Requirement");
    }

}