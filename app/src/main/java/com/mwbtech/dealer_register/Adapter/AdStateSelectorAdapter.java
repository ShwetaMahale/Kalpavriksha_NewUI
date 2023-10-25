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

import com.mwbtech.dealer_register.PojoClass.State;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;



public class AdStateSelectorAdapter extends RecyclerView.Adapter<AdStateSelectorAdapter.MyViewHolder> implements Filterable {


    public List<State> stateList = new ArrayList<>();
    public List<State> stateListFilter = new ArrayList<>();
    public Context context;
    List<State> stateSelectedList = new ArrayList<>();
    private final RecycleitemSelecet itemSelectedListener;



    public interface RecycleitemSelecet {

        void onItemSelected(State state) ;
    }

    public AdStateSelectorAdapter(Context context, List<State> stateList, RecycleitemSelecet recyclelistener) {
        this.context = context;
        this.stateList = stateList;
        this.stateListFilter = stateList;
        stateSelectedList = new ArrayList<>();
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
    public void onBindViewHolder(@NonNull AdStateSelectorAdapter.MyViewHolder holder, int position) {
        holder.checkBox.setText(stateListFilter.get(position).getStateName());
        holder.checkBox.setChecked(stateListFilter.get(position).isChecked());
        holder.checkBox.setTag(stateListFilter.get(position));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                State state = (State) cb.getTag();

                if (cb.isChecked())
                {
                    state.setChecked(cb.isChecked());
                    stateListFilter.get(position).setChecked(cb.isChecked());
                    itemSelectedListener.onItemSelected(state);
                }else
                {
                    state.setChecked(false);
                    stateListFilter.get(position).setChecked(false);
                    itemSelectedListener.onItemSelected(state);
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
                    stateListFilter = stateList;
                } else {
                    ArrayList<State> filteredList = new ArrayList<>();
                    for (State row : stateList) {
                        if (row.getStateName().toUpperCase().contains(charString.toUpperCase())) {
                            filteredList.add(row);
                        }
                    }
                    stateListFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = stateListFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                stateListFilter = (ArrayList<State>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return stateListFilter.size();
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

    public List<State> getResultList() {
        stateSelectedList.clear();
        for (int i = 0; i < stateListFilter.size(); i++) {
            State state = stateListFilter.get(i);
            if (state.isChecked() == true) {
                stateSelectedList.add(new State(state.getStateID(),state.getStateName(),state.isChecked()));
            }
        }
        return stateSelectedList;
    }


}
