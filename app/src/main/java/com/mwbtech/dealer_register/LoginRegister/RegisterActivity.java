package com.mwbtech.dealer_register.LoginRegister;

import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.instance;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TandCVersion;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.PojoClass.AccountVerify;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.PojoClass.ErrorBodyResponse;
import com.mwbtech.dealer_register.PojoClass.RegisterAccount;
import com.mwbtech.dealer_register.PojoClass.StateCityResponse;
import com.mwbtech.dealer_register.PojoClass.TokenObject;
import com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.ApiUtity;
import com.mwbtech.dealer_register.Utils.CommonUtils;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final Pattern PHONE_PATTERN = Pattern.compile("^" + "(?=.*[0-9])" + ".{10}" + "$");
    EditText edName, edMobileNo, edemail, edPassword, edConfirmPassword, pinCode, referalCode;
    TextView tvterms;
    LinearLayout layoutTermsCond;
    CheckBox cbTerms;
    Button btnSave;
    Spinner spinner;
    String termsVersion;
    PrefManager prefManager;
    ArrayAdapter ledgerAdapter;
    String[] ledger = {"Business Class"};
    String Token;
    Customer_Interface customer_interface;
    String FCMtoken;
    boolean isShowing = false, isShowingConfirmPassword = false;
    String regNumber = "^[5-9][0-9]{9}$";
    String pinCodeRegex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
    private AppCompatImageView eyeIV, cnfrmPswdEyeBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        prefManager = new PrefManager(this);
        initMethod();
        checkConnection();
        getFCMtoken();
        btnSave.setOnClickListener(this);
        eyeIV.setOnClickListener((view) -> {
            hideUnhide();
        });
        cnfrmPswdEyeBtn.setOnClickListener((view) -> {
            hideUnhideConfirmPassword();
        });
    }
    private void getFCMtoken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                FCMtoken = task.getResult();
                String msg = getString(R.string.msg_token_fmt, FCMtoken);
                Log.d(TAG, msg);
            }
        });
    }
    private void checkConnection() {
        if (ConnectivityReceiver.isConnected()) {
            GetTokenFromServer();

        } else {
            showError();
        }
    }
    private void showError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Alert.!");
        builder.setMessage("You are not connected to internet");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkConnection();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void initMethod() {
        eyeIV = findViewById(R.id.eyeBtn);
        cnfrmPswdEyeBtn = findViewById(R.id.cnfrmPswdEyeBtn);
        pinCode = findViewById(R.id.pincode_ev);
        referalCode = findViewById(R.id.referal_ev);

        tvterms = findViewById(R.id.termcond);
        layoutTermsCond = findViewById(R.id.layout_terms_cond);
        edName = findViewById(R.id.name);
        edMobileNo = findViewById(R.id.mobile_no);
        edemail = findViewById(R.id.emailEv);
        edPassword = findViewById(R.id.password);
        edConfirmPassword = findViewById(R.id.cpassword);
        cbTerms = findViewById(R.id.checkboxTerm);
        cbTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbTerms.isChecked()) {
                    startActivity(new Intent(getApplicationContext(), TermsCond.class).putExtra("token", Token));
                } else {
                }
            }
        });
        layoutTermsCond.setOnClickListener(v -> cbTerms.performClick());
        tvterms.setOnClickListener(v -> cbTerms.performClick());
        btnSave = findViewById(R.id.btnregister);
    }
    private void hideUnhide() {
        if (isShowing) {
            edPassword.setTransformationMethod(new PasswordTransformationMethod());
            edPassword.setSelection(edPassword.getText().toString().length());
            eyeIV.setColorFilter(ContextCompat.getColor(this, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);

            isShowing = false;
        } else {
            edPassword.setTransformationMethod(null);
            edPassword.setSelection(edPassword.getText().toString().length());
            eyeIV.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);

            isShowing = true;
        }
    }
    private void hideUnhideConfirmPassword() {

        if (isShowingConfirmPassword) {
            edConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
            edConfirmPassword.setSelection(edConfirmPassword.getText().toString().length());
            cnfrmPswdEyeBtn.setColorFilter(ContextCompat.getColor(this, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);

            isShowingConfirmPassword = false;
        } else {
            edConfirmPassword.setTransformationMethod(null);
            edConfirmPassword.setSelection(edConfirmPassword.getText().toString().length());
            cnfrmPswdEyeBtn.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);

            isShowingConfirmPassword = true;
        }


    }
    private void GetTokenFromServer() {
        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(RegisterActivity.this, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        //Call<TokenObject> call = customer_interface.getToken("dMRo3oqVLOs=","Dgy7t7MvU3JZx+ObnW6z3A==","password");
        Call<TokenObject> call = customer_interface.generateToken();
        call.enqueue(new Callback<TokenObject>() {
            @Override
            public void onResponse(Call<TokenObject> call, Response<TokenObject> response) {
                int status_code = response.code();
                Log.e("Token", "status..." + status_code);
                switch (status_code) {
                    case 200:
                        progressBar.dismiss();
                        TokenObject tokenObject1 = response.body();
                        Token = tokenObject1.getAccess_token();
                        //TokenObject Token=response.body();
                        Log.e("Token", ".." + tokenObject1.getAccess_token());
                        prefManager.SaveToken(Token);

                        break;
                    case 500:
                        progressBar.dismiss();
                        break;
                }
            }

            @Override
            public void onFailure(Call<TokenObject> call, Throwable t) {
                progressBar.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(RegisterActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(RegisterActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }
    //| !validatePincode()
    //| !ValidateEmail()
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnregister:
                if (!ValidateNumber() | !validateName()  | !validatePassword()  | !Validaterepass()) {
                    return;
                } else {
                    if (ConnectivityReceiver.isConnected()) {
                        if (cbTerms.isChecked()) {
                            termsVersion = prefManager.getTandcversion().get(TandCVersion);
                            if(!TextUtils.isEmpty(pinCode.getText().toString().trim())){
                                if(validatePincode()){
                                    getStateCityFromPinCode(pinCode.getText().toString().trim());
                                }
                                else
                                    return;
                            }
                            else registerMethod();
                        } else {
                            CustomToast.showToast(this, "Please accept terms and condition before proceeding");
                        }
                    } else {
                        CustomToast.showToast(this, "Sorry! Not connected to internet");
                    }
                }
                break;

        }
    }
    private void registerMethod() {
            //ApiUtity.generateOtpApi(this, "1234567890", mobileNumber, "", new ApiUtity.APIResponseListener<AccountVerify>() {
            //added by shweta to get the dynamic device id
            ApiUtity.generateOtpApi(this, FCMtoken,edMobileNo.getText().toString(), Token, new ApiUtity.APIResponseListener<AccountVerify>() {
                @Override
                public void onReceiveResponse(AccountVerify response) {
                    if(response.getOTPStatus().equals("User Already Exists")){
                        CommonUtils.showToast(getApplicationContext(), "User Already Exists");
                    }
                    else{
                        String mask = edMobileNo.getText().toString().replaceAll("\\w(?=\\w{4})", "*");
                        Toast.makeText(getApplicationContext(), "Otp resent to " + mask, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Otp is" + response.getSMSOTP(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, VerifyPhone.class)
                                .putExtra("NUMBER",edMobileNo.getText().toString())
                                .putExtra("DEVICEID",FCMtoken)
                                .putExtra("CUSTName",edName.getText().toString().trim())
                                .putExtra("EmailID",edemail.getText().toString().trim())
                                .putExtra("PINCODE",pinCode.getText().toString().trim())
                                .putExtra("PassWord", edPassword.getText().toString().trim())
                                .putExtra("ReferralCode",referalCode.getText().toString().trim())
                                .putExtra("isTerms",true)
                                .putExtra("termVersion",termsVersion)
                                .putExtra("OTP",response.getSMSOTP())
                        );
                    }

                }

                @Override
                public void onResponseFailed(String msg) {
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            });
              /* ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(RegisterActivity.this, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        RegisterAccount dealerRegister = new RegisterAccount(
                edName.getText().toString().trim(),
                edemail.getText().toString().trim(),
                edMobileNo.getText().toString().trim(),
                pinCode.getText().toString().trim(),
                edPassword.getText().toString().trim(),
                referalCode.getText().toString().trim(),
                true, termsVersion
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
                        startActivity(new Intent(RegisterActivity.this, VerifyPhone.class)
                                .putExtra("NUMBER",edMobileNo.getText().toString())
                                .putExtra("DEVICEID",FCMtoken)
                                .putExtra("CUSTName",edName.getText().toString().trim())
                                .putExtra("EmailID",edemail.getText().toString().trim())
                                .putExtra("PINCODE",pinCode.getText().toString().trim())
                                .putExtra("PassWord", edPassword.getText().toString().trim())
                                .putExtra("ReferralCode",referalCode.getText().toString().trim())
                                .putExtra("isTerms",true)
                                .putExtra("termVersion",termsVersion)
                        );

                        break;

                    case 409:
                        progressBar.dismiss();
                        CustomToast.showToast(RegisterActivity.this, "User Already Registered.");
                        break;

                    case 307:
                        progressBar.dismiss();
                        CustomToast.showToast(RegisterActivity.this, "Server is not responding." + response.code());
                        break;

                    case 500:
                        progressBar.dismiss();
                        CustomToast.showToast(RegisterActivity.this, "Server Error");
                        break;
                    default:
                        progressBar.dismiss();
                        ErrorBodyResponse errorBodyResponse = new ErrorBodyResponse();
                        try {
                            errorBodyResponse = new Gson().fromJson(response.errorBody().string(), ErrorBodyResponse.class);
                            CustomToast.showToast(RegisterActivity.this, errorBodyResponse.getDisplayMessage());

                        } catch (IOException e) {
                            e.printStackTrace();
                            CustomToast.showToast(RegisterActivity.this, errorBodyResponse.getDisplayMessage());
                        }

                }
            }

            @Override
            public void onFailure(Call<DealerRegister> call, Throwable t) {
                progressBar.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(RegisterActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(RegisterActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });*/
        }



    private void getStateCityFromPinCode(String pincode) {
        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(RegisterActivity.this, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<StateCityResponse>> call = customer_interface.getStateCityFromPincode(pincode);
        call.enqueue(new Callback<List<StateCityResponse>>() {
            @Override
            public void onResponse(Call<List<StateCityResponse>> call, Response<List<StateCityResponse>> response) {
                int status_code = response.code();
                progressBar.dismiss();
                switch (status_code) {
                    case 200:
                        if (response.body() != null) {
                            if (response.body().size() == 0) {
                                pinCode.setError("Please enter valid pincode");
                                pinCode.requestFocus();
                                return;
                            } else registerMethod();
                            //hide keyboard
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(btnSave.getApplicationWindowToken(), 0);
                        }
                        break;
                    case 503:
                    case 404:
                        pinCode.setError("Please enter valid pincode");
                        pinCode.requestFocus();
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(RegisterActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<StateCityResponse>> call, Throwable t) {
                progressBar.dismiss();
                CommonUtils.showToast(RegisterActivity.this, call.toString());
            }
        });

    }

    private boolean ValidateEmail() {
        String email = edemail.getText().toString();
        if (email.isEmpty()) return true;
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.charAt(email.indexOf("@") - 1) == '.') {
            edemail.setError("Please enter valid email");
            CustomToast.showToast(this, "Please enter valid email");
            return false;
        } else {
            return true;
        }
    }
    private boolean ValidateNumber() {
        String NumberInput = edMobileNo.getText().toString().trim();

        if (!PHONE_PATTERN.matcher(NumberInput).matches()) {
            edMobileNo.setError("Please enter valid mobile number");
            return false;
        } else if (NumberInput.isEmpty()) {
            edMobileNo.setError("Please enter valid mobile number");
            return false;
        } else {

            if (!NumberInput.matches(regNumber)) {
                edMobileNo.setError("Please enter valid mobile number");
                return false;
            } else {
                return true;
            }

        }
    }
    private boolean validateName() {
        String NumberInput = edName.getText().toString().trim();

        if (NumberInput.isEmpty()) {
            edName.setError("Please enter name");
            return false;
        } else {
            return true;
        }
    }
    private boolean validatePassword() {
        String NumberInput = edPassword.getText().toString().trim();

        if (NumberInput.isEmpty()) {
            edPassword.setError("Please enter password");
            return false;
        } else {
            return true;
        }
    }
    private boolean validatePincode() {
        String numberInput = pinCode.getText().toString().trim();
        if (pinCode.getText().toString().trim().isEmpty()) {
            pinCode.setError("Please enter pincode");
            return false;

        } else if (!numberInput.matches(pinCodeRegex)) {
            pinCode.setError("Please enter valid pincode");
            pinCode.requestFocus();
            return false;
        } else return true;
    }
    private boolean Validaterepass() {
        String NumberInput = edConfirmPassword.getText().toString().trim();

        if (!NumberInput.matches(edPassword.getText().toString())) {
            edConfirmPassword.setError("Password not matched");
            return false;
        } else if (NumberInput.isEmpty()) {
            edConfirmPassword.setError("Password not matched");
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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