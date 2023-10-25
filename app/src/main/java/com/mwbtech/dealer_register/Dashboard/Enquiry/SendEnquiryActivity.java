package com.mwbtech.dealer_register.Dashboard.Enquiry;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.BusinessTypeSpinnerAdapter;
import com.mwbtech.dealer_register.Adapter.DealerListAdapter;
import com.mwbtech.dealer_register.Adapter.ShowDealerRegisterAdapter;
import com.mwbtech.dealer_register.Dashboard.ChatUtil.CustomLinearLayoutManager;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.PojoClass.CustIDS;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.PojoClass.SearchProductDealer;
import com.mwbtech.dealer_register.PojoClass.SearchProductDealerRequest;
import com.mwbtech.dealer_register.PojoClass.SearchProductDealerResponse;
import com.mwbtech.dealer_register.PojoClass.SubmitQuery;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.FileCompressor;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.ProjectUtilsKt;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.mwbtech.dealer_register.databinding.DialogDealerOptionBinding;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendEnquiryActivity extends AppCompatActivity implements ShowDealerRegisterAdapter.ItemCreationAdapterListener, CompoundButton.OnCheckedChangeListener, DealerListAdapter.DealerAdapterListener, View.OnClickListener, TextWatcher, AdapterView.OnItemSelectedListener {

    //Image
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    static final long image_size = 2097152;
    File mPhotoFile1, mPhotoFile2;
    FileCompressor mCompressor;
    EditText editText;
    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    DealerListAdapter dealerListAdapter;
    PrefManager prefManager;
    TextView tvSelect, Businessheader, SpinnerHeader;
    Button btnDelete;
//    RelativeLayout layoutDeleteAll;
    AppCompatImageView imgBack;
    TextView nodata, tvNumberOfDealer;
    LinearLayout linearLayout;

    Spinner spBusiness;
    BusinessTypeSpinnerAdapter businessTypeSpinnerAdapter;
    String Token;

    Customer_Interface customer_interface;
    AlertDialog.Builder builder, builder1;
    int typeId;
    List<CustIDS> custIDS;
    String typeName;
    Button btnPhoto, btnPhoto1;
    ImageView imgPhoto1, imgPhoto2;
    Dialog mdialogPhoto;

    CustomLinearLayoutManager customLinearLayoutManager;

    private int pastVisibleItems = 0, visibleItemCount = 0, totalItemCount = 0, pageNumber = 0, pageSize, totalPages, totalRecords;
    private boolean isLoading = false;
    private boolean shouldSelectAllDealers = true;
    private final boolean isFilteringList = false;
    private boolean isMoreAvailable = false;

    List<BusinessType> businessTypeLists = new ArrayList<>();
    List<SearchProductDealer> searchProductDealerLists = new ArrayList<>();
    List<SearchProductDealer> selectedDealers = new ArrayList<>();
    List<SearchProductDealer> searchProductDealerLists1 = new ArrayList<>();
//    SearchProductDealer searchProductDealer;
    SearchProductDealerRequest searchProductDealerRequest;
    String SelectedTranscationType = "";
    int imgCount = 0;
    Bitmap base64, base65;

    String ImageString = null, ImageString1 = null;
    CheckBox chselectall, chDeleteAll;
    SwitchCompat switchbtn;
    Button submitBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_list);

        custIDS = new ArrayList<>();
        if (getIntent() != null) {
            searchProductDealerRequest = (SearchProductDealerRequest) getIntent().getSerializableExtra("object");

            businessTypeLists = (List<BusinessType>) getIntent().getSerializableExtra("BusinessList");
            searchProductDealerLists = (List<SearchProductDealer>) getIntent().getSerializableExtra("Dealer");
            pageNumber = (int) getIntent().getSerializableExtra("pageNumber");
            pageSize = (int) getIntent().getSerializableExtra("pageSize");
            totalRecords = (int) getIntent().getSerializableExtra("totalRecords");
            totalPages = (int) getIntent().getSerializableExtra("totalPages");

            isMoreAvailable = pageNumber != totalPages;
            isLoading = isMoreAvailable;
        }
        SelectedTranscationType = getIntent().getStringExtra("Transcation Type");
        Log.e("type..", SelectedTranscationType + "");

        try {
            if (businessTypeLists.size() > 1) {
                businessTypeLists.add(0, new BusinessType(-1, "Select All"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        spBusiness = findViewById(R.id.spinnerTax);
        spBusiness.setSelection(0);
        prefManager = new PrefManager(this);
        mCompressor = new FileCompressor(this);

        Token = prefManager.getToken().get(TOKEN);
        editText = findViewById(R.id.search);

        recyclerView = findViewById(R.id.recyclerView);

        tvNumberOfDealer = findViewById(R.id.tvNumberDealer);
        nodata = findViewById(R.id.no_data);
        Businessheader = findViewById(R.id.typeHeader);
        SpinnerHeader = findViewById(R.id.spinner_header);
        linearLayout = findViewById(R.id.linear);
        tvSelect = findViewById(R.id.tvselect);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        customLinearLayoutManager = new CustomLinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(customLinearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(dealerListAdapter);
        editText.addTextChangedListener(this);


        dealerListAdapter = new DealerListAdapter(this, searchProductDealerLists, SendEnquiryActivity.this);
        recyclerView.setAdapter(dealerListAdapter);
        dealerListAdapter.notifyDataSetChanged();

        tvSelect.setText("Select All");

        btnDelete = findViewById(R.id.btn_delete_all);
        chselectall = findViewById(R.id.selectall);
//        chDeleteAll = (CheckBox) findViewById(R.id.deleteall);
//        switchbtn =  findViewById(R.id.switchbtn);


        submitBtn = findViewById(R.id.btnSubmit);
        imgBack = findViewById(R.id.back);

        chselectall.setOnTouchListener((v, event) -> {
//            if(chselectall.isChecked()){
                shouldSelectAllDealers = true;
//                isFilteringList = true;
//            }
            return false;
        });
        chselectall.setOnCheckedChangeListener(this);
//        chDeleteAll.setOnCheckedChangeListener(this);

//        switchbtn.setOnCheckedChangeListener(this);

        businessTypeSpinnerAdapter = new BusinessTypeSpinnerAdapter(this, businessTypeLists);
        spBusiness.setAdapter(businessTypeSpinnerAdapter);
        spBusiness.setOnItemSelectedListener(this);


        btnDelete.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        imgBack.setOnClickListener(v -> onBackPressed());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = customLinearLayoutManager.getChildCount();
                    totalItemCount = customLinearLayoutManager.getItemCount();
                    pastVisibleItems = customLinearLayoutManager.findFirstVisibleItemPosition();

                    if (isLoading) {
                        if (isMoreAvailable) {
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
//                                isFilteringList = false;
                                fetchDealersList();
                                isLoading = false;
                            }
                        }
                    }
                }
            }
        });

    /*    ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(this, EnquiryRequirementActivity.class);
        finish();
    }

    private String getBase64StringFromGallery(Bitmap thumbnail) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        ImageString1 = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

        return ImageString1;
    }

    @Override
    public void onItemSelected(DealerRegister itemCreation) {

    }


    List<SearchProductDealer> getDealerRegisterList = new ArrayList<>();

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        getDealerRegisterList = dealerListAdapter.getResultDealerAllList();

//        if (buttonView.getId() == R.id.selectall) {
            if (chselectall.isChecked()) {
                tvSelect.setText("Deselect All");
                if(shouldSelectAllDealers) {
                    for (SearchProductDealer model : searchProductDealerLists) {
                        model.setChecked(true);
//                        if(!isFilteringList) {
                            for (SearchProductDealer selectedDealer : selectedDealers) {
                                if (selectedDealer.getCustID().equals(model.getCustID())) {
                                    selectedDealer.setChecked(true);
                                }
                            }
                            if (!selectedDealers.contains(model)) selectedDealers.add(model);
//                        }
                    }
                }
            } else {
                tvSelect.setText("Select All");
                if(shouldSelectAllDealers) {
                    for (SearchProductDealer model : searchProductDealerLists) {
                        model.setChecked(false);
                        /*for (SearchProductDealer selectedDealer : selectedDealers) {
                            if (selectedDealer.getCustID().equals(model.getCustID())){
                                selectedDealer.setChecked(false);
                            }
                        }*/
                        /*if(!isFilteringList)*/ selectedDealers.remove(model);
                    }
                }
            }
        /*} else if (buttonView.getId() == R.id.deleteall) {
            if (chselectall.isChecked()) {
                tvSelect.setText("Deselect All");
                for (SearchProductDealer model : searchProductDealerLists1) {
                    model.setChecked(true);
                }
            } else {
                tvSelect.setText("Select All");
                for (SearchProductDealer model : searchProductDealerLists1) {
                    model.setChecked(false);
                }
            }
        }*/
        dealerListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDealerSelected(SearchProductDealer itemCreation,  boolean isChecked) {
        for (SearchProductDealer model : searchProductDealerLists) {
            if(model.getCustID().equals(itemCreation.getCustID())) model.setChecked(isChecked);
        }
        if(isChecked){
            if (!selectedDealers.contains(itemCreation)) selectedDealers.add(itemCreation);
        } else selectedDealers.remove(itemCreation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dealer_list, menu);

        int positionOfMenuItem = 0; // or whatever...
        MenuItem item = menu.getItem(positionOfMenuItem);
        SpannableString s = new SpannableString("Submit");
        //  s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.5f), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        item.setTitle(s);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            case R.id.select_all:
                if (dealerListAdapter.getResultDealerList().size() > 0 && !dealerListAdapter.getResultDealerList().isEmpty()) {
                    sendPhotoTOServer();
                } else {
                    CustomToast.showToast(SendEnquiryActivity.this, "Please select dealer");
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendPhotoTOServer() {

       /* builder1 = new AlertDialog.Builder(SendEnquiryActivity.this).setMessage(R.string.photo);
        builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                showImageAlertDialog();

            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }

        });
        AlertDialog alertDialog = builder1.create();
        alertDialog.show();*/
        Dialog mdialog = new Dialog(SendEnquiryActivity.this);
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DialogDealerOptionBinding binding = DialogDealerOptionBinding.inflate(LayoutInflater.from(this));

        mdialog.setContentView(binding.getRoot());
        mdialog.setCancelable(false);
        mdialog.show();
        binding.noTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                custIDS.clear();
                dealerListAdapter.notifyDataSetChanged();
                if (dealerListAdapter.getResultDealerList().size() != 0) {
                    //CustomToast.showToast(this, dealerListAdapter.getResultDealerList().toString());
                } else {
                    CustomToast.showToast(SendEnquiryActivity.this, "Please select dealer");
                }
                for (int i = 0; i < dealerListAdapter.getResultDealerList().size(); i++) {

                    SearchProductDealer sarch = dealerListAdapter.getResultDealerList().get(i);
//                    for(City city : searchProductDealer.getCityList()){
//                    if(sarch.)
                        custIDS.add(new CustIDS(sarch.getCustID(),sarch.getCity()));
//                    }
                }
                base64 = BitmapFactory.decodeFile(searchProductDealerRequest.getProductPhoto());
                base65 = BitmapFactory.decodeFile(searchProductDealerRequest.getProductPhoto2());

                try {
                    base64 = getResizedBitmap(base64, 1080, 980);
                    ImageString = getBase64StringFromGallery(base64);

                } catch (Exception e) {
                    ImageString = null;
                    e.printStackTrace();
                }
                try {
                    base65 = getResizedBitmap(base65, 1080, 980);
                    ImageString1 = getBase64StringFromGallery(base65);
                } catch (Exception e) {
                    e.printStackTrace();
                    ImageString1 = null;
                }

                SubmitQuery submitQuery = new SubmitQuery(0, custIDS,
                        searchProductDealerRequest.getCustId(),
                        searchProductDealerRequest.getCityId(),
                        searchProductDealerRequest.getProductID(),
                        searchProductDealerRequest.getBusinessTypeId(),
                        searchProductDealerRequest.getBusinessDemandID(),
                        searchProductDealerRequest.getTypeOfUse(),
                        searchProductDealerRequest.getRequirements(),
                        ImageString, ImageString1,
                        searchProductDealerRequest.getProfessionalRequirementID(),
                        false, "New Enquiry", SelectedTranscationType);
                submitDealerEnquiryToServer(submitQuery);
                mdialog.dismiss();
            }
        });
        binding.yesTv.setOnClickListener(view -> {
            showImageAlertDialog();
            mdialog.dismiss();
        });

    }

    private void showImageAlertDialog() {

        mdialogPhoto = new Dialog(SendEnquiryActivity.this);
        mdialogPhoto.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialogPhoto.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialogPhoto.setContentView(R.layout.dialog_photo);
        mdialogPhoto.setCancelable(false);
        mdialogPhoto.show();
        imgPhoto1 = mdialogPhoto.findViewById(R.id.imag1);
        imgPhoto2 = mdialogPhoto.findViewById(R.id.imag2);
        btnPhoto1 = mdialogPhoto.findViewById(R.id.btnPhoto1);
        btnPhoto = mdialogPhoto.findViewById(R.id.btnPhoto);
        imgPhoto1.setOnClickListener(SendEnquiryActivity.this);
        imgPhoto2.setOnClickListener(SendEnquiryActivity.this);
        btnPhoto.setOnClickListener(SendEnquiryActivity.this);
        btnPhoto1.setOnClickListener(SendEnquiryActivity.this);
    }

    private void fetchDealersList() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Gson gson = new Gson();
        String json = gson.toJson(searchProductDealerRequest);
        searchProductDealerRequest.setPageNumber(pageNumber+1);
        Log.e("productreq...", json);
        Call<SearchProductDealerResponse> call = customer_interface.searchProductDealerDetails("bearer " + Token, searchProductDealerRequest);
        call.enqueue(new Callback<SearchProductDealerResponse>() {
            @Override
            public void onResponse(Call<SearchProductDealerResponse> call, Response<SearchProductDealerResponse> response) {
                int statusCode = response.code();
                Log.e("Status", "code....." + statusCode);
                switch (statusCode) {
                    case 200:
//                        progressDialog.dismiss();
                        if(response.body() == null){
                            CustomToast.showToast(SendEnquiryActivity.this, "Data not found");
                            nodata.setVisibility(View.VISIBLE);
                            return;
                        }
                        if(response.body().getData().size() == 0){
                            submitBtn.setVisibility(View.VISIBLE);
                            nodata.setVisibility(View.VISIBLE);
                        } else nodata.setVisibility(View.GONE);

                        pageNumber = response.body().getPageNumber();

                        if(pageNumber == 1) searchProductDealerLists.clear();
                        totalPages = response.body().getTotalPages();
                        pageSize = response.body().getPageSize();
                        totalRecords = response.body().getTotalRecords();
                        isMoreAvailable = pageNumber != totalPages;
                        isLoading = isMoreAvailable;
//                        if(pageNumber == 1)
                        searchProductDealerLists.addAll(response.body().getData());
                        shouldSelectAllDealers = false;
                        chselectall.setChecked(false);
                        tvNumberOfDealer.setText("Total " + searchProductDealerLists.size() + " Results found");
                        checkForSelectedDealers();
                        dealerListAdapter.updateList(searchProductDealerLists);
//                        searchProductDealerLists = new ArrayList<>(response.body().getData());
//                        if (searchProductDealerLists.size() != 0) {
//                            dealerListAdapter.notifyDataSetChanged();
/*                            Intent i = new Intent(this, SendEnquiryActivity.class);
                            i.putExtra("object", (Serializable) searchProductDealer);
                            i.putExtra("BusinessList", (Serializable) selectedBusinessList);
                            i.putExtra("Dealer", (Serializable) searchProductDealerList);
                            i.putExtra("Transcation Type", (String) selectedTranscationType);
                            startActivity(i);*/
                            // startActivityForResult(i, 1);
//                        }
                        break;

                    case 404:
//                        progressDialog.dismiss();
                        break;

                    case 500:
//                        progressDialog.dismiss();
                        CustomToast.showToast(SendEnquiryActivity.this, "Server Error");
                        break;

                    case 401:
//                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(SendEnquiryActivity.this);
                        break;

                }

            }

            @Override
            public void onFailure(Call<SearchProductDealerResponse> call, Throwable t) {
//                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(SendEnquiryActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(SendEnquiryActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });

    }

    private void checkForSelectedDealers() {
        for (SearchProductDealer selectedDealer : selectedDealers) {
            for (SearchProductDealer searchedDealer : searchProductDealerLists) {
                if (selectedDealer.getCustID().equals(searchedDealer.getCustID())){
                    searchedDealer.setChecked(true);
                }
            }
        }
    }

    private void submitDealerEnquiryToServer(SubmitQuery resultDealerAllList) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(SendEnquiryActivity.this, "Please wait");
        //progressDialog.setMessage("Please wait..."); // Setting Message
        //progressDialog.setTitle("Sending Requirement.."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        Gson gson = new Gson();
        String json = gson.toJson(resultDealerAllList);
        Log.e("json.......", json);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<SubmitQuery> call = customer_interface.sendDealerEnquiry("bearer " + Token, resultDealerAllList);
        call.enqueue(new Callback<SubmitQuery>() {
            @Override
            public void onResponse(Call<SubmitQuery> call, Response<SubmitQuery> response) {
                try {

                    int statusCode = response.code();
                    switch (statusCode) {
                        case 200:
                            progressDialog.dismiss();
                            progressDialog.dismiss();
                            CustomToast.showToast(SendEnquiryActivity.this, "Enquiry Sent Successfully to "+ custIDS.size() + " dealers");
                            startActivity(new Intent(SendEnquiryActivity.this, DashboardActivity.class)
                                    .putExtra("isNewUser", false));
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getBaseContext());
                            builder1.setMessage("Enquiry sent successfully to "+ custIDS.size() + " dealers");
                            builder1.setCancelable(true);
                            builder1.setPositiveButton("Yes", (dialog, id) -> dialog.cancel());
                            builder1.setNegativeButton("No", (dialog, id) -> dialog.cancel());
                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                            CustomToast.showToast(SendEnquiryActivity.this, "Enquiry sent successfully to "+ custIDS.size() + " dealers");
                            //startActivity(new Intent(SendEnquiryActivity.this, DashboardActivity.class));
                            SendEnquiryActivity.this.finish();
                            break;

                        case 404:
                            progressDialog.dismiss();
                            break;

                        case 406:
                            progressDialog.dismiss();
                            break;

                        case 500:
                            progressDialog.dismiss();
                            CustomToast.showToast(SendEnquiryActivity.this, "Server Error");
                            break;

                        case 401:
                            progressDialog.dismiss();
                            SessionDialog.CallSessionTimeOutDialog(SendEnquiryActivity.this);
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
                    Toast.makeText(SendEnquiryActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(SendEnquiryActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }


    private void submitDealerEnquiryToServerPhoto(SubmitQuery resultDealerAllList) {
        Log.e("Send Data", "With  pic....." + resultDealerAllList.toString());
        MultipartBody.Part body = null, body1 = null;
        RequestBody requestBody1, requestBody2, requestData;
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
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(SendEnquiryActivity.this, "");
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        Gson gson = new Gson();
        String json = gson.toJson(resultDealerAllList);
        Log.e("SubmitqWithfile", json);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<SubmitQuery> call = customer_interface.sendDealerEnquiryFile("bearer " + Token, resultDealerAllList, body, body1);
        call.enqueue(new Callback<SubmitQuery>() {
            @Override
            public void onResponse(Call<SubmitQuery> call, Response<SubmitQuery> response) {
                try {
                    int statusCode = response.code();

                    switch (statusCode) {
                        case 200:
                            progressDialog.dismiss();
                            progressDialog.dismiss();
                            CustomToast.showToast(SendEnquiryActivity.this, "Enquiry Sent Successfully to "+ custIDS.size() + " dealers");
                            startActivity(new Intent(SendEnquiryActivity.this, DashboardActivity.class)
                                    .putExtra("isNewUser", false));
                            SendEnquiryActivity.this.finish();

                            // to delete the stored images in package directory
                            clearApplicationData();
                            mdialogPhoto.dismiss();
                            break;

                        case 404:
                            progressDialog.dismiss();
                            break;

                        case 500:
                            progressDialog.dismiss();
                            CustomToast.showToast(SendEnquiryActivity.this, "Server Error");
                            break;

                        case 406:
                            progressDialog.dismiss();
                            break;

                        case 401:
                            progressDialog.dismiss();
                            SessionDialog.CallSessionTimeOutDialog(SendEnquiryActivity.this);
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
                    Toast.makeText(SendEnquiryActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(SendEnquiryActivity.this, "Bad response from server.. Try again later ");
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_delete_all:
                if (searchProductDealerLists1.size() != 0) {
                    builder = new AlertDialog.Builder(SendEnquiryActivity.this).setTitle("Delete All Dealers").setMessage(R.string.alert_delete).setIcon(R.drawable.cross);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (searchProductDealerLists1.size() != 0) {
                                CustomToast.showToast(SendEnquiryActivity.this, "All Dealer Deleted.");
                                searchProductDealerLists1.clear();
                                dealerListAdapter.notifyDataSetChanged();
                            } else {
                                CustomToast.showToast(SendEnquiryActivity.this, "List is Empty");
                            }
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }

                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    CustomToast.showToast(SendEnquiryActivity.this, "List is Empty");
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
                custIDS.clear();
                dealerListAdapter.notifyDataSetChanged();
                if (mPhotoFile1 == null && mPhotoFile2 == null) {
                    CustomToast.showToast(SendEnquiryActivity.this, "Please select image");
                } else if (dealerListAdapter.getResultDealerList().size() == 0) {
                    CustomToast.showToast(SendEnquiryActivity.this, "Please select dealer");
                } else {
                    for (int i = 0; i < dealerListAdapter.getResultDealerList().size(); i++) {

                        SearchProductDealer sarch = dealerListAdapter.getResultDealerList().get(i);
//                        for(City city : searchProductDealer.getCityList()){
//                            custIDS.add(new CustIDS(sarch.getCustID(),city.getStatewithCityID()));
                        custIDS.add(new CustIDS(sarch.getCustID(),sarch.getCity()));
//                        }
                    }

                    SubmitQuery submitQuery = new SubmitQuery(0, custIDS,
                            searchProductDealerRequest.getCustId(),
                            searchProductDealerRequest.getCityId(),
                            searchProductDealerRequest.getProductID(),
                            searchProductDealerRequest.getBusinessTypeId(),
                            searchProductDealerRequest.getBusinessDemandID(),
                            searchProductDealerRequest.getTypeOfUse(),
                            searchProductDealerRequest.getRequirements(),
                            ImageString, ImageString1,
                            searchProductDealerRequest.getProfessionalRequirementID(),
                            false, "New Enquiry", SelectedTranscationType);
                    submitDealerEnquiryToServerPhoto(submitQuery);
                }

                break;

            case R.id.btnPhoto1:
                mdialogPhoto.dismiss();
                break;

            case R.id.btnSubmit:
                if (dealerListAdapter.getResultDealerList().size() > 0 && !dealerListAdapter.getResultDealerList().isEmpty()) {
                    sendPhotoTOServer();
                } else {
                    CustomToast.showToast(SendEnquiryActivity.this, "Please select dealer");
                }
                break;


        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        /*try {
            dealerListAdapter.getFilter().filter(s);
            recyclerView.getRecycledViewPool().clear();
            if (dealerListAdapter.getItemCount() <= 0) {
                linearLayout.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }

    @Override
    public void afterTextChanged(Editable s) {
        /*try {
            dealerListAdapter.getFilter().filter(s);
            recyclerView.getRecycledViewPool().clear();
            if (dealerListAdapter.getItemCount() <= 0) {
                linearLayout.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        pageNumber = 0;
//        isFilteringList = true;
        searchProductDealerRequest.setFilterBNameCityName(s.toString());
        searchProductDealerRequest.setFilterBusinessTypeId(typeId);
        fetchDealersList();
    }

    //business type selected from spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        BusinessType businessType = (BusinessType) parent.getItemAtPosition(position);
        typeId = businessType.getBusinessTypeID();
        typeName = businessType.getNameOfBusiness();
        Log.e("Selected", "tyep.........." + typeName);
//        isFilteringList = true;
        chselectall.setChecked(false);
        shouldSelectAllDealers = false;
        if (typeId == -1) {
            pageNumber = 1;
            searchProductDealerRequest.setFilterBNameCityName(editText.getText().toString().trim());
            searchProductDealerRequest.setFilterBusinessTypeId(typeId);
            fetchDealersList();
//            getFilterAllList(typeId, typeName);
        } else {
            pageNumber = 0;
            searchProductDealerRequest.setFilterBNameCityName(editText.getText().toString().trim());
            searchProductDealerRequest.setFilterBusinessTypeId(typeId);
            fetchDealersList();
//            getFilterList(typeId, typeName);
        }

        if (typeName.toLowerCase().equals("Professionals And Services")) {
            Businessheader.setText("Service Type");
            SpinnerHeader.setText("Service Type");
        } else {
            Businessheader.setText(R.string.txt_businesstype);
            SpinnerHeader.setText(R.string.txt_businesstype);
        }
    }

    private void getFilterAllList() {
        chselectall.setChecked(false);
//        chDeleteAll.setChecked(false);
        searchProductDealerLists1.clear();
        tvNumberOfDealer.setText("Total " + searchProductDealerLists.size() + " Results found");
        for (int i = 0; i < searchProductDealerLists.size(); i++) {
            SearchProductDealer searchProductDealer = searchProductDealerLists.get(i);
            searchProductDealerLists1.add(new SearchProductDealer(searchProductDealer.getCustID(), searchProductDealer.getCustomerName(), searchProductDealer.getBusinessType(), searchProductDealer.getMobileNumber(), searchProductDealer.getCity(), searchProductDealer.getVillageLocalityname(), false));
            dealerListAdapter = new DealerListAdapter(this, searchProductDealerLists1, SendEnquiryActivity.this);
            recyclerView.setAdapter(dealerListAdapter);
            dealerListAdapter.notifyDataSetChanged();

        }
    }


    private void getFilterList(String typeName) {
        chselectall.setChecked(false);
//        chDeleteAll.setChecked(false);
        searchProductDealerLists1.clear();
        for (int i = 0; i < searchProductDealerLists.size(); i++) {
            SearchProductDealer searchProductDealer = searchProductDealerLists.get(i);
            if (searchProductDealer.getBusinessType().equals(typeName)) {
                searchProductDealerLists1.add(new SearchProductDealer(searchProductDealer.getCustID(), searchProductDealer.getCustomerName(), searchProductDealer.getBusinessType(), searchProductDealer.getMobileNumber(), searchProductDealer.getCity(), searchProductDealer.getVillageLocalityname(), false));
                dealerListAdapter = new DealerListAdapter(this, searchProductDealerLists1, SendEnquiryActivity.this);
                recyclerView.setAdapter(dealerListAdapter);
                dealerListAdapter.notifyDataSetChanged();
                tvNumberOfDealer.setText("Total " + searchProductDealerLists1.size() + " Results found");
            } else {
                tvNumberOfDealer.setText("Total " + searchProductDealerLists1.size() + " Results found");
                dealerListAdapter.refreshList();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}


    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SendEnquiryActivity.this);
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
                    ProjectUtilsKt.checkCameraPermission(SendEnquiryActivity.this, (status -> {
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
                    ProjectUtilsKt.checkCameraPermission(SendEnquiryActivity.this, (status -> {
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
                        long len_img = mPhotoFile1.length();
                        if (len_img < image_size) {
                            Picasso.get()
                                    .load(mPhotoFile1)
                                    .into(imgPhoto1);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please select the image within 2MB", Toast.LENGTH_LONG);
                        }

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
                            // imgPhoto1.setImageBitmap(mPhotoFile1);
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
                            //imgPhoto2.setImageBitmap(BitmapFactory.decodeFile(ImagePath1));
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

}
