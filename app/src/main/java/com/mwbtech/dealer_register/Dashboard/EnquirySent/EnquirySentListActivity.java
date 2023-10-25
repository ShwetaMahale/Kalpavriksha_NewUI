package com.mwbtech.dealer_register.Dashboard.EnquirySent;

import static com.mwbtech.dealer_register.Dashboard.DashboardActivity.isFilter;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.mwbtech.dealer_register.APIClient.Customer_Client;
import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.AutoSearchFirmNameAdapter;
import com.mwbtech.dealer_register.Adapter.Fdlist;
import com.mwbtech.dealer_register.Adapter.FilterCityAdapter;
import com.mwbtech.dealer_register.Adapter.OutBoxAdapter;
import com.mwbtech.dealer_register.Dashboard.ChatUtil.RecyclerClickLongClickListener;
import com.mwbtech.dealer_register.Dashboard.ChatUtil.RecyclerViewTouchListener;
import com.mwbtech.dealer_register.Dashboard.DashboardActivity;
import com.mwbtech.dealer_register.PojoClass.City;
import com.mwbtech.dealer_register.PojoClass.DeleteInbox;
import com.mwbtech.dealer_register.PojoClass.InboxDealer;
import com.mwbtech.dealer_register.PojoClass.OutboxDealer;
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

public class EnquirySentListActivity extends AppCompatActivity implements OutBoxAdapter.ItemCreationAdapterListener, TextWatcher, AdapterView.OnItemClickListener, View.OnClickListener {


    int CustID;
    String Token;
    PrefManager prefManager;
    public static int QueryID_, ReceiverID_;
    public static String SelectedCities_;
    TextView Txt_NotFound, tv_Search_Count,tv_result;
    RecyclerView recyclerView;
    EditText edSearch;
    ImageView imgBack;
    AppCompatImageView Clear_txt_btn, btnClearCity;
    AppCompatImageView imgFilter, imgReload, imgDelete;
    LinearLayoutCompat constraintLayout;
    List<OutboxDealer> outboxDealers = new ArrayList<>();
    OutBoxAdapter outBoxAdapter;
    public static boolean isclick = false;

    public static List<OutboxDealer> filterList = new ArrayList<>();


    Customer_Interface customer_interface;
    private ActionMode mActionMode;

    List<OutboxDealer> dealerList = new ArrayList<>();
    List<DeleteInbox> deleteInboxes = new ArrayList<>();

    //filter
    private static final String DATE_PATTERN = "^(?:(?:31(|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
    private int mYear, mMonth, mDay;
    ListView lv, lv2;
    Button ApplyFilter;

    CheckBox BuyCheck,SellCheck;
    RelativeLayout DateFilterLayout, SearchFirmLayout, SearchCityLayout, TransactionTypeLinear;
    String transactionType;
    private Context context;
    EditText SelectFromDate, SelectToDate;
    String[] mainFlr = {"Customer", "City", "By Date","Transaction Type"};
    List<Fdlist> Fdetails = new ArrayList<>();
    ArrayList<InboxDealer> firmlist = new ArrayList<>();
    AutoCompleteTextView searchfirmAuto;
    EditText searchCityName;
    AutoSearchFirmNameAdapter nameAdapter;
    FilterCityAdapter filterCityAdapter = null;
    List<City> getCityList = new ArrayList<>();

