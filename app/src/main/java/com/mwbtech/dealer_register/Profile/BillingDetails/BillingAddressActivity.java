package com.mwbtech.dealer_register.Profile.BillingDetails;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.creation;
import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.dealerRegister;
import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.prefManager;
import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.salesmanID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
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
import com.mwbtech.dealer_register.LoginRegister.TakeTourActivity;
import com.mwbtech.dealer_register.PojoClass.City;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.PojoClass.ErrorBodyResponse;
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

public class BillingAddressActivity extends Fragment implements View.OnClickListener, CityAdapter.CitySelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Button btnNext;
    View billView;
    CallToFragment callToFragment;
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

    EditText spState, spCity;
    int pos = 2, CustID;
    String Token;
    City city;
    State state;

    StateAdapter stateAdapter;
    CityAdapter cityAdapter;

    TextView map_address;
    boolean cityBit = false, stateBit = false, CountryBit = false, districtBit = false, isUpdatedOnce = false,
            shouldUpdateAddress = true, isStateFound = false, isStateChanged = false, isTypingPincode = true;
    Customer_Interface customer_interface;
    List<State> stateList;
    List<City> cityList;

    String address;
    Dialog mDialogState, mDialogCity, mDialogOther, mDialogExit;
    EditText dialogState, dialogCity;
    ListView listViewState;
    RecyclerView recyclerViewCity;
    Button btnExit, btnMap;

    double latitude, longitude;
    EditText edBillingAddress, edArea, edPincode;

    int StateId = 0, CityId = 0;

    TextView chCity, chState, chCountry, chDistrict;
    ImageView imgCity, imgState, imgNational, imgDistrict;
    LinearLayout layoutCity, layoutState, layoutNational, layoutDistrict;
    Boolean isCitySelected = false, isDistrictSelected = false, isStateSelected = false, isNationalSelected = false;

    File UserImageFile;
    File imgFile;
    Context context;


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {

            if (activity instanceof CallToFragment) {
                callToFragment = (CallToFragment) activity;
            } else {
                throw new RuntimeException(activity.toString()
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
        billView = inflater.inflate(R.layout.billing_address, container, false);
        context = getContext();
        checkLocationPermission();

        TextView map_address = (TextView) billView.findViewById(R.id.address);
        edBillingAddress = billView.findViewById(R.id.edBillAddress);
        edArea = billView.findViewById(R.id.edArea);
        linearLayout = billView.findViewById(R.id.linear);

        edPincode = billView.findViewById(R.id.edPincode);
        spState = billView.findViewById(R.id.spinnerState);
        spCity = billView.findViewById(R.id.spinnerCity);

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
        tvLatLong = billView.findViewById(R.id.latlong);

        btnNext = billView.findViewById(R.id.btnNext);
        btnExit = billView.findViewById(R.id.btnExit);
        btnMap = billView.findViewById(R.id.btnMap);


        btnExit.setOnClickListener(this);
        spState.setOnClickListener(this);
        spCity.setOnClickListener(this);

        btnMap.setOnClickListener(this::onClick);
        btnNext.setOnClickListener(this);

        layoutState.setOnClickListener(this);
        layoutDistrict.setOnClickListener(this);
        layoutCity.setOnClickListener(this);
        layoutNational.setOnClickListener(this);

        edPincode.setOnTouchListener((v, event) -> {
            isTypingPincode = true;
            return false;
        });
        edPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6 && isTypingPincode){
                    /*getad(s.toString());*/
                    getStateCityFromPincode(s.toString().trim());
                    isTypingPincode = false;
                }
            }
        });

        edArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edArea.setCursorVisible(true);
            }
        });
