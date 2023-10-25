package com.mwbtech.dealer_register.Dashboard.EnquirySent;

import static com.mwbtech.dealer_register.Dashboard.EnquirySent.EnquirySentListActivity.QueryID_;
import static com.mwbtech.dealer_register.Dashboard.EnquirySent.EnquirySentListActivity.ReceiverID_;
import static com.mwbtech.dealer_register.Dashboard.EnquirySent.EnquirySentListActivity.SelectedCities_;
import static com.mwbtech.dealer_register.Dashboard.EnquirySent.EnquirySentListActivity.isclick;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
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
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.mwbtech.dealer_register.Adapter.FdChild;
import com.mwbtech.dealer_register.Adapter.Fdlist;
import com.mwbtech.dealer_register.Adapter.Fdlist1;
import com.mwbtech.dealer_register.Adapter.InboxAdapter;
import com.mwbtech.dealer_register.Dashboard.ChatUtil.RecyclerClickLongClickListener;
import com.mwbtech.dealer_register.Dashboard.ChatUtil.RecyclerViewTouchListener;
import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.PojoClass.DeleteInbox;
import com.mwbtech.dealer_register.PojoClass.GetEnquiryConversationRequest;
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

public class EnquirySentListDealerActivity extends AppCompatActivity implements InboxAdapter.starClickEvent, InboxAdapter.ItemCreationAdapterListener, AdapterView.OnItemClickListener, View.OnClickListener, TextWatcher {

    int QueryID, ReceiverID, CustID,cityId;
    String Token, selectedCities;
    PrefManager prefManager;
    EditText edSearch;
    AppCompatImageView Clear_txt_btn, back, reload, imgFilter, delete;
    RecyclerView recyclerView;
    LinearLayoutCompat constraintLayout;
    Customer_Interface customer_interface;
    ArrayList<InboxDealer> inboxDealers;
    InboxAdapter inboxAdapter;
    TextView Txt_NotFound, tv_Search_Count;
    private ActionMode mActionMode;
    List<InboxDealer> dealerList = new ArrayList<>();
    List<DeleteInbox> deleteInboxes = new ArrayList<>();
    List<ReadUnreadModel> readUnreadModelList = new ArrayList<>();

    //filter
    private static final String DATE_PATTERN = "^(?:(?:31(|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
    private int mYear, mMonth, mDay;
    ListView lv, lv2;
    Button ApplyFilter;
    RelativeLayout DateFilterLayout;
    EditText SelectFromDate, SelectToDate;
    String[] mainFlr = {"Business Type", "By Date"};
    //String[] DmdFlr = {"Less than 1 lakh","1 to 5 Lakh","Above 5 Lakh"};
    List<Fdlist> Fdetails = new ArrayList<>();
    List<Fdlist1> Fdetails1 = new ArrayList<>();

    List<BusinessType> businessTypeList = new ArrayList<>();
    List<BusinessType> businessTypes = new ArrayList<>();
    FdChild businessTypeAdapter = null;


