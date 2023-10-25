package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mwbtech.dealer_register.PojoClass.SubCategoryProduct;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class ContactSubCatAdapter extends BaseAdapter implements Filterable {

    Context context;
    List<SubCategoryProduct> subCategoryProducts;
    List<SubCategoryProduct> subCategoryProductsFilter;
    LayoutInflater inflater;
    ViewHolder viewHolder;
    public ContactSubCatAdapter(Context context, List<SubCategoryProduct> categoryProducts){
        this.context = context;
        this.subCategoryProducts = categoryProducts;
        this.subCategoryProductsFilter = categoryProducts;
    }


    @Override
    public int getCount() {
        return subCategoryProductsFilter==null ? 0 :subCategoryProductsFilter.size();
    }

    @Override
    public Object getItem(int position) {
        return subCategoryProductsFilter.get(position);
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
        viewHolder.tvItemName.setText(subCategoryProductsFilter.get(position).getSubCategoryName());
        return convertView;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                ArrayList<SubCategoryProduct> filteredList = new ArrayList<>();
                if (charString.isEmpty()) {
                    subCategoryProductsFilter = subCategoryProducts;
                } else {

                    for (SubCategoryProduct row : subCategoryProducts) {
                        if (row.getSubCategoryName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    subCategoryProductsFilter = filteredList;
                }


                FilterResults filterResults = new FilterResults();

                if (filterResults.count==0)
                {
                    subCategoryProductsFilter.add(new SubCategoryProduct("Other"));
                    filterResults.values = subCategoryProductsFilter;
                }else
                {
                    filterResults.values = subCategoryProductsFilter;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                subCategoryProductsFilter = (ArrayList<SubCategoryProduct>) filterResults.values;
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