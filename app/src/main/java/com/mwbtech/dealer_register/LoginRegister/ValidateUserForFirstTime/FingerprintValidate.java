package com.mwbtech.dealer_register.LoginRegister.ValidateUserForFirstTime;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.LoginRegister.LoginActivity;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.Profile.BillingDetails.OnFirstRegistration;
import com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Splash.WelcomeScreen;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;

import java.io.IOException;
import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FingerprintValidate extends AppCompatActivity {

    String Token;
    Customer_Interface customer_interface;
    Context _context;
    private static final String PREF_NAME = "mwb-dealer_register";
    String fpuname,fppswd;
    int PRIVATE_MODE = 0;
    PrefManager prefManager;

    String FCMtoken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprintlogin_validate);

        Intent intent = getIntent();
        if(intent != null) {
             fpuname = intent.getStringExtra("fpuname");
             fppswd = intent.getStringExtra("fppswd");
             FCMtoken = intent.getStringExtra("FCMtoken");
             Log.e("FCMToken.......",FCMtoken);
        }
        prefManager = new PrefManager(this);
        Token = prefManager.getToken().get(TOKEN);
        loginRequestSendToServer(fpuname, fppswd);
    }

    private void loginRequestSendToServer(String mobileNumber, String password) { //, String FCMtokenn

        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(FingerprintValidate.this,"");
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<DealerRegister> call = customer_interface.loginCustomerDetailsMethod("bearer "+Token,mobileNumber, password,FCMtoken);//,FCMtokenn

        call.enqueue(new Callback<DealerRegister>() {
            @Override
            public void onResponse(Call<DealerRegister> call, Response<DealerRegister> response) {
                int statusCode = response.code();
                Log.e("Login","status..."+statusCode);
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        DealerRegister dealerRegister = response.body();
                        String adUrl = dealerRegister.getFullScreenAdURL();
                        prefManager.saveCustId(dealerRegister.getCustID(), dealerRegister.getPassword(),
                                Integer.parseInt(dealerRegister.getUserType()), mobileNumber);
                        if (dealerRegister.isOTPVerified()) {
                            if (dealerRegister.getUserType().equals("2")) {
                                if (dealerRegister.getIsRegistered() == 1) {
                                    if(dealerRegister.getFullScreenAdURL()==null)
                                    {
                                        if(dealerRegister.getIsWelcomeMsg().equals("1")){
                                            startActivity(new Intent(FingerprintValidate.this, OnFirstRegistration.class));
                                            FingerprintValidate.this.finish();
                                        }

                                        else{
                                            //startActivity(new Intent(FingerprintValidate.this, WelcomeScreen.class));
                                            startActivity(new Intent(FingerprintValidate.this, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    .putExtra("isNewUser", false));
                                            FingerprintValidate.this.finish();
                                        }
                                    }
                                    else{
                                        startActivity(new Intent(FingerprintValidate.this, DashboardActivity.class).putExtra("adUrl",(Serializable) dealerRegister));
                                        FingerprintValidate.this.finish();
                                    }

                                }
                                else {
                                    startActivity(new Intent(FingerprintValidate.this, MainActivity.class));
                                    FingerprintValidate.this.finish();
                                }
                            }
                            else {
                                startActivity(new Intent(FingerprintValidate.this, DashboardActivity.class)
                                        .putExtra("adUrl", (Serializable) dealerRegister)
                                        .putExtra("loginurl", dealerRegister.getAdUserID())
                                        .putExtra("isNewUser", false));
                            }
                        }
//                        else {
//                            GetVerifyOtp();
//                        }
                        break;

                    case 404:
                        progressDialog.dismiss();
                        CustomToast.showToast(FingerprintValidate.this, "Enter Valid Mobile Number and Password.");
                        startActivity(new Intent(FingerprintValidate.this, LoginActivity.class));
                        break;

                    case 307:
                        progressDialog.dismiss();
                        CustomToast.showToast(FingerprintValidate.this, "Server is not responding." + response.code());
                        startActivity(new Intent(FingerprintValidate.this, LoginActivity.class));
                        break;

                    case 500:
                        CustomToast.showToast(FingerprintValidate.this, "Server Error ");
                        startActivity(new Intent(FingerprintValidate.this, LoginActivity.class));
                        progressDialog.dismiss();
                        break;
                    case 401:
                        CustomToast.showToast(FingerprintValidate.this, "Unauthorized");
                        startActivity(new Intent(FingerprintValidate.this, LoginActivity.class));
                        progressDialog.dismiss();
                        break;
                    case 503:
                        progressDialog.dismiss();
                        CustomToast.showToast(FingerprintValidate.this, "Please contact Admin for login");
                        startActivity(new Intent(FingerprintValidate.this, LoginActivity.class));
                        break;
                }
            }

            @Override
            public void onFailure(Call<DealerRegister> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ","error..."+t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText( FingerprintValidate.this, "Time out..", Toast.LENGTH_LONG).show();
                }
                else {
                    CustomToast.showToast( FingerprintValidate.this,"Bad response from server.. Try again later ");
                }
            }
        });
    }


}
