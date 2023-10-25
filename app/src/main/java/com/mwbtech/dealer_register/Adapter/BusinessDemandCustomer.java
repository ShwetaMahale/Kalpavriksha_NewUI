package com.mwbtech.dealer_register.Adapter;

import static com.mwbtech.dealer_register.Dashboard.DashboardActivity.creation;
import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.prefManager;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;

import android.app.Activity;
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
import com.mwbtech.dealer_register.PojoClass.BusinessDemand;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class BusinessDemandCustomer extends BaseAdapter {

    Activity activity;
    List<BusinessDemand> FDlist;
    List<BusinessDemand> FDlist1;
    private int selectedPosition = -1;
    List<BusinessDemand> businessDemandslist = new ArrayList<>();
    List<BusinessDemand> businessTypeTrueList;
    List<BusinessDemand> businessTypeList;
    int BusinessDemandID = 0;
    String BusinessDemandName;
    BusinessDemandclick click;

    public BusinessDemandCustomer(Activity activity, List<BusinessDemand> fdlist1s) {
        this.activity = activity;
        this.FDlist = fdlist1s;
        businessTypeList = new ArrayList<>();
        this.FDlist1 = fdlist1s;
    }

    @Override
    public int getCount() {
        return FDlist.size();
    }

    @Override
    public Object getItem(int position) {
        return FDlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.business_adapter, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.ckId);
            holder.textView = convertView.findViewById(R.id.item_name);
            holder.imgBusiness = convertView.findViewById(R.id.img_business);
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }


