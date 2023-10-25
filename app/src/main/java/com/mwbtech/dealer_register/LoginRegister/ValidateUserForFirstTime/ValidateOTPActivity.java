package com.mwbtech.dealer_register.LoginRegister.ValidateUserForFirstTime;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.PojoClass.AccountVerify;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValidateOTPActivity extends AppCompatActivity implements View.OnClickListener {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^" +"(?=.*[0-9])"+ ".{10}" + "$");
    EditText EdMobileNumber,EdOTP;
    Button BtnVerifyOTP,BtnGetOTP,BtnResendOTP;
    String System_ID;
    Customer_Interface customer_interface;
    RelativeLayout OTPlayout;
    String ValidNumber = "^[7-9][0-9]{9}$";
    int CustID;
    String MobileNUmber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_o_t_p);

        initializeViews();
        initializeAPI();
        GetDeviceMACID();
        Log.e("System","ID....."+System_ID);
        //GetUserDetails();
        initializeClickEvents();

    }

    private void initializeAPI() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
    }


    private void initializeViews() {
        EdMobileNumber = findViewById(R.id.MobileNumber);
        EdOTP = findViewById(R.id.OTP);
        BtnGetOTP = findViewById(R.id.getOTP);
        BtnVerifyOTP = findViewById(R.id.Verify_OTP);
        BtnResendOTP = findViewById(R.id.Resend_OTP);
        OTPlayout = findViewById(R.id.layout);

        EdOTP.setVisibility(View.GONE);
        BtnResendOTP.setVisibility(View.GONE);
    }

    private void initializeClickEvents() {
        BtnGetOTP.setOnClickListener(this);
        BtnResendOTP.setOnClickListener(this);
        BtnVerifyOTP.setOnClickListener(this);
    }

    private void GetDeviceMACID() {
        try {
            List<NetworkInterface> networkInterfaceList = Collections.list(NetworkInterface.getNetworkInterfaces());

            String stringMac = "";

            for (NetworkInterface networkInterface:networkInterfaceList){
                if (networkInterface.getName().equalsIgnoreCase("wlan0")){
                    for (int i=0; i<networkInterface.getHardwareAddress().length;i++){
                        String stringMacByte = Integer.toHexString(networkInterface.getHardwareAddress()[i] & 0xFF);

                        if (stringMacByte.length() == 1){
                            stringMacByte = "0" + stringMacByte;
                        }

                        //stringMac = stringMac + stringMacByte.toUpperCase() + ":";
                        stringMac = stringMac + stringMacByte.toUpperCase();
                    }
                    break;
                }
            }
            System_ID=stringMac;
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void ResendTimerStart() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BtnResendOTP.setVisibility(View.VISIBLE);
            }
        }, 30000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.getOTP:
                if (!ValidateMobileNumber())
                {
                    return;
                }else
                {
                    MobileNUmber = EdMobileNumber.getText().toString();
                    GetOTPFromServer(MobileNUmber);
                }
                break;

            case R.id.Resend_OTP:
                ResendOTP();
                BtnResendOTP.setVisibility(View.GONE);
                break;

            case R.id.Verify_OTP:
                if (!TextUtils.isEmpty(EdOTP.getText().toString()))
                {
                    String OTP = EdOTP.getText().toString();
                    ValidateOTP(CustID,MobileNUmber,OTP);
                }else
                {
                    Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }





    private void GetOTPFromServer(String mobileNUmber) {
        ProgressDialog progressDialog = ShowProgressDialog.createProgressDialog(this);
        Call<AccountVerify> call = customer_interface.GetOTP(System_ID,mobileNUmber);
        call.enqueue(new Callback<AccountVerify>() {
            @Override
            public void onResponse(Call<AccountVerify> call, Response<AccountVerify> response) {
                int status_code = response.code();
                Log.e("send otp Status","code....."+status_code);
                switch (status_code)
                {
                    case 200:
                        progressDialog.dismiss();
                        AccountVerify accountVerify = response.body();
                        Log.e("Response","in Get OTP...."+accountVerify.toString());
                        CustID = accountVerify.getCustID();
                        Log.e("CustID","........."+CustID);
                        EdOTP.setVisibility(View.VISIBLE);
                        EdMobileNumber.setEnabled(false);
                        BtnGetOTP.setVisibility(View.GONE);
                        BtnVerifyOTP.setVisibility(View.VISIBLE);
                        EdOTP.requestFocus();
                        ResendTimerStart();
                        break;
                    case 500:
                        progressDialog.dismiss();
                        Toast.makeText(ValidateOTPActivity.this, "Server Error..", Toast.LENGTH_SHORT).show();
                        break;

                    case 409:
                        progressDialog.dismiss();
                        // if mobile number already exists
                        break;


                }
            }

            @Override
            public void onFailure(Call<AccountVerify> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ValidateOTPActivity.this, "Time out..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ResendOTP() {
        ProgressDialog progressDialog = ShowProgressDialog.createProgressDialog(this);
        Log.e("CustID","in resend........."+CustID);
        Log.e("Mobile","........."+MobileNUmber);
        Call<AccountVerify> call = customer_interface.ResendOTP(System_ID,MobileNUmber);
        call.enqueue(new Callback<AccountVerify>() {
            @Override
            public void onResponse(Call<AccountVerify> call, Response<AccountVerify> response) {
                int status_code = response.code();
                Log.e("Response","code in resend...."+response.code());
                switch (status_code)
                {
                    case 200:
                        progressDialog.dismiss();
                        AccountVerify accountVerify = response.body();
                        Log.e("Response","in Resend OTP...."+accountVerify.toString());
                        BtnResendOTP.setVisibility(View.GONE);
                        ResendTimerStart();
                        break;
                    case 500:
                        progressDialog.dismiss();
                        Toast.makeText(ValidateOTPActivity.this, "Server Error..", Toast.LENGTH_SHORT).show();
                        break;

                }
            }

            @Override
            public void onFailure(Call<AccountVerify> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ValidateOTPActivity.this, "Time Out", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ValidateOTP(int custID, String mobileNUmber, String OTP) {
        Log.e("CustID","in resend........."+custID);
        Log.e("Mobile","........."+mobileNUmber);
        Log.e("OTP","........."+OTP);
        ProgressDialog progressDialog = ShowProgressDialog.createProgressDialog(this);
        Call<AccountVerify> call = customer_interface.ValidateOTP(custID,mobileNUmber,OTP);
        call.enqueue(new Callback<AccountVerify>() {
            @Override
            public void onResponse(Call<AccountVerify> call, Response<AccountVerify> response) {
                int status_code = response.code();
                switch (status_code)
                {
                    case 200:
                        progressDialog.dismiss();
                        AccountVerify accountVerify = response.body();
                        Log.e("Response","in validate otp...."+accountVerify.toString());
                        //navigate to question and answer
                        //send system ID
                        Intent intent1 = new Intent(ValidateOTPActivity.this, QuestionAnswerActivity.class);
                        intent1.putExtra("SystemID",System_ID);
                        intent1.putExtra("MobileNumber",MobileNUmber);
                        startActivity(intent1);
                        ValidateOTPActivity.this.finish();
                        break;
                    case 500:
                        progressDialog.dismiss();
                        Toast.makeText(ValidateOTPActivity.this, "Server Error..", Toast.LENGTH_SHORT).show();
                        break;
                    case 404:
                        progressDialog.dismiss();
                        CustomToast.showToast(ValidateOTPActivity.this,"Problem in sending OTP, please try again.");
                        break;
                }

            }

            @Override
            public void onFailure(Call<AccountVerify> call, Throwable t) {
                progressDialog.dismiss();
                CustomToast.showToast(ValidateOTPActivity.this,"Time out, please try again.");
            }
        });
    }

    private boolean ValidateMobileNumber()
    {
        String NumberInput = EdMobileNumber.getText().toString();

        if (!PHONE_PATTERN.matcher(NumberInput).matches()) {
            EdMobileNumber.setError("please enter valid mobile number");
            return false;
        }else if (NumberInput.isEmpty())
        {
            EdMobileNumber.setError("Please enter valid mobile number");
            return false;
        }else {

            if(!NumberInput.matches(ValidNumber)){
                EdMobileNumber.setError("please enter valid mobile number");
                return  false;
            } else {
                return true;
            }

        }
    }

}