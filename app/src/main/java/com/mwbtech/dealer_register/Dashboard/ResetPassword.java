package com.mwbtech.dealer_register.Dashboard;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_PHONE;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.LoginRegister.LoginActivity;
import com.mwbtech.dealer_register.PojoClass.OTPValidation;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.SessionDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword extends AppCompatActivity {
    EditText opsd, cpsd, npsd;
    AppCompatImageView newpswdeyeBtn, oldpswdeyeBtn, cnfrmPswdEyeBtn;
    String phone;
    AppCompatImageView imgBack;
    SharedPreferences pref;
    private static final String PREF_NAME = "mwb-dealer_register";
    int PRIVATE_MODE = 0;
    Customer_Interface customer_interface;
    String Token;
    Button submit;
    boolean isShowingOldPassword = false, isShowingNewPassword = false, isShowingConfirmPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        opsd = (EditText) findViewById(R.id.opsd);
        npsd = (EditText) findViewById(R.id.npsd);
        cpsd = (EditText) findViewById(R.id.cpsd);
        cnfrmPswdEyeBtn = findViewById(R.id.cnfrmPswdEyeBtn);
        newpswdeyeBtn = findViewById(R.id.newpswdeyeBtn);
        oldpswdeyeBtn = findViewById(R.id.oldpswdeyeBtn);
        imgBack = findViewById(R.id.back);
        submit = (Button) findViewById(R.id.btnregister);

        pref = this.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        phone = pref.getString(CUST_PHONE, null);

        PrefManager prefManager = new PrefManager(this);
        Token = prefManager.getToken().get(TOKEN);

        imgBack.setOnClickListener(v -> onBackPressed());
        oldpswdeyeBtn.setOnClickListener(v -> hideUnhideOldPassword());
        newpswdeyeBtn.setOnClickListener(v -> hideUnhideNewPassword());
        cnfrmPswdEyeBtn.setOnClickListener(v -> hideUnhideConfirmPassword());

        submit.setOnClickListener(v -> {

            if (opsd.getText().toString().trim().equals("")) {
                Toast.makeText(ResetPassword.this, "Please enter old password", Toast.LENGTH_LONG).show();
            } else if (npsd.getText().toString().trim().equals("")) {
                Toast.makeText(ResetPassword.this, "Please enter new password", Toast.LENGTH_LONG).show();
            } else if (cpsd.getText().toString().trim().equals("")) {
                Toast.makeText(ResetPassword.this, "Please enter confirm password", Toast.LENGTH_LONG).show();
            } else {
                if (npsd.getText().toString().trim().equals(cpsd.getText().toString().trim())) {
                    GenerateNewPassword();
                } else {

                    Toast.makeText(ResetPassword.this, "Password and confirm password not matching", Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    private void hideUnhideOldPassword() {
        if (isShowingOldPassword) {
            opsd.setTransformationMethod(new PasswordTransformationMethod());
            opsd.setSelection(opsd.getText().toString().length());
            oldpswdeyeBtn.setColorFilter(ContextCompat.getColor(this, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
            isShowingOldPassword = false;
        } else {
            opsd.setTransformationMethod(null);
            opsd.setSelection(opsd.getText().toString().length());
            oldpswdeyeBtn.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);
            isShowingOldPassword = true;
        }
    }

    private void hideUnhideConfirmPassword() {
        if (isShowingConfirmPassword) {
            cpsd.setTransformationMethod(new PasswordTransformationMethod());
            cpsd.setSelection(cpsd.getText().toString().length());
            cnfrmPswdEyeBtn.setColorFilter(ContextCompat.getColor(this, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
            isShowingConfirmPassword = false;
        } else {
            cpsd.setTransformationMethod(null);
            cpsd.setSelection(cpsd.getText().toString().length());
            cnfrmPswdEyeBtn.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);
            isShowingConfirmPassword = true;
        }
    }

    private void hideUnhideNewPassword() {
        if (isShowingNewPassword) {
            npsd.setTransformationMethod(new PasswordTransformationMethod());
            npsd.setSelection(npsd.getText().toString().length());
            newpswdeyeBtn.setColorFilter(ContextCompat.getColor(this, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
            isShowingNewPassword = false;
        } else {
            npsd.setTransformationMethod(null);
            npsd.setSelection(npsd.getText().toString().length());
            newpswdeyeBtn.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);
            isShowingNewPassword = true;
        }
    }

    private void GenerateNewPassword() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<OTPValidation> call = customer_interface.ChangePassword("bearer " + Token, phone, opsd.getText().toString(), npsd.getText().toString());
        call.enqueue(new Callback<OTPValidation>() {
            @Override
            public void onResponse(Call<OTPValidation> call, Response<OTPValidation> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        Toast.makeText(ResetPassword.this, "Password Changed successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ResetPassword.this, LoginActivity.class);
                        startActivity(intent);
                        ResetPassword.this.finish();
                        break;
                    case 404:
                        Toast.makeText(ResetPassword.this, "The old password you have entered is incorrect", Toast.LENGTH_LONG).show();

                        break;
                    case 503:
                        Toast.makeText(ResetPassword.this, "Credentials are not matching", Toast.LENGTH_LONG).show();

                        break;
                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(ResetPassword.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<OTPValidation> call, Throwable t) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class).putExtra("isNewUser", false);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        ResetPassword.this.finish();
    }
}