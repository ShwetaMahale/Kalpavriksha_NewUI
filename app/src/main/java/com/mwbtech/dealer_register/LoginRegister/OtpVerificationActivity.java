package com.mwbtech.dealer_register.LoginRegister;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.mukesh.OtpView;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.LoginRegister.ValidateUserForFirstTime.ChangePasswordActivity;
import com.mwbtech.dealer_register.PojoClass.AccountVerify;
import com.mwbtech.dealer_register.PojoClass.OTPApprove;
import com.mwbtech.dealer_register.PojoClass.OTPValidation;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.ApiUtity;
import com.mwbtech.dealer_register.Utils.ChangePasswordRequest;
import com.mwbtech.dealer_register.Utils.CommonUtils;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.mwbtech.dealer_register.databinding.ActivityOtpVerificationBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerificationActivity extends AppCompatActivity {
    String OtpForgot, OtpMobile, Token;
    TextView resendOtpTv, otpTimerTv, phoneNumberTv;
    LinearLayoutCompat resendOtpLayout;
    private int counter = 60;
    private CountDownTimer otpExpiryTimer;
    private AlertDialog registrationCompletedDialog;
    private boolean isCheckMailDialogShown;

    OtpView otpView;
    private Context context;
    ImageView imgBack;
    Button submit;
    Customer_Interface customer_interface;
    private String otp = "";
    private String phoneNmber;
    private EditText passwod, cPass;
    private Button done;
    boolean isShowing = false, cIsShowing;
    private ImageView eye, c_eye;
    private ActivityOtpVerificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp_verification);
        context = this;
        imgBack = findViewById(R.id.img_nav_back);
        otpView = findViewById(R.id.otp_view);
        phoneNumberTv = findViewById(R.id.phone_number_text_view);
        resendOtpTv = findViewById(R.id.resend_otp_text_view);
        otpTimerTv = findViewById(R.id.resend_otp_timer_text_view);
        resendOtpLayout = findViewById(R.id.layout_resend_otp);
        passwod = findViewById(R.id.password);
        cPass = findViewById(R.id.c_password);
        eye = findViewById(R.id.eyeBtn);
        c_eye = findViewById(R.id.c_eyeBtn);
        done = findViewById(R.id.verify_button);
        submit = findViewById(R.id.submit_button);
        phoneNmber = getIntent().getStringExtra("NUMBER");
