package com.mwbtech.dealer_register.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mwbtech.dealer_register.PojoClass.OutboxDealer;
import com.mwbtech.dealer_register.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OutBoxAdapter extends RecyclerView.Adapter<OutBoxAdapter.MyViewHolder> implements Filterable {


    Context context;
    List<OutboxDealer> dealerRegisters;
    List<OutboxDealer> dealerRegisterList;
    ItemCreationAdapterListener listener;
    List<OutboxDealer> selectedItemCreationArrayList;
    String date, time;
    private SparseBooleanArray mSelectedItemsIds;
    private static int currentSelectedIndex = -1;
    MyViewHolder myViewHolder;

    public OutBoxAdapter(Context context, List<OutboxDealer> dealerRegisters, ItemCreationAdapterListener listener) {
        this.context = context;
        this.dealerRegisters = dealerRegisters;
        this.dealerRegisterList = dealerRegisters;
        this.listener = listener;
        selectedItemCreationArrayList = new ArrayList<>();
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (context != null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.outbox_list_item, parent, false);
            myViewHolder = new MyViewHolder(itemView);
            return new MyViewHolder(itemView, context);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OutboxDealer itemCreation = dealerRegisterList.get(position);
        if (!TextUtils.isEmpty(itemCreation.getLastUpdatedMsgDate())) {
            date = ConvertdateFormat(itemCreation.getLastUpdatedMsgDate().substring(0, 10));
            time = itemCreation.getLastUpdatedMsgDate().substring(11, 16);
        } else {
            date = "";
            time = "";
        }

        holder.ChildName.setText(itemCreation.getChildCategoryName());
        holder.ChatCity.setText("City : " + itemCreation.getVillageLocalityname());
        if (!TextUtils.isEmpty(itemCreation.getBusinessDemand())) {
            holder.BusinessDemandtxt.setText("Business Demand : " + itemCreation.getBusinessDemand());
        } else {
            holder.BusinessDemandtxt.setText("Request sent for : " + itemCreation.getRequirementName());
        }
        holder.ChatDateTime.setText("" + date + " " + time);

        if (!TextUtils.isEmpty(itemCreation.getEnquiryDate())) {
            String Enq_Date = Convertdate(itemCreation.getEnquiryDate().substring(0, 10));
            holder.EnquiryDate.setText("Enquiry Date : " + Enq_Date);
        }

        holder.ChatCount.setText(String.valueOf(itemCreation.getReplyCount()));
        if (itemCreation.getReplyCount() == 0) {
            holder.ChatCount.setVisibility(View.GONE);
        } else {
            holder.ChatCount.setVisibility(View.VISIBLE);

        }
        if(itemCreation.getEnquiryType()!=null)
        {
            holder.EnquiryType.setText("EnquiryType : "+itemCreation.getEnquiryType());
            if(!TextUtils.isEmpty(itemCreation.getTransactionType())) {
                holder.TransactionType.setText("Transaction Type :" + itemCreation.getTransactionType());
            }
            else
                holder.TransactionType.setVisibility(View.GONE);
            System.out.println("HHHHHHHHHHH"+itemCreation.getEnquiryType());
            if(itemCreation.getEnquiryType().equals("Group Enquiry"))
            {
                holder.TileRelative.setBackgroundColor(Color.parseColor("#c4ede2"));
                //holder.EnquiryType.setTextColor(0xFF00FF00);
            }
            if(itemCreation.getEnquiryType().equals("New Enquiry")){
                holder.TileRelative.setBackgroundColor(Color.TRANSPARENT);
            }
            if(itemCreation.getEnquiryType().equals("Advertisement Enquiry")){
                holder.TileRelative.setBackgroundColor(Color.parseColor("#cddcfe"));
            }

            if(itemCreation.getProfessionalRequirementID()!=0){
                holder.TransactionType.setVisibility(View.GONE);
            }
        }
//        if(itemCreation.getEnquiryType()!=null)
//        {
//            holder.EnquiryType.setText("EnquiryType : "+itemCreation.getEnquiryType());
//            System.out.println("HHHHHHHHHHH"+itemCreation.getEnquiryType());
//            if(itemCreation.getEnquiryType().equals("General Enquiry"))
//            {
//                holder.TileRelative.setBackgroundColor(Color.parseColor("0x9934B5E4"));
//                //holder.EnquiryType.setTextColor(0xFF00FF00);
//            }
//        }
        holder.itemView.setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4
                : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return dealerRegisters == null ? 0 : dealerRegisterList.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dealerRegisterList = dealerRegisters;
                } else {
                    ArrayList<OutboxDealer> filteredList = new ArrayList<>();
                    for (OutboxDealer row : dealerRegisters) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getChildCategoryName().toLowerCase().contains(charString.toLowerCase()) || row.getEnquiryType().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(row);
                        }
                    }
                    dealerRegisterList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = dealerRegisterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dealerRegisterList = (ArrayList<OutboxDealer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ChildName, ChatDateTime, BusinessDemandtxt, ChatCity, ChatCount, EnquiryDate,EnquiryType,TransactionType;
        RelativeLayout TileRelative;
        ImageView ProfileImg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            ChildName = (TextView) itemView.findViewById(R.id.ChildCategoryName);
            ChatDateTime = (TextView) itemView.findViewById(R.id.outbox_date_txt);
            BusinessDemandtxt = (TextView) itemView.findViewById(R.id.Business_Demand);
            ChatCity = (TextView) itemView.findViewById(R.id.chat_city);
            ChatCount = (TextView) itemView.findViewById(R.id.textNotifycount);
            EnquiryDate = (TextView) itemView.findViewById(R.id.enq_date);
            EnquiryType = (TextView) itemView.findViewById(R.id.enq_type);
            TransactionType = (TextView) itemView.findViewById(R.id.trans_type);
            TileRelative = (RelativeLayout) itemView.findViewById(R.id.tileRelative);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onItemSelected(dealerRegisterList.get(getAdapterPosition()));
                }
            });


        }
    }

    public interface ItemCreationAdapterListener {
        void onItemSelected(OutboxDealer itemCreation);
    }

    private String ConvertdateFormat(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date mydate = null;
        try {

            mydate = dateFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MMM");
        date = dateFormat1.format(mydate);

        return date;

    }

    private String Convertdate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date mydate = null;
        try {

            mydate = dateFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat1.format(mydate);

        return date;

    }

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    public void selectAll() {

        for (int i = 0; i < getItemCount(); i++)
            mSelectedItemsIds.put(i, true);
        notifyDataSetChanged();

    }


    public int getSelectedItemCount() {
        return mSelectedItemsIds.size();
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
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


    public List<OutboxDealer> getUpdatedList() {
        return dealerRegisterList;

    }
}