//        Bundle extras = getActivity().getIntent().getExtras();
//        if(extras != null) {
//            String address = extras.getString("latitude");
//            System.out.println("Latitude in billing"+address);
//
//        }
        try {
            CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
            Token = prefManager.getToken().get(TOKEN);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        sharePreferenceData();
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
            creation = prefManager.getSavedObjectFromPreference(context, "mwb-welcome", "customer", DealerRegister.class);
            if (creation != null) {
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

                if (creation.getCity() == null) {
                    spCity.setText("");
                } else {
                    CityId = city.getID();
                    city = creation.getCity();
                    spCity.setText("" + creation.getCity());
                }
                if (creation.getState() == null) {
                    spState.setText("");
                } else {
                    state = creation.getState();
                    StateId = state.getStateID();
                    spState.setText("" + creation.getState());
                }

                if (creation.getPincode() != null && !TextUtils.isEmpty(creation.getPincode())) {
                    edPincode.setText("" + creation.getPincode());
//                    getad(creation.getPincode());
//                    getStateCityFromPincode();
                } else {
                    edPincode.setText("");
                }/*

                if (creation.getInterstCity() == null) {
                    chCity.setChecked(false);
                } else {
                    chCity.setChecked(creation.getInterstCity());
                }
                if (creation.getInterstDistrict() == null) {
                    chDistrict.setChecked(false);
                } else {
                    chDistrict.setChecked(creation.getInterstDistrict());
                }
                if (creation.getInterstState() == null) {
                    chState.setChecked(false);
                } else {
                    chState.setChecked(creation.getInterstState());
                }
                if (creation.getInterstCountry() == null) {
                    chCountry.setChecked(false);
                } else {
                    chCountry.setChecked(creation.getInterstCountry());
                }*/


                setCityAsSelected(creation.getInterstCity());
                setDistrictAsSelected(creation.getInterstDistrict());
                setStateAsSelected(creation.getInterstState());
                setNationalAsSelected(creation.getInterstCountry());

                if (!TextUtils.isEmpty(creation.getLattitude())) {
                    latitude = Double.parseDouble(creation.getLattitude());
                    System.out.println("HIIIIIIIIII" + latitude);

                }
                if (!TextUtils.isEmpty(creation.getLangitude())) {
                    longitude = Double.parseDouble(creation.getLangitude());
                    System.out.println("HIIIIIIIIIIIII" + longitude);
                }
                Log.e("Image_getUserImageFile", creation.getUserImageFile().toString());
                if (creation.getUserImageFile() != null) {
                    UserImageFile = creation.getUserImageFile();

                }
                map_address.setText(address);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCityAsSelected(boolean shouldSelect) {
        if (shouldSelect) {
            isCitySelected = true;
            cityBit = true;
            layoutCity.setSelected(true);
            layoutCity.setBackgroundResource(R.drawable.shape_rectangle_red);
            imgCity.setImageResource(R.drawable.ic_city_selected);
            chCity.setTypeface(null, Typeface.BOLD);
            chCity.setTextColor(getResources().getColor(R.color.white));
        } else {
            layoutCity.setSelected(false);
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
            layoutDistrict.setSelected(true);
            isDistrictSelected = true;
            layoutDistrict.setBackgroundResource(R.drawable.shape_rectangle_red);
            imgDistrict.setImageResource(R.drawable.ic_district_selected);
            chDistrict.setTypeface(null, Typeface.BOLD);
            chDistrict.setTextColor(getResources().getColor(R.color.white));
        } else {
            layoutDistrict.setSelected(false);
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
            layoutState.setSelected(true);
            stateBit = true;
            isStateSelected = true;
            layoutState.setBackgroundResource(R.drawable.shape_rectangle_red);
            imgState.setImageResource(R.drawable.ic_state_selected);
            chState.setTypeface(null, Typeface.BOLD);
            chState.setTextColor(getResources().getColor(R.color.white));
        } else {
            layoutState.setSelected(false);
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
            layoutNational.setSelected(true);
            CountryBit = true;
            isNationalSelected = true;
            layoutNational.setBackgroundResource(R.drawable.shape_rectangle_red);
            imgNational.setImageResource(R.drawable.ic_national_selected);
            chCountry.setTypeface(null, Typeface.BOLD);
            chCountry.setTextColor(getResources().getColor(R.color.white));
        } else {
            layoutNational.setSelected(false);
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

    /***
     *-------Initializing OnClick Methods for Views---------*
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            /*
             *-------Initializing OnClick Method for "#########" Buttom---------*
             */

            case R.id.btnNext:
                getAlert();
                /*if (!ValidateBillingAddress() | !ValidateArea() | !ValidateState() | !ValidateCity() | !ValidatePincode()) {
                    return;
                }
                else {

                    if (chCity.isChecked() || chState.isChecked() || chCountry.isChecked()) {
                        creation = prefManager.getSavedObjectFromPreference(context, "mwb-welcome", "customer", DealerRegister.class);
                        dealerRegister = new DealerRegister(CustID, creation.getFirmName(), creation.getPassword(), creation.getUserType(),
                                creation.getSubCategoryTypeWithCust(), creation.getBusinessTypeWithCust(), creation.getCustName(),
                                creation.getEmailID(), creation.getMobileNumber(), creation.getAdditionalPersonName(),
                                creation.getMobileNumber2(), creation.getTelephoneNumber(), creation.getShopImage(),
                                edBillingAddress.getText().toString(), edArea.getText().toString(), dealerRegister.getCity(),
                                "", state, edPincode.getText().toString(), String.valueOf(Lat),
                                String.valueOf(lng), cityBit, stateBit, CountryBit, null, null,
                                null, null, null, null, null, null,
                                salesmanID, creation.getCustID(), 1, creation.getCreatedDate(),
                                creation.isBGSMember(), creation.getUserImageFile(),creation.getBusinessDemandID());
                        Log.e("Dealer class...",dealerRegister.toString());
                        Log.e("Demandid...",String.valueOf(creation.getBusinessDemandID()));
                        prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                        callToFragment.communicateFragment(pos);
                    } else {
                        CustomToast.showToast(getActivity(), "Please Select Selling Area");
                    }
                    startActivity(new Intent(getActivity(), DashboardActivity.class));
                }
*/
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
            /*
             *-------Launching  Google Map---------*
             */
            case R.id.btnMap:
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                mapFragment.getMapAsync(this);
//                tvLatLong.setVisibility(View.VISIBLE);
                btnMap.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                break;

            /*
             * sending The dealer KYC Details to server to save the details and exit*
             */
            case R.id.btnExit:
                if(!isStateFound) {
                    Toast.makeText(context, "Can't proceed without State and City of the given pincode.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!ValidateBillingAddress() | !ValidateArea() | !ValidateState() | !ValidateCity() | !ValidatePincode()) {
                    return;
                } else {
                    if (layoutCity.isSelected() || layoutState.isSelected() || layoutDistrict.isSelected() || layoutNational.isSelected()) {

//                        mDialogExit = new Dialog(getActivity());
//                        mDialogExit.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                        mDialogExit.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                        mDialogExit.setContentView(R.layout.exit_register_screen);
//                        mDialogExit.setCancelable(false);
//                        mDialogExit.findViewById(R.id.tvLogoutOk).setOnClickListener(this);
//                        mDialogExit.findViewById(R.id.tvLogoutCancel).setOnClickListener(this);
//                        mDialogExit.show();
                        creation = prefManager.getSavedObjectFromPreference(context, "mwb-welcome", "customer", DealerRegister.class);
                        dealerRegister = new DealerRegister(creation.getCustID(), creation.getFirmName(), creation.getPassword(), creation.getUserType(),
                                creation.getSubCategoryTypeWithCust(), creation.getBusinessTypeWithCust(), creation.getCustName(),
                                creation.getEmailID(), creation.getMobileNumber(), creation.getAdditionalPersonName(), creation.getMobileNumber2(),
                                creation.getTelephoneNumber(), creation.getShopImage(), edBillingAddress.getText().toString(),
                                edArea.getText().toString(), city, "", state, edPincode.getText().toString(),
                                String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit, CountryBit, creation.getRegistrationType(), creation.getTinNumber(),
                                creation.getPanNumber(), creation.getBankname(), creation.getBankBranchName(), creation.getAccountnumber(), creation.getiFSCCode(), creation.getBankCity(), salesmanID,
                                creation.getCustID(), 1, creation.getCreatedDate(), creation.isBGSMember(), creation.getIsProfessional(),
                                creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);
                        prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                        //mDialogExit.dismiss();
                        dealerRegister.setIsRegistered(1);
                        Log.e("IsRegiste", String.valueOf(dealerRegister.getIsRegistered()));
                        Log.e("IsRegiste", dealerRegister.toString());
                        Log.e("Demandid...", String.valueOf(creation.getBusinessDemandID()));
                        imgFile = dealerRegister.getUserImageFile();
                        sendDealerRegisterDetailsToServerExit(creation.getCustID(), dealerRegister);
                    } else {
                        CustomToast.showToast(getActivity(), "Please Select Selling Area");
                    }
                }
                break;

            case R.id.tvLogoutOk:
                creation = prefManager.getSavedObjectFromPreference(context, "mwb-welcome", "customer", DealerRegister.class);
                dealerRegister = new DealerRegister(creation.getCustID(), creation.getFirmName(), creation.getPassword(), creation.getUserType(),
                        creation.getSubCategoryTypeWithCust(), creation.getBusinessTypeWithCust(), creation.getCustName(),
                        creation.getEmailID(), creation.getMobileNumber(), creation.getAdditionalPersonName(), creation.getMobileNumber2(),
                        creation.getTelephoneNumber(), creation.getShopImage(), edBillingAddress.getText().toString(),
                        edArea.getText().toString(), city, "", state, edPincode.getText().toString(),
                        String.valueOf(Lat), String.valueOf(lng), cityBit, stateBit, CountryBit, creation.getRegistrationType(), creation.getTinNumber(),
                        creation.getPanNumber(), creation.getBankname(), creation.getBankBranchName(), creation.getAccountnumber(), creation.getiFSCCode(), creation.getBankCity(), salesmanID,
                        creation.getCustID(), 1, creation.getCreatedDate(), creation.isBGSMember(), creation.getIsProfessional(),
                        creation.getUserImageFile(), creation.getBusinessDemandID(), creation.getBusinessDemandWithCust(), districtBit);
                prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                //mDialogExit.dismiss();
                dealerRegister.setIsRegistered(1);
                imgFile = dealerRegister.getUserImageFile();
                Log.e("Demandid...", String.valueOf(creation.getBusinessDemandID()));
                sendDealerRegisterDetailsToServerExit(creation.getCustID(), dealerRegister);
                break;

            case R.id.tvLogoutCancel:
                mDialogExit.dismiss();
                break;

            /*
             * Opening a Pop Up Dialog Box to Select State*
             */
            case R.id.spinnerState:
                mDialogState = new Dialog(getActivity());
                mDialogState.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialogState.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mDialogState.setContentView(R.layout.dialog_category_list);
                mDialogState.setCancelable(true);
                dialogState = (EditText) mDialogState.findViewById(R.id.edState);
                AppCompatImageView Clear_txt_btn = mDialogState.findViewById(R.id.clear_txt_Prise);
                listViewState = (ListView) mDialogState.findViewById(R.id.recyclerCategory);
                ImageView imageView = (ImageView) mDialogState.findViewById(R.id.cancel_category);
                mDialogState.show();
                getStateFromServer();

                imageView.setOnClickListener(v12 -> mDialogState.dismiss());

                listViewState.setOnItemClickListener((parent, view, position, id) -> {
                    state = (State) listViewState.getItemAtPosition(position);
                    StateId = state.getStateID();
                    isStateChanged = true;
                    spState.setText("" + state.getStateName());
                    if (cityList == null) cityList = new ArrayList<>();
                    cityList.clear();
                    spCity.setText("");
                    edArea.setCursorVisible(false);
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
                            stateAdapter.getFilter().filter(s);
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
                break;

            /*
             *------ Opening a Pop Up Dialog Box to Select City----------*
             */

            case R.id.spinnerCity:
                if (spState.getText().toString().isEmpty()) {
                    CustomToast.showToast(getActivity(), "Please Select State");
                } else {
                    isStateChanged = true;
                    mDialogCity = new Dialog(getActivity());
                    mDialogCity.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mDialogCity.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    mDialogCity.setContentView(R.layout.dialog_city_item_layout);
                    mDialogCity.setCancelable(false);
                    dialogCity = (EditText) mDialogCity.findViewById(R.id.edCity);
                    AppCompatImageView Clear_txt_btn1 = mDialogCity.findViewById(R.id.clear_txt_Prise);
                    recyclerViewCity = mDialogCity.findViewById(R.id.recyclerCity);
                    recyclerViewCity.setHasFixedSize(true);
                    recyclerViewCity.setLayoutManager(new LinearLayoutManager(getActivity()));
                    if (cityAdapter == null) {
                        cityAdapter = new CityAdapter(getActivity(), cityList, BillingAddressActivity.this);
                    }
                    recyclerViewCity.setAdapter(cityAdapter);
                    cityAdapter.notifyDataSetChanged();
                    ImageView imageView3 = (ImageView) mDialogCity.findViewById(R.id.cancel_childcategory);
                    mDialogCity.show();

                    imageView3.setOnClickListener(v1 -> {
                        //hide keyboard
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(edPincode.getApplicationWindowToken(), 0);
                        mDialogCity.dismiss();
                    });
                    Clear_txt_btn1.setOnClickListener(view -> {
                        if (!dialogCity.getText().toString().isEmpty()) {
                            dialogCity.getText().clear();
                            Clear_txt_btn1.setVisibility(View.GONE);

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
                                if (s.length() <= 0) {
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
                                if (s.length() <= 0) {
                                    Clear_txt_btn1.setVisibility(View.GONE);
                                } else if (cityName.length() >= 3) {
//                                    if (isStateFound && cityList.size()>0) cityAdapter.getFilter().filter(s);
                                    if (isStateChanged && isStateFound) getCityFromServer(StateId, cityName);
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

            /*
             * Closing State selecting Pop Up Dialog Box *
             */

            case R.id.cancel_category:
                mDialogState.dismiss();
                break;
            /*
             * Closing City selecting Pop Up Dialog Box *
             */

            case R.id.cancel_childcategory:
                //hide keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(edPincode.getApplicationWindowToken(), 0);
                mDialogCity.dismiss();
                break;
            /*
             * Closing Other City selecting Pop Up Dialog Box *
             */


            case R.id.cancel_city:
                mDialogOther.dismiss();
                break;


        }
    }

    /***
     * Storing city Value when City is Selected*
     ***/
    @Override
    public void onCitySelected(City cityy) {
        CityId = cityy.getID();
        city = cityy;
        edPincode.setText("" + cityy.getPinCode());
        isTypingPincode = false;
        isStateChanged = false;
        dealerRegister.setCity(city);
        spCity.setText("" + cityy);
        edArea.setCursorVisible(true);
        //hide keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(edPincode.getApplicationWindowToken(), 0);
        mDialogCity.dismiss();
        edArea.requestFocus();
    }

    private void sendDealerRegisterDetailsToServerExit(Integer custID, DealerRegister dealerRegister) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(context, "Please wait.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        MultipartBody.Part body = null, body1 = null;
        // File file1,file2;
        RequestBody requestBody1, requestBody2, requestBody_custId;
        try {
            //file1 = new File(ImagePath);
            //File imgFile = new File(creation.isUserImage());
            requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
            requestBody_custId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(custID));
            body = MultipartBody.Part.createFormData("file", imgFile.getName(), requestBody1);
            customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
            Call<ResponseBody> call = customer_interface.updatePhoto("bearer " + Token, body, requestBody_custId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int statusCode = response.code();
                    Log.e("statuscode...", String.valueOf(statusCode));
                    switch (statusCode) {
                        case 200:
                            progressDialog.dismiss();
                    /*        CustomToast.showToast(getActivity(), "Congratulations!! You Are Now Registered On Kalpavriksha App");
                            Intent i = new Intent(context,OnFirstRegistration.class);
                            startActivity(i);
                   */
                            break;
                        case 404:
                            CustomToast.showToast(getActivity(), "Invalid pincode");
                            progressDialog.dismiss();
                            break;

                        case 500:
                            progressDialog.dismiss();
                            CustomToast.showToast(getActivity(), "Server Error");
                            break;

                        case 400:
                            progressDialog.dismiss();
                            CustomToast.showToast(getActivity(), "Request Not Granted");
                            break;

                        case 401:
                            progressDialog.dismiss();
                            SessionDialog.CallSessionTimeOutDialog(getActivity());
                            break;

                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("failure ", "error..." + t.getMessage());
                    if (t instanceof IOException) {
                        Toast.makeText(getActivity(), "Time out..", Toast.LENGTH_LONG).show();
                    } else {
                        CustomToast.showToast(getActivity(), "Bad response from server.. Try again later ");
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
            Log.e("model...", dealerRegister.toString());
            Gson gson = new Gson();
            String json = gson.toJson(dealerRegister);
            Log.e("jsonKYC.......", json);
            //Call<DealerRegister> call = customer_interface.updateDealerRegisterDetails("bearer " + Token, custID, dealerRegister,body);
            Call<DealerRegister> call = customer_interface.updateDealerRegister("bearer " + Token, dealerRegister);
            call.enqueue(new Callback<DealerRegister>() {
                @Override
                public void onResponse(Call<DealerRegister> call, Response<DealerRegister> response) {
                    int statusCode = response.code();
                    progressDialog.dismiss();
                    switch (statusCode) {
                        case 200:

                            DealerRegister dealerresp = response.body();
                            try {
                                /*if (dealerresp.getIsWelcomeMsg().equals("1")) {
                                    showNavigationDialog();
                                    startActivity(new Intent(getActivity(), ThankyouScreen.class));
                                } else {*/
                                //CustomToast.showToast(getActivity(), "Congratulations!! You Are Now Registered On Kalpavriksha App");
                                /*startActivity(new Intent(getActivity(), LoginActivity.class));*/
                                showNavigationDialog();
//                                }
                            } catch (Exception Ex) {
                                Log.e("Exception..", Ex.getMessage());
                            }
                            break;


                        case 500:

                            CustomToast.showToast(getActivity(), "Server Error");
                            break;

                        case 400:
                        case 404:
                            CustomToast.showToast(getActivity(), "Invalid pincode");
                            break;

                        case 401:

                            SessionDialog.CallSessionTimeOutDialog(getActivity());
                            break;
                        default: {

                            ErrorBodyResponse errorBodyResponse = new ErrorBodyResponse();
                            try {
                                errorBodyResponse = new Gson().fromJson(response.errorBody().string(), ErrorBodyResponse.class);

                            } catch (IOException e) {
                                e.printStackTrace();

                            }
                            CustomToast.showToast(getActivity(), errorBodyResponse.getDisplayMessage());

                        }

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


    }

    private void showNavigationDialog() {
        AlertDialog checkMailDialog;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View layoutView =
                this.getLayoutInflater().inflate(R.layout.dialog_navigation, null);

        Button kycButton = layoutView.findViewById(R.id.take_tour_button);
        AppCompatTextView ctnButton = layoutView.findViewById(R.id.ctn_button);
        dialogBuilder.setView(layoutView);
        dialogBuilder.setCancelable(false);
        checkMailDialog = dialogBuilder.create();

        checkMailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        checkMailDialog.show();
        kycButton.setOnClickListener(v -> {
            checkMailDialog.dismiss();
            startActivity(new Intent(getActivity(), TakeTourActivity.class));
//            VerifyPhone.this.finish();
        });
        ctnButton.setOnClickListener(v -> {
            checkMailDialog.dismiss();
            startActivity(new Intent(getActivity(), DashboardActivity.class).putExtra("isNewUser", false));
//            VerifyPhone.this.finish();
        });
    }

    /***
     * Fetching List Of States from Server*
     * ***/
    private void getStateFromServer() {
        spCity.getText().clear();
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
                        CustomToast.showToast(getActivity(), "Server Error");
                        progressDialog.dismiss();
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
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(getActivity(), "Bad response from server.. Try again later ");
                }
            }
        });
    }

    /***
     * Fetching List Of Cities from Server*
     * ***/
    private void getCityFromServer(int stateId, String cityName) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(context, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<City>> call = customer_interface.getAllCity(/*"bearer " + Token,*/ stateId, cityName);
        Log.e("stateid...", String.valueOf(stateId));
        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                int statusCode = response.code();
                Log.e("response code...", String.valueOf(statusCode));
                progressDialog.dismiss();
                switch (statusCode) {
                    case 200:

                        cityList = new ArrayList<>(response.body());
                        if (cityList.isEmpty()) {
                            CustomToast.showToast(getActivity(), "No city found");
                        } else {
                            cityAdapter = new CityAdapter(getActivity(), cityList, BillingAddressActivity.this);
                            recyclerViewCity.setAdapter(cityAdapter);
                            cityAdapter.notifyDataSetChanged();
                        }
                        break;

                    case 404:
                        progressDialog.dismiss();
                        CustomToast.showToast(getActivity(), "No city found");
                        break;


                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(getActivity(), "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(getActivity());
                        break;

                    default:
                        CustomToast.showToast(getActivity(), response.message());

                }

            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
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


    /***
     * Selecting Interested Area Limit To do Business *
     ***/
   /* @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.chCity:
                if (isChecked) {
                    cityBit = true;
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
                    layoutState.setBackgroundResource(R.drawable.shape_rectangle_red);
                    imgState.setImageResource(R.drawable.ic_state_selected);
                    stateBit = true;
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
    private boolean ValidateBillingAddress() {
        String str = edBillingAddress.getText().toString();
        if (str.isEmpty()) {
            edBillingAddress.setError("Please Enter Building/Road Address");
            CustomToast.showToast(getActivity(), "Please Enter Building/Road Address");
            return false;
        } else {
            return true;
        }
    }

    private void getAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setCancelable(true);
        builder1.setTitle("");
        builder1.setMessage("Do you want to exit from Registration ?");
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.setCanceledOnTouchOutside(false);
        alert11.show();
    }

    /***
     * Validating The Billing Area Entered By User*
     ***/

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

    /***
     * Validating The City Selected By User*
     ***/

    private boolean ValidateCity() {
        String str = spCity.getText().toString();

        if (str.isEmpty()) {
            //spCity.setError("Please Select city");
            CustomToast.showToast(getActivity(), "Please Select city");
            return false;
        } else {
            return true;
        }
    }

    /***
     * Validating The PinCode Entered By User *
     ***/

    private boolean ValidatePincode() {
        String str = edPincode.getText().toString();

        if (str.isEmpty()) {
            edPincode.setError("Please enter pincode");
            return false;
        } else {
            return true;
        }
    }

    /***
     * This method will be called when user Press Back button *
     * ***/
    public void onBackClicked() {
        isUpdatedOnce = true;
        callToFragment.communicateFragment(0);
    }

    /***
     * Initializing The Google Api Client
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    /***
     * Requesting Location Permission to User when the Google Api client is Connected*
     ***/
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if(!edPincode.getText().toString().equals("")) getad(edPincode.getText().toString());
//       else{
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
//        }

    }

    /**
     * Checking The Location Permission granted or not if not Again requesting Permission
     **/
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
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
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
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
        final Geocoder geocoder = new Geocoder(context);

        try {
            List<Address> addresses = geocoder.getFromLocationName(pincode, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Use the address as needed
                String message = String.format("Latitude: %f, Longitude: %f",
                        address.getLatitude(), address.getLongitude());
//                edBillingAddress.setText(address.getAddressLine(0));
//                edArea.setText(address.getAdminArea());
                double lat = address.getLatitude();
                double lng = address.getLongitude();
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
        mLastLocation.setLatitude(latitude);
        mLastLocation.setLongitude(longitude);
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker

        Lat = latitude;
        System.out.println("HIIIII" + Lat);
        lng = longitude;

        latLng = new LatLng(latitude, longitude);
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng);


        try {
            Log.i("12343", String.valueOf(latLng));
            tvLatLong.setText("Your Location: " + latLng + "\n Address: " + getCompleteAddressString(latitude, longitude));
        } catch (Exception e) {
            e.printStackTrace();
        }
        markerOptions.title("Current Location : " + latLng);
        markerOptions.anchor(0.5f, 0.5f);
//        markerOptions.snippet(getCompleteAddressString(latitude, longitude));
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
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    /**
     * Changing Marker position Of User Location
     * when Location Is Changed*
     **/
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker

        Lat = location.getLatitude();
//        System.out.println("HIIIII" + Lat);
        lng = location.getLongitude();

        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng);


        /*try {
            Log.i("12343", String.valueOf(latLng));
            tvLatLong.setText("Your Location: " + latLng + "\n Address: " + getCompleteAddressString(location.getLatitude(), location.getLongitude()));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        markerOptions.title("Current Location : " + latLng);
        markerOptions.anchor(0.5f, 0.5f);
//        markerOptions.snippet(getCompleteAddressString(location.getLatitude(), location.getLongitude()));
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


    }

    /**
     * ================UnUsed Method===============*
     **/
    private String getCompleteAddress(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {

                Address returnedAddress = addresses.get(0);
                Log.e("City", "....." + addresses.get(0).getLocality());
                Log.e("State", "....." + addresses.get(0).getAdminArea());
                Log.e("Area", "....." + addresses.get(0).getSubLocality());
                Log.e("pincode", "....." + addresses.get(0).getPostalCode());
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My loction address", strReturnedAddress.toString());
            } else {
                Log.w("My loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My loction address", "Canont get Address!");
        }
        return strAdd;
    }

    /**
     * Method To Get Complete Address in string Format *
     **/
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {

                Address returnedAddress = addresses.get(0);
                Log.e("City", "....." + addresses.get(0).getLocality());
                Log.e("State", "....." + addresses.get(0).getAdminArea());
                Log.e("Area", "....." + addresses.get(0).getSubLocality());
                Log.e("pincode", "....." + addresses.get(0).getPostalCode());
//                edBillingAddress.setText(addresses.get(0).getAddressLine(0));
//                edArea.setText(addresses.get(0).getLocality());
                spCity.setText(addresses.get(0).getLocality());

                if (addresses.get(0).getAdminArea() == null || addresses.get(0).getAdminArea().equals(""))
                    isStateFound = false;
                else isStateFound = true;
                spState.setText(addresses.get(0).getAdminArea());
//                getCityByName();
               /* edBillingAddress.setText(addresses.get(0).getAddressLine(0));
                edPincode.setText(addresses.get(0).getPostalCode());
                edArea.setText(addresses.get(0).getSubLocality());
                spState.setText(addresses.get(0).getAdminArea());
                spCity.setText(addresses.get(0).getLocality());
                areaName = addresses.get(0).getSubLocality();*/
                shouldUpdateAddress = true;
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My loction address", strReturnedAddress.toString());
            } else {
                Log.w("My loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My loction address", "Canont get Address!");
        }
        return strAdd;
    }

    /**
     * This Method Is Called When Map is Ready*
     **/
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
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
                    Map dataModel = (Map) markers.get(marker);
                    String title = (String) dataModel.toString();
                    //Toast.makeText(context, "" + title, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //openPlacePicker();
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker position) {

                Map dataModel = (Map) markers.get(position);
                String title = (String) dataModel.toString();

                //Toast.makeText(getActivity(), ""+dataModel.get("latitude"), Toast.LENGTH_SHORT).show();
                openDialogMethod(dataModel.get("title").toString(), dataModel.get("snippet").toString(), tvLatLong.getText().toString());

            }
        });
    }

    /**
     * Opening A PopUp Dialog to Show Details Of location *
     **/
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
                        Toast.makeText(context, "City not found, please choose from list", Toast.LENGTH_LONG).show();
                        break;

                    case 401:
                        getStateByName();
                        SessionDialog.CallSessionTimeOutDialog(context);
                        break;

                }
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                getStateByName();
//                CommonUtils.showToast(context, call.toString());
            }
        });

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
                            List<City> newCityList= new ArrayList<>();
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
                            cityAdapter = new CityAdapter(getActivity(), cityList, BillingAddressActivity.this);
//                            recyclerViewCity.setAdapter(cityAdapter);
//                            cityAdapter.notifyDataSetChanged();
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
                        SessionDialog.CallSessionTimeOutDialog(context);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<StateCityResponse>> call, Throwable t) {
//                getStateByName();
                CommonUtils.showToast(context, call.toString());
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
                        Toast.makeText(context, "State not found, please choose from list", Toast.LENGTH_LONG).show();
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(context);
                        break;

                }
            }

            @Override
            public void onFailure(Call<State> call, Throwable t) {
//                CommonUtils.showToast(context, call.toString());
            }
        });

    }
}
