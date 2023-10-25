package com.mwbtech.dealer_register.LoginRegister;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.mukesh.OtpView;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.PojoClass.AccountVerify;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.PojoClass.ErrorBodyResponse;
import com.mwbtech.dealer_register.PojoClass.OTPVerificationResponse;
import com.mwbtech.dealer_register.PojoClass.RegisterAccount;
import com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.ApiUtity;
import com.mwbtech.dealer_register.Utils.CommonUtils;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.databinding.DialogRegistrationCompletedBinding;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPhone extends AppCompatActivity {
    String OtpForgot, OtpMobile, Token,deviceID,eduserName,edEmailId,edPincode,edTermsVersion,edPassword,edReferralCode;

    PrefManager prefManager;
    Boolean isTerms;

    String OTPforChecking;
    TextView resendOtpTv, otpTimerTv, phoneNumberTv;
    FrameLayout resendOtpLayout;
    OtpView otpView;
    ImageView imgBack;
    Button submit;
    Customer_Interface customer_interface;
    private int counter = 60;
    private CountDownTimer otpExpiryTimer;
    private AlertDialog registrationCompletedDialog;
    private boolean isCheckMailDialogShown;
    private String mobileNumber;
    private RegisterAccount _registerAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        imgBack = findViewById(R.id.img_nav_back);
        otpView = findViewById(R.id.otp_view);
        phoneNumberTv = findViewById(R.id.phone_number_text_view);
        resendOtpTv = findViewById(R.id.resend_otp_text_view);
        otpTimerTv = findViewById(R.id.resend_otp_timer_text_view);
        resendOtpLayout = findViewById(R.id.layout_resend_otp);
        submit = findViewById(R.id.submit_button);
        mobileNumber = getIntent().getStringExtra("NUMBER");
        deviceID = getIntent().getStringExtra("DEVICEID");
        eduserName = getIntent().getStringExtra("CUSTName");
        edEmailId = getIntent().getStringExtra("EmailID");
        edPincode= getIntent().getStringExtra("PINCODE");
        edPassword = getIntent().getStringExtra("PassWord");
        edReferralCode = getIntent().getStringExtra("ReferralCode");
        edTermsVersion = getIntent().getStringExtra("termVersion");
        isTerms = getIntent().getBooleanExtra("isTerms",true);
        OTPforChecking = getIntent().getStringExtra("OTP");
        phoneNumberTv.setText(getString(R.string.format_code_sent_to) + " ******" + mobileNumber.substring(6));
        prefManager = new PrefManager(this);
        Token = prefManager.getToken().get(TOKEN);
        Intent i = getIntent();
        OtpForgot = (String) i.getSerializableExtra("OtpForgot");
        OtpMobile = (String) i.getSerializableExtra("OtpMobile");
        otpView.setOtpCompletionListener(otp -> OtpMobile = otp);
        //hitResendOtpApi();
        startOtpTimer();
        resendOtpTv.setOnClickListener(v -> {
            startOtpTimer();
            hitResendOtpApi();
            otpView.setText("");
        });

        imgBack.setOnClickListener((v) -> onBackPressed());

        submit.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            String otp = otpView.getText().toString();
            if (otp.length() < 4) {
                CommonUtils.showToast(this, "Please enter 4 digit of otp");
            } else if (otp.isEmpty()) {
                CommonUtils.showToast(this, "Please enter otp");

            } else {
                //uncommented by Shweta
                if(otp.equals(OTPforChecking))
                    registerCustomerafterOTPValidation();
                else {
                    CommonUtils.showToast(VerifyPhone.this, "Please enter valid OTP");
                }
               //hitVerifyOtpApi(otp);


                //commented by Shweta
               /* startActivity(new Intent(VerifyPhone.this, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("isNewUser", true)
                );*/
            }

        });

    }

    private void registerCustomerafterOTPValidation(){
        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(VerifyPhone.this, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        RegisterAccount dealerRegister = new RegisterAccount(
                eduserName,edEmailId,mobileNumber,edPincode,edPassword,edReferralCode,isTerms,edTermsVersion
        );
        Call<DealerRegister> call = customer_interface.registerCustomerDetailsMethod(dealerRegister);
        call.enqueue(new Callback<DealerRegister>() {
            @Override
            public void onResponse(Call<DealerRegister> call, Response<DealerRegister> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressBar.dismiss();
                        DealerRegister dealerRegisterCust = response.body();
                        if (dealerRegisterCust == null) return;
                        prefManager.saveCustId(dealerRegisterCust.getUserID(), dealerRegisterCust.getPassword(), Integer.parseInt(dealerRegisterCust.getUserType()), dealerRegisterCust.getMobileNumber());
                        prefManager.saveCustPincode(dealerRegisterCust.getPincode());
                        prefManager.setSave_isBannerImages(false);
                         // need to show the success msg
                        showPhoneVerifiedDialog();
                        otpExpiryTimer.cancel();
                        resendOtpLayout.setVisibility(View.GONE);
                        resendOtpTv.setVisibility(View.GONE);
                        break;

                    case 409:
                        progressBar.dismiss();
                        CustomToast.showToast(VerifyPhone.this, "User Already Registered.");
                        break;

                    case 307:
                        progressBar.dismiss();
                        CustomToast.showToast(VerifyPhone.this, "Server is not responding." + response.code());
                        break;

                    case 500:
                        progressBar.dismiss();
                        CustomToast.showToast(VerifyPhone.this, "Server Error");
                        break;
                    default:
                        progressBar.dismiss();
                        ErrorBodyResponse errorBodyResponse = new ErrorBodyResponse();
                        try {
                            errorBodyResponse = new Gson().fromJson(response.errorBody().string(), ErrorBodyResponse.class);
                            CustomToast.showToast(VerifyPhone.this, errorBodyResponse.getDisplayMessage());

                        } catch (IOException e) {
                            e.printStackTrace();
                            CustomToast.showToast(VerifyPhone.this, errorBodyResponse.getDisplayMessage());
                        }

                }
            }

            @Override
            public void onFailure(Call<DealerRegister> call, Throwable t) {
                progressBar.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(VerifyPhone.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(VerifyPhone.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

   /* private void hitVerifyOtpApi(String otp) {
        ApiUtity.verifyOtpApi(this, mobileNumber, otp, new ApiUtity.APIResponseListener<OTPVerificationResponse>() {
            @Override
            public void onReceiveResponse(OTPVerificationResponse response) {
                showPhoneVerifiedDialog();

            }

            @Override
            public void onResponseFailed(String msg) {
                CommonUtils.showToast(VerifyPhone.this, msg);
            }
        });
    }*/


    private void showPhoneVerifiedDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View layoutView =
                LayoutInflater.from(this).inflate(R.layout.dialog_registration_completed, null);

        DialogRegistrationCompletedBinding binding;
        binding = DataBindingUtil.bind(layoutView);
        dialogBuilder.setView(binding.getRoot());
        dialogBuilder.setCancelable(false);
        registrationCompletedDialog = dialogBuilder.create();

        registrationCompletedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        registrationCompletedDialog.show();
        isCheckMailDialogShown = true;
        binding.kycButton.setOnClickListener(v -> {
            registrationCompletedDialog.dismiss();
            startActivity(new Intent(VerifyPhone.this, MainActivity.class));
            VerifyPhone.this.finish();
        });
        binding.ctnButton.setOnClickListener((v) -> {
            registrationCompletedDialog.dismiss();
            showNavigationDialog();
        });
        binding.professionalButton.setOnClickListener(v -> {
            registrationCompletedDialog.dismiss();
            startActivity(new Intent(VerifyPhone.this, MainActivity.class));
            VerifyPhone.this.finish();
        });
    }

    private void startOtpTimer() {
        if (otpExpiryTimer != null) {
            otpExpiryTimer = null;
        }

        resendOtpLayout.setVisibility(View.VISIBLE);
        resendOtpTv.setVisibility(View.GONE);
        counter = 60;

        otpExpiryTimer = new CountDownTimer(61000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                otpTimerTv.setText(getResources().getString(R.string.format_resend_otp_in_seconds, counter));
                counter--;
            }

            @Override
            public void onFinish() {
                resendOtpLayout.setVisibility(View.GONE);
                resendOtpTv.setVisibility(View.VISIBLE);
            }
        }.start();
    }

   /* private void sendOTPMethod(){
        if (!ConnectivityReceiver.isConnected()) {
            CommonUtils.showToast(this, "Sorry! Not connected to internet");
            return;
        }
        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(this, "Please wait");
        Customer_Interface customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<AccountVerify> call = customer_interface.ResendOTP(deviceId, request);
        call.enqueue(new Callback<AccountVerify>() {
            @Override
            public void onResponse(Call<AccountVerify> call, Response<AccountVerify> response) {
                int statusCode = response.code();
                Log.e("Status", "code...." + statusCode);
                Log.e("Token", ".........." + Token);
                progressBar.dismiss();
                switch (statusCode) {
                    case 200:
                        listener.onReceiveResponse(response.body());
                        break;
                    case 307:
                        listener.onResponseFailed("Server is not responding." + response.code());
                        break;
                    default:
                        ErrorBodyResponse errorBodyResponse = new ErrorBodyResponse();
                        try {
                            errorBodyResponse = new Gson().fromJson(response.errorBody().string(), ErrorBodyResponse.class);
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        }
                }
            }

            @Override
            public void onFailure(Call<AccountVerify> call, Throwable t) {
                progressBar.dismiss();
                listener.onResponseFailed(t.getLocalizedMessage());
                Log.e("failure ", "error..." + t.getMessage());
            }
        });
    }*/

    private void hitResendOtpApi() {
        //ApiUtity.generateOtpApi(this, "1234567890", mobileNumber, "", new ApiUtity.APIResponseListener<AccountVerify>() {
        //added by shweta to get the dynamic device id
        ApiUtity.generateOtpApi(this, deviceID, mobileNumber, "", new ApiUtity.APIResponseListener<AccountVerify>() {
            @Override
            public void onReceiveResponse(AccountVerify response) {
                String mask = mobileNumber.replaceAll("\\w(?=\\w{4})", "*");
                Toast.makeText(getApplicationContext(), "Otp is" + response.getSMSOTP(), Toast.LENGTH_LONG).show();
                Toast.makeText(VerifyPhone.this, "Otp resent to " + mask, Toast.LENGTH_LONG).show();
                OTPforChecking=response.getSMSOTP();
                Log.e("OTP Sent....",OTPforChecking);
            }

            @Override
            public void onResponseFailed(String msg) {
                Toast.makeText(VerifyPhone.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void GetOtpForgotPassword() {
        /*customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<OTPValidation> call = customer_interface.ForgotRest_Password("bearer " + Token, OtpMobile, psd.getText().toString().trim(), otp.getText().toString().trim()); //OtpForgot
        call.enqueue(new Callback<OTPValidation>() {
            @Override
            public void onResponse(Call<OTPValidation> call, Response<OTPValidation> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        Toast.makeText(ForgotPassword.this, "Password changed successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                        startActivity(intent);
                        ForgotPassword.this.finish();
                        break;
                    case 503:
                        Toast.makeText(ForgotPassword.this, "Number not registered", Toast.LENGTH_LONG).show();

                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(ForgotPassword.this);
                        break;


                    case 404:
                        Toast.makeText(ForgotPassword.this, "Incorrect OTP", Toast.LENGTH_LONG).show();

                        break;

                }
            }

            @Override
            public void onFailure(Call<OTPValidation> call, Throwable t) {

            }
        });*/

    }

    private void showNavigationDialog() {
        AlertDialog checkMailDialog;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
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
            startActivity(new Intent(VerifyPhone.this, TakeTourActivity.class));
            VerifyPhone.this.finish();
        });
        ctnButton.setOnClickListener(v -> {
            checkMailDialog.dismiss();
            startActivity(new Intent(VerifyPhone.this, DashboardActivity.class).putExtra("isNewUser", true));
            VerifyPhone.this.finish();
        });
    }
}