package com.mwbtech.dealer_register.Adapter;

import static com.mwbtech.dealer_register.Dashboard.DashboardActivity.creation;

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

public class BusinessDemandAdaper extends BaseAdapter {

    Activity activity;
    List<BusinessDemand> FDlist;
    private int selectedPosition = -1;
    List<BusinessDemand> businessDemandslist=new ArrayList<>();
    int BusinessDemandID=0;
    String BusinessDemandName;
    public BusinessDemandAdaper(Activity activity, List<BusinessDemand> fdlist1s) {
        this.activity = activity;
        this.FDlist = fdlist1s;
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
            convertView = inflater.inflate(R.layout.business_adapter_new, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.itemName.setTag(position); // This line is important.
        holder.itemName.setText(FDlist.get(position).getBusinessDemand());

//        holder.checkBox.setText(FDlist.get(position).getBusinessDemand());
        //holder.checkBox.setChecked(position == selectedPosition);
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
            holder.itemName.setTextColor(activity.getResources().getColor(R.color.white));
            holder.itemName.setTypeface(null, Typeface.BOLD);
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
            holder.itemName.setTextColor(activity.getResources().getColor(R.color.black));
            holder.itemName.setTypeface(null, Typeface.NORMAL);
        }
        /*if(FDlist.get(position).isChecked()) {
            holder.checkBox.setChecked(FDlist.get(position).isChecked());
            // FDlist.get(position).setChecked(false);
        }
        else {
            holder.checkBox.setChecked(position == selectedPosition);
        }*/

        holder.root.setOnClickListener(v -> {
            if (!FDlist.get(position).isChecked()) {
                // checkBox.setChecked(position == selectedPosition);
                selectedPosition = position;
                BusinessDemandName = FDlist.get(position).getBusinessDemand();
                BusinessDemandID = FDlist.get(position).getBusinessDemandID();
                //checkBox.setChecked(FDlist.get(position).isChecked());
                businessDemandslist.add(new BusinessDemand(BusinessDemandName,BusinessDemandID));
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
//        holder.checkBox.setOnClickListener(onStateChangedListener(holder.checkBox, position));

        return convertView;
    }

    /*public View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedPosition = position;

                    BusinessDemandName = FDlist.get(position).getBusinessDemand();
                    BusinessDemandID = FDlist.get(position).getBusinessDemandID();
                    businessDemandslist.add(new BusinessDemand(BusinessDemandName,BusinessDemandID));
                    Log.e("Business","ID....."+BusinessDemandID);
                } else {
                    BusinessDemandID=0;
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
            }
        };
    }*/

    private static class ViewHolder {
        private final TextView itemName;
        private final LinearLayout root;
        private final ImageView imgBusiness;
        private final CheckBox checkBox;

        public ViewHolder(View v) {
            checkBox = (CheckBox) v.findViewById(R.id.ckId);
            root = (LinearLayout) v.findViewById(R.id.root);
            imgBusiness = (ImageView) v.findViewById(R.id.img_business);
            itemName = (TextView) v.findViewById(R.id.item_name);
        }
    }

    public int GetselectedBusinessDemand()
    {
        return BusinessDemandID;
    }

}
