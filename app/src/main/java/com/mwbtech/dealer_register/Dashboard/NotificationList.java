package com.mwbtech.dealer_register.Dashboard;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.NotificationAdapter;
import com.mwbtech.dealer_register.Dashboard.Advertisement.MyAd.ShowAdsActivity;
import com.mwbtech.dealer_register.Dashboard.EnquiryReceived.EnquiryReceivedChatActivity;
import com.mwbtech.dealer_register.Dashboard.EnquirySent.EnquirySentChatActivity;
import com.mwbtech.dealer_register.PojoClass.Notification;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.DialogUtilsKt;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationList extends Fragment{
    Customer_Interface customer_interface;
    List<Notification> notificationModel;
    RecyclerView recyclerView;
    List<Notification> notificationList = new ArrayList<>();
    NotificationAdapter myNotification;
    TextView Txt_NotFound;
    int CustID;
    PrefManager prefManager;
    String Token;
    View customerView;

    /*@Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            if (activity instanceof BillingAddressActivity.CallToFragment) {
                callToBillFragment = (BillingAddressActivity.CallToFragment) activity;
            } else {
                throw new RuntimeException(activity.toString()
                        + " must implement OnGreenFragmentListener");
            }
        } catch (ClassCastException e) {
        }
    }*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        customerView = inflater.inflate(R.layout.activity_notification_list, null);
        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }
        return customerView;
    }
    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }
    }
*/
    private void showContent() {
//        setContentView(R.layout.activity_notification_list);
        initializeRecyclerView();
        initializeViews();
        initializeSharedData();
        getNotification();

    }
    private void initializeSharedData() {
        prefManager = new PrefManager(getContext());
        CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        Token = prefManager.getToken().get(TOKEN);
    }

    private void initializeViews() {
        Txt_NotFound = customerView.findViewById(R.id.not_found_txt);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));

    }

    private void showNoSignalContent() {
//        setContentView(R.layout.no_signal_layout);
        Button tryButton = customerView.findViewById(R.id.tryBtn);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
        tryButton.setOnClickListener(view -> checkInternetConnected());
    }
    private void checkInternetConnected() {
        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }
    }
    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) customerView.findViewById(R.id.recyclerViewNotification);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
    public void getNotification() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(getContext(),"Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        Gson gson=new Gson();
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<Notification>> call = customer_interface.GetUserNotification("bearer " + Token, CustID);
        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        notificationModel = new ArrayList<>(response.body());
                        notificationList.clear();
                        Log.e("model...",gson.toJson(notificationModel));
                        if (notificationModel.size() != 0) {
                            for (int i = 0; i < notificationModel.size(); i++) {
                                Notification notificationModels = notificationModel.get(i);
                                notificationList.add(new Notification(notificationModels.getID(),notificationModels.getCustID(),notificationModels.getPushNotification(),notificationModels.getNotificationDateStr(),notificationModels.getTitle(),notificationModels.getCategoryName(),notificationModels.getImageURL(),notificationModels.getQueryID(),notificationModels.getCustomerDetailsQuestionDTO(),notificationModels.getNotificationType(),notificationModels.getCreatedBy(),notificationModels.getCreatedDate()));
                                }
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.getRecycledViewPool().clear();
                            myNotification = new NotificationAdapter(getContext(), notificationList, new NotificationAdapter.AdClickEvent() {
                                @Override
                                public void adclick(Notification notification) {
                                    NotificationList.this.adclick(notification);
                                }

                                @Override
                                public void deleteNotification(Notification notification) {
                                    DialogUtilsKt.confirm(requireContext(), getString(R.string.are_you_sure_you_want_to_delete_this_notifications), getString(R.string.yes), getString(R.string.no), v1 ->  NotificationList.this.deleteSelectedNotification(notification), null);
                                }
                            });
                            recyclerView.setAdapter(myNotification);
                            myNotification.notifyDataSetChanged();
                            Txt_NotFound.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            Txt_NotFound.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 404:
                        progressDialog.dismiss();
                        recyclerView.setVisibility(View.GONE);
                        Txt_NotFound.setVisibility(View.VISIBLE);
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(getContext(), "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(getContext());
                        break;
                }

            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(getContext(), "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(getContext(), "Bad response from server.. Try again later ");
                }
            }
        });
    }

    public void deleteAllNotifications() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(getContext(),"Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        Gson gson=new Gson();
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<Object> call = customer_interface.deleteAllNotifications("bearer " + Token, CustID, new ArrayList<>());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                       getNotification();
                        break;
                    case 404:

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(getContext(), "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(getContext());
                        break;
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(getContext(), "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(getContext(), "Bad response from server.. Try again later ");
                }
            }
        });
    }

    public void deleteSelectedNotification(Notification notification) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(getContext(),"Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<Notification>> call = customer_interface.deleteNotification("bearer " + Token, CustID, new ArrayList<>(Collections.singletonList(notification)));
        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        getNotification();
                        break;
                    case 404:

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(getContext(), "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(getContext());
                        break;
                }

            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(getContext(), "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(getContext(), "Bad response from server.. Try again later ");
                }
            }
        });
    }

    public void adclick(Notification notification) {
        Gson gson=new Gson();
        if(notification.getTitle().contains("New Message From")){
            Log.e("Replylist...","msg");
            Log.e("sentmodel....",gson.toJson(notification));
            Log.e("rcvid....",String.valueOf(notification.getCustomerDetailsQuestionDTO().getCustID()));
            startActivity(new Intent(getActivity(), EnquirySentChatActivity.class).
                    putExtra("notification",true).
                    putExtra("recId",notification.getCreatedBy()).
                    putExtra("phone_no", notification.getCustomerDetailsQuestionDTO().getMobileNumber()).
                    putExtra("QryID", notification.getQueryID()).
                    putExtra("name", notification.getCustomerDetailsQuestionDTO().getFirmName()).
                    putExtra("email", notification.getCustomerDetailsQuestionDTO().getEmailID()).
                    putExtra("purpose", "").
                    putExtra("demand", "").
                    putExtra("city", notification.getCustomerDetailsQuestionDTO().getCityName()));
        }
        else{
            if(notification.getNotificationType()!=null){
                Log.e("NewEnquiry...","msg");
                startActivity(new Intent(getActivity(), EnquiryReceivedChatActivity.class).
                        putExtra("notification",true).
                        putExtra("recId",notification.getCustomerDetailsQuestionDTO().getCustID()).
                        putExtra("phone_no", notification.getCustomerDetailsQuestionDTO().getMobileNumber()).
                        putExtra("QryID", notification.getQueryID()).
                        putExtra("name", notification.getCustomerDetailsQuestionDTO().getFirmName()).
                        putExtra("email", notification.getCustomerDetailsQuestionDTO().getEmailID()).
                        putExtra("purpose", "").
                        putExtra("demand", "").
                        putExtra("city", notification.getCustomerDetailsQuestionDTO().getCityName()));
            }
            else{
                startActivity(new Intent(getActivity(), ShowAdsActivity.class));
            }
        }

    }/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                onBackPressed();
//                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        NotificationList.this.finish();
    }*/
}