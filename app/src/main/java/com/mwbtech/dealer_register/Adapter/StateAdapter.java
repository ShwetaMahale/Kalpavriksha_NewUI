package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mwbtech.dealer_register.PojoClass.State;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class StateAdapter extends BaseAdapter implements Filterable {


    Context context;
    List<State> stateList;
    List<State> stateListFilter;
    LayoutInflater inflater;
    ViewHolder viewHolder;
    public StateAdapter(Context context, List<State> stateList){
        this.context = context;
        this.stateList = stateList;
        this.stateListFilter = stateList;
    }


    @Override
    public int getCount() {
        return stateListFilter.size();
    }

    @Override
    public Object getItem(int position) {
        return stateListFilter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.state_adapter, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.item_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setRowColor(convertView,position);
        viewHolder.tvItemName.setText(stateListFilter.get(position).getStateName());
        return convertView;
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

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getStateName().toLowerCase().contains(charString.toLowerCase())) {
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
    private void setRowColor(View view, int var) {
        if (var % 2 == 0 ) {
            view.setBackgroundResource(R.color.bg);
        } else {
            view.setBackgroundResource(R.color.white);
        }
    }
    class ViewHolder {

        public TextView tvItemName;

    }
}

