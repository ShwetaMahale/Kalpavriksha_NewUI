package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.PojoClass.City;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.MyViewHolder> implements Filterable {


    public List<City> cityList = new ArrayList<>();
    public List<City> cityListFilter = new ArrayList<>();
    public Context context;

    private RecycleitemSelecet itemSelectedListener;


    public interface RecycleitemSelecet {

        void onItemSelected(City city);
    }

    public ExampleAdapter(Context context, List<City> stateList) {
        this.context = context;
        this.cityList = stateList;
        this.cityListFilter = stateList;


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
                    ArrayList<City> filteredList = new ArrayList<>();
                    for (City row : cityList) {
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
            tvStateName = (TextView) itemLayoutView.findViewById(R.id.city_name);


        }
    }

}