//        holder.checkBox.setChecked(FDlist.get(position).isChecked());
        holder.textView.setTag(position); // This line is important.

       /* if(FDlist.get(position).isChecked()) {
            holder.checkBox.setChecked(FDlist.get(position).isChecked());
            // FDlist.get(position).setChecked(false);
        }
        else {
            holder.checkBox.setChecked(position == selectedPosition);
        }*/
        if(FDlist.get(position).isChecked()){
            holder.root.setBackgroundResource(R.drawable.shape_rectangle_red);
            Glide.with(activity)
                    .load(FDlist.get(position).getSelectedImage())
//                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .disallowHardwareConfig()
                    .into(holder.imgBusiness);
            /*Picasso.get()
                    .load(businessTypes.get(position).getSelectedImage())
                    .into(viewHolder.imgBusiness);*/
            holder.textView.setTextColor(activity.getResources().getColor(R.color.white));
            holder.textView.setTypeface(null, Typeface.BOLD);
        } else {
            holder.root.setBackgroundResource(0);
            Glide.with(activity)
                    .load(FDlist.get(position).getUnSelectedImage())
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                    .into(holder.imgBusiness);
            /*Picasso.get()
                    .load(businessTypes.get(position).getSelectedImage())
                    .into(viewHolder.imgBusiness);*/
            holder.textView.setTextColor(activity.getResources().getColor(R.color.black));
            holder.textView.setTypeface(null, Typeface.NORMAL);
        }
        //holder.checkBox.setChecked(FDlist.get(position).isChecked());
        holder.textView.setTag(FDlist.get(position));
        holder.textView.setText(FDlist.get(position).getBusinessDemand());
        holder.textView.setOnClickListener(v -> {
            if (FDlist.get(position).isChecked()) {
                // checkBox.setChecked(position == selectedPosition);
                selectedPosition = position;
                BusinessDemandName = FDlist.get(position).getBusinessDemand();
                BusinessDemandID = FDlist.get(position).getBusinessDemandID();
                //checkBox.setChecked(FDlist.get(position).isChecked());
                businessDemandslist.add(new BusinessDemand(BusinessDemandName, BusinessDemandID, FDlist.get(position).isChecked(), Integer.parseInt(prefManager.getCustId().get(CUST_ID))));
                Log.e("model...",businessDemandslist.toString());
                Log.e("Business","ID....."+BusinessDemandID);
                if(creation!=null) {
                    creation.setBusinessDemandID(BusinessDemandID);
                    creation.setBusinessDemandWithCust(businessDemandslist);
                }
                Log.e("id...",String.valueOf(BusinessDemandID));

            } else {
                try {
                    BusinessDemandID = 0;
                    creation.setBusinessDemandID(BusinessDemandID);
                    selectedPosition = -1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < FDlist.size(); i++) {
                FDlist.get(i).setChecked(i == position);
            }
            notifyDataSetChanged();
        });

        convertView.setTag(R.id.textView, holder.textView);
        convertView.setTag(holder);
        return convertView;
    }

    /*public View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    // checkBox.setChecked(position == selectedPosition);
                    selectedPosition = position;
                    BusinessDemandName = FDlist.get(position).getBusinessDemand();
                    BusinessDemandID = FDlist.get(position).getBusinessDemandID();
                    //checkBox.setChecked(FDlist.get(position).isChecked());
                    businessDemandslist.add(new BusinessDemand(BusinessDemandName, BusinessDemandID, checkBox.isChecked(), Integer.parseInt(prefManager.getCustId().get(CUST_ID))));
                    Log.e("model...",businessDemandslist.toString());
                    Log.e("Business","ID....."+BusinessDemandID);
                    if(creation!=null) {
                        creation.setBusinessDemandID(BusinessDemandID);
                        creation.setBusinessDemandWithCust(businessDemandslist);
                    }
                    Log.e("id...",String.valueOf(BusinessDemandID));

                } else {
                    try {
                        BusinessDemandID = 0;
                        creation.setBusinessDemandID(BusinessDemandID);
                        selectedPosition = -1;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < FDlist.size(); i++) {
                    FDlist.get(i).setChecked(i == position);
                }
                notifyDataSetChanged();
            }
        };
    }*/

    class ViewHolder {

        private CheckBox checkBox;
        public TextView textView;
        public ImageView imgBusiness;
        public LinearLayout root;

        public ViewHolder(View v) {
            checkBox = (CheckBox) v.findViewById(R.id.ckId);
            textView = v.findViewById(R.id.item_name);
            imgBusiness = v.findViewById(R.id.img_business);
            root = v.findViewById(R.id.root);
        }
    }


    public int GetselectedBusinessDemand() {
        return BusinessDemandID;
    }

    public List<BusinessDemand> getTrueResultList() {
//        businessTypeTrueList.clear();
        for (int i = 0; i < FDlist.size(); i++) {
            if (FDlist.get(i).isChecked()) {
                businessTypeTrueList.add(new BusinessDemand(BusinessDemandName, BusinessDemandID, FDlist.get(i).isChecked(), Integer.parseInt(prefManager.getCustId().get(CUST_ID))));
                if (creation != null) {
                    //creation.setBusinessDemandID(BusinessDemandID);
                    creation.setBusinessDemandWithCust(businessTypeTrueList);
                }
                Log.e("modeltrue...", businessTypeTrueList.toString());
            }
        }
        return businessTypeTrueList;
    }

    public List<BusinessDemand> getResultList() {
        businessTypeList.clear();
        for (int i = 0; i < FDlist.size(); i++) {
            if (FDlist.get(i).isChecked()) {
                businessTypeList.add(new BusinessDemand(FDlist.get(i).getBusinessDemand(), FDlist.get(i).getBusinessDemandID(), FDlist.get(i).isChecked(), Integer.parseInt(prefManager.getCustId().get(CUST_ID))));
                Log.e("model...", businessTypeList.toString());
                if (creation != null) {
                    //creation.setBusinessDemandID(BusinessDemandID);
                    creation.setBusinessDemandWithCust(businessTypeList);
                }
            }


        }
        return businessTypeList;
    }

    public interface BusinessDemandclick {
        void TypeOfBusinessClick(int pos, List<BusinessDemand> types);
    }


}