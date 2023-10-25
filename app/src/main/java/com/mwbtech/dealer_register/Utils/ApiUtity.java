package com.mwbtech.dealer_register.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.PojoClass.AccountVerify;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.PojoClass.ErrorBodyResponse;
import com.mwbtech.dealer_register.PojoClass.OTPApprove;
import com.mwbtech.dealer_register.PojoClass.OTPVerificationResponse;
import com.mwbtech.dealer_register.PojoClass.SocialLoginRequest;
import com.mwbtech.dealer_register.PojoClass.StateCityResponse;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiUtity {

    public static void callChangePasswordApi(Context context, ChangePasswordRequest request, String Token, final APIResponseListener<Object> listener) {

        if (!ConnectivityReceiver.isConnected()) {
            CommonUtils.showToast(context, "Sorry! Not connected to internet");
            return;
        }
        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(context, "Please wait");
        Customer_Interface customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<DealerRegister> call = customer_interface.changePassword("bearer " + Token, request.getMobile(), request.getNewPassowrd());
        call.enqueue(new Callback<DealerRegister>() {
            @Override
            public void onResponse(Call<DealerRegister> call, Response<DealerRegister> response) {
                int statusCode = response.code();
                Log.e("Status", "code...." + statusCode);
                Log.e("Token", ".........." + Token);
                progressBar.dismiss();
                switch (statusCode) {
                    case 200:

                        listener.onReceiveResponse(response.body());
                        break;

                    case 307:

                        listener.onResponseFailed("Server is not responding." + response.code());
                        break;
                    default:

                        ErrorBodyResponse errorBodyResponse = new ErrorBodyResponse();
                        try {
                            errorBodyResponse = new Gson().fromJson(response.errorBody().string(), ErrorBodyResponse.class);
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        }
                }
            }

            @Override
            public void onFailure(Call<DealerRegister> call, Throwable t) {
                progressBar.dismiss();
                listener.onResponseFailed(t.getLocalizedMessage());
                Log.e("failure ", "error..." + t.getMessage());
            }
        });
    }

/*
    public static void callLoginApi(Context context, LoginRequest request, String Token, final APIResponseListener<DealerRegister> listener) {

        if (!ConnectivityReceiver.isConnected()) {
            CommonUtils.showToast(context, "Sorry! Not connected to internet");
            return;
        }


        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(context, "Please wait");
        Customer_Interface customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<DealerRegister> call = customer_interface.loginCustomerDetailsMethod(request.getMobileNo(), request.getPassword(), );
        call.enqueue(new Callback<DealerRegister>() {
            @Override
            public void onResponse(Call<DealerRegister> call, Response<DealerRegister> response) {
                int statusCode = response.code();
                Log.e("Status", "code...." + statusCode);
                Log.e("Token", ".........." + Token);
                progressBar.dismiss();
                switch (statusCode) {
                    case 200:

                        listener.onReceiveResponse(response.body());
                        break;

                    case 307:

                        listener.onResponseFailed("Server is not responding." + response.code());
                        break;
                    default:

                        ErrorBodyResponse errorBodyResponse = new ErrorBodyResponse();
                        try {
                            errorBodyResponse = new Gson().fromJson(response.errorBody().string(), ErrorBodyResponse.class);
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        }
                }
            }

            @Override
            public void onFailure(Call<DealerRegister> call, Throwable t) {
                progressBar.dismiss();
                listener.onResponseFailed(t.getLocalizedMessage());
                Log.e("failure ", "error..." + t.getMessage());
            }
        });
    }
*/

    public static void generateOtpApi(Context context, String deviceId, String request, String Token, final APIResponseListener<AccountVerify> listener) {

        if (!ConnectivityReceiver.isConnected()) {
            CommonUtils.showToast(context, "Sorry! Not connected to internet");
            return;
        }


        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(context, "Please wait");
        Customer_Interface customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<AccountVerify> call = customer_interface.ResendOTP(deviceId, request);
        call.enqueue(new Callback<AccountVerify>() {
            @Override
            public void onResponse(Call<AccountVerify> call, Response<AccountVerify> response) {
                int statusCode = response.code();
                Log.e("Status", "code...." + statusCode);
                Log.e("Token", ".........." + Token);
                progressBar.dismiss();
                switch (statusCode) {
                    case 200:
                        listener.onReceiveResponse(response.body());
                        break;
                    case 307:
                        listener.onResponseFailed("Server is not responding." + response.code());
                        break;
                    default:
                        ErrorBodyResponse errorBodyResponse = new ErrorBodyResponse();
                        try {
                            errorBodyResponse = new Gson().fromJson(response.errorBody().string(), ErrorBodyResponse.class);
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        }
                }
            }

            @Override
            public void onFailure(Call<AccountVerify> call, Throwable t) {
                progressBar.dismiss();
                listener.onResponseFailed(t.getLocalizedMessage());
                Log.e("failure ", "error..." + t.getMessage());
            }
        });
    }
/*

    public static void verifyOtpApi(Context context, String request, String otp, final APIResponseListener<OTPApprove> listener) {

        if (!ConnectivityReceiver.isConnected()) {
            CommonUtils.showToast(context, "Sorry! Not connected to internet");
            return;
        }


        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(context, "Please wait");
        Customer_Interface customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<OTPApprove> call = customer_interface.approveOTPatLoginMethod(request, otp);
        call.enqueue(new Callback<OTPApprove>() {
            @Override
            public void onResponse(Call<OTPApprove> call, Response<OTPApprove> response) {
                int statusCode = response.code();
                Log.e("Status", "code...." + statusCode);
                progressBar.dismiss();
                switch (statusCode) {
                    case 200:

                        listener.onReceiveResponse(response.body());
                        break;


                    default: {

                        ErrorBodyResponse errorBodyResponse = new ErrorBodyResponse();
                        try {
                            errorBodyResponse = new Gson().fromJson(response.errorBody().string(), ErrorBodyResponse.class);
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<OTPApprove> call, Throwable t) {
                progressBar.dismiss();
                listener.onResponseFailed(t.getLocalizedMessage());
                Log.e("failure ", "error..." + t.getMessage());
            }
        });
    }
*/


    /*public static void verifyOtpApi(Context context, String request, String otp, final APIResponseListener<OTPVerificationResponse> listener) {

        if (!ConnectivityReceiver.isConnected()) {
            CommonUtils.showToast(context, "Sorry! Not connected to internet");
            return;
        }


        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(context, "Please wait");
        Customer_Interface customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<OTPVerificationResponse> call = customer_interface.approveOTPatLoginMethod(request, otp);
        call.enqueue(new Callback<OTPVerificationResponse>() {
            @Override
            public void onResponse(Call<OTPVerificationResponse> call, Response<OTPVerificationResponse> response) {
                int statusCode = response.code();
                Log.e("Status", "code...." + statusCode);
                progressBar.dismiss();
                switch (statusCode) {
                    case 200:

                        listener.onReceiveResponse(response.body());
                        break;


                    default: {

                        ErrorBodyResponse errorBodyResponse = new ErrorBodyResponse();
                        try {
                            errorBodyResponse = new Gson().fromJson(response.errorBody().string(), ErrorBodyResponse.class);
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<OTPVerificationResponse> call, Throwable t) {
                progressBar.dismiss();
                listener.onResponseFailed(t.getLocalizedMessage());
                Log.e("failure ", "error..." + t.getMessage());
            }
        });
    }*/


    public static void verifyForgotPassOtpApi(Context context, String request, String otp, final APIResponseListener<OTPApprove> listener) {

        if (!ConnectivityReceiver.isConnected()) {
            CommonUtils.showToast(context, "Sorry! Not connected to internet");
            return;
        }
        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(context, "Please wait");
        Customer_Interface customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<OTPApprove> call = customer_interface.approveOTPatForgotMethod(request, otp);
        call.enqueue(new Callback<OTPApprove>() {
            @Override
            public void onResponse(Call<OTPApprove> call, Response<OTPApprove> response) {
                int statusCode = response.code();
                Log.e("Status", "code...." + statusCode);
                progressBar.dismiss();
                switch (statusCode) {
                    case 200:

                        listener.onReceiveResponse(response.body());
                        break;


                    default: {

                        ErrorBodyResponse errorBodyResponse = new ErrorBodyResponse();
                        try {
                            errorBodyResponse = new Gson().fromJson(response.errorBody().string(), ErrorBodyResponse.class);
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<OTPApprove> call, Throwable t) {
                progressBar.dismiss();
                listener.onResponseFailed(t.getLocalizedMessage());
                Log.e("failure ", "error..." + t.getMessage());
            }
        });
    }

    public static void registerUsingSocialMediaApi(Context context, SocialLoginRequest request, final APIResponseListener<DealerRegister> listener) {

        if (!ConnectivityReceiver.isConnected()) {
            CommonUtils.showToast(context, "Sorry! Not connected to internet");
            return;
        }


        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(context, "Please wait");
        Customer_Interface customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<DealerRegister> call = customer_interface.socialRegister(request);
        call.enqueue(new Callback<DealerRegister>() {
            @Override
            public void onResponse(Call<DealerRegister> call, Response<DealerRegister> response) {
                int statusCode = response.code();
                Log.e("Status", "code...." + statusCode);
                progressBar.dismiss();
                if (statusCode == 200) {
                    listener.onReceiveResponse(response.body());
                } else {
                    ErrorBodyResponse errorBodyResponse = new ErrorBodyResponse();
                    try {
                        errorBodyResponse = new Gson().fromJson(response.errorBody().string(), ErrorBodyResponse.class);
                        listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<DealerRegister> call, Throwable t) {
                progressBar.dismiss();
                listener.onResponseFailed(t.getLocalizedMessage());
                Log.e("failure ", "error..." + t.getMessage());
            }
        });
    }

    public static void loginUsingSocialMediaApi(Context context, SocialLoginRequest request, final APIResponseListener<DealerRegister> listener) {

        if (!ConnectivityReceiver.isConnected()) {
            CommonUtils.showToast(context, "Sorry! Not connected to internet");
            return;
        }


        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(context, "Please wait");
        Customer_Interface customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<DealerRegister> call = customer_interface.socialLogin(request.getSocialMediaToken());
        call.enqueue(new Callback<DealerRegister>() {
            @Override
            public void onResponse(Call<DealerRegister> call, Response<DealerRegister> response) {
                int statusCode = response.code();
                Log.e("Status", "code...." + statusCode);
                progressBar.dismiss();
                if (statusCode == 200) {
                    listener.onReceiveResponse(response.body());
                } else {
                    ErrorBodyResponse errorBodyResponse = new ErrorBodyResponse();
                    try {
                        errorBodyResponse = new Gson().fromJson(response.errorBody().string(), ErrorBodyResponse.class);
                        listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<DealerRegister> call, Throwable t) {
                progressBar.dismiss();
                listener.onResponseFailed(t.getLocalizedMessage());
                Log.e("failure ", "error..." + t.getMessage());
            }
        });
    }

    public static void validateUserApi(Context context, String number, final APIResponseListener<ErrorBodyResponse

            > listener) {

        if (!ConnectivityReceiver.isConnected()) {
            CommonUtils.showToast(context, "Sorry! Not connected to internet");
            return;
        }


        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(context, "Please wait");
        Customer_Interface customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<ErrorBodyResponse> call = customer_interface.validateUser(number);
        call.enqueue(new Callback<ErrorBodyResponse

                >() {
            @Override
            public void onResponse(Call<ErrorBodyResponse

                    > call, Response<ErrorBodyResponse

                    > response) {
                int statusCode = response.code();
                Log.e("Status", "code...." + statusCode);
                progressBar.dismiss();
                switch (statusCode) {
                    case 200:

                        listener.onReceiveResponse(response.body());
                        break;

                    case 307:

                        listener.onResponseFailed("Server is not responding." + response.code());
                        break;
                    default: {

                        ErrorBodyResponse errorBodyResponse = new ErrorBodyResponse();
                        try {
                            errorBodyResponse = new Gson().fromJson(response.errorBody().string(), ErrorBodyResponse.class);
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onResponseFailed(errorBodyResponse.getDisplayMessage());

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ErrorBodyResponse

                    > call, Throwable t) {
                progressBar.dismiss();
                listener.onResponseFailed(t.getLocalizedMessage());
                Log.e("failure ", "error..." + t.getMessage());
            }
        });
    }

    public static void getAddress(Context context, String pincode, final APIResponseListener<Object

            > listener) {

        if (!ConnectivityReceiver.isConnected()) {
            CommonUtils.showToast(context, "Sorry! Not connected to internet");
            return;
        }


//        ProgressDialog progressBar = ShowProgressDialog.customProgressDialog(context, "Please wait");
        Customer_Interface customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<StateCityResponse>> call = customer_interface.getStateCityFromPincode(pincode);
        call.enqueue(new Callback<List<StateCityResponse>>() {
            @Override
            public void onResponse(Call<List<StateCityResponse>> call, Response<List<StateCityResponse>> response) {
                int statusCode = response.code();
                Log.e("Status", "code...." + statusCode);
               // progressBar.dismiss();
                switch (statusCode) {
                    case 200:

                        listener.onReceiveResponse(response.body());
                        break;

                    case 307:

                        listener.onResponseFailed("Server is not responding." + response.code());
                        break;
                    default: {

                        Object errorBodyResponse = new ErrorBodyResponse();
                        try {
                            errorBodyResponse = new Gson().fromJson(response.errorBody().string(), ErrorBodyResponse.class);
                            listener.onResponseFailed("Failed");

                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onResponseFailed("Failed");

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<StateCityResponse>> call, Throwable t) {
           //     progressBar.dismiss();
                listener.onResponseFailed(t.getLocalizedMessage());
                Log.e("failure ", "error..." + t.getMessage());
            }

        });
    }

    public interface APIResponseListener<T> {

        void onReceiveResponse(T response);

        void onResponseFailed(String msg);

    }
}
