package com.mwbtech.dealer_register.LoginRegister;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.mwbtech.dealer_register.PojoClass.AccountVerify;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.ApiUtity;
import com.mwbtech.dealer_register.Utils.CommonUtils;

public class ContinueWithPhone extends AppCompatActivity {
    String Token;
    AppCompatEditText phoneNumberTv;

    ImageView imgBack;
    Button submit;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_verification_screen);
        context = ContinueWithPhone.this;
        imgBack = findViewById(R.id.img_nav_back);
        phoneNumberTv = findViewById(R.id.phoneEv);
        phoneNumberTv.setText(getIntent().getStringExtra("PHONE"));
        submit = findViewById(R.id.ctnBtn);

        PrefManager prefManager = new PrefManager(this);
        Token = prefManager.getToken().get(TOKEN);

        submit.setOnClickListener(v -> {
            Intent intent = new Intent(context, VerifyPhone.class).putExtra("NUMBER", phoneNumberTv.getText().toString());
            startActivity(intent);
//            generateOptApi();

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
        imgBack.setOnClickListener((v) -> {
            onBackPressed();
        });
    }

    private void generateOptApi() {

        ApiUtity.generateOtpApi(this, "1234567890", phoneNumberTv.getText().toString(), "", new ApiUtity.APIResponseListener<AccountVerify>() {
            @Override
            public void onReceiveResponse(AccountVerify response) {
                Intent intent = new Intent(context, VerifyPhone.class).putExtra("NUMBER", phoneNumberTv.getText().toString());
                startActivity(intent);
            }

            @Override
            public void onResponseFailed(String msg) {
                CommonUtils.showToast(context, msg);
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

}