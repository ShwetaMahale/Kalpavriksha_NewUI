package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.PojoClass.City;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> implements Filterable {


    public List<City> cityList = new ArrayList<>();
    public List<City> cityListFilter = new ArrayList<>();
    public Context context;
    private int selectedCityCount = 0;

    private final CitySelectedListener itemSelectedListener;


    public interface CitySelectedListener {

        void onCitySelected(City city);
    }

    public CityAdapter(Context context, List<City> stateList,CitySelectedListener itemSelectedListener) {
        this.context = context;
        this.cityList = stateList;
        this.cityListFilter = stateList;
        this.itemSelectedListener = itemSelectedListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context != null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item_list, parent, false);
            return new MyViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvStateName.setText(cityListFilter.get(position).getVillageLocalityname());
        Log.e("list","3..........."+cityListFilter.size());

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    cityListFilter = cityList;
                    Toast.makeText(context,"No city found", Toast.LENGTH_LONG).show();
                } else {
                    ArrayList<City> filteredList = new ArrayList<>();

                    for (City row : cityList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getVillageLocalityname().toUpperCase().contains(charString.toUpperCase())) {
                            filteredList.add(row);
                        }
                    }
                    if(filteredList.isEmpty()) {
                        Toast.makeText(context,"No city found", Toast.LENGTH_LONG).show();
                    }
                    else
                        cityListFilter = filteredList;
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

    @Override
    public int getItemCount() {
        return cityListFilter.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvStateName;

        public MyViewHolder(@NonNull View itemLayoutView) {
            super(itemLayoutView);
            tvStateName = itemLayoutView.findViewById(R.id.city_name);
            if(cityListFilter != null && getAdapterPosition() != -1 && cityListFilter.get(getAdapterPosition()).isChecked()){
                itemLayoutView.setBackgroundResource(R.color.bg_selected_item);
            } else {
                itemLayoutView.setBackgroundResource(R.color.white);
            }
            itemLayoutView.setOnClickListener(view -> {
                City city = cityListFilter.get(getAdapterPosition());
                if(city.isChecked()){
                    selectedCityCount--;
                    cityListFilter.get(getAdapterPosition()).setChecked(false);
                    itemLayoutView.setBackgroundResource(R.color.white);
                } else {
//                    if(selectedCityCount<4) {
                        selectedCityCount++;
                        itemLayoutView.setBackgroundResource(R.color.bg_selected_item);
                        cityListFilter.get(getAdapterPosition()).setChecked(true);
//                    }
                }
                itemSelectedListener.onCitySelected(cityListFilter.get(getAdapterPosition()));
            });
        }
    }

}
