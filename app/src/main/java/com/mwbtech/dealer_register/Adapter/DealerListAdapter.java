package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.PojoClass.SearchProductDealer;
import com.mwbtech.dealer_register.R;
import com.mwbtech.dealer_register.Utils.CustomToast;

import java.util.ArrayList;
import java.util.List;

public class DealerListAdapter extends RecyclerView.Adapter<DealerListAdapter.MyDealerViewHolder> implements Filterable {

    List<SearchProductDealer> itemCreationArrayList;
    List<SearchProductDealer> itemCreationArrayListFiltered;
    Context context;
    DealerAdapterListener listener;
    List<SearchProductDealer> selectedItemCreationArrayList;
    int custId;
    int cityId;
    AlertDialog dialog;
    String firmname, phoneNo, businessName;
    boolean isTrue;
    String VillageLocalityname;

    public DealerListAdapter(Context context, List<SearchProductDealer> itemCreationArrayList, DealerAdapterListener listener) {

        this.context = context;
        this.listener = listener;
        this.itemCreationArrayList = itemCreationArrayList;
        this.itemCreationArrayListFiltered = itemCreationArrayList;
        selectedItemCreationArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyDealerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context != null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dealer_list_adapter, parent, false);
            return new MyDealerViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyDealerViewHolder holder, int position) {
        if (context == null) {
            return;
        }
        try {
            holder.setIsRecyclable(false);
            final int pos = holder.getAdapterPosition();
            SearchProductDealer itemCreation = itemCreationArrayListFiltered.get(pos);
            holder.dealerName.setText(itemCreation.getCustomerName());
            holder.dealerCity.setText("" + itemCreation.getVillageLocalityname());
            holder.dealerBusiness.setText("" + itemCreation.getBusinessType());

            //important
            holder.checkBox.setChecked(itemCreationArrayListFiltered.get(pos).isChecked());
            holder.checkBox.setTag(itemCreationArrayListFiltered.get(pos));


            holder.checkBox.setOnClickListener(v -> {
                CheckBox cb = (CheckBox) v;
                SearchProductDealer dealerRegister = (SearchProductDealer) cb.getTag();

                dealerRegister.setChecked(cb.isChecked());
                itemCreationArrayListFiltered.get(pos).setChecked(cb.isChecked());
                listener.onDealerSelected(itemCreationArrayListFiltered.get(pos), cb.isChecked());

                //CustomToast.showToast(v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked());

            });

            holder.imgBtn.setTag(itemCreationArrayListFiltered.get(position));
            holder.imgBtn.setOnClickListener(v -> {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Do you want to delete " + itemCreationArrayListFiltered.get(pos).getCustomerName() + " ?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        CustomToast.showToast(v.getContext(),
                                "Firm Name : " + itemCreationArrayListFiltered.get(pos).
                                        getCustomerName() + " Deleted.");
                        itemCreationArrayListFiltered.remove(pos);

                        notifyDataSetChanged();
                    });

                    builder.setNegativeButton("No", (dialogInterface, i) -> dialog.dismiss());
                    dialog = builder.create();
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        setRowColor(holder.itemView, position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemCreationArrayListFiltered = itemCreationArrayList;
                } else {
                    ArrayList<SearchProductDealer> filteredList = new ArrayList<>();
                    for (SearchProductDealer row : itemCreationArrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCustomerName().toLowerCase().contains(charString.toLowerCase()) || row.getVillageLocalityname().toLowerCase().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    itemCreationArrayListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemCreationArrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemCreationArrayListFiltered = (ArrayList<SearchProductDealer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public List<SearchProductDealer> getResultDealerList() {
        selectedItemCreationArrayList.clear();
        for (int i = 0; i < itemCreationArrayListFiltered.size(); i++) {

            SearchProductDealer categoryProduct = itemCreationArrayListFiltered.get(i);
            if (categoryProduct.isChecked() == true) {
                custId = categoryProduct.getCustID();

                firmname = categoryProduct.getCustomerName();
                businessName = categoryProduct.getBusinessType();
                phoneNo = categoryProduct.getMobileNumber();
                cityId = categoryProduct.getCity();
                VillageLocalityname = categoryProduct.getVillageLocalityname();
                //searchId = categoryProduct.getSearchID();
                isTrue = categoryProduct.isChecked();
                selectedItemCreationArrayList.add(new SearchProductDealer(custId, firmname, businessName, phoneNo, cityId, VillageLocalityname, true));
            }
        }
        return selectedItemCreationArrayList;
    }


    public List<SearchProductDealer> getResultDealerAllList() {


        selectedItemCreationArrayList.clear();
        for (int i = 0; i < itemCreationArrayListFiltered.size(); i++) {

            SearchProductDealer categoryProduct = itemCreationArrayListFiltered.get(i);
            custId = categoryProduct.getCustID();

            firmname = categoryProduct.getCustomerName();
            businessName = categoryProduct.getBusinessType();
            phoneNo = categoryProduct.getMobileNumber();
            cityId = categoryProduct.getCity();
            //searchId = categoryProduct.getSearchID();
            VillageLocalityname = categoryProduct.getVillageLocalityname();
            isTrue = categoryProduct.isChecked();
            selectedItemCreationArrayList.add(new SearchProductDealer(custId, firmname, businessName, phoneNo, cityId, VillageLocalityname, true));

        }
        return selectedItemCreationArrayList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemCreationArrayList == null ? 0 : itemCreationArrayListFiltered.size();
    }

    private void setRowColor(View view, int var) {
        if (var % 2 == 0) view.setBackgroundResource(R.color.colorPrimarylightt);
        else view.setBackgroundResource(R.color.white);
    }

    public void refreshList() {
        notifyDataSetChanged();
    }

    public void updateList(List<SearchProductDealer> searchProductDealers) {
        this.itemCreationArrayList = searchProductDealers;
        this.itemCreationArrayListFiltered = searchProductDealers;
        notifyDataSetChanged();
    }

    public interface DealerAdapterListener {
        void onDealerSelected(SearchProductDealer itemCreation, boolean isChecked);
    }

    public class MyDealerViewHolder extends RecyclerView.ViewHolder {

        TextView dealerName, dealerCity, dealerBusiness, offerPrice;
        CheckBox checkBox;
        CardView cardView;
        LinearLayout linearLayout;
        ImageButton imgBtn;
        private View mEmptyView;

        public MyDealerViewHolder(@NonNull View itemView) {
            super(itemView);

            dealerName = itemView.findViewById(R.id.dealerName);
            dealerCity = itemView.findViewById(R.id.dealercity);
            dealerBusiness = itemView.findViewById(R.id.dealerBusiness);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            checkBox = itemView.findViewById(R.id.checkedDealer);
            cardView = itemView.findViewById(R.id.carview);
            imgBtn = itemView.findViewById(R.id.btn_delete_unit);
            itemView.setOnClickListener(view -> {
                // send selected contact in callback
                checkBox.performClick();
            });
        }
    }
}
