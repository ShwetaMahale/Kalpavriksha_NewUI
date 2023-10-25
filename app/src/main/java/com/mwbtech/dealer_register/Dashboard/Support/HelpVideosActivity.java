package com.mwbtech.dealer_register.Dashboard.Support;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.APIInterface.Customer_Interface;
import com.mwbtech.dealer_register.Adapter.HelpVideoAdapter;
import com.mwbtech.dealer_register.PojoClass.VideoItems;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class HelpVideosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HelpVideoAdapter helpVideoAdapter;
    List<VideoItems> videoItems=new ArrayList<>();
    Customer_Interface customer_interface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_videos);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_color));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);

        //GetVideosList();
        
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }

   /* private void GetVideosList() {
        ProgressDialog progressDialog = ShowProgressDialog.createProgressDialog(this);
        customer_interface = Customer_Client.getClient().create(Customer_Interface.class);
        Call<List<VideoItems>> call = customer_interface.getVideos();
        call.enqueue(new Callback<List<VideoItems>>() {
            @Override
            public void onResponse(Call<List<VideoItems>> call, Response<List<VideoItems>> response) {
                int status_code = response.code();
                switch (status_code)
                {
                    case 200:
                        progressDialog.dismiss();
                        videoItems = response.body();
                        helpVideoAdapter = new HelpVideoAdapter(videoItems, HelpVideosActivity.this);
                        recyclerView.setAdapter(helpVideoAdapter);
                        break;

                    case 500:
                        progressDialog.dismiss();
                        CustomToast.showToast(HelpVideosActivity.this,"Server Error");
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<VideoItems>> call, Throwable t) {
                progressDialog.dismiss();
                CustomToast.showToast(HelpVideosActivity.this,"Connection Error");
            }
        });
    }*/

}