//        phoneNumberTv.setText(getString(R.string.format_code_sent_to) + " " + phoneNmber);
        otp = getIntent().getStringExtra("OTP");
        PrefManager prefManager = new PrefManager(this);
        Token = prefManager.getToken().get(TOKEN);
        Intent i = getIntent();
        OtpForgot = (String) i.getSerializableExtra("OtpForgot");
        OtpMobile = (String) i.getSerializableExtra("OtpMobile");
        otpView.setOtpCompletionListener(otp -> OtpMobile = otp);
        startTimer();
        binding.resendTv.setOnClickListener(v -> startOtpTimer());
        imgBack.setOnClickListener(view -> onBackPressed());
        submit.setOnClickListener(v -> {
            String otpTxt = otpView.getText().toString();
            if (otpTxt.trim().equals("")) {
                CommonUtils.showToast(this, "Please enter otp");
            } else if (otpTxt.length() < 4) {
                CommonUtils.showToast(this, "Please enter all digit of otp");

            } else {
                done.setEnabled(true);
                CommonUtils.showToast(context,"Otp verified successfully. Please reset password.");
                passwod.requestFocus();
                resendOtpLayout.setVisibility(View.GONE);
//                hitVerifyOtpApi(otpTxt);
            }
        });
        eye.setOnClickListener((v) -> showHidePassword());
        c_eye.setOnClickListener((v) -> showCHidePassword());
        done.setOnClickListener((v) -> verifyAndContinue());
    }

    private void hitVerifyOtpApi(String otpTxt) {

        ApiUtity.verifyForgotPassOtpApi(this, phoneNmber, otpTxt, new ApiUtity.APIResponseListener<OTPApprove>() {
            @Override
            public void onReceiveResponse(OTPApprove response) {
                done.setEnabled(true);
                CommonUtils.showToast(context,"Otp verified successfully");
                // showPhoneVerifiedDialog();

            }

            @Override
            public void onResponseFailed(String msg) {
                CommonUtils.showToast(context, msg);
            }
        });
    }

    private void showPhoneVerifiedDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View layoutView =
                LayoutInflater.from(this).inflate(R.layout.dialog_registration_completed, null);

        Button doneButton = layoutView.findViewById(R.id.kyc_button);
        TextView msg = layoutView.findViewById(R.id.succTv);
        msg.setVisibility(View.GONE);
        doneButton.setText("Change password");
        dialogBuilder.setView(layoutView);
        dialogBuilder.setCancelable(false);
        registrationCompletedDialog = dialogBuilder.create();

        registrationCompletedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        registrationCompletedDialog.show();
        isCheckMailDialogShown = true;
        doneButton.setOnClickListener(v -> {
            registrationCompletedDialog.dismiss();
            startActivity(new Intent(this, ChangePasswordActivity.class).putExtra("Mobile",phoneNmber));
        });
    }

    private void startOtpTimer() {
        if (otpTimerTv.getVisibility() == View.VISIBLE) {
            return;
        }
        if (otpExpiryTimer != null) {
            otpExpiryTimer = null;
        }
        hitResendOtpApi();

    }

    private void hitResendOtpApi() {
        ApiUtity.generateOtpApi(this, "1234567890", phoneNmber, "", new ApiUtity.APIResponseListener<AccountVerify>() {
            @Override
            public void onReceiveResponse(AccountVerify response) {
                String mask = phoneNmber.replaceAll("\\w(?=\\w{4})", "*");
                Toast.makeText(context, "Otp resent to " + mask, Toast.LENGTH_LONG).show();

                startTimer();
            }

            @Override
            public void onResponseFailed(String msg) {
                CommonUtils.showToast(context, msg);
            }
        });
    }

    private void startTimer() {
        binding.resendTv.setTextColor(getResources().getColor(R.color.gray_light));

        otpTimerTv.setVisibility(View.VISIBLE);
        counter = 60;

        otpExpiryTimer = new CountDownTimer(61000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                otpTimerTv.setText(getResources().getString(R.string.format_resend_otp_in_seconds, counter));
                counter--;
            }

            @Override
            public void onFinish() {
                otpTimerTv.setVisibility(View.GONE);
                binding.resendTv.setTextColor(getResources().getColor(R.color.red));
            }
        }.start();
    }


    private void showHidePassword() {
        if (isShowing) {
            passwod.setTransformationMethod(new PasswordTransformationMethod());
            passwod.setSelection(passwod.getText().toString().length());
            eye.setColorFilter(ContextCompat.getColor(this, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);

            isShowing = false;
        } else {
            passwod.setTransformationMethod(null);
            passwod.setSelection(passwod.getText().toString().length());
            eye.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);

            isShowing = true;
        }
    }

    private void showCHidePassword() {
        if (cIsShowing) {
            cPass.setTransformationMethod(new PasswordTransformationMethod());
            cPass.setSelection(cPass.getText().toString().length());
            c_eye.setColorFilter(ContextCompat.getColor(this, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);

            cIsShowing = false;
        } else {
            cPass.setTransformationMethod(null);
            cPass.setSelection(cPass.getText().toString().length());
            c_eye.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);

            cIsShowing = true;
        }
    }

    private void verifyAndContinue() {
        if (passwod.getText().toString().isEmpty()) {
            showToast("Please enter password");
        } else if (cPass.getText().toString().isEmpty()) {
            showToast("Please enter confirm password");

        } else if (!passwod.getText().toString().equals(cPass.getText().toString())) {
            showToast("Password and confirm password must be same");

        } else {
//            hitChangePasswordApi();
            GetOtpForgotPassword();
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void GetOtpForgotPassword() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<OTPValidation> call = customer_interface.ForgotRest_Password("bearer " + Token, phoneNmber, passwod.getText().toString().trim(),otpView.getText().toString().trim() ); //OtpForgot
        call.enqueue(new Callback<OTPValidation>() {
            @Override
            public void onResponse(Call<OTPValidation> call, Response<OTPValidation> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        Toast.makeText(context, "Password changed successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case 503:
                    case 404:
                        Toast.makeText(context, "Number not registered", Toast.LENGTH_LONG).show();
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(context);
                        break;

                }
            }

            @Override
            public void onFailure(Call<OTPValidation> call, Throwable t) {
//                CommonUtils.showToast(context, call.toString());
            }
        });

    }

    private void hitChangePasswordApi() {


        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setNewPassowrd(cPass.getText().toString());
        changePasswordRequest.setMobile(phoneNmber);

        ApiUtity.callChangePasswordApi(context, changePasswordRequest, ",", new ApiUtity.APIResponseListener<Object>() {
            @Override
            public void onReceiveResponse(Object response) {
                startActivity(new Intent(context, LoginActivity.class));
            }

            @Override
            public void onResponseFailed(String msg) {
                CommonUtils.showToast(context, msg);

            }
        });

    }
}