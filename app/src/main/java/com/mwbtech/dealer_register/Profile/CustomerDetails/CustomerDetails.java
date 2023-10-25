package com.mwbtech.dealer_register.Profile.CustomerDetails;

import static android.app.Activity.RESULT_OK;
import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.creation;
import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.dealerRegister;
import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.prefManager;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_PASSWORD;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_TYPE;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.BusinessDemandCustomer;
import com.mwbtech.dealer_register.Adapter.BusinessTypeAdapter;
import com.mwbtech.dealer_register.Adapter.SubCategoryAdapter;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.PojoClass.BusinessDemand;
import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.PojoClass.MainCategoryProduct;
import com.mwbtech.dealer_register.PojoClass.SubCategoryProduct;
import com.mwbtech.dealer_register.Profile.ProfileMain.UpdateMainActivity;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.DialogUtilsKt;
import com.mwbtech.dealer_register.Utils.FileCompressor;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.ProjectUtilsKt;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.DeflaterOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomerDetails extends Fragment implements View.OnClickListener, RecycleitemSelecetd, SubCategoryAdapter.RecycleitemSelecet, BusinessTypeAdapter.BusinessTypeClick {

    Spinner spLedger;
    Button btnNext, btnSub, btnSearch, btnExit;
    EditText edChild, edSub, edMain;
    View customerView;
    int pos = 1, CustID;
    String Token;
    CallToBillFragment callToBillFragment;
    int imgCount = 0;
    RecyclerView recyclerViewMother;
    ImageView imageView, Clear_txt_btn;
    GridView listBusines;
    CheckBox checkBoxSubCategory;
    Dialog mDialogSubCategory;
    FrameLayout layoutSelectAll;
    EditText edFirmName, edOwnerName, edMobileNo, edMobileNo1, edEmailId, edTelephone, edAdditionPerson, edProduct;
    TextView interestedCategory;
    Button submitBtn;
    AlertDialog.Builder builder, builder1;
    ImageView imgPhoto1, imgPhoto2;
    Button btnPhoto, btnPhoto1;
    Dialog mdialogPhoto;
    String imgBase64 = "";

    BusinessTypeAdapter businessTypeAdapter;
    List<BusinessType> businessTypeList = new ArrayList<>();
    List<SubCategoryProduct> subCategoryProducts;
    List<MainCategoryProduct> mainCategoryProductList;
    SubCategoryAdapter subCategoryAdapter;
    List<SubCategoryProduct> selectSubIdList = new ArrayList<>();
    List<SubCategoryProduct> selectedProductsInDialog = new ArrayList<>();
    List<SubCategoryProduct> FinalSelectedSubList = new ArrayList<>();
    Customer_Interface customer_interface;
    RecyclerView recyclerVieww;
    ChipGroup chipGroupDemo, chipGroupSearchDialog;
    ScrollView chipScroll;
    ConstraintLayout LinearBusinessDemand;

    //String regFname = "^(?![\\s.]+$)[a-zA-Z\\s.]*$";   //"^[A-Za-z]+$";
    String regNumber = "^[5-9][0-9]{9}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile("^" + "(?=.*[0-9])" + ".{10}" + "$");
    // String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    String yahooPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+";
    HorizontalScrollView hsv;
    boolean isProfessional = false, isUpdatedOnce = false;
    CheckBox checkbox1;
    //Image
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    static final long image_size = 2097152;
    File mPhotoFile1;
    File mPhotoFile2;
    FileCompressor mCompressor;
    int DemandID;

    GridView listBusinessDemand;
    List<BusinessDemand> businessDemandList;
    BusinessDemandCustomer businessDemandAdaper;
    private LinearLayout businessOwnerCheck, professionalCheck, layoutCategories;
    private ImageView imgBusinessOwner, imgProfessional;
    private TextView txtBusinessOwner, txtProfessional;
    private String pincode;

    private boolean isBusinessTypeSelected = false;


    /***
     * -----Initializing The Next Fragment in The Tab i.e,
     * BillingAddressFragment Using CallToBillFragment----*
     */
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            if (activity instanceof CallToBillFragment) {

                callToBillFragment = (CallToBillFragment) activity;
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
        setHasOptionsMenu(true);
    }

    /***
     * ----------Initializing All views, ImageFiles,sharedPreference and Variables -------*
     * ***/
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        customerView = inflater.inflate(R.layout.customer_details, container,false);
        imgPhoto1 = customerView.findViewById(R.id.imag1);
        imgPhoto1.setOnClickListener(this);
        mCompressor = new FileCompressor(getContext());
        progressDialog = ShowProgressDialog.createProgressDialog(getActivity());
        initializeViews();
        initializeSharedData();
        initializeRecyclerView();
        initializeClickEvent();
        getBusinessListFromWebServer();
        GetBusinessDemand();
        getCustomerDetails();
        sharePreferencesMethod();
        return customerView;
    }


    /***
     * --------------------Initializing All Views-------*
     ***/
    private void initializeViews() {
        edFirmName = customerView.findViewById(R.id.edFirmName);
        businessOwnerCheck = customerView.findViewById(R.id.bownerCk);
        professionalCheck = customerView.findViewById(R.id.professionalCk);
        imgBusinessOwner = customerView.findViewById(R.id.img_business_owner);
        imgProfessional = customerView.findViewById(R.id.img_professional);
        txtBusinessOwner = customerView.findViewById(R.id.txt_business_owner);
        txtProfessional = customerView.findViewById(R.id.txt_professional);
        layoutCategories = customerView.findViewById(R.id.layout_categories);
        edSub = customerView.findViewById(R.id.edMCategoryName);
        edMain = customerView.findViewById(R.id.edFCategoryName);
        edOwnerName = customerView.findViewById(R.id.edCustomer);
        edAdditionPerson = customerView.findViewById(R.id.edContactPerson);
        edEmailId = customerView.findViewById(R.id.edEmailId);
        interestedCategory = customerView.findViewById(R.id.textView6);
        edMobileNo = customerView.findViewById(R.id.edOwnerNo);
        edMobileNo1 = customerView.findViewById(R.id.edMobile);
        btnNext = customerView.findViewById(R.id.btnNext);
        btnExit = customerView.findViewById(R.id.btnExit);
        edTelephone = customerView.findViewById(R.id.edtelephone);
        listBusines = customerView.findViewById(R.id.listBusines);
        hsv = customerView.findViewById(R.id.horizontalScrollView1);
        chipGroupDemo = customerView.findViewById(R.id.chipgroup);
        submitBtn = customerView.findViewById(R.id.btnSubmit);/* not used*/
        checkbox1 = customerView.findViewById(R.id.checkbox1);
        listBusinessDemand = customerView.findViewById(R.id.listBusinesDemand);
        LinearBusinessDemand = customerView.findViewById(R.id.linearBusinessDemand);
        businessTypeList = new ArrayList<>();
        mainCategoryProductList = new ArrayList<>();
        submitBtn.setOnClickListener(this);
    }

    /***
     * ---------------Initializing CustId and Token --------*
     ***/
    private void initializeSharedData() {
        try {
            CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Token = prefManager.getToken().get(TOKEN);
    }

    /***
     * -------Initializing RecyclerView For List of Categories----------*
     * **/
    private void initializeRecyclerView() {
        recyclerVieww = customerView.findViewById(R.id.listSubcat);
        recyclerVieww.setHasFixedSize(true);
        recyclerVieww.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /***
     * --------------initializing onClickListener ----------------*
     * **/
    private void initializeClickEvent() {
        edSub.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnExit.setVisibility(View.GONE);
        professionalCheck.setOnClickListener(this);
        businessOwnerCheck.setOnClickListener(this);
        /*professionalCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                businessOwnerCheck.setChecked(false);
            }
        });
        businessOwnerCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                professionalCheck.setChecked(false);
            }
        });*/
    }

    private void GetBusinessDemand() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<BusinessDemand>> call = customer_interface.GetBusinessDemand("bearer " + Token);
        call.enqueue(new Callback<List<BusinessDemand>>() {
            @Override
            public void onResponse(Call<List<BusinessDemand>> call, Response<List<BusinessDemand>> response) {
                int status_code = response.code();
                Log.e("statuscode..", String.valueOf(status_code));
                progressDialog.dismiss();
                switch (status_code) {
                    case 200:
                        //
                        businessDemandList = response.body();
                        if (DemandID != 0) {
                            for (int i = 0; i < businessDemandList.size(); i++) {
                                businessDemandList.get(i).setChecked(businessDemandList.get(i).getBusinessDemandID() == DemandID);
                            }
                        }
                        businessDemandAdaper = new BusinessDemandCustomer(getActivity(), businessDemandList);
                        listBusinessDemand.setAdapter(businessDemandAdaper);
                        businessDemandAdaper.notifyDataSetChanged();
                        break;

                    case 404:
                        //  progressDialog.dismiss();
                        break;

                    case 500:
                        CustomToast.showToast(getContext(), "Server Error");
                        //  progressDialog.dismiss();
                        break;

                    case 401:
                        // progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(getContext());
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<BusinessDemand>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(getContext(), "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(getContext(), "Bad response from server.. Try again later ");
                }
            }
        });
    }

    /**
     * ---------Fetching Customer Details From Server
     * by using token And CustomerId----------------
     **/
    private void getCustomerDetails() {
        //ProgressDialog progressDialog = ShowProgressDialog.createProgressDialog(getActivity());
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<DealerRegister> call = customer_interface.getCustomerDetails("bearer " + Token, CustID);
        call.enqueue(new Callback<DealerRegister>() {
            @Override
            public void onResponse(Call<DealerRegister> call, Response<DealerRegister> response) {
                int statusCode = response.code();
                try {
                    Log.e("cust_response", response.body().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                switch (statusCode) {

                    case 200:
                        // progressDialog.dismiss();
                        if (!TextUtils.isEmpty(response.body().getFirmName())) {
                            edFirmName.setText("" + response.body().getFirmName());
                        } else {
                            edFirmName.setText("");
                        }

                        edMobileNo.setText("" + response.body().getMobileNumber());

                        if (dealerRegister != null && creation != null) {
                            if (!TextUtils.isEmpty(dealerRegister.getEmailID())
                                    && !dealerRegister.getEmailID().equals(creation.getEmailID()))
                                edEmailId.setText("" + dealerRegister.getEmailID());
                            else if(!TextUtils.isEmpty(creation.getEmailID()))
                                edEmailId.setText("" + creation.getEmailID());
                        } else {
                            if (creation != null && !TextUtils.isEmpty(creation.getEmailID())) {
                                edEmailId.setText("" + creation.getEmailID());
                            } else if (!TextUtils.isEmpty(response.body().getEmailID())) {
                                    edEmailId.setText("" + response.body().getEmailID());
                            }
                            else edEmailId.setText("");
                        }

                        pincode = response.body().getPincode();
                        edFirmName.setEnabled(true);
                        edMobileNo.setEnabled(false);
                        break;

                    case 404:
                        // progressDialog.dismiss();
                        break;

                    case 500:
                        // progressDialog.dismiss();
                        CustomToast.showToast(getActivity(), "Server Error");
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(getActivity());
                        break;

                }

            }

            @Override
            public void onFailure(Call<DealerRegister> call, Throwable t) {
                //progressDialog.dismiss();
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
     * --------Fetching Business type list(Retailer,Wholesaler, distributor...) from server-------*
     * ***/

    private void getBusinessListFromWebServer() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<BusinessType>> call = customer_interface.getBusinessTypes("bearer " + Token);
        call.enqueue(new Callback<List<BusinessType>>() {
            @Override
            public void onResponse(Call<List<BusinessType>> call, Response<List<BusinessType>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        businessTypeList = (businessTypeList != null && !businessTypeList.isEmpty()) ? businessTypeList : new ArrayList<>(response.body());
                        businessTypeAdapter = new BusinessTypeAdapter(getActivity(), businessTypeList, CustomerDetails.this);
                        listBusines.setAdapter(businessTypeAdapter);
                        businessTypeAdapter.notifyDataSetChanged();
                        break;
                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(getActivity(), "Server Error");
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(getActivity());
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<BusinessType>> call, Throwable t) {
                //progressDialog.dismiss();
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
     * ----------Initializing OnclickListeners on Business
     * Types(Retailer,wholesaler,Professional...etc)-----------**/
    @Override
    public void TypeOfBusinessClick(int pos, List<BusinessType> types) {
        if (isProfessional || types.get(pos).getNameOfBusiness().equals("Professionals And Services")) {
//            isProfessional = true;
            edSub.setHint(R.string.text_select_service);
            LinearBusinessDemand.setVisibility(View.GONE);
            selectSubIdList.clear();
            selectedProductsInDialog.clear();
            callChip(selectSubIdList);
        } else {
            for (int i = 0; i < types.size(); i++) {
                if (types.get(i).getNameOfBusiness().equals("Professionals And Services") && types.get(i).isChecked()) {
//                    isProfessional = false;
                    edSub.setHint(R.string.text_select_product);
                    LinearBusinessDemand.setVisibility(View.GONE);
                    selectSubIdList.clear();
                    selectedProductsInDialog.clear();
                    callChip(selectSubIdList);
//                    interestedCategory.setText("Interested " + types.get(i).getNameOfBusiness());
                } else {
                    /*if (types.get(i).isChecked()) {
                        interestedCategory.setText("Interested " + types.get(i).getNameOfBusiness());
                    }*/
//                    isProfessional = false;
                    LinearBusinessDemand.setVisibility(View.VISIBLE);
                    edSub.setHint(R.string.text_select_product);
                    //Log.e("Demandid...",String.valueOf(creation.getBusinessDemandID()));
                }
            }
        }
    }


    /***
     * ---Initializing Shared Preference ad retrieving dealer details and image---*
     * **/
    private void sharePreferencesMethod() {
        try {
//            Log.e("model...",creation.toString());
            creation = prefManager.getSavedObjectFromPreference(getContext(), "mwb-dealer_register", "customer", DealerRegister.class);
            if (creation != null) {
                Log.e("preference", "data is available" + creation);


                Log.e("Data", ".........." + creation.toString());

                if (!TextUtils.isEmpty(creation.getFirmName())) {
                    edFirmName.setText("" + creation.getFirmName());
                } else {
                    edFirmName.setText("");
                }

                if (!TextUtils.isEmpty(creation.getCustName())) {
                    edOwnerName.setText("" + creation.getCustName());
                } else {
                    edOwnerName.setText("");
                }

                if (!TextUtils.isEmpty(creation.getMobileNumber())) {
                    edMobileNo.setText("" + creation.getMobileNumber());
                } else {
                    edMobileNo.setText("");
                }

                if (!TextUtils.isEmpty(creation.getMobileNumber2())) {
                    edMobileNo1.setText("" + creation.getMobileNumber2());
                } else {
                    edMobileNo1.setText("");
                }

                if (!TextUtils.isEmpty(creation.getAdditionalPersonName())) {
                    edAdditionPerson.setText("" + creation.getAdditionalPersonName());
                } else {
                    edAdditionPerson.setText("");
                }

                if (!TextUtils.isEmpty(creation.getTelephoneNumber())) {
                    edTelephone.setText("" + creation.getTelephoneNumber());
                    edTelephone.setEnabled(false);
                } else {
                    edTelephone.setText("");
                }

                if(dealerRegister != null) {
                    if(!dealerRegister.getEmailID().equals(creation.getEmailID()))
                        edEmailId.setText("" + dealerRegister.getEmailID());
                    else edEmailId.setText("" + creation.getEmailID());
                } else {
                    if (!TextUtils.isEmpty(creation.getEmailID())) {
                        edEmailId.setText("" + creation.getEmailID());
                    } else {
                        edEmailId.setText("");
                    }
                }
                if (creation.getUserImageFile() != null) {
                    Glide.with(this).load(creation.getUserImageFile()).into(imgPhoto1);
                } else if (creation.isUserImage() == null) {
                    imgBase64 = "";
                } else if (creation.isUserImage().isEmpty()) {
                    imgBase64 = "";
                } else if (creation.isUserImage().equals("null")) {
                    imgBase64 = "";
                } else {
//                    Picasso.get()
//                            .load(creation.isUserImage())
//                            .into(imgPhoto1);
                    //decode base64 string to image
                    byte[] imageBytes = Base64.decode(creation.isUserImage(), Base64.DEFAULT);
//                    Log.e("BBBBBBytes",imageBytes.toString());
//                    //deflaterCompress(imageBytes);
//                    Log.e("bytes after compress",imageBytes.toString());

                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    imgPhoto1.setImageBitmap(decodedImage);

                    imgBase64 = DashboardActivity.creation.isUserImage();
                }
                //set business types
                if (creation.getBusinessTypeWithCust() != null) {
                    if (creation.getIsProfessional()) updateUIForProfessional();
                    else if(isUpdatedOnce){
                        if(!isProfessional)updateUIForBusinessOwner();
                        else updateUIForBusinessOwner();
                    }

                    Log.e("preference", "business Type Found");
                    businessTypeList = creation.getBusinessTypeWithCust();
                    Log.e("preference", businessTypeList.toString());

                    businessTypeAdapter = new BusinessTypeAdapter(getActivity(), businessTypeList, this);
                    listBusines.setAdapter(businessTypeAdapter);
                    businessTypeAdapter.notifyDataSetChanged();

                    for (int i = 0; i < businessTypeList.size(); i++) {
                        if (businessTypeList.get(i).getNameOfBusiness().equals("Professionals And Services")) {
                            if (businessTypeList.get(i).isChecked()) {
                                Log.e("Professional", "checked...........");
//                                isProfessional = true;
                                edSub.setHint(R.string.text_select_service);
                                LinearBusinessDemand.setVisibility(View.GONE);
//                                interestedCategory.setText("Interested " + businessTypeList.get(i).getNameOfBusiness());
                            }
                        } else {
                            /*if (businessTypeList.get(i).isChecked()) {
                                interestedCategory.setText("Interested " + businessTypeList.get(i).getNameOfBusiness());
                            }*/
                        }
                    }
                } else {
                    Log.e("preference", "Business Type Not found");
                    Log.e("preference", creation.getBusinessTypeWithCust().toString());
                }

                for (int i = 0; i < creation.getSubCategoryTypeWithCust().size(); i++) {
                    SubCategoryProduct childCategoryProduct = creation.getSubCategoryTypeWithCust().get(i);
                    selectSubIdList.add(new SubCategoryProduct(Integer.parseInt(prefManager.getCustId().get(CUST_ID)), childCategoryProduct.getCategoryProductId(), childCategoryProduct.getSubCategoryId(), childCategoryProduct.getSubCategoryName(), childCategoryProduct.isChecked()));
                }

                if (!creation.getBusinessDemandWithCust().isEmpty()) {
                    DemandID = creation.getBusinessDemandWithCust().get(0).getBusinessDemandID();
                    if (businessDemandList != null) {
//                        for (int i = 0; i < businessDemandList.size(); i++) {
//                            businessDemandList.get(i).setChecked(businessDemandList.get(i).getBusinessDemandID() == creation.getBusinessDemandWithCust().get(0).getBusinessDemandID());
//                        }
                        businessDemandAdaper = new BusinessDemandCustomer(getActivity(), businessDemandList);
                        listBusinessDemand.setAdapter(businessDemandAdaper);
                        businessDemandAdaper.notifyDataSetChanged();
                    }
                }

                if (creation.getBusinessTypeWithCust() != null && !creation.getBusinessTypeWithCust().isEmpty()) {
                    businessTypeList = creation.getBusinessTypeWithCust();
                    businessTypeAdapter = new BusinessTypeAdapter(getActivity(), businessTypeList, CustomerDetails.this);
                    listBusines.setAdapter(businessTypeAdapter);
                    businessTypeAdapter.notifyDataSetChanged();
                }

                //set product list
                if (creation.getSubCategoryTypeWithCust().size() > 0)
                    hsv.setVisibility(View.VISIBLE);
                else hsv.setVisibility(View.GONE);
                for (SubCategoryProduct category : creation.getSubCategoryTypeWithCust()) {

                    Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.chip_grp_item, chipGroupDemo, false);
                    mChip.findViewById(R.id.chips);
                    mChip.setText(category.getSubCategoryName());
                    Chip chip = mChip;
                    mChip.setOnCloseIconClickListener(view -> {
                        category.setChecked(false);
                        chipGroupDemo.removeView(chip);
                        if(selectedProductsInDialog.size() > 0)
                            selectedProductsInDialog.remove(category);
                        if(selectSubIdList.size() > 0){
                            for (SubCategoryProduct child : selectSubIdList) {
                                if (child.getSubCategoryId() == category.getSubCategoryId()) {
                                    selectSubIdList.remove(child);
                                    break;
                                }
                            }
                        }
                    }
                    );
                    chipGroupDemo.addView(mChip);
                    if(!selectedProductsInDialog.contains(category))
                        selectedProductsInDialog.add(category);
                }
                FinalSelectedSubList = creation.getSubCategoryTypeWithCust();
            } else {
                Log.e("preference", "data is empty");
                recyclerVieww.setVisibility(View.GONE);
                hsv.setVisibility(View.GONE);
                edFirmName.getText().clear();
                edChild.getText().clear();
                edOwnerName.getText().clear();
                edMobileNo.getText().clear();
                edAdditionPerson.getText().clear();
                edMobileNo1.getText().clear();
                edTelephone.getText().clear();
                edEmailId.getText().clear();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * ---------Activity result when user tries to upload image-------*
     ***/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            /* ------When User Select "Take Photo" from Dialog Box----------*,
             *----------This Request Will Be executed---------*
             */
            if (requestCode == REQUEST_TAKE_PHOTO) {
                try {
                    if (imgCount == 0) {
                        mPhotoFile1 = mCompressor.compressToFile(mPhotoFile1);
                        long len_img = mPhotoFile1.length();
                        if (len_img < image_size) {
                            Glide.with(this)
                                    .load(mPhotoFile1)
                                    .into(imgPhoto1);

//                        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
//                        Bitmap bm = ((android.graphics.drawable.BitmapDrawable) imgPhoto1.getDrawable()).getBitmap();
//                        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                        byte[] imageBytes = baos.toByteArray();
//                        imgBase64 = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);

                            String a = mPhotoFile1.getPath();
                            final Bitmap selectedImage = BitmapFactory.decodeFile(a);
                            imgBase64 = encodeImage(selectedImage);
                        } else {
                            Toast.makeText(getActivity(), "Please select the image within 2MB", Toast.LENGTH_LONG);
                        }
                        // imgPhoto1.setImageBitmap(mPhotoFile1);
                    }
//                    else
//                    {
//                        mPhotoFile2 = mCompressor.compressToFile(mPhotoFile2);
//                        //imgPhoto2.setImageBitmap(BitmapFactory.decodeFile(ImagePath1));
//                        Picasso.get()
//                                .load(mPhotoFile2)
//                                .into(imgPhoto2);
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            /*---When User Select "Choose From Gallery" from Dialog Box---*,
             **------This Request Will Be executed------**
             */

            else if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();
                try {
                    if (imgCount == 0) {
                        if (!TextUtils.isEmpty(getRealPathFromUri(selectedImage))) {
                            mPhotoFile1 = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                            // imgPhoto1.setImageBitmap(mPhotoFile1);
                            long len_img = mPhotoFile1.length();
                            if (len_img < image_size) {
                                Glide.with(this)
                                        .load(mPhotoFile1)
                                        .into(imgPhoto1);

                                String a = mPhotoFile1.getPath();
                                final Bitmap selectedImagee = BitmapFactory.decodeFile(a);
                                imgBase64 = encodeImage(selectedImagee);
                            } else {
                                Toast.makeText(getActivity(), "Please select the image within 2MB", Toast.LENGTH_LONG);
                            }
                        } else {
                            CustomToast.showToast(getActivity(), "Please select the image from Gallery");
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 5000);
                        }
//                        imgPhoto1.buildDrawingCache();
//                        Bitmap bitmap = imgPhoto1.getDrawingCache();
//                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
//                        byte[] image=stream.toByteArray();
//                        System.out.println("byte array:"+image);
//                        imgBase64 = Base64.encodeToString(image, 0);
//                        System.out.println("string4444:"+imgBase64);
                    }
//                    else{
//
//                        mPhotoFile2= mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
//                        //imgPhoto2.setImageBitmap(BitmapFactory.decodeFile(ImagePath1));
//                        Picasso.get()
//                                .load(mPhotoFile2)
//                                .into(imgPhoto2);
//                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    /**
     * =====================UnUsed Method============*
     */
    private byte[] deflaterCompress(byte[] toCompress) {
        try {
            ByteArrayOutputStream compressedStream = new ByteArrayOutputStream();

            DeflaterOutputStream inflater = new DeflaterOutputStream(compressedStream);
            inflater.write(toCompress, 0, toCompress.length);
            inflater.close();

            return compressedStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ----------------Method used to encode Image------------*
     */
    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    /**
     * ==========UnUsed Method===================*
     */
    private void UploadProfilePhoto() {
        mdialogPhoto = new Dialog(getContext());
        mdialogPhoto.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialogPhoto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialogPhoto.setContentView(R.layout.dialogue_profile_photo);
        mdialogPhoto.setCancelable(false);
        mdialogPhoto.show();
        imgPhoto1 = mdialogPhoto.findViewById(R.id.imag1);
        //imgPhoto2 = mdialogPhoto.findViewById(R.id.imag2);
        btnPhoto1 = mdialogPhoto.findViewById(R.id.btnPhoto1);
        btnPhoto = mdialogPhoto.findViewById(R.id.btnPhoto);
        imgPhoto1.setOnClickListener(CustomerDetails.this);
        //imgPhoto2.setOnClickListener(UpdateCustomer.this);
        btnPhoto.setOnClickListener(CustomerDetails.this);
        btnPhoto1.setOnClickListener(CustomerDetails.this);
    }

    /**
     * ---------------Method to Get Real File Path From URI----------------*
     **/
    public String getRealPathFromUri(Uri contentUri) {
        String path = "";
        if (getActivity().getContentResolver() != null) {
            Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;

    }

    /**
     * ---------------Initializing OnCLick Listeners-------------*
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /* ---method that is Called When User Clicks On "Next" Btn----*/
            case R.id.btnNext:

                if (!ValidateOwnerName() | !ValidateBusiness() | !ValidateProducts() | !ValidateMobileNumber() | !ValidateFirmName()) {
                    return;
                } else {
                    selectSubIdList = GetUpdatedSubList();
                    if (selectSubIdList.size() == 0 | selectSubIdList.isEmpty()) {
                        CustomToast.showToast(getActivity(), "Product must not be empty");
                    } else if (!isProfessional && businessDemandAdaper.getResultList().isEmpty()) {
                        CustomToast.showToast(getActivity(), "Please select expected business value");
                    } else {
                        if (!TextUtils.isEmpty(edEmailId.getText().toString())) {
                            if (!ValidateEmail()) {
                                return;
                            } else {
                                Log.e("condition...", String.valueOf(isProfessional));
                                if (isProfessional) {
                                    businessTypeList.add(new BusinessType(6,6,Integer.parseInt(prefManager.getCustId().get(CUST_ID)),"Professionals And Services",true));
                                    dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                            edFirmName.getText().toString(), prefManager.getCustId().get(CUST_PASSWORD),
                                            prefManager.getCustId().get(CUST_TYPE), selectSubIdList, businessTypeList,
                                            edOwnerName.getText().toString(), edEmailId.getText().toString(), edMobileNo.getText().toString(),
                                            edAdditionPerson.getText().toString(), edMobileNo1.getText().toString(), edTelephone.getText().toString()
                                            , "", checkbox1.isChecked(), isProfessional, mPhotoFile1, 0); //""
                                    dealerRegister.setPincode(pincode);

                                 /*   dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                            edFirmName.getText().toString(), prefManager.getCustId().get(CUST_PASSWORD),
                                            prefManager.getCustId().get(CUST_TYPE), selectSubIdList, businessTypeAdapter.getDefaultList(),
                                            edOwnerName.getText().toString(), edEmailId.getText().toString(), edMobileNo.getText().toString(),
                                            edAdditionPerson.getText().toString(), edMobileNo1.getText().toString(), edTelephone.getText().toString()
                                            , "", checkbox1.isChecked(), isProfessional, mPhotoFile1, 0); //""
                                    dealerRegister.setPincode(pincode);*/
                                    prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                                    isUpdatedOnce = true;
                                    callToBillFragment.callingBillingFragment(pos);
                                } else {
                                    Log.e("Demandid...", businessDemandAdaper.getResultList().toString());
                                    if (businessDemandAdaper.getResultList() != null) {
                                        if (!businessTypeAdapter.getResultList().toString().equals("[]")) {
                                            DemandID = businessDemandAdaper.GetselectedBusinessDemand();
                                            Log.e("Demandid...", String.valueOf(DemandID));
                                            dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                                    edFirmName.getText().toString(), prefManager.getCustId().get(CUST_PASSWORD),
                                                    prefManager.getCustId().get(CUST_TYPE), selectSubIdList, businessTypeAdapter.getResultList(),
                                                    edOwnerName.getText().toString(), edEmailId.getText().toString(), edMobileNo.getText().toString(),
                                                    edAdditionPerson.getText().toString(), edMobileNo1.getText().toString(), edTelephone.getText().toString()
                                                    , "", checkbox1.isChecked(), isProfessional, mPhotoFile1, DemandID, businessDemandAdaper.getResultList()); //""
                                            dealerRegister.setPincode(pincode);
                                            prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                                            isUpdatedOnce = true;
                                            callToBillFragment.callingBillingFragment(pos);

                                        } else {
                                            CustomToast.showToast(getContext(), "Please select Business Demand");
                                        }

                                    } else {
                                        CustomToast.showToast(getContext(), "Please select Business Demand");
                                    }
                                }
                            }
                        } else {
//                            if (businessDemandAdaper.GetselectedBusinessDemand() != 0) {
//                                DemandID = businessDemandAdaper.GetselectedBusinessDemand();
                            Log.e("condition...", String.valueOf(isProfessional));
                            if (isProfessional) {
                                businessTypeList.add(new BusinessType(6,6,Integer.parseInt(prefManager.getCustId().get(CUST_ID)), "Professionals And Services", true));
                                dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                        edFirmName.getText().toString(), prefManager.getCustId().get(CUST_PASSWORD),
                                        prefManager.getCustId().get(CUST_TYPE), selectSubIdList, businessTypeList,
                                        edOwnerName.getText().toString(), edEmailId.getText().toString(), edMobileNo.getText().toString(),
                                        edAdditionPerson.getText().toString(), edMobileNo1.getText().toString(), edTelephone.getText().toString(), "", checkbox1.isChecked(), isProfessional, mPhotoFile1, 0); //imgBase64
                                dealerRegister.setPincode(pincode);
                                prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                                Log.e("Model", dealerRegister.toString());
                                isUpdatedOnce = true;
                                callToBillFragment.callingBillingFragment(pos);
                            } else {
                                Log.e("Demandid...", businessDemandAdaper.getResultList().toString());
                                if (businessDemandAdaper.getResultList() != null) {
                                    if (!businessDemandAdaper.getResultList().toString().equals("[]")) {
                                        DemandID = businessDemandAdaper.GetselectedBusinessDemand();
                                        Log.e("Demandid...", String.valueOf(DemandID));
                                        dealerRegister = new DealerRegister(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                                                edFirmName.getText().toString(), prefManager.getCustId().get(CUST_PASSWORD),
                                                prefManager.getCustId().get(CUST_TYPE), selectSubIdList, businessTypeAdapter.getResultList(),
                                                edOwnerName.getText().toString(), edEmailId.getText().toString(), edMobileNo.getText().toString(),
                                                edAdditionPerson.getText().toString(), edMobileNo1.getText().toString(), edTelephone.getText().toString(), "", checkbox1.isChecked(), isProfessional, mPhotoFile1, DemandID, businessDemandAdaper.getResultList()); //imgBase64
                                        dealerRegister.setPincode(pincode);
                                        prefManager.saveObjectToSharedPreference("customer", dealerRegister);
                                        Log.e("Model", dealerRegister.toString());
                                        isUpdatedOnce = true;
                                        callToBillFragment.callingBillingFragment(pos);
                                    } else {
                                        CustomToast.showToast(getContext(), "Please select Business Demand");
                                    }
                                } else {
                                    CustomToast.showToast(getContext(), "Please select Business Demand");
                                }

                            }

//                            }
                        }
                    }
                }
                break;


            case R.id.edMCategoryName:
                /* ---method that is Called When User Clicks On "Select Product" or "Select Service" EditText ----*/
                getSubcategoryDialogue();
                break;

            case R.id.bownerCk:
                if (isBusinessTypeSelected == false) {
                    selectSubIdList.clear();
                    selectedProductsInDialog.clear();
                    callChip(selectSubIdList);
                    for (int i = 0; i < businessDemandList.size(); i++) {
                        businessDemandList.get(i).setChecked(false);
                    }
                    businessDemandAdaper = new BusinessDemandCustomer(getActivity(), businessDemandList);
                    listBusinessDemand.setAdapter(businessDemandAdaper);
                    updateUIForBusinessOwner();
                } else {
                    if (isProfessional == true) {
                        DialogUtilsKt.confirm(requireContext(), getString(R.string.do_you_really_want_to_switch), getString(R.string.yes), getString(R.string.no), v1 -> {
                            selectSubIdList.clear();
                            selectedProductsInDialog.clear();
                            callChip(selectSubIdList);
                            for (int i = 0; i < businessDemandList.size(); i++) {
                                businessDemandList.get(i).setChecked(false);
                            }
                            businessDemandAdaper = new BusinessDemandCustomer(getActivity(), businessDemandList);
                            listBusinessDemand.setAdapter(businessDemandAdaper);
                            updateUIForBusinessOwner();
                        }, null);
                    }
                }
                break;

            case R.id.professionalCk:
                if(isBusinessTypeSelected==false){
                    selectSubIdList.clear();
                    selectedProductsInDialog.clear();
                    callChip(selectSubIdList);
                    for (int i = 0; i < businessDemandList.size(); i++) {
                        businessDemandList.get(0).setChecked(false);
                    }
                    businessDemandAdaper = new BusinessDemandCustomer(getActivity(), businessDemandList);
                    listBusinessDemand.setAdapter(businessDemandAdaper);
                    updateUIForProfessional();
                }else {
                    if (isProfessional == false) {
                        DialogUtilsKt.confirm(requireContext(), getString(R.string.do_you_really_want_to_switch), getString(R.string.yes), getString(R.string.no), v1 -> {
                            selectSubIdList.clear();
                            selectedProductsInDialog.clear();
                            callChip(selectSubIdList);
                            for (int i = 0; i < businessDemandList.size(); i++) {
                                businessDemandList.get(0).setChecked(false);
                            }
                            businessDemandAdaper = new BusinessDemandCustomer(getActivity(), businessDemandList);
                            listBusinessDemand.setAdapter(businessDemandAdaper);
                            updateUIForProfessional();
                        }, null);
                    }
                }

                break;

            /* ---the Below method that is Called When User Clicks On "submit" which present
             *in DialogBox that is pop upped when user tries to select
             * SubCategory(Product or service)----*/

            case R.id.sub_submit:
                if(chipGroupSearchDialog == null) chipGroupSearchDialog = mDialogSubCategory.findViewById(R.id.chipgroup);
                if (chipGroupSearchDialog.getChildCount() == 0) {
                    Toast.makeText(getContext(), "Please select product", Toast.LENGTH_SHORT).show();
                    break;
                }
                /*if (subCategoryAdapter.getResultList().size() > 0) hsv.setVisibility(View.VISIBLE);
                else hsv.setVisibility(View.GONE);*/
                hsv.setVisibility(View.VISIBLE);
                for (int i = 0; i < selectedProductsInDialog.size(); i++) {

                    SubCategoryProduct childCategoryProduct = selectedProductsInDialog.get(i);
                    selectSubIdList.add(new SubCategoryProduct(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),
                            childCategoryProduct.getCategoryProductId(),
                            childCategoryProduct.getSubCategoryId(),
                            childCategoryProduct.getSubCategoryName(), true));
                }
                LinkedHashSet<SubCategoryProduct> listRepatedArryList = new LinkedHashSet<SubCategoryProduct>();
                listRepatedArryList.addAll(selectSubIdList);
                selectSubIdList.clear();
                selectSubIdList.addAll(listRepatedArryList);

                mDialogSubCategory.dismiss();
                if (selectSubIdList.size() != 0 | !selectSubIdList.isEmpty()) {
                    callChip(selectSubIdList);
                    getAlert();
                }
                Log.e("finaal....", selectSubIdList.toString());
                break;

            /*
             * Not Executable Case
             * */
            case R.id.btnSubmit:
                sendPhotoTOServer();

                break;

            /*
             * method to select An image For Profile will
             * be called when we click on "imag1" */
            case R.id.imag1:
                imgCount = 0;
                selectImage();
                break;
            /* id "imag2" is not present in this Xml */
            case R.id.imag2:
                imgCount = 1;
                selectImage();
                break;

            /* id "btnPhoto1" is not present in this Xml */
            case R.id.btnPhoto1:
                mdialogPhoto.dismiss();
                break;
        }
    }

    private void updateUIForProfessional() {
        isBusinessTypeSelected = true;
        isProfessional = true;
        businessTypeAdapter.setDefaultList();
        businessTypeAdapter.notifyDataSetChanged();
        professionalCheck.setSelected(true);
        businessOwnerCheck.setSelected(false);
        businessOwnerCheck.setBackgroundResource(0);
        professionalCheck.setBackgroundResource(R.drawable.shape_rectangle_red);
        imgBusinessOwner.setImageResource(R.drawable.ic_business_owner_unselected);
        imgProfessional.setImageResource(R.drawable.ic_professional_selected);
        txtProfessional.setTextColor(getResources().getColor(R.color.white));
        txtBusinessOwner.setTextColor(getResources().getColor(R.color.black));
        layoutCategories.setVisibility(View.GONE);
        LinearBusinessDemand.setVisibility(View.GONE);
        txtProfessional.setTypeface(null, Typeface.BOLD);
        txtBusinessOwner.setTypeface(null, Typeface.NORMAL);
        edSub.setHint(R.string.text_select_service);
    }

    private void updateUIForBusinessOwner() {
        isBusinessTypeSelected = true;
        isProfessional = false;
        businessOwnerCheck.setSelected(true);
        professionalCheck.setSelected(true);
        professionalCheck.setBackgroundResource(0);
        businessOwnerCheck.setBackgroundResource(R.drawable.shape_rectangle_red);
        imgProfessional.setImageResource(R.drawable.ic_professional_unselected);
        imgBusinessOwner.setImageResource(R.drawable.ic_business_owner_selected);
        txtBusinessOwner.setTextColor(getResources().getColor(R.color.white));
        txtProfessional.setTextColor(getResources().getColor(R.color.black));
        layoutCategories.setVisibility(View.VISIBLE);
        LinearBusinessDemand.setVisibility(View.VISIBLE);
        txtBusinessOwner.setTypeface(null, Typeface.BOLD);
        txtProfessional.setTypeface(null, Typeface.NORMAL);
        edSub.setHint(R.string.text_select_product);
    }

    /**
     * Alert Dialog Box to select
     * image from "Camera","Gallery"
     **/
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Choose your picture");

        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                dispatchTakePictureIntent();
            } else if (options[item].equals("Choose from Gallery")) {
                dispatchGalleryIntent();
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * ----Opening the Camera to
     * <p>
     * Take Picture--------
     **/
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
                            "com.mwbtech.dealer_register" + ".provider",
                            photoFile);
                    mPhotoFile1 = photoFile;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    ProjectUtilsKt.checkCameraPermission(requireActivity(), (status -> {
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                        return null;
                    }));
                }
            } else {
                if (photoFile1 != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
                            "com.mwbtech.dealer_register" + ".provider",
                            photoFile1);
                    mPhotoFile2 = photoFile1;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    ProjectUtilsKt.checkCameraPermission(requireActivity(), (status -> {
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                        return null;
                    }));
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
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }
    /**
     * Get real file path from URI
     */
//    public String getRealPathFromUri(Uri contentUri) {
//        String path = "";
//        if (getContentResolver() != null) {
//            Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
//            if (cursor != null) {
//                cursor.moveToFirst();
//                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//                path = cursor.getString(idx);
//                cursor.close();
//            }
//        }
//        return path;
//
//    }
//
//    public void clearApplicationData() {
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File appDir = new File(storageDir.getParent());
//        Log.e("cache","path........."+appDir);
//        if(appDir.exists()){
//            String[] children = appDir.list();
//            for(String s : children){
//                if(!s.equals("files")){
//                    deleteDir(new File(appDir, s));
//                    Log.e("TAG", "**************** File /data/data/APP_PACKAGE/" + s +" DELETED *******************");
//                }
//            }
//        }
//    }

    /**
     * ============= NotExecutable Method============*
     */
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

    /**
     * Select image from gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }

    /*
     *======Not Executable Method===========*
     * */
    private void sendPhotoTOServer() {

        builder1 = new AlertDialog.Builder(getContext()).setMessage(R.string.photo);
        builder1.setPositiveButton(R.string.ok, (dialog, id) -> {

            mdialogPhoto = new Dialog(getContext());
            mdialogPhoto.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mdialogPhoto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mdialogPhoto.setContentView(R.layout.dialog_photo);
            mdialogPhoto.setCancelable(false);
            mdialogPhoto.show();
            imgPhoto1 = mdialogPhoto.findViewById(R.id.imag1);
            //imgPhoto2 = mdialogPhoto.findViewById(R.id.imag2);
            btnPhoto1 = mdialogPhoto.findViewById(R.id.btnPhoto1);
            btnPhoto = mdialogPhoto.findViewById(R.id.btnPhoto);
            imgPhoto1.setOnClickListener(CustomerDetails.this);
            imgPhoto2.setOnClickListener(CustomerDetails.this);
            btnPhoto.setOnClickListener(CustomerDetails.this);
            btnPhoto1.setOnClickListener(CustomerDetails.this);

        }).setNegativeButton(R.string.cancel, (dialog, id) -> dialog.dismiss());
        AlertDialog alertDialog = builder1.create();
        alertDialog.show();
    }

    /***
     * -------Fetching  Products list or Service List------*
     ***/
    private List<SubCategoryProduct> GetUpdatedSubList() {
        List<SubCategoryProduct> adapters = new ArrayList<>();
        selectSubIdList.clear();
        if (FinalSelectedSubList.size() != 0) {
            for (SubCategoryProduct categoryProduct : FinalSelectedSubList) {
                if (categoryProduct.isChecked()) {
                    adapters.add(new SubCategoryProduct(categoryProduct.getCustID(),
                            categoryProduct.getCategoryProductId(), categoryProduct.getSubCategoryId(), categoryProduct.getSubCategoryName(), true));
                }
            }
        }
        return adapters;
    }

    /***
     * -----Asking User To whether he likes to Add more Products or Service
     * from Products list or Service List respectively------*
     ***/
    private void getAlert() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setCancelable(false);
        if (isProfessional == true) {
            builder1.setMessage("Would you like to add more services?");
        } else {
            builder1.setMessage("Would you like to add more categories?");
        }

        builder1.setCancelable(true);

        builder1.setPositiveButton("Yes", (dialog, id) -> getSubcategoryDialogue());

        builder1.setNegativeButton("No", (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.setCanceledOnTouchOutside(false);
        alert11.show();
    }

    /***This below Method Is Called To Show The Selected Products or Services
     * In Form Of Chips*
     ***/
    private void callChip(List<SubCategoryProduct> selectSubIdList) {
        if (chipGroupDemo.getChildCount() > 0) {
            chipGroupDemo.removeAllViews();
        }

        List<SubCategoryProduct> withoutDuplicate = new ArrayList<>();
        int i = 0;

        for (SubCategoryProduct category : selectSubIdList) {
            if (i == 0) {
                withoutDuplicate.add(category);
            } else {
                int k = 0;
                for (SubCategoryProduct cat : withoutDuplicate) {
                    if (cat.getSubCategoryId() == category.getSubCategoryId()) {
                        k = 1;
                        break;
                    }
                }
                if (k != 1) {
                    withoutDuplicate.add(category);
                }
            }
            i++;
        }

        for (SubCategoryProduct category : withoutDuplicate) {
            Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.chip_grp_item, chipGroupDemo, false);
            mChip.findViewById(R.id.chips);
            mChip.setText("" + category.getSubCategoryName());
            Chip finalChip = mChip;
            mChip.setOnCloseIconClickListener(view -> {
                category.setChecked(false);
                chipGroupDemo.removeView(finalChip);
                if(selectedProductsInDialog.size() > 0){
                    for (SubCategoryProduct child : selectedProductsInDialog) {
                        if (child.getSubCategoryId() == category.getSubCategoryId()) {
                            selectedProductsInDialog.remove(child);
                            break;
                        }
                    }
                }

                if(selectSubIdList.size() > 0){
                    for (SubCategoryProduct child : selectSubIdList) {
                        if (child.getSubCategoryId() == category.getSubCategoryId()){
                            selectSubIdList.remove(child);
                            break;
                        }
                    }
                }

            });
            chipGroupDemo.addView(mChip);
        }
        FinalSelectedSubList = withoutDuplicate;
    }

    /***
     * This below Method Is Called When User press the Search Button
     * To Get List OF Products Or Services From Server
     ***/

    private void getSearchSubCategoryMethodToServer(String searchChildCategory, boolean showLoader) {
        edMain.getText().clear();
        ProgressDialog progressDialog = ShowProgressDialog.createProgressDialog(getActivity());
        if (!showLoader) progressDialog.dismiss();
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Log.e("is professionals", "....." + isProfessional);
        Call<List<SubCategoryProduct>> call = customer_interface.getSubCategoryChildCategoryList("bearer " + Token, searchChildCategory, CustID, isProfessional);
        call.enqueue(new Callback<List<SubCategoryProduct>>() {
            @Override
            public void onResponse(Call<List<SubCategoryProduct>> call, Response<List<SubCategoryProduct>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        subCategoryProducts = new ArrayList<>(response.body());
                        if (subCategoryProducts.isEmpty() | subCategoryProducts.size() == 0) {
                            CustomToast.showToast(getActivity(), "Not found");
                            /*if(subCategoryAdapter.getResultList().size() == 0) */
                            btnSub.setVisibility(View.VISIBLE);
                            subCategoryAdapter = new SubCategoryAdapter(getContext(), subCategoryProducts, CustomerDetails.this);
                            recyclerViewMother.setAdapter(subCategoryAdapter);
                            subCategoryAdapter.notifyDataSetChanged();
                            checkBoxSubCategory.setChecked(false);
                            checkBoxSubCategory.setVisibility(View.GONE);
                            layoutSelectAll.setVisibility(View.GONE);
                        } else {
                            subCategoryAdapter = new SubCategoryAdapter(getContext(), subCategoryProducts, CustomerDetails.this);
                            recyclerViewMother.setAdapter(subCategoryAdapter);
                            subCategoryAdapter.notifyDataSetChanged();
                            edMain.setEnabled(true);
                            checkBoxSubCategory.setChecked(false);
                            checkBoxSubCategory.setVisibility(View.VISIBLE);
                            layoutSelectAll.setVisibility(View.VISIBLE);
                            MyListAdapter adapter = new MyListAdapter(getActivity(), selectSubIdList);
                            recyclerVieww.setAdapter(adapter);
                            btnSub.setVisibility(View.VISIBLE);
                        }
                        break;

                    case 404:
                        progressDialog.dismiss();
                        btnSub.setVisibility(View.VISIBLE);
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
            public void onFailure(Call<List<SubCategoryProduct>> call, Throwable t) {
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
     * Adding OnItemSelected() Listener To the Selected Products or Services*
     ***/
    @Override
    public void onItemSelected(SubCategoryProduct subProduct) {
        chipScroll.setVisibility(View.VISIBLE);
//        chipGroupDemo.setVisibility(View.VISIBLE);

        ChipGroup chipGroupSearchDialog = mDialogSubCategory.findViewById(R.id.chipgroup);
        Chip mChipp = (Chip) this.getLayoutInflater().inflate(R.layout.chip_grp_fragment, chipGroupSearchDialog, false);
        mChipp.findViewById(R.id.chips);

        if (subProduct.isChecked()) {
            mChipp.setText(subProduct.getSubCategoryName());

            Chip finalMChipp = mChipp;
            mChipp.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subProduct.setChecked(false);
                    chipGroupSearchDialog.removeView(finalMChipp);
                    if(selectedProductsInDialog.size() > 0){
                        for (SubCategoryProduct child : selectedProductsInDialog) {
                            if (child.getSubCategoryId() == subProduct.getSubCategoryId()) {
                                selectedProductsInDialog.remove(child);
                                break;
                            }
                        }
                    }

                    if(selectSubIdList.size() > 0){
                        for (SubCategoryProduct child : selectSubIdList) {
                            if (child.getSubCategoryId() == subProduct.getSubCategoryId()){
                                selectSubIdList.remove(child);
                                break;
                            }
                        }
                    }
                }
            });
            chipGroupSearchDialog.addView(mChipp);
            if(!selectedProductsInDialog.contains(subProduct))
                selectedProductsInDialog.add(subProduct);

        } else {
            for (int i = 0; i < chipGroupSearchDialog.getChildCount(); i++) {
                mChipp = (Chip) chipGroupSearchDialog.getChildAt(i);

                if (mChipp.getText().toString().equals(subProduct.getSubCategoryName())) {
                    if (!subProduct.isChecked()) {
                        subProduct.setChecked(false);
                        chipGroupSearchDialog.removeView(mChipp);

                        if(selectedProductsInDialog.size() > 0){
                            for (SubCategoryProduct child : selectedProductsInDialog) {
                                if (child.getSubCategoryId() == subProduct.getSubCategoryId()) {
                                    selectedProductsInDialog.remove(child);
                                    break;
                                }
                            }
                        }

                        if(selectSubIdList.size() > 0){
                            for (SubCategoryProduct child : selectSubIdList) {
                                if (child.getSubCategoryId() == subProduct.getSubCategoryId()){
                                    selectSubIdList.remove(child);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * -----------------Open A Dialog box To select Subcategory( product ot service)-----------*
     */
    public void getSubcategoryDialogue() {
        if (!isProfessional && businessTypeAdapter.getTrueResultList().size() == 0) {
            CustomToast.showToast(getActivity(), "Please select categories");
        } else {

            mDialogSubCategory = new Dialog(getActivity());
            mDialogSubCategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialogSubCategory.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialogSubCategory.setContentView(R.layout.sub_product_list);
            mDialogSubCategory.setCancelable(true);
            edProduct = mDialogSubCategory.findViewById(R.id.edState);
            if (isProfessional) {
                edProduct.setHint(R.string.text_search_service);
            } else {
                edProduct.setHint(R.string.text_search_product);
            }
            btnSearch = mDialogSubCategory.findViewById(R.id.btnSearch);
            Clear_txt_btn = mDialogSubCategory.findViewById(R.id.clear_txt_Prise);
            checkBoxSubCategory = mDialogSubCategory.findViewById(R.id.checkboxSubCategory);
            layoutSelectAll = mDialogSubCategory.findViewById(R.id.layout_select_all);
            recyclerViewMother = mDialogSubCategory.findViewById(R.id.recyclerCategory);
            recyclerViewMother.setHasFixedSize(true);
            recyclerViewMother.setLayoutManager(new LinearLayoutManager(getActivity()));
            imageView = mDialogSubCategory.findViewById(R.id.cancel_category);
            btnSub = mDialogSubCategory.findViewById(R.id.sub_submit);
            chipScroll = mDialogSubCategory.findViewById(R.id.chipScrollView);
            mDialogSubCategory.show();
            subCategoryAdapter = new SubCategoryAdapter(getContext(), subCategoryProducts, CustomerDetails.this);
            btnSub.setOnClickListener(this);
            imageView.setOnClickListener(v -> mDialogSubCategory.dismiss());
            Clear_txt_btn.setOnClickListener(view -> {
                if (!edProduct.getText().toString().isEmpty()) {
                    edProduct.getText().clear();
                    Clear_txt_btn.setVisibility(View.GONE);
                }
            });
            /*
             * Selecting All Products or Services When Below CheckBox is Selected*
             * */
            checkBoxSubCategory.setOnClickListener(view -> {
                try {
                    for (SubCategoryProduct subCategoryProduct : subCategoryProducts) {
                        subCategoryProduct.setChecked(false);
                        onItemSelected(subCategoryProduct);
                    }
                    if (checkBoxSubCategory.isChecked()) {

                        for (SubCategoryProduct subCategoryProduct : subCategoryProducts) {
//                                if (subCategoryProduct.isChecked()) {
//                                    subCategoryProduct.setChecked(false);
//                                    onItemSelected(subCategoryProduct);
//                                }

                            subCategoryProduct.setChecked(true);
                            onItemSelected(subCategoryProduct);
                        }
                    } else {
                        for (SubCategoryProduct subCategoryProduct : subCategoryProducts) {
                            subCategoryProduct.setChecked(false);
                            onItemSelected(subCategoryProduct);
                        }
                    }

                    subCategoryAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            /* **
             *fetching All kind of Services or Products when user press Search Button in dialog Button*
             ** */
            btnSearch.setOnClickListener(view -> {
                if (edProduct.getText().toString().isEmpty()) {
                    CustomToast.showToast(getActivity(), "Enter product/service name");
                    btnSub.setVisibility(View.VISIBLE);
                } else {
                    getSearchSubCategoryMethodToServer(edProduct.getText().toString().trim(), true);
                    btnSub.setVisibility(View.VISIBLE);
                }
            });
            edProduct.addTextChangedListener(new TextWatcher() {
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
                    String searchedFor = edProduct.getText().toString().trim();
                    if (searchedFor.length() < 3) return;
                    if (edProduct.getText().toString().isEmpty()) {
                        CustomToast.showToast(getActivity(), "Enter product/service name");
                    } else {
                        btnSub.setVisibility(View.VISIBLE);
                        if (ConnectivityReceiver.isConnected()) {
                            getSearchSubCategoryMethodToServer(searchedFor, searchedFor.length() <= 3);
                        } else {
                            Toast.makeText(getActivity(), "Not connected to internet", Toast.LENGTH_SHORT).show();
                            //showConnectionError();
                        }
                    }
                }
            });
            edProduct.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (edProduct.getText().toString().isEmpty()) {
                        CustomToast.showToast(getActivity(), "Enter product/service name");
                        btnSub.setVisibility(View.VISIBLE);
                    } else {
                        getSearchSubCategoryMethodToServer(edProduct.getText().toString().trim(), true);
                        btnSub.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
                return false;
            });
            /*edProduct.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        subCategoryAdapter.getFilter().filter(s);
                        if (s.length() <= 0) {

                            Clear_txt_btn.setVisibility(View.GONE);
                        } else {
                            getSearchSubCategoryMethodToServer(edProduct.getText().toString().trim());
                            btnSub.setVisibility(View.VISIBLE);
                            Clear_txt_btn.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        subCategoryAdapter.getFilter().filter(s);
                        if (s.length() <= 0) {

                            Clear_txt_btn.setVisibility(View.GONE);
                        } else {
                            getSearchSubCategoryMethodToServer(edProduct.getText().toString().trim());
                            btnSub.setVisibility(View.VISIBLE);
                            Clear_txt_btn.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });*/
        }
        Log.e("is professionals", "....." + isProfessional);

    }


    /***
     * Validating The Username Entered By User*
     ***/
    private boolean ValidateOwnerName() {
        String ownName = edOwnerName.getText().toString();

        if (ownName.isEmpty()) {
            edOwnerName.setError("Please enter owner name");
            CustomToast.showToast(getContext(), "Please enter owner name");
            return false;
        } else if (ownName.startsWith(" ")) {
            edOwnerName.setError("Please enter proper ownername");
            CustomToast.showToast(getContext(), "Please enter proper ownername");
            return false;
        } else {
            return true;
           /* if (!ownName.matches(regFname)) {
                edOwnerName.setError("please enter proper owner name");
                return false;
            } else {
                return true;
            }*/
        }
    }

    /***
     * Validating The FirmName Entered By User*
     ***/

    private boolean ValidateFirmName() {
        String ownName = edFirmName.getText().toString();

        if (ownName.isEmpty()) {
            edFirmName.setError("Please enter Firmname");
            CustomToast.showToast(getContext(), "Please enter Firmname");
            return false;
        } else if (ownName.startsWith(" ")) {
            edFirmName.setError("Please enter proper Firmname");
            CustomToast.showToast(getContext(), "Please enter proper Firmname");
            return false;
        } else {
            return true;
        }
    }

    /***
     * Validating The Email Entered By User*
     ***/

    private boolean ValidateEmail() {
        String email = edEmailId.getText().toString();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.charAt(email.indexOf("@") - 1) == '.') {
            edEmailId.setError("Please enter valid email");
            CustomToast.showToast(getContext(), "Please enter valid email");
            return false;
        } else {
            return true;
        }
    }

    /***
     * Validating The ProductList or serviceList Selected By User*
     ***/


    private boolean ValidateProducts() {
        if (selectSubIdList.isEmpty() | selectSubIdList.size() == 0) {
            CustomToast.showToast(getActivity(), isProfessional ? "Please select services" : "Please select products");
            return false;
        } else {
            return true;
        }
    }

    /***
     * Validating The BusinessType Selected By User*
     ***/

    private boolean ValidateBusiness() {
        if (!isProfessional && businessTypeAdapter.getTrueResultList().size() == 0 | businessTypeAdapter.getTrueResultList().isEmpty()) {
            CustomToast.showToast(getActivity(), "Please select business type");
            return false;
        } else {
            return true;
        }
    }

    /***
     * Validating The Mobile Number Entered By User*
     ***/

    private boolean ValidateMobileNumber() {
        String NumberInput = edMobileNo.getText().toString();

        if (!PHONE_PATTERN.matcher(NumberInput).matches()) {
            edMobileNo.setError("please enter valid mobile number");
            return false;
        } else if (NumberInput.isEmpty()) {
            edMobileNo.setError("Please enter valid mobile number");
            return false;
        } else {
            if (!NumberInput.matches(regNumber)) {
                edMobileNo.setError("please enter valid mobile number");
                return false;
            } else {
                return true;
            }
        }
    }

    public interface CallToBillFragment {
        void callingBillingFragment(int pos);
    }
}
