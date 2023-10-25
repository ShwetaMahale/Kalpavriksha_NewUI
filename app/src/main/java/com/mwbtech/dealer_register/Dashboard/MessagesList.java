package com.mwbtech.dealer_register.Dashboard;


import static com.mwbtech.dealer_register.Dashboard.NotificationsTabs.MainNotificationsActivity.prefManager;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.TOKEN;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.SubCategoryAdapter;
import com.mwbtech.dealer_register.PojoClass.SubCategoryProduct;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.Utils.FileCompressor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MessagesList extends Fragment {
    View messagesListView;
    int pos = 1, CustID = 0;
    String Token;
    SubCategoryAdapter subCategoryAdapter;
    List<SubCategoryProduct> selectSubIdList = new ArrayList<>();
    Customer_Interface customer_interface;
    ImageView imgFilter;
    RecyclerView rvMessagesList;
    EditText txtSearch;
    int imgCount = 0;
    //Image
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    static final long image_size = 2097152;
    File mPhotoFile1, mPhotoFile2;
    FileCompressor mCompressor;
    String imgBase64 = "";
    public boolean isSaved = false;
    int DemandID;

    /*
        @Override
        public void onAttach(Context activity) {
            super.onAttach(activity);
            try {
                if (activity instanceof CallToBillFragment) {
                    callToBillFragment = (CallToBillFragment) activity;
                } else {
                    throw new RuntimeException(activity.toString()
                            + " must implement OnGreenFragmentListener");
                }
            } catch (ClassCastException e) {
            }
        }*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        messagesListView = inflater.inflate(R.layout.messages_list, null);
        // GetBusinessDemand();
        // CustID = Integer.parseInt(prefManager.getCustId().get(CUST_ID));
        Token = prefManager.getToken().get(TOKEN);
        txtSearch = messagesListView.findViewById(R.id.txt_search);
        //spLedger = customerView.findViewById(R.id.spinnerLedger);
        imgFilter = messagesListView.findViewById(R.id.img_filter);
        imgFilter.setOnClickListener(v -> {

        });

        rvMessagesList = messagesListView.findViewById(R.id.rv_messages_list);
        rvMessagesList.setHasFixedSize(true);
        rvMessagesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCompressor = new FileCompressor(getContext());
        sharePreferencesMethod();
        return messagesListView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void sharePreferencesMethod() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("navigation", "OnPause calleddddd");
        // savedata();
    }
}
