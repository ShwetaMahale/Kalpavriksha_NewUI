package com.mwbtech.dealer_register.LoginRegister;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_PHONE;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.PojoClass.AccountVerify;
import com.mwbtech.dealer_register.PojoClass.ErrorBodyResponse;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.ApiUtity;
import com.mwbtech.dealer_register.Utils.CommonUtils;

public class ForgotPassFirst extends AppCompatActivity {

    EditText editMobile;
    ImageView imgBack;
    Button submit;

    SharedPreferences pref;
    private static final String PREF_NAME = "mwb-dealer_register";
    int PRIVATE_MODE = 0;
    Customer_Interface customer_interface;
    String Token, phone;
    private AlertDialog checkMailDialog;
    private boolean isCheckMailDialogShown;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_first);
        context = this;
        editMobile = findViewById(R.id.edt_phone);
        submit = findViewById(R.id.submit_button);
        imgBack = findViewById(R.id.img_nav_back);

        pref = this.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        phone = pref.getString(CUST_PHONE, null);

        PrefManager prefManager = new PrefManager(this);
        Token = prefManager.getToken().get(TOKEN);
        imgBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        submit.setOnClickListener(v -> {
            if (editMobile.getText().toString().trim().equals("") && editMobile.getText().toString().trim().length() < 10) {
                Toast.makeText(this, "Please enter valid phone number", Toast.LENGTH_LONG).show();
            } else {
                //   showMailSentDialog();
//                validateUser();
                GetOtpForgotPassword();
            }
        });
    }

    private void validateUser() {
        ApiUtity.validateUserApi(context, editMobile.getText().toString(), new ApiUtity.APIResponseListener<ErrorBodyResponse>() {
            @Override
            public void onReceiveResponse(ErrorBodyResponse response) {
//                GetOtpForgotPassword();
                startActivity(new Intent(ForgotPassFirst.this, OtpVerificationActivity.class).putExtra("NUMBER", editMobile.getText().toString().trim()));

            }

            @Override
            public void onResponseFailed(String msg) {
                CommonUtils.showToast(context, msg);
            }
        });
    }


    private void showMailSentDialog(String smsotp) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View layoutView =
                this.getLayoutInflater().inflate(R.layout.dialog_check_mail, null);

        Button doneButton = layoutView.findViewById(R.id.done_button);
        dialogBuilder.setView(layoutView);
        dialogBuilder.setCancelable(false);
        checkMailDialog = dialogBuilder.create();

        checkMailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        checkMailDialog.show();
        isCheckMailDialogShown = true;
        doneButton.setOnClickListener(v -> {
            checkMailDialog.dismiss();
            startActivity(new Intent(this, OtpVerificationActivity.class).putExtra("NUMBER", editMobile.getText().toString().trim()).putExtra("OTP", smsotp));
            // Toast.makeText(this, "In progress.....", Toast.LENGTH_SHORT).show();
        });
    }

    private void GetOtpForgotPassword() {
        ApiUtity.generateOtpApi(context, "1234567890", editMobile.getText().toString(), "", new ApiUtity.APIResponseListener<AccountVerify>() {
            @Override
            public void onReceiveResponse(AccountVerify response) {
                if(!response.isOTPSent()){
                    Toast.makeText(context, response.getOTPStatus(), Toast.LENGTH_SHORT).show();
                } else {
                    String number = editMobile.getText().toString().trim();
                    String mask = number.replaceAll("\\w(?=\\w{4})", "*");
                    Toast.makeText(context, "Otp sent to " + mask, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ForgotPassFirst.this, OtpVerificationActivity.class).putExtra("NUMBER", editMobile.getText().toString().trim()));
                }
            }

            @Override
            public void onResponseFailed(String msg) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(this, LoginActivity.class);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}