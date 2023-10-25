package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mwbtech.dealer_register.PojoClass.ChildCategoryProduct;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class ChildProductAdapter extends ArrayAdapter implements Filterable {

    public List<ChildCategoryProduct> childCategoryProductList;
    public List<ChildCategoryProduct> childCategoryProductListFilter;
    public Context context;
    private final int itemLayout;
    public ChildProductAdapter(Context context, int child_category_adapter, List<ChildCategoryProduct> childCategoryProductList){
        super(context,child_category_adapter, childCategoryProductList);
        this.context = context;
        this.childCategoryProductList = childCategoryProductList;
        this.itemLayout = child_category_adapter;
    }


    @Override
    public int getCount() {
        return childCategoryProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return childCategoryProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = (TextView) convertView.findViewById(R.id.item_name);
        strName.setText(childCategoryProductList.get(position).getChildCategoryName());
        setRowColor(convertView,position);
        return convertView;
    }



    @Override
    public Filter getFilter() {
        return new Filter() {

            private final Object lock = new Object();
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {


                FilterResults results = new FilterResults();
                if (childCategoryProductListFilter == null) {
                    synchronized (lock) {
                        childCategoryProductListFilter = new ArrayList<ChildCategoryProduct>(childCategoryProductList);
                    }
                }

                if (charSequence == null || charSequence.length() == 0) {
                    synchronized (lock) {
                        results.values = childCategoryProductListFilter;
                        results.count = childCategoryProductListFilter.size();
                    }
                } else {
                    final String searchStrLowerCase = charSequence.toString().toLowerCase();

                    ArrayList<ChildCategoryProduct> matchValues = new ArrayList<ChildCategoryProduct>();

                    for (ChildCategoryProduct dataItem : childCategoryProductListFilter) {
                        if (dataItem.getChildCategoryName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            matchValues.add(dataItem);
                        }
                    }

                    results.values = matchValues;
                    results.count = matchValues.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.values != null) {
                    childCategoryProductList = (ArrayList<ChildCategoryProduct>)filterResults.values;
                } else {
                    childCategoryProductList = null;
                }
                if (filterResults.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }


    class ViewHolder {

        public TextView tvChildItemName;

    }

    private void setRowColor(View view, int var) {
        if (var % 2 == 0 ) {
            view.setBackgroundResource(R.color.bg);
        } else {
            view.setBackgroundResource(R.color.white);
        }
    }

}
