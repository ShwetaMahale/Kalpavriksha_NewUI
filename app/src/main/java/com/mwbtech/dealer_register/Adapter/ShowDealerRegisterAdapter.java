package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.PojoClass.DealerRegister;
import com.mwbtech.dealer_register.R;

import java.util.ArrayList;
import java.util.List;

public class ShowDealerRegisterAdapter extends RecyclerView.Adapter<ShowDealerRegisterAdapter.MyViewHolder> implements Filterable {
    List<DealerRegister> itemCreationArrayList;
    List<DealerRegister> itemCreationArrayListFiltered;
    Context context;
    ItemCreationAdapterListener listener;
    private SparseBooleanArray mSelectedItemsIds;
    public ShowDealerRegisterAdapter(Context context, List<DealerRegister> itemCreationArrayList, ItemCreationAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.itemCreationArrayList = itemCreationArrayList;
        this.itemCreationArrayListFiltered = itemCreationArrayList;
        mSelectedItemsIds = new SparseBooleanArray();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context != null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dealer_list, parent, false);
            return new MyViewHolder(itemView, context);
        }
        return null;
    }

    public void setOnclickListerner(ItemCreationAdapterListener listerner){

    }

    public void updateData(List<DealerRegister> viewModels) {
        itemCreationArrayListFiltered.clear();
        itemCreationArrayListFiltered.addAll(viewModels);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        if (context == null) {
            return;
        }
        try {
            DealerRegister itemCreation = itemCreationArrayListFiltered.get(position);
            Log.i("*********",itemCreation.toString());
            holder.itemName.setText(itemCreation.getFirmName());
            holder.itemQuantity.setText(itemCreation.getMobileNumber());
            //setAnimation(holder.linearLayout, position);
            holder.linearLayout
                    .setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4
                            : Color.TRANSPARENT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public SparseBooleanArray toggleSelectionAll(){
        for (int i=0;i< itemCreationArrayListFiltered.size();i++){
            if(i == 0) {
                selectView1(i, mSelectedItemsIds.get(i));
            }else {
                selectView1(i, !mSelectedItemsIds.get(i));
            }
        }
        return mSelectedItemsIds;
    }


    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }
    public void selectView1(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.put(position, value);

        notifyDataSetChanged();
    }

    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    private void setAnimation(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        viewToAnimate.startAnimation(animation);
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
                    ArrayList<DealerRegister> filteredList = new ArrayList<>();
                    for (DealerRegister row : itemCreationArrayList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFirmName().toLowerCase().contains(charString.toLowerCase()) || row.getMobileNumber().contains(charSequence)) {
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
                itemCreationArrayListFiltered = (ArrayList<DealerRegister>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return itemCreationArrayList == null ? 0 :itemCreationArrayListFiltered.size();
    }


    public List<DealerRegister> getFilterList(){

        return itemCreationArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView itemName,itemQuantity,itemPrice,offerPrice;
        ImageView imageView;
        CardView cardView;
        LinearLayout linearLayout;
        private View mEmptyView;
        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            //imageView = (ImageView)itemView.findViewById(R.id.imageData);
            itemName = (TextView)itemView.findViewById(R.id.itemName);
            itemQuantity = (TextView)itemView.findViewById(R.id.ItemQuantity);
            itemPrice = (TextView)itemView.findViewById(R.id.ItemPrice);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.linearLayout);
            cardView = (CardView)itemView.findViewById(R.id.carview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callbac

                    listener.onItemSelected(itemCreationArrayListFiltered.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface ItemCreationAdapterListener {
        void onItemSelected(DealerRegister itemCreation);
    }
}
