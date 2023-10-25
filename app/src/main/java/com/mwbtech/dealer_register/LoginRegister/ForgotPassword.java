package com.mwbtech.dealer_register.LoginRegister;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mukesh.OtpView;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;

public class ForgotPassword extends AppCompatActivity {
    String OtpForgot, OtpMobile, Token;
    TextView resendOtpTv, otpTimerTv;
    FrameLayout resendOtpLayout;
    private int counter = 60;
    private CountDownTimer otpExpiryTimer;

    OtpView otpView;
    ImageView imgBack;
    Button submit;
    Customer_Interface customer_interface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password2);

        imgBack = findViewById(R.id.img_nav_back);
        otpView = findViewById(R.id.otp_view);
        resendOtpTv = findViewById(R.id.resend_otp_text_view);
        otpTimerTv = findViewById(R.id.resend_otp_timer_text_view);
        resendOtpLayout = findViewById(R.id.layout_resend_otp);
        submit = findViewById(R.id.verify_button);

        PrefManager prefManager = new PrefManager(this);
        Token = prefManager.getToken().get(TOKEN);
        Intent i = getIntent();
        OtpForgot = (String) i.getSerializableExtra("OtpForgot");
        OtpMobile = (String) i.getSerializableExtra("OtpMobile");
        otpView.setOtpCompletionListener(otp -> OtpMobile = otp);

        resendOtpTv.setOnClickListener(v -> startOtpTimer());

        submit.setOnClickListener(v -> {
            Toast.makeText(ForgotPassword.this, "Password changed successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
            startActivity(intent);
            ForgotPassword.this.finish();


            /*if (otp.getText().toString().trim().equals("")) {
                Toast.makeText(ForgotPassword.this, "Please enter valid otp", Toast.LENGTH_LONG).show();
            } else if (psd.getText().toString().trim().equals("")) {
                Toast.makeText(ForgotPassword.this, "Please enter password", Toast.LENGTH_LONG).show();
            } else if (cpsd.getText().toString().trim().equals("")) {
                Toast.makeText(ForgotPassword.this, "Please enter confirm password", Toast.LENGTH_LONG).show();
            } else {
                if (psd.getText().toString().trim().equals(cpsd.getText().toString().trim())) {*/
//                    GetOtpForgotPassword();
//                } else {
//
//                    Toast.makeText(ForgotPassword.this, "Password and confirm password not matching", Toast.LENGTH_LONG).show();
//
//                }
//
//            }
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

}