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

import com.mwbtech.dealer_register.PojoClass.State;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class StateSelectorAdapter extends RecyclerView.Adapter<StateSelectorAdapter.MyViewHolder> implements Filterable {

    StateAdapterListener stateAdapterListener;
    Context context;
    List<State> stateList;
    List<State> states;
    public StateSelectorAdapter(Context context, List<State> childCategoryProducts, StateAdapterListener childAdapterListener){
        this.context = context;
        this.stateList = childCategoryProducts;
        this.states = childCategoryProducts;
        this.stateAdapterListener = childAdapterListener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context != null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_category_adapters, parent, false);
            return new MyViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            final int pos = position;
            holder.tvItemName.setText("" + states.get(position).getStateName());

            setRowColor(holder.itemView, position);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void setRowColor(View view, int var) {
        if (var % 2 == 0 ) {
            view.setBackgroundResource(R.color.bg);
        } else {
            view.setBackgroundResource(R.color.white);
        }
    }
    @Override
    public int getItemCount() {
        return states ==null ? 0 : states.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    states = stateList;
                } else {
                    ArrayList<State> filteredList = new ArrayList<>();
                    for (State row : stateList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getStateName().toLowerCase().trim().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    states = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = states;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                states = (ArrayList<State>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvItemName,CatName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = (TextView) itemView.findViewById(R.id.item_name);
            CatName = (TextView) itemView.findViewById(R.id.catName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callbac

                    stateAdapterListener.onItemSelected(states.get(getAdapterPosition()));
                }
            });
        }
    }


    public interface StateAdapterListener {
        void onItemSelected(State state);
    }
}
