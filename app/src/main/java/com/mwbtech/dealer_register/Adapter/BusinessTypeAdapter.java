package com.mwbtech.dealer_register.Adapter;

import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.prefManager;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class BusinessTypeAdapter extends BaseAdapter {

    Activity activity;
    List<BusinessType> businessTypes;
    List<BusinessType> businessTypeList;
    List<BusinessType> businessTypeTrueList;
    LayoutInflater inflater;
    ViewHolder viewHolder;
    BusinessTypeClick click;

    public BusinessTypeAdapter(Activity activity, List<BusinessType> businessTypes, BusinessTypeClick typeClick) {

        this.activity = activity;
        this.businessTypes = businessTypes;
        businessTypeList = new ArrayList<>();
        businessTypeTrueList = new ArrayList<>();
        this.click = typeClick;
        prefManager = new PrefManager(activity);
    }


    @Override
    public int getCount() {
        return businessTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return businessTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final int pos = position;
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.business_adapter, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = convertView.findViewById(R.id.ckId);
            viewHolder.textView = convertView.findViewById(R.id.item_name);
            viewHolder.imgBusiness = convertView.findViewById(R.id.img_business);
            viewHolder.root = convertView.findViewById(R.id.root);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Log.e("BTypelist----", businessTypes.toString());
        viewHolder.textView.setText(businessTypes.get(position).getNameOfBusiness());

//        viewHolder.checkBox.setChecked(businessTypes.get(position).isChecked());
//        viewHolder.checkBox.setTag(businessTypes.get(position));
        if (businessTypes.get(position).isChecked()) {
            viewHolder.root.setBackgroundResource(R.drawable.shape_rectangle_red);
            Glide.with(activity)
                    .load(businessTypes.get(position).getSelectedImage())
//                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .disallowHardwareConfig()
                    .into(viewHolder.imgBusiness);
            /*Picasso.get()
                    .load(businessTypes.get(position).getSelectedImage())
                    .into(viewHolder.imgBusiness);*/
            viewHolder.textView.setTextColor(activity.getResources().getColor(R.color.white));
            viewHolder.textView.setTypeface(null, Typeface.BOLD);
        } else {
            viewHolder.root.setBackgroundResource(0);
            Glide.with(activity)
                    .load(businessTypes.get(position).getUnSelectedImage())
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                    .into(viewHolder.imgBusiness);
            /*Picasso.get()
                    .load(businessTypes.get(position).getSelectedImage())
                    .into(viewHolder.imgBusiness);*/
            viewHolder.textView.setTextColor(activity.getResources().getColor(R.color.black));
            viewHolder.textView.setTypeface(null, Typeface.NORMAL);
        }
        viewHolder.textView.setTag(businessTypes.get(position));

        viewHolder.root.setOnClickListener(v -> {
            if(businessTypes.get(pos).getNameOfBusiness().equals("Professionals And Services")) {
                if (businessTypes.get(pos).isChecked()) {
                    for (int i=0; i < businessTypes.size(); i++) {
                        businessTypes.get(i).setChecked(businessTypes.get(i).getNameOfBusiness().equals("Professionals And Services"));
                    }
                }
                else {

                }
            }
            else {
                if (businessTypes.get(pos).isChecked()) {
                    businessTypes.get(pos).setChecked(false);
                    viewHolder.root.setBackgroundResource(0);
                    Glide.with(activity).load(businessTypes.get(pos).getUnSelectedImage())
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .skipMemoryCache(true)
                            .into(viewHolder.imgBusiness);
                    /*Picasso.get()
                            .load(businessTypes.get(position).getSelectedImage())
                            .into(viewHolder.imgBusiness);*/
                    viewHolder.textView.setTextColor(activity.getResources().getColor(R.color.black));
                    viewHolder.textView.setTypeface(null, Typeface.NORMAL);
                    for (int i = 0; i < businessTypes.size(); i++) {
                        if (businessTypes.get(i).getNameOfBusiness().equals("Professionals And Services")) {
                            businessTypes.get(i).setChecked(false);
                        }
                    }

                } else {
                    businessTypes.get(pos).setChecked(true);
                    viewHolder.root.setBackgroundResource(R.drawable.shape_rectangle_red);
                    Glide.with(activity)
                            .load(businessTypes.get(pos).getSelectedImage())
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .skipMemoryCache(true)
                            .into(viewHolder.imgBusiness);
                    /*Picasso.get()
                            .load(businessTypes.get(position).getSelectedImage())
                            .into(viewHolder.imgBusiness);*/
                    viewHolder.textView.setTextColor(activity.getResources().getColor(R.color.white));
                    viewHolder.textView.setTypeface(null, Typeface.BOLD);
                }
            }

            /*for(int i=0;i<businessTypes.size();i++){
                businessTypes.get(i).setChecked(i==pos);
            }*/
            notifyDataSetChanged();
            click.TypeOfBusinessClick(pos, businessTypes);
        });

        /*viewHolder.checkBox.setOnClickListener(v -> {
            businessTypes.get(pos).setChecked(!businessTypes.get(pos).isChecked());
            click.TypeOfBusinessClick(pos, businessTypes);
            if (businessTypes.get(pos).getNameOfBusiness().equals("Professionals And Services")) {
                if (businessTypes.get(pos).isChecked()) {
                    for (int i = 0; i < businessTypes.size(); i++) {
                        businessTypes.get(i).setChecked(businessTypes.get(i).getNameOfBusiness().equals("Professionals And Services"));
                    }
                }
            } else {
                if (businessTypes.get(pos).isChecked()) {
                    for (int i = 0; i < businessTypes.size(); i++) {
                        if (businessTypes.get(i).getNameOfBusiness().equals("Professionals And Services")) {
                            businessTypes.get(i).setChecked(false);
                        }
                    }
                }
            }

            for(int i=0;i<businessTypes.size();i++){
                businessTypes.get(i).setChecked(i==pos);
            }

            notifyDataSetChanged();
        });*/

        convertView.setTag(R.id.item_name, viewHolder.textView);

//        setRowColor(convertView, position);
        convertView.setTag(viewHolder);
        return convertView;
    }


    class ViewHolder {

        public CheckBox checkBox;
        public TextView textView;
        public ImageView imgBusiness;
        public LinearLayout root;

    }


    public List<BusinessType> getResultList() {
        businessTypeList.clear();
        for(int i = 0; i< businessTypes.size(); i++){
            businessTypeList.add(new BusinessType(businessTypes.get(i).getBusinessTypeID(), businessTypes.get(i).getNameOfBusiness(), Integer.parseInt(prefManager.getCustId().get(CUST_ID)),businessTypes.get(i).isChecked()));
        }
        return businessTypeList;
    }

    public List<BusinessType> getDefaultList() {
        businessTypeList.clear();
        for (int i = 0; i < businessTypes.size(); i++) {
            businessTypeList.add(new BusinessType(businessTypes.get(i).getBusinessTypeID(),
                    businessTypes.get(i).getNameOfBusiness(),
                    businessTypes.get(i).getSelectedImage(),
                    businessTypes.get(i).getUnSelectedImage(),
                    Integer.parseInt(prefManager.getCustId().get(CUST_ID)), false
            ));
        }
        return businessTypeList;
    }

    public void setDefaultList() {
        for (int i = 0; i < businessTypes.size(); i++) {
            businessTypes.get(i).setChecked(false);
        }
    }

    public List<BusinessType> getTrueResultList() {
        businessTypeTrueList.clear();
        for(int i = 0; i< businessTypes.size(); i++){
            if(businessTypes.get(i).isChecked()) {
                businessTypeTrueList.add(new BusinessType(businessTypes.get(i).getBusinessTypeID(), businessTypes.get(i).getNameOfBusiness(), Integer.parseInt(prefManager.getCustId().get(CUST_ID)), businessTypes.get(i).isChecked()));
            }
        }
        return businessTypeTrueList;
    }

    public void shouldSelectProfessional(boolean isSelected) {
        for (int i = 0; i < businessTypes.size(); i++) {
            if (businessTypes.get(i).getNameOfBusiness().equals("Professionals And Services")) {
                businessTypes.get(i).setChecked(isSelected);
            }
        }
    }


    public interface BusinessTypeClick {
        void TypeOfBusinessClick(int pos, List<BusinessType> types);
    }
}