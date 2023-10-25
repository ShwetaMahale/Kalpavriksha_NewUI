package com.mwbtech.dealer_register.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.mwbtech.dealer_register.PojoClass.BusinessDemand;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class FilterBusinessDemandAdapter extends BaseAdapter {

    Activity activity;
    List<BusinessDemand> BusinessList;
    LayoutInflater inflater;
    ViewHolder viewHolder;
    List<BusinessDemand> businessDemandTypeList;
    List<BusinessDemand> businessDemandTypeTrueList;

    public FilterBusinessDemandAdapter(Activity activity, List<BusinessDemand> demandList) {
        this.activity =activity;
        this.BusinessList = demandList;
        businessDemandTypeList = new ArrayList<>();
        businessDemandTypeTrueList = new ArrayList<>();
        Log.e("list","count..."+BusinessList.size());
    }


    @Override
    public int getCount() {
        return BusinessList.size();
    }

    @Override
    public Object getItem(int position) {
        return BusinessList.get(position);
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
            convertView = inflater.inflate(R.layout.filter_childitems, parent, false);
            viewHolder = new ViewHolder();
            //viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.item_name);
            viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.Checkbox2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.checkBox.setText(BusinessList.get(position).getBusinessDemand());
        viewHolder.checkBox.setChecked(BusinessList.get(position).isChecked());
        viewHolder.checkBox.setTag(BusinessList.get(position));
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                BusinessDemand contact = (BusinessDemand) cb.getTag();
                contact.setChecked(cb.isChecked());
                BusinessList.get(pos).setChecked(cb.isChecked());

                //CustomToast.showToast(v.getContext(), "Clicked on Checkbox: " +businessTypes.get(pos).getNameOfBusiness() + cb.getText() + " is " + cb.isChecked());
            }
        });



        convertView.setTag(R.id.ckId, viewHolder.checkBox);
        //convertView.setTag(viewHolder);

        convertView.setTag(viewHolder);
        return convertView;
    }


    class ViewHolder {

        public CheckBox checkBox;

    }

    public List<BusinessDemand> GetSelectedDemand()
    {
        businessDemandTypeTrueList.clear();
        for(int i = 0; i< BusinessList.size(); i++){
            if(BusinessList.get(i).isChecked() == true) {
                businessDemandTypeTrueList.add(new BusinessDemand(BusinessList.get(i).getBusinessDemandID()));
            }
        }
        Log.e("Selected","check box...."+businessDemandTypeTrueList.size());
        return businessDemandTypeTrueList;
    }


    public List<BusinessDemand> GetResultDemand() {

        businessDemandTypeList.clear();
        for (int i = 0; i < BusinessList.size(); i++) {
            if (BusinessList.get(i).isChecked() == true) {
                businessDemandTypeList.add(new BusinessDemand(BusinessList.get(i).getBusinessDemand(),BusinessList.get(i).getBusinessDemandID(),true));
            }else
            {
                businessDemandTypeList.add(new BusinessDemand(BusinessList.get(i).getBusinessDemand(),BusinessList.get(i).getBusinessDemandID(),false));
            }
        }

        return businessDemandTypeList;
    }
}
