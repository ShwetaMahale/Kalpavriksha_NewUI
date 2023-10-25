package com.mwbtech.dealer_register.Splash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.LoginRegister.RegisterActivity;
import com.mwbtech.dealer_register.PojoClass.AdDetailsModel;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.PojoClass.SocialLoginRequest;
import com.mwbtech.dealer_register.PojoClass.TokenObject;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash_Sec_Activity extends AppCompatActivity {
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    CallbackManager callbackManager;
    private final int RC_SIGN_IN = 101;
    Customer_Interface customer_interface;
    PrefManager prefManager;
    private LoginManager loginManager;
    private FirebaseAuth mAuth;
    private Button fbLoginButton, googleLoginButton, registerButton;
    String Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_sec);

        prefManager = new PrefManager(this);
        googleLoginButton = findViewById(R.id.btn_google_login);
        fbLoginButton = findViewById(R.id.btn_facebook_login);
        registerButton = findViewById(R.id.signupBtn);
        printHashKey(this);
        mAuth = FirebaseAuth.getInstance();

//        FacebookSdk.sdkInitialize(Splash_Sec_Activity.this);
        callbackManager = CallbackManager.Factory.create();
        facebookLogin();
        googleLoginButton.setOnClickListener((view) -> googleSignIn());
        fbLoginButton.setOnClickListener(v -> loginWithFacebook());
        registerButton.setOnClickListener((view) -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void loginWithFacebook() {
        loginManager.logInWithReadPermissions(
                Splash_Sec_Activity.this,
                Arrays.asList("email", "public_profile"));
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

    private void facebookLogin() {
        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                /* GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {

                    if (object != null) {
                        try {
                            String name = object.getString("name");
                            String email = object.getString("email");
                            String fbUserID = object.getString("id");
                            disconnectFromFacebook();
                            getFacebookLogin(fbUserID, email, name);
                        } catch (JSONException | NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString(
                        "fields",
                        "id, name, email, gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();*/
            }

            @Override
            public void onCancel() {
                Log.v("LoginScreen", "---onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                // here write code when get error
                Log.v("LoginScreen", "----onError: "
                        + error.getMessage());
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("LoginScreen", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("LoginScreen", "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        getFacebookLogin(token.getToken(), user.getEmail(), user.getDisplayName());
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("LoginScreen", "signInWithCredential:failure", task.getException());
                        Toast.makeText(Splash_Sec_Activity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                    }
                });
    }

    public void disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                graphResponse -> LoginManager.getInstance().logOut())
                .executeAsync();
    }
    private void googleSignIn() {
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    private void GetTokenFromServer() {
        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(Splash_Sec_Activity.this, "Please wait");
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
                    Toast.makeText(Splash_Sec_Activity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(Splash_Sec_Activity.this, "Bad response from server.. Try again later ");
                }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Splash_Sec_Activity.this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            socialLoginApi(account, Splash_Sec_Activity.this);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(Splash_Sec_Activity.class.getSimpleName(), "signInResult:failed code=" + e.getStatusCode());
            //  updateUI(null);
        }
    }

    private void socialLoginApi(GoogleSignInAccount account, Context context) {
        SocialLoginRequest socialLoginRequest = new SocialLoginRequest();
        socialLoginRequest.setSocialMediaToken(account.getId());
        socialLoginRequest.setEmail(account.getEmail());
        socialLoginRequest.setName(account.getDisplayName());
        socialLoginRequestSendToServer(socialLoginRequest);
        /*ApiUtity.registerUsingSocialMediaApi(context, socialLoginRequest, new ApiUtity.APIResponseListener<DealerRegister>() {
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

    private void getFacebookLogin(String token, String email, String username) {
        SocialLoginRequest socialLoginRequest = new SocialLoginRequest();
        socialLoginRequest.setSocialMediaToken(token);
        socialLoginRequest.setEmail(email);
        socialLoginRequest.setName(username);
        socialLoginRequestSendToServer(socialLoginRequest);
    }

    private void socialLoginRequestSendToServer(SocialLoginRequest request) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(this, "");
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
                                Integer.parseInt(dealerRegister.getUserType()), dealerRegister.getMobileNumber());
                        startActivity(new Intent(Splash_Sec_Activity.this, DashboardActivity.class)
                                .putExtra("adUrl", dealerRegister)
                                .putExtra("loginurl", dealerRegister.getAdUserID())
                                .putExtra("isNewUser", false)
                        );
                        break;
                    case 404:
                        progressDialog.dismiss();
                        CustomToast.showToast(Splash_Sec_Activity.this, "Invalid user details");
                        break;

                    case 307:
                        progressDialog.dismiss();
                        CustomToast.showToast(Splash_Sec_Activity.this, "Server is not responding." + response.code());
                        break;

                    case 500:
                        CustomToast.showToast(Splash_Sec_Activity.this, "Server Error ");
                        progressDialog.dismiss();
                        break;
                    case 401:
                        CustomToast.showToast(Splash_Sec_Activity.this, "Unauthorized");
                        progressDialog.dismiss();
                        break;
                    case 503:
                        progressDialog.dismiss();
                        CustomToast.showToast(Splash_Sec_Activity.this, "Please contact Admin for login");
                        break;
                }
            }

            @Override
            public void onFailure(Call<DealerRegister> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(Splash_Sec_Activity.this, "login Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(Splash_Sec_Activity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Splash_Sec_Activity.this.finish();
    }
}