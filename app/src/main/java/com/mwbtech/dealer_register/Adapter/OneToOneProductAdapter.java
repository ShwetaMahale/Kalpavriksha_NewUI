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

import com.mwbtech.dealer_register.PojoClass.ChildCategoryProduct;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class OneToOneProductAdapter extends RecyclerView.Adapter<OneToOneProductAdapter.MyViewHolder> implements Filterable {

    ChildAdapterListener childAdapterListener;
    Context context;
    List<ChildCategoryProduct> childCategoryProducts;
    List<ChildCategoryProduct> childCategoryProductList;
    public OneToOneProductAdapter(Context context, List<ChildCategoryProduct> childCategoryProducts, ChildAdapterListener childAdapterListener){
        this.context = context;
        this.childCategoryProducts = childCategoryProducts;
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
            //holder.tvItemName.setText("" + childCategoryProductList.get(position).getItemName());
            holder.tvItemName.setText("" + childCategoryProductList.get(position).getChildCategoryName());
            holder.CatName.setText("in"+" "+childCategoryProductList.get(position).getMainCategoryName());

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
                if (charString.isEmpty()) {
                    childCategoryProductList = childCategoryProducts;
                } else {
                    ArrayList<ChildCategoryProduct> filteredList = new ArrayList<>();
                    for (ChildCategoryProduct row : childCategoryProducts) {
                        if (row.getChildCategoryName().toLowerCase().trim().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    childCategoryProductList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = childCategoryProductList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                childCategoryProductList = (ArrayList<ChildCategoryProduct>) filterResults.values;
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

                    childAdapterListener.onItemSelected(childCategoryProductList.get(getAdapterPosition()));
                }
            });
        }
    }


    public interface ChildAdapterListener {
        void onItemSelected(ChildCategoryProduct childCreation);
    }
}
