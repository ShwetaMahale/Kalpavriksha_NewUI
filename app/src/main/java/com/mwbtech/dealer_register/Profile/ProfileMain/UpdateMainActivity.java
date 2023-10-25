package com.mwbtech.dealer_register.Profile.ProfileMain;


import static com.mwbtech.dealer_register.Dashboard.Enquiry.EnquiryRequirementActivity.connectivityReceiver;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.Profile.BankDetails.UpdateBankDetails;
import com.mwbtech.dealer_register.Profile.BillingDetails.UpdateBillingAddress;
import com.mwbtech.dealer_register.Profile.CustomerDetails.UpdateCustomer;
import com.mwbtech.dealer_register.Profile.TaxDetails.UpdateTaxRegistration;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;
import com.mwbtech.dealer_register.internet_connection.MyInternetCheck;
/** -------It Is the Screen the user will see when user press "Update Profile" in Drawer which consists
 * Four tabs "Update Customer Details", "Update Billing Address",
 * "Update Tax Registration" and Update bank details---------**/
public class UpdateMainActivity extends AppCompatActivity implements  UpdateCustomer.CallToBillFragment, UpdateBillingAddress.CallToFragment, UpdateTaxRegistration.CallToBankFragment,UpdateBankDetails.CallToTaxFragment,ConnectivityReceiver.ConnectivityReceiverListener{

    LinearLayoutCompat coordinatorLayout;
    public static UpdateMainActivity instance;
    private UpdateCustomer customerDetails;
    private UpdateBillingAddress billingAddresssActivity;
    private UpdateTaxRegistration taxRegistrationActivity;
    private UpdateBankDetails bankDetailsActvity;
    AlertDialog dialog;
    private TabLayout allTabs;
    IntentFilter intentFilter;
    Toolbar toolbar;

    public static PrefManager prefManager;
    public static DealerRegister updateDealerRegister;
    /**----------Initializing All views, Tabs, Timer and Variables ------- **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // setTitle("Profile Update");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.red, this.getTheme()));
        } else {
            getWindow().setStatusBarColor(getResources().getColor(R.color.red));
        }

        updateDealerRegister = new DealerRegister();

        prefManager = new PrefManager(this);
        coordinatorLayout = findViewById(R.id.coordinator);
        intentFilter = new IntentFilter();
        connectivityReceiver = new ConnectivityReceiver();
        instance=UpdateMainActivity.this;
        getAllWidgets();
        bindWidgetsWithAnEvent();
        setupTabLayout();
        //setupTabIcons();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    /** --------Adding Icons For The Tabs----------**/

    private void setupTabIcons() {
        allTabs.getTabAt(0).setIcon(R.drawable.customer1);
        allTabs.getTabAt(1).setIcon(R.drawable.billing);
        allTabs.getTabAt(2).setIcon(R.drawable.tax);
        allTabs.getTabAt(3).setIcon(R.drawable.bank);

    }

    private void getAllWidgets() {
        allTabs = (TabLayout) findViewById(R.id.tabs);
    }
    /** -------------Initializing Fragments And Adding
     * Names For Fragments for the Tabs-----------**/

    private void setupTabLayout() {
        customerDetails = new UpdateCustomer();
        billingAddresssActivity = new UpdateBillingAddress();
        taxRegistrationActivity = new UpdateTaxRegistration();
        bankDetailsActvity = new UpdateBankDetails();

        allTabs.addTab(allTabs.newTab().setText(R.string.txt_updatecustomerdetails),true);
        allTabs.addTab(allTabs.newTab().setText(R.string.txt_updatebilling),false);
        allTabs.addTab(allTabs.newTab().setText(R.string.txt_updatetxt),false);
        //allTabs.addTab(allTabs.newTab().setText("Update Bank Details"),false);

        LinearLayout tabStrip = ((LinearLayout)allTabs.getChildAt(0));
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
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
        }

    }
public void showToast(String message)
{
     Toast.makeText(UpdateMainActivity.this,message,Toast.LENGTH_SHORT).show();
}

    /**----------Adding OnTabSelected Listener for the tabs------------**/

    private void bindWidgetsWithAnEvent()
    {
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

    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0 :
//                customerDetails = new UpdateCustomer();
                replaceFragment(customerDetails);
                break;
            case 1 :
//                billingAddresssActivity = new UpdateBillingAddress();
                replaceFragment(billingAddresssActivity);
                break;
            case 2 :
//                taxRegistrationActivity = new UpdateTaxRegistration();
                replaceFragment(taxRegistrationActivity);
                break;
            case 3 :
//                bankDetailsActvity = new UpdateBankDetails();
                replaceFragment(bankDetailsActvity);
                break;

        }
    }

    /**--------------------Adding Frame Container To The Activity-----------------**/

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }


    /**-----------This Method Will Be Executed When User Press Back button--------**/
    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (currentFragment instanceof  UpdateCustomer)
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMainActivity.this);
            builder.setMessage(R.string.text_go_back);
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(UpdateMainActivity.this, DashboardActivity.class).putExtra("isNewUser", false);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    UpdateMainActivity.this.finish();
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
        }else if (currentFragment instanceof  UpdateBillingAddress)
        {
            ((UpdateBillingAddress) currentFragment).onBackClicked();
        }else if (currentFragment instanceof  UpdateTaxRegistration)
        {
            ((UpdateTaxRegistration) currentFragment).onBackClicked();
        }else if (currentFragment instanceof  UpdateBankDetails)
        {
            ((UpdateBankDetails) currentFragment).onBackClicked();
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
            setCurrentTabFragment(pos);
            allTabs.getTabAt(pos).select();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**---------Overriding the method of CallToBillFragment interface------**/

    @Override
    public void callingBillingFragment(int pos) {
        try {
                setCurrentTabFragment(pos);
                allTabs.getTabAt(pos).select();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**---------Overriding the method of CallToBankFragment interface------**/

    @Override
    public void callingBankingFragment(int pos) {
        try {
            setCurrentTabFragment(pos);
            allTabs.getTabAt(pos).select();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**---------Overriding the method of CallToTaxFragment interface------**/

    @Override
    public void callingTaxFragment(int pos) {
        try {
            setCurrentTabFragment(pos);
            allTabs.getTabAt(pos).select();
        }catch (Exception e){
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
            //message = "Good! Connected to Internet";
            message = "Sorry! Not connected to internet";
            //Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, ""+message, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            //textView.setTextColor(Color.GREEN);
            textView.setTextColor(Color.RED);
            snackbar.show();

        } else {

        }
    }

    /** Starting The Timer when App Is Resumed**/

    @Override
    protected void onResume() {
        super.onResume();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);
        MyInternetCheck.getInstance().setConnectivityListener(UpdateMainActivity.this);
    }
    /** Starting The Timer when App Is Paused**/

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(connectivityReceiver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}