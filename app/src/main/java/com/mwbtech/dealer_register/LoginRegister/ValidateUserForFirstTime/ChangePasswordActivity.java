package com.mwbtech.dealer_register.LoginRegister.ValidateUserForFirstTime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.mwbtech.dealer_register.LoginRegister.LoginActivity;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.Utils.ApiUtity;
import com.mwbtech.dealer_register.Utils.ChangePasswordRequest;
import com.mwbtech.dealer_register.Utils.CommonUtils;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText passwod, cPass;
    private Button done;
    boolean isShowing = false, cIsShowing;
    private ImageView back, eye, c_eye;
    private String mobile;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getIds();
        mobile = getIntent().getStringExtra("Mobile");
        context = ChangePasswordActivity.this;
        clickListener();
    }

    private void clickListener() {
        back.setOnClickListener((v) -> onBackPressed());
        eye.setOnClickListener((v) -> showHidePassword());
        c_eye.setOnClickListener((v) -> showCHidePassword());
        done.setOnClickListener((v) -> verifyAndContinue());
    }

    private void verifyAndContinue() {
        if (passwod.getText().toString().isEmpty()) {
            showToast("Please enter password");
        } else if (cPass.getText().toString().isEmpty()) {
            showToast("Please enter confirm password");

        } else if (!passwod.getText().toString().equals(cPass.getText().toString())) {
            showToast("Password and confirm password must be same");

        } else {
            hitChangePasswordApi();
        }
    }

    private void hitChangePasswordApi() {


        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setNewPassowrd(cPass.getText().toString());
        changePasswordRequest.setMobile(mobile);

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

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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

    private void getIds() {
        passwod = findViewById(R.id.password);
        cPass = findViewById(R.id.c_password);
        eye = findViewById(R.id.eyeBtn);
        c_eye = findViewById(R.id.c_eyeBtn);
        done = findViewById(R.id.verify_button);
        back = findViewById(R.id.img_nav_back);
    }
}