package com.mwbtech.dealer_register.Dashboard.Support;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.ContactChildCatAdapter;
import com.mwbtech.dealer_register.Adapter.ContactSubCatAdapter;
import com.mwbtech.dealer_register.Dashboard.Advertisement.AdRequestDetails.AdRequestDetailsActivity;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.PojoClass.ChildCategoryProduct;
import com.mwbtech.dealer_register.PojoClass.Mailmodule;
import com.mwbtech.dealer_register.PojoClass.SendMail;
import com.mwbtech.dealer_register.PojoClass.SubCategoryProduct;
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
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //Image
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;

    static final long image_size = 2097152;
    File mPhotoFile1, mPhotoFile2, mPhotoFile3;
    FileCompressor mCompressor;
    Boolean isproduct;
    Uri photoURI;

    EditText edMessage, EdSubcategory, EdChildCategory, EdProduct;
    EditText EnteredSubcategory, EnteredChildCategory;
    Button btnSend;
    Spinner edSubject;
    ImageView Image1, Image2;
    RelativeLayout SubjectLayout, ProductLayout;
    LinearLayoutCompat constraintLayout;
    AppCompatImageView Attach, whatsappSend, imgBack;

    String SubCategoryName, ChildCategoryName;
    int SubCategoryID, ChildCategoryID;

    Dialog mDialogSubcategory, mDialogChildcategory;
    EditText dialogEdSubCategory, dialogEdChildCategory;
    ListView listViewSubCat, lisViewChildCat;
    ContactSubCatAdapter subCategoryAdapter;
    ContactChildCatAdapter childCategoryAdapter;
    List<SubCategoryProduct> subCategoryProducts;
    List<ChildCategoryProduct> childCategoryProducts;

    ArrayAdapter taxAdapter;
    String type;
    int imgCount = 0, CustID;
    String ImagePath, ProductImage1, ProductImage2;
    public static PrefManager prefManager;
    Customer_Interface customer_interface;
    String Token;
    ImageView attachImg;
    String[] SubType = {"Select Subject", "Problem related to App", "Problem related to data","Problem related to advertisement", "Request for addition of new product","Feedback", "Others"};
    private static final float maxHeight = 1280.0f;
    private static final float maxWidth = 1280.0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        initializeViews();
        initializeSharedData();
        initializeClickEvents();
        initializeDropDown();

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
    }



    private void initializeViews() {
        constraintLayout = findViewById(R.id.parentLayout);
        edSubject = findViewById(R.id.subject);
        edMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.send_email);

        EdSubcategory = findViewById(R.id.subcategory);
        EnteredSubcategory = findViewById(R.id.EnterSubCat);

        EdChildCategory = findViewById(R.id.childcategory);
        EnteredChildCategory = findViewById(R.id.EnterChildCat);

        EdProduct = findViewById(R.id.productname);
        SubjectLayout = findViewById(R.id.subjectLayout);
        ProductLayout = findViewById(R.id.productLayout);
        Image1 = findViewById(R.id.imageFront1);
        Image2 = findViewById(R.id.imageFront2);

        attachImg = findViewById(R.id.attactImg);
        Attach = findViewById(R.id.attachment);
        imgBack = findViewById(R.id.back);
        whatsappSend = findViewById(R.id.whatsapp_send);
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
  */
    }


    private void initializeSharedData() {
        prefManager = new PrefManager(this);
        mCompressor = new FileCompressor(this);
        Token = prefManager.getToken().get(TOKEN);
        CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
    }

    private void initializeClickEvents() {
        btnSend.setOnClickListener(this);
        whatsappSend.setOnClickListener(this);
        Attach.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        EdSubcategory.setOnClickListener(this);
        EdChildCategory.setOnClickListener(this);
        Image1.setOnClickListener(this);
        Image2.setOnClickListener(this);
        edSubject.setOnItemSelectedListener(this);
        findViewById(R.id.back).setOnClickListener(view -> {
            startActivity(new Intent(FeedbackActivity.this, DashboardActivity.class)
                    .putExtra("isNewUser", false));
            finish();
        });

        attachImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeImgPath();
            }
        });

    }


    private void initializeDropDown() {
        taxAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, SubType) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                text.setTextSize(18);
                return view;
            }
        };
        taxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edSubject.setAdapter(taxAdapter);
    }
    private void removeImgPath() {
        AlertDialog alertDialog = new AlertDialog.Builder(FeedbackActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Do you sure, you want to remove image");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ImagePath = null;
                        attachImg.setVisibility(View.GONE);
                        dialog.dismiss();

                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = SubType[position];
        if (type.equals("Request for addition of new product")) {
            // visible product add details layout
            Attach.setVisibility(View.GONE);
            ProductLayout.setVisibility(View.VISIBLE);
            SubjectLayout.setVisibility(View.GONE);

        } else {
            // visible email body layout
            Attach.setVisibility(View.VISIBLE);
            ProductLayout.setVisibility(View.GONE);
            SubjectLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                finish();
                break;
            case R.id.attachment:
                imgCount = 0;
                selectImage();
                break;
            case R.id.whatsapp_send:
                try {
                    String toNumber = "918197834545";
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + "" + toNumber + "?body=" + ""));
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(FeedbackActivity.this, "it may be you dont have whats app", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.send_email:
                if (!ValidateSubject()) {
                    return;
                } else {
                    if (type.equals("Request for addition of new product")) {
                        if (EdSubcategory.getText().toString().isEmpty()) {
                            CustomToast.showToast(FeedbackActivity.this, "Please select Sub-category");
                        } else {
                            if (EdSubcategory.getText().toString().equals("Other")) {
                                if (!ValidateSubcategory() | !ValidateChildcategory() | !ValidateProduct()) {
                                    return;
                                } else {
                                    if (EdChildCategory.getText().toString().equals("Other")) {
                                        if (mPhotoFile2==null | mPhotoFile3==null) {
                                            CustomToast.showToast(FeedbackActivity.this, "Please upload product image");
                                        } else {
                                            String EntSubcategory = EnteredSubcategory.getText().toString();
                                            String EntChildCat = EdChildCategory.getText().toString();
                                            String Product = EdProduct.getText().toString();
                                            if (ConnectivityReceiver.isConnected()) {
                                                SendProductRequestToServer(CustID, EntSubcategory, EntChildCat, Product,type);
                                            }else {
                                                showError();
                                            }
                                        }

                                    } else {
                                        if (mPhotoFile2==null | mPhotoFile3==null) {
                                            CustomToast.showToast(FeedbackActivity.this, "Please upload product image");
                                        } else {
                                            //call add product method
                                            String EntSubcategory = EnteredSubcategory.getText().toString();
                                            String EntChildCat = EnteredChildCategory.getText().toString();
                                            String Product = EdProduct.getText().toString();
                                            if (ConnectivityReceiver.isConnected()) {
                                                SendProductRequestToServer(CustID, EntSubcategory, EntChildCat, Product,type );
                                            }else {
                                                showError();
                                            }

                                        }
                                    }
                                }
                            } else {
                                if (!ValidateSelectedSubcategory() | !ValidateSelectedcategory() | !ValidateProduct()) {
                                    return;
                                } else {
                                    if (mPhotoFile2==null | mPhotoFile3==null) {
                                        CustomToast.showToast(FeedbackActivity.this, "Please upload product image");
                                    } else {
                                        //call add selected product method
                                        String Subcategory = EdSubcategory.getText().toString();
                                        String ChildCat = EdChildCategory.getText().toString();
                                        String Product = EdProduct.getText().toString();
                                        if (ConnectivityReceiver.isConnected()) {
                                            SendProductRequestToServer(CustID, Subcategory, ChildCat, Product,type);
                                        }else {
                                            showError();
                                        }
                                    }
                                }
                            }
                        }
                    } else {

                        if (!ValidateBody()) {
                            return;
                        } else {
                            if (ConnectivityReceiver.isConnected()) {
                                SendMail sendMail = new SendMail(CustID, type, edMessage.getText().toString().trim());
                                if (mPhotoFile1 == null) {
                                    SendMailToServer(sendMail);
                                } else {
                                    SendMailToServerWithImage(sendMail);
                                }
                            }else {
                             showError();
                            }
                        }
                    }
                }

                break;

            case R.id.subcategory:
                mDialogSubcategory = new Dialog(FeedbackActivity.this);
                mDialogSubcategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialogSubcategory.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mDialogSubcategory.setContentView(R.layout.dailog_subcategory_contact);
                mDialogSubcategory.setCancelable(true);
                dialogEdSubCategory = (EditText) mDialogSubcategory.findViewById(R.id.edSubcategory);
                listViewSubCat = (ListView) mDialogSubcategory.findViewById(R.id.listviewSubcat);
                ImageView imageView = (ImageView) mDialogSubcategory.findViewById(R.id.cancel_category);
                mDialogSubcategory.show();
                if (ConnectivityReceiver.isConnected()) {
                    GetAllSubCategory();
                }else {
                    Toast.makeText(this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
                }

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogSubcategory.dismiss();
                    }
                });

                listViewSubCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SubCategoryProduct item = (SubCategoryProduct) listViewSubCat.getItemAtPosition(position);
                        SubCategoryID = item.getSubCategoryId();
                        Log.e("subid...",String.valueOf(SubCategoryID));
                        SubCategoryName = item.getSubCategoryName();
                        if (SubCategoryName.equals("Other")) {
                            //visible edittext user to enter
                            EdSubcategory.setText(SubCategoryName);
                            EnteredSubcategory.setVisibility(View.VISIBLE);
                            EdChildCategory.setVisibility(View.GONE);
                            EnteredChildCategory.setVisibility(View.VISIBLE);
                            mDialogSubcategory.dismiss();
                        } else {
                            EdSubcategory.setText(SubCategoryName);
                            EnteredSubcategory.setVisibility(View.GONE);
                            EdChildCategory.setVisibility(View.VISIBLE);
                            EnteredChildCategory.setVisibility(View.GONE);
                            mDialogSubcategory.dismiss();
                        }
                    }
                });
                dialogEdSubCategory.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            subCategoryAdapter.getFilter().filter(s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            subCategoryAdapter.getFilter().filter(s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;

            case R.id.childcategory:
                if (TextUtils.isEmpty(EdSubcategory.getText().toString())) {
                    CustomToast.showToast(FeedbackActivity.this, "Please select SubCategory");
                } else {

                    mDialogChildcategory = new Dialog(FeedbackActivity.this);
                    mDialogChildcategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mDialogChildcategory.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    mDialogChildcategory.setContentView(R.layout.dailog_subcategory_contact);
                    mDialogChildcategory.setCancelable(true);
                    dialogEdChildCategory = (EditText) mDialogChildcategory.findViewById(R.id.edSubcategory);
                    lisViewChildCat = (ListView) mDialogChildcategory.findViewById(R.id.listviewSubcat);
                    ImageView imageView1 = (ImageView) mDialogChildcategory.findViewById(R.id.cancel_category);
                    mDialogChildcategory.show();
                    if (ConnectivityReceiver.isConnected()) {
                        GetAllChildCategory(SubCategoryID);
                    }else {
                        Toast.makeText(this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
                    }

                    imageView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialogChildcategory.dismiss();
                        }
                    });


                    lisViewChildCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ChildCategoryProduct item = (ChildCategoryProduct) lisViewChildCat.getItemAtPosition(position);
                            ChildCategoryID = item.getChildCategoryId();
                            ChildCategoryName = item.getChildCategoryName();
                            if (ChildCategoryName.equals("Other")) {
                                //visible edittext user to enter
                                EdChildCategory.setText(ChildCategoryName);
                                EnteredChildCategory.setVisibility(View.VISIBLE);
                                mDialogChildcategory.dismiss();
                            } else {
                                EdChildCategory.setText(ChildCategoryName);
                                EnteredChildCategory.setVisibility(View.GONE);
                                mDialogChildcategory.dismiss();
                            }


                        }
                    });
                    dialogEdChildCategory.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            try {

                                childCategoryAdapter.getFilter().filter(s);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            try {
                                childCategoryAdapter.getFilter().filter(s);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;

            case R.id.imageFront1:
                imgCount = 1;
                selectImage();
                break;

            case R.id.imageFront2:
                imgCount = 2;
                selectImage();
                break;

        }
    }

    private void showError() {
        Snackbar snackbar = Snackbar
                .make(constraintLayout, "You are not connected to Internet.!", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void SendProductRequestToServer(int custID, String Subcategory, String ChildCat, String product,String type) {
        MultipartBody.Part body = null, body1 = null;
      //  File file1, file2;
        RequestBody requestBody1, requestBody2;
        try {

           // file1 = new File(productImage1);
            requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile2);
            body = MultipartBody.Part.createFormData("file1", mPhotoFile2.getName(), requestBody1);

            //file2 = new File(productImage2);
            requestBody2 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile3);
            body1 = MultipartBody.Part.createFormData("file2", mPhotoFile3.getName(), requestBody2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending ...."); // Setting Message
        // progressDialog.setTitle("Loading details"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        SendMail productRequestMail = new SendMail(custID, Subcategory, ChildCat, product,type);
        Gson gson=new Gson();
        String json=gson.toJson(productRequestMail);
        Log.e("module...",json);
        Call<Mailmodule> call = customer_interface.SendEmailWithFile("bearer " + Token,true, productRequestMail, body, body1);
        call.enqueue(new Callback<Mailmodule>() {
            @Override
            public void onResponse(Call<Mailmodule> call, Response<Mailmodule> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        progressDialog.dismiss();
                        Mailmodule msg = response.body();
                        if (msg.getValue().equals("Failure sending mail.")) {
                            CustomToast.showToast(FeedbackActivity.this, "Sending failed");
                        } else {
                            CustomToast.showToast(FeedbackActivity.this, "Your feedback sent successfully");
                            startActivity(new Intent(FeedbackActivity.this, DashboardActivity.class)
                                    .putExtra("isNewUser", false));
                            clearApplicationData();
                        }
                        /*CustomToast.showToast(FeedbackActivity.this, "Your feedback sent successfully");
                        startActivity(new Intent(FeedbackActivity.this, DashboardActivity.class));*/
                        // to delete the stored images in package directory
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(FeedbackActivity.this, "Server error...Try again");
                        break;

                    case 404:
                        progressDialog.dismiss();
                        CustomToast.showToast(FeedbackActivity.this, "Try again later....");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(FeedbackActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<Mailmodule> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(FeedbackActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(FeedbackActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void GetAllChildCategory(int subCategoryID) {
        ProgressDialog progressDialog = ShowProgressDialog.createProgressDialogWithtxt(FeedbackActivity.this, "Getting details please wait...");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Log.e("subcategoryid....",String.valueOf(subCategoryID));
        Call<List<ChildCategoryProduct>> call = customer_interface.GetAllChildCatName("bearer " + Token, subCategoryID);
        call.enqueue(new Callback<List<ChildCategoryProduct>>() {
            @Override
            public void onResponse(Call<List<ChildCategoryProduct>> call, Response<List<ChildCategoryProduct>> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        progressDialog.dismiss();
                        childCategoryProducts = (List<ChildCategoryProduct>) response.body();
                        Log.e("childlist.....",childCategoryProducts.toString());
                        childCategoryAdapter = new ContactChildCatAdapter(FeedbackActivity.this, childCategoryProducts);
                        lisViewChildCat.setAdapter(childCategoryAdapter);
                        childCategoryAdapter.notifyDataSetChanged();
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(FeedbackActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(FeedbackActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<ChildCategoryProduct>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(FeedbackActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(FeedbackActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void GetAllSubCategory() {
        ProgressDialog progressDialog = ShowProgressDialog.createProgressDialogWithtxt(FeedbackActivity.this, "Getting details please wait...");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<SubCategoryProduct>> call = customer_interface.GetAllSubCategory("bearer " + Token);
        call.enqueue(new Callback<List<SubCategoryProduct>>() {
            @Override
            public void onResponse(Call<List<SubCategoryProduct>> call, Response<List<SubCategoryProduct>> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        progressDialog.dismiss();
                        subCategoryProducts = (List<SubCategoryProduct>) response.body();
                        subCategoryAdapter = new ContactSubCatAdapter(FeedbackActivity.this, subCategoryProducts);
                        listViewSubCat.setAdapter(subCategoryAdapter);
                        subCategoryAdapter.notifyDataSetChanged();
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(FeedbackActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(FeedbackActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<SubCategoryProduct>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(FeedbackActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(FeedbackActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }


    private void SendMailToServerWithImage(SendMail sendMail) {
        MultipartBody.Part body = null;
       // File file1;
        RequestBody requestBody1;
       // file1 = new File(imagePath);
        requestBody1 = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile1);
        body = MultipartBody.Part.createFormData("file1", mPhotoFile1.getName(), requestBody1);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending ...."); // Setting Message
        // progressDialog.setTitle("Loading details"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Gson gson=new Gson();
        String json= gson.toJson(sendMail);
        Log.e("feedback body...",json);
       /* if(sendMail.getMailSubject().equals("Request for addition of new product"))
            isproduct=true;
        else
            isproduct=false;*/
        Call<Mailmodule> call = customer_interface.SendEmailWithFile("bearer " + Token,false, sendMail, body, null);
        call.enqueue(new Callback<Mailmodule>() {
            @Override
            public void onResponse(Call<Mailmodule> call, Response<Mailmodule> response) {
                int status_code = response.code();
                Log.e("statuscode...",String.valueOf(response.code()));
                switch (status_code) {
                    case 200:
                        progressDialog.dismiss();
                        Mailmodule msg = response.body();
                        if (msg.getValue().equals("Failure sending mail.")) {
                            CustomToast.showToast(FeedbackActivity.this, "Sending failed");
                        } else {
                            CustomToast.showToast(FeedbackActivity.this, "Your feedback sent successfully");
                            startActivity(new Intent(FeedbackActivity.this, DashboardActivity.class)
                                    .putExtra("isNewUser", false));
                        }
                        // to delete the stored images in package directory
                        clearApplicationData();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(FeedbackActivity.this, "Server error...Try again");
                        break;

                    case 404:
                        progressDialog.dismiss();
                        CustomToast.showToast(FeedbackActivity.this, "Try again later....");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(FeedbackActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<Mailmodule> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(FeedbackActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(FeedbackActivity.this, "Bad response from server.. Try again later ");
                }

            }
        });
    }

    private void SendMailToServer(SendMail sendMail) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending ...."); // Setting Message
        // progressDialog.setTitle("Loading details"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<Mailmodule> call = customer_interface.SendEmail("bearer " + Token, sendMail);
        call.enqueue(new Callback<Mailmodule>() {
            @Override
            public void onResponse(Call<Mailmodule> call, Response<Mailmodule> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        progressDialog.dismiss();
                        Mailmodule msg = response.body();
                        if (msg.getValue().equals("Failure sending mail.")) {
                            CustomToast.showToast(FeedbackActivity.this, "Sending failed");
                        } else {
                            CustomToast.showToast(FeedbackActivity.this, "Your feedback sent successfully");
                            startActivity(new Intent(FeedbackActivity.this, DashboardActivity.class)
                                    .putExtra("isNewUser", false));
                        }

                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(FeedbackActivity.this, "Server error...Try again");
                        break;

                    case 404:
                        progressDialog.dismiss();
                        CustomToast.showToast(FeedbackActivity.this, "Try again later....");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(FeedbackActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<Mailmodule> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(FeedbackActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(FeedbackActivity.this, "Bad response from server.. Try again later ");
                }

            }
        });
    }

    public void clearApplicationData() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File appDir = new File(storageDir.getParent());
        if(appDir.exists()){
            String[] children = appDir.list();
            for(String s : children){
                if(!s.equals("files")){
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

    public boolean ValidateSubject() {
        if (type.equals("Select Subject")) {
            CustomToast.showToast(FeedbackActivity.this, "Please select the subject");
            return false;
        } else {
            return true;
        }
    }
    public boolean ValidateBody() {
        if (TextUtils.isEmpty(edMessage.getText().toString())) {
            CustomToast.showToast(FeedbackActivity.this, "Please enter message body");
            return false;
        } else {
            return true;
        }
    }
    public boolean ValidateSubcategory() {
        if (TextUtils.isEmpty(EnteredSubcategory.getText().toString())) {
            CustomToast.showToast(FeedbackActivity.this, "Please enter sub-category name");
            return false;
        } else {
            return true;
        }
    }
    public boolean ValidateSelectedSubcategory() {
        if (TextUtils.isEmpty(EdSubcategory.getText().toString())) {
            CustomToast.showToast(FeedbackActivity.this, "Please select sub-category name");
            return false;
        } else {
            return true;
        }
    }
    public boolean ValidateChildcategory() {
        if (TextUtils.isEmpty(EnteredChildCategory.getText().toString())) {
            CustomToast.showToast(FeedbackActivity.this, "Please enter child-category name");
            return false;
        } else {
            return true;
        }
    }
    public boolean ValidateSelectedcategory() {
        if (TextUtils.isEmpty(EdChildCategory.getText().toString())) {
            CustomToast.showToast(FeedbackActivity.this, "Please select child-category name");
            return false;
        } else {
            return true;
        }
    }
    public boolean ValidateProduct() {
        if (TextUtils.isEmpty(EdProduct.getText().toString())) {
            CustomToast.showToast(FeedbackActivity.this, "Please enter product name");
            return false;
        } else {
            return true;
        }
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.email, menu);
        Attach = menu.findItem(R.id.attachment);
        return true;
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            case R.id.attachment:
                imgCount = 0;
                selectImage();
                return true;
            case R.id.whatsapp_send:
                try {
                    String toNumber = "918197834545";
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + "" + toNumber + "?body=" + ""));
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(FeedbackActivity.this, "it may be you dont have whats app", Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FeedbackActivity.this);
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
            File photoFile1 = null;
            File photoFile2 = null;
            File photoFile3 = null;
            try {
                if (imgCount == 0) {
                    photoFile1 = createImageFile();
                } else if (imgCount == 1) {
                    photoFile2 = createImageFile();
                } else {
                    photoFile3 = createImageFile();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (imgCount == 0) {
                if (photoFile1 != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.mwbtech.dealer_register" + ".provider",
                            photoFile1);
                    mPhotoFile1 = photoFile1;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    ProjectUtilsKt.checkCameraPermission(FeedbackActivity.this, (status -> {
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                        return null;
                    }));

                }
            } else if (imgCount == 1) {
                if (photoFile2 != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.mwbtech.dealer_register" + ".provider",
                            photoFile2);
                    mPhotoFile2 = photoFile2;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    ProjectUtilsKt.checkCameraPermission(FeedbackActivity.this, (status -> {
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                        return null;
                    }));

                }
            } else {
                if (photoFile3 != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.mwbtech.dealer_register" + ".provider",
                            photoFile3);
                    mPhotoFile3 = photoFile3;
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    ProjectUtilsKt.checkCameraPermission(FeedbackActivity.this, (status -> {
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
                        long len_img=mPhotoFile1.length();
                        if(len_img<image_size) {
                            attachImg.setVisibility(View.VISIBLE);
                            Picasso.get()
                                    .load(mPhotoFile1)
                                    .into(attachImg);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Please select the image within 2MB",Toast.LENGTH_LONG);
                        }

                    } else if (imgCount == 1) {
                        mPhotoFile2 = mCompressor.compressToFile(mPhotoFile2);
                        long len_img=mPhotoFile2.length();
                        if(len_img<image_size) {
                            attachImg.setVisibility(View.VISIBLE);
                            Picasso.get()
                                    .load(mPhotoFile2)
                                    .into(Image1);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Please select the image within 2MB",Toast.LENGTH_LONG);
                        }
                    } else {
                        mPhotoFile3 = mCompressor.compressToFile(mPhotoFile3);
                        long len_img=mPhotoFile3.length();
                        if(len_img<image_size) {
                            Picasso.get()
                                    .load(mPhotoFile3)
                                    .into(Image2);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Please select the image within 2MB",Toast.LENGTH_LONG);
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
                                attachImg.setVisibility(View.VISIBLE);
                                Picasso.get()
                                        .load(mPhotoFile1)
                                        .into(attachImg);
                            } else {
                                Toast.makeText(getApplicationContext(), "Please select the image within 2MB", Toast.LENGTH_LONG);
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

                    } else if (imgCount == 1) {
                        if (!TextUtils.isEmpty(getRealPathFromUri(selectedImage))) {
                            mPhotoFile2 = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                            long len_img = mPhotoFile2.length();
                            if (len_img < image_size) {
                                Picasso.get()
                                        .load(mPhotoFile2)
                                        .into(Image1);
                            } else {
                                Toast.makeText(getApplicationContext(), "Please select the image within 2MB", Toast.LENGTH_LONG);
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
                    } else {
                        if (!TextUtils.isEmpty(getRealPathFromUri(selectedImage))) {
                            mPhotoFile3 = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                            long len_img = mPhotoFile3.length();
                            if (len_img < image_size) {
                                Picasso.get()
                                        .load(mPhotoFile3)
                                        .into(Image2);
                            } else {
                                Toast.makeText(getApplicationContext(), "Please select the image within 2MB", Toast.LENGTH_LONG);
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
        FeedbackActivity.this.finish();
    }
}
