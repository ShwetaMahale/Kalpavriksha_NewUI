package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.PojoClass.CustomerModel;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;


public class CustomerSelectorAdapter extends RecyclerView.Adapter<CustomerSelectorAdapter.MyViewHolder> implements Filterable {


    public List<CustomerModel> customerList = new ArrayList<>();
    public List<CustomerModel> customerListFilter = new ArrayList<>();
    public List<CustomerModel> FinalCustomerList = new ArrayList<>();
    public Context context;
    List<CustomerModel> customerSelectedList = new ArrayList<>();
    private CustomerItemSelecet itemSelectedListener;


    public interface CustomerItemSelecet {

        void onItemSelected(CustomerModel subProduct);
    }

    public CustomerSelectorAdapter(Context context, List<CustomerModel> childCategoryProductList, CustomerItemSelecet recyclelistener) {
        this.context = context;
        this.customerList = childCategoryProductList;
        this.customerListFilter = childCategoryProductList;
        customerSelectedList = new ArrayList<>();
        itemSelectedListener = recyclelistener;
    }

    public CustomerSelectorAdapter(Context context, List<CustomerModel> customerModels) {
        this.context = context;
        this.customerList = customerModels;
        this.customerListFilter = customerModels;
        customerSelectedList = new ArrayList<>();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context != null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_dialog_layout, parent, false);
            return new MyViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int pos = position;
        holder.checkBox.setText("" + customerListFilter.get(position).getFirmName());
        //important
        holder.checkBox.setChecked(customerListFilter.get(position).isChecked());
        holder.checkBox.setTag(customerListFilter.get(position));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                CustomerModel contact = (CustomerModel) cb.getTag();
                if (cb.isChecked()) {
                    FinalCustomerList.clear();
                    Log.e("Checked", "..." + customerListFilter.get(pos).getFirmName());
                    contact.setChecked(cb.isChecked());
                    customerListFilter.get(pos).setChecked(cb.isChecked());
                    for(CustomerModel Finalrow:customerListFilter)
                    {
                        FinalCustomerList.add(Finalrow);
                    }
                    itemSelectedListener.onItemSelected(contact);
                } else {
                    Log.e("Checked", "1..." + customerListFilter.get(pos).getFirmName());
                    contact.setChecked(false);
                    customerListFilter.get(pos).setChecked(false);
                    for(CustomerModel Finalrow:customerListFilter)
                    {
                        FinalCustomerList.add(Finalrow);
                    }
                    itemSelectedListener.onItemSelected(contact);
                }
                //CustomToast.showToast(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked());
            }
        });
      //  setRowColor(holder.itemView, position);
    }

    public void selectAll() {
        for (int i = 0; i < customerListFilter.size(); i++) {
            customerListFilter.get(i).setChecked(true);
        }
        for(CustomerModel Finalrow:customerListFilter)
        {
            FinalCustomerList.add(Finalrow);
        }
        notifyDataSetChanged();
    }

    public void DeselectAll() {
        for (int i = 0; i < customerListFilter.size(); i++) {
            customerListFilter.get(i).setChecked(false);
        }
        for(CustomerModel Finalrow:customerListFilter)
        {
            FinalCustomerList.add(Finalrow);
        }
        //FinalCustomerList = customerListFilter;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                Log.e("Checking...",customerList.toString());
                if (charString.isEmpty()) {
                    customerListFilter = customerList;
                } else {
                    ArrayList<CustomerModel> filteredList = new ArrayList<>();
                    for (CustomerModel row : customerList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        Log.e("ListFilterTypes...","CID"+ row.getCustID() +" : "+row.getBusinessTypeForFilter());
                        if (row.getFirmName().toUpperCase().contains(charString.toUpperCase())||
                                row.getStateName().toUpperCase().contains(charString.toUpperCase())||
                                row.getBusinessTypeForFilter().toUpperCase().contains(charString.toUpperCase())) {
                            filteredList.add(row);
                        }
                    }
                    customerListFilter = filteredList;
                    for(CustomerModel Finalrow:customerListFilter)
                    {
                        FinalCustomerList.add(Finalrow);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = customerListFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                customerListFilter = (ArrayList<CustomerModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return customerListFilter==null? 0 :customerListFilter.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvItemName;
        public CheckBox checkBox;

        public MyViewHolder(@NonNull View itemLayoutView) {
            super(itemLayoutView);

            tvItemName = (TextView) itemLayoutView.findViewById(R.id.item_name);
            checkBox = (CheckBox) itemLayoutView.findViewById(R.id.ckId);
        }
    }

    private void setRowColor(View view, int var) {
        if (var % 2 == 0) {
            view.setBackgroundResource(R.color.bg);
        } else {
            view.setBackgroundResource(R.color.white);
        }
    }


    public List<CustomerModel> getResultList() {

        customerSelectedList.clear();
        for (int i = 0; i < FinalCustomerList.size(); i++) {
            CustomerModel customerModel = FinalCustomerList.get(i);
            if (customerModel.isChecked() == true) {
                customerSelectedList.add(new CustomerModel(customerModel.getCustID(),customerModel.getFirmName(), customerModel.getCityID()));
            }
        }
        return customerSelectedList;
    }
}