package com.mwbtech.dealer_register.Dashboard.Advertisement.QuoteAd;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;
import static com.mwbtech.dealer_register.Utils.Utility.getCalculatedDate;
import static com.mwbtech.dealer_register.Utils.Utility.getScreenHeight;
import static com.mwbtech.dealer_register.Utils.Utility.getScreenWidth;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.AdBrandSelectorAdapter;
import com.mwbtech.dealer_register.Adapter.AdCitySelectorAdapter;
import com.mwbtech.dealer_register.Adapter.AdDistrictSelectorAdapter;
import com.mwbtech.dealer_register.Adapter.AdStateSelectorAdapter;
import com.mwbtech.dealer_register.Adapter.AdvertisementAreaAdapter;
import com.mwbtech.dealer_register.Adapter.AdvertisementTypeAdapter;
import com.mwbtech.dealer_register.Adapter.ChildCatgoryAdapter;
import com.mwbtech.dealer_register.Adapter.TimeSlotAdapter;
import com.mwbtech.dealer_register.Dashboard.Advertisement.AdvertisementMenuActivity;
import com.mwbtech.dealer_register.PojoClass.AdvertisementAreaLevel;
import com.mwbtech.dealer_register.PojoClass.AdvertisementAreaModel;
import com.mwbtech.dealer_register.PojoClass.AdvertisementSlotModel;
import com.mwbtech.dealer_register.PojoClass.AdvertisementTypeModel;
import com.mwbtech.dealer_register.PojoClass.Brand;
import com.mwbtech.dealer_register.PojoClass.ChildCategoryProduct;
import com.mwbtech.dealer_register.PojoClass.CityAD;
import com.mwbtech.dealer_register.PojoClass.District;
import com.mwbtech.dealer_register.PojoClass.Holidays;
import com.mwbtech.dealer_register.PojoClass.NewAdvertisementModule;
import com.mwbtech.dealer_register.PojoClass.State;
import com.mwbtech.dealer_register.PojoClass.TierCity;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.mwbtech.dealer_register.Utils.Utility;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRequestUserActivity extends AppCompatActivity implements AdvertisementAreaAdapter.AdAreaClick, AdvertisementTypeAdapter.AdTypeClick, AdapterView.OnItemSelectedListener, View.OnClickListener, AdStateSelectorAdapter.RecycleitemSelecet, AdDistrictSelectorAdapter.RecycleitemSelecet, AdCitySelectorAdapter.RecycleitemSelecet, ChildCatgoryAdapter.ChildAdapterListener, AdBrandSelectorAdapter.BrandAdapterListener, TimeSlotAdapter.OnSlotSelectedListener {
    private final String ddmmyyyy = "DDMMYYYY";
    private final Calendar cal = Calendar.getInstance();
    EditText edStateName, edDistrictName, edCityName, edSelectFromDate, edSelectToDate, edAdProduct, edBrand, edDialogBrand, edDialogProduct, edFullPageAdDate, edOtherBrand, edTierCity;
    TextView tvShowSelectedState, tvShowSelectedDistrict, tvShowSelectedCity, tvPreviewAd, tvEstimatedPrice, tvDiscPrice, tvTotalPrice, tvInvoiceReportDownload, tvslotSelected, tvslotsSelected, tvTierCity;
    ImageView ivFromCalendar, ivToCalendar, imgBack;
    Dialog mDialogProduct;
    Dialog mDialogBrand;
    Dialog mDialogState, mDialogShowStates;
    Dialog mDialogDistrict, mDialogShowDistrict;
    Dialog mDialogCity, mDialogShowCity;
    Dialog mDialogMsg;
    Spinner spTimeSlots, spIntervals, spTimeSlotFullScreen;
    LinearLayout llMainLayout, llAdDurationFullPage, llAdDuration;
    Button AdSlotBtn, BtnSelectedState, BtnSelectedDistrict, BtnSelectedCity, BtnEstimatePrice;
    GridView listViewAdArea, listViewAdtype;
    RecyclerView recyclerViewState, recyclerViewDistrict, recyclerViewCity, recyclerViewChild, recyclerViewBrand;
    LinearLayout llPriceLayout, llParentLayout;
    ImageView dateicon;
    ChildCatgoryAdapter childProductAdapter;
    AdStateSelectorAdapter adStateSelectorAdapter;
    AdDistrictSelectorAdapter adDistrictSelectorAdapter;
    AdCitySelectorAdapter adCitySelectorAdapter;
    AdvertisementAreaAdapter areaAdapter;
    AdvertisementTypeAdapter adTypeAdapter;
    TimeSlotAdapter timeSlotAdapter;
    AdBrandSelectorAdapter adBrandSelectorAdapter;
    AdvertisementAreaLevel ad, selectedAd;
    int CustID, selectedSlotId, selectedIntervalId, selectedBrandId, selectedAdTypeId, selectedProductId, selectedCategoryProductId, selectedAdAreaId, adMainId;
    int height, width;
    String selectedAdType, selectedAdArea, CurrentDate, selectedBrandName, brandName;
    String Token;
    PrefManager prefManager;
    ChipGroup chipGroupSlot;
    List<ChildCategoryProduct> childCategoryProducts = new ArrayList<>();
    List<State> stateList = new ArrayList<>();
    List<State> selectedStateList = new ArrayList<>();
    List<State> finalSelectedStateList = new ArrayList<>();
    List<District> districtList = new ArrayList<>();
    List<District> selectedDistrictList = new ArrayList<>();
    List<District> finalSelectedDistrictList = new ArrayList<>();
    List<CityAD> cityList = new ArrayList<>();
    List<CityAD> selectedCityList = new ArrayList<>();
    List<CityAD> finalSelectedCityList = new ArrayList<>();
    List<TierCity> tiercityList = new ArrayList<>();
    List<Brand> brandList = new ArrayList<>();
    List<AdvertisementAreaModel> areaModels = new ArrayList<>();
    List<AdvertisementTypeModel> typeModels = new ArrayList<>();
    List<AdvertisementSlotModel> slotModelList = new ArrayList<>();
    List<AdvertisementSlotModel> selectedSlotModelList = new ArrayList<>();
    Holidays holidays;
    int holidayCount;
    String[] intervals = {"3", "6", "9", "12", "15", "18", "21", "24", "27", "30"};
    Customer_Interface customer_interface;
    int mYear, mMonth, mDay;
    SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
    private String current = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }
    }

    private void showContent() {
        setContentView(R.layout.activity_add_request_user);
        initializeViews();
        initializeScreenHeightWidth();
        initializeSharedData();
        initializeClickEvents();
        getHolidayCount();

        getAdvertisementArea();

        getAdvertisementType();

        initializeIntervals();
    }

    private void showNoSignalContent() {
        setContentView(R.layout.no_signal_layout);

        Button tryButton = findViewById(R.id.tryBtn);
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
        llParentLayout = findViewById(R.id.parent);
        edAdProduct = findViewById(R.id.AddProduct);
        edBrand = findViewById(R.id.AddBrand);
        edOtherBrand = findViewById(R.id.OtherBrand);
        edCityName = findViewById(R.id.ed_select_city);
        edTierCity = findViewById(R.id.ed_tier_city);
        tvTierCity = findViewById(R.id.tv_tier_city);
        edDistrictName = findViewById(R.id.ed_select_district);
        tvShowSelectedState = findViewById(R.id.tv_show_state);
        tvShowSelectedDistrict = findViewById(R.id.tv_show_district);
        tvShowSelectedCity = findViewById(R.id.tv_show_city);
        tvPreviewAd = findViewById(R.id.tv_preview);
        edStateName = findViewById(R.id.ed_select_state);
        spTimeSlots = findViewById(R.id.sp_slots);
        spTimeSlotFullScreen = findViewById(R.id.sp_slots1);
        edSelectFromDate = findViewById(R.id.ed_from_date);
        edSelectToDate = findViewById(R.id.ed_to_date);
        edFullPageAdDate = findViewById(R.id.ed_from_date1);
        ivFromCalendar = findViewById(R.id.iv_cal_from);
        ivToCalendar = findViewById(R.id.iv_cal_to);
        llPriceLayout = findViewById(R.id.llMainPrice);
        dateicon = findViewById(R.id.iv_cal_from1);

        spIntervals = findViewById(R.id.sp_intervals);
        AdSlotBtn = findViewById(R.id.bt_add);
        llMainLayout = findViewById(R.id.ll_main);
        llAdDurationFullPage = findViewById(R.id.linearduration1);
        llAdDuration = findViewById(R.id.linearduration);
        listViewAdArea = findViewById(R.id.lv_ad_area);
        chipGroupSlot = findViewById(R.id.chipgroup);
        listViewAdtype = findViewById(R.id.lv_ad_type);
        imgBack = findViewById(R.id.back);

        BtnEstimatePrice = findViewById(R.id.bt_estimate_price);
        tvslotSelected = findViewById(R.id.slotSelected);
        tvslotsSelected = findViewById(R.id.slotSelected1);

        edStateName.setVisibility(View.GONE);
        edDistrictName.setVisibility(View.GONE);
        edCityName.setVisibility(View.GONE);
        edTierCity.setVisibility(View.GONE);
        dateicon.setEnabled(false);

    }

    private void initializeScreenHeightWidth() {
        height = getScreenHeight(AddRequestUserActivity.this);
        width = getScreenWidth(AddRequestUserActivity.this);
    }

    private void initializeSharedData() {
        prefManager = new PrefManager(this);
        try {
            Token = prefManager.getToken().get(TOKEN);
            CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void initializeIntervals() {
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, intervals);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIntervals.setAdapter(adapter1);
    }

    private void initializeCurrentDate() {
        if (holidayCount == 0) {
            CurrentDate = getCalculatedDate(3);
        } else if (holidayCount == 1) {
            CurrentDate = getCalculatedDate(4);
        } else if (holidayCount == 2) {
            CurrentDate = getCalculatedDate(5);
        } else {
            //
        }

    }

    private void initializeClickEvents() {
        edAdProduct.setOnClickListener(this);
        edBrand.setOnClickListener(this);
        edStateName.setOnClickListener(this);
        edDistrictName.setOnClickListener(this);
        edSelectFromDate.setOnClickListener(this);
        edSelectToDate.setOnClickListener(this);
        edCityName.setOnClickListener(this);
        edTierCity.setOnClickListener(this);
        ivFromCalendar.setOnClickListener(this);
        ivToCalendar.setOnClickListener(this);
        dateicon.setOnClickListener(this);
        tvShowSelectedState.setOnClickListener(this);
        tvShowSelectedDistrict.setOnClickListener(this);
        tvShowSelectedCity.setOnClickListener(this);
        tvTierCity.setOnClickListener(this);
        tvPreviewAd.setOnClickListener(this);
        spTimeSlots.setOnItemSelectedListener(this);
        spIntervals.setOnItemSelectedListener(this);
        BtnEstimatePrice.setOnClickListener(this);
        imgBack.setOnClickListener((v) -> {
            onBackPressed();
        });
    }

    private void initializeFromDateTextWatcher(EditText edSelectFromDate) {
        edSelectFromDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon < 1 || mon > 12) {
                            edSelectFromDate.setError("Please enter valid date format(DD-MM-YYYY)");
                        } else if (year < 1900 || year > 2100) {
                            edSelectFromDate.setError("Please enter valid date format(DD-MM-YYYY)");
                        }
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s-%s-%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    edSelectFromDate.setText(current);
                    edSelectFromDate.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getHolidayCount() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Date date = new Date();
        SimpleDateFormat dateFormat4 = new SimpleDateFormat("yyyy-MM-dd");
        String currentDatePass = dateFormat4.format(date);
        System.out.println(dateFormat4.format(date));
        Call<Holidays> call = customer_interface.getHolidays("bearer " + Token, currentDatePass);
        call.enqueue(new Callback<Holidays>() {
            @Override
            public void onResponse(Call<Holidays> call, Response<Holidays> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        holidays = response.body();
                        holidayCount = holidays.getValue();
                        Log.e("notification..", String.valueOf(holidayCount));
                        initializeCurrentDate();
                        break;
                    case 404:
                        break;
                    case 500:
                        CustomToast.showToast(AddRequestUserActivity.this, "Server Error");
                        break;
                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(AddRequestUserActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<Holidays> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(AddRequestUserActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AddRequestUserActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });


    }

    private void initializeToDateTextWatcher(EditText edSelectToDate) {
        edSelectToDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon < 1 || mon > 12) {
                            edSelectToDate.setError("Please enter valid date format(DD-MM-YYYY)");
                        } else if (year < 1900 || year > 2100) {
                            edSelectToDate.setError("Please enter valid date format(DD-MM-YYYY)");
                        }
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s-%s-%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    edSelectToDate.setText(current);
                    edSelectToDate.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initializeFullPageDateTextWatcher(EditText edFullPageAdDate) {
        edFullPageAdDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int i2) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon < 1 || mon > 12) {
                            edFullPageAdDate.setError("Please enter valid date format(DD-MM-YYYY)");
                        } else if (year < 1900 || year > 2100) {
                            edFullPageAdDate.setError("Please enter valid date format(DD-MM-YYYY)");
                        }
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s-%s-%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    edFullPageAdDate.setText(current);
                    edFullPageAdDate.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getAdvertisementArea() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AddRequestUserActivity.this, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<AdvertisementAreaModel>> call = customer_interface.GetAdAreas("bearer " + Token);
        call.enqueue(new Callback<List<AdvertisementAreaModel>>() {
            @Override
            public void onResponse(Call<List<AdvertisementAreaModel>> call, Response<List<AdvertisementAreaModel>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        areaModels = response.body();
                        areaAdapter = new AdvertisementAreaAdapter(AddRequestUserActivity.this, areaModels, AddRequestUserActivity.this);
                        listViewAdArea.setAdapter(areaAdapter);
                        break;
                    case 404:
                        progressDialog.dismiss();
                        break;
                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(AddRequestUserActivity.this, "Server Error");
                        break;
                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AddRequestUserActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<AdvertisementAreaModel>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(AddRequestUserActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AddRequestUserActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void getAdvertisementType() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AddRequestUserActivity.this, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<AdvertisementTypeModel>> call = customer_interface.GetAdType("bearer " + Token);
        call.enqueue(new Callback<List<AdvertisementTypeModel>>() {
            @Override
            public void onResponse(Call<List<AdvertisementTypeModel>> call, Response<List<AdvertisementTypeModel>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        typeModels = response.body();
                        adTypeAdapter = new AdvertisementTypeAdapter(AddRequestUserActivity.this, typeModels, AddRequestUserActivity.this);
                        listViewAdtype.setAdapter(adTypeAdapter);
                        break;
                    case 404:
                        progressDialog.dismiss();
                        break;
                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(AddRequestUserActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AddRequestUserActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<AdvertisementTypeModel>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(AddRequestUserActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AddRequestUserActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void getAdvertisementTimeSlot(Spinner spTimeSlots) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AddRequestUserActivity.this, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<AdvertisementSlotModel>> call = customer_interface.GetTimeSlot("bearer " + Token);
        call.enqueue(new Callback<List<AdvertisementSlotModel>>() {
            @Override
            public void onResponse(Call<List<AdvertisementSlotModel>> call, Response<List<AdvertisementSlotModel>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        slotModelList = response.body();
                        slotModelList.add(0, new AdvertisementSlotModel(-1, ""));
                        timeSlotAdapter = new TimeSlotAdapter(AddRequestUserActivity.this, 0, slotModelList, AddRequestUserActivity.this);
                        spTimeSlots.setAdapter(timeSlotAdapter);
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(AddRequestUserActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AddRequestUserActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<AdvertisementSlotModel>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(AddRequestUserActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AddRequestUserActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void addView() {
        final View slotView = getLayoutInflater().inflate(R.layout.row_add_slot, null, false);
        Button addButton = slotView.findViewById(R.id.btn_add);
        ImageView imageView = slotView.findViewById(R.id.image_remove);
        EditText edFromDate, edToDate;
        ImageView ivFromDate, ivToDate;
        Spinner spSlot;
        edFromDate = slotView.findViewById(R.id.ed_from_date);
        edToDate = slotView.findViewById(R.id.ed_to_date);
        ivFromDate = slotView.findViewById(R.id.iv_from);
        ivToDate = slotView.findViewById(R.id.iv_to);
        spSlot = slotView.findViewById(R.id.sp_ad_slots);
        ivFromDate.setOnClickListener(view -> launchFromDateSelector(edFromDate));
        ivToDate.setOnClickListener(view -> launchToDateSelector(edToDate));
        //initializeFromDateTextWatcher(edFromDate);
        // initializeToDateTextWatcher(edToDate);
        getAdvertisementTimeSlot(spSlot);
        AdSlotBtn.setVisibility(View.GONE);
        addButton.setOnClickListener(view -> {
            addButton.setVisibility(View.GONE);
            addView();
        });

        imageView.setOnClickListener(view -> removeView(slotView));
        llMainLayout.addView(slotView);
    }

    private void removeView(View slotView) {
        AdSlotBtn.setVisibility(View.VISIBLE);
        llMainLayout.removeView(slotView);
    }

    //type of ad area click
    @Override
    public void TypeOfAreaClick(int pos, List<AdvertisementAreaModel> types) {
        if (types.get(pos).isChecked()) {
            selectedAdArea = types.get(pos).getAdvertisementAreaName();
            selectedAdAreaId = types.get(pos).getAdvertisementAreaID();
            ad = new AdvertisementAreaLevel(types.get(pos).getAdvertisementAreaID(), types.get(pos).getAdvertisementAreaName(), types.get(pos).getAdAreaMatrix());
            switch (selectedAdArea) {
                case "National Level":
                    edStateName.setVisibility(View.GONE);
                    edDistrictName.setVisibility(View.GONE);
                    edCityName.setVisibility(View.GONE);
                    edTierCity.setVisibility(View.GONE);
                    tvShowSelectedState.setVisibility(View.GONE);
                    tvTierCity.setVisibility(View.GONE);
                    stateList.clear();
                    finalSelectedStateList.clear();
                    districtList.clear();
                    finalSelectedDistrictList.clear();
                    cityList.clear();
                    finalSelectedCityList.clear();
                    edStateName.getText().clear();
                    edDistrictName.getText().clear();
                    edCityName.getText().clear();
                    dateicon.setEnabled(true);
                    break;
                case "State Level":
                    edStateName.setVisibility(View.VISIBLE);
                    edDistrictName.setVisibility(View.GONE);
                    tvTierCity.setVisibility(View.GONE);
                    edCityName.setVisibility(View.GONE);
                    edTierCity.setVisibility(View.GONE);
                    stateList.clear();
                    finalSelectedStateList.clear();
                    districtList.clear();
                    finalSelectedDistrictList.clear();
                    cityList.clear();
                    finalSelectedCityList.clear();
                    edStateName.getText().clear();
                    edDistrictName.getText().clear();
                    edCityName.getText().clear();
                    dateicon.setEnabled(true);
                    tvShowSelectedCity.setVisibility(View.GONE);
                    tvShowSelectedDistrict.setVisibility(View.GONE);
                    getStateList();
                    break;
                case "District Level":
                    edStateName.setVisibility(View.VISIBLE);
                    edDistrictName.setVisibility(View.VISIBLE);
                    edCityName.setVisibility(View.GONE);
                    edTierCity.setVisibility(View.GONE);
                    tvShowSelectedState.setVisibility(View.GONE);
                    tvShowSelectedCity.setVisibility(View.GONE);
                    tvTierCity.setVisibility(View.GONE);
                    stateList.clear();
                    finalSelectedStateList.clear();
                    districtList.clear();
                    finalSelectedDistrictList.clear();
                    cityList.clear();
                    finalSelectedCityList.clear();
                    edStateName.getText().clear();
                    edDistrictName.getText().clear();
                    edCityName.getText().clear();
                    dateicon.setEnabled(true);
                    getStateList();
                    break;
                case "Tier 1 Cities":
                    edStateName.setVisibility(View.GONE);
                    edDistrictName.setVisibility(View.GONE);
                    edTierCity.setVisibility(View.VISIBLE);
                    edCityName.setVisibility(View.GONE);
                    tvShowSelectedState.setVisibility(View.GONE);
                    tvShowSelectedDistrict.setVisibility(View.GONE);
                    tvShowSelectedCity.setVisibility(View.GONE);
                    tvTierCity.setVisibility(View.GONE);
                    stateList.clear();
                    finalSelectedStateList.clear();
                    districtList.clear();
                    finalSelectedDistrictList.clear();
                    cityList.clear();
                    finalSelectedCityList.clear();
                    edStateName.getText().clear();
                    edDistrictName.getText().clear();
                    edTierCity.getText().clear();
                    dateicon.setEnabled(true);
                    getTierCityList();
                    break;
                case "Other Cities":
                    edStateName.setVisibility(View.VISIBLE);
                    edDistrictName.setVisibility(View.GONE);
                    edTierCity.setVisibility(View.GONE);
                    edCityName.setVisibility(View.VISIBLE);
                    edTierCity.setVisibility(View.GONE);
                    tvShowSelectedState.setVisibility(View.GONE);
                    tvShowSelectedDistrict.setVisibility(View.GONE);
                    tvTierCity.setVisibility(View.GONE);
                    stateList.clear();
                    finalSelectedStateList.clear();
                    districtList.clear();
                    finalSelectedDistrictList.clear();
                    cityList.clear();
                    finalSelectedCityList.clear();
                    edStateName.getText().clear();
                    edDistrictName.getText().clear();
                    edCityName.getText().clear();
                    dateicon.setEnabled(true);
                    getStateList();
                    break;
            }
        } /*else {
            edStateName.setVisibility(View.GONE);
            edDistrictName.setVisibility(View.GONE);
            edCityName.setVisibility(View.GONE);
            edTierCity.setVisibility(View.GONE);
            tvShowSelectedState.setVisibility(View.GONE);
            tvShowSelectedDistrict.setVisibility(View.GONE);
            tvShowSelectedCity.setVisibility(View.GONE);
            tvTierCity.setVisibility(View.GONE);
            dateicon.setEnabled(false);
        }*/
    }

    //type of ad click event
    @Override
    public void TypeOfAdClick(int pos, List<AdvertisementTypeModel> types) {
        if (types.get(pos).isChecked()) {
            selectedAdType = types.get(pos).getAdvertisementType();
            selectedAdTypeId = types.get(pos).getAdTypeId();
            switch (selectedAdType) {
                case "FullPageAd":
                    llAdDurationFullPage.setVisibility(View.VISIBLE);
                    llAdDuration.setVisibility(View.GONE);
                    tvPreviewAd.setVisibility(View.VISIBLE);
                    if (ConnectivityReceiver.isConnected()) {
                        getAdvertisementTimeSlot(spTimeSlotFullScreen);
                        //set slot list to spinner
                        slotModelList.add(0, new AdvertisementSlotModel(-1, ""));
                        timeSlotAdapter = new TimeSlotAdapter(AddRequestUserActivity.this, 0, slotModelList, AddRequestUserActivity.this);
                        spTimeSlotFullScreen.setAdapter(timeSlotAdapter);
                        tvslotSelected.setText("");
                        tvslotsSelected.setText("");
                    } else {
                        showError();
                    }
                    break;
                case "TextAd":

                case "BannerAd":
                    llAdDurationFullPage.setVisibility(View.GONE);
                    llAdDuration.setVisibility(View.VISIBLE);
                    tvPreviewAd.setVisibility(View.VISIBLE);
                    if (ConnectivityReceiver.isConnected()) {
                        getAdvertisementTimeSlot(spTimeSlots);
                        //set slot list to spinner
                        slotModelList.add(0, new AdvertisementSlotModel(-1, ""));
                        timeSlotAdapter = new TimeSlotAdapter(AddRequestUserActivity.this, 0, slotModelList, AddRequestUserActivity.this);
                        spTimeSlots.setAdapter(timeSlotAdapter);
                        tvslotSelected.setText("");
                        tvslotsSelected.setText("");
                    } else {
                        showError();
                    }
                    break;

            }


        } /*else {
            llAdDurationFullPage.setVisibility(View.GONE);
            llAdDuration.setVisibility(View.GONE);
            tvPreviewAd.setVisibility(View.GONE);
        }*/
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.sp_intervals:
                selectedIntervalId = Integer.parseInt(intervals[i]);
                break;
            case R.id.sp_slots:
                break;
            case R.id.sp_slots1:
                selectedSlotId = slotModelList.get(i).getAdSlotId();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void showError() {
        Snackbar snackbar = Snackbar
                .make(llParentLayout, "You are not connected to Internet.!", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.AddProduct:
                if (ConnectivityReceiver.isConnected()) {
                    openDialogToSelectProduct();
                } else {
                    showError();
                }

                break;

            case R.id.AddBrand:
                if (edAdProduct.getText().toString().isEmpty()) {
                    CustomToast.showToast(AddRequestUserActivity.this, "Please select Product");
                } else {
                    if (ConnectivityReceiver.isConnected()) {
                        getBrands();
                    } else {
                        showError();
                    }
                }

                break;

            case R.id.ed_from_date:
                edSelectFromDate.getText().clear();
                launchFromDateSelector(edSelectFromDate);
                break;

            case R.id.ed_to_date:
                edSelectToDate.getText().clear();
                launchToDateSelector(edSelectToDate);
                break;

            case R.id.ed_from_date1:
                edFullPageAdDate.getText().clear();
                launchFromDateSelector(edFullPageAdDate);
                break;

            case R.id.iv_cal_from:
                launchFromDateSelector(edSelectFromDate);
                break;

            case R.id.iv_cal_to:
                launchToDateSelector(edSelectToDate);
                break;

            case R.id.iv_cal_from1:
                launchFromDateSelector(edFullPageAdDate);
                break;

            case R.id.ed_select_state:
                if (areaAdapter.getTrueResultList().size() == 0) {
                    CustomToast.showToast(AddRequestUserActivity.this, "Please select advertisement area");
                } else {
                    districtList.clear();
                    cityList.clear();
                    finalSelectedDistrictList.clear();
                    finalSelectedCityList.clear();
                    if (ConnectivityReceiver.isConnected()) {

                        openDialogToSelectState();
                    } else {
                        showError();
                    }

                }
                break;

            case R.id.ed_select_district:
                if (selectedStateList.size() == 0 | selectedStateList.isEmpty()) {
                    CustomToast.showToast(AddRequestUserActivity.this, "Please select state");
                } else {
                    districtList.clear();
                    cityList.clear();
                    finalSelectedCityList.clear();
                    if (ConnectivityReceiver.isConnected()) {
                        openDialogToSelectDistrict();
                    } else {
                        showError();
                    }
                }
                break;

            case R.id.btn_state_submit:
                edStateName.getText().clear();
                adStateSelectorAdapter = new AdStateSelectorAdapter(AddRequestUserActivity.this, stateList, AddRequestUserActivity.this);
                recyclerViewState.setAdapter(adStateSelectorAdapter);
                selectedStateList.clear();
                for (int i = 0; i < adStateSelectorAdapter.getResultList().size(); i++) {
                    State state = adStateSelectorAdapter.getResultList().get(i);
                    selectedStateList.add(new State(state.getStateID(), state.getStateName(), state.isChecked()));
                    LinkedHashSet<State> listRepatedArryList = new LinkedHashSet<State>();
                    listRepatedArryList.addAll(selectedStateList);
                    selectedStateList.clear();
                    selectedStateList.addAll(listRepatedArryList);
                    mDialogState.dismiss();
                    if (selectedStateList.size() != 0 | !selectedStateList.isEmpty()) {
                        edStateName.setText("" + selectedStateList.toString().replace("[", "").replace("]", ""));
                        if (selectedAdArea.equals("State Level")) {
                            tvShowSelectedState.setVisibility(View.VISIBLE);
                        } else {
                            tvShowSelectedState.setVisibility(View.GONE);
                        }
                        finalSelectedStateList = selectedStateList;
                    }
                }
                break;

            case R.id.tv_show_state:
                //show selected states
                showSelectedStates(selectedStateList);
                break;

            case R.id.btn_district_submit:
                for (int i = 0; i < adDistrictSelectorAdapter.getResultList().size(); i++) {
                    District district = adDistrictSelectorAdapter.getResultList().get(i);
                    selectedDistrictList.add(new District(district.getDistrictID(), district.getDistrictName(), district.isChecked()));
                    LinkedHashSet<District> listRepatedArryList = new LinkedHashSet<District>();
                    listRepatedArryList.addAll(selectedDistrictList);
                    selectedDistrictList.clear();
                    selectedDistrictList.addAll(listRepatedArryList);
                    mDialogDistrict.dismiss();
                    if (selectedDistrictList.size() != 0 | !selectedDistrictList.isEmpty()) {
                        edDistrictName.setText("" + selectedDistrictList.toString().replace("[", "").replace("]", ""));
                        if (selectedAdArea.equals("District Level")) {
                            tvShowSelectedDistrict.setVisibility(View.VISIBLE);
                        } else {
                            tvShowSelectedDistrict.setVisibility(View.GONE);
                        }
                        finalSelectedDistrictList = selectedDistrictList;
                    }
                }
                break;

            case R.id.tv_show_district:
                showSelectedDistrict(selectedDistrictList);
                break;

            case R.id.ed_select_city:
                if (selectedStateList.size() == 0 | selectedStateList.isEmpty()) {
                    CustomToast.showToast(AddRequestUserActivity.this, "Please select state");
                } else {
                    cityList.clear();
                    finalSelectedDistrictList.clear();
                    if (ConnectivityReceiver.isConnected()) {
                        openDialogToSelectCity();
                    } else {
                        showError();
                    }
                }
                break;
            case R.id.ed_tier_city:
                openDialogToSelectTierCity();
                break;
            case R.id.btn_city_submit:
                adCitySelectorAdapter = new AdCitySelectorAdapter(AddRequestUserActivity.this, cityList, AddRequestUserActivity.this);
                recyclerViewCity.setAdapter(adCitySelectorAdapter);
                selectedCityList.clear();
                for (int i = 0; i < adCitySelectorAdapter.getResultList().size(); i++) {
                    CityAD city = adCitySelectorAdapter.getResultList().get(i);
                    selectedCityList.add(new CityAD(city.getStatewithCityID(), city.getVillageLocalityname(), city.isChecked()));
                    LinkedHashSet<CityAD> listRepatedArryList = new LinkedHashSet<>();
                    listRepatedArryList.addAll(selectedCityList);
                    selectedCityList.clear();
                    selectedCityList.addAll(listRepatedArryList);
                    mDialogCity.dismiss();
                    if (selectedCityList.size() != 0 | !selectedCityList.isEmpty()) {
                        edCityName.setText("" + selectedCityList.toString().replace("[", "").replace("]", ""));
                        edTierCity.setText("" + selectedCityList.toString().replace("[", "").replace("]", ""));
                        if (selectedAdArea.equals("Tier 1 Cities")) {
                            tvTierCity.setVisibility(View.VISIBLE);
                        }
                        finalSelectedCityList = selectedCityList;
                    }

                }
                finalSelectedCityList = finalSelectedCityList;
                break;
            case R.id.btn_other_city_submit:

                for (int i = 0; i < adCitySelectorAdapter.getResultList().size(); i++) {
                    CityAD city = adCitySelectorAdapter.getResultList().get(i);
                    selectedCityList.add(new CityAD(city.getStatewithCityID(), city.getVillageLocalityname(), city.isChecked()));
                    LinkedHashSet<CityAD> listRepatedArryList = new LinkedHashSet<>();
                    listRepatedArryList.addAll(selectedCityList);
                    selectedCityList.clear();
                    selectedCityList.addAll(listRepatedArryList);
                    mDialogCity.dismiss();
                    if (selectedCityList.size() != 0 | !selectedCityList.isEmpty()) {
                        edCityName.setText("" + selectedCityList.toString().replace("[", "").replace("]", ""));
                        edTierCity.setText("" + selectedCityList.toString().replace("[", "").replace("]", ""));
                        if (selectedAdArea.equals("Other Cities")) {
                            tvShowSelectedCity.setVisibility(View.VISIBLE);
                        }
                        finalSelectedCityList = selectedCityList;
                    }

                }
                finalSelectedCityList = finalSelectedCityList;
                break;

            case R.id.tv_show_city:
                showSelectedCity(selectedCityList);
                break;

            case R.id.tv_tier_city:
                showSelectedTierCity(selectedCityList);
                break;

            case R.id.bt_estimate_price:
                try {
                    selectedSlotModelList = timeSlotAdapter.getSelectedSlots();
                    if (selectedSlotModelList.size() == 1) {
                        tvslotSelected.setText("One time slot selected");
                        tvslotsSelected.setText("One time slot selected");
                    }
                    if (selectedSlotModelList.size() == 2) {
                        tvslotSelected.setText("Two time slots selected");
                        tvslotsSelected.setText("Two time slots selected");
                    }
                    if (selectedSlotModelList.size() == 3) {
                        tvslotSelected.setText("Three time slots selected");
                        tvslotsSelected.setText("Three time slots selected");
                    }
                    if (selectedAdTypeId != 0 && !selectedAdType.isEmpty()) {
                        if (selectedProductId != 0 && !edAdProduct.getText().toString().isEmpty()) {
                            if (selectedAdAreaId != 0 && !selectedAdArea.isEmpty()) {
                                switch (selectedAdArea) {
                                    case "National Level":
                                        if (selectedAdType.equals("FullPageAd")) {
                                            if (edFullPageAdDate.getText().toString().isEmpty()) {
                                                CustomToast.showToast(getApplicationContext(), "Please select advertisement date");
                                            } else {
                                                selectedSlotModelList = timeSlotAdapter.getSelectedSlots();
                                                if (!selectedSlotModelList.isEmpty()) {
                                                    //send full screen data to server
                                                    if (ConnectivityReceiver.isConnected()) {
                                                        if (selectedBrandId == 72) {
                                                            edOtherBrand.setVisibility(View.VISIBLE);
                                                            selectedBrandName = edOtherBrand.getText().toString();
                                                        } else {
                                                            edOtherBrand.setVisibility(View.GONE);
                                                            selectedBrandName = brandName;
                                                            // selectedBrandName=edOtherBrand.getText.toString();
                                                        }
                                                        List<String> advertisementLevel = new ArrayList<String>();
                                                        List<District> adDistricts = new ArrayList<>();
                                                        List<State> adStates = new ArrayList<>();
                                                        List<CityAD> adCities = new ArrayList<>();
                                                        advertisementLevel.add("India");
                                                        String selectedDate = Utility.ConvertdateFormat(edFullPageAdDate.getText().toString());
                                                        NewAdvertisementModule available = new NewAdvertisementModule(CustID, selectedProductId, selectedBrandId, selectedBrandName, selectedAdTypeId, selectedAdType, ad, advertisementLevel, selectedDate, selectedDate, selectedSlotModelList, 1, selectedCategoryProductId, adStates, adDistricts, adCities);
                                                        getFullPageEstimation(available);
                                                    } else {
                                                        showError();
                                                    }

                                                } else {
                                                    CustomToast.showToast(getApplicationContext(), "Please select advertisement slots");
                                                }

                                            }
                                        } else {
                                            if (!edSelectFromDate.getText().toString().isEmpty() && !edSelectToDate.getText().toString().isEmpty()) {
                                                if (selectedIntervalId != 0) {
                                                    selectedSlotModelList = timeSlotAdapter.getSelectedSlots();
                                                    if (!selectedSlotModelList.isEmpty()) {
                                                        // send data to server
                                                        if (ConnectivityReceiver.isConnected()) {
                                                            if (selectedBrandId == 72) {
                                                                edOtherBrand.setVisibility(View.VISIBLE);
                                                                selectedBrandName = edOtherBrand.getText().toString();
                                                            } else {
                                                                edOtherBrand.setVisibility(View.GONE);
                                                                selectedBrandName = brandName;
                                                                // selectedBrandName=edOtherBrand.getText.toString();
                                                            }
                                                            String selectedFromDate = Utility.ConvertdateFormat(edSelectFromDate.getText().toString());
                                                            String selectedToDate = Utility.ConvertdateFormat(edSelectToDate.getText().toString());
                                                            List<String> advertisementLevel = new ArrayList<String>();
                                                            List<District> adDistricts = new ArrayList<>();
                                                            List<State> adStates = new ArrayList<>();
                                                            List<CityAD> adCities = new ArrayList<>();
                                                            advertisementLevel.add("India");
                                                            NewAdvertisementModule available = new NewAdvertisementModule(CustID, selectedProductId, selectedBrandId, selectedBrandName, selectedAdTypeId, selectedAdType, ad, advertisementLevel, selectedFromDate, selectedToDate, selectedSlotModelList, selectedIntervalId, selectedCategoryProductId, adStates, adDistricts, adCities);
                                                            if (checkDates(selectedFromDate, selectedToDate) == true) {
                                                                getEstimationPriceNew(available);
                                                            } else {
                                                                CustomToast.showToast(getApplicationContext(), "Please select date greater than From date");
                                                            }
                                                        } else {
                                                            showError();
                                                        }

                                                    } else {
                                                        CustomToast.showToast(getApplicationContext(), "Please select advertisement slots");
                                                    }
                                                } else {
                                                    CustomToast.showToast(getApplicationContext(), "Please select advertisement intervals");
                                                }

                                            } else {
                                                CustomToast.showToast(getApplicationContext(), "Please select advertisement date range");
                                            }
                                        }
                                        break;
                                    case "State Level":
                                        if (finalSelectedStateList.isEmpty() && edStateName.getText().toString().isEmpty()) {
                                            CustomToast.showToast(getApplicationContext(), "Please select advertisement state");
                                        } else {
                                            if (selectedAdType.equals("FullPageAd")) {
                                                if (edFullPageAdDate.getText().toString().isEmpty()) {
                                                    CustomToast.showToast(getApplicationContext(), "Please select advertisement date");
                                                } else {
                                                    selectedSlotModelList = timeSlotAdapter.getSelectedSlots();
                                                    if (!selectedSlotModelList.isEmpty()) {
                                                        //send full screen data to server
                                                        if (ConnectivityReceiver.isConnected()) {
                                                            if (selectedBrandId == 72) {
                                                                edOtherBrand.setVisibility(View.VISIBLE);
                                                                selectedBrandName = edOtherBrand.getText().toString();
                                                            } else {
                                                                edOtherBrand.setVisibility(View.GONE);
                                                                selectedBrandName = brandName;
                                                                // selectedBrandName=edOtherBrand.getText.toString();
                                                            }
                                                            List<String> advertisementLevel = new ArrayList<String>();
                                                            List<District> adDistricts = new ArrayList<>();
                                                            List<State> adStates = new ArrayList<>();
                                                            List<CityAD> adCities = new ArrayList<>();
                                                            // advertisementLevel.add("India");
                                                            for (int i = 0; i < finalSelectedStateList.size(); i++) {
                                                                advertisementLevel.add(finalSelectedStateList.get(i).getStateName());
                                                                adStates.add(new State(finalSelectedStateList.get(i).getStateID()));
                                                            }
                                                            String selectedDate = Utility.ConvertdateFormat(edFullPageAdDate.getText().toString());
                                                            //NewAdvertisementModule available=new NewAdvertisementModule(CustID,selectedProductId,selectedBrandId,selectedBrandName,selectedAdTypeId,selectedAdType,ad,advertisementLevel,selectedDate,selectedDate,selectedSlotModelList,selectedIntervalId,selectedCategoryProductId);
                                                            NewAdvertisementModule available = new NewAdvertisementModule(CustID, selectedProductId, selectedBrandId, selectedBrandName, selectedAdTypeId, selectedAdType, ad, advertisementLevel, selectedDate, selectedDate, selectedSlotModelList, 1, selectedCategoryProductId, adStates, adDistricts, adCities);
                                                            getFullPageEstimation(available);
                                                        } else {
                                                            showError();
                                                        }

                                                    } else {
                                                        CustomToast.showToast(getApplicationContext(), "Please select advertisement slots");
                                                    }

                                                }
                                            } else {
                                                if (!edSelectFromDate.getText().toString().isEmpty() && !edSelectToDate.getText().toString().isEmpty()) {
                                                    if (selectedIntervalId != 0) {
                                                        selectedSlotModelList = timeSlotAdapter.getSelectedSlots();
                                                        if (!selectedSlotModelList.isEmpty()) {
                                                            // send data to server
                                                            if (ConnectivityReceiver.isConnected()) {
                                                                if (selectedBrandId == 72) {
                                                                    edOtherBrand.setVisibility(View.VISIBLE);
                                                                    selectedBrandName = edOtherBrand.getText().toString();
                                                                } else {
                                                                    edOtherBrand.setVisibility(View.GONE);
                                                                    selectedBrandName = brandName;
                                                                    // selectedBrandName=edOtherBrand.getText.toString();
                                                                }
                                                                String selectedFromDate = Utility.ConvertdateFormat(edSelectFromDate.getText().toString());
                                                                String selectedToDate = Utility.ConvertdateFormat(edSelectToDate.getText().toString());
                                                                List<String> advertisementLevel = new ArrayList<String>();
                                                                List<District> adDistricts = new ArrayList<>();
                                                                List<State> adStates = new ArrayList<>();
                                                                List<CityAD> adCities = new ArrayList<>();
                                                                // advertisementLevel.add("India");
                                                                for (int i = 0; i < finalSelectedStateList.size(); i++) {
                                                                    advertisementLevel.add(finalSelectedStateList.get(i).getStateName());
                                                                    adStates.add(new State(finalSelectedStateList.get(i).getStateID()));
                                                                }
                                                                //NewAdvertisementModule available=new NewAdvertisementModule(CustID,selectedProductId,selectedBrandId,selectedBrandName,selectedAdTypeId,selectedAdType,ad,advertisementLevel,selectedFromDate,selectedToDate,selectedSlotModelList,selectedIntervalId,selectedCategoryProductId);
                                                                NewAdvertisementModule available = new NewAdvertisementModule(CustID, selectedProductId, selectedBrandId, selectedBrandName, selectedAdTypeId, selectedAdType, ad, advertisementLevel, selectedFromDate, selectedToDate, selectedSlotModelList, selectedIntervalId, selectedCategoryProductId, adStates, adDistricts, adCities);
                                                                if (checkDates(selectedFromDate, selectedToDate) == true) {
                                                                    getEstimationPriceNew(available);
                                                                } else {
                                                                    CustomToast.showToast(getApplicationContext(), "Please select date greater than From date");
                                                                }
                                                            } else {
                                                                showError();
                                                            }

                                                        } else {
                                                            CustomToast.showToast(getApplicationContext(), "Please select advertisement slots");
                                                        }
                                                    } else {
                                                        CustomToast.showToast(getApplicationContext(), "Please select advertisement intervals");
                                                    }

                                                } else {
                                                    CustomToast.showToast(getApplicationContext(), "Please select advertisement date range");
                                                }
                                            }
                                        }
                                        break;
                                    case "District Level":
                                        if (finalSelectedStateList.isEmpty() && edStateName.getText().toString().isEmpty()) {
                                            CustomToast.showToast(getApplicationContext(), "Please select advertisement state");
                                        } else if (finalSelectedDistrictList.isEmpty() && edDistrictName.getText().toString().isEmpty()) {
                                            CustomToast.showToast(getApplicationContext(), "Please select advertisement district");
                                        } else {
                                            if (selectedAdType.equals("FullPageAd")) {
                                                if (edFullPageAdDate.getText().toString().isEmpty()) {
                                                    CustomToast.showToast(getApplicationContext(), "Please select advertisement date");
                                                } else {
                                                    selectedSlotModelList = timeSlotAdapter.getSelectedSlots();
                                                    if (!selectedSlotModelList.isEmpty()) {
                                                        //send full screen data to server
                                                        if (ConnectivityReceiver.isConnected()) {
                                                            if (selectedBrandId == 72) {
                                                                edOtherBrand.setVisibility(View.VISIBLE);
                                                                selectedBrandName = edOtherBrand.getText().toString();
                                                            } else {
                                                                edOtherBrand.setVisibility(View.GONE);
                                                                selectedBrandName = brandName;
                                                            }
                                                            String selectedDate = Utility.ConvertdateFormat(edFullPageAdDate.getText().toString());
                                                            List<String> advertisementLevel = new ArrayList<String>();
                                                            List<District> adDistricts = new ArrayList<>();
                                                            List<State> adStates = new ArrayList<>();
                                                            List<CityAD> adCities = new ArrayList<>();
                                                            // advertisementLevel.add("India");
                                                            for (int i = 0; i < finalSelectedDistrictList.size(); i++) {
                                                                advertisementLevel.add(finalSelectedDistrictList.get(i).getDistrictName());
                                                                adDistricts.add(new District(finalSelectedDistrictList.get(i).getDistrictID()));
                                                            }
                                                            Log.e("advertisementDetials...", adDistricts.toString());
                                                            //NewAdvertisementModule available=new NewAdvertisementModule(CustID,selectedProductId,selectedBrandId,selectedBrandName,selectedAdTypeId,selectedAdType,ad,advertisementLevel,selectedDate,selectedDate,selectedSlotModelList,selectedIntervalId,selectedCategoryProductId);
                                                            NewAdvertisementModule available = new NewAdvertisementModule(CustID, selectedProductId, selectedBrandId, selectedBrandName, selectedAdTypeId, selectedAdType, ad, advertisementLevel, selectedDate, selectedDate, selectedSlotModelList, 1, selectedCategoryProductId, adStates, adDistricts, adCities);
                                                            getFullPageEstimation(available);
                                                        } else {
                                                            showError();
                                                        }

                                                    } else {
                                                        CustomToast.showToast(getApplicationContext(), "Please select advertisement slots");
                                                    }

                                                }
                                            } else {
                                                if (!edSelectFromDate.getText().toString().isEmpty() && !edSelectToDate.getText().toString().isEmpty()) {
                                                    if (selectedIntervalId != 0) {
                                                        selectedSlotModelList = timeSlotAdapter.getSelectedSlots();
                                                        if (!selectedSlotModelList.isEmpty()) {
                                                            // send data to server
                                                            if (ConnectivityReceiver.isConnected()) {
                                                                if (selectedBrandId == 72) {
                                                                    edOtherBrand.setVisibility(View.VISIBLE);
                                                                    selectedBrandName = edOtherBrand.getText().toString();
                                                                } else {
                                                                    edOtherBrand.setVisibility(View.GONE);
                                                                    selectedBrandName = brandName;
                                                                }
                                                                String selectedFromDate = Utility.ConvertdateFormat(edSelectFromDate.getText().toString());
                                                                String selectedToDate = Utility.ConvertdateFormat(edSelectToDate.getText().toString());
                                                                List<String> advertisementLevel = new ArrayList<String>();
                                                                List<District> adDistricts = new ArrayList<>();
                                                                List<State> adStates = new ArrayList<>();
                                                                List<CityAD> adCities = new ArrayList<>();
                                                                // advertisementLevel.add("India");
                                                                for (int i = 0; i < finalSelectedDistrictList.size(); i++) {
                                                                    advertisementLevel.add(finalSelectedDistrictList.get(i).getDistrictName());
                                                                    adDistricts.add(new District(finalSelectedDistrictList.get(i).getDistrictID()));
                                                                }
                                                                Log.e("advertisementDisricts..", adDistricts.toString());
                                                                //NewAdvertisementModule available=new NewAdvertisementModule(CustID,selectedProductId,selectedBrandId,selectedBrandName,selectedAdTypeId,selectedAdType,ad,advertisementLevel,selectedFromDate,selectedToDate,selectedSlotModelList,selectedIntervalId,selectedCategoryProductId);
                                                                NewAdvertisementModule available = new NewAdvertisementModule(CustID, selectedProductId, selectedBrandId, selectedBrandName, selectedAdTypeId, selectedAdType, ad, advertisementLevel, selectedFromDate, selectedToDate, selectedSlotModelList, selectedIntervalId, selectedCategoryProductId, adStates, adDistricts, adCities);
                                                                if (checkDates(selectedFromDate, selectedToDate) == true) {
                                                                    getEstimationPriceNew(available);
                                                                } else {
                                                                    CustomToast.showToast(getApplicationContext(), "Please select date greater than From date");
                                                                }
                                                            } else {
                                                                showError();
                                                            }

                                                        } else {
                                                            CustomToast.showToast(getApplicationContext(), "Please select advertisement slots");
                                                        }
                                                    } else {
                                                        CustomToast.showToast(getApplicationContext(), "Please select advertisement intervals");
                                                    }
                                                } else {
                                                    CustomToast.showToast(getApplicationContext(), "Please select advertisement date range");
                                                }
                                            }
                                        }
                                        break;
                                    case "Tier 1 Cities":
                                        if (finalSelectedCityList.isEmpty() && edTierCity.getText().toString().isEmpty()) {
                                            CustomToast.showToast(getApplicationContext(), "Please select advertisement city");
                                        } else {
                                            if (selectedAdType.equals("FullPageAd")) {
                                                if (edFullPageAdDate.getText().toString().isEmpty()) {
                                                    CustomToast.showToast(getApplicationContext(), "Please select advertisement date");
                                                } else {
                                                    selectedSlotModelList = timeSlotAdapter.getSelectedSlots();
                                                    if (!selectedSlotModelList.isEmpty()) {
                                                        //send full screen data to server
                                                        if (ConnectivityReceiver.isConnected()) {
                                                            if (selectedBrandId == 72) {
                                                                edOtherBrand.setVisibility(View.VISIBLE);
                                                                selectedBrandName = edOtherBrand.getText().toString();
                                                            } else {
                                                                edOtherBrand.setVisibility(View.GONE);
                                                                selectedBrandName = brandName;
                                                            }
                                                            String selectedDate = Utility.ConvertdateFormat(edFullPageAdDate.getText().toString());
                                                            List<String> advertisementLevel = new ArrayList<String>();
                                                            List<District> adDistricts = new ArrayList<>();
                                                            List<State> adStates = new ArrayList<>();
                                                            List<CityAD> adCities = new ArrayList<>();
                                                            for (int i = 0; i < finalSelectedCityList.size(); i++) {
                                                                advertisementLevel.add(finalSelectedCityList.get(i).getVillageLocalityname());
                                                                adCities.add(new CityAD(finalSelectedCityList.get(i).getStatewithCityID()));
                                                            }
                                                            //NewAdvertisementModule available=new NewAdvertisementModule(CustID,selectedProductId,selectedBrandId,selectedBrandName,selectedAdTypeId,selectedAdType,ad,advertisementLevel,selectedDate,selectedDate,selectedSlotModelList,selectedIntervalId,selectedCategoryProductId);
                                                            NewAdvertisementModule available = new NewAdvertisementModule(CustID, selectedProductId, selectedBrandId, selectedBrandName, selectedAdTypeId, selectedAdType, ad, advertisementLevel, selectedDate, selectedDate, selectedSlotModelList, 1, selectedCategoryProductId, adStates, adDistricts, adCities);
                                                            getFullPageEstimation(available);
                                                        } else {
                                                            showError();
                                                        }

                                                    } else {
                                                        CustomToast.showToast(getApplicationContext(), "Please select advertisement slots");
                                                    }

                                                }
                                            } else {
                                                if (!edSelectFromDate.getText().toString().isEmpty() && !edSelectToDate.getText().toString().isEmpty()) {
                                                    if (selectedIntervalId != 0) {
                                                        selectedSlotModelList = timeSlotAdapter.getSelectedSlots();
                                                        if (!selectedSlotModelList.isEmpty()) {
                                                            // send data to server
                                                            if (ConnectivityReceiver.isConnected()) {
                                                                if (selectedBrandId == 72) {
                                                                    edOtherBrand.setVisibility(View.VISIBLE);
                                                                    selectedBrandName = edOtherBrand.getText().toString();
                                                                } else {
                                                                    edOtherBrand.setVisibility(View.GONE);
                                                                    selectedBrandName = brandName;
                                                                }
                                                                String selectedFromDate = Utility.ConvertdateFormat(edSelectFromDate.getText().toString());
                                                                String selectedToDate = Utility.ConvertdateFormat(edSelectToDate.getText().toString());
                                                                List<String> advertisementLevel = new ArrayList<String>();
                                                                List<District> adDistricts = new ArrayList<>();
                                                                List<State> adStates = new ArrayList<>();
                                                                List<CityAD> adCities = new ArrayList<>();
                                                                for (int i = 0; i < finalSelectedCityList.size(); i++) {
                                                                    advertisementLevel.add(finalSelectedCityList.get(i).getVillageLocalityname());
                                                                    adCities.add(new CityAD(finalSelectedCityList.get(i).getStatewithCityID()));
                                                                }
                                                                // NewAdvertisementModule available=new NewAdvertisementModule(CustID,selectedProductId,selectedBrandId,selectedBrandName,selectedAdTypeId,selectedAdType,ad,advertisementLevel,selectedFromDate,selectedToDate,selectedSlotModelList,selectedIntervalId,selectedCategoryProductId);
                                                                NewAdvertisementModule available = new NewAdvertisementModule(CustID, selectedProductId, selectedBrandId, selectedBrandName, selectedAdTypeId, selectedAdType, ad, advertisementLevel, selectedFromDate, selectedToDate, selectedSlotModelList, selectedIntervalId, selectedCategoryProductId, adStates, adDistricts, adCities);
                                                                if (checkDates(selectedFromDate, selectedToDate) == true) {
                                                                    getEstimationPriceNew(available);
                                                                } else {
                                                                    CustomToast.showToast(getApplicationContext(), "Please select date greater than From date");
                                                                }

                                                            } else {
                                                                showError();
                                                            }

                                                        } else {
                                                            CustomToast.showToast(getApplicationContext(), "Please select advertisement slots");
                                                        }
                                                    } else {
                                                        CustomToast.showToast(getApplicationContext(), "Please select advertisement intervals");
                                                    }
                                                } else {
                                                    CustomToast.showToast(getApplicationContext(), "Please select advertisement date range");
                                                }
                                            }
                                        }
                                        break;
                                    case "Other Cities":
                                        if (finalSelectedStateList.isEmpty() && edStateName.getText().toString().isEmpty()) {
                                            CustomToast.showToast(getApplicationContext(), "Please select advertisement state");
                                        } else if (finalSelectedCityList.isEmpty() && edCityName.getText().toString().isEmpty()) {
                                            CustomToast.showToast(getApplicationContext(), "Please select advertisement city");
                                        } else {
                                            if (selectedAdType.equals("FullPageAd")) {
                                                if (edFullPageAdDate.getText().toString().isEmpty()) {
                                                    CustomToast.showToast(getApplicationContext(), "Please select advertisement date");
                                                } else {
                                                    selectedSlotModelList = timeSlotAdapter.getSelectedSlots();
                                                    if (!selectedSlotModelList.isEmpty()) {
                                                        //send full screen data to server
                                                        if (ConnectivityReceiver.isConnected()) {
                                                            if (selectedBrandId == 72) {
                                                                edOtherBrand.setVisibility(View.VISIBLE);
                                                                selectedBrandName = edOtherBrand.getText().toString();
                                                            } else {
                                                                edOtherBrand.setVisibility(View.GONE);
                                                                selectedBrandName = brandName;
                                                                // selectedBrandName=edOtherBrand.getText.toString();
                                                            }
                                                            String selectedDate = Utility.ConvertdateFormat(edFullPageAdDate.getText().toString());
                                                            List<String> advertisementLevel = new ArrayList<String>();
                                                            List<District> adDistricts = new ArrayList<>();
                                                            List<State> adStates = new ArrayList<>();
                                                            List<CityAD> adCities = new ArrayList<>();
                                                            for (int i = 0; i < finalSelectedCityList.size(); i++) {
                                                                String[] city = finalSelectedCityList.get(i).getVillageLocalityname().split("[(]");
                                                                advertisementLevel.add(city[0]);
                                                                //String city=finalSelectedCityList.get(i).getVillageLocalityname().split(" ")[0];
                                                                //advertisementLevel.add(finalSelectedCityList.get(i).getVillageLocalityname().split("")[0]);
                                                                adCities.add(new CityAD(finalSelectedCityList.get(i).getStatewithCityID()));
                                                            }
                                                            Log.e("advertsiemnt area...", advertisementLevel.toString());
                                                            //NewAdvertisementModule available=new NewAdvertisementModule(CustID,selectedProductId,selectedBrandId,selectedBrandName,selectedAdTypeId,selectedAdType,ad,advertisementLevel,selectedDate,selectedDate,selectedSlotModelList,selectedIntervalId,selectedCategoryProductId);
                                                            NewAdvertisementModule available = new NewAdvertisementModule(CustID, selectedProductId, selectedBrandId, selectedBrandName, selectedAdTypeId, selectedAdType, ad, advertisementLevel, selectedDate, selectedDate, selectedSlotModelList, 1, selectedCategoryProductId, adStates, adDistricts, adCities);
                                                            getFullPageEstimation(available);
                                                        } else {
                                                            showError();
                                                        }

                                                    } else {
                                                        CustomToast.showToast(getApplicationContext(), "Please select advertisement slots");
                                                    }

                                                }
                                            } else {
                                                if (!edSelectFromDate.getText().toString().isEmpty() && !edSelectToDate.getText().toString().isEmpty()) {
                                                    if (selectedIntervalId != 0) {
                                                        selectedSlotModelList = timeSlotAdapter.getSelectedSlots();
                                                        if (!selectedSlotModelList.isEmpty()) {
                                                            // send data to server
                                                            if (ConnectivityReceiver.isConnected()) {
                                                                if (selectedBrandId == 72) {
                                                                    edOtherBrand.setVisibility(View.VISIBLE);
                                                                    selectedBrandName = edOtherBrand.getText().toString();
                                                                } else {
                                                                    edOtherBrand.setVisibility(View.GONE);
                                                                    selectedBrandName = brandName;
                                                                    // selectedBrandName=edOtherBrand.getText.toString();
                                                                }
                                                                String selectedFromDate = Utility.ConvertdateFormat(edSelectFromDate.getText().toString());
                                                                String selectedToDate = Utility.ConvertdateFormat(edSelectToDate.getText().toString());
                                                                List<String> advertisementLevel = new ArrayList<String>();
                                                                List<District> adDistricts = new ArrayList<>();
                                                                List<State> adStates = new ArrayList<>();
                                                                List<CityAD> adCities = new ArrayList<>();
                                                                for (int i = 0; i < finalSelectedCityList.size(); i++) {
                                                                    String[] city = finalSelectedCityList.get(i).getVillageLocalityname().split("[(]");
                                                                    advertisementLevel.add(city[0]);
                                                                    Log.e("city.....", advertisementLevel.toString());
                                                                    adCities.add(new CityAD(finalSelectedCityList.get(i).getStatewithCityID()));
                                                                }
                                                                //NewAdvertisementModule available=new NewAdvertisementModule(CustID,selectedProductId,selectedBrandId,selectedBrandName,selectedAdTypeId,selectedAdType,ad,advertisementLevel,selectedFromDate,selectedToDate,selectedSlotModelList,selectedIntervalId,selectedCategoryProductId);
                                                                NewAdvertisementModule available = new NewAdvertisementModule(CustID, selectedProductId, selectedBrandId, selectedBrandName, selectedAdTypeId, selectedAdType, ad, advertisementLevel, selectedFromDate, selectedToDate, selectedSlotModelList, selectedIntervalId, selectedCategoryProductId, adStates, adDistricts, adCities);
                                                                if (checkDates(selectedFromDate, selectedToDate) == true) {
                                                                    getEstimationPriceNew(available);
                                                                } else {
                                                                    CustomToast.showToast(getApplicationContext(), "Please select date greater than From date");
                                                                }
                                                            } else {
                                                                showError();
                                                            }

                                                        } else {
                                                            CustomToast.showToast(getApplicationContext(), "Please select advertisement slots");
                                                        }
                                                    } else {
                                                        CustomToast.showToast(getApplicationContext(), "Please select advertisement intervals");
                                                    }
                                                } else {
                                                    CustomToast.showToast(getApplicationContext(), "Please select advertisement date range");
                                                }
                                            }
                                        }
                                        break;
                                }
                            } else {
                                CustomToast.showToast(getApplicationContext(), "Please select advertisement area");
                            }
                        } else {
                            CustomToast.showToast(getApplicationContext(), "Please select Product");
                        }
                    } else {
                        CustomToast.showToast(getApplicationContext(), "Please select type of advertisement");
                    }
                } catch (Exception e) {
                    Toast.makeText(AddRequestUserActivity.this, "Please enter the fields", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.tvReport:
                if (ConnectivityReceiver.isConnected()) {
                    downloadInvoice(adMainId);
                } else {
                    showError();
                }
                break;

            case R.id.tv_preview:
                showPreviewToUser();
                break;
        }
    }

    private void getAlertDistrict() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddRequestUserActivity.this);
        builder1.setCancelable(false);
        builder1.setMessage("Would you like to advertise in more districts?");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes", (dialog, id) -> openDialogToSelectDistrict());
        builder1.setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert11 = builder1.create();
        alert11.setCanceledOnTouchOutside(false);
        alert11.show();
    }

    private void getAlertCity() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddRequestUserActivity.this);
        builder1.setCancelable(false);
        builder1.setMessage("Would you like to advertise in more cities?");
        builder1.setCancelable(true);

        builder1.setPositiveButton("Yes", (dialog, id) -> openDialogToSelectCity());

        builder1.setNegativeButton("No", (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.setCanceledOnTouchOutside(false);
        alert11.show();
    }

    private void showPreviewToUser() {
        Dialog mDialog = new Dialog(AddRequestUserActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.show_preview_dialog);
        mDialog.setCancelable(true);
        //set layout height and width to its screen size
        LinearLayout linearLayout = mDialog.findViewById(R.id.mainLayout);
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.height = height - 60;
        params.width = width - 60;
        linearLayout.setLayoutParams(params);

        ImageView closeImage = mDialog.findViewById(R.id.cancel_icon);
        ImageView imageView = (PhotoView) mDialog.findViewById(R.id.photo_view);
        if (selectedAdType.equals("BannerAd")) {
//            Toast.makeText(this, "Preview not available now", Toast.LENGTH_SHORT).show();
            imageView.setBackgroundResource(R.drawable.banner_ad);
        } else if (selectedAdType.equals("FullPageAd")) {
            imageView.setBackgroundResource(R.drawable.fullpage_ad);
        } else {
//            Toast.makeText(this, "Preview not available now", Toast.LENGTH_SHORT).show();
            imageView.setBackgroundResource(R.drawable.text_ad);
        }
        closeImage.setOnClickListener(view -> mDialog.dismiss());
        mDialog.show();
    }


    private void getEstimationPriceNew(NewAdvertisementModule availableSlotModel) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AddRequestUserActivity.this, "Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Gson gson = new Gson();
        String json = gson.toJson(availableSlotModel);
        Log.e("json...", json);
        //Call<NewAdvertisementModule> call = customer_interface.GetAvailableSlotsNew("bearer " + Token, availableSlotModel);
        Call<NewAdvertisementModule> call = customer_interface.GetAvailableSlotsNew("bearer " + Token, availableSlotModel);
        call.enqueue(new Callback<NewAdvertisementModule>() {
            @Override
            public void onResponse(Call<NewAdvertisementModule> call, Response<NewAdvertisementModule> response) {
                int statusCode = response.code();
                Log.e("Response Msg", response.toString());
                Log.e("Response Msg", call.toString());
                NewAdvertisementModule responseSlotModel = response.body();
                Gson gson = new Gson();
                Log.e("response....", gson.toJson(responseSlotModel));
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        if (responseSlotModel.getStatusCode() == 409) {
                            String msg = responseSlotModel.getDispayMessage();
                            showDialogToUser(msg);
                        } else {
                            Intent intent = new Intent(AddRequestUserActivity.this, EstimationPriceActivity.class);
                            NewAdvertisementModule slotModelDetails = new NewAdvertisementModule(responseSlotModel.getBrandID(),
                                    responseSlotModel.getBrandName(),
                                    responseSlotModel.getAdvertisementType(),
                                    responseSlotModel.getAdvertisementMainID(),
                                    responseSlotModel.getAdvertisementName(),
                                    responseSlotModel.getAdvertisementAreaID(),
                                    responseSlotModel.advertisementArea,
                                    responseSlotModel.getCreatedDateStr(),
                                    responseSlotModel.getcGSTPer(),
                                    responseSlotModel.getsGSTPer(), responseSlotModel.getiGSTPer(), responseSlotModel.getcGSTAmount(), responseSlotModel.getsGSTAmount(), responseSlotModel.getiGSTAmount(), responseSlotModel.getTaxAmount(), responseSlotModel.getFinalPrice(), responseSlotModel.getBookingExpiryDateStr(), responseSlotModel.getTotalPrice(), responseSlotModel.getTotalDiscountAmount(), responseSlotModel.getTotalDays(), responseSlotModel.getInvoiceUrl(), responseSlotModel.getAdTotalPrice());
                            Log.e("response....", gson.toJson(slotModelDetails));
                            intent.putExtra("data", (Serializable) slotModelDetails);
                            startActivity(intent);
                        }
                        break;

                    case 409:
                        progressDialog.dismiss();
                        /*String msg = responseSlotModel.getDispayMessage();
                        showDialogToUser(msg);*/
                        break;
                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(AddRequestUserActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AddRequestUserActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<NewAdvertisementModule> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(AddRequestUserActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AddRequestUserActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
       /* call.enqueue(new Callback<NewAdvertisementModule>() {
            @Override
            public void onResponse(Call<NewAdvertisementModule> call, Response<NewAdvertisementModule> response) {

                }
            }

            @Override
            public void onFailure(Call<NewAdvertisementModule> call, Throwable t) {
               *//* progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(AddRequestUserActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AddRequestUserActivity.this, "Bad response from server.. Try again later ");
                }*//*
            }
        });*/
    }

    private void getFullPageEstimation(NewAdvertisementModule availableSlotModel) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AddRequestUserActivity.this, "Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Gson gson = new Gson();
        String json = gson.toJson(availableSlotModel);
        Log.e("json...", json);
        //Call<NewAdvertisementModule> call = customer_interface.GetAvailableSlotsNew("bearer " + Token, availableSlotModel);
        Call<NewAdvertisementModule> call = customer_interface.FullPageAdSlots("bearer " + Token, availableSlotModel);
        call.enqueue(new Callback<NewAdvertisementModule>() {
            @Override
            public void onResponse(Call<NewAdvertisementModule> call, Response<NewAdvertisementModule> response) {
                int statusCode = response.code();
                Log.e("Response Msg", response.toString());
                Log.e("Response Msg", call.toString());
                NewAdvertisementModule responseSlotModel = response.body();
                Gson gson = new Gson();
                Log.e("response....", gson.toJson(responseSlotModel));
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        if (responseSlotModel.getStatusCode() == 409) {
                            String msg = responseSlotModel.getDispayMessage();
                            showDialogToUser(msg);
                        } else {
                            Intent intent = new Intent(AddRequestUserActivity.this, EstimationPriceActivity.class);
                            NewAdvertisementModule slotModelDetails = new NewAdvertisementModule(responseSlotModel.getBrandID(),
                                    responseSlotModel.getBrandName(),
                                    responseSlotModel.getAdvertisementType(),
                                    responseSlotModel.getAdvertisementMainID(),
                                    responseSlotModel.getAdvertisementName(),
                                    responseSlotModel.getAdvertisementAreaID(),
                                    responseSlotModel.advertisementArea,
                                    responseSlotModel.getCreatedDateStr(),
                                    responseSlotModel.getcGSTPer(),
                                    responseSlotModel.getsGSTPer(), responseSlotModel.getiGSTPer(), responseSlotModel.getcGSTAmount(), responseSlotModel.getsGSTAmount(), responseSlotModel.getiGSTAmount(), responseSlotModel.getTaxAmount(), responseSlotModel.getFinalPrice(), responseSlotModel.getBookingExpiryDateStr(), responseSlotModel.getTotalPrice(), responseSlotModel.getTotalDiscountAmount(), responseSlotModel.getTotalDays(), responseSlotModel.getInvoiceUrl(), responseSlotModel.getAdTotalPrice());
                            Log.e("response....", gson.toJson(slotModelDetails));
                            intent.putExtra("data", (Serializable) slotModelDetails);
                            startActivity(intent);
                        }
                        break;

                    case 409:
                        progressDialog.dismiss();
                        /*String msg = responseSlotModel.getDispayMessage();
                        showDialogToUser(msg);*/
                        break;
                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(AddRequestUserActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AddRequestUserActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<NewAdvertisementModule> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(AddRequestUserActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AddRequestUserActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
       /* call.enqueue(new Callback<NewAdvertisementModule>() {
            @Override
            public void onResponse(Call<NewAdvertisementModule> call, Response<NewAdvertisementModule> response) {

                }
            }

            @Override
            public void onFailure(Call<NewAdvertisementModule> call, Throwable t) {
               *//* progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(AddRequestUserActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AddRequestUserActivity.this, "Bad response from server.. Try again later ");
                }*//*
            }
        });*/
    }

    private void downloadInvoice(int adMainId) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AddRequestUserActivity.this, "Please wait");
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Preparing Invoice"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<String> call = customer_interface.GetInvoice("bearer " + Token, adMainId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        String url = response.body();
                        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                        pdfIntent.setDataAndType(Uri.parse(url), "application/pdf");
                        startActivity(pdfIntent);
                        break;
                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(AddRequestUserActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AddRequestUserActivity.this);
                        break;
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(AddRequestUserActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AddRequestUserActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void showDialogToUser(String msg) {
        mDialogMsg = new Dialog(AddRequestUserActivity.this);
        mDialogMsg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogMsg.setContentView(R.layout.custom_dialog_show_layout);
        mDialogMsg.setCancelable(true);
        TextView tv_msg = mDialogMsg.findViewById(R.id.tv_txt_msg);
        tv_msg.setText("" + msg);
        Button btnOk = mDialogMsg.findViewById(R.id.buttonOk);
        btnOk.setOnClickListener(view -> mDialogMsg.dismiss());
        mDialogMsg.show();
    }


    private void openDialogToSelectProduct() {
        mDialogProduct = new Dialog(this);
        mDialogProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //setting the dialog height and width to screen size
        mDialogProduct.getWindow().setLayout(width, height);

        mDialogProduct.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialogProduct.setContentView(R.layout.ads_product_item_layout);
        mDialogProduct.setCancelable(true);
        //set layout height and width to its screen size
        LinearLayout linearLayout = mDialogProduct.findViewById(R.id.mainLayout);
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.height = height;
        params.width = width;
        linearLayout.setLayoutParams(params);

        edDialogProduct = (EditText) mDialogProduct.findViewById(R.id.edState);
        AppCompatImageView Clear_txt_btn = mDialogProduct.findViewById(R.id.clear_txt_Prise);
        ImageView imageView = (ImageView) mDialogProduct.findViewById(R.id.cancel_category);
        Button btnSearch = (Button) mDialogProduct.findViewById(R.id.btnSearch);

        recyclerViewChild = (RecyclerView) mDialogProduct.findViewById(R.id.recyclerCategory);
        recyclerViewChild.setHasFixedSize(true);
        recyclerViewChild.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        childProductAdapter = new ChildCatgoryAdapter(AddRequestUserActivity.this, childCategoryProducts, AddRequestUserActivity.this);
        mDialogProduct.show();
        //getChildCategoryListFromServer();

        imageView.setOnClickListener(v -> mDialogProduct.dismiss());
        Clear_txt_btn.setOnClickListener(view -> {
            if (!edDialogProduct.getText().toString().isEmpty()) {
                edDialogProduct.getText().clear();
                Clear_txt_btn.setVisibility(View.GONE);
            }
        });
        btnSearch.setOnClickListener(view -> {

            if (edDialogProduct.getText().toString().isEmpty()) {
                CustomToast.showToast(AddRequestUserActivity.this, "Enter product/service name");
            } else {
                getChildCategoryListFromServer(edDialogProduct.getText().toString().trim());
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
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

    private void openDialogToSelectBrand() {
        mDialogBrand = new Dialog(this);
        mDialogBrand.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //setting the height and width to dialog
        mDialogBrand.getWindow().setLayout(width, height);

        mDialogBrand.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialogBrand.setContentView(R.layout.dialog_brand_item_layout);
        mDialogBrand.setCancelable(true);
        //set layout height and width to its screen size
        LinearLayout linearLayout = mDialogBrand.findViewById(R.id.brandMainLayout);
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.height = height;
        params.width = width;
        linearLayout.setLayoutParams(params);

        edDialogBrand = (EditText) mDialogBrand.findViewById(R.id.edBrand);
        AppCompatImageView Clear_txt_btn = mDialogBrand.findViewById(R.id.clear_txt_Prise);
        ImageView imageView = (ImageView) mDialogBrand.findViewById(R.id.cancel_category);

        recyclerViewBrand = (RecyclerView) mDialogBrand.findViewById(R.id.recyclerBrand);
        recyclerViewBrand.setHasFixedSize(true);
        recyclerViewBrand.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adBrandSelectorAdapter = new AdBrandSelectorAdapter(AddRequestUserActivity.this, brandList, AddRequestUserActivity.this);
        recyclerViewBrand.setAdapter(adBrandSelectorAdapter);
        mDialogBrand.show();

        imageView.setOnClickListener(v -> mDialogBrand.dismiss());
        Clear_txt_btn.setOnClickListener(view -> {
            if (!edDialogBrand.getText().toString().isEmpty()) {
                edDialogBrand.getText().clear();
                Clear_txt_btn.setVisibility(View.GONE);
            }
        });

        edDialogBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adBrandSelectorAdapter.getFilter().filter(s);
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
                    adBrandSelectorAdapter.getFilter().filter(s);

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

    private void getBrands() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AddRequestUserActivity.this, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<Brand>> call = customer_interface.GetBrands("bearer " + Token);
        call.enqueue(new Callback<List<Brand>>() {
            @Override
            public void onResponse(Call<List<Brand>> call, Response<List<Brand>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        brandList = response.body();
                        if (brandList.isEmpty()) {
                            CustomToast.showToast(AddRequestUserActivity.this, "No such brand found.");
                        } else {
                            openDialogToSelectBrand();
                        }
                        break;
                    case 404:
                        progressDialog.dismiss();
                        CustomToast.showToast(AddRequestUserActivity.this, "No such brand found.");
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(AddRequestUserActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AddRequestUserActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<Brand>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(AddRequestUserActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AddRequestUserActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void getChildCategoryListFromServer(String childName) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AddRequestUserActivity.this, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<ChildCategoryProduct>> call = customer_interface.getChildCategoryProduct("bearer " + Token, childName, false, true);
        call.enqueue(new Callback<List<ChildCategoryProduct>>() {
            @Override
            public void onResponse(Call<List<ChildCategoryProduct>> call, Response<List<ChildCategoryProduct>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        childCategoryProducts = new ArrayList<ChildCategoryProduct>(response.body());
                        if (childCategoryProducts.isEmpty()) {
                            CustomToast.showToast(AddRequestUserActivity.this, "No such product found..");
                        } else {
                            childProductAdapter = new ChildCatgoryAdapter(AddRequestUserActivity.this, childCategoryProducts, AddRequestUserActivity.this);
                            recyclerViewChild.setAdapter(childProductAdapter);
                            childProductAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 404:
                        progressDialog.dismiss();
                        CustomToast.showToast(AddRequestUserActivity.this, "No such product found.");
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(AddRequestUserActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AddRequestUserActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<ChildCategoryProduct>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(AddRequestUserActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AddRequestUserActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void showSelectedStates(List<State> selectStateList) {
        mDialogShowStates = new Dialog(AddRequestUserActivity.this);
        mDialogShowStates.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogShowStates.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialogShowStates.setContentView(R.layout.show_states_dialog);
        mDialogShowStates.setCancelable(true);
        ImageView ivCancelIcon = mDialogShowStates.findViewById(R.id.iv_cancel_icon);
        mDialogShowStates.show();
        ChipGroup chipGroup = mDialogShowStates.findViewById(R.id.chipgroup);
        ivCancelIcon.setOnClickListener(view -> mDialogShowStates.dismiss());
        if (chipGroup.getChildCount() > 0) {
            chipGroup.removeAllViews();
        }

        List<State> withoutDuplicate = new ArrayList<>();
        int i = 0;

        for (State state : selectStateList) {
            if (i == 0) {
                withoutDuplicate.add(state);

            } else {
                int k = 0;
                for (State cat : withoutDuplicate) {
                    if (cat.getStateID() == state.getStateID()) {
                        k = 1;
                        break;
                    }
                }
                if (k != 1) {
                    withoutDuplicate.add(state);
                }
            }
            i++;

        }

        for (State category : withoutDuplicate) {
            Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.state_chip_item, chipGroup, false);
            mChip.findViewById(R.id.chips);
            mChip.setText("" + category.getStateName());
            Chip finalChip = mChip;
            mChip.setCloseIconVisible(false);
            chipGroup.addView(mChip);

        }
        finalSelectedStateList = withoutDuplicate;
        selectedStateList = withoutDuplicate;

    }

    private void showSelectedDistrict(List<District> selectDistrictList) {
        mDialogShowDistrict = new Dialog(AddRequestUserActivity.this);
        mDialogShowDistrict.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogShowDistrict.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialogShowDistrict.setContentView(R.layout.show_states_dialog);
        mDialogShowDistrict.setCancelable(true);
        ImageView ivCancelIcon = mDialogShowDistrict.findViewById(R.id.iv_cancel_icon);
        mDialogShowDistrict.show();
        ChipGroup chipGroup = mDialogShowDistrict.findViewById(R.id.chipgroup);
        ivCancelIcon.setOnClickListener(view -> mDialogShowDistrict.dismiss());
        if (chipGroup.getChildCount() > 0) {
            chipGroup.removeAllViews();
        }

        List<District> withoutDuplicate = new ArrayList<>();
        int i = 0;

        for (District district : selectDistrictList) {
            if (i == 0) {
                withoutDuplicate.add(district);

            } else {
                int k = 0;
                for (District cat : withoutDuplicate) {
                    if (cat.getDistrictID() == district.getDistrictID()) {
                        k = 1;
                        break;
                    }
                }
                if (k != 1) {
                    withoutDuplicate.add(district);
                }
            }
            i++;

        }

        for (District category : withoutDuplicate) {
            Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.state_chip_item, chipGroup, false);
            mChip.findViewById(R.id.chips);
            mChip.setText("" + category.getDistrictName());
            Chip finalChip = mChip;
            mChip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    category.setChecked(false);
                    chipGroup.removeView(finalChip);
                    withoutDuplicate.remove(category);
                    edDistrictName.setText("" + withoutDuplicate.toString().replace("[", "").replace("]", ""));
                }
            });
            chipGroup.addView(mChip);
        }
        finalSelectedDistrictList = withoutDuplicate;
        selectedDistrictList = withoutDuplicate;
    }

    private void showSelectedCity(List<CityAD> selectCityList) {
        mDialogShowCity = new Dialog(AddRequestUserActivity.this);
        mDialogShowCity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogShowCity.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialogShowCity.setContentView(R.layout.show_states_dialog);
        mDialogShowCity.setCancelable(true);
        ImageView ivCancelIcon = mDialogShowCity.findViewById(R.id.iv_cancel_icon);
        mDialogShowCity.show();
        ChipGroup chipGroup = mDialogShowCity.findViewById(R.id.chipgroup);
        ivCancelIcon.setOnClickListener(view -> mDialogShowCity.dismiss());
        if (chipGroup.getChildCount() > 0) {
            chipGroup.removeAllViews();
        }

        List<CityAD> withoutDuplicate = new ArrayList<>();
        int i = 0;

        for (CityAD city : selectCityList) {
            if (i == 0) {
                withoutDuplicate.add(city);

            } else {
                int k = 0;
                for (CityAD cat : withoutDuplicate) {
                    if (cat.getStatewithCityID() == city.getStatewithCityID()) {
                        k = 1;
                        break;
                    }
                }
                if (k != 1) {
                    withoutDuplicate.add(city);
                }
            }
            i++;

        }

        for (CityAD city : withoutDuplicate) {
            Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.state_chip_item, chipGroup, false);
            mChip.findViewById(R.id.chips);
            mChip.setText("" + city.getVillageLocalityname());
            Chip finalChip = mChip;
            mChip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    city.setChecked(false);
                    chipGroup.removeView(finalChip);
                    withoutDuplicate.remove(city);
                    edCityName.setText("" + withoutDuplicate.toString().replace("[", "").replace("]", ""));
                }
            });
            chipGroup.addView(mChip);
        }
        finalSelectedCityList = withoutDuplicate;
        selectedCityList = withoutDuplicate;
    }

    private void showSelectedTierCity(List<CityAD> selectCityList) {
        mDialogShowCity = new Dialog(AddRequestUserActivity.this);
        mDialogShowCity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogShowCity.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialogShowCity.setContentView(R.layout.show_states_dialog);
        mDialogShowCity.setCancelable(true);
        ImageView ivCancelIcon = mDialogShowCity.findViewById(R.id.iv_cancel_icon);
        mDialogShowCity.show();
        ChipGroup chipGroup = mDialogShowCity.findViewById(R.id.chipgroup);
        ivCancelIcon.setOnClickListener(view -> mDialogShowCity.dismiss());
        if (chipGroup.getChildCount() > 0) {
            chipGroup.removeAllViews();
        }

        List<CityAD> withoutDuplicate = new ArrayList<>();
        int i = 0;

        for (CityAD city : selectCityList) {
            if (i == 0) {
                withoutDuplicate.add(city);

            } else {
                int k = 0;
                for (CityAD cat : withoutDuplicate) {
                    if (cat.getStatewithCityID() == city.getStatewithCityID()) {
                        k = 1;
                        break;
                    }
                }
                if (k != 1) {
                    withoutDuplicate.add(city);
                }
            }
            i++;

        }
        for (CityAD city : withoutDuplicate) {
            Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.state_chip_item, chipGroup, false);
            mChip.findViewById(R.id.chips);
            mChip.setText("" + city.getVillageLocalityname());
            mChip.setCloseIconVisible(false);
            Chip finalChip = mChip;
            mChip.setOnCloseIconClickListener(view -> {
                city.setChecked(false);
                chipGroup.removeView(finalChip);
                withoutDuplicate.remove(city);
                edTierCity.setText("" + withoutDuplicate.toString().replace("[", "").replace("]", ""));
            });
            chipGroup.addView(mChip);
        }
        finalSelectedCityList = withoutDuplicate;
        selectedCityList = withoutDuplicate;
    }


    private void launchFromDateSelector(EditText edSelectFromDate) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {

                    String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    String converted = Utility.convertCalendarFormat(selectedDate);
                    edSelectFromDate.setText(converted);
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(CurrentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal1.add(Calendar.MONTH, 4);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(cal1.getTimeInMillis());

    }

    private void launchToDateSelector(EditText edSelectToDate) {
        final Calendar c1 = Calendar.getInstance();
        mYear = c1.get(Calendar.YEAR);
        mMonth = c1.get(Calendar.MONTH);
        mDay = c1.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog1 = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {

                    String selectedDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    String converted = Utility.convertCalendarFormat(selectedDate);
                    edSelectToDate.setText(converted);
                }, mYear, mMonth, mDay);
        datePickerDialog1.show();
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(CurrentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePickerDialog1.getDatePicker().setMinDate(cal.getTimeInMillis());
        cal1.add(Calendar.MONTH, 4);
        datePickerDialog1.getDatePicker().setMaxDate(cal1.getTimeInMillis());
    }


    private void getAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddRequestUserActivity.this);
        builder1.setCancelable(false);
        builder1.setMessage("Would you like to add more states?");

        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> openDialogToSelectState());

        builder1.setNegativeButton(
                "No",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.setCanceledOnTouchOutside(false);
        alert11.show();
    }

    private void openDialogToSelectState() {
        if (areaAdapter.getTrueResultList().size() == 0) {
            CustomToast.showToast(AddRequestUserActivity.this, "Please select advertisement area");
        } else {
            mDialogState = new Dialog(AddRequestUserActivity.this);

            //setting the dialog  height and width to screen size
            mDialogState.getWindow().setLayout(width, height);

            mDialogState.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialogState.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialogState.setContentView(R.layout.ads_state_item_layout);
            mDialogState.setCancelable(true);
            //set layout height and width to its screen size
            LinearLayout linearLayout = mDialogState.findViewById(R.id.mainLayout);
            ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
            params.height = height;
            params.width = width;
            linearLayout.setLayoutParams(params);

            EditText edStateSelect = mDialogState.findViewById(R.id.edState);
            AppCompatImageView Btnclear_txt_Prise = mDialogState.findViewById(R.id.clear_txt_Prise);

            BtnSelectedState = mDialogState.findViewById(R.id.btn_state_submit);
            recyclerViewState = mDialogState.findViewById(R.id.recyclerstate);
            ImageView ivCancelIcon = mDialogState.findViewById(R.id.cancel_icon);
            recyclerViewState.setHasFixedSize(true);
            //setting recyclerview height width to screen size
            recyclerViewState.setLayoutManager(new LinearLayoutManager(AddRequestUserActivity.this));
            ViewGroup.LayoutParams params1 = recyclerViewState.getLayoutParams();
            params1.height = height - 280;
            recyclerViewState.setLayoutParams(params1);
            adStateSelectorAdapter = new AdStateSelectorAdapter(AddRequestUserActivity.this, stateList, AddRequestUserActivity.this);
            recyclerViewState.setAdapter(adStateSelectorAdapter);
            mDialogState.show();
            BtnSelectedState.setOnClickListener(this);
            ivCancelIcon.setOnClickListener(view -> mDialogState.dismiss());

            Btnclear_txt_Prise.setOnClickListener(view -> {
                if (!edStateSelect.getText().toString().isEmpty()) {
                    edStateSelect.getText().clear();
                }
            });


            edStateSelect.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    try {
                        adStateSelectorAdapter.getFilter().filter(s);
                        if (s.length() <= 0) {
                            Btnclear_txt_Prise.setVisibility(View.GONE);
                        } else {
                            Btnclear_txt_Prise.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        adStateSelectorAdapter.getFilter().filter(s);
                        if (s.length() <= 0) {
                            Btnclear_txt_Prise.setVisibility(View.GONE);
                        } else {
                            Btnclear_txt_Prise.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void openDialogToSelectDistrict() {
        if (selectedStateList.size() == 0 | selectedStateList.isEmpty()) {
            CustomToast.showToast(AddRequestUserActivity.this, "Please select state");
        } else {
            mDialogDistrict = new Dialog(AddRequestUserActivity.this);
            //setting the dialog height and width to screen size
            mDialogDistrict.getWindow().setLayout(width, height);

            mDialogDistrict.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialogDistrict.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialogDistrict.setContentView(R.layout.ads_district_item_layout);
            mDialogDistrict.setCancelable(true);
            //set layout height and width to its screen size
            LinearLayout linearLayout = mDialogDistrict.findViewById(R.id.districtMainLayout);
            ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
            params.height = height;
            params.width = width;
            linearLayout.setLayoutParams(params);

            EditText edDistrictSelect = mDialogDistrict.findViewById(R.id.edState);
            AppCompatImageView Btnclear_txt_Prise = mDialogDistrict.findViewById(R.id.clear_txt_Prise);
            CheckBox ckSelectAllOption = mDialogDistrict.findViewById(R.id.ck_select_all);
            BtnSelectedDistrict = mDialogDistrict.findViewById(R.id.btn_district_submit);
            recyclerViewDistrict = mDialogDistrict.findViewById(R.id.recyclerstate);
            ImageView ivCancelIcon = mDialogDistrict.findViewById(R.id.cancel_icon);

            recyclerViewDistrict.setHasFixedSize(true);
            recyclerViewDistrict.setLayoutManager(new LinearLayoutManager(AddRequestUserActivity.this));
            ViewGroup.LayoutParams params1 = recyclerViewDistrict.getLayoutParams();
            params1.height = height - 300;
            recyclerViewDistrict.setLayoutParams(params1);

            //shweta
            adDistrictSelectorAdapter = new AdDistrictSelectorAdapter(AddRequestUserActivity.this, districtList, AddRequestUserActivity.this);
            recyclerViewDistrict.setAdapter(adDistrictSelectorAdapter);


            mDialogDistrict.show();
            BtnSelectedDistrict.setOnClickListener(this);
            ivCancelIcon.setOnClickListener(view -> mDialogDistrict.dismiss());

            Btnclear_txt_Prise.setOnClickListener(view -> {
                if (!edDistrictSelect.getText().toString().isEmpty()) {
                    edDistrictSelect.getText().clear();
                }
            });

            ckSelectAllOption.setOnClickListener(view -> {
                if (ckSelectAllOption.isChecked()) {
                    for (District district : districtList) {
                        district.setChecked(true);
                        onItemSelected(district);
                    }
                } else {
                    for (District district : districtList) {
                        district.setChecked(false);
                        onItemSelected(district);
                    }
                }
                adDistrictSelectorAdapter.notifyDataSetChanged();
            });

            edDistrictSelect.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    try {
                        adDistrictSelectorAdapter.getFilter().filter(s);
                        String dName = s.toString();
                        if (dName.length() <= 0) {
                            Btnclear_txt_Prise.setVisibility(View.GONE);
                        } else if (dName.length() >= 3) {
                            getDistrictList(dName);
                            Btnclear_txt_Prise.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        adDistrictSelectorAdapter.getFilter().filter(s);
                        if (s.length() <= 0) {

                            Btnclear_txt_Prise.setVisibility(View.GONE);
                        } else {
                            Btnclear_txt_Prise.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void openDialogToSelectCity() {
        if (selectedStateList.size() == 0 | selectedStateList.isEmpty()) {
            CustomToast.showToast(AddRequestUserActivity.this, "Please select state");
        } else {
            mDialogCity = new Dialog(AddRequestUserActivity.this);
            mDialogCity.requestWindowFeature(Window.FEATURE_NO_TITLE);

            //setting the height and width to dialog
            mDialogCity.getWindow().setLayout(width, height);

            mDialogCity.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialogCity.setContentView(R.layout.ads_other_city_item_layout);
            mDialogCity.setCancelable(true);
            //set layout height and width to its screen size
            LinearLayout linearLayout = mDialogCity.findViewById(R.id.mainLayout);
            ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
            params.height = height;
            params.width = width;
            linearLayout.setLayoutParams(params);

            EditText edCitySelect = mDialogCity.findViewById(R.id.edCity);
            AppCompatImageView Btnclear_txt_Prise = mDialogCity.findViewById(R.id.clear_txt_Prise);

            BtnSelectedCity = mDialogCity.findViewById(R.id.btn_other_city_submit);
            ImageView ivCancelIcon = mDialogCity.findViewById(R.id.cancel_icon);
            recyclerViewCity = mDialogCity.findViewById(R.id.recyclerCity);
            recyclerViewCity.setHasFixedSize(true);
            recyclerViewCity.setLayoutManager(new LinearLayoutManager(AddRequestUserActivity.this));
            ViewGroup.LayoutParams params1 = recyclerViewCity.getLayoutParams();
            params1.height = height - 280;
            recyclerViewCity.setLayoutParams(params1);
            adCitySelectorAdapter = new AdCitySelectorAdapter(AddRequestUserActivity.this, cityList, AddRequestUserActivity.this);
            recyclerViewCity.setAdapter(adCitySelectorAdapter);
            mDialogCity.show();
            BtnSelectedCity.setOnClickListener(this);
            ivCancelIcon.setOnClickListener(view -> mDialogCity.dismiss());

            Btnclear_txt_Prise.setOnClickListener(view -> {
                if (!edCitySelect.getText().toString().isEmpty()) {
                    edCitySelect.getText().clear();
                    Btnclear_txt_Prise.setVisibility(View.GONE);
                }
            });

            edCitySelect.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                    try {
                        adCitySelectorAdapter.getFilter().filter(s);
                        String cityName = s.toString();
                        if (s.length() <= 0) {
                            Btnclear_txt_Prise.setVisibility(View.GONE);
                        } else if (cityName.length() >= 3) {
                            getCityList(cityName);
                            Btnclear_txt_Prise.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        adCitySelectorAdapter.getFilter().filter(s);
                        if (s.length() <= 0) {

                            Btnclear_txt_Prise.setVisibility(View.GONE);
                        } else {
                            Btnclear_txt_Prise.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void openDialogToSelectTierCity() {
        mDialogCity = new Dialog(AddRequestUserActivity.this);
        mDialogCity.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //setting the height and width to dialog
        mDialogCity.getWindow().setLayout(width, height);

        mDialogCity.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialogCity.setContentView(R.layout.ads_city_item_layout);
        mDialogCity.setCancelable(true);
        //set layout height and width to its screen size
        LinearLayout linearLayout = mDialogCity.findViewById(R.id.mainLayout);
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.height = height;
        params.width = width;
        linearLayout.setLayoutParams(params);

        EditText edCitySelect = mDialogCity.findViewById(R.id.edCity);
        AppCompatImageView Btnclear_txt_Prise = mDialogCity.findViewById(R.id.clear_txt_Prise);

        BtnSelectedCity = mDialogCity.findViewById(R.id.btn_city_submit);
        ImageView ivCancelIcon = mDialogCity.findViewById(R.id.cancel_icon);
        recyclerViewCity = mDialogCity.findViewById(R.id.recyclerCity);
        recyclerViewCity.setHasFixedSize(true);
        recyclerViewCity.setLayoutManager(new LinearLayoutManager(AddRequestUserActivity.this));
        ViewGroup.LayoutParams params1 = recyclerViewCity.getLayoutParams();
        params1.height = height - 280;
        recyclerViewCity.setLayoutParams(params1);
        adCitySelectorAdapter = new AdCitySelectorAdapter(AddRequestUserActivity.this, cityList, AddRequestUserActivity.this);
        recyclerViewCity.setAdapter(adCitySelectorAdapter);
        mDialogCity.show();
        BtnSelectedCity.setOnClickListener(this);
        ivCancelIcon.setOnClickListener(view -> mDialogCity.dismiss());

        Btnclear_txt_Prise.setOnClickListener(view -> {
            if (!edCitySelect.getText().toString().isEmpty()) {
                edCitySelect.getText().clear();
                Btnclear_txt_Prise.setVisibility(View.GONE);
            }
        });

        edCitySelect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                try {
                    adCitySelectorAdapter.getFilter().filter(s);
                    String cityName = s.toString();
                    if (s.length() <= 0) {
                        Btnclear_txt_Prise.setVisibility(View.GONE);
                    } else {
                        Btnclear_txt_Prise.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    adCitySelectorAdapter.getFilter().filter(s);
                    if (s.length() <= 0) {
                        Btnclear_txt_Prise.setVisibility(View.GONE);
                    } else {
                        Btnclear_txt_Prise.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getDistrictList(String dName) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AddRequestUserActivity.this, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Log.e("selectedStateList...", selectedStateList.toString());
        Gson gson = new Gson();
        Log.e("District body....", gson.toJson(selectedStateList));
        Call<List<District>> call = customer_interface.GetDistrict("bearer " + Token, dName, selectedStateList);
        call.enqueue(new Callback<List<District>>() {
            @Override
            public void onResponse(Call<List<District>> call, Response<List<District>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        districtList = response.body();
                        if (districtList.isEmpty()) {
                            CustomToast.showToast(AddRequestUserActivity.this, "No district found");
                        } else {
                            adDistrictSelectorAdapter = new AdDistrictSelectorAdapter(AddRequestUserActivity.this, districtList, AddRequestUserActivity.this);
                            recyclerViewDistrict.setAdapter(adDistrictSelectorAdapter);
                            adDistrictSelectorAdapter.notifyDataSetChanged();
                        }
                        break;

                    case 404:
                        progressDialog.dismiss();
                        CustomToast.showToast(AddRequestUserActivity.this, "No district found");
                        break;

                    case 500:
                        CustomToast.showToast(AddRequestUserActivity.this, "Server Error");
                        progressDialog.dismiss();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AddRequestUserActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<District>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(AddRequestUserActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AddRequestUserActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void getStateList() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<State>> call = customer_interface.getAllStates(/*"bearer " + Token*/);
        call.enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        stateList = new ArrayList<>(response.body());
                        if (stateList.isEmpty()) {
                        } else {
                        }
                        break;
                    case 404:
                        CustomToast.showToast(AddRequestUserActivity.this, "No State found");
                        break;
                    case 500:
                        CustomToast.showToast(AddRequestUserActivity.this, "Server Error");
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(AddRequestUserActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                //  progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(AddRequestUserActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AddRequestUserActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void getCityList(String cityName) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(AddRequestUserActivity.this, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<CityAD>> call = customer_interface.GetCitiesAD("bearer " + Token, cityName, selectedStateList);
        call.enqueue(new Callback<List<CityAD>>() {
            @Override
            public void onResponse(Call<List<CityAD>> call, Response<List<CityAD>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        cityList = new ArrayList<>(response.body());
                        if (cityList.isEmpty()) {
                            CustomToast.showToast(AddRequestUserActivity.this, "No city found");
                        } else {
                            adCitySelectorAdapter = new AdCitySelectorAdapter(AddRequestUserActivity.this, cityList, AddRequestUserActivity.this);
                            recyclerViewCity.setAdapter(adCitySelectorAdapter);
                        }
                        break;

                    case 404:
                        CustomToast.showToast(AddRequestUserActivity.this, "No city found");
                        progressDialog.dismiss();
                        break;


                    case 500:
                        CustomToast.showToast(AddRequestUserActivity.this, "Server Error");
                        progressDialog.dismiss();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(AddRequestUserActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<CityAD>> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(AddRequestUserActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AddRequestUserActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void getTierCityList() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<CityAD>> call = customer_interface.getTierCities("bearer " + Token);
        call.enqueue(new Callback<List<CityAD>>() {
            @Override
            public void onResponse(Call<List<CityAD>> call, Response<List<CityAD>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        cityList = new ArrayList<>(response.body());
                        if (cityList.isEmpty()) {
                            CustomToast.showToast(AddRequestUserActivity.this, "No city found");
                        } else {
                        }
                        break;
                    case 404:
                        CustomToast.showToast(AddRequestUserActivity.this, "No city found");
                        break;
                    case 500:
                        CustomToast.showToast(AddRequestUserActivity.this, "Server Error");
                        break;
                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(AddRequestUserActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<CityAD>> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(AddRequestUserActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(AddRequestUserActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    // product item selected
    @Override
    public void onItemSelected(ChildCategoryProduct childCreation) {
        selectedProductId = childCreation.getChildCategoryId();
        selectedCategoryProductId = childCreation.getCategoryProductID();
        edAdProduct.setText("" + childCreation.getChildCategoryName());
        mDialogProduct.dismiss();
    }

    //brand item selected
    @Override
    public void onItemSelected(Brand brand) {
        selectedBrandId = brand.getBrandID();
        brandName = brand.getBrandName();
        edBrand.setText("" + brand.getBrandName());
        mDialogBrand.dismiss();
        if (brand.getBrandName().equals("Create New..")) {
            edOtherBrand.setVisibility(View.VISIBLE);
        } else {
            edOtherBrand.setVisibility(View.GONE);
        }
    }

    //state item selected
    @Override
    public void onItemSelected(State State) {}

    //district item selected
    @Override
    public void onItemSelected(District district) {}

    //city item selected
    @Override
    public void onItemSelected(CityAD city) {}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AdvertisementMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        AddRequestUserActivity.this.finish();
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

    public boolean checkDates(String d1, String d2) {
        boolean b = false;
        try {
            //If start date is after the end date
            if (dfDate.parse(d1).before(dfDate.parse(d2))) {
                b = true;//If start date is before end date
            } else b = dfDate.parse(d1).equals(dfDate.parse(d2));//If two dates are equal
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public void onSlotSelected() {
        selectedSlotModelList = timeSlotAdapter.getSelectedSlots();
        if (selectedSlotModelList.size() == 0) {
            tvslotSelected.setText("");
            tvslotsSelected.setText("");
        }
        if (selectedSlotModelList.size() == 1) {
            tvslotSelected.setText("One time slot selected");
            tvslotsSelected.setText("One time slot selected");
        }
        if (selectedSlotModelList.size() == 2) {
            tvslotSelected.setText("Two time slots selected");
            tvslotsSelected.setText("Two time slots selected");
        }
        if (selectedSlotModelList.size() == 3) {
            tvslotSelected.setText("Three time slots selected");
            tvslotsSelected.setText("Three time slots selected");
        }
    }
}