package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.mwbtech.dealer_register.PojoClass.ChildCategoryProduct;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class ContactChildCatAdapter extends BaseAdapter implements Filterable {

    Context context;
    List<ChildCategoryProduct> childCategoryProducts;
    List<ChildCategoryProduct> childCategoryProductsFilter;
    LayoutInflater inflater;
    ViewHolder viewHolder;
    public ContactChildCatAdapter(Context context, List<ChildCategoryProduct> categoryProducts){
        this.context = context;
        this.childCategoryProducts = categoryProducts;
        this.childCategoryProductsFilter = categoryProducts;
    }


    @Override
    public int getCount() {
        return childCategoryProductsFilter.size();
    }

    @Override
    public Object getItem(int position) {
        return childCategoryProductsFilter.get(position);
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
        viewHolder.tvItemName.setText(childCategoryProductsFilter.get(position).getChildCategoryName());
        return convertView;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    childCategoryProductsFilter = childCategoryProducts;
                } else {
                    ArrayList<ChildCategoryProduct> filteredList = new ArrayList<>();
                    for (ChildCategoryProduct row : childCategoryProducts) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getChildCategoryName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    childCategoryProductsFilter = filteredList;
                }



                filterResults.values = childCategoryProductsFilter;

                if (filterResults.count==0)
                {
                    childCategoryProductsFilter.add(new ChildCategoryProduct("Other"));
                    filterResults.values = childCategoryProductsFilter;
                }else
                {
                    filterResults.values = childCategoryProductsFilter;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                childCategoryProductsFilter = (ArrayList<ChildCategoryProduct>) filterResults.values;
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