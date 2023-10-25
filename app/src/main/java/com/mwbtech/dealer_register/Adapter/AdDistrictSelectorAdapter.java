package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.PojoClass.District;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class AdDistrictSelectorAdapter extends RecyclerView.Adapter<AdDistrictSelectorAdapter.MyViewHolder> implements Filterable {


    public List<District> districtList = new ArrayList<>();
    public List<District> districtListFilter = new ArrayList<>();
    public Context context;
    List<District> selectedDistrictList;
    private final RecycleitemSelecet itemSelectedListener;

    public interface RecycleitemSelecet {

        void onItemSelected(District district) ;
    }

    public AdDistrictSelectorAdapter(Context context, List<District> stateList, RecycleitemSelecet recyclelistener) {
        this.context = context;
        this.districtList = stateList;
        this.districtListFilter = stateList;
        selectedDistrictList = new ArrayList<>();
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

        final int pos = position;

        holder.checkBox.setText(districtListFilter.get(pos).getDistrictName());
        holder.checkBox.setChecked(districtListFilter.get(position).isChecked());
        holder.checkBox.setTag(districtListFilter.get(position));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                District district = (District) cb.getTag();
                if (cb.isChecked())
                {
                    Log.e("Checked","..."+districtListFilter.get(pos).getDistrictName());
                    district.setChecked(cb.isChecked());
                    districtListFilter.get(pos).setChecked(cb.isChecked());
                    itemSelectedListener.onItemSelected(district);
                }else
                {
                    Log.e("Checked","1..."+districtListFilter.get(pos).getDistrictName());
                    district.setChecked(false);
                    districtListFilter.get(pos).setChecked(false);
                    itemSelectedListener.onItemSelected(district);
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
                    districtListFilter = districtList;
                } else {
                    ArrayList<District> filteredList = new ArrayList<>();
                    for (District row : districtList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getDistrictName().toUpperCase().contains(charString.toUpperCase())) {
                            filteredList.add(row);
                        }
                    }

                    districtListFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = districtListFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                districtListFilter = (ArrayList<District>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return districtListFilter.size();
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
    public List<District> getResultList() {
        selectedDistrictList.clear();
        for (int i = 0; i < districtListFilter.size(); i++) {
            District  district = districtListFilter.get(i);
            if (district.isChecked() == true) {
                selectedDistrictList.add(new District(district.getDistrictID(),district.getDistrictName(),district.isChecked()));
            }
        }
        return selectedDistrictList;
    }
}
