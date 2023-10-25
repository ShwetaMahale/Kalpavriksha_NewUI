package com.mwbtech.dealer_register.Dashboard.BannerCarouselView;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.mwbtech.dealer_register.PojoClass.AdDetailsModel;
import com.mwbtech.dealer_register.PojoClass.SlotBookImages;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerCarouselView extends RelativeLayout {
    private FragmentManager fragmentManager;                // FragmentManager for managing the fragments withing the ViewPager
    //private ViewPager vpCarousel;                           // ViewPager for the Carousel view
    private ViewPager vpCarousel;                           // ViewPager for the Carousel view
    private ImageView imgPrevious, imgNext;
    private LinearLayout llPageIndicatorContainer;          // Carousel view item indicator, the little bullets at the bottom of the carousel
    private ArrayList<ImageView> carouselPageIndicators;    // Carousel view item, the little bullet at the bottom of the carousel
    private int [] imageResourceIds;                        // Carousel view background image
    //List<PromoImage> promoImages;
    List<SlotBookImages> promoImages;
    private long carouselSlideInterval;                     // Carousel view item sliding interval
    private Handler carouselHandler;                        // Carousel view item sliding interval automation handler
    private int lastPageIndex;
    private int mCurrentPosition = 0;
    private int mScrollState;
    List<AdDetailsModel> adDetailsModel;
    //List<PromoImage> PromoImageslist;
    List<SlotBookImages> PromoImageslist;
    public ViewPagerCarouselView (Context context) {
        super(context);
        initView(context);
    }

    public ViewPagerCarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ViewPagerCarouselView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_pager_carousel_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        vpCarousel = (ViewPager) this.findViewById(R.id.vp_carousel);
        imgPrevious = (ImageView) this.findViewById(R.id.img_previous_banner);
        imgNext = (ImageView) this.findViewById(R.id.img_next_banner);
        llPageIndicatorContainer = (LinearLayout) this.findViewById(R.id.ll_page_indicator_container);
    }


    /**
     * Set the data and initialize the carousel view
     * @param fragmentManager
     * @param //adDetailsModel
     * @param carouselSlideInterval
     */
//    public void setData(FragmentManager fragmentManager, int [] imageResourceIds, long carouselSlideInterval) {
//        this.fragmentManager = fragmentManager;
//        this.imageResourceIds = imageResourceIds;
//        this.carouselSlideInterval = carouselSlideInterval;
//        this.lastPageIndex = imageResourceIds.length - 1;
//        initData();
//        initCarousel();
//        initCarouselSlide();
//    }

    //promoimages
    public void setData(FragmentManager fragmentManager, List<SlotBookImages> promoImages, long carouselSlideInterval) {
        this.fragmentManager = fragmentManager;
        this.PromoImageslist = promoImages;
        this.carouselSlideInterval = carouselSlideInterval;
        this.lastPageIndex = promoImages.size() - 1;
        initData();
        initCarousel();
        initCarouselSlide();
    }


    /*public void setData(FragmentManager fragmentManager,  List<AdDetailsModel> adDetailsModel, long carouselSlideInterval) {
        this.fragmentManager = fragmentManager;
        this.adDetailsModel = adDetailsModel;
        this.carouselSlideInterval = carouselSlideInterval;
        this.lastPageIndex = adDetailsModel.size() - 1;
        initData();
        initCarousel();
        initCarouselSlide();
    }*/

    //for promo images
    private void initData() {
        carouselPageIndicators = new ArrayList<>();
        for (int i = 0; i < PromoImageslist.size(); i++) {
            ImageView obj = new ImageView(getContext());
            //obj.setImageResource(R.drawable.selector_carousel_page_indicator);
            //Picasso.get().load(PromoImageslist.get(i).getImageUrl()).into(obj);
           /* Glide.with(this)
                    .load(PromoImageslist.get(i).getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(obj);*/
          /*  Picasso.get().load(PromoImageslist.get(i).getImageURL()).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(obj);*/
            Log.e("promooooo..",(PromoImageslist.get(i).getImageUrl()));

            obj.setPadding(0, 0, 5, 0); // left, top, right, bottom
            if (i==0 || i==PromoImageslist.size()-1) obj.setVisibility(View.VISIBLE);
            llPageIndicatorContainer.addView(obj);
            carouselPageIndicators.add(obj);
        }
    }

    /**
     * Initialize the data for the carousel
     */
