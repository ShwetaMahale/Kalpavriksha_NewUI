package com.mwbtech.dealer_register.Adapter;

import static com.mwbtech.dealer_register.Profile.ProfileMain.MainActivity.prefManager;
import static com.mwbtech.dealer_register.SharedPreferences.PrefManager.CUST_ID;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.PojoClass.SubCategoryProduct;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> implements Filterable {


    public List<SubCategoryProduct> childCategoryProductList = new ArrayList<>();
    public List<SubCategoryProduct> childCategoryProductListFilter = new ArrayList<>();
    public Context context;
    List<SubCategoryProduct> childcategoriesSelectedList = new ArrayList<>();
    private RecycleitemSelecet itemSelectedListener;



    public interface RecycleitemSelecet {

        void onItemSelected(SubCategoryProduct subProduct) ;
    }

    public SubCategoryAdapter(Context context, List<SubCategoryProduct> childCategoryProductList, RecycleitemSelecet recyclelistener) {
        this.context = context;
        if(childCategoryProductList!=null){
            for(SubCategoryProduct categoryProduct : childCategoryProductList){
                categoryProduct.setChecked(false);
                this.childCategoryProductList.add(categoryProduct);
            }
        }
        this.childCategoryProductListFilter = this.childCategoryProductList;
        childcategoriesSelectedList = new ArrayList<>();
        itemSelectedListener = recyclelistener;
    }

    public SubCategoryAdapter(Context context, List<SubCategoryProduct> childCategoryProductList) {
        this.context = context;
        this.childCategoryProductList = childCategoryProductList;
        this.childCategoryProductListFilter = childCategoryProductList;
        childcategoriesSelectedList = new ArrayList<>();

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

        holder.tvItemName.setText("" + childCategoryProductListFilter.get(position).getSubCategoryName());
        holder.tvCatname.setText("in"+" "+childCategoryProductListFilter.get(position).getCatname());
        //important
        holder.checkBox.setChecked(childCategoryProductListFilter.get(position).isChecked());
        holder.checkBox.setTag(childCategoryProductListFilter.get(position));



        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                SubCategoryProduct contact = (SubCategoryProduct) cb.getTag();

                if (cb.isChecked())
                {
                    holder.itemView.setBackgroundResource(R.color.bg_selected_item);
                    Log.e("Checked","..."+childCategoryProductListFilter.get(pos).getSubCategoryName());
                    contact.setChecked(cb.isChecked());
                    childCategoryProductListFilter.get(pos).setChecked(cb.isChecked());
                    itemSelectedListener.onItemSelected(contact);
                } else {
                    holder.itemView.setBackgroundResource(R.color.white);
                    Log.e("Checked","1..."+childCategoryProductListFilter.get(pos).getSubCategoryName());
                    contact.setChecked(false);
                    childCategoryProductListFilter.get(pos).setChecked(false);
                    itemSelectedListener.onItemSelected(contact);
                }


                //CustomToast.showToast(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked());
            }
        });
        holder.layout.setOnClickListener(v -> {
            holder.checkBox.performClick();
        });
        setRowColor(holder.itemView, position);
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
                    ArrayList<SubCategoryProduct> filteredList = new ArrayList<>();
                    for (SubCategoryProduct row : childCategoryProductList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getSubCategoryName().toUpperCase().contains(charString.toUpperCase())) {
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
                childCategoryProductListFilter = (ArrayList<SubCategoryProduct>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return childCategoryProductListFilter.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName,tvCatname;
        public CheckBox checkBox;
        public RelativeLayout layout;

        public MyViewHolder(@NonNull View itemLayoutView) {
            super(itemLayoutView);

            tvItemName = (TextView) itemLayoutView.findViewById(R.id.item_name);
            layout = itemLayoutView.findViewById(R.id.layout);
            checkBox = (CheckBox) itemLayoutView.findViewById(R.id.ckId);
            tvCatname = itemLayoutView.findViewById(R.id.catName);
        }
    }

    private void setRowColor(View view, int var) {
//        if (var % 2 == 0) {
//            view.setBackgroundResource(R.color.bg);
//        } else {
            view.setBackgroundResource(R.color.white);
//        }
    }

    public List<SubCategoryProduct> getResultList() {

        childcategoriesSelectedList.clear();
        for (int i = 0; i < childCategoryProductListFilter.size(); i++) {
            SubCategoryProduct mainCategoryProduct = childCategoryProductListFilter.get(i);
            if (mainCategoryProduct.isChecked() == true) {
                childcategoriesSelectedList.add(new SubCategoryProduct(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),childCategoryProductListFilter.get(i).getCategoryProductId(),childCategoryProductListFilter.get(i).getSubCategoryId(), childCategoryProductListFilter.get(i).getSubCategoryName(), true));

            }
        }

        return childcategoriesSelectedList;
    }


    public List<SubCategoryProduct> getResultAllList() {

        childcategoriesSelectedList.clear();
        for (int i = 0; i < childCategoryProductListFilter.size(); i++) {
            childcategoriesSelectedList.add(new SubCategoryProduct(Integer.parseInt(prefManager.getCustId().get(CUST_ID)),childCategoryProductListFilter.get(i).getCategoryProductId(),childCategoryProductListFilter.get(i).getSubCategoryId(), childCategoryProductListFilter.get(i).getSubCategoryName(), true));

        }

        return childcategoriesSelectedList;
    }


    public List<SubCategoryProduct> getResultList1() {



        return childcategoriesSelectedList;
    }


}