    public static List<City> cityList = new ArrayList<>();
    String ConvertedFromDate, ConvertedToDate;
    public static String SelectedFromDate, SelectedToDate, SelectedFirmName;
    public static int ReceiverID = 0;
    MenuItem DeleteBtn;
    int Total_List_Count, Filter_Count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=EnquirySentListActivity.this;
        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
    }

    private void showContent() {
        setContentView(R.layout.activity_out_box_list);
        initializeViews();
        initializeSharedData();
        initializeRecyclerView();
        checkIsAppliedFilter();
        initializeRecyclerTouchEvent();
        initializeClickEvent();
    }

    private void showNoSignalContent() {
        setContentView(R.layout.no_signal_layout);

        Button tryButton = findViewById(R.id.tryBtn);
       /* ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
      */  tryButton.setOnClickListener(new View.OnClickListener() {
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
        edSearch = findViewById(R.id.searchName);
        Clear_txt_btn = findViewById(R.id.clear_txt_Prise);
        Txt_NotFound = findViewById(R.id.not_found_txt);
        imgBack = findViewById(R.id.back);
        tv_result=findViewById(R.id.no_reult);
        tv_Search_Count = findViewById(R.id.search_count);
        imgDelete = findViewById(R.id.delete);
        imgFilter = findViewById(R.id.menu);
        imgReload = findViewById(R.id.reload);
      /*  ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
    */}

    private void initializeSharedData() {
        prefManager = new PrefManager(this);
        CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        Token = prefManager.getToken().get(TOKEN);
    }

    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(outBoxAdapter);
    }

    private void checkIsAppliedFilter() {
        if (isFilter == true) {
            if (filterList.size() != 0 | !filterList.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
                outBoxAdapter = new OutBoxAdapter(context, filterList, this);
                recyclerView.setAdapter(outBoxAdapter);
                outBoxAdapter.notifyDataSetChanged();
            } else {
                recyclerView.setVisibility(View.GONE);
            }

        } else {
            GetOutboxDealerList();
        }
    }

    private void initializeRecyclerTouchEvent() {
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

    private void initializeClickEvent() {
        edSearch.addTextChangedListener(this);
        imgBack.setOnClickListener((v) -> {
            onBackPressed();
            finish();
        });
        imgDelete.setOnClickListener(v -> {
            int count = 0;
            try {
                if (ConnectivityReceiver.isConnected()) {
                    if (outBoxAdapter.getUpdatedList().size() == 0 | outBoxAdapter.getUpdatedList() == null) {
                        //do nothing
                    } else {
                        onListItemSelect(count);
                    }
                } else {
                    Toast.makeText(this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        imgFilter.setOnClickListener(v -> {
            if (ConnectivityReceiver.isConnected()) {
                if (isFilter == true) {
                    GetListOfFirmNames();
                } else {
                    GetListOfFirmNames();
                    getCitiesList();
                }
                openPopUpdialog();
            } else {
                Toast.makeText(this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
            }
        });
        imgReload.setOnClickListener(v -> {
            if (ConnectivityReceiver.isConnected()) {
                isFilter = false;
                cityList.clear();
                ReceiverID = 0;
                SelectedFromDate = "";
                SelectedToDate = "";

                GetOutboxDealerList();
                tv_Search_Count.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
            }
        });
        Clear_txt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edSearch.getText().toString().isEmpty()) {
                    edSearch.getText().clear();
                    Clear_txt_btn.setVisibility(View.GONE);
                    //hide keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                }

            }
        });
    }

    private void onListItemSelect(int position) {

        outBoxAdapter.toggleSelection(position);//Toggle the selection
        boolean hasCheckedItems = outBoxAdapter.getSelectedCount() > 0;//Check if any items are already selected or not

        if(mActionMode == null)
            mActionMode = startSupportActionMode(new EnquirySentListToolbarActionMode(this, outBoxAdapter, this, outboxDealers));

        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode

            mActionMode = startSupportActionMode(new EnquirySentListToolbarActionMode(this, outBoxAdapter, this, outboxDealers));
        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
//            Toast.makeText(this, "Please select a product to delete.!", Toast.LENGTH_SHORT).show();
            mActionMode.finish();

        if (mActionMode != null)
            //set action mode title on item selection
            mActionMode.setTitle(outBoxAdapter
                    .getSelectedCount() + " selected");
    }


    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    public void selectAll() {
        outBoxAdapter.selectAll();
        int count = outBoxAdapter.getSelectedItemCount();
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
        SparseBooleanArray selected = outBoxAdapter.getSelectedIds();//Get selected ids

        //Loop all selected ids
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {

                //If current id is selected remove the item via key
                dealerList.add(outboxDealers.get(selected.keyAt(i)));
                outboxDealers.remove(selected.keyAt(i));
                outBoxAdapter.notifyDataSetChanged();//notify CitySelectedAdapter
            }
        }

        for (OutboxDealer outDealer : dealerList) {
            deleteInboxes.add(new DeleteInbox(CustID, outDealer.getQueryId()));
        }

        if (ConnectivityReceiver.isConnected()) {
            deleteEnquiryFromOutbox(deleteInboxes);
            mActionMode.finish();//Finish action mode after use
        } else {
            showError();
        }


    }

    private void deleteEnquiryFromOutbox(List<DeleteInbox> deleteInboxes) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(context,"Please wait");
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Deleting Chat"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        Gson gson = new Gson();
        String Json = gson.toJson(deleteInboxes);
        Log.e("OutboxRowDeleted..",Json);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<DeleteInbox>> call = customer_interface.DeleteEnquiry("bearer " + Token, CustID, deleteInboxes);
        call.enqueue(new Callback<List<DeleteInbox>>() {
            @Override
            public void onResponse(Call<List<DeleteInbox>> call, Response<List<DeleteInbox>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        outBoxAdapter.notifyDataSetChanged();

                        if (outBoxAdapter.getItemCount() > 0) {

                            recyclerView.setVisibility(View.VISIBLE);

                        } else {
                            recyclerView.setVisibility(View.GONE);

                        }

                        GetOutboxDealerList();

                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(context, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(context);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<DeleteInbox>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(context, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(context, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void GetOutboxDealerList() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(context,"Please wait");
//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading...."); // Setting Message
//        //progressDialog.setTitle("Loading details"); // Setting Title
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        ReceiverID = 0;
        getCityList.clear();
        SearchProductDealer searchProductDealer = new SearchProductDealer(ReceiverID, CustID, "", "", getCityList);
        Gson gson = new Gson();
        String json = gson.toJson(searchProductDealer);
        Log.e("GetOutboxDealerList...", json);
        Call<List<OutboxDealer>> call = customer_interface.GetEnquiryDealerList("bearer " + Token, searchProductDealer);
        call.enqueue(new Callback<List<OutboxDealer>>() {
            @Override
            public void onResponse(Call<List<OutboxDealer>> call, Response<List<OutboxDealer>> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        progressDialog.dismiss();
                        outboxDealers.clear();
                        outboxDealers = response.body();
                        if (outboxDealers.size() != 0) {
                            Total_List_Count = outboxDealers.size();
                            recyclerView.setVisibility(View.VISIBLE);
                            outBoxAdapter = new OutBoxAdapter(context, outboxDealers, EnquirySentListActivity.this);
                            recyclerView.setAdapter(outBoxAdapter);
                            outBoxAdapter.notifyDataSetChanged();
                           // DeleteBtn.setEnabled(true);
                            Txt_NotFound.setVisibility(View.GONE);
                        } else {
                            progressDialog.dismiss();
                            ReceiverID = 0;
                            recyclerView.setVisibility(View.GONE);
                            //DeleteBtn.setEnabled(false);
                            Txt_NotFound.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(context, "Server Error");
                        //DeleteBtn.setEnabled(false);
                        break;

                    case 404:
                        progressDialog.dismiss();
                        ReceiverID = 0;
                        recyclerView.setVisibility(View.GONE);
                        //DeleteBtn.setEnabled(false);
                        Txt_NotFound.setVisibility(View.VISIBLE);
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(context);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<OutboxDealer>> call, Throwable t) {
                progressDialog.dismiss();
                //DeleteBtn.setEnabled(false);
                if (t instanceof IOException) {
                    Toast.makeText(context, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(context, "Bad response from server.. Try again later ");
                }
            }
        });
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        try {
            outBoxAdapter.getFilter().filter(s);
            recyclerView.getRecycledViewPool().clear();
            if (s.length() <= 0) {

                Clear_txt_btn.setVisibility(View.GONE);
                tv_Search_Count.setVisibility(View.GONE);
            } else {
                Clear_txt_btn.setVisibility(View.VISIBLE);
            }

            if (s.length() != 0 && outBoxAdapter.getItemCount() > 0 && Total_List_Count != 0) {
                tv_Search_Count.setText("Out of " + Total_List_Count + " found " + outBoxAdapter.getItemCount());
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
            outBoxAdapter.getFilter().filter(s);
            recyclerView.getRecycledViewPool().clear();
            if (s.length() <= 0) {
                Clear_txt_btn.setVisibility(View.GONE);
                tv_Search_Count.setVisibility(View.GONE);
            } else {
                Clear_txt_btn.setVisibility(View.VISIBLE);
            }

            if (s.length() != 0 && outBoxAdapter.getItemCount() > 0 && Total_List_Count != 0) {
                tv_Search_Count.setText("Out of " + Total_List_Count + " found " + outBoxAdapter.getItemCount());
                tv_Search_Count.setVisibility(View.VISIBLE);
            } else {
                tv_Search_Count.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    @Override
    public void onItemSelected(OutboxDealer outboxDealer) {
        isclick = true;

        QueryID_ = outboxDealer.getQueryId();
        ReceiverID_ = ReceiverID;
        SelectedCities_ = outboxDealer.getSelectedCities();
        String selectedCities = outboxDealer.getSelectedCities();

        startActivity(new Intent(this, EnquirySentListDealerActivity.class).
                putExtra("QueryID", outboxDealer.getQueryId())
                .putExtra("CityID",outboxDealer.getCityId())
                .putExtra("ReceiverID", ReceiverID)
                .putExtra("selectedCities", selectedCities));
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//         Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.outbox_main_menu, menu);
//        DeleteBtn = menu.findItem(R.id.delete);
//        return true;
//    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            case R.id.refresh:
                if (ConnectivityReceiver.isConnected()) {
                    isFilter = false;
                    cityList.clear();
                    ReceiverID = 0;
                    SelectedFromDate = "";
                    SelectedToDate = "";

                    GetOutboxDealerList();
                    tv_Search_Count.setVisibility(View.GONE);
                } else {
                    Toast.makeText(this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.filter:
                if (ConnectivityReceiver.isConnected()) {
                    if (isFilter == true) {
                        GetListOfFirmNames();
                    } else {
                        GetListOfFirmNames();
                        getCitiesList();
                    }
                    openPopUpdialog();
                } else {
                    Toast.makeText(this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
                }

                return true;

            case R.id.delete:
                int count = 0;
                try {
                    if (ConnectivityReceiver.isConnected()) {
                        if (outBoxAdapter.getUpdatedList().size() == 0 | outBoxAdapter.getUpdatedList() == null) {
                            //do nothing
                        } else {
                            onListItemSelect(count);

                        }
                    } else {
                        Toast.makeText(this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;

        }
        return super.onOptionsItemSelected(item);
    }*/

    private void GetListOfFirmNames() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(context,"Please wait");
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<InboxDealer>> call = customer_interface.GetUsersFirmName("bearer " + Token, CustID);
        call.enqueue(new Callback<List<InboxDealer>>() {
            @Override
            public void onResponse(Call<List<InboxDealer>> call, Response<List<InboxDealer>> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        progressDialog.dismiss();
                        firmlist = (ArrayList<InboxDealer>) response.body();
                        nameAdapter = new AutoSearchFirmNameAdapter(context, R.layout.statelist_layout, firmlist);
                        searchfirmAuto.setAdapter(nameAdapter);
                        searchfirmAuto.setThreshold(2);
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;


                    case 500:
                        CustomToast.showToast(context, "Server Error");
                        progressDialog.dismiss();
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(context);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<InboxDealer>> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof IOException) {
                    Toast.makeText(context, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(context, "Bad response from server.. Try again later ");
                }
            }
        });
    }
    private void openPopUpdialog() {
        Fdetails.clear();
        getCityList.clear();
        ConvertedFromDate = "";
        ConvertedToDate = "";

        Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.outbox_filter);
        mDialog.setCancelable(true);
        lv = (ListView) mDialog.findViewById(R.id.listView1);
        lv2 = (ListView) mDialog.findViewById(R.id.listView2);
        lv.setOnItemClickListener(this);

        DateFilterLayout = mDialog.findViewById(R.id.DateLayout);
        SearchFirmLayout = mDialog.findViewById(R.id.SearchLayout);
        SearchCityLayout = mDialog.findViewById(R.id.CityLayout);
        TransactionTypeLinear=mDialog.findViewById(R.id.linearTranscation);
        searchfirmAuto = mDialog.findViewById(R.id.SearchFirmName);
        searchCityName = mDialog.findViewById(R.id.SearchCityName);
        SelectFromDate = mDialog.findViewById(R.id.FromdateSelect);
        SelectToDate = mDialog.findViewById(R.id.TodateSelect);
        btnClearCity = mDialog.findViewById(R.id.clear_txt_Prise);
        BuyCheck=mDialog.findViewById(R.id.ckBuy);
        SellCheck=mDialog.findViewById(R.id.ckSell);
        SelectFromDate.setOnClickListener(this);
        SelectToDate.setOnClickListener(this);
        BuyCheck.setOnCheckedChangeListener(this::onCheckedChanged);
        SellCheck.setOnCheckedChangeListener(this::onCheckedChanged);

        //SelectToDate.setEnabled(false);
        ApplyFilter = mDialog.findViewById(R.id.Apply);
        for (int i = 0; i < 4; i++) {
            Fdetails.add(new Fdlist(mainFlr[i]));
        }


        ArrayAdapter<Fdlist> adapter = new MyAdapter();
        lv.setAdapter(adapter);

        if (isFilter == true) {
            if (!SelectFromDate.getText().toString().isEmpty()) {
                SelectFromDate.setText(SelectedFromDate);
                SelectToDate.setText(SelectedToDate);
            }
            if (!TextUtils.isEmpty(SelectedFirmName)) {
                searchfirmAuto.setText(SelectedFirmName);
            }
            if (cityList.size() != 0) {
                filterCityAdapter = new FilterCityAdapter(this, cityList);
                lv2.setAdapter(filterCityAdapter);
                filterCityAdapter.notifyDataSetChanged();
            }
            if (ReceiverID == 0) {
                searchfirmAuto.getText().clear();
            }
        }


       /* nameAdapter = new AutoSearchFirmNameAdapter(context,R.layout.statelist_layout,firmlist);
        searchfirmAuto.setAdapter(nameAdapter);
        searchfirmAuto.setThreshold(2);

        //set city
        filterCityAdapter = new FilterCityAdapter(context,cityList);
        lv2.setAdapter(filterCityAdapter);*/
        if(transactionType=="Buy"){
            BuyCheck.setChecked(true);
            SellCheck.setChecked(false);
        }
        if(transactionType=="Sell"){
            BuyCheck.setChecked(false);
            SellCheck.setChecked(true);
        }
        mDialog.show();

        searchfirmAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InboxDealer item = (InboxDealer) adapterView.getItemAtPosition(i);
                ReceiverID = item.getCustID();
                SelectedFirmName = item.getFirmName();
                //hide keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });
        searchCityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {}

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
                        if (TextUtils.isEmpty(searchfirmAuto.getText().toString()) &&
                                TextUtils.isEmpty(SelectFromDate.getText().toString()) &&
                                filterCityAdapter.GetSelectedCity().isEmpty() | filterCityAdapter.GetSelectedCity() == null && transactionType=="") {
                            Toast.makeText(context, "Please select appropriate data", Toast.LENGTH_SHORT).show();
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
                                        getCityList = filterCityAdapter.GetSelectedCity();

                                        //to get the checked city list
                                        cityList = filterCityAdapter.GetResultCity();
                                        Log.e("Selected", "City.............." + cityList.size());

                                        ApplyFilterMethod(CustID, ReceiverID, ConvertedFromDate, ConvertedToDate, getCityList,transactionType);
                                        mDialog.dismiss();
                                    }
                                }
                            } else {

                                getCityList = filterCityAdapter.GetSelectedCity();
                                //to get the checked city list
                                cityList = filterCityAdapter.GetResultCity();
                                Log.e("Selected", "City.............." + cityList.size());
                                ApplyFilterMethod(CustID, ReceiverID, ConvertedFromDate, ConvertedToDate, getCityList,transactionType);
                                mDialog.dismiss();
                            }
                        }
                    } else {
                        showError();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }
    public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
        switch (compoundButton.getId()) {
            case R.id.ckBuy:
                if(ischecked){
                    transactionType="Buy";
                    SellCheck.setEnabled(false);
                }
                else{
                    transactionType="";
                    SellCheck.setEnabled(true);
                }
                break;
            case R.id.ckSell:
                if(ischecked){
                    transactionType="Sell";
                    BuyCheck.setEnabled(false);
                }
                else{
                    transactionType="";
                    BuyCheck.setEnabled(true);
                }
        }
    }
    private void ApplyFilterMethod(int custID, int receiverID, String convertedFromDate, String convertedToDate, List<City> city,String transactionType) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(context,"Please wait");
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Applying Filter"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        SearchProductDealer searchProductDealer = new SearchProductDealer(receiverID, custID, convertedFromDate, convertedToDate, city,transactionType);
        Log.e("Receiver", "ID...." + ReceiverID);
        Gson gson = new Gson();
        String json = gson.toJson(searchProductDealer);
        Log.e("Sent_Filter...", json);
        Call<List<OutboxDealer>> call = customer_interface.GetEnquiryDealerList("bearer " + Token, searchProductDealer);
        call.enqueue(new Callback<List<OutboxDealer>>() {
            @Override
            public void onResponse(Call<List<OutboxDealer>> call, Response<List<OutboxDealer>> response) {
                int status_code = response.code();
                Log.e("Status", "code...." + status_code);
                switch (status_code) {
                    case 200:
                        progressDialog.dismiss();
                        isFilter = true;
                        // List<OutboxDealer> outboxDealers = new ArrayList<>();
                        outboxDealers = response.body();
                        filterList = response.body();

                        Log.e("Outbox", "count...." + outboxDealers.size());
                        if (outboxDealers.size() != 0) {
                            Filter_Count = outboxDealers.size();
                            recyclerView.setVisibility(View.VISIBLE);
                            outBoxAdapter = new OutBoxAdapter(context, outboxDealers, EnquirySentListActivity.this);
                            recyclerView.setAdapter(outBoxAdapter);
                            outBoxAdapter.notifyDataSetChanged();
                            if(DeleteBtn!=null) {
                                DeleteBtn.setEnabled(true);
                            }
                            tv_Search_Count.setVisibility(View.VISIBLE);
                            tv_Search_Count.setText("Out of " + Total_List_Count + " found " + Filter_Count);
                            Txt_NotFound.setVisibility(View.GONE);
                        } else {

                            progressDialog.dismiss();
                            ReceiverID = 0;
                            recyclerView.setVisibility(View.GONE);
                            //DeleteBtn.setEnabled(false);
                            tv_Search_Count.setVisibility(View.GONE);
                            Txt_NotFound.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 500:
                        isFilter = false;
                        progressDialog.dismiss();
                        CustomToast.showToast(context, "Server Error");
                        //DeleteBtn.setEnabled(false);
                        break;

                    case 404:
                        progressDialog.dismiss();
                        isFilter = false;
                        ReceiverID = 0;
                        recyclerView.setVisibility(View.GONE);
                        //DeleteBtn.setEnabled(false);
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(context);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<OutboxDealer>> call, Throwable t) {
                isFilter = false;
                progressDialog.dismiss();
                //DeleteBtn.setEnabled(false);
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(context, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(context, "Bad response from server.. Try again later ");
                }
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (i == 0) {
            DateFilterLayout.setVisibility(View.GONE);
            TransactionTypeLinear.setVisibility(View.GONE);
            SearchFirmLayout.setVisibility(View.VISIBLE);
            SearchCityLayout.setVisibility(View.GONE);
        } else if (i == 1) {
            DateFilterLayout.setVisibility(View.GONE);
            SearchFirmLayout.setVisibility(View.GONE);
            TransactionTypeLinear.setVisibility(View.GONE);
            SearchCityLayout.setVisibility(View.VISIBLE);
            filterCityAdapter = new FilterCityAdapter(this, cityList);
            lv2.setAdapter(filterCityAdapter);
            filterCityAdapter.notifyDataSetChanged();
        } else if (i == 2) {
            DateFilterLayout.setVisibility(View.VISIBLE);
            TransactionTypeLinear.setVisibility(View.GONE);
            SearchFirmLayout.setVisibility(View.GONE);
            SearchCityLayout.setVisibility(View.GONE);
            SelectFromDate.setText(SelectedFromDate);
            SelectToDate.setText(SelectedToDate);
        }
        else if(i == 3){
            TransactionTypeLinear.setVisibility(View.VISIBLE);
            DateFilterLayout.setVisibility(View.GONE);
            SearchFirmLayout.setVisibility(View.GONE);
            SearchCityLayout.setVisibility(View.GONE);
        }

    }


    private class MyAdapter extends ArrayAdapter<Fdlist> {
        public MyAdapter() {
            super(context, R.layout.filter_items, Fdetails);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(
                        R.layout.filter_items, parent, false);
            }
            Fdlist nation = Fdetails.get(position);
            TextView name = (TextView) itemView
                    .findViewById(R.id.textView1);
            name.setText(nation.getFattname());
            return itemView;
        }
    }

    private void getCitiesList() {
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<City>> call = customer_interface.getOutboxDealerCities("bearer " + Token, CustID);
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
                            filterCityAdapter = new FilterCityAdapter(EnquirySentListActivity.this, cityList);
                        }
                        break;

                    case 500:
                        Toast.makeText(context, "Server Error...", Toast.LENGTH_SHORT).show();
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(context);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(context, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(context, "Bad response from server.. Try again later ");
                }
            }
        });
    }

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
                                //SelectToDate.setEnabled(true);
                            }
                        }, mYear, mMonth, mDay);
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

    private boolean validateFromDate() {
        String Fromdate = SelectFromDate.getText().toString();

        if (Fromdate.isEmpty()) {
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
            Toast.makeText(this, "Please select To date", Toast.LENGTH_SHORT).show();
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

    private void showError() {
        Snackbar snackbar = Snackbar
                .make(constraintLayout, "You are not connected to Internet.!", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isFilter = false;
        Intent intent = new Intent(this, DashboardActivity.class).putExtra("isNewUser", false);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}