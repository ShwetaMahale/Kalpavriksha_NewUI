package com.mwbtech.dealer_register.LoginRegister;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.LoginRegister.OTPRetriver.AppSignatureHashHelper;
import com.mwbtech.dealer_register.LoginRegister.OTPRetriver.SMSReceiver;
import com.mwbtech.dealer_register.PojoClass.AdDetailsModel;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.PojoClass.OTPApprove;
import com.mwbtech.dealer_register.PojoClass.OTPValidation;
import com.mwbtech.dealer_register.PojoClass.SocialLoginRequest;
import com.mwbtech.dealer_register.PojoClass.TokenObject;
import com.mwbtech.dealer_register.Profile.BillingDetails.OnFirstRegistration;
import com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.LoginRequest;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.biometrics.BiometricManager;
import com.mwbtech.dealer_register.biometrics.BiometricStatus;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;
import com.mwbtech.dealer_register.internet_connection.MyInternetCheck;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener, SMSReceiver.OTPReceiveListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int RequestPermissionCode = 1;
    private static final Pattern PHONE_PATTERN = Pattern.compile("^" + "(?=.*[0-9])" + ".{10}" + "$");
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "androidHive";
    private static final String EMAIL = "email";
    private final int RC_SIGN_IN = 101;
    private final LoginRequest loginRequest = new LoginRequest();
    ConnectivityReceiver connectivityReceiver;
    IntentFilter intentFilter;
    String FCMtoken;
    //notification
    NotificationManager mNotificationManager;
    SharedPreferences sharedpreferences;
    String prevStarted = "prevStarted";
    SharedPreferences.Editor editor;
    String regNumber = "^[5-9][0-9]{9}$";
    Button btnSave;
    TextView tvRegister, tvForgot;
    String Token = "", Token1, otpStatus, otpString;
    //String staticToken="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MzUxNzYyMDIsImlzcyI6Imh0dHBzOi8vd3d3Lm13Yi5jb20iLCJhdWQiOiJodHRwczovL3d3dy5td2IuY29tIn0.W3tjPZsajg84-F9LEvUAa6krrJAIzOxePl5315nidtg";
    PrefManager prefManager;
    LinearLayout constraintLayout;
    Customer_Interface customer_interface;
    int reSendCount;
    Dialog mDialogSubCategory;
    String slatestversion, scurrentversion;
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
    LoginManager loginManager;
    CallbackManager callbackManager;
    boolean isShowing = false;
    private String username, password;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private EditText edMobileNo, edPassword, editTextConfirmOtp1, editTextConfirmOtp2, editTextConfirmOtp3, editTextConfirmOtp4;
    private SMSReceiver smsReceiver;
    private KeyStore keyStore;
    private Cipher cipher;
    private AppCompatImageView eyeIV, imgFingerPrint;
    private Context context;
    private BiometricManager biometricManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        context = LoginActivity.this;
        //new GetLatestVersion().execute();
        loginPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        intentFilter = new IntentFilter();
        connectivityReceiver = new ConnectivityReceiver();
        biometricManager = new BiometricManager(this);
        initializeViews();

        getFCMtoken();
        CheckPermission();
        checkConnection();
//        showBiometricPrompt();

        // Set the dimensions of the sign-in button.
        AppCompatImageView signInButton = findViewById(R.id.sign_in_button);
//        signInButton.setSize(SignInButton.SIZE_STANDARD);

        signInButton.setOnClickListener(view -> googleSignIn());

        callbackManager = CallbackManager.Factory.create();

//        facebookLogin();
        AppCompatImageView loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener((view) -> {
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, (Arrays.asList("public_profile", "user_friends", "user_birthday", "user_about_me", "email")));
        });
