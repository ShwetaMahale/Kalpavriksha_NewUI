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

import com.mwbtech.dealer_register.PojoClass.Brand;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class AdBrandSelectorAdapter extends RecyclerView.Adapter<AdBrandSelectorAdapter.MyViewHolder> implements Filterable {
    BrandAdapterListener childAdapterListener;
    Context context;
    List<Brand> brandList;
    List<Brand> childCategoryProductList;

    public AdBrandSelectorAdapter(Context context, List<Brand> childCategoryProducts, BrandAdapterListener childAdapterListener){
        this.context = context;
        this.brandList = childCategoryProducts;
        this.childCategoryProductList = childCategoryProducts;
        this.childAdapterListener = childAdapterListener;

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
            holder.tvItemName.setText("" + childCategoryProductList.get(position).getBrandName());

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
        return childCategoryProductList==null ? 0 :childCategoryProductList.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                ArrayList<Brand> filteredList = new ArrayList<>();
                if (charString.isEmpty()) {
                    childCategoryProductList = brandList;
                } else {

                    for (Brand row : brandList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getBrandName().toLowerCase().trim().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    childCategoryProductList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                if (filteredList.size()==0) {

                    filteredList.add(new Brand(72,"Create New.."));
                    filterResults.values = childCategoryProductList;
                }else {
                    filterResults.values = childCategoryProductList;
                    //filteredList.add(new Brand(0,"Create New.."));
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                childCategoryProductList = (ArrayList<Brand>) filterResults.values;
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
                    // send selected contact in callback
                    childAdapterListener.onItemSelected(childCategoryProductList.get(getAdapterPosition()));
                }
            });
        }
    }
    public interface BrandAdapterListener {
        void onItemSelected(Brand childCreation);
    }
}
