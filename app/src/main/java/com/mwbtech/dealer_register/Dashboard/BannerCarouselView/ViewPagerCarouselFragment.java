package com.mwbtech.dealer_register.Dashboard.BannerCarouselView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mwbtech.dealer_register.Dashboard.Advertisement.AdRequestDetails.AdRequestDetailsActivity;
import com.mwbtech.dealer_register.Dashboard.Advertisement.QuoteAd.AddRequestUserActivity;
import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.PojoClass.SlotBookImages;
import com.mwbtech.dealer_register.R;

import java.util.List;


public class ViewPagerCarouselFragment extends Fragment {
    public static final String IMAGE_RESOURCE_ID = "image_resource_id";

    private ImageView ivCarouselImage;
    private int imageResourceId;
    private String Url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_pager_carousel_fragment, container, false);
        ivCarouselImage = (ImageView) v.findViewById(R.id.iv_carousel_image);
        Url = getArguments().getString(IMAGE_RESOURCE_ID); // default to car1 image resource
       // imageResourceId = getArguments().getInt("ID");


        //AdDetailsModel adDetailsModel = (AdDetailsModel) getArguments().getSerializable("object");
        SlotBookImages adDetailsModel = (SlotBookImages) getArguments().getSerializable("object");
        List<BusinessType> test;
        //test=adDetailsModel.getBusinessTypes();
        //Picasso.get().load(Url).into(ivCarouselImage);
        Glide.with(this)
                .load(Url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivCarouselImage);
        /*v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "This is Company ad", Toast.LENGTH_LONG).show();
                Log.e("url",Url);
            }
        });*/

       v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(), "image: " + imageResourceId, Toast.LENGTH_SHORT).show();
                Log.e("data","............"+adDetailsModel.toString());
                if(adDetailsModel.getCompanyAd()==true){
                    if(adDetailsModel.getPromoAd()){
                        startActivity(new Intent(getActivity(), AddRequestUserActivity.class));
                    }
                    else{
                        Toast.makeText(getContext(), "This is Company ad", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Intent intent = new Intent(getActivity(),AdRequestDetailsActivity.class)
                            .putExtra("CustID",adDetailsModel.getCustID())
                            .putExtra("promoImage",adDetailsModel.getImageUrl())
                            .putExtra("Product",adDetailsModel.getProductName())
                            .putExtra("FirmName",adDetailsModel.getCustomerInfo().getFirmName())
                            .putExtra("state",adDetailsModel.getCustomerInfo().getStateName())
                            .putExtra("city",adDetailsModel.getCustomerInfo().getCityName())
                            .putExtra("cityid",adDetailsModel.getCustomerInfo().getCityID())
                            .putExtra("customerid",adDetailsModel.getCustID())
                            .putExtra("productid",adDetailsModel.getProductID())
                            .putExtra("admodel", adDetailsModel);
                    //.putExtra("Btypes",test)   .putExtra("btype", (Parcelable) test)
                    //Log.e("data","............"+adDetailsModel.getImageURL());
                    startActivity(intent);
                }

            }
        });
        return v;
    }
}
