package com.mwbtech.dealer_register.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.mwbtech.dealer_register.PojoClass.City;
import com.mwbtech.dealer_register.PojoClass.CityAD;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class FilterCityAdapter extends BaseAdapter implements Filterable {

    Activity activity;
    List<City> CityList;
    LayoutInflater inflater;
    ViewHolder viewHolder;
    List<City> CitySelectedList;
    List<City> cityListFilter;
    List<City> CityTrueList;

    public FilterCityAdapter(Activity activity, List<City> cityList) {
        this.activity =activity;
        this.CityList = cityList;
        this.cityListFilter = cityList;
        CitySelectedList = new ArrayList<>();
        CityTrueList = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return cityListFilter.size();
    }

    @Override
    public Object getItem(int position) {
        return cityListFilter.get(position);
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
            viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.Checkbox2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CityTrueList.clear();
        viewHolder.checkBox.setText(cityListFilter.get(position).getVillageLocalityname());
        viewHolder.checkBox.setChecked(cityListFilter.get(position).isChecked());
        viewHolder.checkBox.setTag(cityListFilter.get(position));
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                City contact = (City) cb.getTag();
                contact.setChecked(cb.isChecked());
                cityListFilter.get(pos).setChecked(cb.isChecked());
            }
        });
        convertView.setTag(R.id.ckId, viewHolder.checkBox);
        convertView.setTag(viewHolder);
        return convertView;
    }

    class ViewHolder {
        public CheckBox checkBox;
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    cityListFilter = CityList;
                } else {
                    ArrayList<City> filteredList = new ArrayList<>();
                    for (City row : CityList) {
                        if (row.getVillageLocalityname().toUpperCase().contains(charString.toUpperCase())) {
                            filteredList.add(row);
                        }
                    }
                    if(filteredList.isEmpty()) {
                        Toast.makeText(activity,"No city found", Toast.LENGTH_LONG).show();
                    }
                    else cityListFilter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = cityListFilter;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cityListFilter = (ArrayList<City>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<City> GetSelectedCity() {
        try{
            CityTrueList.clear();
            for (int i = 0; i < cityListFilter.size(); i++) {
                if (cityListFilter.get(i).isChecked() == true) {
                    CityTrueList.add(new City(cityListFilter.get(i).getStatewithCityID()));
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
        for(int i = 0; i< cityListFilter.size(); i++){
            if(cityListFilter.get(i).isChecked() == true) {
                CitySelectedList.add(new City(cityListFilter.get(i).getStatewithCityID(),cityListFilter.get(i).getVillageLocalityname(),true));
            }else
            {
                CitySelectedList.add(new City(cityListFilter.get(i).getStatewithCityID(),cityListFilter.get(i).getVillageLocalityname(),false));
            }
        }

        return CitySelectedList;
    }
}
