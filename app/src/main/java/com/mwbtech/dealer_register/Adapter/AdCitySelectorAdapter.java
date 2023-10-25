package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.PojoClass.CityAD;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class AdCitySelectorAdapter extends RecyclerView.Adapter<AdCitySelectorAdapter.MyViewHolder> implements Filterable {

    public List<CityAD> cityList = new ArrayList<>();
    public List<CityAD> cityListFilter = new ArrayList<>();
    public Context context;
    List<CityAD> citySelectedList = new ArrayList<>();
    List<CityAD> finalcitySelectedList = new ArrayList<>();
    private final RecycleitemSelecet itemSelectedListener;

    public interface RecycleitemSelecet {

        void onItemSelected(CityAD city);
    }

    public AdCitySelectorAdapter(Context context, List<CityAD> stateList, RecycleitemSelecet recyclelistener) {
        this.context = context;
        this.cityList = stateList;
        this.cityListFilter = stateList;
        citySelectedList = new ArrayList<>();
        itemSelectedListener = recyclelistener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context != null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_item_list, parent, false);
            return new MyViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.checkBox.setText(cityListFilter.get(position).getVillageLocalityname());
        holder.checkBox.setChecked(cityListFilter.get(position).isChecked());
        holder.checkBox.setTag(cityListFilter.get(position));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                CityAD city = (CityAD) cb.getTag();

                if (cb.isChecked()) {
                    city.setChecked(cb.isChecked());
                    cityListFilter.get(position).setChecked(cb.isChecked());
                    itemSelectedListener.onItemSelected(city);

                } else {
                    city.setChecked(false);
                    cityListFilter.get(position).setChecked(false);
                    itemSelectedListener.onItemSelected(city);
                }
            }
        });
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    cityListFilter = cityList;
                } else {
                    ArrayList<CityAD> filteredList = new ArrayList<>();
                    for (CityAD row : cityList) {
                        if (row.getVillageLocalityname().toUpperCase().contains(charString.toUpperCase())) {
                            filteredList.add(row);
                        }
                    }
                    cityListFilter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = cityListFilter;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cityListFilter = (ArrayList<CityAD>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public int getItemCount() {
        return cityListFilter.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvStateName;
        public CheckBox checkBox;
        public MyViewHolder(@NonNull View itemLayoutView) {
            super(itemLayoutView);
            tvStateName = (TextView) itemLayoutView.findViewById(R.id.state_name);
            checkBox = (CheckBox) itemLayoutView.findViewById(R.id.ckId);
        }
    }
    public List<CityAD> getResultList() {
        citySelectedList.clear();
        for (int i = 0; i < cityListFilter.size(); i++) {
            CityAD city = cityListFilter.get(i);
            if (city.isChecked() == true) {
                citySelectedList.add(new CityAD(city.getStatewithCityID(),city.getVillageLocalityname(),city.isChecked()));
            }

        }
        finalcitySelectedList=citySelectedList;
        return finalcitySelectedList;
    }
}
