package com.mwbtech.dealer_register.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mwbtech.dealer_register.PojoClass.BusinessType;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class BusinessTypeSpinnerAdapter extends BaseAdapter {

    Activity activity;
    List<BusinessType> businessTypes;
    List<BusinessType> businessTypeList;
    LayoutInflater inflater;
    ViewHolder viewHolder;

    public BusinessTypeSpinnerAdapter(Activity activity, List<BusinessType> businessTypes) {

        this.activity =activity;
        this.businessTypes = businessTypes;
        businessTypeList = new ArrayList<>();
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
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.business_adapter1, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.item_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvItemName.setText(businessTypes.get(position).getNameOfBusiness());

        setRowColor(convertView,position);
        convertView.setTag(viewHolder);
        return convertView;
    }

    class ViewHolder {

        public TextView tvItemName;

    }

    private void setRowColor(View view, int var) {
//        if (var % 2 == 0 ) {
//            view.setBackgroundResource(R.color.bg);
//        } else {
            view.setBackgroundResource(R.color.white);
//        }
    }
    public List<BusinessType> getResultList()
    {
        return businessTypeList;
    }
}
