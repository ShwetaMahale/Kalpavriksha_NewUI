package com.mwbtech.dealer_register.Profile.ProfileMain;

import static com.mwbtech.dealer_register.Dashboard.Enquiry.EnquiryRequirementActivity.connectivityReceiver;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.mwbtech.dealer_register.LoginRegister.LoginActivity;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.Profile.BankDetails.BankDetailsActvity;
import com.mwbtech.dealer_register.Profile.BillingDetails.BillingAddressActivity;
import com.mwbtech.dealer_register.Profile.CustomerDetails.CustomerDetails;
import com.mwbtech.dealer_register.Profile.TaxDetails.TaxRegistrationActivity;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;
import com.mwbtech.dealer_register.internet_connection.MyInternetCheck;

/** -------It Is the First Screen that user will see after Successful
 * Account Creation And Logged In For The First Time which consists
 * two tabs "Customer Details" and "Billing Address"---------**/
public class MainActivity extends AppCompatActivity implements  CustomerDetails.CallToBillFragment, BillingAddressActivity.CallToFragment, TaxRegistrationActivity.CallToBankFragment,BankDetailsActvity.CallToTaxFragment,ConnectivityReceiver.ConnectivityReceiverListener {

    LinearLayoutCompat coordinatorLayout;
    public static MainActivity instance;
    private CustomerDetails customerDetails;
    private BillingAddressActivity billingAddresssActivity;
    private TaxRegistrationActivity taxRegistrationActivity;
    private BankDetailsActvity bankDetailsActvity;
    AlertDialog dialog;
    private TabLayout allTabs;
    IntentFilter intentFilter;
    Toolbar toolbar;
    public static PrefManager prefManager;
    public static DealerRegister dealerRegister;
    public static DealerRegister creation;
    public static int salesmanID = 1599;

    /**----------Initializing All views, Tabs, Timer and Variables ------- **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.red, this.getTheme()));
        } else {
            getWindow().setStatusBarColor(getResources().getColor(R.color.red));
        }
        setTitle(R.string.app_name);

        dealerRegister = new DealerRegister();
        prefManager = new PrefManager(this);
        coordinatorLayout = findViewById(R.id.coordinator);
        intentFilter = new IntentFilter();
        connectivityReceiver = new ConnectivityReceiver();
        instance = this;
        getAllWidgets();
        bindWidgetsWithAnEvent();
        setupTabLayout();
        setupTabIcons();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/

    }

/** --------Adding Icons For The Tabs----------**/
    private void setupTabIcons() {
        allTabs.getTabAt(0).setIcon(R.drawable.customer1);
        allTabs.getTabAt(1).setIcon(R.drawable.billing);
        // allTabs.getTabAt(2).setIcon(R.drawable.tax);
        //allTabs.getTabAt(3).setIcon(R.drawable.bank);
        LinearLayout tabStrip = ((LinearLayout)allTabs.getChildAt(0));
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            // View next=tabStrip.getChildAt(i).findViewById(R.id.btnNext);


                tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                           // showToast("Please enter valid details");
                            Log.e("TTTT","Ontouch clicked......");
                            return true;
                    }

                });}


        }


    private void getAllWidgets() {
        allTabs = (TabLayout) findViewById(R.id.tabs);
    }

    /** -------------Initializing Fragments And Adding
     * Names For Fragments for the Tabs-----------**/
    private void setupTabLayout() {
        customerDetails = new CustomerDetails();
        billingAddresssActivity = new BillingAddressActivity();
        //taxRegistrationActivity = new TaxRegistrationActivity();
        //bankDetailsActvity = new BankDetailsActvity();

        allTabs.addTab(allTabs.newTab().setText(R.string.txt_customerDetails), true);
        allTabs.addTab(allTabs.newTab().setText(R.string.txt_billingDetails));
//        replaceFragment(customerDetails,true);
        //allTabs.addTab(allTabs.newTab().setText("TAX REGISTRATION"));
        //allTabs.addTab(allTabs.newTab().setText("BANK DETAILS"));

    }

    /**----------Adding OnTabSelected Listener for the tabs------------**/
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

/**----------------Setting Fragments For Tabs According to Their Positions-----------------**/
    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                replaceFragment(customerDetails);
                break;
            case 1:
                replaceFragment(billingAddresssActivity/*, true*/);
                break;
//            case 2 :
//                replaceFragment(taxRegistrationActivity);
//                break;
//            case 3 :
//                replaceFragment(bankDetailsActvity);
//                break;

        }
    }

    /**--------------------Adding Frame Container To The Activity-----------------**/
    public void replaceFragment(Fragment fragment/*, boolean add*/) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private Fragment getVisibleFragment() {
        Fragment visibleFragment = null;
        getSupportFragmentManager().getFragments();
        for(Fragment fragment : getSupportFragmentManager().getFragments()){
            if(fragment.isVisible() && fragment.isAdded()){
                visibleFragment = fragment;
            }
        }
       return  visibleFragment;
    }


    /**-----------This Method Will Be Executed When User Press Back button--------**/
    @Override
    public void onBackPressed() {
      //  super.onBackPressed();

       /* Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        MainActivity.this.finish();*/
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (currentFragment instanceof CustomerDetails) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.text_go_back);
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();
                }
            });

            dialog = builder.create();
            dialog.show();
        } else if (currentFragment instanceof BillingAddressActivity) {
            ((BillingAddressActivity) currentFragment).onBackClicked();
        } else if (currentFragment instanceof TaxRegistrationActivity) {
            ((TaxRegistrationActivity) currentFragment).onBackClicked();
        } else if (currentFragment instanceof BankDetailsActvity) {
            ((BankDetailsActvity) currentFragment).onBackClicked();
        }
    }

    /**-----------This Method Will Be Executed When User Press Home button--------**/

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

    /**---------Overriding the method of CallToFragment interface------**/

    @Override
    public void communicateFragment(int pos) {
        try {
            /*if(pos < allTabs.getSelectedTabPosition()){
                getSupportFragmentManager().popBackStack();
            }*/
            setCurrentTabFragment(pos);
            allTabs.getTabAt(pos).select();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

/**---------Overriding the method of CallToBillFragment interface------**/
    @Override
    public void callingBillingFragment(int pos) {
        try {
//            billingAddresssActivity = new BillingAddressActivity();
//            replaceFragment(billingAddresssActivity,true);
            setCurrentTabFragment(pos);
            allTabs.getTabAt(pos).select();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**---------Overriding the method of CallToBankFragment interface------**/

    @Override
    public void callingBankingFragment(int pos) {
        try {
//           replaceFragment(bankDetailsActvity,true);
            setCurrentTabFragment(pos);
            allTabs.getTabAt(pos).select();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**---------Overriding the method of CallToTaxFragment interface------**/

    @Override
    public void callingTaxFragment(int pos) {
        try {
//            replaceFragment(taxRegistrationActivity,true);
            setCurrentTabFragment(pos);
            allTabs.getTabAt(pos).select();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        showSnack(ConnectivityReceiver.isConnected());
    }

    /**-------------------method to show teh user is not Connected to Internet-----------**/
    private void showSnack(boolean isConnected) {
        String message;
        if (!isConnected) {
            // message = "Good! Connected to Internet";
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
           /* message = "Sorry! Not connected to internet";
            //Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, ""+message, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();*/
        }
    }

    /** Starting The Timer when App Is Resumed**/
    @Override
    protected void onResume() {
        super.onResume();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);
        MyInternetCheck.getInstance().setConnectivityListener(MainActivity.this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityReceiver);
    }
}