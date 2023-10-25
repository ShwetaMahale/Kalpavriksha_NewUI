package com.mwbtech.dealer_register.Dashboard.Advertisement;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cashfree.pg.CFPaymentService;
import com.cashfree.pg.ui.gpay.GooglePayStatusListener;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIClient.Payment_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Dashboard.Advertisement.MyAd.ShowAdsActivity;
import com.mwbtech.dealer_register.PojoClass.PaymentToken;
import com.mwbtech.dealer_register.PojoClass.PostPayment;
import com.mwbtech.dealer_register.PojoClass.TokenCf;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Payment extends AppCompatActivity  {

    enum SeamlessMode {
        CARD, WALLET, NET_BANKING, UPI_COLLECT, PAY_PAL
    }

    SeamlessMode currentMode = SeamlessMode.CARD;

    private static final String TAG = "Payment";
    Customer_Interface customer_interface;
    PaymentToken paymentToken;
    String Token,TokenJBN;
    PrefManager prefManager;
    String adName,emailid,mobileNumber,custName,orderamountStr;
    int orderamount,orderamountint,CustID,adID;
    Button web;
    TextView tvadName,tvtotalAmount,tvdiscAmount,tvgstAmount,tvgrandTotal,tvAdText;
    Double orderAmountDoub;
    ImageView ivAdImage, back;
    String imageUrl,adType,textContent ;
    LinearLayout llimage,lltext;

    Double taxAmount,adAmount,totalDiscount,totalAmount;

    String  paymentMode,orderId,txTime,referenceId,type,txMsg,signature,orderAmount,txStatus,adIDstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        web=findViewById(R.id.web);
        tvadName=findViewById(R.id.advertiseName);
        tvtotalAmount=findViewById(R.id.talAmount);
        tvdiscAmount=findViewById(R.id.discAmount);
        tvgstAmount=findViewById(R.id.gstAmount);
        tvgrandTotal=findViewById(R.id.grandTotal);
        ivAdImage=findViewById(R.id.ivImage);
        tvAdText = findViewById(R.id.tvTextScrollAd);
        llimage=findViewById(R.id.llImage);
        lltext = findViewById(R.id.llText);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            onBackPressed();
        });

        initializeSharedData();
        tvadName.setText(adName);
        tvtotalAmount.setText(adAmount.toString());
        tvdiscAmount.setText(totalDiscount.toString());
        tvgstAmount.setText(taxAmount.toString());
        tvgrandTotal.setText(totalAmount.toString());

        llimage.setVisibility(View.VISIBLE);
        if(imageUrl!=null){
            lltext.setVisibility(View.GONE);
            Glide.with(Payment.this)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivAdImage);
        }
        else{
            llimage.setVisibility(View.GONE);
            tvAdText.setText("" + textContent);

        }

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void initializeSharedData() {
        prefManager = new PrefManager(this);
        TokenJBN = prefManager.getToken().get(TOKEN);
        CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            adID= getIntent().getExtras().getInt("adMainId");
            adName=bundle.getString("advertisemntName");
            custName=bundle.getString("customerName");
            emailid=bundle.getString("Emailid");
            if(emailid.isEmpty())
                emailid="kalpavriksha.support@wbtechindia.com";
            mobileNumber=bundle.getString("mobileNumber");
            taxAmount=bundle.getDouble("TaxTotal");
            adAmount=bundle.getDouble("AdTotal");
            totalDiscount=bundle.getDouble("DisTotal");
            totalAmount=bundle.getDouble("FinalTotal");
            //for dynamic amount
            orderamountint=totalAmount.intValue();
            //for static amount
            //orderamountint=1;
            orderamountStr=String.valueOf(totalAmount);
            imageUrl=bundle.getString("imgUrl");
            adType=bundle.getString("adtype");
            textContent=bundle.getString("adtext");
        }
        else{
            //
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "API Response : ");
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
                for (String key : bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        Log.d(TAG, key + " : " + bundle.getString(key));
                        paymentMode = bundle.getString("paymentMode");
                        orderId = bundle.getString("orderId");
                        txTime = bundle.getString("txTime");
                        referenceId = bundle.getString("referenceId");
                        type = bundle.getString("type");
                        txMsg = bundle.getString("txMsg");
                        signature = (String) bundle.get("signature");
                        //orderAmount = (String) bundle.get("orderAmount");
                        //orderAmountDoub = Double.parseDouble(orderAmount);
                        txStatus = (String) bundle.get("txStatus");
                    }
                }
                        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
                        PostPayment postPayment = new PostPayment(orderId,orderamountint,txMsg,txStatus,paymentMode,signature,type,txTime,referenceId,CustID,adID);
                        //for dynamic amount
                        Gson gson=new Gson();
                        Log.e("json...Payment...",gson.toJson(postPayment));
                        Call<PostPayment> call = customer_interface.postAdPayments("bearer "+TokenJBN,postPayment);
                        call.enqueue(new Callback<PostPayment>() {
                            @Override
                            public void onResponse(Call<PostPayment> call, Response<PostPayment> response) {
                                int statusCode = response.code();
                                switch (statusCode) {
                                    case 200:
                                        PostPayment payment=response.body();
                                        if(payment.getDisplayMessage().equals("Success..")){
                                            CustomToast.showToast(Payment.this,"Payment Successful");
                                            startActivity(new Intent(Payment.this, ShowAdsActivity.class));
                                        }
                                        else
                                        {
                                            CustomToast.showToast(Payment.this,"Payment Unsuccessful");
                                            startActivity(new Intent(Payment.this, ShowAdsActivity.class));
                                        }
                                        break;

                                    case 409:
                                        CustomToast.showToast(Payment.this,"Error");
                                        break;

                                    case 307:
                                        break;

                                    case 500:
                                        break;
                                }
                            }
                            @Override
                            public void onFailure(Call<PostPayment> call, Throwable t) {
                                if (t instanceof IOException) {
                                    Toast.makeText( Payment.this, "Time out..", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    CustomToast.showToast( Payment.this,"Bad response from server.. Try again later ");
                                }
                            }
                        });
                    }

    }
    public void onClick(View view) {
        String stage = "PROD";

        //Show the UI for doGPayPayment and phonePePayment only after checking if the apps are ready for payment
        if (view.getId() == R.id.phonePe_exists) {
            Toast.makeText(
                    Payment.this,
                    CFPaymentService.getCFPaymentServiceInstance().doesPhonePeExist(Payment.this, stage)+"",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (view.getId() == R.id.gpay_ready) {
            CFPaymentService.getCFPaymentServiceInstance().isGPayReadyForPayment(Payment.this, new GooglePayStatusListener() {
                @Override
                public void isReady() {
                    Toast.makeText(Payment.this, "Ready", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void isNotReady() {
                    Toast.makeText(Payment.this, "Not Ready", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        String appID="136824df211450cb5c4d84b2ed428631";
        String secretKey="a216d33a6088d824cd1a9398531373bb15fd18d5";
        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);
        switch (view.getId()) {
            case R.id.web: {
                customer_interface= Payment_Client.getToken().create(Customer_Interface.class);
                PaymentToken paymentToken = new PaymentToken(adName,orderamountint,"INR");//for dynamic amount
                Call<TokenCf> call = customer_interface.generateToken(""+appID,""+secretKey,paymentToken);
                call.enqueue(new Callback<TokenCf>() {
                    @Override
                    public void onResponse(Call<TokenCf> call, Response<TokenCf> response) {
                        int statusCode = response.code();
                        switch (statusCode) {
                            case 200:
                                TokenCf tokenCf = response.body();
                                Token = tokenCf.getCftoken();
                                Log.e("token....",Token);
                                cfPaymentService.doPayment(Payment.this, getInputParams(), Token, stage, "#784BD2", "#FFFFFF", false);
                                break;
                        }
                    }
                    @Override
                    public void onFailure(Call<TokenCf> call, Throwable t) {
                    }
                });
                 break;
            }
        }
    }
    private Map<String, String> getInputParams() {
        String appId = "136824df211450cb5c4d84b2ed428631";
        String orderId = adName;
        String orderAmount =String.valueOf(orderamountint);   //for dynamic amount value
        //String orderAmount =String.valueOf(1);   //for static amount value
        String orderNote = "Production";
        String customerName=custName;
        String customerPhone=mobileNumber;
        String customerEmail=emailid;
        //String customerEmail="kalpavriksha.support@wbtechindia.com";
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_ORDER_CURRENCY, "INR");
        return params;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ShowAdsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Payment.this.finish();
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
}