//   private void initData() {
//        carouselPageIndicators = new ArrayList<>();
//        for (int i = 0; i < imageResourceIds.length; i++) {
//            ImageView obj = new ImageView(getContext());
//            obj.setImageResource(R.drawable.selector_carousel_page_indicator);
//            obj.setPadding(0, 0, 5, 0); // left, top, right, bottom
//            if (i==0 || i==imageResourceIds.length-1) obj.setVisibility(View.INVISIBLE);
//            llPageIndicatorContainer.addView(obj);
//            carouselPageIndicators.add(obj);
//        }
    //}

    /*private void initData() {
        carouselPageIndicators = new ArrayList<>();
        for (int i = 0; i < adDetailsModel.size(); i++) {
            ImageView obj = new ImageView(getContext());
            //obj.setImageResource(R.drawable.selector_carousel_page_indicator);
            Picasso.get().load(adDetailsModel.get(i).getAdImageURL()).into(obj);
            obj.setPadding(0, 0, 5, 0); // left, top, right, bottom
            if (i==0 || i==adDetailsModel.size()-1) obj.setVisibility(View.VISIBLE);
            llPageIndicatorContainer.addView(obj);
            carouselPageIndicators.add(obj);
        }
    }*/

    /**
     * Initialize carousel views, each item in the carousel view is a fragment
     */
    private void initCarousel() {
        carouselPageIndicators.get(0).setSelected(true);
        //carouselPageIndicators.get(1).setSelected(true);

        imgPrevious.setOnClickListener(v -> {
            final int nCount = PromoImageslist.size();
            int curPos = vpCarousel.getCurrentItem();
            curPos--;
            if (curPos == 0) curPos = nCount;
            vpCarousel.setCurrentItem(curPos, true);
        });

        imgNext.setOnClickListener(v -> {
            final int nCount = PromoImageslist.size();
            int curPos = vpCarousel.getCurrentItem();
            curPos++;
            if (curPos == nCount) curPos = 0;
            vpCarousel.setCurrentItem(curPos, true);
        });

        // Update the carousel page indicator on change
        vpCarousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
                updateSlideIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // For going from the first item to the last item, Set the current item to the item before the last item if the current position is 0
                if (mCurrentPosition == 0)
                    vpCarousel.setCurrentItem(lastPageIndex - 1, false);

                // For going from the last item to the first item, Set the current item to the second item if the current position is on the last
                if (mCurrentPosition == lastPageIndex)      vpCarousel.setCurrentItem(1, false);
            }
        });

        // reset the slider when the ViewPager is scrolled manually to prevent the quick slide after it is scrolled.
        vpCarousel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d("resetAutoScroll", "  MotionEvent.ACTION_UP ");
                    initCarouselSlide();
                } else {
                    Log.d("resetAutoScroll", "  MotionEvent>" + event.getAction());
                    if (carouselHandler != null) {
                        carouselHandler.removeCallbacksAndMessages(null);
                        carouselHandler = null;
                    }
                }
                return false;
            }
        });

        ViewPagerCarouselAdapter viewPagerCarouselAdapter = new ViewPagerCarouselAdapter(fragmentManager, PromoImageslist);
        //ViewPagerCarouselAdapter viewPagerCarouselAdapter = new ViewPagerCarouselAdapter(fragmentManager, adDetailsModel);
        vpCarousel.setAdapter(viewPagerCarouselAdapter);
        vpCarousel.setCurrentItem(1);

    }

    /**
     * Handler to make the view pager to slide automatically
     */
    private void initCarouselSlide() {
        //final int nCount = imageResourceIds.length;
        final int nCount = PromoImageslist.size();
        //final int nCount = adDetailsModel.size();
        try {
            if (carouselHandler == null) carouselHandler = new Handler();
            carouselHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int curPos = vpCarousel.getCurrentItem();
                    curPos++;
                    if (curPos == nCount) curPos = 0;
                    vpCarousel.setCurrentItem(curPos, true);
                    carouselHandler.postDelayed(this, carouselSlideInterval);
                }
            }, carouselSlideInterval);

        } catch (Exception e) {
            Log.d("Carousel view", e.getMessage());
        }
    }

    private void updateSlideIndicator(int position) {
        for (int i = 0; i < carouselPageIndicators.size(); i++) {
            if (i == position) carouselPageIndicators.get(position).setSelected(true);
            else carouselPageIndicators.get(i).setSelected(false);
        }
    }

    public void onDestroy() {
        if (carouselHandler != null) carouselHandler.removeCallbacksAndMessages(null); // remove call backs to prevent memory leaks
    }
}