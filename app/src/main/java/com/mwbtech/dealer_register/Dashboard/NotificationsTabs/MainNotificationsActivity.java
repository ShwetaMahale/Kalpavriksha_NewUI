package com.mwbtech.dealer_register.Dashboard.NotificationsTabs;


import static com.mwbtech.dealer_register.Dashboard.Enquiry.EnquiryRequirementActivity.connectivityReceiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.Dashboard.MessagesList;
import com.mwbtech.dealer_register.Dashboard.NotificationList;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.DialogUtilsKt;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;
import com.mwbtech.dealer_register.internet_connection.MyInternetCheck;

/**
 * -------It is the screen the user will see when user press "Notifications" icon in toolbar which consists of
 * Two tabs, "Message" and "Notification" ---------
 **/
public class MainNotificationsActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    LinearLayoutCompat coordinatorLayout;
    public static MainNotificationsActivity instance;
    private MessagesList messagesList;
    private NotificationList notificationList;
    private TabLayout allTabs;
    IntentFilter intentFilter;
    ImageView imgBack;
    AppCompatImageView imgReload, deleteAll;
    private boolean isShowingNotifications = false;

    public static PrefManager prefManager;
    public static DealerRegister updateDealerRegister;

    /**
     * ----------Initializing All views, Tabs, Timer and Variables -------
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_notifications);
        // setTitle("Profile Update");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.red, this.getTheme()));
        } else {
            getWindow().setStatusBarColor(getResources().getColor(R.color.red));
        }
//        setHasOptionsMenu(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));

        updateDealerRegister = new DealerRegister();

        prefManager = new PrefManager(this);
        coordinatorLayout = findViewById(R.id.parentLayout);
        imgBack = findViewById(R.id.back);
        imgReload = findViewById(R.id.img_reload);
        deleteAll = findViewById(R.id.delete);
        intentFilter = new IntentFilter();
        connectivityReceiver = new ConnectivityReceiver();
        instance = MainNotificationsActivity.this;
        getAllWidgets();
        bindWidgetsWithAnEvent();
        setupTabLayout();
        imgBack.setOnClickListener((v) -> {
            onBackPressed();
            finish();
        });
        imgReload.setOnClickListener((v) -> {
            if(isShowingNotifications) notificationList.getNotification();
        });
        deleteAll.setOnClickListener(v ->
                DialogUtilsKt.confirm(MainNotificationsActivity.this, getString(R.string.are_you_sure_you_want_to_delete_all_notifications), getString(R.string.yes), getString(R.string.no), v1 -> notificationList.deleteAllNotifications(), null)
        );
        setCurrentTabFragment(1);
        //setupTabIcons();
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                onBackPressed();
//                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /**
     * --------Adding Icons For The Tabs----------
     **/

    private void getAllWidgets() {
        allTabs = (TabLayout) findViewById(R.id.tabs);
    }

    /**
     * -------------Initializing Fragments And Adding
     * Names For Fragments for the Tabs-----------
     **/

    private void setupTabLayout() {
        messagesList = new MessagesList();
        notificationList = new NotificationList();

        allTabs.addTab(allTabs.newTab().setText("Message"), true);
        allTabs.addTab(allTabs.newTab().setText("Notification"), false);
        allTabs.setVisibility(View.GONE);
        LinearLayout tabStrip = ((LinearLayout) allTabs.getChildAt(0));
       /* for(int i = 0; i < tabStrip.getChildCount(); i++) {
            if(i==1){
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(UpdateMainActivity.this,"Please click on Next",Toast.LENGTH_SHORT).show();
                    return true;}

            });}
            if(i==2){
                tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Toast.makeText(UpdateMainActivity.this,"Please click on Next",Toast.LENGTH_SHORT).show();
                                                       return true;}

                });
            }
               if(i==3){
                tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                              Toast.makeText(UpdateMainActivity.this,"Please click on Next",Toast.LENGTH_SHORT).show();
                                return true;

                    }

                });
            }
        }*/

    }

    public void showToast(String message) {
        Toast.makeText(MainNotificationsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * ----------Adding OnTabSelected Listener for the tabs------------
     **/

    private void bindWidgetsWithAnEvent() {
        allTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * ----------------Setting Fragments For Tabs According to Their Positions-----------------
     **/

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                isShowingNotifications = false;
                replaceFragment(messagesList);
                break;
            case 1:
                isShowingNotifications = true;
                replaceFragment(notificationList);
                break;
        }
    }

    /**
     * --------------------Adding Frame Container To The Activity-----------------
     **/

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    /**
     * -----------This Method Will Be Executed When User Press Home button--------
     **/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class).putExtra("isNewUser", false);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        MainNotificationsActivity.this.finish();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        showSnack(ConnectivityReceiver.isConnected());
    }

    /**
     * -------------------method to show teh user is not Connected to Internet-----------
     **/

    private void showSnack(boolean isConnected) {
        String message;
        if (!isConnected) {
            //message = "Good! Connected to Internet";
            message = "Sorry! Not connected to internet";
            //Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "" + message, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            //textView.setTextColor(Color.GREEN);
            textView.setTextColor(Color.RED);
            snackbar.show();

        } else {

        }
    }

    /**
     * Starting The Timer when App Is Resumed
     **/

    @Override
    protected void onResume() {
        super.onResume();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);
        MyInternetCheck.getInstance().setConnectivityListener(MainNotificationsActivity.this);
    }

    /**
     * Starting The Timer when App Is Paused
     **/

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(connectivityReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}