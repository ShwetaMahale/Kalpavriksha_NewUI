package com.mwbtech.dealer_register.Profile.BillingDetails;


import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static com.mwbtech.dealer_register.Dashboard.DashboardActivity.creation;
import static com.mwbtech.dealer_register.Dashboard.DashboardActivity.dealerRegister;
import static com.mwbtech.dealer_register.Dashboard.DashboardActivity.isUpdate;
import static com.mwbtech.dealer_register.Profile.ProfileMain.UpdateMainActivity.prefManager;
import static com.mwbtech.dealer_register.Profile.ProfileMain.UpdateMainActivity.updateDealerRegister;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.CityAdapter;
import com.mwbtech.dealer_register.Adapter.StateAdapter;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.LoginRegister.LoginActivity;
import com.mwbtech.dealer_register.PojoClass.City;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.PojoClass.State;
import com.mwbtech.dealer_register.PojoClass.StateCityResponse;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.Utils.CommonUtils;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.SessionDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateBillingAddress extends Fragment implements View.OnClickListener, CityAdapter.CitySelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Button btnNext, btnExit, btnMap;
    View billView;
    int pos = 2, CustID;
    CallToFragment callToFragment;
    String Token;
    EditText spState, spCity;
    Boolean isCitySelected, isDistrictSelected, isStateSelected, isNationalSelected, isUpdatedOnce = false;

    StateAdapter stateAdapter;
    CityAdapter cityAdapter;

    City city = null;
    State state = null;

    boolean cityBit = false, stateBit = false, CountryBit = false, districtBit = false,
            shouldUpdateAddress = false, isStateFound = false, isStateChanged = false, isTypingPincode = false;
    Customer_Interface customer_interface;
    List<State> stateList;
    List<City> cityList;


    Dialog mDialogState, mDialogCity, mDialogOther, mDialogExit;
    EditText dialogState, dialogCity;
    ListView listViewState;
    RecyclerView recyclerViewCity;
    String TaxType, GSTNumber, PanNumber, BankName, BranchBank, BankCityName, AccountNumber, BankIFSCCode;

    int BusinessDemandID;
    EditText edBillingAddress, edArea, edPincode;
    int StateId = 0, CityId = 0, DistrictID = 0;

    TextView chCity, chState, chCountry, chDistrict;
    ImageView imgCity, imgState, imgNational, imgDistrict;
    LinearLayout layoutCity, layoutState, layoutNational, layoutDistrict;

    Dialog mDialogOpen;
    private GoogleMap mMap;
    LinearLayout linearLayout;
    Location mLastLocation = new Location(LocationManager.NETWORK_PROVIDER);
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 599;
    LocationManager locationManager;
    TextView tvLatLong;
    double Lat, lng;
    TextView tvAtmName, tvAtmArea, tvAreaLatitude, tvAreaLongitude;
    LatLng latLng;
    private final Map<Marker, Map<String, Object>> markers = new HashMap<>();
    private final Map<String, Object> dataModel = new HashMap<>();
    String areaName;
    MarkerOptions markerOptions;
    File UserImageFile;

    File imgFile;

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            if (activity instanceof CallToFragment) {
                callToFragment = (CallToFragment) activity;
            } else {
                throw new RuntimeException(activity
                        + " must implement OnGreenFragmentListener");
            }
        } catch (ClassCastException e) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        billView = inflater.inflate(R.layout.billing_address, null);
        checkLocationPermission();


        edBillingAddress = billView.findViewById(R.id.edBillAddress);
        edArea = billView.findViewById(R.id.edArea);
        edPincode = billView.findViewById(R.id.edPincode);
        spState = billView.findViewById(R.id.spinnerState);
        spCity = billView.findViewById(R.id.spinnerCity);

        tvLatLong = billView.findViewById(R.id.latlong);
        linearLayout = billView.findViewById(R.id.linear);
        btnNext = billView.findViewById(R.id.btnNext);
        btnExit = billView.findViewById(R.id.btnExit);
        btnExit.setText(R.string.txt_save_exit);
        btnNext.setText(R.string.text_next);
        btnMap = billView.findViewById(R.id.btnMap);

        layoutState = billView.findViewById(R.id.layout_state);
        layoutCity = billView.findViewById(R.id.layout_city);
        layoutDistrict = billView.findViewById(R.id.layout_district);
        layoutNational = billView.findViewById(R.id.layout_national);

        imgState = billView.findViewById(R.id.img_state);
        imgCity = billView.findViewById(R.id.img_city);
        imgDistrict = billView.findViewById(R.id.img_district);
        imgNational = billView.findViewById(R.id.img_national);

        chCity = billView.findViewById(R.id.chCity);
        chDistrict = billView.findViewById(R.id.chDistrict);
        chState = billView.findViewById(R.id.chState);
        chCountry = billView.findViewById(R.id.chNational);

        btnExit.setOnClickListener(this);
        btnMap.setOnClickListener(this::onClick);
        btnNext.setOnClickListener(this);
        spState.setOnClickListener(this);
        spCity.setOnClickListener(this);
        layoutDistrict.setOnClickListener(this);
        layoutCity.setOnClickListener(this);
        layoutState.setOnClickListener(this);
        layoutNational.setOnClickListener(this);
        try {
            CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        Token = prefManager.getToken().get(TOKEN);
        sharePreferenceData();

        edPincode.setOnTouchListener((v, event) -> {
            isTypingPincode = true;
            return false;
        });
        //        edPincode.setOnClickListener(v -> isTypingPincode = true);
        edPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6 && isTypingPincode) {
                    /*getad(s.toString());*/
                    getStateCityFromPincode(s.toString().trim());
                isTypingPincode = false;
                }
            }
        });
        return billView;
    }

    private void sharePreferenceData() {
        if(isUpdatedOnce){
            setCityAsSelected(isCitySelected);
            setDistrictAsSelected(isDistrictSelected);
            setStateAsSelected(isStateSelected);
            setNationalAsSelected(isNationalSelected);
            return;
        }

        try {
            if (creation.getBillingAddress() != null) {
                if (!TextUtils.isEmpty(creation.getBillingAddress())) {
                    edBillingAddress.setText("" + creation.getBillingAddress());
                } else {
                    edBillingAddress.setText("");
                }
                if (!TextUtils.isEmpty(creation.getArea())) {
                    edArea.setText("" + creation.getArea());
                } else {
                    edArea.setText("");
                }
                spCity.setText("" + creation.getCity());
                spState.setText("" + creation.getState());


                setCityAsSelected(creation.getInterstCity());
                setDistrictAsSelected(creation.getInterstDistrict());
                setStateAsSelected(creation.getInterstState());
                setNationalAsSelected(creation.getInterstCountry());
//                chCity.setChecked(creation.getInterstCity());
//                chState.setChecked(creation.getInterstState());
//                chCountry.setChecked(creation.getInterstCountry());
//                chDistrict.setChecked(creation.getInterstDistrict());
                city = creation.getCity();
                state = creation.getState();
                if (state != null) StateId = state.getStateID();
                if (city != null) CityId = city.getID();

                TaxType = creation.getRegistrationType();
                GSTNumber = creation.getTinNumber();
                PanNumber = creation.getPanNumber();
                BankName = creation.getBankname();
                BranchBank = creation.getBankBranchName();
                BankCityName = creation.getBankCity();
                AccountNumber = creation.getAccountnumber();
                BankIFSCCode = creation.getiFSCCode();

                if (creation.getPincode() != null && !TextUtils.isEmpty(creation.getPincode())) {
                    edPincode.setText("" + creation.getPincode());
//                    getad(creation.getPincode());
                } else {
                    edPincode.setText("");
                }
                if (creation.getUserImageFile() != null) {
                    UserImageFile = creation.getUserImageFile();
                }
                BusinessDemandID = creation.getBusinessDemandID();

            } else {
                updateDealerRegister = prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);
                edBillingAddress.setText("" + updateDealerRegister.getBillingAddress());
                edArea.setText("" + updateDealerRegister.getArea());
                spCity.setText("" + updateDealerRegister.getCity());

//                if (updateDealerRegister.getPincode() != null)
//                    getad(creation.getPincode());

                spState.setText("" + updateDealerRegister.getState());
                setCityAsSelected(updateDealerRegister.getInterstCity());
                setDistrictAsSelected(updateDealerRegister.getInterstDistrict());
                setStateAsSelected(updateDealerRegister.getInterstState());
                setNationalAsSelected(updateDealerRegister.getInterstCountry());
//                chCity.setChecked(updateDealerRegister.getInterstCity());
//                chDistrict.setChecked(updateDealerRegister.getInterstDistrict());
//                chState.setChecked(updateDealerRegister.getInterstState());
//                chCountry.setChecked(updateDealerRegister.getInterstCountry());

                city = updateDealerRegister.getCity();
                state = updateDealerRegister.getState();

                StateId = state.getStateID();
                CityId = city.getID();
                lng = Double.parseDouble(updateDealerRegister.getLangitude());
                edPincode.setText("" + updateDealerRegister.getPincode());

                TaxType = updateDealerRegister.getRegistrationType();
                GSTNumber = updateDealerRegister.getTinNumber();
                PanNumber = updateDealerRegister.getPanNumber();
                BankName = updateDealerRegister.getBankname();
                BranchBank = updateDealerRegister.getBankBranchName();
                BankCityName = updateDealerRegister.getBankCity();
                AccountNumber = updateDealerRegister.getAccountnumber();
                BankIFSCCode = updateDealerRegister.getiFSCCode();
                UserImageFile = updateDealerRegister.getUserImageFile();

                if (updateDealerRegister.getUserImageFile() != null) {
                    UserImageFile = updateDealerRegister.getUserImageFile();

                }
                BusinessDemandID = updateDealerRegister.getBusinessDemandID();
            }
            if (city == null && state == null) shouldUpdateAddress = true;
            if (edPincode.getText().length() > 0)
                getStateCityFromPincode(edPincode.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCityAsSelected(boolean shouldSelect) {
        if (shouldSelect) {
            isCitySelected = true;
            cityBit = true;
            layoutCity.setBackgroundResource(R.drawable.shape_rectangle_red);
            imgCity.setImageResource(R.drawable.ic_city_selected);
            chCity.setTypeface(null, Typeface.BOLD);
            chCity.setTextColor(getResources().getColor(R.color.white));
        } else {
            isCitySelected = false;
            cityBit = false;
            layoutCity.setBackgroundResource(0);
            imgCity.setImageResource(R.drawable.ic_city_unselected);
            chCity.setTypeface(null, Typeface.NORMAL);
            chCity.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void setDistrictAsSelected(boolean shouldSelect) {
        if (shouldSelect) {
            districtBit = true;
            isDistrictSelected = true;
            layoutDistrict.setBackgroundResource(R.drawable.shape_rectangle_red);
            imgDistrict.setImageResource(R.drawable.ic_district_selected);
            chDistrict.setTypeface(null, Typeface.BOLD);
            chDistrict.setTextColor(getResources().getColor(R.color.white));
        } else {
            districtBit = false;
            isDistrictSelected = false;
            layoutDistrict.setBackgroundResource(0);
            imgDistrict.setImageResource(R.drawable.ic_district_unselected);
            chDistrict.setTypeface(null, Typeface.NORMAL);
            chDistrict.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void setStateAsSelected(boolean shouldSelect) {
        if (shouldSelect) {
            stateBit = true;
            isStateSelected = true;
            layoutState.setBackgroundResource(R.drawable.shape_rectangle_red);
            imgState.setImageResource(R.drawable.ic_state_selected);
            chState.setTypeface(null, Typeface.BOLD);
            chState.setTextColor(getResources().getColor(R.color.white));
        } else {
            stateBit = false;
            isStateSelected = false;
            layoutState.setBackgroundResource(0);
            imgState.setImageResource(R.drawable.ic_state_unselected);
            chState.setTypeface(null, Typeface.NORMAL);
            chState.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void setNationalAsSelected(boolean shouldSelect) {
        if (shouldSelect) {
            CountryBit = true;
            isNationalSelected = true;
            layoutNational.setBackgroundResource(R.drawable.shape_rectangle_red);
            imgNational.setImageResource(R.drawable.ic_national_selected);
            chCountry.setTypeface(null, Typeface.BOLD);
            chCountry.setTextColor(getResources().getColor(R.color.white));
        } else {
            CountryBit = false;
            isNationalSelected = false;
            layoutNational.setBackgroundResource(0);
            imgNational.setImageResource(R.drawable.ic_national_unselected);
            chCountry.setTypeface(null, Typeface.NORMAL);
            chCountry.setTextColor(getResources().getColor(R.color.black));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:


                imgFile = dealerRegister.getUserImageFile();
                MultipartBody.Part body = null, body1 = null;
                RequestBody requestBody1;
                try {
                    requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
                    body = MultipartBody.Part.createFormData("file", imgFile.getName(), requestBody1);
                    updateImage(CustID, body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (saveData())
                    callToFragment.communicateFragment(pos);
                break;

            case R.id.layout_state:
                setStateAsSelected(!isStateSelected);
                break;

            case R.id.layout_city:
                setCityAsSelected(!isCitySelected);
                break;

            case R.id.layout_district:
                setDistrictAsSelected(!isDistrictSelected);
                break;

            case R.id.layout_national:
                setNationalAsSelected(!isNationalSelected);
                break;
            case R.id.btnMap:
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                mapFragment.getMapAsync(this);
//                tvLatLong.setVisibility(View.VISIBLE);
                btnMap.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.btnExit:
                if (!isStateFound) {
                    Toast.makeText(getContext(), "Can't proceed without State and City of the given pincode.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!ValidateBillingAddress() | !ValidateArea() | !ValidateState() | !ValidateCity() | !ValidatePincode()) {
                    return;
                } else {
                    if (isCitySelected || isStateSelected || isDistrictSelected || isNationalSelected) {
                        creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);
                        if (creation.isUserImage() == null || creation.isUserImage().equals("")) {
                            Log.e("isUserImage", "UserImage is null");
                            if ((state != null && !state.getStateName().isEmpty()) && (city != null && !city.getVillageLocalityname().isEmpty())) {
                                dealerRegister = new DealerRegister(CustID, creation.getFirmName(),
                                        creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                                        creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(),
                                        creation.getMobileNumber(), creation.getAdditionalPersonName(), creation.getMobileNumber2(),
                                        creation.getTelephoneNumber(), creation.getShopImage(), edBillingAddress.getText().toString(),
                                        edArea.getText().toString(), city, "", state, edPincode.getText().toString(),
                                        String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit, CountryBit, TaxType, GSTNumber,
                                        PanNumber, BankName, BranchBank, AccountNumber, BankIFSCCode, BankCityName, 1599,
                                        CustID, 1, creation.getCreatedDate(), creation.isBGSMember(), creation.getIsProfessional(), creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);
                            } else if (creation.getState() != null || creation.getCity() != null) {
                                dealerRegister = new DealerRegister(CustID, creation.getFirmName(),
                                        creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                                        creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(),
                                        creation.getMobileNumber(), creation.getAdditionalPersonName(), creation.getMobileNumber2(),
                                        creation.getTelephoneNumber(), creation.getShopImage(), edBillingAddress.getText().toString(),
                                        edArea.getText().toString(), creation.getCity(), "", creation.getState(),
                                        edPincode.getText().toString(), String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit,
                                        CountryBit, TaxType, GSTNumber, PanNumber, BankName, BranchBank, AccountNumber, BankIFSCCode, BankCityName,
                                        1599, CustID, 1, creation.getCreatedDate(), creation.isBGSMember(), creation.getIsProfessional(), creation.getUserImageFile(),
                                        creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);
                            } else {
                                dealerRegister = new DealerRegister(CustID,
                                        creation.getFirmName(), creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                                        creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(), creation.getMobileNumber(),
                                        creation.getAdditionalPersonName(), creation.getMobileNumber2(), creation.getTelephoneNumber(), creation.getShopImage(),
                                        edBillingAddress.getText().toString(), edArea.getText().toString(), updateDealerRegister.getCity(),
                                        "", updateDealerRegister.getState(), edPincode.getText().toString(),
                                        String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit, CountryBit, TaxType, GSTNumber,
                                        PanNumber, BankName, BranchBank, AccountNumber, BankIFSCCode, BankCityName, 1599,
                                        CustID, 1, creation.getCreatedDate(), creation.isBGSMember(), creation.getIsProfessional(), creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);

                            }
                            imgFile = dealerRegister.getUserImageFile();
                            sendDealerRegisterDetailsToServerExit(CustID, dealerRegister);
                        } else {
                            if ((state != null && !state.getStateName().isEmpty()) && (city != null && !city.getVillageLocalityname().isEmpty())) {
                                dealerRegister = new DealerRegister(CustID, creation.getFirmName(),
                                        creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                                        creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(),
                                        creation.getMobileNumber(), creation.getAdditionalPersonName(), creation.getMobileNumber2(),
                                        creation.getTelephoneNumber(), creation.getShopImage(), edBillingAddress.getText().toString(),
                                        edArea.getText().toString(), city, "", state, edPincode.getText().toString(),
                                        String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit, CountryBit, TaxType, GSTNumber,
                                        PanNumber, BankName, BranchBank, AccountNumber, BankIFSCCode, BankCityName, 1599,
                                        CustID, 1, creation.getCreatedDate(), creation.isBGSMember(), creation.getIsProfessional(), creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);
                            } else if (creation.getState() != null || creation.getCity() != null) {

                                dealerRegister = new DealerRegister(CustID, creation.getFirmName(),
                                        creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                                        creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(),
                                        creation.getMobileNumber(), creation.getAdditionalPersonName(), creation.getMobileNumber2(),
                                        creation.getTelephoneNumber(), creation.getShopImage(), edBillingAddress.getText().toString(),
                                        edArea.getText().toString(), creation.getCity(), "", creation.getState(),
                                        edPincode.getText().toString(), String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit,
                                        CountryBit, TaxType, GSTNumber, PanNumber, BankName, BranchBank, AccountNumber, BankIFSCCode, BankCityName,
                                        1599, CustID, 1, creation.getCreatedDate(),
                                        creation.isBGSMember(), creation.getIsProfessional(), creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);
                            } else {

                                dealerRegister = new DealerRegister(CustID,
                                        creation.getFirmName(), creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                                        creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(), creation.getMobileNumber(),
                                        creation.getAdditionalPersonName(), creation.getMobileNumber2(), creation.getTelephoneNumber(), creation.getShopImage(),
                                        edBillingAddress.getText().toString(), edArea.getText().toString(), updateDealerRegister.getCity(),
                                        "", updateDealerRegister.getState(), edPincode.getText().toString(),
                                        String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit, CountryBit, TaxType, GSTNumber,
                                        PanNumber, BankName, BranchBank, AccountNumber, BankIFSCCode, BankCityName, 1599,
                                        CustID, 1, creation.getCreatedDate(), creation.isBGSMember(), creation.getIsProfessional(), creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);

                            }
                            imgFile = dealerRegister.getUserImageFile();
                            UserImageFile = dealerRegister.getUserImageFile();
                            Log.e("imgfile", "Firstone Called");
                            sendDealerRegisterDetailsToServerExit(CustID, dealerRegister);
                        }
                    } else {
                        CustomToast.showToast(getActivity(), "Please select selling area");
                    }
                }
                break;
            case R.id.tvLogoutOk:
                creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);

                if (creation.isUserImage() == null || creation.isUserImage().equals("")) {
                    if ((state != null && !state.getStateName().isEmpty()) && (city != null && !city.getVillageLocalityname().isEmpty())) {
                        dealerRegister = new DealerRegister(CustID, creation.getFirmName(),
                                creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                                creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(),
                                creation.getMobileNumber(), creation.getAdditionalPersonName(), creation.getMobileNumber2(),
                                creation.getTelephoneNumber(), creation.getShopImage(), edBillingAddress.getText().toString(),
                                edArea.getText().toString(), city, "", state, edPincode.getText().toString(),
                                String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit, CountryBit, TaxType, GSTNumber,
                                PanNumber, BankName, BranchBank, AccountNumber, BankIFSCCode, BankCityName, 1599,
                                CustID, 1, creation.getCreatedDate(), creation.isBGSMember(), creation.getIsProfessional(), creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);
                    } else if (creation.getState() != null || creation.getCity() != null) {

                        dealerRegister = new DealerRegister(CustID, creation.getFirmName(),
                                creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                                creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(),
                                creation.getMobileNumber(), creation.getAdditionalPersonName(), creation.getMobileNumber2(),
                                creation.getTelephoneNumber(), creation.getShopImage(), edBillingAddress.getText().toString(),
                                edArea.getText().toString(), creation.getCity(), "", creation.getState(),
                                edPincode.getText().toString(), String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit,
                                CountryBit, TaxType, GSTNumber, PanNumber, BankName, BranchBank, AccountNumber, BankIFSCCode, BankCityName,
                                1599, CustID, 1, creation.getCreatedDate(),
                                creation.isBGSMember(), creation.getIsProfessional(), creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);
                    } else {

                        dealerRegister = new DealerRegister(CustID,
                                creation.getFirmName(), creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                                creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(), creation.getMobileNumber(),
                                creation.getAdditionalPersonName(), creation.getMobileNumber2(), creation.getTelephoneNumber(), creation.getShopImage(),
                                edBillingAddress.getText().toString(), edArea.getText().toString(), updateDealerRegister.getCity(),
                                "", updateDealerRegister.getState(), edPincode.getText().toString(),
                                String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit, CountryBit, TaxType, GSTNumber,
                                PanNumber, BankName, BranchBank, AccountNumber, BankIFSCCode, BankCityName, 1599,
                                CustID, 1, creation.getCreatedDate(), creation.isBGSMember(), creation.getIsProfessional(), creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);
                    }
                    imgFile = creation.getUserImageFile();
                    sendDealerRegisterDetailsToServerExitPhoto(CustID, dealerRegister);
                } else {
                    if ((state != null && !state.getStateName().isEmpty()) && (city != null && !city.getVillageLocalityname().isEmpty())) {
                        dealerRegister = new DealerRegister(CustID, creation.getFirmName(),
                                creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                                creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(),
                                creation.getMobileNumber(), creation.getAdditionalPersonName(), creation.getMobileNumber2(),
                                creation.getTelephoneNumber(), creation.getShopImage(), edBillingAddress.getText().toString(),
                                edArea.getText().toString(), city, "", state, edPincode.getText().toString(),
                                String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit, CountryBit, TaxType, GSTNumber,
                                PanNumber, BankName, BranchBank, AccountNumber, BankIFSCCode, BankCityName, 1599,
                                CustID, 1, creation.getCreatedDate(), creation.isBGSMember(), creation.getIsProfessional(), creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);
                    } else if (creation.getState() != null || creation.getCity() != null) {

                        dealerRegister = new DealerRegister(CustID, creation.getFirmName(),
                                creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                                creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(),
                                creation.getMobileNumber(), creation.getAdditionalPersonName(), creation.getMobileNumber2(),
                                creation.getTelephoneNumber(), creation.getShopImage(), edBillingAddress.getText().toString(),
                                edArea.getText().toString(), creation.getCity(), "", creation.getState(),
                                edPincode.getText().toString(), String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit,
                                CountryBit, TaxType, GSTNumber, PanNumber, BankName, BranchBank, AccountNumber, BankIFSCCode, BankCityName,
                                1599, CustID, 1, creation.getCreatedDate(),
                                creation.isBGSMember(), creation.getIsProfessional(), creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);
                    } else {

                        dealerRegister = new DealerRegister(CustID,
                                creation.getFirmName(), creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                                creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(), creation.getMobileNumber(),
                                creation.getAdditionalPersonName(), creation.getMobileNumber2(), creation.getTelephoneNumber(), creation.getShopImage(),
                                edBillingAddress.getText().toString(), edArea.getText().toString(), updateDealerRegister.getCity(),
                                "", updateDealerRegister.getState(), edPincode.getText().toString(),
                                String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit, CountryBit, TaxType, GSTNumber,
                                PanNumber, BankName, BranchBank, AccountNumber, BankIFSCCode, BankCityName, 1599,
                                CustID, 1, creation.getCreatedDate(), creation.isBGSMember(), creation.getIsProfessional(), creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);

                    }
                    imgFile = dealerRegister.getUserImageFile();
                    Log.e("model.", String.valueOf(dealerRegister.getBusinessDemandID()));
                    sendDealerRegisterDetailsToServerExit(CustID, dealerRegister);
                }
                mDialogExit.dismiss();
                break;

            case R.id.tvLogoutCancel:
                mDialogExit.dismiss();
                break;

            case R.id.spinnerState:
                mDialogState = new Dialog(getActivity());
                mDialogState.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialogState.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mDialogState.setContentView(R.layout.dialog_category_list);
                mDialogState.setCancelable(true);
                dialogState = mDialogState.findViewById(R.id.edState);
                AppCompatImageView Clear_txt_btn = mDialogState.findViewById(R.id.clear_txt_Prise);
                listViewState = mDialogState.findViewById(R.id.recyclerCategory);
                ImageView imageView = mDialogState.findViewById(R.id.cancel_category);
                mDialogState.show();
                getStateFromServer();
                imageView.setOnClickListener(v1 -> mDialogState.dismiss());

                listViewState.setOnItemClickListener((parent, view, position, id) -> {
                    state = (State) listViewState.getItemAtPosition(position);
                    StateId = state.getStateID();
                    isStateChanged = true;
                    spState.setText("" + state.getStateName());
                    if (cityList == null) cityList = new ArrayList<>();
                    cityList.clear();
                    spCity.setText("");
                    mDialogState.dismiss();
                });
                Clear_txt_btn.setOnClickListener(view -> {
                    if (!dialogState.getText().toString().isEmpty()) {
                        dialogState.getText().clear();
                        Clear_txt_btn.setVisibility(View.GONE);
                    }
                });
                dialogState.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            stateAdapter.getFilter().filter(s);
                            if (s.length() == 0) {

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
                            stateAdapter.getFilter().filter(s);
                            if (s.length() == 0) {

                                Clear_txt_btn.setVisibility(View.GONE);
                            } else {
                                Clear_txt_btn.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.spinnerCity:
                if (spState.getText().toString().isEmpty()) {
                    CustomToast.showToast(getActivity(), "Please select state");
                } else {
                    mDialogCity = new Dialog(getActivity());
                    mDialogCity.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mDialogCity.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    mDialogCity.setContentView(R.layout.dialog_city_item_layout);
                    mDialogCity.setCancelable(true);
                    dialogCity = mDialogCity.findViewById(R.id.edCity);
                    AppCompatImageView Clear_txt_btn1 = mDialogCity.findViewById(R.id.clear_txt_Prise);
                    recyclerViewCity = mDialogCity.findViewById(R.id.recyclerCity);
                    recyclerViewCity.setHasFixedSize(true);
                    recyclerViewCity.setLayoutManager(new LinearLayoutManager(getActivity()));
                    if (cityAdapter == null) {
                        cityAdapter = new CityAdapter(getActivity(), cityList, UpdateBillingAddress.this);
                    }
                    recyclerViewCity.setAdapter(cityAdapter);
                    cityAdapter.notifyDataSetChanged();

                    ImageView imageView3 = mDialogCity.findViewById(R.id.cancel_childcategory);
                    mDialogCity.show();
                    imageView3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialogCity.dismiss();
                        }
                    });
                    Clear_txt_btn1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!dialogCity.getText().toString().isEmpty()) {
                                dialogCity.getText().clear();
                                Clear_txt_btn1.setVisibility(View.GONE);

                            }
                        }
                    });
                    dialogCity.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //uncommented by Shweta
                            try {

                                String cityName = s.toString();
                                if (s.length() == 0) {
                                    Clear_txt_btn1.setVisibility(View.GONE);
                                } else if (cityName.length() >= 3) {
                                    cityAdapter.getFilter().filter(s);
                                    if(!isStateFound) getCityFromServer(StateId, cityName);
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

                                String cityName = s.toString();
                                if (s.length() == 0) {
                                    Clear_txt_btn1.setVisibility(View.GONE);
                                } else if (cityName.length() >= 3) {
                                    /*if (isStateFound && cityList.size()>0) cityAdapter.getFilter().filter(s);
                                    else*/ if (isStateChanged || isStateFound) getCityFromServer(StateId, cityName);
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

                }

                break;

            case R.id.cancel_category:
                mDialogState.dismiss();
                break;
            case R.id.cancel_childcategory:
                mDialogCity.dismiss();
                break;

            case R.id.cancel_city:
                mDialogOther.dismiss();
                break;
        }
    }

    @Override
    public void onCitySelected(City cityy) {
        CityId = cityy.getID();
        city = cityy;
        edPincode.setText("" + cityy.getPinCode());
        isTypingPincode = false;
        isStateChanged = false;
        dealerRegister.setCity(city);
        spCity.setText("" + cityy);
        mDialogCity.dismiss();
    }

    private void sendDealerRegisterDetailsToServerExit(Integer custID, DealerRegister dealerRegister) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(getContext(), "Please wait");
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        MultipartBody.Part body = null, body1 = null;
        RequestBody requestBody1, requestBody2;
        try {
            requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
            body = MultipartBody.Part.createFormData("file", imgFile.getName(), requestBody1);
            updateImage(custID, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        String json = gson.toJson(dealerRegister);
        Log.e("updated data...", json);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<DealerRegister> call = customer_interface.updateDealerRegister("bearer " + Token, dealerRegister);
        call.enqueue(new Callback<DealerRegister>() {
            @Override
            public void onResponse(Call<DealerRegister> call, Response<DealerRegister> response) {
                int statusCode = response.code();
                Log.e("updatedCustomerdata ...", String.valueOf(response.code()));
                switch (statusCode) {
                    case 200:
                        Log.e("updatedCustomerdata ...", String.valueOf(response.body()));
                        progressDialog.dismiss();
                        isUpdate = true;
                        CustomToast.showToast(getActivity(), "Successfully updated");
                        Intent intent = new Intent(getActivity(), DashboardActivity.class).putExtra("isNewUser", false);
                        startActivity(intent);
                        break;

                    case 400:
                    case 404:
                        CustomToast.showToast(getActivity(), "Invalid pincode");
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(getActivity(), "Server Error");
                        break;

/*                    case 400:
                        progressDialog.dismiss();
                        CustomToast.showToast(getActivity(), "Request not granted");
                        break;*/

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(getActivity());
                        break;
                }
            }

            @Override
            public void onFailure(Call<DealerRegister> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(getActivity(), "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void updateImage(Integer custID, MultipartBody.Part body) {
        RequestBody requestBody_custid = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(custID));
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<ResponseBody> call = customer_interface.updatePhoto("bearer " + Token, body, requestBody_custid);
        Log.e("Insidetry...", custID.toString());
        Gson gson = new Gson();
        String json = gson.toJson(body);
        Log.e("imagee..", json);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int statusCode = response.code();
                Log.e("updateC_statuscode...", String.valueOf(statusCode));
                switch (statusCode) {
                    case 200:
                        //Log.e("update_status...",response.body());
                        //CustomToast.showToast(getActivity(), "Photo saved uploaded");
                        //startActivity(new Intent(getActivity(), LoginActivity.class));
                        break;
                    case 404:
                        break;
                    case 500:
                        CustomToast.showToast(getActivity(), "Server Error");
                        break;

                    case 400:
                        CustomToast.showToast(getActivity(), "Request Not Granted");
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(getActivity());
                        break;

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("UC_failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(getActivity(), "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void sendDealerRegisterDetailsToServerExitPhoto(Integer custID, DealerRegister dealerRegister) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(getContext(), "Please wait");
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        MultipartBody.Part body = null, body1 = null;
        try {
            if (dealerRegister.getUserImageFile() != null) {
                UserImageFile = dealerRegister.getUserImageFile();
            } else {
            }

        } catch (Exception e) {
        }
        RequestBody requestBody1, requestBody2;
        try {
            //requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"),UserImageFile );
            //body = MultipartBody.Part.createFormData("picture", UserImageFile.getName(), requestBody1);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("body...", e.getMessage());
        }
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<DealerRegister> call = customer_interface.updateDealerRegister("bearer " + Token, dealerRegister);

        call.enqueue(new Callback<DealerRegister>() {
            @Override
            public void onResponse(Call<DealerRegister> call, Response<DealerRegister> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        isUpdate = true;
                        CustomToast.showToast(getActivity(), "Successfully updated");
                        Intent intent = new Intent(getActivity(), DashboardActivity.class).putExtra("isNewUser", false);
                        startActivity(intent);
                        break;
                    case 400:
                    case 404:
                        CustomToast.showToast(getActivity(), "Invalid pincode");
                        progressDialog.dismiss();
                        break;
                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(getActivity(), "Server Error");
                        break;
                    /*case 400:
                        progressDialog.dismiss();
                        CustomToast.showToast(getActivity(), "Request not granted");
                        break;*/

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(getActivity());
                        break;
                }

            }

            @Override
            public void onFailure(Call<DealerRegister> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(getActivity(), "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void getStateFromServer() {
        ProgressDialog progressDialog = ShowProgressDialog.createProgressDialogWithtxt(getActivity(), "Getting states please wait..");
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
                        stateAdapter = new StateAdapter(getActivity(), stateList);
                        listViewState.setAdapter(stateAdapter);
                        stateAdapter.notifyDataSetChanged();
                        break;

                    case 404:
                        CustomToast.showToast(getActivity(), "No State found");
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(getActivity(), "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(getActivity());
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(getActivity(), "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void getCityFromServer(int stateId, String cityName) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(getContext(), "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<City>> call = customer_interface.getAllCity(/*"bearer " + Token, */stateId, cityName);
        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        cityList = new ArrayList<>(response.body());
                        if (cityList.isEmpty()) {
                            CustomToast.showToast(getActivity(), "No city found");
                        } else {
                            cityAdapter = new CityAdapter(getActivity(), cityList, UpdateBillingAddress.this);
                            recyclerViewCity.setAdapter(cityAdapter);
                            cityAdapter.notifyDataSetChanged();
                        }
                        break;
                    case 404:
                        CustomToast.showToast(getActivity(), "No City found");
                        progressDialog.dismiss();
                        break;
                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(getActivity(), "Server Error");
                        break;
                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(getActivity());
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(getActivity(), "Bad response from server.. Try again later ");
                }
            }
        });

    }

/*

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.chCity:
                if (isChecked) {
                    chCity.setChecked(true);
                } else {
                    cityBit = false;
                    chCity.setChecked(false);
                }
                break;
            case R.id.chDistrict:
                if (isChecked) {
                    districtBit = true;
                    chDistrict.setChecked(true);
                } else {
                    districtBit = false;
                    chDistrict.setChecked(false);
                }
                break;
            case R.id.chState:
                if (isChecked) {
                    stateBit = true;
                    layoutState.setBackgroundResource(R.drawable.shape_rectangle_red);
                    imgState.setImageResource(R.drawable.ic_state_selected);
                    chState.setChecked(true);
                } else {
                    stateBit = false;
                    imgState.setImageResource(R.drawable.ic_state_unselected);
                    layoutState.setBackgroundResource(R.drawable.shape_rectangle_red_circular_corners_gray);
                    chState.setChecked(false);
                }
                break;
            case R.id.chNational:
                if (isChecked) {
                    CountryBit = true;
                    chCountry.setChecked(true);
                } else {
                    CountryBit = false;
                    chCountry.setChecked(false);
                }
                break;
            default:
                break;
        }


    }
*/

    public void onBackClicked() {
        isUpdatedOnce = true;
        callToFragment.communicateFragment(0);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if(!edPincode.getText().toString().equals("")) getad(edPincode.getText().toString());
//        else{
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
//        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private void getad(String pincode) {
        final Geocoder geocoder = new Geocoder(getContext());

        try {
            List<Address> addresses = geocoder.getFromLocationName(pincode, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Use the address as needed
                String message = String.format("Latitude: %f, Longitude: %f",
                        address.getLatitude(), address.getLongitude());
                Double lat = address.getLatitude();
                Double lng = address.getLongitude();
                shouldUpdateAddress = true;
                getCompleteAddressString(lat, lng);
//                if(mMap != null) showLocationOnMap(lat, lng);
                Log.e("TAG", "getad: " + message);
            } else {
                // Display appropriate message when Geocoder services are not available
                Log.e("TAG", "getad: Unable to geocode zipcode");
            }
        } catch (IOException e) {
            Log.e("TAG", "getad: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showLocationOnMap(Double latitude, Double longitude) {
        if (creation.getLattitude() == null && creation.getLangitude() == null) {
            mLastLocation.setLatitude(latitude);
            mLastLocation.setLongitude(longitude);
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            Lat = latitude;
            System.out.println("HIIIII" + Lat);
            lng = longitude;
            latLng = new LatLng(latitude, longitude);
            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            try {
                tvLatLong.setText("Your Location: " + latLng + "\n Address: " + getCompleteAddressString(latitude, longitude));
            } catch (Exception e) {
                e.printStackTrace();
            }
            markerOptions.title("Current Location : " + latLng);
            markerOptions.anchor(0.5f, 0.5f);
            markerOptions.snippet(getCompleteAddressString(latitude, longitude));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            dataModel.put("title", "Current Position");
            dataModel.put("snippet", "This is my spot!");
            dataModel.put("latitude", latLng);
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            markers.put(mCurrLocationMarker, dataModel);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            mMap.getMapType();

            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }

        } else if (creation.getLattitude().isEmpty() && creation.getLangitude().isEmpty()) {
            mLastLocation.setLatitude(latitude);
            mLastLocation.setLongitude(longitude);
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            Lat = latitude;
            System.out.println("HIIIII" + Lat);
            lng = longitude;

            latLng = new LatLng(latitude, longitude);
            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);

            try {
                tvLatLong.setText("Your Location: " + latLng + "\n Address: " + getCompleteAddressString(latitude, longitude));
            } catch (Exception e) {
                e.printStackTrace();
            }
            markerOptions.title("Current Location : " + latLng);
            markerOptions.anchor(0.5f, 0.5f);
            markerOptions.snippet(getCompleteAddressString(latitude, longitude));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            dataModel.put("title", "Current Position");
            dataModel.put("snippet", "This is my spot!");
            dataModel.put("latitude", latLng);
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            markers.put(mCurrLocationMarker, dataModel);


            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            mMap.getMapType();

            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }

        } else if (creation.getLangitude().equals("0.0") && creation.getLangitude().equals("0.0")) {
            mLastLocation.setLatitude(latitude);
            mLastLocation.setLongitude(longitude);
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            Lat = latitude;
            lng = longitude;

            latLng = new LatLng(latitude, longitude);
            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);

            try {
                tvLatLong.setText("Your Location: " + latLng + "\n Address: " + getCompleteAddressString(latitude, longitude));
            } catch (Exception e) {
                e.printStackTrace();
            }
            markerOptions.title("Current Location : " + latLng);
            markerOptions.anchor(0.5f, 0.5f);
            markerOptions.snippet(getCompleteAddressString(latitude, longitude));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            dataModel.put("title", "Current Position");
            dataModel.put("snippet", "This is my spot!");
            dataModel.put("latitude", latLng);
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            markers.put(mCurrLocationMarker, dataModel);
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            mMap.getMapType();

            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        } else {
            mLastLocation.setLatitude(latitude);
            mLastLocation.setLongitude(longitude);
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            Lat = Double.parseDouble(creation.getLattitude());
            lng = Double.parseDouble(creation.getLangitude());

            latLng = new LatLng(Double.parseDouble(creation.getLattitude()), Double.parseDouble(creation.getLangitude()));
            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            try {
                tvLatLong.setText("Your Location: " + latLng + "\n Address: " + getCompleteAddressString(Double.parseDouble(creation.getLattitude()), Double.parseDouble(creation.getLangitude())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            markerOptions.title("Current Location : " + latLng);
            markerOptions.anchor(0.5f, 0.5f);
            markerOptions.snippet(getCompleteAddressString(latitude, longitude));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            dataModel.put("title", "Current Position");
            dataModel.put("snippet", "This is my spot!");
            dataModel.put("latitude", latLng);
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            markers.put(mCurrLocationMarker, dataModel);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            mMap.getMapType();
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (creation.getLattitude() == null && creation.getLangitude() == null) {
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            Lat = location.getLatitude();
            System.out.println("HIIIII" + Lat);
            lng = location.getLongitude();
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            /*try {
                tvLatLong.setText("Your Location: " + latLng + "\n Address: " + getCompleteAddressString(location.getLatitude(), location.getLongitude()));
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            markerOptions.title("Current Location : " + latLng);
            markerOptions.anchor(0.5f, 0.5f);
            shouldUpdateAddress = false;
            markerOptions.snippet(getCompleteAddressString(location.getLatitude(), location.getLongitude()));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            dataModel.put("title", "Current Position");
            dataModel.put("snippet", "This is my spot!");
            dataModel.put("latitude", latLng);
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            markers.put(mCurrLocationMarker, dataModel);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            mMap.getMapType();

            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }

        } else if (creation.getLattitude().isEmpty() && creation.getLangitude().isEmpty()) {
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            Lat = location.getLatitude();
            System.out.println("HIIIII" + Lat);
            lng = location.getLongitude();

            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);

            /*try {
                tvLatLong.setText("Your Location: " + latLng + "\n Address: " + getCompleteAddressString(location.getLatitude(), location.getLongitude()));
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            markerOptions.title("Current Location : " + latLng);
            markerOptions.anchor(0.5f, 0.5f);
            shouldUpdateAddress = false;
            markerOptions.snippet(getCompleteAddressString(location.getLatitude(), location.getLongitude()));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            dataModel.put("title", "Current Position");
            dataModel.put("snippet", "This is my spot!");
            dataModel.put("latitude", latLng);
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            markers.put(mCurrLocationMarker, dataModel);


            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            mMap.getMapType();

            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }

        } else if (creation.getLangitude().equals("0.0") && creation.getLangitude().equals("0.0")) {
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            Lat = location.getLatitude();
            lng = location.getLongitude();

            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);

            /*try {
                tvLatLong.setText("Your Location: " + latLng + "\n Address: " + getCompleteAddressString(location.getLatitude(), location.getLongitude()));
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            markerOptions.title("Current Location : " + latLng);
            markerOptions.anchor(0.5f, 0.5f);
            shouldUpdateAddress = false;
            markerOptions.snippet(getCompleteAddressString(location.getLatitude(), location.getLongitude()));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            dataModel.put("title", "Current Position");
            dataModel.put("snippet", "This is my spot!");
            dataModel.put("latitude", latLng);
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            markers.put(mCurrLocationMarker, dataModel);
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            mMap.getMapType();

            //stop location updates
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        } else {
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }
            Lat = location.getLatitude();
            lng = location.getLongitude();

            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            /*try {
                tvLatLong.setText("Your Location: " + latLng + "\n Address: " + getCompleteAddressString(Double.parseDouble(creation.getLattitude()), Double.parseDouble(creation.getLangitude())));
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            markerOptions.title("Current Location : " + latLng);
            markerOptions.anchor(0.5f, 0.5f);
            shouldUpdateAddress = false;
            markerOptions.snippet(getCompleteAddressString(location.getLatitude(), location.getLongitude()));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            dataModel.put("title", "Current Position");
            dataModel.put("snippet", "This is my spot!");
            dataModel.put("latitude", latLng);
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            markers.put(mCurrLocationMarker, dataModel);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
            mMap.getMapType();
            if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }

        }
    }

    private String getCompleteAddress(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {

                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {

                Address returnedAddress = addresses.get(0);
                if (shouldUpdateAddress) {
//                    edBillingAddress.setText(addresses.get(0).getAddressLine(0));
//                    edArea.setText(addresses.get(0).getLocality());
//                    spCity.setText(addresses.get(0).getLocality());

//                    if (addresses.get(0).getAdminArea() == null || addresses.get(0).getAdminArea().equals(""))
//                        isStateFound = false;
//                    else isStateFound = true;

//                    spState.setText(addresses.get(0).getAdminArea());
//                    getCityByName();
                }
//                shouldUpdateAddress = true;
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                try {
                    Map dataModel = markers.get(marker);
                    String title = dataModel.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker position) {

                Map dataModel = markers.get(position);
                String title = dataModel.toString();
                openDialogMethod(dataModel.get("title").toString(), dataModel.get("snippet").toString(), tvLatLong.getText().toString());

            }
        });


    }

    private void openDialogMethod(String title, String snippet, String toString) {

        mDialogOpen = new Dialog(getActivity());
        mDialogOpen.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogOpen.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialogOpen.setContentView(R.layout.dialog_address);
        mDialogOpen.setCancelable(true);
        mDialogOpen.show();
        tvAtmName = mDialogOpen.findViewById(R.id.tvAtmName);
        tvAtmArea = mDialogOpen.findViewById(R.id.tvAtmArea);
        tvAreaLatitude = mDialogOpen.findViewById(R.id.tvAtmLat);


        tvAtmName.setText("Name : " + areaName);
        tvAtmArea.setText("Address : " + snippet);
        tvAreaLatitude.setText(toString);
    }


    public interface CallToFragment {
        void communicateFragment(int pos);

        void callingTaxFragment(int pos);
    }


    private boolean ValidateBillingAddress() {
        String str = edBillingAddress.getText().toString();
        if (str.isEmpty()) {
            edBillingAddress.setError("Please Enter Building/Road Address");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidateArea() {
        String str = edArea.getText().toString();
        if (str.isEmpty()) {
            edArea.setError("Please enter Area/Nagar");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidateState() {
        String str = spState.getText().toString();
        if (str.isEmpty()) {
            CustomToast.showToast(getActivity(), "Please Select state");
            return false;
        } else {
            return true;
        }
    }

    private boolean ValidateCity() {
        String str = spCity.getText().toString();

        if (str.isEmpty()) {
            CustomToast.showToast(getActivity(), "Please Select city");
            return false;
        } else {
            return true;
        }
    }

    public boolean saveData() {
        boolean value = false;
        if (!ValidateBillingAddress() | !ValidateArea() | !ValidateState() | !ValidateCity() | !ValidatePincode()) {
            value = false;
            return value;
        } else {
            if (isCitySelected || isDistrictSelected || isStateSelected || isNationalSelected) {
                creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", DealerRegister.class);
                if (state != null && city != null) {
                    dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)), creation.getFirmName(),
                            creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                            creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(),
                            creation.getMobileNumber(), creation.getAdditionalPersonName(), creation.getMobileNumber2(),
                            creation.getTelephoneNumber(), creation.getShopImage(), edBillingAddress.getText().toString(),
                            edArea.getText().toString(), city, "", state, edPincode.getText().toString(),
                            String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit, CountryBit, TaxType, GSTNumber,
                            PanNumber, BankName, BranchBank, AccountNumber, BankIFSCCode, BankCityName, 1599,
                            Integer.parseInt(prefManager.getCustId().get(CUST_ID)), 1, creation.getCreatedDate(),
                            creation.isBGSMember(), creation.getIsProfessional(), creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);
                } else if (creation.getState() != null || creation.getCity() != null) {
                    dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)), creation.getFirmName(),
                            creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                            creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(), creation.getMobileNumber(),
                            creation.getAdditionalPersonName(), creation.getMobileNumber2(), creation.getTelephoneNumber(),
                            creation.getShopImage(), edBillingAddress.getText().toString(), edArea.getText().toString(),
                            creation.getCity(), "", creation.getState(), edPincode.getText().toString(),
                            String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit, CountryBit, TaxType, GSTNumber,
                            PanNumber, BankName, BranchBank, AccountNumber, BankIFSCCode, BankCityName, 1599,
                            Integer.parseInt(prefManager.getCustId().get(CUST_ID)), 1, creation.getCreatedDate(),
                            creation.isBGSMember(), creation.getIsProfessional(), creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);
                } else {
                    dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)), creation.getFirmName(),
                            creation.getPassword(), creation.getUserType(), creation.getSubCategoryTypeWithCust(),
                            creation.getBusinessTypeWithCust(), creation.getCustName(), creation.getEmailID(),
                            creation.getMobileNumber(), creation.getAdditionalPersonName(), creation.getMobileNumber2(),
                            creation.getTelephoneNumber(), creation.getShopImage(), edBillingAddress.getText().toString(),
                            edArea.getText().toString(), updateDealerRegister.getCity(), "",
                            updateDealerRegister.getState(), edPincode.getText().toString(), String.valueOf(Lat),
                            String.valueOf(lng), cityBit, stateBit, CountryBit, TaxType, GSTNumber, PanNumber,
                            BankName, BranchBank, AccountNumber, BankIFSCCode, BankCityName, 1599,
                            Integer.parseInt(prefManager.getCustId().get(CUST_ID)), 1, creation.getCreatedDate(),
                            creation.isBGSMember(), creation.getIsProfessional(), creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);
                }
                value = true;
                if (creation.getUserImageFile() == null)
                    dealerRegister.setUserImage(creation.getUserImage());


                dealerRegister.setPanNumber(creation.getPanNumber());
                dealerRegister.setAccountnumber(creation.getAccountnumber());
                dealerRegister.setiFSCCode(creation.getiFSCCode());
                dealerRegister.setBankCity(creation.getBankCity());
                dealerRegister.setBankBranchName(creation.getBankBranchName());
                dealerRegister.setTinNumber(creation.getTinNumber());
                dealerRegister.setRegistrationType(creation.getRegistrationType());


                prefManager.saveObjectToSharedPreference("customer", dealerRegister);
            } else {
                Toast.makeText(getContext(), "Select the Interested Area", Toast.LENGTH_SHORT).show();
            }


        }

        return value;

    }

    private boolean ValidatePincode() {
        String str = edPincode.getText().toString();

        if (str.isEmpty()) {
            edPincode.setError("Please enter pincode");
            return false;
        } else {
            return true;
        }
    }

    private void getStateCityFromPincode(String pincode) {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<StateCityResponse>> call = customer_interface.getStateCityFromPincode(pincode);
        call.enqueue(new Callback<List<StateCityResponse>>() {
            @Override
            public void onResponse(Call<List<StateCityResponse>> call, Response<List<StateCityResponse>> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        if (response.body() != null) {
                            if (response.body().size() == 0) {
                                CustomToast.showToast(getContext(), "State and city not available for this pincode");
                                isStateFound = false;
                                cityList.clear();
                                CityId = 0;
                                spCity.setText("");
                                //hide keyboard
                                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(edPincode.getApplicationWindowToken(), 0);
                                return;
                            }
                            List<City> newCityList = new ArrayList<>();
                            cityList = new ArrayList<>();
                            for (int i = 0; i < response.body().size(); i++) {
                                newCityList.add(new City(response.body().get(i).getID(),
                                        response.body().get(i).getStateID(),
                                        response.body().get(i).getStatewithCityID(),
                                        response.body().get(i).getVillageLocalityname(),
                                        response.body().get(i).getStateName(),
                                        response.body().get(i).getPinCode()));
                            }
                            cityList.addAll(newCityList);
                            cityAdapter = new CityAdapter(getActivity(), cityList, UpdateBillingAddress.this);
//                            recyclerViewCity.setAdapter(cityAdapter);
//                            cityAdapter.notifyDataSetChanged();
                            /*if(city == null) city = cityList.get(0);
                            else {
                                for (City currentCity : cityList) {
                                    if(city.getID() == currentCity.getID()) city = currentCity;
                                }
                            }*/
                            city = cityList.get(0);
                            state = new State(city.getStateID(), city.getStateName());
                            StateId = state.getStateID();
                            isStateFound = true;
                            /*City firstCity = new City(response.body().get(0).getStateID(),
                                    response.body().get(0).getStatewithCityID(),
                                    response.body().get(0).getVillageLocalityname(),
                                    response.body().get(0).getStateName());
                            city = firstCity;*/
                            if (shouldUpdateAddress) {
                                if (city.getStateName() != null)
                                    spState.setText(city.getStateName());
                                if (city.getVillageLocalityname() != null)
                                    spCity.setText(city.getVillageLocalityname());
                            }
                            //hide keyboard
                            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(edPincode.getApplicationWindowToken(), 0);
                            shouldUpdateAddress = true;

//                        getStateByName();
                        }
                        break;
                    case 503:
                    case 404:
//                        getStateByName();
                        cityList.clear();
                        CityId = 0;
                        spCity.setText("");
                        CustomToast.showToast(getContext(), "City not found");
                        //hide keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(edPincode.getApplicationWindowToken(), 0);
                        break;

                    case 401:
//                        getStateByName();
                        SessionDialog.CallSessionTimeOutDialog(getContext());
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<StateCityResponse>> call, Throwable t) {
//                getStateByName();
                CommonUtils.showToast(getContext(), call.toString());
            }
        });

    }

    private void getCityByName() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<City> call = customer_interface.getCityByName(spCity.getText().toString().trim());
        call.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        city = response.body();
                        if (city != null && city.getVillageLocalityname() != null)
                            spCity.setText(city.getVillageLocalityname());
                        getStateByName();
                        break;
                    case 503:
                    case 404:
                        getStateByName();
                        cityList.clear();
                        CityId = 0;
                        spCity.setText("");
                        CustomToast.showToast(getContext(), "City not found");
                        //hide keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(edPincode.getApplicationWindowToken(), 0);
                        break;

                    case 401:
                        getStateByName();
                        SessionDialog.CallSessionTimeOutDialog(getContext());
                        break;

                }
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
//                CommonUtils.showToast(getContext(), call.toString());
                getStateByName();
            }
        });

    }

    private void getStateByName() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<State> call;
        if (isStateFound)
            call = customer_interface.getStateByName(spState.getText().toString().trim());
        else call = customer_interface.getStateByName(city.getStateName());
        call.enqueue(new Callback<State>() {
            @Override
            public void onResponse(Call<State> call, Response<State> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        state = response.body();
                        if (state != null && state.getStateName() != null)
                            spState.setText(state.getStateName());
                        break;
                    case 503:
                    case 404:
                        CustomToast.showToast(getContext(), "State not found");
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(getContext());
                        break;

                }
            }

            @Override
            public void onFailure(Call<State> call, Throwable t) {
//                CommonUtils.showToast(getContext(), call.toString());
            }
        });

    }
}