package com.mwbtech.dealer_register.Dashboard.Advertisement.QuoteAd;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.PojoClass.Mailmodule;
import com.mwbtech.dealer_register.PojoClass.NewAdvertisementModule;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;

import java.io.IOException;
import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstimationPriceActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvAdName, tvAdType, tvAdArea, tvAdCreated, tvAdExpire, tvDaysCount, tvEstimatedPrice, tvTxtCGST, tvTxtSGST, tvTxtIGST, tvValueCGST, tvValueSGST, tvValueIGST,
            tvTxtGST, tvValueTotalGST, tvDiscPrice, tvTotalPrice;
    TextView tvInvoiceReportDownload;

    double TotalPrice, DiscountPrice, EstimatedPrice, valueCGSTPer, valueSGSTPer, valueIGSTPer, valueCGST, valueSGST, valueIGST, TotalGST, TotalGSTPer;
    int adMainId, TotalDays;
    String advertisementName, advertisementType, advertisementArea, createdDate, ExpireDate,invoiceURL;
    Customer_Interface customer_interface;
    String Token;
    AlertDialog dialog;
    Button proceedPaymentBtn,cancelBtn;
    AppCompatImageView imgBack;
    PrefManager prefManager;
    //AvailableSlotModel availableSlotModel;
    NewAdvertisementModule availableSlotModel;
    LinearLayout llIGSTLayout, llSGSTLayout, llCGSTLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimation_price);

        initializeViews();
        initializeSharedData();
        setData();
        initializeClickEvent();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
    }


    private void initializeViews() {
        tvAdName = findViewById(R.id.adName);
        tvAdType = findViewById(R.id.adType);
        tvAdArea = findViewById(R.id.adArea);
        tvAdCreated = findViewById(R.id.adCreatedDate);
        tvAdExpire = findViewById(R.id.adExpire);
        tvDaysCount = findViewById(R.id.daysCount);
        tvEstimatedPrice = findViewById(R.id.estimated_price);
        tvTxtCGST = findViewById(R.id.txtCGST);
        tvValueCGST = findViewById(R.id.tax_cgst);
        tvTxtSGST = findViewById(R.id.txtSGST);
        tvValueSGST = findViewById(R.id.tax_sgst);
        tvTxtIGST = findViewById(R.id.txtIGST);
        tvValueIGST = findViewById(R.id.tax_igst);
        tvTxtGST = findViewById(R.id.txtTotalGST);
        tvValueTotalGST = findViewById(R.id.tax_gst);
        tvDiscPrice = findViewById(R.id.disc_price);
        tvTotalPrice = findViewById(R.id.total_price);

        llIGSTLayout = findViewById(R.id.ll_igst);
        llCGSTLayout = findViewById(R.id.ll_cgst);
        llSGSTLayout = findViewById(R.id.ll_sgst);

        tvInvoiceReportDownload = findViewById(R.id.tvReport);

        proceedPaymentBtn = findViewById(R.id.bt_payment);
        cancelBtn=findViewById(R.id.bt_cancel);
        imgBack=findViewById(R.id.back);
        prefManager = new PrefManager(this);
        Token = prefManager.getToken().get(TOKEN);

//        ActionBar actionBar=getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
    }

    private void initializeSharedData() {
            availableSlotModel = (NewAdvertisementModule) getIntent().getSerializableExtra("data");
            adMainId = availableSlotModel.getAdvertisementMainID();
            advertisementName = availableSlotModel.getAdvertisementName();
            advertisementType = availableSlotModel.getAdvertisementType();
            advertisementArea = availableSlotModel.getAdvertisementArea();
            createdDate = availableSlotModel.getCreatedDateStr();
            ExpireDate = availableSlotModel.getBookingExpiryDateStr();
            valueCGSTPer = availableSlotModel.getcGSTPer();
            valueSGSTPer = availableSlotModel.getsGSTPer();
            valueIGSTPer = availableSlotModel.getiGSTPer();
            valueCGST = availableSlotModel.getcGSTAmount();
            valueSGST = availableSlotModel.getsGSTAmount();
            valueIGST = availableSlotModel.getiGSTAmount();
            TotalGST = availableSlotModel.getTaxAmount();
            TotalDays = availableSlotModel.getTotalDays();
            TotalPrice = availableSlotModel.getFinalPrice();
            EstimatedPrice = availableSlotModel.getAdTotalPrice();
            DiscountPrice = availableSlotModel.getTotalDiscountAmount();
            invoiceURL = availableSlotModel.getInvoiceUrl();
    }

    private void setData() {
        tvAdName.setText("" + advertisementName);
        tvAdArea.setText("" + advertisementArea);
        tvAdType.setText("" + advertisementType);
        tvAdCreated.setText("" + createdDate);
        tvAdExpire.setText("" + ExpireDate);
        tvDaysCount.setText("" + TotalDays);
        tvEstimatedPrice.setText("" + EstimatedPrice);

        if (valueIGST != 0) {
            llIGSTLayout.setVisibility(View.VISIBLE);
            llCGSTLayout.setVisibility(View.GONE);
            llSGSTLayout.setVisibility(View.GONE);
            tvTxtIGST.setText("IGST " + "(" + valueIGSTPer + " % " + ")");
            tvValueIGST.setText("" + valueIGST);
            tvTxtGST.setText("Total GST" + "(" + valueIGSTPer + " % " + ")");
        } else {
            llIGSTLayout.setVisibility(View.GONE);
            llCGSTLayout.setVisibility(View.VISIBLE);
            llSGSTLayout.setVisibility(View.VISIBLE);
            tvTxtCGST.setText("CGST " + "(" + valueCGSTPer + " % " + ")");
            tvValueCGST.setText("" + valueCGST);
            tvTxtSGST.setText("SGST " + "(" + valueSGSTPer + " % " + ")");
            tvValueSGST.setText("" + valueSGST);
            TotalGSTPer = valueCGSTPer + valueSGSTPer;
            tvTxtGST.setText("Total GST" + "(" + TotalGSTPer + " % " + ")");
        }
        tvValueTotalGST.setText("" + TotalGST);
        tvDiscPrice.setText("" + DiscountPrice);
        tvTotalPrice.setText("" + TotalPrice);
    }

    private void initializeClickEvent() {
        tvInvoiceReportDownload.setOnClickListener(this);
        proceedPaymentBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        imgBack.setOnClickListener(this);
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
                        Toast.makeText(EstimationPriceActivity.this, "Advertisement Deleted Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(EstimationPriceActivity.this, AddRequestUserActivity.class));
                        break;
                    case 404:
                        Toast.makeText(EstimationPriceActivity.this, "Not found", Toast.LENGTH_LONG).show();
                        break;

                    case 500:
                        CustomToast.showToast(EstimationPriceActivity.this, "Server Error");
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(EstimationPriceActivity.this);
                        break;
                }

            }

            @Override
            public void onFailure(Call<Mailmodule> call, Throwable t) {

            }

        });

    }
    private void downloadInvoice(int adMainId) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(EstimationPriceActivity.this,"Please wait");
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
                        CustomToast.showToast(EstimationPriceActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(EstimationPriceActivity.this);
                        break;
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(EstimationPriceActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EstimationPriceActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvReport:
                if (ConnectivityReceiver.isConnected()) {
                    //downloadInvoice(adMainId);
                    //String url="https://wbtechindia.com/apps/kalpavrikshadev/api/index/15";
                    Log.e("invoiceUrl....",invoiceURL);
                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                    pdfIntent.setDataAndType(Uri.parse(invoiceURL), "application/pdf");
                    startActivity(pdfIntent);
                } else {
                    Toast.makeText(this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_payment:
                startActivity(new Intent(EstimationPriceActivity.this, UploadImageForAdActivity.class)
                        .putExtra("adMainId", adMainId)
                        .putExtra("adType", advertisementType)
                        .putExtra("adname",advertisementName)
                        .putExtra("data", (Serializable) availableSlotModel));
                EstimationPriceActivity.this.finish();
                break;

            case R.id.back:
                onBackPressed();
                break;

            case R.id.bt_cancel:
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(EstimationPriceActivity.this);
                builder.setMessage("Do you want to cancel the Ad?");
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
                                        deleteAd();
                                    }
                                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
    }
    @Override
    public void onBackPressed() {
       /* super.onBackPressed();
        Intent intent = new Intent(this, AddRequestUserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        EstimationPriceActivity.this.finish();*/
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(EstimationPriceActivity.this);
        builder.setMessage("Do you want to Book this Ad?");
        builder.setTitle("");
        builder.setCancelable(false);
        builder
                .setPositiveButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                deleteAd();
                            }
                        })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(EstimationPriceActivity.this);
                builder.setMessage("Do you want to Book this Ad?");
                builder.setTitle("");
                builder.setCancelable(false);
                builder
                        .setPositiveButton(
                                "No",
                                new DialogInterface
                                        .OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which)
                                    {
                                        deleteAd();
                                    }
                                })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
               /* onBackPressed();
                finish();*/
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.delete)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code

                        dialog.dismiss();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }
}