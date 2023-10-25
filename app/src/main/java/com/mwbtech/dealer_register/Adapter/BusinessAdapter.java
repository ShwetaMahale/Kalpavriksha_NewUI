package com.mwbtech.dealer_register.Adapter;

import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.prefManager;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class BusinessAdapter extends BaseAdapter {

    Activity activity;

    private int selectedPosition = -1;
    List<BusinessType> businessTypes;
    List<BusinessType> businessTypeList;
    List<BusinessType> businessTypeTrueList;
    String SelectedBusiness;

    public BusinessAdapter(Activity activity, List<BusinessType> businessTypes) {

        this.activity =activity;
        this.businessTypes = businessTypes;
        businessTypeList = new ArrayList<>();
        businessTypeTrueList = new ArrayList<>();
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

        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.business_adapter, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox.setTag(position); // This line is important.

        holder.friendName.setText(businessTypes.get(position).getNameOfBusiness());
        if (position == selectedPosition) {
            holder.checkBox.setChecked(true);
            if (SelectedBusiness.toLowerCase().equals("professionals and services"))
            {

                holder.checkBox.setEnabled(true);
            }else if (SelectedBusiness.toLowerCase().equals("retailer") | SelectedBusiness.toLowerCase().equals("wholesaler") | SelectedBusiness.toLowerCase().equals("distributor") |
                    SelectedBusiness.toLowerCase().equals("super stockist") | SelectedBusiness.toLowerCase().equals("manufacturer") )
            {
                holder.checkBox.setEnabled(false);
            }

        } else
        {
            //holder.checkBox.setEnabled(false);
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(onStateChangedListener(holder.checkBox, position));

        return convertView;
    }

    public View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedPosition = position;
                    businessTypes.get(position).setChecked(checkBox.isChecked());
                    SelectedBusiness = businessTypes.get(position).getNameOfBusiness();
                    Log.e("Business","checked....."+businessTypes.get(position).getNameOfBusiness());

                } else {
                    selectedPosition = -1;

                }
                notifyDataSetChanged();
            }
        };
    }

    private static class ViewHolder {
        private final TextView friendName;
        private final CheckBox checkBox;

        public ViewHolder(View v) {
            checkBox = (CheckBox) v.findViewById(R.id.ckId);
            friendName = (TextView) v.findViewById(R.id.item_name);
        }
    }

    public List<BusinessType> getTrueResultList()
    {
        businessTypeTrueList.clear();
        for(int i = 0; i< businessTypes.size(); i++){
            if(businessTypes.get(i).isChecked() == true) {
                businessTypeTrueList.add(new BusinessType(businessTypes.get(i).getBusinessTypeID(), businessTypes.get(i).getNameOfBusiness(), Integer.parseInt(prefManager.getCustId().get(CUST_ID)), businessTypes.get(i).isChecked()));
            }
        }
        return businessTypeTrueList;
    }

    public List<BusinessType> getResultList()
    {
        businessTypeList.clear();
        for(int i = 0; i< businessTypes.size(); i++){
            businessTypeList.add(new BusinessType(businessTypes.get(i).getBusinessTypeID(), businessTypes.get(i).getNameOfBusiness(), Integer.parseInt(prefManager.getCustId().get(CUST_ID)),businessTypes.get(i).isChecked()));
        }
        return businessTypeList;
    }

}
