package com.mwbtech.dealer_register.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.mwbtech.dealer_register.PojoClass.City;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class OneFilterCityAdapter extends BaseAdapter {

    Activity activity;
    List<City> CityList;
    LayoutInflater inflater;
    OneFilterCityAdapter.ViewHolder viewHolder;
    List<City> CitySelectedList;
    List<City> CityTrueList;
    private int selectedPosition = -1;

    public OneFilterCityAdapter(Activity activity, List<City> cityList) {
        this.activity =activity;
        this.CityList = cityList;
        CitySelectedList = new ArrayList<>();
        CityTrueList = new ArrayList<>();
        Log.e("list","count..."+cityList.size());

    }


    @Override
    public int getCount() {
        return CityList.size();
    }

    @Override
    public Object getItem(int position) {
        return CityList.get(position);
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
            convertView = inflater.inflate(R.layout.filter_cityitem, parent, false);
            viewHolder = new OneFilterCityAdapter.ViewHolder();
            //viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.item_name);
            viewHolder.chckBox = (CheckBox) convertView.findViewById(R.id.Checkbox2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OneFilterCityAdapter.ViewHolder) convertView.getTag();
        }

        CityTrueList.clear();
        viewHolder.chckBox.setText(CityList.get(position).getVillageLocalityname());
        viewHolder.chckBox.setChecked(CityList.get(position).isChecked());
        viewHolder.chckBox.setTag(CityList.get(position));
//        viewHolder.chckBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CheckBox cb = (CheckBox) v;
//                City contact = (City) cb.getTag();
//                contact.setChecked(cb.isChecked());
//                for (int i = 0; i < CityList.size(); i++) {
//                    CityList.get(i).setChecked(false);
//                    contact.setChecked(false);
//
//                }
//                CityList.get(pos).setChecked(cb.isChecked());
//            }
//        });
//        convertView.setTag(R.id.ckId, viewHolder.chckBox);
//        convertView.setTag(viewHolder);
//        return convertView;


        viewHolder.chckBox.setChecked(position == selectedPosition);

        viewHolder.chckBox.setOnClickListener(onStateChangedListener(viewHolder.chckBox, position));

        return convertView;
    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedPosition = position;
                } else {
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
            }
        };
    }
    class ViewHolder {
       // public RadioButton radioBtn;
        public CheckBox chckBox;
    }

    public List<City> GetSelectedCity() {
        try{
            CityTrueList.clear();
            for (int i = 0; i < CityList.size(); i++) {
                if (CityList.get(i).isChecked() == true) {
                    CityTrueList.add(new City(CityList.get(i).getStatewithCityID()));
                }
            }

        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
        return CityTrueList;
    }

    public List<City> GetResultCity()
    {
        CitySelectedList.clear();
        for(int i = 0; i< CityList.size(); i++){
            if(CityList.get(i).isChecked() == true) {
                CitySelectedList.add(new City(CityList.get(i).getStatewithCityID(),CityList.get(i).getVillageLocalityname(),true));
            }else
            {
                CitySelectedList.add(new City(CityList.get(i).getStatewithCityID(),CityList.get(i).getVillageLocalityname(),false));
            }
        }

        return CitySelectedList;
    }
}

