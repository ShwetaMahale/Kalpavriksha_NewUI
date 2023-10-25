package com.mwbtech.dealer_register.Dashboard.BannerCarouselView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.mwbtech.dealer_register.PojoClass.SlotBookImages;

import java.util.List;
/**
 * This CitySelectedAdapter gets the individual fragment into the carousel view
 */
public class ViewPagerCarouselAdapter extends FragmentStatePagerAdapter {
   // private int[] imageResourceIds;
    private final List<SlotBookImages> promoImages;
   /* public ViewPagerCarouselAdapter(FragmentManager fm, int[] imageResourceIds) {
        super(fm);
        this.imageResourceIds = imageResourceIds;
    }*/

    public ViewPagerCarouselAdapter(FragmentManager fm, List<SlotBookImages> promoImages) {
        super(fm);
        this.promoImages = promoImages;
        //Log.e("sixe","........."+promoImages.size());
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(ViewPagerCarouselFragment.IMAGE_RESOURCE_ID, promoImages.get(position).getImageUrl());
        SlotBookImages adDetailsModel = promoImages.get(position);
        bundle.putSerializable("object", adDetailsModel);
        //bundle.put("object", (Serializable) adDetailsModel);
        //bundle.putInt("ID", promoImages.get(position).getAdvertisementMainID());
        ViewPagerCarouselFragment frag = new ViewPagerCarouselFragment();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public int getCount() {
        return (promoImages == null) ? 0: promoImages.size();
    }

}