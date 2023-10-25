package com.mwbtech.dealer_register.Dashboard;

import static com.mwbtech.dealer_register.Constants.Business_Category;
import static com.mwbtech.dealer_register.Constants.Enternaiment_Category;
import static com.mwbtech.dealer_register.Constants.General_Category;
import static com.mwbtech.dealer_register.Constants.HealthScience_Category;
import static com.mwbtech.dealer_register.Constants.International;
import static com.mwbtech.dealer_register.Constants.Sports_Category;
import static com.mwbtech.dealer_register.Constants.Technology_Category;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;
import static com.mwbtech.dealer_register.Utils.Utility.getScreenHeight;
import static com.mwbtech.dealer_register.Utils.Utility.getScreenWidth;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.Constants;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.AutoScrollTextAdapter;
import com.mwbtech.dealer_register.Adapter.NewsHeadlineAdapter;
import com.mwbtech.dealer_register.BuildConfig;
import com.mwbtech.dealer_register.Dashboard.Advertisement.AdRequestDetails.AdRequestDetailsActivity;
import com.mwbtech.dealer_register.Dashboard.Advertisement.AdvertisementMenuActivity;
import com.mwbtech.dealer_register.Dashboard.BannerCarouselView.ViewPagerCarouselView;
import com.mwbtech.dealer_register.Dashboard.Enquiry.EnquiryRequirementActivity;
import com.mwbtech.dealer_register.Dashboard.EnquiryReceived.EnquiryReceivedActivity;
import com.mwbtech.dealer_register.Dashboard.EnquirySent.EnquirySentListActivity;
import com.mwbtech.dealer_register.Dashboard.FavouriteChat.FavChatInboxActivity;
import com.mwbtech.dealer_register.Dashboard.NotificationsTabs.MainNotificationsActivity;
import com.mwbtech.dealer_register.Dashboard.OnetoOne.OnetoOneActivity;
import com.mwbtech.dealer_register.Dashboard.Support.AboutUs;
import com.mwbtech.dealer_register.Dashboard.Support.BusinessNewsListActivity;
import com.mwbtech.dealer_register.Dashboard.Support.FeedbackActivity;
import com.mwbtech.dealer_register.Dashboard.Support.HelpVideoListActivity;
import com.mwbtech.dealer_register.Dashboard.Support.NewsArticle;
import com.mwbtech.dealer_register.Dashboard.Support.TermsConditionActivity;
import com.mwbtech.dealer_register.Dashboard.Wallet.Wallet_Main_Screen;
import com.mwbtech.dealer_register.LoginRegister.LoginActivity;
import com.mwbtech.dealer_register.LoginRegister.TakeTourActivity;
import com.mwbtech.dealer_register.PojoClass.DashBoardData;
import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.PojoClass.PromoImage;
import com.mwbtech.dealer_register.PojoClass.ScrollTextModel;
import com.mwbtech.dealer_register.PojoClass.SlotBookImages;
import com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity;
import com.mwbtech.dealer_register.Profile.ProfileMain.UpdateMainActivity;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.DialogUtilsKt;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.ProjectUtilsKt;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.mwbtech.dealer_register.biometrics.BiometricManager;
import com.mwbtech.dealer_register.biometrics.BiometricStatus;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;
import com.mwbtech.dealer_register.internet_connection.MyInternetCheck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener, NavigationView.OnNavigationItemSelectedListener, AutoScrollTextAdapter.TextSelected {
    PrefManager prefManager;
    public static boolean isUpdate = false;
    int CustID;
    String Token;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private AlertDialog registrationCompletedDialog;
    DrawerLayout drawer;
    NavigationView navigationView;
    RecyclerView recyclerView,newsRecycler;
    BarChart chart;
    Button _btnInternationalNews,_btnBussinessNews,_btnSportsNews,_btnGeneral,_btnTechnology,_btnHealth,_btnEntNews;
    Toolbar toolbar;
    public static ConnectivityReceiver connectivityReceiver;
    CoordinatorLayout coordinatorLayout;
    long carouselSlideInterval = 15000;
    IntentFilter intentFilter;
    //private boolean doubleBack = false;

    List<PromoImage> promoImages = new ArrayList<>();
    List<ScrollTextModel> scrollTextModels = new ArrayList<>();
    TextView tvDealer;
    Dialog mDialog;
    boolean isImageLoaded = false, isNewUser = false;
    boolean isImages = false;
    public static DealerRegister dealerRegister;
    public static DealerRegister creation;
    //public static DealerRegister creation1;
    Customer_Interface customer_interface;
    LinearLayout enquiryButton;
    ConstraintLayout OneToOneButton, inboxButton, favChatButton, OutboxChatButton, AdvertisementButton;
    public static boolean isFilter = false;
    ProgressDialog progressDialog;

    String slatestversion, scurrentversion;

    ViewPagerCarouselView viewPagerCarouselView;

    public static boolean isVideoPlayed = false;
    TextView FavNotify, OutBoxNotify, inboxNotify;

    Handler handler = new Handler();
    Handler handler1 = new Handler();
    Runnable runnable;
    Runnable runnable1;
    int delay = 10000;
    String fullScreenAdUrl, userType;
    AutoScrollTextAdapter autoScrollTextAdapter;
    DealerRegister dealerRegisterforad;
    List<PromoImage> adDetailsModels, adImages;
    List<SlotBookImages> adBannerImages;
    int scrollCount = 0;
    private ProgressBar mProgressBar;

    //notification count
    TextView textCartItemCount;
    public int mCartItemCount;
    CountDownTimer timer;

    private BiometricManager biometricManager;

    //TimerActivity timerActivity=new TimerActivity();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_cutomer);
        isNewUser = getIntent().getExtras().getBoolean("isNewUser", false);
        timer = new CountDownTimer(15 * 60 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                //Some code
                Log.v(Constants.TAG, "Service Started");
            }

            public void onFinish() {
                //Logout
                Log.v(Constants.TAG, "Call Logout by Service");
                // Code for Logout
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        };

        /* new GetLatestVersion().execute();*/
        initializeViews();
        initializeNavigationBar();
        initializeSharedData();
        initializeClickEvents();
        checkUserDetails();
        getBannerTextAd();
        try {
            if (prefManager.isSave_isBannerImages()) {
                //
                Log.e("Already exists...", String.valueOf(prefManager.isSave_isBannerImages()));
                List<SlotBookImages> bannerImages = PrefManager.getImages();
                Log.e("Images...", bannerImages.toString());
                viewPagerCarouselView = findViewById(R.id.carousel_view);
                viewPagerCarouselView.setData(getSupportFragmentManager(), bannerImages, carouselSlideInterval);
                List<SlotBookImages> textAds = prefManager.getTextAD();
                for (int i = 0; i < textAds.size(); i++) {
                    String adText = textAds.get(i).getAdText();
                    Log.e("AdText.......", adText);
                    scrollTextModels.add(new ScrollTextModel(adText));
                    autoScrollTextAdapter = new AutoScrollTextAdapter(DashboardActivity.this, textAds, DashboardActivity.this);
                    recyclerView.setAdapter(autoScrollTextAdapter);
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.this);
                autoScrollAnother();
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(1000);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                recyclerView.setAdapter(autoScrollTextAdapter);
            } else {
                Log.e("banner", String.valueOf(prefManager.isSave_isBannerImages()));
                getBannerTextAd();
            }
        } catch (Exception e) {
            //
        }
        GetDashBoardData();

        biometricManager.checkFingerPrint(status -> {
            if(!prefManager.isBiometricRegistrationAsked() && status.equals(BiometricStatus.BIOMETRIC_AVAILABLE)){
                DialogUtilsKt.confirm(DashboardActivity.this, getString(R.string.allow_login_using_bimetric), getString(R.string.yes), getString(R.string.no), v -> {
                    prefManager.saveBiometricStatus(true);
                    prefManager.setBiometricRegistrationAsked(true);
                    if (isNewUser) showRegisteredUserDialog();
                }, v -> {
                    prefManager.setBiometricRegistrationAsked(true);
                    prefManager.saveBiometricStatus(false);
                    if (isNewUser) showRegisteredUserDialog();
                });
            }else{
                if (isNewUser) showRegisteredUserDialog();
            }
        });


        // this.onUserInteraction();

    }

    private void showRegisteredUserDialog() {
    }

    private void showTakeTourDialog() {
        AlertDialog checkMailDialog;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View layoutView =
                this.getLayoutInflater().inflate(R.layout.dialog_navigation, null);

        Button kycButton = layoutView.findViewById(R.id.take_tour_button);
        AppCompatTextView ctnButton = layoutView.findViewById(R.id.ctn_button);
        dialogBuilder.setView(layoutView);
        dialogBuilder.setCancelable(false);
        checkMailDialog = dialogBuilder.create();

        checkMailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        checkMailDialog.show();
        kycButton.setOnClickListener(v -> {
            checkMailDialog.dismiss();
            startActivity(new Intent(this, TakeTourActivity.class));
            finish();
        });
        ctnButton.setOnClickListener(v -> {
            checkMailDialog.dismiss();
            getBannerTextAd();
//            startActivity(new Intent(this, DashboardActivity.class));
//            finish();
        });
    }

    private void initializeViews() {
        setTitle("Dashboard");
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        biometricManager = new BiometricManager(this);
        coordinatorLayout = findViewById(R.id.coordinator);
        enquiryButton = findViewById(R.id.EnquiryButton);
        inboxButton = findViewById(R.id.InboxButton);
        favChatButton = findViewById(R.id.FavChatButton);
        OutboxChatButton = findViewById(R.id.OutboxButton);
        AdvertisementButton = findViewById(R.id.AdvertiseButton);
        OneToOneButton = findViewById(R.id.onetooneButton);
        _btnBussinessNews=findViewById(R.id.businessbtn);
        _btnInternationalNews=findViewById(R.id.internationalbtn);
        _btnSportsNews=findViewById(R.id.sportsbtn);
        _btnEntNews=findViewById(R.id.enterbtn);
        _btnGeneral=findViewById(R.id.generalbtn);
        _btnHealth=findViewById(R.id.healthbtn);
        _btnTechnology=findViewById(R.id.technologybtn);
        chart = findViewById(R.id.bar_chart);

//        textCartItemCount = findViewById(R.id.cart_badge);

        FavNotify = findViewById(R.id.FavChatNotify);
        inboxNotify = findViewById(R.id.EnqRecvChatNotify);
        OutBoxNotify = findViewById(R.id.EnqSentChatNotify);

        recyclerView = findViewById(R.id.rec_scroll_txt);
        isFilter = false;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_dashboard_toolbar));
        showBarChart();
    }

    private void initializeNavigationBar() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        tvDealer = headerView.findViewById(R.id.dealername);

        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void initializeSharedData() {
        prefManager = new PrefManager(this);
        dealerRegister = new DealerRegister();
        intentFilter = new IntentFilter();
        connectivityReceiver = new ConnectivityReceiver();

        try {
            CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
            Token = prefManager.getToken().get(TOKEN);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dealerRegisterforad = (DealerRegister) getIntent().getSerializableExtra("adUrl");
            //fullScreenAdUrl = bundle.getString("adUrl");
            if(dealerRegisterforad != null && dealerRegisterforad.getFullScreenAdURL() != null && dealerRegisterforad.getFullScreenAdURL() != "") {
                fullScreenAdUrl = dealerRegisterforad.getFullScreenAdURL();
                showAdToUser();
            }
        }

        try {
           // get_news_from_api();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initializeClickEvents() {
        navigationView.setNavigationItemSelectedListener(this);
        enquiryButton.setOnClickListener(this);
        inboxButton.setOnClickListener(this);
        favChatButton.setOnClickListener(this);
        OutboxChatButton.setOnClickListener(this);
        AdvertisementButton.setOnClickListener(this);
        OneToOneButton.setOnClickListener(this);
        _btnBussinessNews.setOnClickListener(this);
        _btnInternationalNews.setOnClickListener(this);
        _btnSportsNews.setOnClickListener(this);
        _btnEntNews.setOnClickListener(this);
        _btnGeneral.setOnClickListener(this);
        _btnHealth.setOnClickListener(this);
        _btnTechnology.setOnClickListener(this);
    }

    private void checkUserDetails() {
        if (isUpdate) {
            getCustomerDetails();
        } else {
            if (prefManager.getSavedObjectFromPreference(DashboardActivity.this,
                    "mwb-welcome", "customer", DealerRegister.class) == null) {
                getCustomerDetails();
            } else {
                getCustomerDetails();
                tvDealer.setText("" + prefManager.getSavedObjectFromPreference(DashboardActivity.this, "mwb-welcome", "customer", DealerRegister.class).getFirmName());
            }
        }
    }

    private void showAdToUser() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setting the height and width to dialog
        int height = getScreenHeight(DashboardActivity.this);
        int width = getScreenWidth(DashboardActivity.this);
        dialog.getWindow().setLayout(width, height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.show_full_screen_ad_dialog);
        dialog.setCancelable(true);
        //set layout height and width to its screen size
        FrameLayout frameLayout = dialog.findViewById(R.id.parent);
        ViewGroup.LayoutParams params = frameLayout.getLayoutParams();
        params.height = height;
        params.width = width;
        frameLayout.setLayoutParams(params);

        ImageView imageView = dialog.findViewById(R.id.fullImage);
        Button buttonSkip = dialog.findViewById(R.id.btnSkip);
        Button buttonEnq = dialog.findViewById(R.id.btnEnquiry);

        handler1.postDelayed(runnable1 = new Runnable() {
            public void run() {
                handler1.postDelayed(runnable1, delay);
                buttonSkip.setVisibility(View.VISIBLE);
            }
        }, 5000);

        buttonEnq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AdRequestDetailsActivity.class).putExtra("adUrl", dealerRegisterforad);
                startActivity(i);
            }
        });

        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Glide.with(this).load(fullScreenAdUrl).into(imageView);
        dialog.show();
    }


    private void GetDashBoardData() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<DashBoardData> call = customer_interface.GetDashBoardData("bearer " + Token, CustID);
        call.enqueue(new Callback<DashBoardData>() {
            @Override
            public void onResponse(Call<DashBoardData> call, Response<DashBoardData> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        DashBoardData dashBoardData = response.body();
                        int fav = dashBoardData.getFavouriteEnquiryMessagesCount();
                        int inbox = dashBoardData.getReceivedEnquiryMessagesCount();
                        int outbox = dashBoardData.getSentEnquiryMessagesCount();
                        mCartItemCount = dashBoardData.getNotificationsCount();
                        //to handle the navigation when user is buyer or seller
                        int isRegisteredValue=dashBoardData.getIsRegistered();
                        Log.e("isRegisteredvalue...",String.valueOf(isRegisteredValue));
                        if(String.valueOf(isRegisteredValue).equals("1"))
                           prefManager.setIsRegistered(true);
                        else
                            prefManager.setIsRegistered(false);
//                        if (mCartItemCount == 0) {
                        //textCartItemCount.setVisibility(View.GONE);
//                        } else {
//                            textCartItemCount.setVisibility(View.VISIBLE);
//                        }
                        setupBadge();
                        if (fav == 0) {
                            FavNotify.setVisibility(View.GONE);
                        } else if (fav > 100) {
                            FavNotify.setVisibility(View.VISIBLE);
                            FavNotify.setText("99+");
                        } else {
                            FavNotify.setVisibility(View.VISIBLE);
                            FavNotify.setText(String.valueOf(fav));
                        }

                        if (inbox == 0) {
                            inboxNotify.setVisibility(View.GONE);
                        } else if (inbox > 100) {
                            inboxNotify.setVisibility(View.VISIBLE);
                            inboxNotify.setText("99+");
                        } else {
                            inboxNotify.setVisibility(View.VISIBLE);
                            inboxNotify.setText(String.valueOf(inbox));
                        }

                        if (outbox == 0) {
                            OutBoxNotify.setVisibility(View.GONE);
                        } else if (outbox > 100) {
                            OutBoxNotify.setVisibility(View.VISIBLE);
                            OutBoxNotify.setText("99+");
                        } else {
                            OutBoxNotify.setVisibility(View.VISIBLE);
                            OutBoxNotify.setText(String.valueOf(outbox));
                        }
                        break;

                    case 500:
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(DashboardActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<DashBoardData> call, Throwable t) {
                Log.e("failure ", "error..." + t.getMessage());
            }
        });
    }


    private void updateUserType() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<Boolean> call = customer_interface.updateUserType("bearer " + Token, "" + CustID, userType);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        if (userType.equals("2")) {
                            registrationCompletedDialog.dismiss();
                            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                            finish();
                        } else if (userType.equals("1")) {
                            registrationCompletedDialog.dismiss();
                            showTakeTourDialog();
                        }
                        break;

                    case 500:
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(DashboardActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("failure ", "error..." + t.getMessage());
            }
        });
    }

    /* private void GetPromoImages() {
         customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
         Call<List<PromoImage>> call = customer_interface.getPromoAndTextAds("bearer " + Token);
         call.enqueue(new Callback<List<PromoImage>>() {
             @Override
             public void onResponse(Call<List<PromoImage>> call, Response<List<PromoImage>> response) {
                 int status_code = response.code();
                 Log.e("promo", "status..." + status_code);
                 switch (status_code) {
                     case 200:
                         List<PromoImage> adDetailsModels = response.body();
                         adImages=response.body();
                         prefManager.setImages(adImages);
                         Log.e("promo", "status..." + response.body());
                         long carouselSlideInterval = 3000; // 3 SECONDS
                         isImageLoaded = true;
                         viewPagerCarouselView = (ViewPagerCarouselView) findViewById(R.id.carousel_view);
                         viewPagerCarouselView.setData(getSupportFragmentManager(), adDetailsModels, carouselSlideInterval);
                         break;
                     case 500:
                         break;

                     case 401:
                         SessionDialog.CallSessionTimeOutDialog(DashboardActivity.this);
                         break;
                 }
             }

             @Override
             public void onFailure(Call<List<PromoImage>> call, Throwable t) {

             }
         });
     }*/
    private void GetBannerImages() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<SlotBookImages>> call = customer_interface.GetBannerText("bearer " + Token, CustID, true);
        call.enqueue(new Callback<List<SlotBookImages>>() {
            @Override
            public void onResponse(Call<List<SlotBookImages>> call, Response<List<SlotBookImages>> response) {
                int status_code = response.code();
                Log.e("promo", "status..." + status_code);
                switch (status_code) {
                    case 200:
                        List<SlotBookImages> adBannerImages = response.body();
                        adBannerImages = response.body();
                        prefManager.setImages(adBannerImages);
                        Log.e("promo", "status..." + response.body());
                        long carouselSlideInterval = 5000; // 3 SECONDS
                        isImageLoaded = true;
                        viewPagerCarouselView = findViewById(R.id.carousel_view);
                        viewPagerCarouselView.setData(getSupportFragmentManager(), adBannerImages, carouselSlideInterval);
                        break;
                    case 500:
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(DashboardActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<SlotBookImages>> call, Throwable t) {
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(DashboardActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(DashboardActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }
    //bannerid
    /*private void GetPromoImages() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<AdDetailsModel>> call = customer_interface.GetBannerImages("bearer " + Token, CustID);
        call.enqueue(new Callback<List<AdDetailsModel>>() {
            @Override
            public void onResponse(Call<List<AdDetailsModel>> call, Response<List<AdDetailsModel>> response) {
                int status_code = response.code();
                Log.e("promo", "status..." + status_code);
                switch (status_code) {
                    case 200:
                        List<AdDetailsModel> adDetailsModels = response.body();
                        Log.e("promo", "status..." + response.body());
                        long carouselSlideInterval = 3000; // 3 SECONDS
                        isImageLoaded = true;
                        viewPagerCarouselView = (ViewPagerCarouselView) findViewById(R.id.carousel_view);
                        viewPagerCarouselView.setData(getSupportFragmentManager(), adDetailsModels, carouselSlideInterval);
                        break;
                    case 500:
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(DashboardActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<AdDetailsModel>> call, Throwable t) {

            }
        });
    }*/

    //Only For First Demo App (with Company Ads)
    /*private void GetPromoImages() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<PromoImage>> call = customer_interface.getPromoAndTextAds("bearer " + Token);
        call.enqueue(new Callback<List<PromoImage>>() {
            @Override
            public void onResponse(Call<List<PromoImage>> call, Response<List<PromoImage>> response) {
                int status_code = response.code();
                Log.e("promo", "status..." + status_code);
                switch (status_code) {
                    case 200:
                        List<PromoImage> promoImageList = response.body();
                        List<PromoImage> promoImagesOnly = new ArrayList<>();
                        List<PromoImage> promoTextList = new ArrayList<>();
                        for (int i = 0; i < promoImageList.size(); i++) {
                            if(promoImageList.get(i).getPromoType().equals("text")){
                                //For Text Ads
                                String adText = promoImageList.get(i).getPromoText();
                                promoTextList.add(promoImageList.get(i));
                                scrollTextModels.add(new ScrollTextModel(adText));
                                Log.e("scrolltext...",scrollTextModels.toString());
                                autoScrollTextAdapter = new AutoScrollTextAdapter(DashboardActivity.this, promoTextList, DashboardActivity.this);
                                recyclerView.setAdapter(autoScrollTextAdapter);
                            }
                            else
                            {
                                //For Image Ads
                                promoImagesOnly.add(promoImageList.get(i));
                                long carouselSlideInterval = 3000; // 3 SECONDS
                                isImageLoaded = true;
                                viewPagerCarouselView = (ViewPagerCarouselView) findViewById(R.id.carousel_view);
                                viewPagerCarouselView.setData(getSupportFragmentManager(), promoImagesOnly, carouselSlideInterval);

                            }
                        }
                        break;

                    case 500:
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(DashboardActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<PromoImage>> call, Throwable t) {

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.this);
        autoScrollAnother();
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(1000);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(autoScrollTextAdapter);
    }*/

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private ArrayList<BarEntry> getDataValues() {
        ArrayList<BarEntry> dataV = new ArrayList<>();

        dataV.add(new BarEntry(0, (float) (Math.random()*(400-100+1)+100)));
        dataV.add(new BarEntry(1, (float) (Math.random()*(400-100+1)+100)));
        dataV.add(new BarEntry(2, (float) (Math.random()*(400-100+1)+100)));
        dataV.add(new BarEntry(3, (float) (Math.random()*(400-100+1)+100)));
        dataV.add(new BarEntry(4, (float) (Math.random()*(400-100+1)+100)));
        dataV.add(new BarEntry(5, (float) (Math.random()*(400-100+1)+100)));
        dataV.add(new BarEntry(6, (float) (Math.random()*(400-100+1)+100)));
//        dataV.add(new BarEntry(350, 0));
//        dataV.add(new BarEntry(220, 1));
//        dataV.add(new BarEntry(350, 2));
//        dataV.add(new BarEntry(280, 3));
//        dataV.add(new BarEntry(380, 4));
//        dataV.add(new BarEntry(320, 5));
//        dataV.add(new BarEntry(260, 6));
        return dataV;
    }

    private List<GradientColor> getGradientColors() {
        int startColor = ContextCompat.getColor(this, R.color.graph_start);
        int endColor = ContextCompat.getColor(this, R.color.graph_end);
        List<GradientColor> gradientFills = new ArrayList<>();
        gradientFills.add(new GradientColor(startColor, endColor));
        gradientFills.add(new GradientColor(startColor, endColor));
        gradientFills.add(new GradientColor(startColor, endColor));
        gradientFills.add(new GradientColor(startColor, endColor));
        gradientFills.add(new GradientColor(startColor, endColor));
        gradientFills.add(new GradientColor(startColor, endColor));
        gradientFills.add(new GradientColor(startColor, endColor));
        return gradientFills;
    }

    private ArrayList<String> getXAxisLabels() {
        final ArrayList<String> xLabel = new ArrayList<>();
        xLabel.add("Sun");
        xLabel.add("Mon");
        xLabel.add("Tue");
        xLabel.add("Wed");
        xLabel.add("Thu");
        xLabel.add("Fri");
        xLabel.add("Sat");
        return xLabel;
    }

    private ArrayList<String> getYAxisLabels() {
        ArrayList<String> yLabel = new ArrayList<>();
        yLabel.add("0");
        yLabel.add("100");
        yLabel.add("200");
        yLabel.add("300");
        yLabel.add("400");
//        yLabel.add("500");
//        yLabel.add("600");

        return yLabel;
    }

    public class MyXAxisValueFormatter extends ValueFormatter {

        @Override
        public String getFormattedValue(float v) {
//            axisBase.setLabelCount(7);
//            axisBase.setTextColor(Color.BLACK);
            return getXAxisLabels().get((int) v);
        }
    }

    public class MyYAxisValueFormatter extends ValueFormatter {

        private final DecimalFormat mFormat;

        public MyYAxisValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value);
        }
    }

    private void showBarChart() {
/*
        ArrayList NoOfEmp = new ArrayList();

        NoOfEmp.add(new BarEntry(10, 350f));
        NoOfEmp.add(new BarEntry(11, 310f));
        NoOfEmp.add(new BarEntry(12, 350f));
        NoOfEmp.add(new BarEntry(13, 280f));
        NoOfEmp.add(new BarEntry(14, 300f));*/
//        NoOfEmp.add(new BarEntry(5f, 350f));
//        NoOfEmp.add(new BarEntry(6f, 330f));
//        String[] xValues = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
//        String[] yValues = {"0", "100", "200", "300", "400"};

//        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
//        chart.getXAxis().setLabelCount(xValues.length, true);
//        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        BarDataSet bardataset = new BarDataSet(getDataValues(), "Enquiry Trend");
        bardataset.setGradientColors(getGradientColors());

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(bardataset);

        BarData data = new BarData(dataSets);
        data.setDrawValues(false);
//        data.setValueTextSize(14f);
        data.setBarWidth(0.5f);
        chart.setData(data);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new MyXAxisValueFormatter());

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setDrawGridLines(false);
        yAxis.setValueFormatter(new MyYAxisValueFormatter());

        // Disable the right y axis
        YAxis rightYAxis = chart.getAxisRight();
        rightYAxis.setEnabled(false);
        rightYAxis.setDrawGridLines(false);

//        chart.setMaxVisibleValueCount(400);

        chart.getAxisLeft().setAxisMinimum(0f);
        chart.getAxisLeft().setAxisMaximum(400f);
        chart.getXAxis().setEnabled(true);
        chart.getXAxis().setDrawLabels(true);
        chart.getXAxis().setDrawAxisLine(true);
//        chart.getXAxis().setGranularity(1f);
//        chart.getXAxis().setGranularityEnabled(true);
//        chart.getXAxis().setXOffset(0f);
        chart.getXAxis().setYOffset(0f);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.setTouchEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleEnabled(false);
        chart.setClickable(false);

        chart.setDrawValueAboveBar(false);
//        chart.getXAxis().setTextSize(15f);
        chart.getLegend().setEnabled(false);
        // Remove the grid line from background
        chart.getXAxis().setDrawGridLines(false);
        chart.setDrawGridBackground(false);
        // Hide the desc value of each bar on top
        Description desc = new Description();
        desc.setText("");
        chart.setDescription(desc);
//Now add the labels to be added on the vertical axis
//        chart.animateXY(500, 500);
//        chart.invalidate();

        chart.animateY(3000);
        chart.invalidate();


//        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

    }

    private void getCustomerDetails() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(DashboardActivity.this, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<DealerRegister> call = customer_interface.getCustomerDetails("bearer " + Token, CustID);
        call.enqueue(new Callback<DealerRegister>() {
            @Override
            public void onResponse(Call<DealerRegister> call, Response<DealerRegister> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        dealerRegister = response.body();
                        creation = response.body();
                        Log.e("Dashboard..", creation.toString());
                        prefManager.saveObjectToSharedPreference("customer", creation);
                        tvDealer.setText("" + creation.getFirmName());
                        if (dealerRegister.isProductRegistered()) {

                        } else {
                            AlertDialog.Builder builder
                                    = new AlertDialog
                                    .Builder(DashboardActivity.this);
                            builder.setMessage("Please select the category you belongs to");
                            builder.setTitle("");
                            builder.setCancelable(false);
                            builder
                                    .setPositiveButton(
                                            "Update",
                                            new DialogInterface
                                                    .OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                    int which) {
                                                    startActivity(new Intent(getApplicationContext(), UpdateMainActivity.class));
                                                    DashboardActivity.this.finish();
                                                }
                                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }
                        break;

                    case 404:

                    case 500:
                        progressDialog.dismiss();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(DashboardActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<DealerRegister> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(DashboardActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(DashboardActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        final MenuItem menuItem = menu.findItem(R.id.notification);

        View actionView = menuItem.getActionView();
        textCartItemCount = actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    private void setupBadge() {
        if (textCartItemCount != null) {
            Log.e("count...", String.valueOf(mCartItemCount));
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setText("");
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
//                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                    textCartItemCount.setText("" + mCartItemCount);
                    //int newmcart=mCartItemCount;
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            case R.id.out:
                openLogout();
                break;
            case R.id.wallet:
                Intent intent = new Intent(this, Wallet_Main_Screen.class);
                startActivity(intent);
                break;
            case R.id.notification:
                startActivity(new Intent(DashboardActivity.this, MainNotificationsActivity.class));
                textCartItemCount.setVisibility(View.GONE);
                break;

            case R.id.share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "Kalpavriksha Business App\n\nLet's join hands and grow together\n\nUseful for all type of\nBusiness.Professional.Services\n\nTo download:\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    e.toString();
                }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvLogoutOk:
                prefManager.logout();
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class).
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                DashboardActivity.this.finish();
                mDialog.dismiss();
                break;

            case R.id.tvLogoutCancel:
                mDialog.dismiss();
                break;

            case R.id.EnquiryButton:
                startActivity(new Intent(DashboardActivity.this, EnquiryRequirementActivity.class));
                DashboardActivity.this.finish();
                break;
            case R.id.InboxButton:
                startActivity(new Intent(DashboardActivity.this, EnquiryReceivedActivity.class));
                DashboardActivity.this.finish();
                break;

            case R.id.FavChatButton:
                startActivity(new Intent(DashboardActivity.this, FavChatInboxActivity.class));
                DashboardActivity.this.finish();
                break;

            case R.id.OutboxButton:
                startActivity(new Intent(DashboardActivity.this, EnquirySentListActivity.class));
                DashboardActivity.this.finish();
                break;

            case R.id.AdvertiseButton:
                startActivity(new Intent(DashboardActivity.this, AdvertisementMenuActivity.class));
                //startActivity(new Intent(DashboardActivity.this, AdDevelopment.class));
                DashboardActivity.this.finish();
                break;

            case R.id.onetooneButton:
                startActivity(new Intent(DashboardActivity.this, OnetoOneActivity.class));
                DashboardActivity.this.finish();
                break;

            case R.id.internationalbtn:
                startActivity(new Intent(DashboardActivity.this, BusinessNewsListActivity.class)
                        .putExtra("categoryName",International));
                DashboardActivity.this.finish();
                break;
            case R.id.businessbtn:
                startActivity(new Intent(DashboardActivity.this, BusinessNewsListActivity.class)
                        .putExtra("categoryName",Business_Category));
                DashboardActivity.this.finish();
                break;
            case R.id.sportsbtn:
                startActivity(new Intent(DashboardActivity.this, BusinessNewsListActivity.class)
                        .putExtra("categoryName",Sports_Category));
                DashboardActivity.this.finish();
                break;
            case R.id.enterbtn:
                startActivity(new Intent(DashboardActivity.this, BusinessNewsListActivity.class)
                        .putExtra("categoryName",Enternaiment_Category));
                DashboardActivity.this.finish();
                break;
            case R.id.generalbtn:
                startActivity(new Intent(DashboardActivity.this, BusinessNewsListActivity.class)
                        .putExtra("categoryName",General_Category));
                DashboardActivity.this.finish();
                break;
            case R.id.healthbtn:
                startActivity(new Intent(DashboardActivity.this, BusinessNewsListActivity.class)
                        .putExtra("categoryName",HealthScience_Category));
                DashboardActivity.this.finish();
                break;
            case R.id.technologybtn:
                startActivity(new Intent(DashboardActivity.this, BusinessNewsListActivity.class)
                        .putExtra("categoryName",Technology_Category));
                DashboardActivity.this.finish();
                break;

        }
    }


    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                GetDashBoardData();
            }
        }, delay);
        super.onResume();
        timer.start();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);
        MyInternetCheck.getInstance().setConnectivityListener(DashboardActivity.this);
        //textScroll();
    }

    private void textScroll() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<SlotBookImages>> call = customer_interface.GetBannerText("bearer " + Token, CustID, false);
        call.enqueue(new Callback<List<SlotBookImages>>() {
            @Override
            public void onResponse(Call<List<SlotBookImages>> call, Response<List<SlotBookImages>> response) {
                int status_code = response.code();
                Log.e("promo", "status..." + status_code);
                switch (status_code) {
                    case 200:
                        List<SlotBookImages> adDetailsModels = response.body();
                        prefManager.setTextAD(adDetailsModels);
                        for (int i = 0; i < adDetailsModels.size(); i++) {
                            String adText = adDetailsModels.get(i).getAdText();
                            scrollTextModels.add(new ScrollTextModel(adText));
                            autoScrollTextAdapter = new AutoScrollTextAdapter(DashboardActivity.this, adDetailsModels, DashboardActivity.this);
                            recyclerView.setAdapter(autoScrollTextAdapter);
                        }
                        break;
                    case 500:
                        break;
                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(DashboardActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<SlotBookImages>> call, Throwable t) {

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(DashboardActivity.this);
        autoScrollAnother();
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(1000);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(autoScrollTextAdapter);

    }

    public void autoScrollAnother() {
        scrollCount = 0;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollToPosition((scrollCount++));
                if (scrollCount == scrollTextModels.size()) {
                    scrollTextModels.addAll(scrollTextModels);
                    autoScrollTextAdapter.notifyDataSetChanged();
                }
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 2000);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //timer.start();
        Log.v(Constants.TAG, "Application in background");
        unregisterReceiver(connectivityReceiver);
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(ConnectivityReceiver.isConnected());
    }

    private void showSnack(boolean isConnected) {
        String message;
        if (!isConnected) {
            // message = "Good! Connected to Internet";
            message = "Sorry! Not connected to internet";
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "" + message, Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);

            textView.setTextColor(Color.RED);
            snackbar.show();

        } else {

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.inbox:
                startActivity(new Intent(this, EnquiryReceivedActivity.class));
                DashboardActivity.this.finish();
                break;

            case R.id.outbox:
                startActivity(new Intent(this, EnquirySentListActivity.class));
                DashboardActivity.this.finish();
                break;

            case R.id.contact_us:
                startActivity(new Intent(this, FeedbackActivity.class));
                DashboardActivity.this.finish();
                break;

            case R.id.about_us:
                startActivity(new Intent(this, AboutUs.class));
                DashboardActivity.this.finish();
                break;

            case R.id.logout:
                openLogout();
                break;

            case R.id.terms:
                startActivity(new Intent(this, TermsConditionActivity.class));
                DashboardActivity.this.finish();
                break;

            case R.id.refer_friend:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "Kalpavriksha Business App\n\nLet's join hands and grow together\n\nUseful for all type of\nBusiness.Professional.Services\n\nTo download:\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                    drawer.closeDrawer(GravityCompat.START);
                } catch (Exception e) {
                    e.toString();
                }
                break;

            case R.id.help:
                startActivity(new Intent(this, HelpVideoListActivity.class));
                //startActivity(new Intent(this, HelpVideosActivity.class));
                DashboardActivity.this.finish();
//                Toast.makeText(this, "Not available now.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.favinbox:
                startActivity(new Intent(DashboardActivity.this, FavChatInboxActivity.class));
                DashboardActivity.this.finish();
                break;

            case R.id.update_customer:
                startActivity(new Intent(this, UpdateMainActivity.class));
                DashboardActivity.this.finish();
                break;

            case R.id.resetPassword:
                startActivity(new Intent(this, ResetPassword.class));
                DashboardActivity.this.finish();
                break;

            default:
                return true;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else if (doubleBack) {
//            super.onBackPressed();
//            return;
//        }
        //this.doubleBack = true;
        openLogout();
    }

    public void openLogout() {
        //this.doubleBack = true;
        mDialog = DialogUtilsKt.confirm(this,
                getString(R.string.dialog_logout_text),
                getString(R.string.yes),
                getString(R.string.no),this,this);
    }

    /* @Override
     public void onTextSelected(SlotBookImages textModel) {
         Toast.makeText(this, "This is Company ad", Toast.LENGTH_LONG).show();
     }*/
    @Override
    public void onTextSelected(SlotBookImages textModel) {
        String customer = textModel.getAdText();
        Log.e("Advertisement type....", customer);
        if (textModel.getCompanyAd() == true) {
            Toast.makeText(this, "This is Company ad", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, AdRequestDetailsActivity.class)
                    //.putExtra("promoImage",textModel.getAdImageURL())
                    .putExtra("textad", textModel.getAdText())
                    .putExtra("textModel", textModel);
            startActivity(intent);
        }
    }

    public class GetLatestVersion extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            try {
                slatestversion = Jsoup.connect("https://play.google.com/store/apps/details?id="
                                + getPackageName())
                        .timeout(30000)
                        .get()
                        .select("div.hAyfc:nth-child(4)>" + "span:nth-child(2) >div:nth-child(1)" + "> span:nth-child(1)")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return slatestversion;
        }

        @Override
        protected void onPostExecute(String s) {
            scurrentversion = BuildConfig.VERSION_NAME;
            if (slatestversion != null) {
                float cVersion = Float.parseFloat(scurrentversion);
                float lversion = Float.parseFloat(slatestversion);
                Log.e("latestversion....", String.valueOf(lversion));
                Log.e("currentversion....", String.valueOf(cVersion));
                if (cVersion < lversion) {
                    updateAlertDialog();

                } else {

                }

            }
        }

        private void updateAlertDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
            builder.setTitle(getResources().getString(R.string.app_name));
            builder.setTitle("Minor Uptledates");
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    public void getBannerTextAd() {
        try {
            Date date = new Date();
            SimpleDateFormat dateFormat4 = new SimpleDateFormat("hh:mm");
            dateFormat4.format(date);
            System.out.println(dateFormat4.format(date));

            if (dateFormat4.parse(dateFormat4.format(date)).after(dateFormat4.parse("09:00")) && dateFormat4.parse(dateFormat4.format(date)).before(dateFormat4.parse("19:00"))) {
                Log.e("primetime....", date.toString());
                System.out.println(true);
                //callapi
                textScroll();
                GetBannerImages();
                prefManager.setSave_isBannerImages(true);
            } else if (dateFormat4.parse(dateFormat4.format(date)).after(dateFormat4.parse("19:00")) && dateFormat4.parse(dateFormat4.format(date)).before(dateFormat4.parse("00:00"))) {
                Log.e("offtime....", date.toString());
                //checkes whether the current time is between 09:00 am and 06:59 pm.
                System.out.println(true);
                //callapi
                textScroll();
                GetBannerImages();
                prefManager.setSave_isBannerImages(true);
            } else if (dateFormat4.parse(dateFormat4.format(date)).after(dateFormat4.parse("00:00")) && dateFormat4.parse(dateFormat4.format(date)).before(dateFormat4.parse("09:00"))) {
                Log.e("nighttime....", date.toString());
                //checkes whether the current time is between 09:00 am and 06:59 pm.
                System.out.println(true);
                //callapi
                textScroll();
                GetBannerImages();
                prefManager.setSave_isBannerImages(true);
            } else {
                System.out.println("No if....");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void get_news_from_api() throws IOException, JSONException {
      /*  if(!prefManager.getArticleResponse().isEmpty()){
            JSONObject json = new JSONObject(prefManager.getArticleResponse());
            JSONArray articles = json.getJSONArray("item");

            for (int j = 0; j < articles.length(); j++) {
                JSONObject article = articles.getJSONObject(j);
                NewsArticle currentArticle = new NewsArticle();
                String title = article.getString("title");
                btnBusinewsNews.setText(title);
                currentArticle.setTitle(title);
                break;
            }
            return;
        }*/
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://yahoo-finance15.p.rapidapi.com/api/yahoo/ne/news/AAPL")
                .get()
                .addHeader("X-RapidAPI-Key", getString(R.string.yahoo_finance_api_key))
                .addHeader("X-RapidAPI-Host", "yahoo-finance15.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {

                    final String myResponse = response.body().string();

                    DashboardActivity.this.runOnUiThread(() -> {

                        // handling the response
                        try {
                            JSONObject json = new JSONObject(myResponse);
//                        NewsResponse newsResponse = json.getJSONObject("item").getJSONObject("item");
                            // storing the response in a JSONArray
                            JSONArray articles = json.getJSONArray("item");
                            ArrayList<NewsArticle> articleList = new ArrayList<>();
                            // looping through all the articles
                            // to access them individually
                            for (int j = 0; j < articles.length(); j++) {
                                // accessing each article object in the JSONArray
                                JSONObject article = articles.getJSONObject(j);

                                // initializing an empty ArticleModel
                                NewsArticle currentArticle = new Gson().fromJson(String.valueOf(article),NewsArticle.class);
                                articleList.add(currentArticle);
                            }
                            newsRecycler.setAdapter(new NewsHeadlineAdapter(DashboardActivity.this,articleList,(newsArticle ->{
                                ProjectUtilsKt.openCustomTab(DashboardActivity.this,newsArticle.getLink());
                                return null;
                            })));
                            newsScroller(articleList.size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // logging the JSONException LogCat
                            Log.d("DashboardAct", "Error : " + e.getMessage());
                        }

                    });

                }
            }
        });
    }

    private void newsScroller(int articleSize) {
        AtomicInteger scrollCount = new AtomicInteger(0);
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                newsRecycler.smoothScrollToPosition(scrollCount.get());
                scrollCount.set(scrollCount.get()+1);
                if (scrollCount.get() == articleSize) {
                    scrollCount.set(0);
                }
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable, 2000);
    }
}