//        if(prefManager.getToken() != null) showBiometricPrompt();
        imgFingerPrint.setOnClickListener(v -> CheckFingerPrint());

        /*LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(context, "SUCC", Toast.LENGTH_SHORT).show();
                        //1. Logged in to Facebook
                        AccessToken accessToken = loginResult.getAccessToken();
                        performLogin(accessToken);
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(context, "ERRR", Toast.LENGTH_SHORT).show();

                    }
                });

*/
    }

    private void googleSignIn() {
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }


    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            getGoogleLogin(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //  updateUI(null);
        }
    }

    private void getFacebookLogin(String token, String email, String username) {
        SocialLoginRequest socialLoginRequest = new SocialLoginRequest();
        socialLoginRequest.setSocialMediaToken(token);
        socialLoginRequest.setEmail(email);
        socialLoginRequest.setName(username);
        socialLoginRequestSendToServer(socialLoginRequest);
    }

    public void getGoogleLogin(GoogleSignInAccount account) {
        SocialLoginRequest socialLoginRequest = new SocialLoginRequest();
        socialLoginRequest.setSocialMediaToken(account.getIdToken());
        socialLoginRequest.setEmail(account.getEmail());
        socialLoginRequest.setName(account.getDisplayName());
        socialLoginRequestSendToServer(socialLoginRequest);
/*        ApiUtity.loginUsingSocialMediaApi(context, socialLoginRequest, new ApiUtity.APIResponseListener<DealerRegister>() {
            @Override
            public void onReceiveResponse(DealerRegister response) {
                context.startActivity(new Intent(context, DashboardActivity.class).putExtra("isNewUser", false));
            }

            @Override
            public void onResponseFailed(String msg) {
                CommonUtils.showToast(context, msg);
            }
        });*/
    }

    private void checkConnection() {
        if (ConnectivityReceiver.isConnected()) {
            GetTokenFromServer();
        } else {
            showError();
        }
    }

    private void hideUnhide() {

        if (isShowing) {
            edPassword.setTransformationMethod(new PasswordTransformationMethod());
            edPassword.setSelection(edPassword.getText().toString().length());
            isShowing = false;
            eyeIV.setColorFilter(ContextCompat.getColor(this, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);

        } else {
            edPassword.setTransformationMethod(null);
            edPassword.setSelection(edPassword.getText().toString().length());
            eyeIV.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), android.graphics.PorterDuff.Mode.MULTIPLY);

            isShowing = true;
        }
    }

    private void checkForAutoLogin() {
    }

    private void checkForBiometric(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        if (prefManager.getToken().get(TOKEN) != null) showBiometricPrompt();
        else
            Toast.makeText(context, "Last login details not available, please login using credentials", Toast.LENGTH_SHORT).show();
    }

    private void showBiometricPrompt() {
        biometricManager.verifyFinerPrint(status -> {
            if (BiometricStatus.BIOMETRIC_SUCCESS.equals(status)) {
                loginRequestSendToServer(edMobileNo.getText().toString(), edPassword.getText().toString());
            } else {
                CustomToast.showToast(LoginActivity.this, "Authentication failed");
            }
        });
    }

    private void showError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert.!");
        builder.setMessage("You are not connected to internet.");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //checkConnection();
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void CheckFingerPrint() {
        // Check if we're running on Android 6.0 (M) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                //Fingerprint API only available on from Android 6.0 (M)
                FingerprintManager fingerprintManager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);
                if (!fingerprintManager.isHardwareDetected()) {
                    // Device doesn't support fingerprint authentication
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    // User hasn't enrolled any fingerprints to authenticate with
                } else {
                    // Everything is ready for fingerprint authentication
                    //Toast.makeText(this, "Finger print sensor is present", Toast.LENGTH_SHORT).show();
                    loginPreferences = getSharedPreferences("saveLogin", Context.MODE_PRIVATE);
                    //String unamecheck = loginPreferences.getString("username", "");
                    //String pswdcheck = loginPreferences.getString("password", "");
                    String unamecheck = prefManager.getUserName();
                    String pswdcheck = prefManager.getPassword();
                    if (!unamecheck.equals("") || !pswdcheck.equals("")) {
                        mDialogSubCategory = new Dialog(this);
                        mDialogSubCategory.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        mDialogSubCategory.getWindow().setBackgroundDrawable(
                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        mDialogSubCategory.setContentView(R.layout.activity_fingerprint);
                        mDialogSubCategory.setCancelable(true);
                        // Initializing both Android Keyguard Manager and Fingerprint Manager
                        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

                        TextView textView = mDialogSubCategory.findViewById(R.id.errorText);

                        // Check whether the device has a Fingerprint sensor.
                        if (!fingerprintManager.isHardwareDetected()) {
                            textView.setText("Your Device does not have a Fingerprint Sensor");
                        } else {
                            // Checks whether fingerprint permission is set on manifest
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                                textView.setText("Fingerprint authentication permission not enabled");
                            } else {
                                // Check whether at least one fingerprint is registered
                                if (!fingerprintManager.hasEnrolledFingerprints()) {
                                    textView.setText("Register at least one fingerprint in Settings");
                                } else {
                                    // Checks whether lock screen security is enabled or not
                                    if (!keyguardManager.isKeyguardSecure()) {
                                        textView.setText("Lock screen security not enabled in Settings");
                                    } else {
                                        generateKey();
                                        if (cipherInit()) {
                                            /*BiometricPrompt.CryptoObject cryptoObject = new BiometricPrompt.CryptoObject(cipher);
                                            FingerprintHandler helper = new FingerprintHandler(this);
                                            //helper.startAuth(fingerprintManager, cryptoObject, unamecheck, pswdcheck, Token, FCMtoken);*/
                                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                            FingerprintHandler helper = new FingerprintHandler(this);
                                            helper.startAuth(fingerprintManager, cryptoObject, unamecheck, pswdcheck, Token, FCMtoken);

                                        }
                                    }
                                }
                            }
                        }
                        mDialogSubCategory.show();
                    }
                }
            } catch (Exception ex) {

            }
        }
    }

    private void GetTokenFromServer() {
        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(context, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        //Call<TokenObject> call = customer_interface.getToken("dMRo3oqVLOs=","Dgy7t7MvU3JZx+ObnW6z3A==","password");
        Call<TokenObject> call = customer_interface.generateToken();
        call.enqueue(new Callback<TokenObject>() {
            @Override
            public void onResponse(Call<TokenObject> call, Response<TokenObject> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        progressBar.dismiss();
                        //TokenObject Token=response.body();
                        //Log.e("new token...",Token.getAccess_token());
                        //Log.e("new token...",response.body().getAccess_token());
                        TokenObject tokenObject1 = response.body();
                        Token = tokenObject1.getAccess_token();
                        Token1 = tokenObject1.getAccess_token();
                        prefManager.SaveToken(Token);
                        // prefManager.SaveToken(Token.getAccess_token());
                        if (prefManager.isBiometricRegistrationAsked()) {
                            if (prefManager.getBiometricStatus()) CheckFingerPrint();
                            else {
                                imgFingerPrint.setVisibility(View.GONE);
                            }
                        } else imgFingerPrint.setVisibility(View.GONE);

                        if(prefManager != null){
                            saveLogin = prefManager.isSaveLogin();
                            if (saveLogin) {
                                String unamecheck = prefManager.getUserName();
                                String pswdcheck = prefManager.getPassword();
                                edMobileNo.setText(unamecheck);
                                edPassword.setText(pswdcheck);
                                saveLoginCheckBox.setChecked(true);
                            }
                        }
                        //CheckFingerPrint();
                        break;
                    case 500:
                        progressBar.dismiss();
                        Toast.makeText(context, "Internal Server Error", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<TokenObject> call, Throwable t) {
                progressBar.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(context, "Token Time out..", Toast.LENGTH_LONG).show();
                    Log.e("Token Exept.....", t.toString());
                } else {
                    Log.e("Token Exept.....", t.toString());
                    CustomToast.showToast(context, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void CheckPermission() {
        if (CheckingPermissionIsEnabledOrNot()) {

        } else {
            RequestMultiplePermission();
        }
    }

    private void getFCMtoken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    FCMtoken = task.getResult();
                    String msg = getString(R.string.msg_token_fmt, FCMtoken);
                    Log.d(TAG, msg);
                });

    }

    private void RequestMultiplePermission() {
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]
                {
                        CAMERA,
                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE,
                        RECEIVE_SMS,
                }, RequestPermissionCode);
    }

    private boolean CheckingPermissionIsEnabledOrNot() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    public void printHashKey(Context pContext) {
        // Add code to print out the key hash
        try {


            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                MessageDigest md
                        = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String key = Base64.encodeToString(
                        md.digest(),
                        Base64.DEFAULT);
                Log.e("KeyHash:", key);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e("NameNotFoundException:", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e("NoSuchAlgorithmEx:", e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteExternalPermisssion = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadSMSPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && WriteExternalPermisssion && ReadExternalPermission && ReadSMSPermission) {
                        Toast.makeText(context, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                    }
                }
                break;
        }
    }

    private void initializeViews() {
        prefManager = new PrefManager(this);
        imgFingerPrint = findViewById(R.id.img_fingerprint);
        constraintLayout = findViewById(R.id.parentlayout);
        edMobileNo = findViewById(R.id.mobile_no);
        edPassword = findViewById(R.id.password);
        btnSave = findViewById(R.id.btnlogin);
        eyeIV = findViewById(R.id.eyeBtn);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        btnSave.setOnClickListener(this);
        tvRegister = findViewById(R.id.btnsign);
        tvRegister.setOnClickListener(this);
        tvForgot = findViewById(R.id.btnforgot);
        tvForgot.setOnClickListener(this);
        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);
        // This code requires one time to get Hash keys do comment and share key
        Log.e(TAG, "HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));
        eyeIV.setOnClickListener((view) -> {
            hideUnhide();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnsign:
                prefManager.setSaveLogin(false);
                sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
                editor = sharedpreferences.edit();
                editor.putBoolean(prevStarted, Boolean.FALSE);
                editor.apply();
                //startActivity(new Intent(this, QuestionAnswerActivity.class));
                startActivity(new Intent(this, RegisterActivity.class));
                break;

            case R.id.btnforgot:
                startActivity(new Intent(this, ForgotPassFirst.class));
                finish();
                break;

            case R.id.btnlogin:
                if (!ValidateNumber() | !ValidatePassword()) {
                    return;
                } else {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    username = edMobileNo.getText().toString();
                    password = edPassword.getText().toString();
                    loginRequest.setMobileNo(username);
                    loginRequest.setPassword(password);
                    loginPrefsEditor = loginPreferences.edit();
                    if(prefManager != null){
                        if (saveLoginCheckBox.isChecked()) {
                            prefManager.setSaveLogin(true);
                            prefManager.saveUserName(username);
                            prefManager.savePassword(password);
                        } else {
                            prefManager.setSaveLogin(false);
                            prefManager.saveUserName(username);
                            prefManager.savePassword(password);
                        }
                    }
                    if (loginPrefsEditor != null) {
                        if (saveLoginCheckBox.isChecked()) {
                            loginPrefsEditor.putBoolean("saveLogin", true);
                            loginPrefsEditor.putString("username", username);
                            loginPrefsEditor.putString("password", password);
                            loginPrefsEditor.commit();
                        } else {
                            loginPrefsEditor.clear();
                            loginPrefsEditor.putBoolean("saveLogin", false);
                            loginPrefsEditor.putString("username", username);
                            loginPrefsEditor.putString("password", password);
                            loginPrefsEditor.commit();
                        }
                    }
                    loginRequestSendToServer(edMobileNo.getText().toString(), edPassword.getText().toString());

                    /*ApiUtity.callLoginApi(context, loginRequest, "bearer " + Token, new ApiUtity.APIResponseListener<DealerRegister>() {
                        @Override
                        public void onReceiveResponse(DealerRegister dealerRegister) {

                            prefManager.saveLoginDetails(dealerRegister.getCityID(),
                                    dealerRegister.getVillageLocalityName(),
                                    dealerRegister.getStateID(), dealerRegister.getStateName(),
                                    dealerRegister.getChildCategoryId(),
                                    dealerRegister.getChildCategoryName(), dealerRegister.getAdFirmName());
                            prefManager.setBTypeList(dealerRegister.getBusinessTypeIDList());
                            prefManager.saveCustId(dealerRegister.getCustID(), dealerRegister.getPassword(),
                                    Integer.parseInt(dealerRegister.getUserType()), edMobileNo.getText().toString());
//                            if (dealerRegister.isOTPVerified()) {
                                if (dealerRegister.getUserType().equals("1")) {
                                    if (dealerRegister.getIsRegistered() == 1) {
                                        if (dealerRegister.getFullScreenAdURL() == null) {
                                            if (dealerRegister.getIsWelcomeMsg().equals("1")) {
                                                startActivity(new Intent(context, OnFirstRegistration.class));
                                                finish();
                                            } else {
//                                                startActivity(new Intent(context, WelcomeScreen.class));
                                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                                                finish();
                                            }

                                        } else {
                                            startActivity(new Intent(context, DashboardActivity.class).putExtra("adUrl", (Serializable) dealerRegister)
                                                    .putExtra("loginurl", dealerRegister.getAdUserID()));
                                            //thank you page
                                            //startActivity(new Intent(context, ThankyouScreen.class));
                                            finish();
                                        }

                                    } else {
                                        startActivity(new Intent(context, MainActivity.class));
                                        finish();
                                    }
                                }
//                            } else {
//                                GetVerifyOtp();
//                            }
                        }

                        @Override
                        public void onResponseFailed(String msg) {
                            CommonUtils.showToast(context, msg);
                        }
                    });*/


                    break;
                }
        }
    }

    private void loginRequestSendToServer(String mobileNumber, String password) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(context, "");
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Log.e("token..", Token + "");
        Call<DealerRegister> call = customer_interface.loginCustomerDetailsMethod("bearer " + Token, mobileNumber, password, FCMtoken);
        call.enqueue(new Callback<DealerRegister>() {
            @Override
            public void onResponse(Call<DealerRegister> call, Response<DealerRegister> response) {
                int statusCode = response.code();
                Log.e("Login", "status..." + statusCode);
                Log.e("Answer", "status code......" + response.code());
                Log.e("Response Msg", response.toString());
                Log.e("Response Msg", call.toString());
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        AdDetailsModel adDetailsModel;
                        DealerRegister dealerRegister = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(dealerRegister);
                        Log.e("Response body", json);
                        String adUrl = dealerRegister.getFullScreenAdURL();
                        Log.e(".......", ".........0" + dealerRegister.getVillageLocalityName());
                        prefManager.saveLoginDetails(dealerRegister.getCityID(),
                                dealerRegister.getVillageLocalityName(),
                                dealerRegister.getStateID(), dealerRegister.getStateName(),
                                dealerRegister.getChildCategoryId(),
                                dealerRegister.getChildCategoryName(), dealerRegister.getAdFirmName());
                        prefManager.setBTypeList(dealerRegister.getBusinessTypeIDList());
                        prefManager.saveCustId(dealerRegister.getUserID(), dealerRegister.getPassword(),
                                Integer.parseInt(dealerRegister.getUserType()), edMobileNo.getText().toString());
                        if (dealerRegister.isOTPVerified()) {
                            if (dealerRegister.getUserType().equals("2")) {
                                if (dealerRegister.getIsRegistered() == 1) {
                                    if (dealerRegister.getFullScreenAdURL() == null) {
                                        if (dealerRegister.getIsWelcomeMsg().equals("1")) {
                                            startActivity(new Intent(LoginActivity.this, OnFirstRegistration.class));
                                            LoginActivity.this.finish();
                                        } else {
//                                        startActivity(new Intent(LoginActivity.this, WelcomeScreen.class));
                                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    .putExtra("isNewUser", false));
                                            LoginActivity.this.finish();
                                        }

                                    } else {
                                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class)
                                                .putExtra("adUrl", (Serializable) dealerRegister)
                                                .putExtra("loginurl", dealerRegister.getAdUserID())
                                                .putExtra("isNewUser", false)
                                        );
                                        //thank you page
                                        //startActivity(new Intent(LoginActivity.this, ThankyouScreen.class));
                                        LoginActivity.this.finish();
                                    }

                                } else {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    LoginActivity.this.finish();
                                }
                            } else {
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class)
                                        .putExtra("adUrl", (Serializable) dealerRegister)
                                        .putExtra("loginurl", dealerRegister.getAdUserID())
                                        .putExtra("isNewUser", false)
                                );
                                //thank you page
                                //startActivity(new Intent(LoginActivity.this, ThankyouScreen.class));
                                LoginActivity.this.finish();
                            }
                        } else {
                            GetVerifyOtp();
                        }
                        break;
                    case 404:
                        progressDialog.dismiss();
                        CustomToast.showToast(context, "Invalid mobile number or password");
                        break;

                    case 307:
                        progressDialog.dismiss();
                        CustomToast.showToast(context, "Server is not responding." + response.code());
                        break;

                    case 500:
                        CustomToast.showToast(context, "Server Error ");
                        progressDialog.dismiss();
                        break;
                    case 401:
                        CustomToast.showToast(context, "Unauthorized");
                        progressDialog.dismiss();
                        break;
                    case 503:
                        progressDialog.dismiss();
                        CustomToast.showToast(context, "Please contact Admin for login");
                        break;
                }
            }

            @Override
            public void onFailure(Call<DealerRegister> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(context, "login Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(context, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void socialLoginRequestSendToServer(SocialLoginRequest request) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(context, "");
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Log.e("token..", Token + "");
        Call<DealerRegister> call = customer_interface.socialLogin(request.getSocialMediaToken());
        call.enqueue(new Callback<DealerRegister>() {
            @Override
            public void onResponse(Call<DealerRegister> call, Response<DealerRegister> response) {
                int statusCode = response.code();
                Log.e("Login", "status..." + statusCode);
                Log.e("Answer", "status code......" + response.code());
                Log.e("Response Msg", response.toString());
                Log.e("Response Msg", call.toString());
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        AdDetailsModel adDetailsModel;
                        DealerRegister dealerRegister = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(dealerRegister);
                        Log.e("Response body", json);
                        String adUrl = dealerRegister.getFullScreenAdURL();
                        Log.e(".......", ".........0" + dealerRegister.getVillageLocalityName());
                        prefManager.saveLoginDetails(dealerRegister.getCityID(),
                                dealerRegister.getVillageLocalityName(),
                                dealerRegister.getStateID(), dealerRegister.getStateName(),
                                dealerRegister.getChildCategoryId(),
                                dealerRegister.getChildCategoryName(), dealerRegister.getAdFirmName());
                        prefManager.setBTypeList(dealerRegister.getBusinessTypeIDList());
                        prefManager.saveCustId(dealerRegister.getUserID(), dealerRegister.getPassword(),
                                Integer.parseInt(dealerRegister.getUserType()), edMobileNo.getText().toString());
                        if (dealerRegister.isOTPVerified()) {
                            if (dealerRegister.getUserType().equals("2")) {
                                if (dealerRegister.getIsRegistered() == 1) {
                                    if (dealerRegister.getFullScreenAdURL() == null) {
                                        if (dealerRegister.getIsWelcomeMsg().equals("1")) {
                                            startActivity(new Intent(LoginActivity.this, OnFirstRegistration.class));
                                            LoginActivity.this.finish();
                                        } else {
//                                        startActivity(new Intent(LoginActivity.this, WelcomeScreen.class));
                                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class).putExtra("isNewUser", false));
                                            LoginActivity.this.finish();
                                        }

                                    } else {
                                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class)
                                                .putExtra("adUrl", (Serializable) dealerRegister)
                                                .putExtra("loginurl", dealerRegister.getAdUserID())
                                                .putExtra("isNewUser", false)
                                        );
                                        //thank you page
                                        //startActivity(new Intent(LoginActivity.this, ThankyouScreen.class));
                                        LoginActivity.this.finish();
                                    }

                                } else {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    LoginActivity.this.finish();
                                }
                            } else {
                                startActivity(new Intent(LoginActivity.this, DashboardActivity.class)
                                        .putExtra("adUrl", (Serializable) dealerRegister)
                                        .putExtra("loginurl", dealerRegister.getAdUserID())
                                        .putExtra("isNewUser", false)
                                );
                                //thank you page
                                //startActivity(new Intent(LoginActivity.this, ThankyouScreen.class));
                                LoginActivity.this.finish();
                            }
                        } else {
                            GetVerifyOtp();
                        }
                        break;
                    case 404:
                        progressDialog.dismiss();
                        CustomToast.showToast(context, "Invalid mobile number or password");
                        break;

                    case 307:
                        progressDialog.dismiss();
                        CustomToast.showToast(context, "Server is not responding." + response.code());
                        break;

                    case 500:
                        CustomToast.showToast(context, "Server Error ");
                        progressDialog.dismiss();
                        break;
                    case 401:
                        CustomToast.showToast(context, "Unauthorized");
                        progressDialog.dismiss();
                        break;
                    case 503:
                        progressDialog.dismiss();
                        CustomToast.showToast(context, "Please contact Admin for login");
                        break;
                }
            }

            @Override
            public void onFailure(Call<DealerRegister> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(context, "login Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(context, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    // OTP Validation
    private void GetVerifyOtp() {
        RequestOTP();
        Popup();

    }

    private void Popup() {
        final Button buttonConfirm, ReSendButton;
        Button CancelButton;

        final View dialogView = View.inflate(getApplicationContext(), R.layout.dialog_confirm, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setCancelable(false);
        buttonConfirm = (Button) dialogView.findViewById(R.id.buttonConfirm);
        ReSendButton = (Button) dialogView.findViewById(R.id.btnresend);
        ReSendButton.setVisibility(View.GONE);
        CancelButton = (Button) dialogView.findViewById(R.id.btncancel);

        final EditText editTextConfirmOtp = (EditText) dialogView.findViewById(R.id.editTextOtp);
        editTextConfirmOtp.setVisibility(View.GONE);
        editTextConfirmOtp1 = (EditText) dialogView.findViewById(R.id.editTextOtp1);
        editTextConfirmOtp2 = (EditText) dialogView.findViewById(R.id.editTextOtp2);
        editTextConfirmOtp3 = (EditText) dialogView.findViewById(R.id.editTextOtp3);
        editTextConfirmOtp4 = (EditText) dialogView.findViewById(R.id.editTextOtp4);
        TextView txtView = (TextView) dialogView.findViewById(R.id.tView);


//        //auto detect OTP broadcast receiver
//        new OTP_Receiver().setEditText(editTextConfirmOtp1,editTextConfirmOtp2,editTextConfirmOtp3,editTextConfirmOtp4,editTextConfirmOtp5,editTextConfirmOtp6);

        startSMSListener();

        editTextConfirmOtp1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextConfirmOtp1.getText().toString().length() == 1)     //size as per your requirement
                {
                    editTextConfirmOtp2.requestFocus();
                    txtView.setVisibility(View.GONE);
                } else {
                    txtView.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });

        editTextConfirmOtp2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextConfirmOtp2.getText().toString().length() == 1)     //size as per your requirement
                {
                    editTextConfirmOtp3.requestFocus();
                    txtView.setVisibility(View.GONE);
                } else {
                    txtView.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });
        editTextConfirmOtp3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextConfirmOtp3.getText().toString().length() == 1)     //size as per your requirement
                {
                    editTextConfirmOtp4.requestFocus();
                    txtView.setVisibility(View.GONE);
                } else {
                    txtView.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }

        });


        alert.setView(dialogView);
        AlertDialog alert11 = alert.create();
        alert11.setCanceledOnTouchOutside(false);
        alert11.show();

        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert11.cancel();

            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert11.setCancelable(false);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

                otpString = editTextConfirmOtp1.getText().toString() + editTextConfirmOtp2.getText().toString() + editTextConfirmOtp3.getText().toString() + editTextConfirmOtp4.getText().toString();
                if ((editTextConfirmOtp1.getText().toString().length() == 0) && (editTextConfirmOtp2.getText().toString().length() == 0) && (editTextConfirmOtp3.getText().toString().length() == 0) && (editTextConfirmOtp4.getText().toString().length() == 0)) {

                    Toast.makeText(getApplicationContext(), "Please Enter your OTP", Toast.LENGTH_SHORT)
                            .show();

                } else if (editTextConfirmOtp1.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Enter your OTP", Toast.LENGTH_SHORT)
                            .show();
                } else if (otpString != null) {
                    alert11.cancel();

//                    Toast.makeText(LoginActivity.this, "OTP verified", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    VerifyOTP();
                }
            }
        });

        ReSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReSendButton.setVisibility(View.GONE);
                reSendCount = reSendCount + 1;
                alert11.cancel();
                RequestOTP();
                Popup();
                Toast.makeText(getApplicationContext(), "Please wait..Requesting OTP", Toast.LENGTH_LONG)
                        .show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ReSendButton.setVisibility(View.VISIBLE);
                    }
                }, 40000);

            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if (reSendCount == 2) {
                    ReSendButton.setVisibility(View.GONE);
                } else if ((editTextConfirmOtp1.getText().toString().length() == 0) && (editTextConfirmOtp2.getText().toString().length() == 0) && (editTextConfirmOtp3.getText().toString().length() == 0) && (editTextConfirmOtp4.getText().toString().length() == 0)) {
                    ReSendButton.setVisibility(View.VISIBLE);
                }
            }
        }, 20000);
    }

    private void startSMSListener() {
        try {
            smsReceiver = new SMSReceiver();
            smsReceiver.setOTPListener(this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            this.registerReceiver(smsReceiver, intentFilter);

            SmsRetrieverClient client = SmsRetriever.getClient(this);

            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // API successfully started
                    Log.e(TAG, "API...............started");
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Fail to start API
                    Log.e(TAG, "2  API...............started");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOTPReceived(String otp) {
        //showToast("OTP Received: " + otp);
        String str = otp.split(": ")[1];
        Log.e("OTP", "........" + str);
        editTextConfirmOtp1.setText(str);
        editTextConfirmOtp2.setText(str.substring(1, 2));
        editTextConfirmOtp3.setText(str.substring(2, 3));
        editTextConfirmOtp4.setText(str.substring(3, 4));
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
            smsReceiver = null;
        }
    }


    @Override
    public void onOTPTimeOut() {
        showToast("OTP Time out");
    }

    @Override
    public void onOTPReceivedError(String error) {
        showToast(error);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null) {
            unregisterReceiver(smsReceiver);
        }
    }

    //Method to request otp
    private void RequestOTP() {
        ProgressDialog progressBar = ShowProgressDialog.createProgressDialog(context);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<OTPValidation> call = customer_interface.sendOTPatLoginMethod("bearer " + Token, prefManager.getCustId().get(CUST_ID), edMobileNo.getText().toString());
        call.enqueue(new Callback<OTPValidation>() {
            @Override
            public void onResponse(Call<OTPValidation> call, Response<OTPValidation> response) {

                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressBar.dismiss();
                        OTPValidation otpValid = response.body();
//                        Log.e("otpstatus...", otpValid.getOTPStatus());
                        String otpValue = otpValid.getOTPStatus();
                        String otpSubstring = "OK";
                        if (otpValue.contains(otpSubstring)) {
                            otpStatus = "OK";
                        } else {
                            otpStatus = "Mobile Number is incorrect!";
                            Log.e("Check msg", otpStatus);
                            //Toast.makeText(context, "Mobile Number is incorrect!", Toast.LENGTH_LONG).show();
                        }
                        break;

                    case 404:
                        progressBar.dismiss();
                        CustomToast.showToast(context, "Problem in sending OTP, please try again.");
                        break;

                    case 307:
                        progressBar.dismiss();
                        CustomToast.showToast(context, "Server is not responding." + response.code());
                        break;

                    case 500:
                        CustomToast.showToast(context, "Server Error ");
                        progressBar.dismiss();
                        break;
                }
            }

            @Override
            public void onFailure(Call<OTPValidation> call, Throwable t) {
                progressBar.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(context, "Rotp Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(context, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void VerifyOTP() {
        ProgressDialog progressBar = ShowProgressDialog.createProgressDialog(LoginActivity.this);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<OTPApprove> call = customer_interface.approveOTPatLoginMethod("bearer " + Token,
                prefManager.getCustId().get(CUST_ID), edMobileNo.getText().toString(), otpString);
        call.enqueue(new Callback<OTPApprove>() {
            @Override
            public void onResponse(Call<OTPApprove> call, Response<OTPApprove> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressBar.dismiss();
                        OTPApprove otpApprove = response.body();
                        if (otpApprove.isOTPVerified()) {
                            Toast.makeText(LoginActivity.this, otpApprove.getOTPStatus(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            showOTpAlert();
                        }
                        break;
                    case 404:
                        progressBar.dismiss();
                        CustomToast.showToast(LoginActivity.this, "Problem in sending OTP, please try again.");
                        break;

                    case 307:
                        progressBar.dismiss();
                        CustomToast.showToast(LoginActivity.this, "Server is not responding." + response.code());
                        break;

                    case 500:
                        CustomToast.showToast(LoginActivity.this, "Server Error ");
                        progressBar.dismiss();
                        break;
                }
            }

            @Override
            public void onFailure(Call<OTPApprove> call, Throwable t) {
                progressBar.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(LoginActivity.this, "Votp Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(LoginActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void showOTpAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("OTP not verified");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });

        alertDialog.show();
    }

    private boolean ValidateNumber() {
        String NumberInput = edMobileNo.getText().toString();

        if (!PHONE_PATTERN.matcher(NumberInput).matches()) {
            edMobileNo.setError("please enter valid mobile number");
            return false;
        } else if (NumberInput.isEmpty()) {
            edMobileNo.setError("Please enter valid mobile number");
            return false;
        } else {

            if (!NumberInput.matches(regNumber)) {
                edMobileNo.setError("please enter valid mobile number");
                return false;
            } else {
                return true;
            }

        }
    }

    private boolean ValidatePassword() {
        String NumberInput = edPassword.getText().toString();

        if (NumberInput.isEmpty()) {
            edPassword.setError("please enter password");
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);
        MyInternetCheck.getInstance().setConnectivityListener(LoginActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                 InvalidAlgorithmParameterException
                 | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException |
                 IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*protected void onPostExecute(String s) {
        scurrentversion= BuildConfig.VERSION_NAME;
        if(slatestversion!=null){
            float cVersion=Float.parseFloat(scurrentversion);
            float lversion=Float.parseFloat(slatestversion);
            Log.e("latestversion....",String.valueOf(lversion));
            Log.e("currentversion....",String.valueOf(cVersion));
            if(cVersion<lversion){
                updateAlertDialog();

            }
            else
            {

            }

        }
    }*/
  /*  private void updateAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setTitle("Update Available");
        builder.setCancelable(false);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }*/

    /*public class GetLatestVersion extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                slatestversion= Jsoup.connect("https://play.google.com/store/apps/details?id="
                        +getPackageName())
                        .timeout(30000)
                        .get()
                        .select("div.hAyfc:nth-child(4)>"+"span:nth-child(2) >div:nth-child(1)"+"> span:nth-child(1)")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return slatestversion;
        }
        @Override
        protected void onPostExecute(String s) {
            scurrentversion= BuildConfig.VERSION_NAME;
            if(slatestversion!=null){
                float currentVersion=Float.parseFloat(scurrentversion);
                float playstoreversion=Float.parseFloat(slatestversion);
                Log.e("playstoreversion....",String.valueOf(playstoreversion));
                Log.e("currentversion....",String.valueOf(currentVersion));
                if(currentVersion<playstoreversion){
                    updateAlertDialog();
                }
                else{
//
                }
            }
        }
        private void updateAlertDialog(){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            //builder.setTitle(getResources().getString(R.string.app_name));
            builder.setTitle("Minor Updates");
            builder.setMessage("1.New Enquiry \n\n 2.Advertisement");
            builder.setCancelable(false);
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                    dialog.dismiss();
                }
            });

            builder.show();
        }
    }*/
}