    String ConvertedFromDate, ConvertedToDate, SelectedFromDate, SelectedToDate;
    MenuItem DeleteBtn;
    int Total_List_Count, Filter_Count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ConnectivityReceiver.isConnected()) {
            showContent();
        } else {
            showNoSignalContent();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.red, this.getTheme()));
        } else {
            getWindow().setStatusBarColor(getResources().getColor(R.color.red));
        }
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void showContent() {
        setContentView(R.layout.activity_outbox_list_dealer);
        initializeViews();
        initializeSharedData();
        initializeRecyclerView();
        checkIsAppliedFilter();
        GetListofDealers();
        initializeClickEvents();
        initializeRecyclerViewTouchListener();
    }

    private void showNoSignalContent() {
        setContentView(R.layout.no_signal_layout);

        Button tryButton = findViewById(R.id.tryBtn);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
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
        edSearch = findViewById(R.id.searchName);
        Clear_txt_btn = findViewById(R.id.clear_txt_Prise);
        Txt_NotFound = findViewById(R.id.not_found_txt);
        tv_Search_Count = findViewById(R.id.search_count);
        back = findViewById(R.id.back);
        delete = findViewById(R.id.delete);
        imgFilter = findViewById(R.id.img_filter);
        reload = findViewById(R.id.reload);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));
    }

    private void initializeSharedData() {
        prefManager = new PrefManager(this);
        CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        Token = prefManager.getToken().get(TOKEN);
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(inboxAdapter);
    }


    private void checkIsAppliedFilter() {
        if(getIntent()==null || getIntent().getExtras()==null) return;
        cityId = getIntent().getExtras().getInt("CityID",0);
        if (isclick == true) {
            QueryID = getIntent().getExtras().getInt("QueryID");
            ReceiverID = getIntent().getExtras().getInt("ReceiverID");
            selectedCities = getIntent().getExtras().getString("selectedCities");

        } else {
            QueryID = QueryID_;
            ReceiverID = ReceiverID_;
            selectedCities = SelectedCities_;
        }
    }

    private void initializeClickEvents() {
        edSearch.addTextChangedListener(this);
        Clear_txt_btn.setOnClickListener(view -> {
            if (!edSearch.getText().toString().isEmpty()) {
                edSearch.getText().clear();
                Clear_txt_btn.setVisibility(View.GONE);
                //hide keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }

        });
        back.setOnClickListener(view -> {
            onBackPressed();
            finish();
        });
        imgFilter.setOnClickListener(view -> {
            if (ConnectivityReceiver.isConnected()) {
                if (businessTypeList.size() == 0) {
                    getBusinessListFromWebServer();
                }
                openPopUpdialog();
            } else {
                Toast.makeText(this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
            }
        });
        reload.setOnClickListener(view -> {
            if (ConnectivityReceiver.isConnected()) {
                SelectedFromDate = "";
                SelectedToDate = "";
                businessTypeList.clear();
                GetListofDealers();
                tv_Search_Count.setVisibility(View.GONE);
            } else {
                Toast.makeText(EnquirySentListDealerActivity.this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
            }
        });
        delete.setOnClickListener(view -> {
            int count = 0;
            try {
                if (ConnectivityReceiver.isConnected()) {
                    if (inboxAdapter.getUpdatedList().size() == 0 | inboxAdapter.getUpdatedList() == null) {
                        //do nothing
                    } else {
                        for (int i = 0; i < inboxAdapter.getUpdatedList().size(); i++) {
                            if (inboxAdapter.getUpdatedList().get(i).getIsFavorite() == 1) {
                                //count =i+1;
                                //onListItemSelect(count);
                            } else {
                                count = i;
                                onListItemSelect(count);
                                break;
                            }
                        }
                    }
                    if (inboxAdapter.getSelectedCount() == 0) {
                        CustomToast.showToast(EnquirySentListDealerActivity.this, "Please unfavourite your chat to delete");
                    } else {
                        //do nothing
                    }
                } else {
                    Toast.makeText(EnquirySentListDealerActivity.this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
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

        if(mActionMode == null)
            mActionMode = startSupportActionMode(new EnquirySentToolbarActionMode(this, inboxAdapter, this, inboxDealers));

        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode

            mActionMode = startSupportActionMode(new EnquirySentToolbarActionMode(this, inboxAdapter, this, inboxDealers));
        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();

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
            deleteInboxes.add(new DeleteInbox(CustID, inboxDealer.getQueryId(), inboxDealer.getCustID()));
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
            readUnreadModelList.add(new ReadUnreadModel(inboxDealer.getQueryId(), CustID, inboxDealer.getCustID(), 1));
        }

        if (ConnectivityReceiver.isConnected()) {
            MarkChatAsReadUnRead(readUnreadModelList);
            mActionMode.finish();//Finish action mode after use
        } else {
            showError();
        }

    }


    //set mark as un read
    public void goAlertAsUnRead() {
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
            readUnreadModelList.add(new ReadUnreadModel(inboxDealer.getQueryId(), CustID, inboxDealer.getCustID(), 0));
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
                        GetListofDealers();
                        break;

                    case 404:
                        break;

                    case 500:
                        CustomToast.showToast(EnquirySentListDealerActivity.this, "Server Error");
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(EnquirySentListDealerActivity.this);
                        break;

                }

            }

            @Override
            public void onFailure(Call<List<ReadUnreadModel>> call, Throwable t) {
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(EnquirySentListDealerActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EnquirySentListDealerActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    private void deleteConversionFromInbox(List<DeleteInbox> dealerList) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(EnquirySentListDealerActivity.this, "Please wait");
        progressDialog.setMessage("Please wait..."); // Setting Message
        progressDialog.setTitle("Deleting Chat"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        Gson gson = new Gson();
        String json = gson.toJson(dealerList);
        Log.e("deleteConversation..", json);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<DeleteInbox>> call = customer_interface.deleteConversion("bearer " + Token, CustID, dealerList);
        call.enqueue(new Callback<List<DeleteInbox>>() {
            @Override
            public void onResponse(Call<List<DeleteInbox>> call, Response<List<DeleteInbox>> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        progressDialog.dismiss();
                        inboxAdapter.notifyDataSetChanged();
                        //CustomToast.showToast(EnquiryReceivedActivity.this,"Deleted.");
                        if (inboxAdapter.getItemCount() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.GONE);

                        }

                        GetListofDealers();
                        break;

                    case 404:
                        progressDialog.dismiss();
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(EnquirySentListDealerActivity.this, "Server Error");
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(EnquirySentListDealerActivity.this);
                        break;

                }

            }

            @Override
            public void onFailure(Call<List<DeleteInbox>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(EnquirySentListDealerActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EnquirySentListDealerActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });

    }

    private void GetListofDealers() {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(EnquirySentListDealerActivity.this, "");
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Log.e("msg...", "," + QueryID + "," + CustID + "," + ReceiverID);

        GetEnquiryConversationRequest request = new GetEnquiryConversationRequest(QueryID, ReceiverID, CustID, 0, selectedCities);
        Call<List<InboxDealer>> call = customer_interface.GetOutBoxListDealers("bearer " + Token, request);
//        Call<List<InboxDealer>> call = customer_interface.GetOutBoxListDealers("bearer " + Token, QueryID, CustID, ReceiverID, 0,selectedCities);
        call.enqueue(new Callback<List<InboxDealer>>() {
            @Override
            public void onResponse(Call<List<InboxDealer>> call, Response<List<InboxDealer>> response) {
                int status_code = response.code();
                switch (status_code) {
                    case 200:
                        progressDialog.dismiss();
                        inboxDealers = new ArrayList<>(response.body());
                        if (inboxDealers.size() != 0) {
                            Total_List_Count = inboxDealers.size();
                            recyclerView.setVisibility(View.VISIBLE);
                            inboxAdapter = new InboxAdapter(EnquirySentListDealerActivity.this, inboxDealers, EnquirySentListDealerActivity.this, EnquirySentListDealerActivity.this);
                            recyclerView.setAdapter(inboxAdapter);
                            inboxAdapter.notifyDataSetChanged();
                            //DeleteBtn.setEnabled(true);
                            Txt_NotFound.setVisibility(View.GONE);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            //DeleteBtn.setEnabled(false);
                            Txt_NotFound.setVisibility(View.VISIBLE);
                            tv_Search_Count.setVisibility(View.GONE);
                        }
                        break;

                    case 404:
                        progressDialog.dismiss();
                        // DeleteBtn.setEnabled(false);
                        recyclerView.setVisibility(View.GONE);
                        Txt_NotFound.setVisibility(View.VISIBLE);
                        tv_Search_Count.setVisibility(View.GONE);
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(EnquirySentListDealerActivity.this, "Server Error");
                        //DeleteBtn.setEnabled(false);
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(EnquirySentListDealerActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<InboxDealer>> call, Throwable t) {
                progressDialog.dismiss();
                // DeleteBtn.setEnabled(false);
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(EnquirySentListDealerActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EnquirySentListDealerActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    //***************On fav click to make fav and non fav
    @Override
    public void starclick(int queryid, int custid, int id) {
        switch (id) {
            case 1:
                if (ConnectivityReceiver.isConnected()) {
                    postFavouriteStar(CustID, queryid, custid, 1);
                } else {
                    showError();
                }

                break;
            case 2:
                if (ConnectivityReceiver.isConnected()) {
                    postFavouriteStar(CustID, queryid, custid, 0);
                } else {
                    showError();
                }

                break;
        }
    }

    private void postFavouriteStar(int Cust_ID, int queryId, int receiverId, int isFavorite) {
        // ProgressDialog progressBar = ShowProgressDialog.createProgressDialog(EnquirySentListDealerActivity.this);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<Void> call = customer_interface.postFavouriteData("bearer " + Token, Cust_ID, queryId, receiverId, isFavorite);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int statusCode = response.code();
                switch (statusCode) {
                    case 200:
                        //progressBar.dismiss();
                        GetListofDealers();
                        break;

                    case 404:
                        // progressBar.dismiss();
                        break;

                    case 500:
                        CustomToast.showToast(EnquirySentListDealerActivity.this, "Server Error ");
                        //  progressBar.dismiss();
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(EnquirySentListDealerActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // progressBar.dismiss();
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(EnquirySentListDealerActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EnquirySentListDealerActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    //***********On list click to navigate to chat activity
    @Override
    public void onItemSelected(InboxDealer inboxDealer) {
        isclick = false;
        Gson gson = new Gson();
        Log.e("sentmodel....", gson.toJson(inboxDealer));
        Log.e("revid....", String.valueOf(inboxDealer.getCustID()));
        startActivity(new Intent(this, EnquirySentChatActivity.class).
                putExtra("recId", inboxDealer.getCustID()).
                putExtra("phone_no", inboxDealer.getMobileNumber()).
                putExtra("QryID", inboxDealer.getQueryId()).
                putExtra("name", inboxDealer.getFirmName()).
                putExtra("email", inboxDealer.getEmailID()).
                putExtra("purpose", inboxDealer.getPurposeOfBusiness()).
                putExtra("demand", inboxDealer.getBusinessDemand()).
                putExtra("city", inboxDealer.getVillageLocalityname()).
                putExtra("CityID",cityId)
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_refresh, menu);
        DeleteBtn = menu.findItem(R.id.delete);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            case R.id.refresh:
                if (ConnectivityReceiver.isConnected()) {
                    SelectedFromDate = "";
                    SelectedToDate = "";
                    businessTypeList.clear();
                    GetListofDealers();
                    tv_Search_Count.setVisibility(View.GONE);
                } else {
                    Toast.makeText(this, "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.filter:
                if (ConnectivityReceiver.isConnected()) {
                    if (businessTypeList.size() == 0) {
                        getBusinessListFromWebServer();
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
                        if (inboxAdapter.getUpdatedList().size() == 0 | inboxAdapter.getUpdatedList() == null) {
                            //do nothing
                        } else {
                            for (int i = 0; i < inboxAdapter.getUpdatedList().size(); i++) {
                                if (inboxAdapter.getUpdatedList().get(i).getIsFavorite() == 1) {
                                    //count =i+1;
                                    //onListItemSelect(count);
                                } else {
                                    count = i;
                                    onListItemSelect(count);
                                    break;
                                }
                            }
                        }
                        if (inboxAdapter.getSelectedCount() == 0) {
                            CustomToast.showToast(EnquirySentListDealerActivity.this, "Please unfavourite your chat to delete");
                        } else {
                            //do nothing
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
    }

    private void openPopUpdialog() {
        Fdetails.clear();
        Fdetails1.clear();
        //businessTypeList.clear();
        businessTypes.clear();
        ConvertedFromDate = "";
        ConvertedToDate = "";

        Dialog mDialog = new Dialog(EnquirySentListDealerActivity.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.filter_inbox);
        mDialog.setCancelable(true);
        lv = mDialog.findViewById(R.id.listView1);
        lv.setOnItemClickListener(this);
        lv2 = mDialog.findViewById(R.id.listView2);
        DateFilterLayout = mDialog.findViewById(R.id.DateLayout);
        SelectFromDate = mDialog.findViewById(R.id.FromdateSelect);
        SelectToDate = mDialog.findViewById(R.id.TodateSelect);
        SelectFromDate.setOnClickListener(this);
        SelectToDate.setOnClickListener(this);
        //SelectToDate.setEnabled(false);
        ApplyFilter = mDialog.findViewById(R.id.Apply);
        for (int i = 0; i < 2; i++) {
            Fdetails.add(new Fdlist(mainFlr[i]));

        }


        ArrayAdapter<Fdlist> adapter = new MyAdapter();
        lv.setAdapter(adapter);

        if (!SelectFromDate.getText().toString().isEmpty()) {
            SelectFromDate.setText(SelectedFromDate);
            SelectToDate.setText(SelectedToDate);
        }
        if (businessTypeList.size() != 0) {
            businessTypeAdapter = new FdChild(EnquirySentListDealerActivity.this, businessTypeList);
            lv2.setAdapter(businessTypeAdapter);
            businessTypeAdapter.notifyDataSetChanged();
        }
        mDialog.show();
        ApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (ConnectivityReceiver.isConnected()) {
                        if (businessTypeAdapter.getTrueResultList().isEmpty() | businessTypeAdapter.getTrueResultList() == null &&
                                TextUtils.isEmpty(SelectFromDate.getText().toString())) {
                            Toast.makeText(EnquirySentListDealerActivity.this, "Please select appropriate data", Toast.LENGTH_SHORT).show();
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
                                        //to get the checked business type
                                        businessTypeList = businessTypeAdapter.getResultList();
                                        ApplyFilterMethod(businessTypes, QueryID, ConvertedFromDate, ConvertedToDate);
                                        mDialog.dismiss();
                                    }
                                }
                            } else {
                                businessTypes = businessTypeAdapter.getTrueResultList();
                                //to get the checked business type
                                businessTypeList = businessTypeAdapter.getResultList();
                                ApplyFilterMethod(businessTypes, QueryID, ConvertedFromDate, ConvertedToDate);
                                mDialog.dismiss();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "You are not connected to Internet.!", Toast.LENGTH_SHORT).show();
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


    private void ApplyFilterMethod(List<BusinessType> businessTypes, int queryID, String fromDate, String toDate) {
        ProgressDialog progressDialog = ShowProgressDialog.customProgressDialog(EnquirySentListDealerActivity.this, "Please wait");
        //        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please wait..."); // Setting Message
//        progressDialog.setTitle("Applying Filter"); // Setting Title
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        SearchProductDealer productDealer = new SearchProductDealer(businessTypes, queryID, CustID, fromDate, toDate, 0);
        Gson gson = new Gson();
        String json = gson.toJson(productDealer);
        Log.e("json.......", json);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<InboxDealer>> call = customer_interface.GetSentEnquiryFilterData("bearer " + Token, productDealer);
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
                            inboxAdapter = new InboxAdapter(EnquirySentListDealerActivity.this, dealers, EnquirySentListDealerActivity.this, EnquirySentListDealerActivity.this);
                            recyclerView.setAdapter(inboxAdapter);
                            inboxAdapter.notifyDataSetChanged();
                            //DeleteBtn.setEnabled(true);

                            tv_Search_Count.setVisibility(View.VISIBLE);
                            tv_Search_Count.setText("Out of " + Total_List_Count + " found " + Filter_Count);
                            Txt_NotFound.setVisibility(View.GONE);
                        } else {
                            // DeleteBtn.setEnabled(false);
                            recyclerView.setVisibility(View.GONE);
                            tv_Search_Count.setVisibility(View.GONE);
                            Txt_NotFound.setVisibility(View.VISIBLE);
                        }
                        break;

                    case 404:
                        progressDialog.dismiss();
                        recyclerView.setVisibility(View.GONE);
                        //DeleteBtn.setEnabled(false);
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(EnquirySentListDealerActivity.this, "Server Error");
                        // DeleteBtn.setEnabled(false);
                        break;

                    case 401:
                        progressDialog.dismiss();
                        SessionDialog.CallSessionTimeOutDialog(EnquirySentListDealerActivity.this);
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<InboxDealer>> call, Throwable t) {
                progressDialog.dismiss();
                // DeleteBtn.setEnabled(false);
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(EnquirySentListDealerActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EnquirySentListDealerActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });
    }

    //**********filter list view click listener
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == 0) {
            DateFilterLayout.setVisibility(View.GONE);
            lv2.setVisibility(View.VISIBLE);
            businessTypeAdapter = new FdChild(EnquirySentListDealerActivity.this, businessTypeList);
            lv2.setAdapter(businessTypeAdapter);
            businessTypeAdapter.notifyDataSetChanged();
        } else if (position == 1) {
            DateFilterLayout.setVisibility(View.VISIBLE);
            lv2.setVisibility(View.GONE);

            SelectFromDate.setText(SelectedFromDate);
            SelectToDate.setText(SelectedToDate);
        }
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

    @Override
    public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        try {
            inboxAdapter.getFilter().filter(s);
            recyclerView.getRecycledViewPool().clear();

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

    @Override
    public void afterTextChanged(Editable s) {
        try {
            inboxAdapter.getFilter().filter(s);
            recyclerView.getRecycledViewPool().clear();
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

    private class MyAdapter extends ArrayAdapter<Fdlist> {
        public MyAdapter() {
            super(EnquirySentListDealerActivity.this, R.layout.filter_items, Fdetails);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(
                        R.layout.filter_items, parent, false);
            }

            Fdlist nation = Fdetails.get(position);
            TextView name = itemView
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
                        lv2.setVisibility(View.VISIBLE);
                        businessTypeList = new ArrayList<BusinessType>(response.body());
                        businessTypeAdapter = new FdChild(EnquirySentListDealerActivity.this, businessTypeList);
                        lv2.setAdapter(businessTypeAdapter);
                        businessTypeAdapter.notifyDataSetChanged();
                        break;
                    case 404:

                        break;

                    case 500:
                        CustomToast.showToast(EnquirySentListDealerActivity.this, "Server Error");
                        break;

                    case 401:
                        SessionDialog.CallSessionTimeOutDialog(EnquirySentListDealerActivity.this);
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<BusinessType>> call, Throwable t) {
                Log.e("failure ", "error..." + t.getMessage());
                if (t instanceof IOException) {
                    Toast.makeText(EnquirySentListDealerActivity.this, "Time out..", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.showToast(EnquirySentListDealerActivity.this, "Bad response from server.. Try again later ");
                }
            }
        });

    }

    private void showError() {
        Snackbar snackbar = Snackbar
                .make(constraintLayout, "You are not connected to Internet.!", Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, EnquirySentListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        EnquirySentListDealerActivity.this.finish();
    }

}