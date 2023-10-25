package com.mwbtech.dealer_register.Dashboard.Advertisement.QuoteAd;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Dashboard.Advertisement.AdvertisementMenuActivity;
import com.mwbtech.dealer_register.PojoClass.AvailableSlotModel;
import com.mwbtech.dealer_register.PojoClass.Mailmodule;
import com.mwbtech.dealer_register.PojoClass.NewAdvertisementModule;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.FileCompressor;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImageForAdActivity extends AppCompatActivity implements View.OnClickListener {

    //Image
    static final int REQUEST_GALLERY_PHOTO = 2;
    File mPhotoFile;
    FileCompressor mCompressor;
    static final long image_size = 2097152;
    //AvailableSlotModel availableSlotModel;
    NewAdvertisementModule availableSlotModel;

    Customer_Interface customer_interface;
    Button btnUploadImage,SubmitAdBtn,SubmitImageLater,SubmitTextLater;
    ImageView ivSelectedImage;
    AppCompatImageView imgBack;
    EditText edText;
    int adMainId;
    String Token;
    PrefManager prefManager;
    String advertisementType,advertisementName;
    RelativeLayout rlImageLayout;
    LinearLayout llUploadImageLayout,llTextInputLayout;
    Customer_Interface api_interface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image_for_ad);
        initializeViews();
        initializeSharedData();
        initializeClickEvents();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
    }


    private void initializeViews() {
        mCompressor = new FileCompressor(this);
        rlImageLayout = findViewById(R.id.rlBannerLayout);
        llUploadImageLayout  = findViewById(R.id.llUplaod);
        llTextInputLayout = findViewById(R.id.llTxtLayout);
        edText = findViewById(R.id.text);
        imgBack = findViewById(R.id.back);
        btnUploadImage = findViewById(R.id.uploadBtn);
        ivSelectedImage = findViewById(R.id.ivImage);
        SubmitAdBtn = findViewById(R.id.btnSubmitAd);
        SubmitImageLater=findViewById(R.id.uploadBtnlater);
        SubmitTextLater=findViewById(R.id.uploadAdBtnlater);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));

    }
    private void initializeSharedData() {
        prefManager = new PrefManager(this);
        availableSlotModel = (NewAdvertisementModule) getIntent().getSerializableExtra("data");
        adMainId = getIntent().getExtras().getInt("adMainId");
        advertisementType = getIntent().getExtras().getString("adType");
        advertisementName=getIntent().getExtras().getString("adname");
        Token = prefManager.getToken().get(TOKEN);
        if (advertisementType.equals("TextAd")) {
            llTextInputLayout.setVisibility(View.VISIBLE);
            SubmitTextLater.setVisibility(View.VISIBLE);
            rlImageLayout.setVisibility(View.GONE);
            llUploadImageLayout.setVisibility(View.GONE);
            SubmitImageLater.setVisibility(View.GONE);

        }else {
            llTextInputLayout.setVisibility(View.GONE);
            SubmitTextLater.setVisibility(View.GONE);
            rlImageLayout.setVisibility(View.VISIBLE);
            llUploadImageLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initializeClickEvents() {
        SubmitAdBtn.setOnClickListener(this);
        SubmitImageLater.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);
        SubmitTextLater.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmitAd:
                if (advertisementType.equals("TextAd")) {
                    if (edText.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Please enter message", Toast.LENGTH_SHORT).show();
                    }else {
                        sendTextToServer(edText.getText().toString());
                    }

                }else {
                    if (mPhotoFile==null) {
                        Toast.makeText(this, "Please select Image", Toast.LENGTH_SHORT).show();
                    }else {
                        sendImageToServer();
                    }
                }

                break;

            case R.id.uploadBtn:
                selectImage();
                break;

            case R.id.back:
                onBackPressed();
                break;
            case R.id.uploadBtnlater:
                alertWithoutImage();
                break;
            case R.id.uploadAdBtnlater:
                alertWithoutImage();
                break;
        }
    }

    private void sendImageToServer() {
        MultipartBody.Part body = null;
        RequestBody requestBody_custId;
        try {
            // File file = new File(imageString);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), mPhotoFile);
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("file", mPhotoFile.getName(), requestFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(UploadImageForAdActivity.this,"Please wait");
        //progressDialog.setMessage("Please wait.."); // Setting Message
        //progressDialog.setTitle("Uploading Image"); // Setting Title
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        AvailableSlotModel slotModel = new AvailableSlotModel(adMainId);
        requestBody_custId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(adMainId));
        api_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<NewAdvertisementModule> call = api_interface.upLoadAd("bearer " + Token, body,requestBody_custId);
        //Call<AvailableSlotModel> call = api_interface.upLoadAdImage("bearer " + Token, slotModel, body);
        call.enqueue(new Callback<NewAdvertisementModule>() {
            @Override
            public void onResponse(Call<NewAdvertisementModule> call, Response<NewAdvertisementModule> response) {
                int statusCode = response.code();
                String adname=advertisementName;
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        mPhotoFile=null;
                        AlertDialog.Builder builder
                                = new AlertDialog
                                .Builder(UploadImageForAdActivity.this);
                        builder.setMessage("Your advertisement(" +adname+ ")is sent successfully to Admin,Once it is approved we will notify");
                        builder.setTitle("");
                        builder.setCancelable(false);
                        builder
                                .setPositiveButton(
                                        "Ok",
                                        new DialogInterface
                                                .OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which)
                                            {
                                                navigateToDashboard();
                                                clearApplicationData();
                                            }
                                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(UploadImageForAdActivity.this);
                        break;
                }
            }
            @Override
            public void onFailure(Call<NewAdvertisementModule> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(UploadImageForAdActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(UploadImageForAdActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }
    private void sendTextToServer(String message) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(UploadImageForAdActivity.this,"Please wait");
        progressDialog.setMessage("Please wait.."); // Setting Message
        progressDialog.setTitle("Saving Advertisement"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        AvailableSlotModel slotModel = new AvailableSlotModel(adMainId,message);
        Gson gson = new Gson();
        String json = gson.toJson(slotModel);
        api_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<AvailableSlotModel> call = api_interface.SaveText("bearer " + Token,slotModel);
        call.enqueue(new Callback<AvailableSlotModel>() {
            @Override
            public void onResponse(Call<AvailableSlotModel> call, Response<AvailableSlotModel> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        String adname=advertisementName;
                        AlertDialog.Builder builder
                                = new AlertDialog
                                .Builder(UploadImageForAdActivity.this);
                        builder.setMessage("Your advertisement(" +adname+ ")is sent successfully to Admin,Once it is approved we will notify");
                        builder.setTitle("");
                        builder.setCancelable(false);
                        builder
                                .setPositiveButton(
                                        "Ok",
                                        new DialogInterface
                                                .OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which)
                                            {

                                                navigateToDashboard();
                                                clearApplicationData();
                                            }
                                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(UploadImageForAdActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(UploadImageForAdActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<AvailableSlotModel> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(UploadImageForAdActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(UploadImageForAdActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });

    }

    private void navigateToDashboard() {
        startActivity(new Intent(UploadImageForAdActivity.this, AdvertisementMenuActivity.class));
        UploadImageForAdActivity.this.finish();
    }


    public void clearApplicationData() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File appDir = new File(storageDir.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("files")) {
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


    private void selectImage() {
        dispatchGalleryIntent();
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
            if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();
                try {
                    if (!TextUtils.isEmpty(getRealPathFromUri(selectedImage))) {
                        mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                        long len_img = mPhotoFile.length();
                        if (len_img < image_size) {
                            Picasso.get()
                                    .load(mPhotoFile)
                                    .into(ivSelectedImage);
                        } else {
                            Toast.makeText(UploadImageForAdActivity.this, "Please select the image within 2MB", Toast.LENGTH_LONG);
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
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                alertDelete();
                //onBackPressed();
                //finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        /*startActivity(new Intent(UploadImageForAdActivity.this, EstimationPriceActivity.class)
                .putExtra("data", (Serializable) availableSlotModel));
        UploadImageForAdActivity.this.finish();*/
        //Intent intent = new Intent(this, EstimationPriceActivity.class);
        alertDelete();
    }
    private void alertWithoutImage(){
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(UploadImageForAdActivity.this);
        builder.setMessage("Do you want to Book this Ad?");
        builder.setTitle("");
        builder.setCancelable(false);
        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                AlertDialog.Builder builder
                                        = new AlertDialog
                                        .Builder(UploadImageForAdActivity.this);
                                builder.setMessage("Your advertisement with ReferenceId (" +advertisementName+ ")is sent successfully to Admin,Once it is approved we will notify");
                                builder.setTitle("");
                                builder.setCancelable(false);
                                builder
                                        .setPositiveButton(
                                                "Ok",
                                                new DialogInterface
                                                        .OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog,
                                                                        int which)
                                                    {
                                                        navigateToDashboard();
                                                        clearApplicationData();
                                                    }
                                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                //startActivity(new Intent(UploadImageForAdActivity.this, AdvertisementMenuActivity.class));
                                //dialog.dismiss();
                               /* startActivity(new Intent(UploadImageForAdActivity.this, EstimationPriceActivity.class)
                                        .putExtra("data", (Serializable) availableSlotModel));
                                UploadImageForAdActivity.this.finish();*/
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAd();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    private void alertDelete(){
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(UploadImageForAdActivity.this);
        builder.setMessage("Do you want to Book this Ad?");
        builder.setTitle("");
        builder.setCancelable(false);
        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                dialog.dismiss();
                               /* startActivity(new Intent(UploadImageForAdActivity.this, EstimationPriceActivity.class)
                                        .putExtra("data", (Serializable) availableSlotModel));
                                UploadImageForAdActivity.this.finish();*/
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAd();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteAd() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<Mailmodule> call = customer_interface.DeleteAdvertisement("bearer " + Token, adMainId);
        call.enqueue(new Callback<Mailmodule>() {
            @Override
            public void onResponse(Call<Mailmodule> call, Response<Mailmodule> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        Toast.makeText(UploadImageForAdActivity.this, "Advertisement Deleted Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UploadImageForAdActivity.this, AddRequestUserActivity.class));
                        break;
                    case 404:
                        Toast.makeText(UploadImageForAdActivity.this, "Not found", Toast.LENGTH_LONG).show();
                        break;
                    case 500:
                        CustomToast.showToast(UploadImageForAdActivity.this, "Server Error");
                        break;
                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(UploadImageForAdActivity.this);
                        break;
                }

            }

            @Override
            public void onFailure(Call<Mailmodule> call, Throwable t) {

            }
        });
    }
}