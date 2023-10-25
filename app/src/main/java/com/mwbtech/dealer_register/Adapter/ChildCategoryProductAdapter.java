package com.mwbtech.dealer_register.Adapter;

import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.prefManager;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;

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

import com.mwbtech.dealer_register.PojoClass.ChildCategoryProduct;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.SharedPreferences.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class ChildCategoryProductAdapter extends RecyclerView.Adapter<ChildCategoryProductAdapter.MyViewHolder> implements Filterable {

    public List<ChildCategoryProduct> childCategoryProductList;
    public List<ChildCategoryProduct> childCategoryProductListFilter;
    public Context context;
    List<ChildCategoryProduct> childcategoriesSelectedList;
    public ChildCategoryProductAdapter(Context context, List<ChildCategoryProduct> childCategoryProductList){
        this.context = context;
        this.childCategoryProductList = childCategoryProductList;
        this.childCategoryProductListFilter = childCategoryProductList;
        childcategoriesSelectedList = new ArrayList<>();
        prefManager = new PrefManager(context);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context != null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_product_adapter, parent, false);
            return new MyViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final int pos = position;

        holder.tvItemName.setText(""+ childCategoryProductListFilter.get(position).getChildCategoryName());

        //important
        holder.checkBox.setChecked(childCategoryProductListFilter.get(position).isChecked());
//        holder.checkBox.setTag(childCategoryProductListFilter.get(position));
        holder.tvItemName.setTag(childCategoryProductListFilter.get(position));

        holder.itemView.setOnClickListener(v -> {
            ChildCategoryProduct contact = (ChildCategoryProduct) holder.tvItemName.getTag();

            contact.setChecked(true);
            childCategoryProductListFilter.get(pos).setChecked(true);

        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                ChildCategoryProduct contact = (ChildCategoryProduct) cb.getTag();

                contact.setChecked(cb.isChecked());
                childCategoryProductListFilter.get(pos).setChecked(cb.isChecked());


            }
        });
        setRowColor(holder.itemView,position);
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    childCategoryProductListFilter = childCategoryProductList;
                } else {
                    ArrayList<ChildCategoryProduct> filteredList = new ArrayList<>();
                    for (ChildCategoryProduct row : childCategoryProductList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getChildCategoryName().toUpperCase().contains(charString.toUpperCase())) {
                            filteredList.add(row);
                        }else if(row.isChecked() == true) {
                            filteredList.add(row);
                        }
                    }
                    childCategoryProductListFilter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = childCategoryProductListFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                childCategoryProductListFilter = (ArrayList<ChildCategoryProduct>) filterResults.values;
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
    public List<ChildCategoryProduct> getResultList()
    {
        childcategoriesSelectedList.clear();
        for(int i = 0; i< childCategoryProductListFilter.size(); i++){
            ChildCategoryProduct mainCategoryProduct = childCategoryProductListFilter.get(i);
            if(mainCategoryProduct.isChecked() == true){
                childcategoriesSelectedList.add(new ChildCategoryProduct(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),childCategoryProductListFilter.get(i).getSubCategoryProductId(),childCategoryProductListFilter.get(i).getChildCategoryId(), childCategoryProductListFilter.get(i).getChildCategoryName(), true));
            }
        }
        return childcategoriesSelectedList;
    }


    @Override
    public int getItemCount() {
        return childCategoryProductListFilter.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvItemName;
        public CheckBox checkBox;

        public MyViewHolder(@NonNull View itemLayoutView) {
            super(itemLayoutView);

            tvItemName = (TextView) itemLayoutView.findViewById(R.id.item_name);
            checkBox = (CheckBox) itemLayoutView.findViewById(R.id.ckId);
        }
    }

}
