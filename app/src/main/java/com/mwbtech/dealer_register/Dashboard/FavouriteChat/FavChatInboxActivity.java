package com.mwbtech.dealer_register.Dashboard.FavouriteChat;

import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.FavChatInboxAdapter;
import com.mwbtech.dealer_register.Adapter.FdChild;
import com.mwbtech.dealer_register.Adapter.Fdlist;
import com.mwbtech.dealer_register.Adapter.Fdlist1;
import com.mwbtech.dealer_register.Adapter.FilterBusinessDemandAdapter;
import com.mwbtech.dealer_register.Adapter.FilterCityAdapter;
import com.mwbtech.dealer_register.Dashboard.ChatUtil.RecyclerClickLongClickListener;
import com.mwbtech.dealer_register.Dashboard.ChatUtil.RecyclerViewTouchListener;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.PojoClass.BusinessDemand;
import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.PojoClass.City;
import com.mwbtech.dealer_register.PojoClass.DeleteInbox;
import com.mwbtech.dealer_register.PojoClass.InboxDealer;
import com.mwbtech.dealer_register.PojoClass.ReadUnreadModel;
import com.mwbtech.dealer_register.PojoClass.SearchProductDealer;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;
import com.mwbtech.dealer_register.Utils.CustomToast;
import com.mwbtech.dealer_register.Utils.Progressbar.ShowProgressDialog;
import com.mwbtech.dealer_register.Utils.SessionDialog;
import com.mwbtech.dealer_register.internet_connection.ConnectivityReceiver;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavChatInboxActivity extends AppCompatActivity implements FavChatInboxAdapter.ItemCreationAdapterListener, TextWatcher, /*RecyclerItemTouchHelper.RecyclerItemTouchHelperListener,*/ AdapterView.OnItemClickListener, FavChatInboxAdapter.starClickEvent, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    //filter
    private static final String DATE_PATTERN = "^(?:(?:31(|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
    private int mYear, mMonth, mDay;
    RelativeLayout DateFilterLayout, EnquiryLayout, TransactionTypeLinear, SearchCityLayout;
    EditText SelectFromDate, SelectToDate, searchCityName;
    CheckBox SentEnquiryChk, ReceivedEnquiryChk, BuyCheck, SellCheck;
    ListView lv, lv2,lv3,lv4;
    int isSentEnquiry, isReceivedEnquiry;
    String transactionType;
    Button ApplyFilter;
    String[] mainFlr = {"Enquiry Type", "Business Type", "Business Demand", "City", "By Date", "Transaction Type"};

    //activity views
    RecyclerView recyclerView;
    LinearLayoutCompat constraintLayout;
    FavChatInboxAdapter inboxAdapter;
    EditText edSearch;
    AppCompatImageView Clear_txt_btn, btnClearCity;
    AppCompatImageView imgFilter, imgReload/*, imgDelete*/;
    ArrayList<InboxDealer> inboxDealers = new ArrayList<>();
    List<InboxDealer> listStar;
    TextView Txt_NotFound, tv_Search_Count;
    PrefManager prefManager;
    int custId, Total_List_Count, Filter_Count;
    String Token;
    String ConvertedFromDate, ConvertedToDate, SelectedFromDate, SelectedToDate;
    Customer_Interface customer_interface;
    ImageView imgBack;

    Dialog mDialogCity;
    PhotoView ivimage;

    private ActionMode mActionMode;
    List<InboxDealer> dealerList = new ArrayList<>();
    List<DeleteInbox> deleteInboxes = new ArrayList<>();
    List<Fdlist> fDetails = new ArrayList<>();
    List<Fdlist1> fDetails1 = new ArrayList<>();
    List<City> cityList = new ArrayList<>();
    List<BusinessType> businessTypeList = new ArrayList<>();
    List<BusinessType> businessTypes = new ArrayList<>();
    List<BusinessDemand> businessDemandList = new ArrayList<>();
    List<BusinessDemand> businessDemand = new ArrayList<>();
    List<ReadUnreadModel> readUnreadModelList = new ArrayList<>();
    List<City> getCityList = new ArrayList<>();

    FdChild businessTypeAdapter = null;
    FilterBusinessDemandAdapter businessDemandAdapter = null;
    FilterCityAdapter filterCityAdapter = null;
    MenuItem DeleteBtn, refreshButton, filterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Favourite Chat");

        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }
      /*  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
*/

    }

    private void initializeSharedData() {
        prefManager = new PrefManager(this);
        custId = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        Token = prefManager.getToken().get(TOKEN);
    }

    private void showContent() {
        setContentView(R.layout.activity_fav_chat_inbox);

        initializeViews();
        initializeSharedData();
        initializeRecyclerView();
        initializeClickEvent();
        initializeRecyclerViewTouchListener();
        getListInboxMethodFromServer();

    }

    private void showNoSignalContent() {
        setContentView(R.layout.no_signal_layout);

        Button tryButton = findViewById(R.id.tryBtn);
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
       */
        tryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInternetConnected();
            }
        });
    }

    private void checkInternetConnected() {
        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }
    }

    private void initializeViews() {
        constraintLayout = findViewById(R.id.parentLayout);
        edSearch = findViewById(R.id.search);
        Clear_txt_btn = findViewById(R.id.clear_txt_Prise);
        Txt_NotFound = findViewById(R.id.not_found_txt);
        tv_Search_Count = findViewById(R.id.search_count);
        imgBack = findViewById(R.id.back);
        imgFilter = findViewById(R.id.menu);
        imgReload = findViewById(R.id.reload);
        listStar = new ArrayList<>();

       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
*/
    }

    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(inboxAdapter);
    }

    private void initializeClickEvent() {
        edSearch.addTextChangedListener(this);
        imgBack.setOnClickListener((v) -> onBackPressed());
        imgFilter.setOnClickListener((v) -> {
            if (ConnectivityReceiver.isConnected()) {
                if (businessTypeList.size() == 0) {
                    getBusinessListFromWebServer();
                }
                if (businessDemandList.size() == 0) {
                    GetBusinessDemand();
                }

                if (cityList.size() == 0) {
                    getCitiesList();
                }
                openPopUpdialog();
            } else {
                Toast.makeText(this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
            }
        });
        imgReload.setOnClickListener((v) -> {
            if (ConnectivityReceiver.isConnected()) {
                isReceivedEnquiry = 0;
                isSentEnquiry = 0;
                businessTypeList.clear();
                cityList.clear();
                businessDemandList.clear();
                SelectedFromDate = "";
                SelectedToDate = "";
                getListInboxMethodFromServer();
                tv_Search_Count.setVisibility(View.GONE);
                transactionType = "";
            } else {
                Toast.makeText(this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
            }
        });
        /*imgDelete.setOnClickListener((v) -> {
            int count = 0;
            try {
                if (ConnectivityReceiver.isConnected()) {
                    if (inboxAdapter.getUpdatedList().size() == 0) {
                        //do nothing
                    } else onListItemSelect(count);
                } else {
                    Toast.makeText(this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/
        Clear_txt_btn.setOnClickListener(view -> {
            if (!edSearch.getText().toString().isEmpty()) {
                edSearch.getText().clear();
                Clear_txt_btn.setVisibility(View.GONE);
                //hide keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }

        });
    }

    private void initializeRecyclerViewTouchListener() {
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getBaseContext(), recyclerView, new RecyclerClickLongClickListener() {
            @Override
            public void onClick(View view, int position) {
                //If ActionMode not null select item
                if (mActionMode != null)
                    onListItemSelect(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
                onListItemSelect(position);
            }
        }));

    }

    private void onListItemSelect(int position) {

        inboxAdapter.toggleSelection(position);//Toggle the selection
        boolean hasCheckedItems = inboxAdapter.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode

            mActionMode = startSupportActionMode(new FavChatToolbarActionMode(this, inboxAdapter, this, inboxDealers));
        else if (!hasCheckedItems && mActionMode != null) {
            // there no selected items, finish the actionMode
            Toast.makeText(this, "Please select a product to delete.!", Toast.LENGTH_SHORT).show();
            mActionMode.finish();
        }
        if (mActionMode != null)
            //set action mode title on item selection
            mActionMode.setTitle(inboxAdapter
                    .getSelectedCount() + " selected");
    }


    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    public void selectAll() {
        inboxAdapter.selectAll();
        int count = inboxAdapter.getSelectedItemCount();
        if (count == 0) {
            mActionMode.finish();
        } else {
            mActionMode.setTitle(count + " selected");
            mActionMode.invalidate();
        }
        //mActionMode = null;
    }

    //Delete selected rows
    public void deleteRows() {
        dealerList.clear();
        deleteInboxes.clear();
        SparseBooleanArray selected = inboxAdapter.getSelectedIds();//Get selected ids

        //Loop all selected ids
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {

                //If current id is selected remove the item via key
                dealerList.add(inboxDealers.get(selected.keyAt(i)));
                inboxDealers.remove(selected.keyAt(i));
                inboxAdapter.notifyDataSetChanged();//notify CitySelectedAdapter
            }
        }

        for (InboxDealer inboxDealer : dealerList) {
            deleteInboxes.add(new DeleteInbox(custId, inboxDealer.getQueryId(), inboxDealer.getCustID()));
        }
        if (ConnectivityReceiver.isConnected()) {
            deleteConversionFromInbox(deleteInboxes);
            mActionMode.finish();//Finish action mode after use
        } else {
            showError();
        }
    }

    //set mark as read
    public void markAsReadRows() {
        dealerList.clear();
        readUnreadModelList.clear();
        SparseBooleanArray selected = inboxAdapter.getSelectedIds();//Get selected ids

        //Loop all selected ids
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {

                //If current id is selected set the item via key
                dealerList.add(inboxDealers.get(selected.keyAt(i)));
                //inboxDealers.remove(selected.keyAt(i));
                inboxAdapter.notifyDataSetChanged();//notify CitySelectedAdapter
            }
        }

        //isRead=1
        for (InboxDealer inboxDealer : dealerList) {
            readUnreadModelList.add(new ReadUnreadModel(inboxDealer.getQueryId(), custId, inboxDealer.getCustID(), 1));
        }

        if (ConnectivityReceiver.isConnected()) {
            MarkChatAsReadUnRead(readUnreadModelList);
            mActionMode.finish();//Finish action mode after use
        } else {
            showError();
        }

    }


    //set mark as un read
    public void goAlertasunRead() {
        dealerList.clear();
        readUnreadModelList.clear();
        SparseBooleanArray selected = inboxAdapter.getSelectedIds();//Get selected ids

        //Loop all selected ids
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {

                //If current id is selected set the item via key
                dealerList.add(inboxDealers.get(selected.keyAt(i)));
                //inboxDealers.remove(selected.keyAt(i));
                inboxAdapter.notifyDataSetChanged();//notify CitySelectedAdapter
            }
        }

        for (InboxDealer inboxDealer : dealerList) {
            readUnreadModelList.add(new ReadUnreadModel(inboxDealer.getQueryId(), custId, inboxDealer.getCustID(), 0));
        }

        if (ConnectivityReceiver.isConnected()) {
            MarkChatAsReadUnRead(readUnreadModelList);
            mActionMode.finish();//Finish action mode after use
        } else {
            showError();
        }
    }

    private void MarkChatAsReadUnRead(List<ReadUnreadModel> readUnreadModelList) {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<ReadUnreadModel>> call = customer_interface.markChatAsReadUnread("bearer " + Token, readUnreadModelList);
        call.enqueue(new Callback<List<ReadUnreadModel>>() {
            @Override
            public void onResponse(Call<List<ReadUnreadModel>> call, Response<List<ReadUnreadModel>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        inboxAdapter.notifyDataSetChanged();
                        getListInboxMethodFromServer();
                        break;

                    case 404:
                        break;

                    case 500:
                        CustomToast.showToast(FavChatInboxActivity.this, "Server Error");
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(FavChatInboxActivity.this);
                        break;

                }

            }

            @Override
            public void onFailure(Call<List<ReadUnreadModel>> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(FavChatInboxActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(FavChatInboxActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void deleteConversionFromInbox(List<DeleteInbox> dealerList) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(FavChatInboxActivity.this, "Please wait");
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Deleting Chat"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<DeleteInbox>> call = customer_interface.deleteConversion("bearer " + Token, custId, dealerList);
        call.enqueue(new Callback<List<DeleteInbox>>() {
            @Override
            public void onResponse(Call<List<DeleteInbox>> call, Response<List<DeleteInbox>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        inboxAdapter.notifyDataSetChanged();
                        getListInboxMethodFromServer();
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(FavChatInboxActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(FavChatInboxActivity.this);
                        break;
                }

            }

            @Override
            public void onFailure(Call<List<DeleteInbox>> call, Throwable t) {
                progressDialog.dismiss();
                CustomToast.showToast(FavChatInboxActivity.this, "Time out");
            }
        });

    }


    private void setNullToActionMode1() {
        if (mActionMode != null)
            mActionMode.finish();
    }


    private void getListInboxMethodFromServer() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(FavChatInboxActivity.this, "Please wait");
        progressDialog.setMessage("Loading...."); // Setting Message
        //progressDialog.setTitle("Loading details"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        businessTypes.clear();
        getCityList.clear();
        businessDemandList.clear();
        SearchProductDealer productDealer = new SearchProductDealer(getCityList, businessTypes, businessDemandList, custId, "", "", 0, 0);
        Log.e("Favlist", productDealer.toString());
        Gson gson = new Gson();
        String json = gson.toJson(productDealer);
        Log.e("Favlist....", json);
        Call<List<InboxDealer>> call = customer_interface.GetFavChatFilterList("bearer " + Token, productDealer);
        call.enqueue(new Callback<List<InboxDealer>>() {
            @Override
            public void onResponse(Call<List<InboxDealer>> call, Response<List<InboxDealer>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        inboxDealers = new ArrayList<>(response.body());
                        if (inboxDealers.size() != 0) {
                            Total_List_Count = inboxDealers.size();
                            recyclerView.setVisibility(View.VISIBLE);
                            inboxAdapter = new FavChatInboxAdapter(FavChatInboxActivity.this, inboxDealers, FavChatInboxActivity.this, FavChatInboxActivity.this);
                            recyclerView.setAdapter(inboxAdapter);
                            inboxAdapter.notifyDataSetChanged();
                            Txt_NotFound.setVisibility(View.GONE);
                        } else {
                            progressDialog.dismiss();
                            recyclerView.setVisibility(View.GONE);
                            Txt_NotFound.setVisibility(View.VISIBLE);
                            tv_Search_Count.setVisibility(View.GONE);
                        }
                        break;
                    case 404:
                        progressDialog.dismiss();
                        Txt_NotFound.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        tv_Search_Count.setVisibility(View.GONE);
                        break;
                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(FavChatInboxActivity.this, "Server Error");
                        break;
                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(FavChatInboxActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<InboxDealer>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(FavChatInboxActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(FavChatInboxActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }
    private void GetBusinessDemand() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(FavChatInboxActivity.this, "Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<BusinessDemand>> call = customer_interface.GetBusinessDemand("bearer " + Token);
        call.enqueue(new Callback<List<BusinessDemand>>() {
            @Override
            public void onResponse(Call<List<BusinessDemand>> call, Response<List<BusinessDemand>> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        progressDialog.dismiss();
                        businessDemandList = response.body();
                        //set business demand
                        businessDemandAdapter = new FilterBusinessDemandAdapter(FavChatInboxActivity.this, businessDemandList);
                        break;
                    case 404:
                        progressDialog.dismiss();
                        break;
                    case 500:
                        CustomToast.showToast(FavChatInboxActivity.this, "Server Error");
                        progressDialog.dismiss();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(FavChatInboxActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<BusinessDemand>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(FavChatInboxActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(FavChatInboxActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void openPopUpdialog() {
        fDetails.clear();
        fDetails1.clear();
        businessTypes.clear();
        businessDemand.clear();
        getCityList.clear();
        ConvertedFromDate = "";
        ConvertedToDate = "";
        Dialog mDialog = new Dialog(FavChatInboxActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.fav_inbox_filter);
        mDialog.setCancelable(true);
        lv = (ListView) mDialog.findViewById(R.id.listView1);
        DateFilterLayout = mDialog.findViewById(R.id.DateLayout);
        EnquiryLayout = mDialog.findViewById(R.id.EnqTypeLayout);
        TransactionTypeLinear = mDialog.findViewById(R.id.linearTranscation);

        SelectFromDate = mDialog.findViewById(R.id.FromdateSelect);
        SearchCityLayout = mDialog.findViewById(R.id.CityLayout);
        searchCityName = mDialog.findViewById(R.id.SearchCityName);
        btnClearCity = mDialog.findViewById(R.id.clear_txt_Prise);
        SelectToDate = mDialog.findViewById(R.id.TodateSelect);
        SentEnquiryChk = mDialog.findViewById(R.id.sent_Enq);
        ReceivedEnquiryChk = mDialog.findViewById(R.id.Rcv_Enq);
        BuyCheck = mDialog.findViewById(R.id.ckBuy);
        SellCheck = mDialog.findViewById(R.id.ckSell);

        SentEnquiryChk.setOnCheckedChangeListener(this);
        ReceivedEnquiryChk.setOnCheckedChangeListener(this);
        BuyCheck.setOnCheckedChangeListener(this);
        SellCheck.setOnCheckedChangeListener(this);
        SelectFromDate.setOnClickListener(this);
        SelectToDate.setOnClickListener(this);
        imgBack.setOnClickListener((v) -> {
            onBackPressed();
            finish();
        });
        //SelectToDate.setEnabled(false);
        lv.setOnItemClickListener(this);
        lv2 = (ListView) mDialog.findViewById(R.id.listView2);
        lv4 = (ListView) mDialog.findViewById(R.id.listView4);
        lv3 = (ListView) mDialog.findViewById(R.id.listView3);
        ApplyFilter = mDialog.findViewById(R.id.Apply);
        for (int i = 0; i < 6; i++) {
            fDetails.add(new Fdlist(mainFlr[i]));
        }


        ArrayAdapter<Fdlist> adapter = new MyAdapter();
        lv.setAdapter(adapter);

        //EnquiryLayout.setVisibility(View.VISIBLE);

        if (businessDemandList.size() != 0) {
            businessDemandAdapter = new FilterBusinessDemandAdapter(FavChatInboxActivity.this, businessDemandList);
            lv4.setAdapter(businessDemandAdapter);
            businessDemandAdapter.notifyDataSetChanged();
        }

        if (cityList.size() != 0) {
            filterCityAdapter = new FilterCityAdapter(FavChatInboxActivity.this, cityList);
            lv2.setAdapter(filterCityAdapter);
            filterCityAdapter.notifyDataSetChanged();
        }
        if (!SelectFromDate.getText().toString().isEmpty()) {
            SelectFromDate.setText(SelectedFromDate);
            SelectToDate.setText(SelectedToDate);
        }
        if (businessTypeList.size() != 0) {
            businessTypeAdapter = new FdChild(FavChatInboxActivity.this, businessTypeList);
            lv3.setAdapter(businessTypeAdapter);
            businessTypeAdapter.notifyDataSetChanged();
        }
        if (isSentEnquiry == 1) {
            SentEnquiryChk.setChecked(true);
            ReceivedEnquiryChk.setChecked(false);
        }

        if (isReceivedEnquiry == 1) {
            SentEnquiryChk.setChecked(false);
            ReceivedEnquiryChk.setChecked(true);
        }
        if (transactionType == "Buy") {
            BuyCheck.setChecked(true);
            SellCheck.setChecked(false);
        }
        if (transactionType == "Sell") {
            BuyCheck.setChecked(false);
            SellCheck.setChecked(true);
        }
        mDialog.show();
        searchCityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (s.length() < 0) {
                        btnClearCity.setVisibility(View.GONE);
                    } else {
                        filterCityAdapter.getFilter().filter(s.toString());
                        btnClearCity.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnClearCity.setOnClickListener(v -> searchCityName.setText(""));
        ApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (ConnectivityReceiver.isConnected()) {
                        if (businessTypeAdapter.getTrueResultList().isEmpty() | businessTypeAdapter.getTrueResultList() == null &&
                                filterCityAdapter.GetSelectedCity().isEmpty() | filterCityAdapter.GetSelectedCity() == null
                                && businessDemandAdapter.GetSelectedDemand().isEmpty() | businessDemandAdapter.GetSelectedDemand() == null &&
                                TextUtils.isEmpty(SelectFromDate.getText().toString()) | TextUtils.isEmpty(SelectToDate.getText().toString()) &&
                                isSentEnquiry == 0 && isReceivedEnquiry == 0 && transactionType == "") {
                            Toast.makeText(FavChatInboxActivity.this, "Please select appropriate data", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!TextUtils.isEmpty(SelectFromDate.getText().toString())) {
                                if (!validateFromDate()) {
                                    return;
                                } else {
                                    if (!validateToDate()) {
                                        return;
                                    } else {
                                        SelectedFromDate = SelectFromDate.getText().toString();
                                        SelectedToDate = SelectToDate.getText().toString();
                                        ConvertdateFormat(SelectedFromDate);
                                        ConvertTodateFormat(SelectedToDate);
                                        businessTypes = businessTypeAdapter.getTrueResultList();
                                        getCityList = filterCityAdapter.GetSelectedCity();
                                        businessDemand = businessDemandAdapter.GetSelectedDemand();
                                        //to get the checked business type
                                        businessTypeList = businessTypeAdapter.getResultList();
                                        //to get the checked city list
                                        cityList = filterCityAdapter.GetResultCity();
                                        //to get the checked business demand list
                                        businessDemandList = businessDemandAdapter.GetResultDemand();

                                        ApplyFilterMethod(businessTypes, businessDemand, getCityList, ConvertedFromDate, ConvertedToDate, isSentEnquiry, isReceivedEnquiry, transactionType);
                                        mDialog.dismiss();
                                    }
                                }
                            } else {
                                businessTypes = businessTypeAdapter.getTrueResultList();
                                getCityList = filterCityAdapter.GetSelectedCity();
                                businessDemand = businessDemandAdapter.GetSelectedDemand();

                                //to get the checked business type
                                businessTypeList = businessTypeAdapter.getResultList();
                                //to get the checked city list
                                cityList = filterCityAdapter.GetResultCity();
                                //to get the checked business demand list
                                businessDemandList = businessDemandAdapter.GetResultDemand();
                                ApplyFilterMethod(businessTypes, businessDemand, getCityList, ConvertedFromDate, ConvertedToDate, isSentEnquiry, isReceivedEnquiry, transactionType);
                                mDialog.dismiss();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "You are not connected to Internet", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private String ConvertdateFormat(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date mydate = null;
        try {

            mydate = dateFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        ConvertedFromDate = dateFormat1.format(mydate);
        return ConvertedFromDate;

    }

    private String ConvertTodateFormat(String todate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date mydate = null;
        try {

            mydate = dateFormat.parse(todate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        ConvertedToDate = dateFormat1.format(mydate);
        return ConvertedToDate;
    }

    private boolean validateFromDate() {
        String Fromdate = SelectFromDate.getText().toString();

        if (Fromdate.isEmpty()) {
            SelectFromDate.setError("Select From Date");
            Toast.makeText(this, "Please select from date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Fromdate.matches(DATE_PATTERN)) {
            Toast.makeText(this, "Please select valid date format (DD/MM/YYYY)", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateToDate() {
        String Todate = SelectToDate.getText().toString();
        String FromDate = SelectFromDate.getText().toString();
        Date date1 = null, date2 = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date1 = sdf1.parse(Todate);
            date2 = sdf.parse(FromDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Todate.isEmpty()) {
            Toast.makeText(this, "Please select To Date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Todate.matches(DATE_PATTERN)) {
            Toast.makeText(this, "Please select valid date format (DD/MM/YYYY)", Toast.LENGTH_SHORT).show();
            return false;
        } else if (date1.equals(date2)) {
            return true;
        } else if (!date1.after(date2)) {
            Toast.makeText(this, "Please select valid date after", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void ApplyFilterMethod(List<BusinessType> businessTypes, List<BusinessDemand> business_demand, List<City> city, String fromDate, String toDate, int issentEnuiry, int isreceivedEnquiry, String transactionType) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(FavChatInboxActivity.this, "Please wait");
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Applying Filter"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        SearchProductDealer productDealer = new SearchProductDealer(city, businessTypes, business_demand, custId, fromDate, toDate, issentEnuiry, isreceivedEnquiry, transactionType);
        Gson gson = new Gson();
        String json = gson.toJson(productDealer);
        Log.e("Fav_ApplyFilter..", json);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<InboxDealer>> call = customer_interface.GetFavChatFilterList("bearer " + Token, productDealer);
        call.enqueue(new Callback<List<InboxDealer>>() {
            @Override
            public void onResponse(Call<List<InboxDealer>> call, Response<List<InboxDealer>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();

                        List<InboxDealer> dealers = new ArrayList<>();
                        dealers = response.body();
                        if (dealers.size() != 0) {
                            Filter_Count = dealers.size();
                            recyclerView.setVisibility(View.VISIBLE);
                            inboxAdapter = new FavChatInboxAdapter(FavChatInboxActivity.this, dealers, FavChatInboxActivity.this, FavChatInboxActivity.this);
                            recyclerView.setAdapter(inboxAdapter);
                            inboxAdapter.notifyDataSetChanged();
                            tv_Search_Count.setVisibility(View.VISIBLE);
                            tv_Search_Count.setText("Out of " + Total_List_Count + " found " + Filter_Count);
                            Txt_NotFound.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            progressDialog.dismiss();
                            tv_Search_Count.setVisibility(View.GONE);
                            Txt_NotFound.setVisibility(View.VISIBLE);
                        }
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(FavChatInboxActivity.this, "Server Error");
                        //DeleteBtn.setEnabled(false);
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(FavChatInboxActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<InboxDealer>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(FavChatInboxActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(FavChatInboxActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            EnquiryLayout.setVisibility(View.VISIBLE);
            DateFilterLayout.setVisibility(View.GONE);
            TransactionTypeLinear.setVisibility(View.GONE);
            SearchCityLayout.setVisibility(View.GONE);
            lv2.setVisibility(View.GONE);
            lv3.setVisibility(View.GONE);
            lv4.setVisibility(View.GONE);
        } else if (position == 1) {
            DateFilterLayout.setVisibility(View.GONE);
            EnquiryLayout.setVisibility(View.GONE);
            TransactionTypeLinear.setVisibility(View.GONE);
            SearchCityLayout.setVisibility(View.GONE);
            lv3.setVisibility(View.VISIBLE);
            lv2.setVisibility(View.GONE);
            lv4.setVisibility(View.GONE);
            businessTypeAdapter = new FdChild(FavChatInboxActivity.this, businessTypeList);
            lv3.setAdapter(businessTypeAdapter);
            businessTypeAdapter.notifyDataSetChanged();
        } else if (position == 2) {
            DateFilterLayout.setVisibility(View.GONE);
            TransactionTypeLinear.setVisibility(View.GONE);
            EnquiryLayout.setVisibility(View.GONE);
            SearchCityLayout.setVisibility(View.GONE);
            lv4.setVisibility(View.VISIBLE);
            lv2.setVisibility(View.GONE);
            lv3.setVisibility(View.GONE);
            businessDemandAdapter = new FilterBusinessDemandAdapter(FavChatInboxActivity.this, businessDemandList);
            lv4.setAdapter(businessDemandAdapter);
            businessDemandAdapter.notifyDataSetChanged();
        } else if (position == 3) {
            DateFilterLayout.setVisibility(View.GONE);
            TransactionTypeLinear.setVisibility(View.GONE);
            EnquiryLayout.setVisibility(View.GONE);
            SearchCityLayout.setVisibility(View.VISIBLE);
            lv2.setVisibility(View.VISIBLE);
            lv3.setVisibility(View.GONE);
            lv4.setVisibility(View.GONE);
            filterCityAdapter = new FilterCityAdapter(FavChatInboxActivity.this, cityList);
            lv2.setAdapter(filterCityAdapter);
            filterCityAdapter.notifyDataSetChanged();
        } else if (position == 4) {
            DateFilterLayout.setVisibility(View.VISIBLE);
            TransactionTypeLinear.setVisibility(View.GONE);
            EnquiryLayout.setVisibility(View.GONE);
            SearchCityLayout.setVisibility(View.GONE);
            lv2.setVisibility(View.GONE);
            lv3.setVisibility(View.GONE);
            lv4.setVisibility(View.GONE);
            SelectFromDate.setText(SelectedFromDate);
            SelectToDate.setText(SelectedToDate);
        } else if (position == 5) {
            TransactionTypeLinear.setVisibility(View.VISIBLE);
            EnquiryLayout.setVisibility(View.GONE);
            DateFilterLayout.setVisibility(View.GONE);
            SearchCityLayout.setVisibility(View.GONE);
            lv2.setVisibility(View.GONE);
            lv3.setVisibility(View.GONE);
            lv4.setVisibility(View.GONE);
        }
    }

    ///////star click event interface...............///////////////////

    @Override
    public void starclick(int queryid, int custid, int id) {
        switch (id) {
            case 1:
                if (ConnectivityReceiver.isConnected()) {
                    postFavouriteStar(custId, queryid, custid, 1);
                } else {
                    showError();
                }
                break;
            case 2:
                if (ConnectivityReceiver.isConnected()) {
                    postFavouriteStar(custId, queryid, custid, 0);
                } else {
                    showError();
                }

                break;
        }
    }

    private void postFavouriteStar(int Cust_ID, int queryId, int receiverId, int isFavorite) {
        //ProgressDialog progressBar = ShowProgressDialog.createProgressDialog(FavChatInboxActivity.this);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<Void> call = customer_interface.postFavouriteData("bearer " + Token, Cust_ID, queryId, receiverId, isFavorite);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        //progressBar.dismiss();
                        getListInboxMethodFromServer();
                        break;

                    case 404:
                        //  progressBar.dismiss();
                        break;

                    case 500:
                        CustomToast.showToast(FavChatInboxActivity.this, "Server Error ");
                        //progressBar.dismiss();
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(FavChatInboxActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //progressBar.dismiss();
                CustomToast.showToast(FavChatInboxActivity.this, "Error " + t.getMessage());
            }
        });
    }

    //*********Date On Click Listeners**********************
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.FromdateSelect:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                SelectFromDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                //SelectToDate.setEnabled(true);
                datePickerDialog.show();
                break;

            case R.id.TodateSelect:
                // Get Current Date
                final Calendar c1 = Calendar.getInstance();
                mYear = c1.get(Calendar.YEAR);
                mMonth = c1.get(Calendar.MONTH);
                mDay = c1.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog1 = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                SelectToDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog1.show();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                try {
                    cal.setTime(sdf.parse(SelectFromDate.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePickerDialog1.getDatePicker().setMinDate(cal.getTimeInMillis());
                break;
        }
    }

    // Sent/Recv enquiry check box listener
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
        switch (compoundButton.getId()) {
            case R.id.sent_Enq:
                if (ischecked) {
                    isSentEnquiry = 1;
                    Log.e("Sent ", "value....." + isSentEnquiry);
                    Log.e("Rcvd ", "value....." + isReceivedEnquiry);
                    ReceivedEnquiryChk.setEnabled(false);
                } else {
                    isSentEnquiry = 0;

                    ReceivedEnquiryChk.setEnabled(true);
                }
                break;
            case R.id.Rcv_Enq:
                if (ischecked) {
                    isReceivedEnquiry = 1;
                    Log.e("Sent ", "value....." + isSentEnquiry);
                    Log.e("Rcvd ", "value....." + isReceivedEnquiry);
                    SentEnquiryChk.setEnabled(false);
                } else {
                    isReceivedEnquiry = 0;
                    SentEnquiryChk.setEnabled(true);
                }
                break;
            case R.id.ckBuy:
                if (ischecked) {
                    transactionType = "Buy";
                    SellCheck.setEnabled(false);
                } else {
                    transactionType = "";
                    SellCheck.setEnabled(true);
                }
                break;
            case R.id.ckSell:
                if (ischecked) {
                    transactionType = "Sell";
                    BuyCheck.setEnabled(false);
                } else {
                    transactionType = "";
                    BuyCheck.setEnabled(true);
                }
        }
    }

    private class MyAdapter extends ArrayAdapter<Fdlist> {
        public MyAdapter() {
            super(FavChatInboxActivity.this, R.layout.filter_items, fDetails);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(
                        R.layout.filter_items, parent, false);
            }

            Fdlist nation = fDetails.get(position);
            TextView name = (TextView) itemView
                    .findViewById(R.id.textView1);
            name.setText(nation.getFattname());

            return itemView;

        }
    }

    private void getBusinessListFromWebServer() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<BusinessType>> call = customer_interface.getBusinessTypes("bearer " + Token);
        call.enqueue(new Callback<List<BusinessType>>() {
            @Override
            public void onResponse(Call<List<BusinessType>> call, Response<List<BusinessType>> response) {
                int statusCode = response.code();
                switch (statusCode) {

                    case 200:
                        DateFilterLayout.setVisibility(View.GONE);
                        lv3.setVisibility(View.GONE);
                        businessTypeList = new ArrayList<BusinessType>(response.body());
                        businessTypeAdapter = new FdChild(FavChatInboxActivity.this, businessTypeList);
                        lv3.setAdapter(businessTypeAdapter);
                        businessTypeAdapter.notifyDataSetChanged();
                        break;
                    case 404:

                        break;

                    case 500:
                        CustomToast.showToast(FavChatInboxActivity.this, "Server Error");
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(FavChatInboxActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<BusinessType>> call, Throwable t) {
                CustomToast.showToast(FavChatInboxActivity.this, "Time out");
            }
        });

    }

    private void getCitiesList() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<City>> call = customer_interface.GetFavoriteChatCities("bearer " + Token, custId);
        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                int Status_code = response.code();
                switch (Status_code) {
                    case 200:
                        cityList = response.body();
                        if (cityList.isEmpty()) {
                            //Toast.makeText(EnquiryReceivedActivity.this, "Cities not found..", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            filterCityAdapter = new FilterCityAdapter(FavChatInboxActivity.this, cityList);
                        }
                        break;

                    case 500:
                        Toast.makeText(FavChatInboxActivity.this, "Server Error...", Toast.LENGTH_SHORT).show();
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(FavChatInboxActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {

            }
        });
    }


    @Override
    public void onItemSelected(InboxDealer inboxDealer) {
        startActivity(new Intent(this, FavChatActivity.class).
                putExtra("recId", inboxDealer.getCustID()).
                putExtra("phone_no", inboxDealer.getMobileNumber()).
                putExtra("QryID", inboxDealer.getQueryId()).
                putExtra("name", inboxDealer.getFirmName()).
                putExtra("email", inboxDealer.getEmailID()).
                putExtra("purpose", inboxDealer.getPurposeOfBusiness()).
                putExtra("demand", inboxDealer.getBusinessDemand()).
                putExtra("city", inboxDealer.getVillageLocalityname()));
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            inboxAdapter.getFilter().filter(s);
            recyclerView.getRecycledViewPool().clear();
            if (s.length() <= 0) {
                Clear_txt_btn.setVisibility(View.GONE);

            } else {
                Clear_txt_btn.setVisibility(View.VISIBLE);

            }
            if (s.length() != 0 && inboxAdapter.getItemCount() > 0 && Total_List_Count != 0) {
                tv_Search_Count.setText("Out of " + Total_List_Count + " found " + inboxAdapter.getItemCount());
                tv_Search_Count.setVisibility(View.VISIBLE);
            } else {
                tv_Search_Count.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();


        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            if (s.length() <= 0) {
                Clear_txt_btn.setVisibility(View.GONE);
                tv_Search_Count.setVisibility(View.GONE);
            } else {
                Clear_txt_btn.setVisibility(View.VISIBLE);

            }

            if (s.length() != 0 && inboxAdapter.getItemCount() > 0 && Total_List_Count != 0) {
                tv_Search_Count.setText("Out of " + Total_List_Count + " found " + inboxAdapter.getItemCount());
                tv_Search_Count.setVisibility(View.VISIBLE);
            } else {
                tv_Search_Count.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();


        }
    }

    private void showError() {
        Snackbar snackbar = Snackbar
                .make(constraintLayout, "You are not connected to Internet.!", Snackbar.LENGTH_LONG);
        snackbar.show();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class).putExtra("isNewUser", false);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        FavChatInboxActivity.this.finish();
    }
}
